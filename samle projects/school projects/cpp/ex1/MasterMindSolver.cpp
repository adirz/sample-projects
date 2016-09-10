///*
// * MasterMindSolver.cpp
// *
// *  Created on: Dec 21, 2014
// *      Author: adirz
// *
// *	A game of Master mind. it is run by calling
// *	"mastermindsolver <length of code> <biggest letter possible>"
// *	then, the computer generate codes which are guesses to your code- enjoy!
// */
//
//#include <algorithm>
//#include <string>
//#include "MasterMindUtils.h"
//#include "Restraints.cpp"
//using namespace std;
//
///**
// * The messages needed to interact with the player
// */
//const string MSG_ONE_A = "Please choose ";
//const string MSG_ONE_B = " letters in the range \'a\' to \'";
//const string MSG_ONE_C = "\':\n";
//const string MSG_TWO_A = "My guess is ";
//const string MSG_TWO_B = "\n";
//
///**
// * deciphers the answer given by the user to our guess, check its legality and save them.
// * @param answer - 	a string of what the user replied
// * @param hits - 	an array of size 2 that we change, holds the bulls and cows of the answer.
// *
// * return true if the user is allowed this answer, false otherwise
// */
//bool interpertAnswer(string answer, int * hits)
//{
//	int i = 0;
//	for(; i < (int)(answer.length()) && answer[i] != ' ' ; i ++)
//	{
//		if(answer[i] < '0' || answer[i] > '9')
//		{
//			return false;
//		}
//		hits[0] = hits[0] * 10 + answer[i] - '0' ;
//	}
//	for(; i < (int)(answer.length()) ; i ++)
//	{
//		if(answer[i] < '0' || answer[i] > '9')
//		{
//			return false;
//		}
//		hits[1] = hits[1] * 10 + answer[i] - '0' ;
//	}
//	return true;
//}
//
///**
// * given the restraints so far, we are generating a next possible guess
// * it is done as follows:
// * we start with all 'a' guess.
// * replace is how much is not a bull or a cow, so they need to be replaced.
// * the last ones are being replaced by the next letter and wee advance it.
// * if we look at this as the answer, all previous quarries need to give the same answer,
// * so we make permutations until they do.
// *
// * @param replace -		the number of characters that are missing from the answer
// * @param code -		a pointer to our guess, to be changed
// * @param letter - 		our next letter we add and check.
// * @param restraints - 	the previous quarries.
// *
// * return true if there is a legal next guess, false otherwise.
// */
//bool nextGuess(int replace, Code * code, char letter, Restraints restraints)
//{
//	if(letter > code->max)
//	{
//		return false;
//	}
//	int j = ((*code).code).length() - 1;
//	for(int i = 0; i < replace ; j --)
//	{
//		if(((*code).code)[j] == letter -1)
//		{
//			((*code).code)[j] = letter;
//			i ++;
//		}
//	}
//	bool legal = true;
//	while(!restraints.matchRestraints(code))
//	{
//		legal = next_permutation((code->code).begin(), (code->code).end());
//	}
//	return legal;
//}
//
//int main(int argc, char ** argv)
//{
//	int L;
//	char C;
//	if(!parseStarter(&L, &C, argc, argv))
//	{
//		return EXIT_BAD;
//	}
//	Code guess = Code(L, C, true);
//	cout << MSG_ONE_A << guess.len << MSG_ONE_B << guess.max << MSG_ONE_C;
//
//	Restraints restraints = Restraints(guess.len, guess.max);
//
//	int hits[2] = {0, 0}; //index 0 is bulls index 1 is cows
//	int guesses = 0;
//	string answer;
//	char letter = 'a';
//	while(letter < guess.max && hits[0] < guess.len)
//	{
//		letter ++;
//		hits[0] = 0;
//		hits[1] = 0;
//		guesses ++ ;
//		cout << MSG_TWO_A << guess.code << MSG_TWO_B;
//		cin >> answer;
//		if(!interpertAnswer(answer, hits))
//		{
//			return EXIT_BAD;
//		}
//		if(hits[0] < guess.len)
//		{
//			restraints.add(guess.code, hits[0], hits[1]);
//			if(!nextGuess(hits[0] + hits[1], &guess, letter, restraints))
//			{
//				return EXIT_BAD;
//			}
//		}
//	}
//}
