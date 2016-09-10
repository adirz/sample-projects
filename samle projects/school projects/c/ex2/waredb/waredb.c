/*
 * waredb.c
 *
 *  Created on: Nov 11, 2014
 *      Author: adirz
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stddef.h>


#define FALSE 0
#define TRUE 1

#define MSG_ONE ""

#define MAX_LINES 1000
#define MAX_MSG_LEN 50

#define NAME_LEN 20
#define BARCODE_LEN 4
#define INVENTORY_LEN 3
#define DATE_LEN 7
const int LENS[] ={NAME_LEN + 1, BARCODE_LEN + 1, INVENTORY_LEN + 1, DATE_LEN + 1};

#define TEN 10
#define ZERO '0'
#define NINE '9'

char * whatIsWrong = (char *)malloc(MAX_MSG_LEN*sizeof(char));
int isSomethingWrong = FALSE;

struct DateStruct{
	int month;
	int year;
} Date;

struct ProductStruct
{
	char name[NAME_LEN];
	unsigned int barcode;
	float inventoy;
	Date experation;
} product;

int addDigit(int num, char digit)
{
	if(digit < ZERO || digit > NINE)
	{
		isSomethingWrong = TRUE;
	}
	return num*TEN + digit - ZERO;
}

void buildName(product *p, char buffer[NAME_LEN + 1])
{
	p->name = strtok(buffer,'\s');
}

void buildBarcode(product *p, char buffer[BARCODE_LEN + 1]){

}

void buildInventory(product *p, char buffer[INVENTORY_LEN + 1]){

}

void buildExperation(product *p, char buffer[DATE_LEN + 1]){

}

product parser(char * line)
{
	product p;
	void (*funcArray[5])(*product, char[]);
	funcArray[0] = &buildBarcode;

	const char seperator = '\t';
	char *buffer;
	int i = 0;
	for (buffer = strtok (line, seperator); buffer && !isSomethingWrong; buffer = strtok (NULL, seperator))
	{
		if(strlen(buffer) > LENS[i])
		{
			isSomethingWrong = TRUE;
			whatIsWrong = MSG_ONE;
			return NULL;
		}
		else
		{
			funcArray[i](*p, buffer);
		}
	}
	for(int i = 0 ; i < strlen(line) && line[i] != '\t' && line[i] != EOF; i++)
	{

	}
	return p;
}


int main(int argc, char *argv[]){
	FILE *warehouse;
	FILE *inputFile;

	product *storing = (product *)malloc(2*MAX_LINES*sizeof(product));

	char * line;
	inputFile = fopen(argv[1], mode);
	while((line = getline(inputFile)) != EOF){
		if(isSomethingWrong){
			printf("%s", whatIsWrong);
			exit(0);
		}
	}



	return 0;
}

