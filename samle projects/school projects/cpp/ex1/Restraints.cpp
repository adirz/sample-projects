///*
// * Restraints.cpp
// *
// *  Created on: Dec 19, 2014
// *      Author: Adir
// */
//
//
//#include <string>
//#include <stdlib.h>
//#include "Code.cpp"
//using namespace std;
//
//class Restraints
//{
//public:
//	Restraints(int L, char C) :
//		len(L), max(C)
//	{
//		asked = (string * )malloc(sizeof(string));
//		bullsResults = (int *)(malloc(sizeof(int)));
//		cowsResults = (int *)(malloc(sizeof(int)));
//		amount = 0;
//	}
//	~Restraints()
//	{
//		free(asked);
//		free(bullsResults);
//		free(cowsResults);
//	}
//
//	void add(string guess, int bulls, int cows)
//	{
//		amount ++;
//		asked = (string * )realloc(asked, amount * sizeof(string));
//		asked[amount - 1] = guess;
//		bullsResults = (int *)(realloc(asked, amount * sizeof(int)));
//		bullsResults[amount - 1] = bulls;
//		cowsResults = (int *)(realloc(asked, amount * sizeof(int)));
//		cowsResults[amount - 1] = cows;
//	}
//	bool matchRestraints(Code * guess)
//	{
//		int hits[2];
//		for(int i = 0 ; i < amount ; i ++)
//		{
//			guess->checkCode(asked[i], hits);
//			if(hits[0] != bullsResults[i] || hits[1] != cowsResults[i])
//			{
//				return false;
//			}
//		}
//		return true;
//	}
//private:
//	int len;
//	char max;
//	int amount;
//	string * asked;
//	int * bullsResults;
//	int * cowsResults;
//};
//
