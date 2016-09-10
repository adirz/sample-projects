/*
 * Board.h
 *
 *  Created on: Jan 1, 2015
 *      Author: Adir
 *
 *      represnting the board game and all that you can do with it
 */

#ifndef BOARD_H_
#define BOARD_H_

#include "Pawn.cpp"
#include <iostream>

#define CASTLING_GAP 2
#define KING "\u265A"
#define QUEEN "\u265B"
#define ROOK "\u265C"
#define BISHOP "\u265D"
#define KNIGHT "\u265E"
#define PAWN "\u265F"

using namespace std;

class Board
{
public:
	/**
	 * simple constructor. no arguments because all boards look the same
	 */
	Board();
	/**
	 * simple destructor
	 */
	~Board();
	/**
	 * prints the board
	 */
	void printBoard();

	/**
	 * checking if the specified point is threatened by the color we ask
	 * @param loc - the location we check if threatened
	 * @param fromWhites - if true we check for threats from whites, otherwise from blacks
	 * return true if the color specified can attack this location.
	 */
	bool underThreat(Point loc, bool fromWhites);

	/**
	 * moves as specified if it is possible and legal
	 * @param from - move the piece from here
	 * @param to -	move the piece to here
	 * @param blackTurn - if it is the black's turn
	 * return true iff moved
	 */
	bool moveIfCan(Point from, Point to, bool blackTurn);

	/**
	 * check if the player whose turn it is can move
	 * @param blackTurn - if it is the black's turn
	 * return true iff whoever turns it is he can play
	 */
	bool canPlay(bool blackTurn);
	Point getKing(bool black);
private:

	/**
	 * if we move the piece in 'from' to 'to' will the king of the color blackTurn be threatened?
	 *
	 * @param from - 		the point we move a piece from
	 * @param to - 	 		the point we move the piece to
	 * @param blackTurn -	are we checking the black king
	 *
	 * return true if blackTurn and the king of black will be eaten if we make this move or
	 * 		not black's turn and the king of white will be eaten if we make this move
	 */
	bool willBeEaten(Point from, Point to, bool blackTurn);

	/**
	 * find the seiral of a piece
	 * @param location - the location of the wanted piece
	 * @param isBlack - if the piece is black
	 * returns the location of the piece in the array, 0 if not found but I am looking for it only
	 * after it was found
	 */
	int getPieceSerial(Point location, bool isBlack);

	/**
	 * check if it is possible to do castling and if so, does it
	 * @param from - the location of the king we castle
	 * @param to -	 the location the king is going to
	 * @param blackTurn -	is it the blacks turn
	 * return true if we castled false, otherwise.
	 */
	bool doCasteling(Point from, Point to, bool blackTurn);
	BoardModel _model;/** the shared simple representation of the board */
	ChessPiece *_pieces[COLORS][PIECES];  /** 2d array of pointers to the different pieces */

	/**
	 * prints a location on the board
	 * @param greenBG -	is the background green
	 * @param isBlack -	is the piece in this location is black
	 * @param piece -	what type of piece it is on the board
	 */
	void printSpot(bool greenBG, bool isBlack = true, string piece = " ");
	ChessPiece * getPiece(Point location);
	ChessPiece * getPiece(int x, int y);
};

#endif /* BOARD_H_ */
