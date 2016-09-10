/*
 * pNodeStack.h
 *
 * a class that represent a database to hold pNodes in a stack manner- last in is first out
 *
 *  Created on: Dec 2, 2014
 *      Author: adirz
 */

#ifndef PNODESTACK_H_
#define PNODESTACK_H_

#include "genericdfs.h"

/**
 * for easier readability of code, TRUE and FALSE
 */
#define TRUE 1
#define FALSE 0

/**
 * the structure representing the top of the stack, linking to the next one in it.
 */
typedef struct topStack topStack;

/**
 * build a topStack type and returns a pointer to it
 *
 * return a pointer to a new topStack we created
 */
topStack * buildStack();

/**
 * releases all the memory stored in the stacked, including the memory stored by its values.
 *
 * @param stack-	the stack we want to free
 * @param freeNode-	the function that releases the memory of the value
 */
void freeStack(topStack ** stack, void freeNode(pNode));

/**
 * pushes a node to a stack
 *
 * @param stack-	the stack we want to a value to
 * @param node-		the value we want to push to the stack
 */
void push(topStack * stack, pNode node);

/**
 * removes and return the value that was added last to the stack
 *
 * @param stack-	the stack we want to get her last value
 *
 * return the last value added to the stack
 */
pNode pop(topStack * stack);

/**
 * check to see if the stack has no values left in it
 *
 * @param stack-	the stack we want to know either it is empty or not
 *
 * return 1 if the stack is empty, 0 otherwise
 */
int isEmpty(topStack const* stack);


#endif /* PNODESTACK_H_ */
