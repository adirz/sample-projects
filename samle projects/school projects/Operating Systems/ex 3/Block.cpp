/*
 * Block.cpp
 *
 * Represents single block object.
 *
 *  Created on: May 4, 2015
 *     Authors: adirz, katyac
 */


#include <cstdlib>
#include <iostream>
#include <string.h>

#define HASH_BITS 128

using namespace std;

class Block
{
	int height, block_num, data_len;
	bool to_longest; // true if t_longest was called on block
	Block * _father;
	char * _data;
	bool in_longest; // true if the block is in the longest chain
	char * hashed_data;
public:
	Block():
		height(0), block_num(0),data_len(0)
	{
		_father = NULL;
		_data = NULL;
		to_longest = false;
		in_longest = true;
		hashed_data = NULL;

	}
	Block(char * data, int blockNum, Block * father, int dataLen):
		height(father->getHeight()+1), block_num(blockNum), data_len(dataLen), _data(data)
	{
		_data = new char[data_len];
		strcpy(_data, data);
		_father = father;
		to_longest = false;
		in_longest = false;
		hashed_data = NULL;
	}

	char * getData()
	{
		return _data;
	}



	void setInLongest(bool toSet)
	{
		in_longest = toSet;
	}

	bool getInLongest()
	{
		return in_longest;
	}

	int getNum()
	{
		return block_num;
	}

	int getHeight()
	{
		return height;
	}

	Block * getFather()
	{
		return _father;
	}

	void setFather(Block *father)
	{
		_father = father;
		if (_father != NULL)
		{
			height = father->getHeight() +1;
		}
		else
		{
			height = -1;
		}
	}

	void toLongest()
	{

		to_longest = true;
	}

	bool getToLongest()
	{
		return to_longest;
	}

	int getDataLength()
	{
		return data_len;
	}

	bool isAttached()
	{
		if (hashed_data == NULL)
		{
			return false;
		}

		return true;
	}

	void setHashedData(char * hashed)
	{
		hashed_data = hashed;
	}

	~Block()
	{
		delete[] _data;
		free(hashed_data);

	}
};
