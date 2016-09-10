FILES = Pawn.cpp Board.cpp chess.cpp ChessPiece.cpp BoardModel.cpp Point.cpp
EXTRAS = Board.h ChessPiece.h README makefile
FLAGS = -Wall -Wextra -Wvla -std=c++11
CC = g++


all:
	$(CC) $(FLAGS) $(FILES) -o chess
	
tar:
	tar cvf ex2.tar $(FILES) $(EXTRAS)
	
clean:
	rm -f chess chess.o
