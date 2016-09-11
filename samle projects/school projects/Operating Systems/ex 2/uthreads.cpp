/*
 * uthreads.cpp
 *
 *  Created on: Apr 6, 2015
 *      Author: adirz katyac
 */


#include "uthreads.h"
#include <setjmp.h>
#include <queue>
#include <unordered_map>
#include <unistd.h>
#include <signal.h>
#include <stdio.h>
#include <sys/time.h>
#include <stdlib.h>
#include<fstream>
#include <iostream>

using namespace std;

#ifdef __x86_64__
/* code for 64 bit Intel arch */

typedef unsigned long address_t;
#define JB_SP 6
#define JB_PC 7

/* A translation is required when using an address of a variable.
   Use this as a black box in your code. */
address_t translate_address(address_t addr)
{
	address_t ret;
	asm volatile("xor    %%fs:0x30,%0\n"
			"rol    $0x11,%0\n"
			: "=g" (ret)
			  : "0" (addr));
	return ret;
}

#else
/* code for 32 bit Intel arch */

typedef unsigned int address_t;
#define JB_SP 4
#define JB_PC 5

/* A translation is required when using an address of a variable.
   Use this as a black box in your code. */
address_t translate_address(address_t addr)
{
	address_t ret;
	asm volatile("xor    %%gs:0x18,%0\n"
			"rol    $0x9,%0\n"
			: "=g" (ret)
			  : "0" (addr));
	return ret;
}

#endif

/**
 * a structure to represent each individual thread
 */
typedef struct individualThread{
	char * stack;
	Priority pr;
	int id;
	sigjmp_buf env;
	int quantums;
}individualThread;

/**
 * Our ready queue. Contains queues for all priorities, and push and pop functions.
 */
typedef struct readyQueue{
	queue<individualThread*> red;
	queue<individualThread*> orange;
	queue<individualThread*> green;

	/**
	 * pushes the pointer toPush to the end of a queue according to the thread's priority
	 */
	void push(individualThread * toPush) {
		if (toPush->pr == RED)
		{
			red.push(toPush);
		}
		else if (toPush->pr == ORANGE)
		{
			orange.push(toPush);
		}
		else
		{
			green.push(toPush);
		}
	}

	/**
	 * pops the first thread in the queue, from the highest priority first
	 */
	individualThread* pop ()
	{
		individualThread * temp = NULL;
		if (!red.empty())
		{
			temp = red.front();
			red.pop();
		}
		else if (!orange.empty())
		{
			temp = orange.front();
			orange.pop();
		}
		else if (!green.empty())
		{
			temp = green.front();
			green.pop();
		}
		return temp;
	}

}readyQueue;

static int curId = 0; 					// the id of the running thread
static readyQueue rQueue;				// the queue of the threads in "ready" state
static individualThread * running;		// the thread which currently now run
//static queue<individualThread*> blocked;//
static unordered_map<int, individualThread*> blocked;
static int totalQuantums = 1;			// total number of processes times switched
static struct itimerval tv;				// the time each process gets before switching
static sigset_t signal_set;				// the signal of the alarm to switch threads
struct sigaction sigRun;				// listening to the signal to switch threads
static queue<int> availableId;			// a queue of available Ids to assign to new threads
static unsigned int thread_counter = 0;	// number of threads

#define ERROR -1
#define OK 0
#define MICRO_TO_SECOND 1000000

/**
 * a function used to free all of the space we took in the memory while using threads
 */
static void deleteAll()
{
	individualThread* deleter;
	unordered_map<int, individualThread*>:: const_iterator iter = blocked.begin();
	for(;iter != blocked.end();iter ++)
	{
		deleter = iter->second;
		delete[] deleter->stack;
		delete deleter;
	}

	while(NULL != (deleter = rQueue.pop()))
	{
		delete[] deleter->stack;
		delete deleter;
	}
	delete[] running->stack;
	delete running;
}

/**
 * prints an error message of the library
 *
 * @param message - the message we want to print
 *
 * return ERROR value
 */
static int lib_err(string message)
{
	cerr<<"thread library error: "<<message<<"\n";
	return ERROR;
}

/**
 * prints an error message of the system and releases all of the memory
 *
 * @param message - the message we want to print
 *
 * exit with error
 */
static void sys_err(string message)
{
	cerr<<"system error: "<<message<<"\n";
	deleteAll();
	exit(-1);
}

/**
 * according to sig, block or resume the signal of our alarm
 *
 * @param sig- 0 to block, 1 to resume
 *
 * prints error if occurred
 */
