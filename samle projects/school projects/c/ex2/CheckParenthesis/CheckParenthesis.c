/*
 * CheckParenthesis.c
 *
 *  Created on: Nov 11, 2014
 *      Author: adirz
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_PAREN 5000
#define FALSE -1
#define TRUE 1;
#define PARENTHESIS_NUMBER 4

#define MESSAGE_ONE "<%s>: no such file\n"
#define MESSAGE_TWO "ok\n"
#define MESSAGE_THREE "‫‪bad‬‬ ‫‪structure\n‬‬"
#define MESSAGE_FOUR "‫‪please‬‬ ‫‪supply‬‬ ‫‪a‬‬ ‫‪file!\n‬‬‫‪usage:‬‬ ‫‪CheckParenthesis‬‬ ‫‪<filename>\n‬‬"

const char OPEN[] = {'(', '{', '<', '['};
const char CLOSE[] = {')', '}', '>', ']'};



typedef struct ParenthesisStack
{
	int location;
	int stack[MAX_PAREN];
} parenStack;

int canAdd(parenStack *checkStack, int type)
{
	if(checkStack->location >= MAX_PAREN - 1)
	{
		return FALSE;
	}
	checkStack->stack[checkStack->location] = type;
	checkStack->location ++;
	return TRUE;
}

int canRemove(parenStack *checkStack, int type)
{
	if(checkStack->location == 0){
		return FALSE;
	}
	if(checkStack->location > 0 && checkStack->stack[checkStack->location - 1] == type)
	{
		checkStack->location --;
		return TRUE;
	}
	return FALSE;
}

//parenStack newParenthesisStack(int amount)
//{
//	parenStack checkStack;
//	checkStack.location = 0;
//	checkStack.stack = (int*)malloc(amount*sizeof(int));
//	if(NULL == checkStack.stack)
//	{
//	}
//	return checkStack;
//}

int parenToType(char paren){
	int i = 0;
	for(i = 0; i < PARENTHESIS_NUMBER ; i++)
	{
		if(OPEN[i] == paren)
		{
			return i;
		}
	}
	for(i = 0 ; i < PARENTHESIS_NUMBER ; i++)
	{
		if(CLOSE[i] == paren)
		{
			return i + PARENTHESIS_NUMBER;
		}
	}
	return FALSE;
}

int checkParen(char paren, parenStack *checkStack)
{
	int type;
	if((type = parenToType(paren)) != FALSE)
	{
		if(type < PARENTHESIS_NUMBER)
		{
			return canAdd(checkStack, type);
		}
		type = type % PARENTHESIS_NUMBER;
		return canRemove(checkStack, type);
	}
	return TRUE;
}

int main(int argc, char *argv[])
{
	FILE *inputFile;
	char *mode = "r";
	int faliure = 0;

	if(argc != 2)
	{
		printf(MESSAGE_FOUR);
		exit(0);
	}

	inputFile = fopen(argv[1], mode);

	parenStack myStack;// = newParenthesisStack(MAX_PAREN);
	myStack.location = 0;

	if (inputFile == NULL)
	{
		printf(MESSAGE_ONE, argv[argc-1]);
//		free(myStack.stack);
	 	exit(0);
	}
	char last;
	int c = 0;
	while((!faliure && (last = fgetc(inputFile)) != EOF))
	{
		c++;
		if(checkParen(last, &myStack) == FALSE)
		{
			faliure = 1;
		}
	}
	if(myStack.location > 0 || faliure)
	{
		printf(MESSAGE_THREE);
	}
	else
	{
		printf(MESSAGE_TWO);
	}
//	free(myStack.stack);
	return 0;
}
