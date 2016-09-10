FLAGS = -std=c99 -Wall -Wextra -Wvla
CC = gcc
MATH = -lm
all:sudukuSolver genericdfs.a



sudukuSolver:genericdfs.a sudukutree.o sudukusolver.o
	$(CC) $(FLAGS) sudukutree.o sudukusolver.o  genericdfs.a -o sudukusolver $(MATH)

sudukutree.o:
	$(CC) $(FLAGS) -c sudukutree.c -o sudukutree.o $(MATH)

sudukusolver.o:
	$(CC) $(FLAGS) -c sudukusolver.c -o sudukusolver.o $(MATH)

genericdfs.a:genericdfs.o pNodeStack.o
	ar rcs genericdfs.a pNodeStack.o genericdfs.o

genericdfs.o:
	$(CC) $(FLAGS) -c genericdfs.c -o genericdfs.o 

pNodeStack.o:
	$(CC) $(FLAGS) -c pNodeStack.c -o pNodeStack.o


clean:
	rm -f pNodeStack.o genericdfs.o genericdfs.a sudukusolver sudukusolver.o sudukutree.o
