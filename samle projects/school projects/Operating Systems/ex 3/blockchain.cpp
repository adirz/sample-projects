/*
 * blockchain.cpp
 *
 *  Created on: May 4, 2015
 *     Authors: adirz, katyac
 */

#include "blockchain.h"
//#include "Block.cpp" // todo include
#include "hash.h"
#include "MinHeap.cpp"
#include <map>
#include <string>
#include <iostream>
#include <mutex>
#include <vector>
#include <stdexcept>
#include <queue>

#define ERR_RETURN_VALUE -1
#define BLOCK_NOT_FOUND -2
#define NO_ERRORS 0
#define SUCCESS 1

using namespace std;

static Block * genesis; // the first block
static bool initialized = false; // initialize token
static int num_of_blocks; // number of blocks ever created
static vector<Block*> edges; // stores the available edge blocks - we can attach new blocks to them
static map<int, Block*> unattached_blocks; // stores map of unattached blocks by their numbers
static map<int, Block*> attached_blocks; // stores map of attached blocks by their numbers
static MinHeap freeMinNums;
static int maxBlockNum;
static pthread_t myDaemon;
static int lastAdded; // indicates the number of the last added block
static pthread_t closing; // thread of closing

static bool attach_now_block; // set to true if now attaching block
static bool closed = false; // set to true if now closing
static bool myErr; // set to true if there is an error in close_chain
static bool badThread; // set to true if there is an error while deliting the chain
static bool pruning; // set to true if pruning

static queue<int> pending_blocks; // queue of blocks to be attached.


// Mutexes
pthread_mutex_t block_num_mutex; // locks the heap of free numbers
pthread_mutex_t tree_mutex; // locks the tree of blocks - operations that affect the whole tree
pthread_mutex_t unnatached_mutex; // locks the map of unattached blocks
pthread_mutex_t init_mutex; // static mutex that locks the init
pthread_mutex_t closing_mutex; // locks the close function
pthread_mutex_t pending_mutex; // locks the pending queue


typedef struct
{
	int num;
	pthread_t * thread;
} int_N_thread;


// The deamon function
static void* run(void*);


int init_blockchain()
{
	init_mutex = PTHREAD_MUTEX_INITIALIZER;
	pthread_mutex_lock(&init_mutex);

	if (closed || initialized)
	{
		return ERR_RETURN_VALUE;
	}

	initialized = true;
	closed = false;
	pruning = false;
	num_of_blocks = 0;
	lastAdded = 0;
	maxBlockNum = 0;

	init_hash_generator();
	genesis = new Block();
	if(NULL == genesis)
	{
		return ERR_RETURN_VALUE;
	}
	edges.push_back(genesis);
	attached_blocks[0] = genesis;

	pthread_mutex_init(&unnatached_mutex, NULL);
	pthread_mutex_init(&block_num_mutex, NULL);
	pthread_mutex_init(&tree_mutex, NULL);
	pthread_mutex_init(&closing_mutex, NULL);
	pthread_mutex_init(&pending_mutex, NULL);

	if(pthread_create(&myDaemon, NULL, run, NULL))
	{
		pthread_mutex_destroy(&block_num_mutex);
		pthread_mutex_destroy(&tree_mutex);
		pthread_mutex_destroy(&unnatached_mutex);
		pthread_mutex_destroy(&pending_mutex);
		pthread_mutex_destroy(&closing_mutex);
		delete genesis;
		//
		return ERR_RETURN_VALUE;
	}
	if (pthread_detach(myDaemon))
	{
		pthread_mutex_destroy(&block_num_mutex);
		pthread_mutex_destroy(&tree_mutex);
		pthread_mutex_destroy(&unnatached_mutex);
		pthread_mutex_destroy(&pending_mutex);
		pthread_mutex_destroy(&closing_mutex);
		delete genesis;

		return ERR_RETURN_VALUE;
	}

	return NO_ERRORS;
}

// Returns random block leaf of one of the longest chains
Block * getRandLongest() // tOdo static
{
	return edges[random()%edges.size()];
}


int add_block(char *data , int length)
{
	if(!initialized || closed)
	{
		return ERR_RETURN_VALUE;

	}

	Block * father = getRandLongest();
	int block_num;

	// chose the block number
	pthread_mutex_lock(&block_num_mutex);
	if(freeMinNums.isEmpty())
	{
		maxBlockNum ++;
		block_num = maxBlockNum;
	}
	else
	{
		block_num = freeMinNums.Pop();
	}
	pthread_mutex_unlock(&block_num_mutex);

	// initialize new block
	Block * newBlock = new Block(data, block_num, father, length);
	if(NULL == newBlock)
	{
		return ERR_RETURN_VALUE;
	}

	pthread_mutex_lock(&unnatached_mutex);
	unattached_blocks[block_num] = newBlock;
	pthread_mutex_unlock(&unnatached_mutex);

	pthread_mutex_lock(&pending_mutex);
	pending_blocks.push(block_num);
	pthread_mutex_unlock(&pending_mutex);

	return block_num;
}


