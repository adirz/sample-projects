/*
 * MasterMindUtils.h
 *
 *  Created on: Dec 23, 2014
 *      Author: adirz
 */

#ifndef MASTERMINDUTILS_H_
#define MASTERMINDUTILS_H_

#include <iostream>
using namespace std;

#define DEFAULT_LEN 4
#define DEFAULT_CHAR 'd'

/**
 * As requested, on error returns 1, when finished properly return 0.
 */
#define EXIT_GOOD 0
#define EXIT_BAD 1

/**
 * messages to display to the user in case of bad inputs.
 */
const string ERR_MSG_ONE = "Too many parameters";
const string ERR_MSG_TWO = "Illegal parameters";

/**
 * When a program has run, we check it got valid parameters and set them.
 * @param len -		a pointer to set the length of the quarry.
 * @param max -		a pointer to set the biggest character in the quarry.
 * @param argc -	an indicator to the number of parameters given, including the game.
 * @param argv -	an array of strings, the parameters the game received.
 *
 * return true if the parameters are acceptable, false otherwise.
 */
bool parseStarter(int * len, char * max, int argc, char ** argv);

#endif /* MASTERMINDUTILS_H_ */
