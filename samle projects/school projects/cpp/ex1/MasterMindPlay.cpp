/*
 * MasterMindPlay.cpp
 *
 *  Created on: Dec 16, 2014
 *      Author: Adir
 *
 *	A game of Master mind. it is run by calling
 *	"mastermindplay <length of code> <biggest letter possible>"
 *	then, the computer generate a code which you need to guess- enjoy!
 */

#include "Code.cpp"
#include "MasterMindUtils.h"
using namespace std;

/**
 * The messages needed to interact with the player
 */
const string MSG_ONE_A = "Please enter a guess of ";
const string MSG_ONE_B = " letters between \'a\' and \'";
const string MSG_ONE_C = "\':\n";
const string MSG_TWO_A = "You got ";
const string MSG_TWO_B = " bulls and \'";
const string MSG_TWO_C = " cows.\n";
const string MSG_THREE_A = "It took you ";
const string MSG_THREE_B = " guesses.\n";


int main(int argc, char ** argv)
{
	int L;
	char C;
	if(!parseStarter(&L, &C, argc, argv))
	{
		return EXIT_BAD;
	}
	Code code = Code(L, C, false);
	string guess;
	int hits[2] = {0, 0}; //index 0 is bulls index 1 is cows
	int guesses = 0;
	while(hits[0] < code.len)
	{
		guesses ++ ;
		cout << MSG_ONE_A << code.len << MSG_ONE_B << code.max << MSG_ONE_C;
		cin >> guess;
		code.checkCode(guess, hits);
		cout << MSG_TWO_A << hits[0] << MSG_TWO_B << hits[1] << MSG_TWO_C;
	}
	cout << MSG_THREE_A << guesses << MSG_THREE_B;
	return EXIT_GOOD;
}