int to_longest(int block_num)
{

	if(!initialized)
	{
		return ERR_RETURN_VALUE;
	}
	if(unattached_blocks.find(block_num) == unattached_blocks.end())
	{
		return BLOCK_NOT_FOUND;
	}

	unattached_blocks[block_num]->toLongest();
	return NO_ERRORS;

}

// Retures the hash of given block's data
static char * hash_this(Block * to_hash)
{
	int nonce = generate_nonce(to_hash->getNum(), to_hash->getFather()->getNum());
	return generate_hash(to_hash->getData(), to_hash->getDataLength(), nonce);
}


// Removes the block with the given number from the queue of pending blocks
// Called from attach_now_block
static void remove_block_from_pending(int block_num)
{

	if (pending_blocks.empty())
	{
		return;
	}
	int first = pending_blocks.front();

	if (block_num == first)
	{
		pending_blocks.pop();

		return;
	}

	while(pending_blocks.front() != block_num)
	{
		pending_blocks.push(pending_blocks.front());
		pending_blocks.pop();
	}

	pending_blocks.pop();

	if (!pending_blocks.empty())
	{
		while(pending_blocks.front() != first)
		{
			pending_blocks.push(pending_blocks.front());
			pending_blocks.pop();
		}
	}
}


static int do_attach_now(int block_num)
{
	if(closed)
	{
		return ERR_RETURN_VALUE;
	}

	pthread_mutex_lock(&pending_mutex);
	remove_block_from_pending(block_num);
	pthread_mutex_unlock(&pending_mutex);

	Block * this_block = unattached_blocks[block_num];

	// if toLongest flag set on true or if the father was erased, find new father
	if(this_block->getToLongest() ||this_block->getFather() == NULL)
	{
		Block * father = getRandLongest();
		this_block->setFather(father);
	}
	char * hashed = hash_this(this_block);
	this_block->setHashedData(hashed);
	attached_blocks[block_num] = this_block;
	unattached_blocks.erase(block_num);


	// add this block to the edges
	Block * rand_edge = getRandLongest();
	int height = rand_edge->getHeight();
	if (height < this_block->getHeight())
	{
		edges.clear();
		edges.push_back(this_block);
	}
	else if(height == this_block->getHeight())
	{
		edges.push_back(this_block);
	}

	num_of_blocks++;


	return NO_ERRORS;
}




int attach_now(int block_num)
{

	if (!initialized)
	{
		return ERR_RETURN_VALUE;
	}

	if(attached_blocks.find(block_num) != attached_blocks.end())
	{
		return NO_ERRORS;
	}
	if(unattached_blocks.find(block_num) == unattached_blocks.end())
	{
		return BLOCK_NOT_FOUND;
	}
	attach_now_block = true;

	pthread_mutex_lock(&closing_mutex);
	pthread_mutex_lock(&tree_mutex);
	int n = do_attach_now(block_num);
	pthread_mutex_unlock(&tree_mutex);
	pthread_mutex_unlock(&closing_mutex);

	attach_now_block = false;
	return n;
}

int was_added(int block_num)
{

	if (!initialized)
	{
		return ERR_RETURN_VALUE;
	}
	if ((attached_blocks.find(block_num) != attached_blocks.end()))
	{
		return SUCCESS;
	}
	if ((unattached_blocks.find(block_num) != unattached_blocks.end()))
	{
		return NO_ERRORS;
	}

	return BLOCK_NOT_FOUND;
}

int chain_size()
{
	if(!initialized)
	{
		return ERR_RETURN_VALUE;
	}

	return num_of_blocks;
}

static void setLongest(bool to_set, Block * longest_chain)
{
	while(longest_chain->getNum() > 0)
	{
		longest_chain->setInLongest(to_set);
		longest_chain = longest_chain->getFather();
	}
}