static void blockHandle(int sig)
{
	if (sigprocmask(sig, &signal_set, NULL))
	{
		sys_err("Error while handling signals.");
	}
}

/**
 * start a new thread. give it the time of tv.
 */
static void jump()
{

	running->quantums ++;
	totalQuantums ++;
	if (setitimer(ITIMER_VIRTUAL, &tv, NULL))
	{
		sys_err("Error in timer.");
	}

	siglongjmp(running->env, 1);

}

/**
 * when alarm signal occurs, switch the running thread to the next in the ready queue
 */
static void switchThreads(int sig)
{
	blockHandle(SIG_BLOCK);
	int replace = sigsetjmp(running->env, 1);
	if(!replace)
	{
		individualThread * temp = running;
		running = rQueue.pop();
		if(NULL == running)
		{
			running = temp;
		}
		else
		{
			rQueue.push(temp);
		}
		blockHandle(SIG_UNBLOCK);
		jump();
	}
}


int uthread_init(int quantum_usecs)
{
	if (quantum_usecs <= 0)
	{
		return lib_err("Invalid input to init");
	}
	sigRun.sa_handler = switchThreads;

	uthread_spawn(NULL, ORANGE);
	running = rQueue.pop();

	running->quantums ++;

	if (sigaction(SIGVTALRM, &sigRun, NULL) == ERROR || sigemptyset(&signal_set) == ERROR ||
			sigaddset(&signal_set, SIGVTALRM) == ERROR)
	{
		sys_err("Error while initializing the signal set");
	}

	tv.it_value.tv_sec = (int)(quantum_usecs/MICRO_TO_SECOND);
	tv.it_interval.tv_sec = (int)(quantum_usecs/MICRO_TO_SECOND);
	tv.it_value.tv_usec = quantum_usecs % MICRO_TO_SECOND;
	tv.it_interval.tv_usec = quantum_usecs % MICRO_TO_SECOND;

	if (setitimer(ITIMER_VIRTUAL, &tv, NULL) == ERROR)
	{
		sys_err("Error while setting the timer");
	}

	return OK;
}


int uthread_spawn(void (*f)(void), Priority pr)
{
	blockHandle(SIG_BLOCK);
	if (thread_counter == MAX_THREAD_NUM)
	{
		return lib_err("Number of threads exceeds the maximal number of threads allowed.");
	}

	thread_counter ++;

	individualThread * thread = new individualThread;
	if(NULL == thread)
	{
		delete thread;
		sys_err("Failed to allocate memory");
	}
	thread->stack = new char[STACK_SIZE];

	if (thread->stack == NULL)
	{
		delete thread->stack;
		delete thread;
		sys_err("Failed to allocate memory");
	}

	thread->pr = pr;
	if (!availableId.empty())
	{
		thread->id = availableId.front();
		availableId.pop();
	}
	else
	{
		thread->id = curId ++;
	}
	thread->quantums = 0;
	unsigned long sp, pc;


	sp = (unsigned long)(thread->stack) + STACK_SIZE - sizeof(unsigned long);
	pc = (unsigned long)f;
	sigsetjmp(thread->env, 1);
	(thread->env->__jmpbuf)[JB_SP] = translate_address(sp);
	(thread->env->__jmpbuf)[JB_PC] = translate_address(pc);
	if (sigemptyset(&(thread->env->__saved_mask)) == ERROR)
	{
		sys_err("Error while initializing the signal set");
	}

	rQueue.push(thread);
	blockHandle(SIG_UNBLOCK);
	return thread->id;
}

/**
 * searches our a queue for a thread with the ID of tid and remove it if necessary
 *
 * @param que - a pointer to a queue we search in
 * @param tid - the id of the thread we search
 * @param remove - do we want to remove the thread from the queue or not
 *
 * return a pointer to the thread we found or NULL if we didn't
 *
 */
static individualThread * searchQueue(queue<individualThread*> * que, int tid, bool remove = true){
	individualThread * toReturn = NULL;
	queue<individualThread*> second;
	individualThread * temp;

	while(!que->empty())
	{
		temp = que->front();
		que->pop();
		if(temp->id == tid)
		{
			toReturn = temp;
			if(!remove)
			{
				second.push(temp);
			}
		}
		else
		{
			second.push(temp);
		}
	}

	int count  = 0;
	while(!second.empty())
	{
		temp = second.front();
		second.pop();
		que->push(temp);
		count ++;
	}
	return toReturn;
}

