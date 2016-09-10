/*
 * pNodeStack.c
 *
 * a class that represent a database to hold pNodes in a stack manner- last in is first out
 *
 *  Created on: Dec 2, 2014
 *      Author: adirz
 */

#include <assert.h>
#include <stdlib.h>
#include <stdio.h>
#include "pNodeStack.h"

/**
 * as you did not specify of what to do when we're out of memory, I decided to post this message
 * and than do what I can to exit
 */
const char STACK_MEMORY_MSG[] = "no requirement asked but there is not enough memory!\n";

/**
 * a structure representing each holder of value in the stack
 * each is connected to the next one in stack
 *
 * @param value-	the value it holds
 * @param next-		a pointer to a holder one level deeper in the stack
 */
typedef struct sStackedLink stackedLink;

/**
 * a structure representing each holder of value in the stack
 * each is connected to the next one in stack
 *
 * @param value-	the value it holds
 * @param next-		a pointer to a holder one level deeper in the stack
 */
typedef struct sStackedLink
{
	pNode value;
	stackedLink * next;
} stackedLink;

/**
 * the structure representing the top of the stack, linking to the next one in it.
 *
 * @param top- a pointer to the available holder in the stack
 */
struct topStack
{
	stackedLink *top;
};

/**
 * build a topStack type and returns a pointer to it
 *
 * return a pointer to a new topStack we created
 */
topStack* buildStack()
{
	topStack* stack = (topStack*)(malloc(sizeof(topStack)));
	if(NULL != stack)
	{
		stack->top = NULL;
	}
	else
	{
		printf(STACK_MEMORY_MSG);
	}
	return stack;
}
/**
 * releases all the memory stored in the stacked, including the memory stored by its values.
 *
 * @param stack-	the stack we want to free
 * @param freeNode-	the function that releases the memory of the value
 */
void freeStack(topStack** stack, void freeNode(pNode))
{
	while(!isEmpty(*stack))
	{
		freeNode(pop(*stack));
	}
	free(*stack);
	*stack = NULL;
}

/**
 * check to see if the stack has no values left in it
 *
 * @param stack-	the stack we want to know either it is empty or not
 *
 * return 1 if the stack is empty, 0 otherwise
 */
int isEmpty(topStack const* stack)
{
	assert(NULL != stack);
	return NULL == stack->top;
}

/**
 * pushes a node to a stack
 *
 * @param stack-	the stack we want to a value to
 * @param node-		the value we want to push to the stack
 */
void push(topStack * stack, pNode node)
{
	assert(NULL != stack);
	stackedLink *linked = (stackedLink*) malloc(sizeof(stackedLink));
	if(NULL == linked)
	{
		printf(STACK_MEMORY_MSG);
		return;
	}
	linked->value = node;
	linked->next = stack->top;
	stack->top = linked;
}

/**
 * removes and return the value that was added last to the stack
 *
 * @param stack-	the stack we want to get her last value
 *
 * return the last value added to the stack
 */
pNode pop(topStack * stack)
{
	assert(NULL != stack);
	if(NULL == stack->top)
	{
		return NULL;
	}
	pNode val = stack->top->value;
	stackedLink * linked = stack->top;
	if(NULL != linked)
	{
		stack->top = linked->next;
		free(linked);
		linked = NULL;
	}
	else
	{
		stack->top = NULL;
	}
	return val;
}
