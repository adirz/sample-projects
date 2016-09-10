/*
 * sudukusolver.c
 *
 *	solve a suduku!
 *	use:
 *	sudukusolver <filename>
 *
 *  Created on: Dec 1, 2014
 *      Author: adirz
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include "sudukutree.h"
#include "genericdfs.h"

#define EXPECTED_ARGS 2	// we want to receive only the file name

/**
 * a set of values used in the code
 */
#define MIN_DIGIT '0'
#define MAX_DIGIT '9'
#define NUM_OF_SIZES 10
#define MAX_TABLE 100
#define MAX_LINE_LEN 293 //100 spaces +9*1 one digits +90*2 two digits +1*3 three digits + one EOL

/**
 * is true if 'digit' is a digit otherwise is false
 */
#define IS_DIGIT(digit) !(digit < MIN_DIGIT || digit > MAX_DIGIT)

const char * SEPERATOR = " \t\n";
const int LEGAL_SIZES[] = {1, 4, 9, 16, 25, 36, 49, 64, 81, 100};
const char *READ_MODE = "r";
/**
 * the error message to display if and when errors occur
 */
const char MSG_ONE[] = "please supply a file!\nusage: sudukusolver <filename>";
const char MSG_TWO[] = "%s: no such file\n";
const char MSG_THREE[] = "%s:not a valid suduku file\n";
const char MSG_FOUR[] = "no solution!\n";
const char * MSG[] = {MSG_ONE, MSG_TWO, MSG_THREE, MSG_FOUR};
/**
 * as you did not specify of what to do when we're out of memory, I decided to post this message
 * and than do what I can to exit
 */
const char A_MEMORY_MSG[] = "no requirement asked but there is not enough memory!\n";

/**
 * global variables for the creation of the suduku board and node
 */
int size = 0;
int ** board;

/**
 * check if the number is legal for use
 * it check with compatibility with the size and range of numbers
 *
 * @param num-	the number that we check if it is legal
 *
 * return 0 if it is good, 1 if not
 */
int isIlegalNum(int num)
{
	if(0 == size)
	{
		size = num;
		for(int i = 0 ; i < NUM_OF_SIZES ; i ++)
		{
			if(LEGAL_SIZES[i] == size)
			{
				return FALSE;
			}
		}
	}
	else if(num <= size && num >= 0)
	{
		return FALSE;
	}
	return TRUE;
}

/**
 * convert a string into integer
 *
 * @param string- the string we want to convert
 *
 * return the number 'string' represent
 */
int stringToInt(char * string)
{
	int num = 0;
	for(int j = 0 ; j < (int)strlen(string) ; j ++)
	{
		if(!IS_DIGIT(string[j]))
		{
			return -1;
		}
		num = 10 * num;
		num += string[j] - MIN_DIGIT;
	}
	return num;
}

/**
 * parses a line into an array of values
 *
 * @param num-		a pointer to an array we set the values into
 * @param amount-	our legal number of values that should be in the line
 * @param line-		the line we want to parse
 *
 * return 1 if it is not a legal line, 0 otherwise
 */
int parseLine(int ** num, int amount, char * line)
{
	int place = 0;

	char * buffer = strtok (line, SEPERATOR);
	for(int i = 0 ; i < amount ; i ++)
	{
		if(NULL == buffer)
		{
			return TRUE;
		}
		(*num)[place] = stringToInt(buffer);
		if(-1 == (*num)[place])
		{
			return TRUE;
		}
		if(isIlegalNum((*num)[place]))
		{
			return TRUE;
		}
		buffer = strtok (NULL, SEPERATOR);
		place ++;
	}
	if(NULL != buffer)
	{
		return TRUE;
	}
	for(int j = 0 ; j < (int)strlen(line) ; j ++)
	{
		if('\n' == line[j])
		{
			return TRUE;
		}
	}
	return FALSE;
}

/**
 * when there are errors we need to do all those stuff
 *
 * @param file- 	close it
 * @param numLine-	free it
 * @param fName-	print it as part of a message
 */
