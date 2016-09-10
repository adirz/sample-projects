/*
 * sudukutree.c
 *
 *	A file to represent the type of nodes of the possibilities in the suduku and the functions
 *	needed in order to use the
 *
 *  Created on: Dec 1, 2014
 *      Author: adirz
 */

#include "sudukutree.h"
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>

//the value of points on the board that are not yet set
#define UNFILLED 0

/**
 * as you did not specify of what to do when we're out of memory, I decided to post this message
 * and than do what I can to exit
 */
const char MEMORY_MSG[] = "no requirement asked but there is not enough memory!\n";

/**
 * the structure of each node in the suduku tree
 *
 * @param size-		the size in our size * size board
 * @param locX- 	the last x location we checked up to
 * @param locY- 	the last y location we checked up to
 * @param board-	a 2d array representing the suduku board
 */
typedef struct sNode
{
	int size;
	int locX;
	int locY;
	int ** board;
} Node;

/**
 * A function used to initialize a node of the tree using the paramenters given
 *
 * @param size - 	the size N of the NxN suduku board
 * @param locX - 	the first index on the board 2d array
 * @param locY -	the second index on the board 2d array
 * @param board -	a pointer to a 2d array of numbers of the suduku tree
 * @param val - 	the value in pur current location
 *
 * return an initialized node
 */
Node * initializeNode(int size, int locX, int locY, int ** board, int val)
{
	Node * node = (Node *)(malloc(sizeof(Node)));
	if(NULL == node)
	{
		printf(MEMORY_MSG);
		return NULL;
	}
	node->size = size;
	node->locX = locX;
	node->locY = locY;
	node->board = (int **)(malloc(size * sizeof(int *)));
	if(NULL == node->board)
	{
		free(node);
		node = NULL;
		printf(MEMORY_MSG);
		return NULL;
	}
	for(int i = 0; i < size ; i ++)
	{
		node->board[i] = NULL;
	}
	for(int i = 0; i < size ; i ++)
	{
		node->board[i] = (int *)(malloc(size * sizeof(int)));
		if(NULL == node->board[i])
		{
			free(node);
			node = NULL;
			freeBoard(node->board, size);
			printf(MEMORY_MSG);
			return NULL;
		}
		for(int j = 0; j < size ; j ++)
		{
			node->board[i][j] = board[i][j];
		}
	}
	node->board[locY][locX] = val;
	return node;
}

/**
 * returns the board of a node
 *
 * @param node- a pointer to a board we want to get his board
 *
 * return a pointer to the board in node
 */
int *** getBoard(Node * node)
{
	return &(node->board);
}

/**
 * creates a list of suduku nodes and conveys it into children
 * the nodes are created using the following variables
 *
 * @param locX- 	the x location we change from the father node
 * @param locY-		the y location we change from the father node
 * @param board-	the board of the father node, which we copy except from our new location
 * 					we make a new value for each child
 * @param children-	a pointer to a list of pointers to nodes we make point to our
 * 					newly created nodes
 *
 * return the number of nodes created
 */
int consieveChildren(int locX, int locY, int ** board, int size, pNode ** children)
{
	int kidsCount = 0;
	int * posibbles = (int *)malloc(size * sizeof(int));
	if(NULL == posibbles)
	{
		printf(MEMORY_MSG);
		return BAD_MEM;
	}
	//Initializing everything to be possible
	for(int i = 0 ; i < size ; i ++ )
	{
		posibbles[i] = TRUE;
	}
	//check line
	for(int i = 0 ; i < size ; i ++ )
	{
		if(UNFILLED != board[locY][i])
		{
			posibbles[board[locY][i] - 1] = FALSE;
		}
	}
	//check column
	for(int i = 0 ; i < size ; i ++ )
	{
		if(UNFILLED != board[i][locX])
		{
			posibbles[board[i][locX] - 1] = FALSE;
		}
	}
	//check box. I assume the size has a natural square root.
	int root = (int)sqrt(size);
	for(int i = locX - locX % root ; i < locX - locX % root + root ; i ++ )
	{
		for(int j = locY - locY % root ; j < locY - locY % root + root ; j ++ )
		{
			if(UNFILLED != board[j][i])
			{
				posibbles[board[j][i] - 1] = FALSE;
			}
		}
	}
	//adds all the possible children
	for(int i = 0 ; i < size ; i ++ )
	{
		if(posibbles[i])
		{
			*children = (pNode *)(realloc(*children, (kidsCount + 1) * sizeof(pNode)));
			if(NULL == *children)
			{
				free(posibbles);
				posibbles = NULL;
				return BAD_MEM;
			}
			(*children)[kidsCount] = (pNode)initializeNode(size, locX, locY, board, i + 1);
			if(NULL == (*children)[kidsCount])
			{
				free(posibbles);
				posibbles = NULL;
				return BAD_MEM;
			}
			kidsCount ++ ;
		}
	}
	free(posibbles);
	posibbles = NULL;
	return kidsCount;
}

/**
 * finds the next unfilled member in the board, and changes x and y accordingly
 * @param x 	a pointer to the x axis to be changed
 * @param y 	a pointer to the y axis to be changed
 * @param board our sudoku board
 * @param size 	the size of the sudoku board
 */
