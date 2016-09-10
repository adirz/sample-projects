/*
 * Cache.cpp
 *
 *  Created on: May 22, 2015
 *     Authors: adirz, katyac
 */
#include <cstdlib>
#include <string>
#include <cstring>
#include <cstdio>
#include <stdio.h>
#include <stdlib.h>
#include <fstream>
#include <iostream>
#include <assert.h>


#define MAX_CHARS 50

using namespace std;

/**
 * Cache structure
 */
struct Cache {

	int _numOfBlocks;
	int _curr_size;
	int _block_size;

	/**
	 * represent the blocks of memory
	 */
	struct Block{
		int _num_of_access;
		const int _block_num;
		const char* _content;
		char* _path;

		Block(int block_num, char * content, char * path):
			_block_num(block_num), _content(content), _path(path) {
			_num_of_access = 0;
		}

		void read_from_block(char *buf, size_t size, off_t offset) {
			++_num_of_access;
			strncpy(buf, &_content[offset], size);
		}

		~Block() {
			delete[] _content;
			delete[] _path;
		}
	};

	Block** cache;

	Cache (int numOfBlocks, int block_size) {

		cache = new Block*[numOfBlocks];
		_block_size = block_size;
		_numOfBlocks = numOfBlocks;
		_curr_size = 0;
	}

	/**
	 * allocates memory to output and saves the printed blockes into it
	 */
	void print_blocks(ofstream * output)
	{
		for(int i = 0; i < _curr_size; i++){
			if(NULL != cache[i])
			{
				int x = cache[i]->_num_of_access;
				if(0 != x)
				{
					(*output)<<cache[i]->_path<<" "<<cache[i]->_block_num<<" "<<x<<endl;
				}
			}
		}
	}

	/**
	 * finds in cache the block we used the least and delete it
	 */
	int find_and_delete() {
		int min_appearence = cache[0]->_num_of_access;
		int ret_value = 0;
		for (int i = 0; i < _numOfBlocks; ++ i) {
			if(min_appearence >= cache[i]->_num_of_access) {
				min_appearence = cache[i]->_num_of_access;
				ret_value = i;
			}
		}
		delete cache[ret_value];
		return ret_value;
	}

	/**
	 * find a block in the cache according to it file and place in file
	 */
	Block * find_block(int block_num, string path) {

		for (int i = 0; i < _curr_size; ++ i) {
			if (cache[i]->_path == path) {
				if (cache[i]->_block_num == block_num) {
					return cache[i];
				}
			}
		}

		return NULL;
	}

	/**
	 * adds a block to the cache
	 */
	void add_block(Block* new_block) {

		if (_curr_size == _numOfBlocks){
			cache[find_and_delete()] = new_block;
		}
		cache[_curr_size] = new_block;
		++ _curr_size;
	}

	/**
	 * when we rename a file we check any file with its reference in cache and change its name
	 */
	void rename_in_cache(const char * old_path, const char * new_path) {
		for (int i = 0; i < _curr_size; ++i) {
			if (strncmp(cache[i]->_path, old_path, strlen(old_path)) == 0) {
				char * tmp = new char[strlen(cache[i]->_path)];
				strcpy(tmp, &((cache[i]->_path)[strlen(old_path)]));
				strcpy(cache[i]->_path, new_path);
				strcat(cache[i]->_path, tmp);
				delete tmp;
			}
		}
	}

	~Cache() {
		for (int i = 0; i < _curr_size; ++i) {
			delete cache[i];
		}
		delete[] cache;
	}
};