int prune_chain()
{
	if (pruning)
	{
		return ERR_RETURN_VALUE;
	}

	pthread_mutex_lock(&closing_mutex);
	pthread_mutex_lock(&tree_mutex);


	pruning = true;

	if(!initialized)
	{
		return ERR_RETURN_VALUE;
	}
	if(closed)
	{
		return ERR_RETURN_VALUE;
	}


	Block * longest_chain = getRandLongest();

	edges.clear();
	edges.push_back(longest_chain);



	// set the whole chain of the chosen block to be the longest chain
	setLongest(true, longest_chain);


	// erase all the nodes that are not in the longest chain

	for (map<int, Block*>::iterator iter = unattached_blocks.begin(); iter != unattached_blocks.end(); ++iter) {
		if (!(iter->second)->getFather()->getInLongest())
		{
			(iter->second)->setFather(NULL);
		}
	}

	// erase all the nodes that are not in the longest chain

	static queue<int> to_delete;

	static map<int, Block*>::iterator iter2;

	for (iter2 = attached_blocks.begin(); iter2 != attached_blocks.end(); ++iter2) {

		if (!(iter2->second)->getInLongest())
		{
			freeMinNums.Push(iter2->second->getNum());

			to_delete.push(iter2->first);
			delete iter2->second;

		}
	}

	while (!to_delete.empty())
	{
		attached_blocks.erase(to_delete.front());
		to_delete.pop();
	}


	longest_chain = edges[0];
	setLongest(false, longest_chain);

	pruning = false;

	pthread_mutex_unlock(&tree_mutex);
	pthread_mutex_unlock(&closing_mutex);

	return NO_ERRORS;
}

static void * delete_chain(void* args)
{
	/**
	 * TODO if system error
	 * *badThread = false;
	 * pthread_exit(badThread);
	 */

	// hash pending blocks
	while (!pending_blocks.empty()) {

		int num = pending_blocks.front();
		char * hash = hash_this(unattached_blocks[num]);
		free(hash);
		pending_blocks.pop();
	}


	while (!freeMinNums.isEmpty())
	{
		freeMinNums.Pop();
	}

	// erase all the nodes

	for (map<int, Block*>::iterator iter = attached_blocks.begin(); iter != attached_blocks.end();
			++iter) {
		delete iter->second;
	}
	for (map<int, Block*>::iterator iter = unattached_blocks.begin(); iter != unattached_blocks.end();
			++iter) {
		delete iter->second;
	}

	unattached_blocks.clear();
	attached_blocks.clear();
	edges.clear();


	pthread_mutex_unlock(&tree_mutex);
	pthread_mutex_unlock(&pending_mutex);
	pthread_mutex_unlock(&unnatached_mutex);

	// close mutexes
	pthread_mutex_destroy(&block_num_mutex);
	pthread_mutex_destroy(&tree_mutex);
	pthread_mutex_destroy(&unnatached_mutex);
	pthread_mutex_destroy(&pending_mutex);

	close_hash_generator();

	pthread_mutex_unlock(&closing_mutex);

	badThread = false;
	pthread_exit(&badThread);

	return NULL;
}


void close_chain()
{

	pthread_mutex_lock(&closing_mutex);
	pthread_mutex_lock(&tree_mutex);
	pthread_mutex_lock(&pending_mutex);
	pthread_mutex_lock(&unnatached_mutex);

	if (!initialized)
	{
		myErr = true;
		return;
	}
	closed = true;

	if(!myErr)
	{
		if(pthread_create(&closing, NULL, delete_chain, NULL))
		{
			myErr = true;
		}
	}

	pthread_mutex_destroy(&closing_mutex);
	pthread_mutex_unlock(&init_mutex);


}


int return_on_close()
{

	if(myErr ||!initialized)
	{
		return ERR_RETURN_VALUE;
	}
	if(!closed)
	{
		return BLOCK_NOT_FOUND;
	}
	void * toExit;

	pthread_join(closing, &toExit);

	closed = false;
	initialized = false;
	if(*((bool*)(toExit)))
	{
		return ERR_RETURN_VALUE;
	}
	return SUCCESS;
}


// The daemon function
static void * run(void* args)
{
	while(!closed)
	{
		if(pending_blocks.empty() || attach_now_block)
		{
			/**
			 * we tried making it not busy waiting by:
			 * but before it reached the above "wait" it signaled it
			 * so we got stuck
			 */
			//pthread_cond_wait(&cond, &a);
		}
		else{
			int curr_num = pending_blocks.front();
			if (unattached_blocks.find(curr_num) != unattached_blocks.end() && !closed)
			{
				pthread_mutex_lock(&tree_mutex);
				do_attach_now(curr_num);
				pthread_mutex_unlock(&tree_mutex);
			}
		}

	}
	pthread_exit(0);
	return NULL;
}