void nextUnfilled(int *x, int *y, int **board, int size)
{
	int myX = *x;
	int myY = *y;
	for(; myY < size ; myY ++)
	{
		for(; myX < size ; myX ++)
		{
			if( UNFILLED == board[myY][myX] )
			{
				*x = myX;
				*y = myY;
				return ;
			}
		}
		myX = 0;
	}
}

/**
 * A function that gets a node and a pointer to array of nodes.
 * the function allocates memory for an array of all the children of the node, and
 * returns the number of the children.
 * @param source - the node we want his children
 * @param children - a pointer to an array we change to point at a list of source's children
 *
 * return the number of children of source
 */
int getNodeChildren(pNode source, pNode ** children)
{
	Node * sourceNode = (Node *)source;
	if(NULL == sourceNode)
	{
		return BAD_MEM;
	}
	int x = sourceNode->locX;
	int y = sourceNode->locY;
	nextUnfilled(&x, &y, sourceNode->board, sourceNode->size);
	return consieveChildren(x, y, sourceNode->board, sourceNode->size, children);
}

/**
 * as we use it a lot, this function resets all of the values in posibbles to 0
 *
 * @param posibbles- 	the list we want to reset
 * @param size - 		the size of the list
 */
void resetPosibles(int * posibbles, int size)
{
	for(int i = 0; i < size ; i ++)
	{
		posibbles[i] = 0;
	}
}

/**
 * gets the board a list of possible an a location and check if it exists,
 * changes depends on existence
 *
 * @param i,j - coordinates
 * @param posibbles- a list of possibilities for this location
 * @param node- the node we check in
 *
 * return 1 if it already exists and try another one, 0 otherwise
 */
int checkPosible(int * posibbles, Node * node, int i, int j)
{
	if(UNFILLED != node->board[i][j])
	{
		if(posibbles[node->board[i][j] - 1])
		{
			free(posibbles);
			posibbles = NULL;
			return TRUE;
		}
		posibbles[node->board[i][j] - 1] = TRUE;
	}
	return FALSE;
}

/**
 * check to see if there are two of the same number at any row, column or square.
 *
 * @param node - the node we check for doubles
 *
 * return 1 if there is somewhere with two of the same, 0 otherwise
 */
int checkDoubles(Node * node)
{
	int * posibbles = (int *)malloc(node->size*sizeof(int));
	if(NULL == posibbles)
	{
		printf(MEMORY_MSG);
		return BAD_MEM;
	}
	resetPosibles(posibbles , node->size);
	for(int i = 0; i < node->size ; i ++)
	{
		for(int j = 0; j < node->size ; j ++)
		{
			if(checkPosible(posibbles, node, i, j))
			{
				return TRUE;
			}
		}
		resetPosibles(posibbles , node->size);
	}
	for(int i = 0; i < node->size ; i ++)
	{
		for(int j = 0; j < node->size ; j ++)
		{
			if(checkPosible(posibbles, node, j, i))
			{
				return TRUE;
			}
		}
		resetPosibles(posibbles , node->size);
	}
	int root = (int)sqrt(node->size);
	for(int h = 0 ; h < node->size ; h += root)
	{
		for(int i = 0 ; i < node->size ; i += root)
		{
			for(int j = i ; j < i + root ; j ++ )
			{
				for(int k = h ; k < h + root ; k ++ )
				{
					if(checkPosible(posibbles, node, j, k))
					{
						return TRUE;
					}
				}
			}
			resetPosibles(posibbles , node->size);
		}
	}
	free(posibbles);
	posibbles = NULL;
	return FALSE;
}

/**
 * A function that gets a node and returns its value, as int
 * @param node - the node we want to get his value
 *
 * return the value of node
 */
unsigned int getNodeVal(pNode node)
{
	Node * vNode = (Node *)node;
	if(NULL == vNode)
	{
		return (unsigned int)BAD_MEM;
	}
	int x = vNode->locX;
	int y = vNode->locY;
	nextUnfilled(&x, &y, vNode->board, vNode->size);
	if(vNode->locX == x && vNode->locY == y && 0 != vNode->board[y][x])
	{
		return 1;
	}
	return 0;
}

/**
 * A function that frees a board from memory.
 * @param board - the board we want to free
 * @param size -  the size of the board
 */
void freeBoard(int ** board, int size)
{
	for(int i = 0; i < size ; i ++)
	{
		free(board[i]);
		board[i] = NULL;
	}
	free(board);
	board = NULL;
}

/**
 * A function that frees node from memory.
 * @param toFree - the node we want to free
 */
void freeNode(pNode toFree)
{
	Node * toFreeNode = (Node *)toFree;
	freeBoard(toFreeNode->board, toFreeNode->size);
	free(toFreeNode);
	toFreeNode = NULL;
}

/**
 * A function that do deep copy of Node.
 * @param toCopy - the node we want to copy
 *
 * return a copy of toCopy
 */
pNode copyNode(pNode toCopy)
{
	Node * nodeCopy = (Node *)toCopy;
	if(NULL == nodeCopy)
	{
		return NULL;
	}
	Node * node;
	node = initializeNode(nodeCopy->size, nodeCopy->locX, nodeCopy->locY, nodeCopy->board
			, (nodeCopy->board)[nodeCopy->locY][nodeCopy->locX]);
	return node;

}
