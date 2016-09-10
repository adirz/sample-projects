/*
 * MasterMindUtils.cpp
 *
 *  Created on: Dec 23, 2014
 *      Author: adirz
 */

#include "MasterMindUtils.h"

/**
 * When a program has run, we check it got valid parameters and set them.
 * @param len -		a pointer to set the length of the quarry.
 * @param max -		a pointer to set the biggest character in the quarry.
 * @param argc -	an indicator to the number of parameters given, including the game.
 * @param argv -	an array of strings, the parameters the game received.
 *
 * return true if the parameters are acceptable, false otherwise.
 */
bool parseStarter(int * len, char * max, int argc, char ** argv)
{
	(*len) = DEFAULT_LEN;
	*max = DEFAULT_CHAR;
	if(argc > 3)
	{
		cout << ERR_MSG_ONE;
		return false;
	}
	if(argc == 3)
	{
		if(argv[2][0] < 'a' || argv[2][0] > 'z' || '\0' != argv[2][1])
		{
			cout << ERR_MSG_TWO;
			return false;
		}
		*max = argv[2][0];

		len = 0;
		for(int i = 0 ; argv[1][i] != '\0'; i ++)
		{
			if(argv[1][i] >= '0' && argv[1][i] <= '9')
			{
				*len = 10 * (*len);
				len += argv[1][i] - '0';
			}
			else
			{
				cout << ERR_MSG_TWO;
				return false;
			}
		}
	}
	else
	{
		if(argv[1][0] >= 'a' && argv[1][0] <= 'z')
		{
			if('\0' != argv[1][1])
			{
				cout << ERR_MSG_TWO;
				return false;
			}
			(*max) = argv[1][0];
		}
		else
		{
			len = 0;
			for(int i = 0 ; argv[1][i] != '\0'; i ++)
			{
				if(argv[1][i] >= '0' && argv[1][i] <= '9')
				{
					(*len) = 10 * (*len);
					*len += argv[1][i] - '0';
				}
				else
				{
					cout << ERR_MSG_TWO;
					return false;
				}
			}
		}
	}
	return true;
}