int exitOnError(FILE **file, int ** numLine, char * fName)
{
	printf(MSG[2], fName);
	*numLine = NULL;
	free(*numLine);
	numLine = NULL;
	fclose(*file);
	return FALSE;
}

/**
 * reads a file and set what in it into the global board
 *
 * @param fName-	the name of the file
 *
 * return 0 if it is not a legal file, 1 otherwise
 */
int fileToBoard(char * fName)
{
	FILE *file =  fopen(fName, READ_MODE);
	if(NULL == file)
	{
		printf(MSG[1], fName);
		return FALSE;

	}
	char line[MAX_LINE_LEN];
	fgets(line, MAX_LINE_LEN, file);
	int * numLine = (int *)(malloc(MAX_TABLE * sizeof(int)));
	if(NULL == numLine)
	{
		printf(A_MEMORY_MSG);
		exit(1);
	}
	if(parseLine(&numLine, 1, line))
	{
		return exitOnError(&file, &numLine, fName);
	}
	if(isIlegalNum(numLine[0]))
	{
		return exitOnError(&file, &numLine, fName);
	}
	board = (int **)(malloc(size * sizeof(int *)));
	if(NULL == board)
	{
		free(numLine);
		numLine = NULL;
		printf(A_MEMORY_MSG);
		exit(1);
	}
	for(int i = 0; i < size ; i ++)
	{
		board[i] = NULL;
	}
	for(int i = 0; i < size ; i ++)
	{
		if(NULL == fgets(line, MAX_LINE_LEN, file))
		{
			freeBoard(board, size);
			return exitOnError(&file, &numLine, fName);
		}
		if(parseLine(&numLine, size, line))
		{
			freeBoard(board, size);
			return exitOnError(&file, &numLine, fName);
		}
		board[i] = (int *)(malloc(size * sizeof(int)));
		if(NULL == board[i])
		{
			board[i] = NULL;
			freeBoard(board, size);
			free(numLine);
			numLine = NULL;
			printf(A_MEMORY_MSG);
			exit(1);
		}
		for(int j = 0; j < size ; j ++)
		{
			board[i][j] = numLine[j];
		}
	}
	if(NULL != fgets(line, MAX_LINE_LEN, file))
	{
		freeBoard(board, size);
		return exitOnError(&file, &numLine, fName);
	}
	free(numLine);
	numLine = NULL;
	fclose(file);
	return TRUE;
}

/**
 * prints a board to screen
 *
 * @param board-	the board we want to print
 * @param size-		the size of the board
 */
void printBoard(int ** board, int size)
{
	printf("%d\n", size);
	for(int i = 0 ; i < size; i ++)
	{
		for(int j = 0 ; j < size - 1; j ++)
		{
			printf("%d ", board[i][j]);
		}
		printf("%d\n", board[i][size - 1]);
	}
}

int main(int argc, char ** argv)
{
	Node * head;
	if(argc != EXPECTED_ARGS)
	{
		printf(MSG[0]);
		return 1;
	}
	if(FALSE == fileToBoard(argv[1]))
	{
		return 1;
	}
	head = initializeNode(size, 0 , 0, board, board[0][0]);
	if(NULL == head)
	{
		freeBoard(board, size);
		return 1;
	}
	int doublecheck = checkDoubles(head);
	if(BAD_MEM == doublecheck)
	{
		freeNode(head);
		head = NULL;
		freeBoard(board, size);
		printf(A_MEMORY_MSG);
		return 1;
	}
	if(doublecheck)
	{
		freeNode(head);
		head = NULL;
		freeBoard(board, size);
		printf(MSG[3]);
		return 1;
	}
	Node * solution = (Node *)getBest(head, getNodeChildren, getNodeVal, freeNode, copyNode, 1);
	unsigned int bestVal = getNodeVal(solution);
	if(0 == bestVal)
	{
		printf(MSG[3]);
		return 1;
	}
	if((unsigned int)BAD_MEM == bestVal)
	{
		printf(A_MEMORY_MSG);
		return 1;
	}
	printBoard(*getBoard(solution), size );
	freeNode(solution);
	freeBoard(board, size);
	return 0;
}
