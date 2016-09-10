/*
 * sudukutree.h
 *
 *	A file to represent the type of nodes of the possibilities in the suduku and the functions
 *	needed in order to use the
 *
 *  Created on: Dec 1, 2014
 *      Author: adirz
 */

#ifndef SUDUKUTREE_H_
#define SUDUKUTREE_H_

#include <stdlib.h>

/*
 * in order to efficiently convey information in the functions
 */
#define FALSE 0
#define TRUE 1

//BAD_MEM is used in the return when a function fails due to not enough memory
#define BAD_MEM -1

/**
 * as in genericdfs for readability I use the same name
 */
typedef void* pNode;

/**
 * the structure of each node in the suduku tree
 */
typedef struct sNode Node;

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
Node * initializeNode(int size, int locX, int locY, int ** board, int val);

/**
 * returns the board of the 'Node' structure - the array of placed numbers
 */
int *** getBoard(Node * node);


/**
 * check to see if there are two of the same number at any row, column or square.
 * because I don't generate any nodes like this it is used only once at the node created
 * by the file and sees if the file is wrong.
 *
 * @param node - the node we check for doubles
 *
 * return 1 if there is somewhere with two of the same, 0 otherwise
 */
int checkDoubles(Node * node);

/**
 * frees the memory allocated by the board
 */
void freeBoard(int ** board, int size);

/**
 * A function that gets a node and a pointer to array of nodes.
 * the function allocates memory for an array of all the children of the node, and
 * returns the number of the children.
 * @param source - the node we want his children
 * @param children - a pointer to an array we change to point at a list of source's children
 *
 * return the number of children of source
 */
int getNodeChildren(pNode source, pNode** children);

/**
 * A function that gets a node and returns its value, as int
 * @param node - the node we want to get his value
 *
 * return the value of node
 */
unsigned int getNodeVal(pNode node);

/**
 * A function that frees node from memory.
 * @param toFree - the node we want to free
 */
void freeNode(pNode toFree);

/**
 * A function that do deep copy of Node.
 * @param toCopy - the node we want to copy
 *
 * return a copy of toCopy
 */
pNode copyNode(pNode toCopy);

#endif /* SUDUKUTREE_H_ */
