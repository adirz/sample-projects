/**
 * genericdfs.c
 *
 *  Created on: Nov 24, 2014
 *      Author: adirz
 *
 *  The program implements genericdfs.h getBest function
 *
 */

#include "genericdfs.h"
#include "pNodeStack.h"
#include <limits.h>
#include <stdlib.h>

/**
 * inserts the values in the list 'children' to the stack 'stack'
 *
 * @param
 */
void pushChildren(int numOfKids, pNode * children, topStack * stack)
{
	for(int i = numOfKids - 1 ;  i >= 0 ; i --)
	{
		push(stack, children[i]);
	}
}

/**
 * @brief getBest This function returns the best valued node in a tree using
 * DFS algorithm.
 * @param head The head of the tree
 * @param getChildren A function that gets a node and a pointer to array of nodes.
 * the function allocates memory for an array of all the children of the node, and
 * returns the number of the children.
 * @param getVal A function that gets a node and returns its value, as int
 * @param freeNode A function that frees node from memory.
 * this function will be called for each Node returns by getChildren.
 * @param best The best available value for a node, when the function encounters
 * a node with that value it stops looking and returns it.
 * If the best value can't be determined, pass UINT_MAX (defined in limits.h)
 * for that param.
 * @param copy A function that do deep copy of Node.
 * @return The best valued node in the tree
 * In case of an error, or when all the nodes in the tree valued zero the returns
 * Node is NULL.
 * If some nodes shares the best valued, the function returns the first one it encounters.
 */
pNode getBest(pNode head, getNodeChildrenFunc getChildren, getNodeValFunc getVal,
		      freeNodeFunc freeNode, copyNodeFunc copy, unsigned int best)
{
	unsigned int tempBestVal = getVal(head);
	unsigned int curVal = tempBestVal;
	pNode bestNode = copy(head);
	if(best == tempBestVal)
	{
		freeNode(head);
		return bestNode;
	}
	topStack * stack = buildStack();
	push(stack, head);
	pNode * children;
	int numOfKids = 0;
	pNode current = NULL;
	while(!isEmpty(stack))
	{
		children = NULL;
		current = pop(stack);
//		push(toFree, current);
		numOfKids = getChildren(current, &children);
		pushChildren(numOfKids, children, stack);
		free(children);
		children = NULL;
		curVal = getVal(current);
		if(curVal > tempBestVal)
		{
			tempBestVal = curVal;
			freeNode(bestNode);
			bestNode = copy(current);
			if(best == curVal)
			{
				freeNode(current);
				freeStack(&stack, freeNode);
				return bestNode;
			}
		}
		freeNode(current);
	}
	freeNode(current);
	freeStack(&stack, freeNode);
	return bestNode;
}




