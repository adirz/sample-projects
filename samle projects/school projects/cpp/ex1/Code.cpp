/*
 * MasterMindCode.cpp
 *
 *  Created on: Dec 19, 2014
 *      Author: Adir
 */

#include <string>
//#include "erand.h"
using namespace std;

#include <cstdlib>
#include <ctime>

#define UNMATCHED '0'
#define MIN_CHAR 'a'
#define EXIT_GOOD 0
#define EXIT_BAD 1

/**
 * class Code is a representation of the code needed to be guessed in the game.
 * contains constructors and evaluation method
 */
class Code
{
public:
	Code(Code * copy)
	{
		len = copy->len;
		max = copy->max;
		code = string(copy->code);
	}

	Code(int L, char C, bool fixed):
			len(L), max(C)
	{
		char * codeSeq = (char *)(malloc(L * sizeof(char)));
		for(int i = 0; i < L; i ++)
		{
			if(fixed)
			{
				codeSeq[i] = MIN_CHAR;
			}
			else
			{
	//			codeSeq[i] = randomChar(C);
				codeSeq[i] = rand()%('z' - C) + 'a';
			}
		}
		code = string(codeSeq, (size_t)len);
		free(codeSeq);
	}

	Code(string S, char C) :
		len(S.length()), max(C)
	{
		code = S;
	}
	void checkCode(string guess, int * hits)
	{
		{
			hits[0] = 0;
			hits[1] = 0;
			int * leftovers = (int *)(malloc( (max - MIN_CHAR) * sizeof(int)));
			for(int i =0 ; i < (max - MIN_CHAR) ; i ++)
			{
				leftovers[i] = 0;
			}
			for(int i =0 ; i < len ; i ++)
			{
				if(guess[i] == code[i])
				{
					guess[i] = UNMATCHED;
					hits[0] ++;
				}
				else
				{
					leftovers[code[i] - MIN_CHAR] ++;
					if(0 < leftovers[guess[i] - MIN_CHAR])
					{
						leftovers[guess[i] - MIN_CHAR] --;
						hits[1] ++;
					}
				}
			}
			free(leftovers);
		}
	}

//	~Code();
	int len;
	char max;
	string code;
};