/**
 * searches our ready queue for a thread with the ID of tid and remove it if necessary
 *
 * @param tid - the id of the thread we search
 * @param remove - do we want to remove the thread from the queue or not
 *
 * return a pointer to the thread we found or NULL if we didn't
 *
 */
individualThread * searchReadyQueue(int tid, bool remove = true){
	individualThread * found;

	found = searchQueue(&rQueue.red, tid, remove);

	if(found == NULL)
	{
		found = searchQueue(&rQueue.orange, tid, remove);
	}
	if(found == NULL)
	{
		found = searchQueue(&rQueue.green, tid, remove);
	}
	return found;
}


int uthread_terminate(int tid)
{


	blockHandle(SIG_BLOCK);
	if(tid == 0)
	{
		deleteAll();
		exit(0);
	}

	individualThread* toRemove;
	if(running->id == tid)
	{
		availableId.push(tid);

		delete[] running->stack;
		delete running;



		running = rQueue.pop();
		thread_counter --;

		if(running)
		{
			jump();
		}
	}

	unordered_map<int, individualThread*>:: const_iterator iter = blocked.find(tid);
	if(iter != blocked.end()){
		toRemove = iter->second;
		blocked.erase(tid);
		availableId.push(tid);
		delete[] toRemove->stack;
		delete toRemove;
		thread_counter --;
		blockHandle(SIG_UNBLOCK);
		return OK;
	}


	toRemove = searchReadyQueue(tid);
	if(NULL != toRemove){
		delete[] toRemove->stack;
		delete toRemove;
		thread_counter --;
		blockHandle(SIG_UNBLOCK);
		return OK;
	}
	blockHandle(SIG_UNBLOCK);
	return lib_err("Trying to terminate non-existing thread");
}

int uthread_suspend(int tid)
{
	blockHandle(SIG_BLOCK);
	if(tid == 0)
	{
		blockHandle(SIG_UNBLOCK);
		return lib_err("Trying to suspend the main");
	}
	individualThread* toSuspend;
	if(running->id == tid)
	{
		blocked[running->id] = running;
		blockHandle(SIG_UNBLOCK);
		int replace = sigsetjmp(running->env, 1);
		blockHandle(SIG_BLOCK);
		if(!replace)
		{
			running = rQueue.pop();
			if(running)
			{
				blockHandle(SIG_UNBLOCK);
				jump();
			}
		}
		else
		{
			blockHandle(SIG_UNBLOCK);
			return OK;
		}
	}
	toSuspend = searchReadyQueue(tid);
	if(NULL != toSuspend)
	{
		blocked[toSuspend->id] = toSuspend;
//		blocked.push(toSuspend);
		blockHandle(SIG_UNBLOCK);
		return OK;
	}
	unordered_map<int, individualThread*>:: const_iterator iter = blocked.find(tid);
	if(iter != blocked.end()){
		toSuspend = iter->second;
	}
//	toSuspend = searchQueue(&blocked, tid, false);
	if(toSuspend == NULL)
	{
		blockHandle(SIG_UNBLOCK);
		return lib_err("Trying to suspend non-existing thread");
	}
	blockHandle(SIG_UNBLOCK);
	return OK;
}

int uthread_resume(int tid)
{
	blockHandle(SIG_BLOCK);
	individualThread* toResume;
	unordered_map<int, individualThread*>:: const_iterator iter = blocked.find(tid);
	if(iter != blocked.end()){
		toResume = iter->second;
		blocked.erase(tid);
		rQueue.push(toResume);
		blockHandle(SIG_UNBLOCK);
		return OK;
	}
	toResume = searchReadyQueue(tid, false);
	if(toResume == NULL && running->id != tid)
	{
		blockHandle(SIG_UNBLOCK);
		return lib_err("Trying to resume non-existing thread");
	}
	blockHandle(SIG_UNBLOCK);
	return OK;
}

int uthread_get_total_quantums()
{
	return totalQuantums;
}

int uthread_get_quantums(int tid)
{
	individualThread* toReturn;
	if (tid == running->id)
	{
		return running->quantums;
	}
	unordered_map<int, individualThread*>::const_iterator iter = blocked.find(tid);
	if(iter != blocked.end()){
		toReturn = iter->second;
		return toReturn->quantums;
	}

	toReturn = searchReadyQueue(tid, false);
	if (toReturn != NULL)
	{
		return toReturn->quantums;
	}
	return lib_err("Thread library error: asking for quantums of non-existing thread");
}

int uthread_get_tid(){
	return running->id;
}



