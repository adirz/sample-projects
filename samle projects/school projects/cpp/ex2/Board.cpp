/*
 * Board.cpp
 *
 *  Created on: Jan 1, 2015
 *      Author: Adir
 */

#include "Board.h"
#include <stdlib.h>

using namespace std;
#define ALL_MOVES 8
#define HALF_MOVES 4
#define PAWN_MOVES 3
#define PREFIX "\33["
#define GREEN_BG ";42"
#define BLUE_BG ";46"
#define NO_COLOR "0"
#define BLACK_TEXT "30"
#define WHITE_TEXT "37"
#define SUFFIX "m"

const string EDGE = "  ABCDEFGH\n\n";

const Point ROOK_DIR[HALF_MOVES] = {Point(-1, 0), Point(1, 0), Point(0, 1), Point(0, -1)};
const Point BISHOP_DIR[HALF_MOVES] = {Point(1, 1), Point(-1, -1), Point(-1, 1), Point(1, -1)};
const Point ALL_DIR[ALL_MOVES] = {ROOK_DIR[0], ROOK_DIR[1], ROOK_DIR[2], ROOK_DIR[3],
		BISHOP_DIR[0], BISHOP_DIR[1], BISHOP_DIR[2], BISHOP_DIR[3]};
const Point KNIGHT_DIR[ALL_MOVES] = { Point(2, 1), Point(1, 2), Point(2, -1), Point(-1, 2),
		Point(-2, 1), Point(1, -2), Point(-2, -1), Point(-1, -2)};
const Point PAWN_DIR[COLORS][3] = {{Point(0, 1), Point(1, 1), Point(-1, 1)},
		{Point(0, -1), Point(1, -1), Point(-1, -1)} };

Board::Board()
{
	for(int j = 0; j < COLORS; j++)
	{
		_model = BoardModel();
		_pieces[j][0] = new ChessPiece(Point(4, 7 * j), j, &_model, ALL_MOVES, 1, KING, ALL_DIR);
		_pieces[j][1] = new ChessPiece(Point(3, 7 * j), j, &_model, ALL_MOVES, SIZE - 1, QUEEN,
				ALL_DIR);
		_pieces[j][2] = new ChessPiece(Point(0, 7 * j), j, &_model, HALF_MOVES, SIZE - 1, ROOK,
				ROOK_DIR);
		_pieces[j][3] = new ChessPiece(Point(7, 7 * j), j, &_model, HALF_MOVES, SIZE - 1, ROOK,
				ROOK_DIR);
		_pieces[j][4] = new ChessPiece(Point(2, 7 * j), j, &_model, HALF_MOVES, SIZE - 1, BISHOP,
				BISHOP_DIR);
		_pieces[j][5] = new ChessPiece(Point(5, 7 * j), j, &_model, HALF_MOVES, SIZE - 1, BISHOP,
				BISHOP_DIR);
		_pieces[j][6] = new ChessPiece(Point(1, 7 * j), j, &_model, ALL_MOVES, 1, KNIGHT,
				KNIGHT_DIR);
		_pieces[j][7] = new ChessPiece(Point(6, 7 * j), j, &_model, ALL_MOVES, 1, KNIGHT,
				KNIGHT_DIR);
		for(int i = PIECES/2; i < PIECES ; i ++)
		{
			_pieces[j][i] = new Pawn(Point(i - PIECES/2, 1 + 5 * j), j, &_model, PAWN_MOVES, 2,
					PAWN, PAWN_DIR[j]);
		}
	}
}

Board::~Board()
{
	for(int j = 0; j < COLORS; j++)
	{
		for(int i = 0; i < PIECES ; i ++)
		{
			delete _pieces[j][i];
		}
	}
}

Point Board::getKing(bool black)
{
	return _pieces[black][0]->getLocation();
}

void Board::printBoard()
{
	bool greenBG = false;
	ChessPiece * curr = NULL;
	cout << EDGE;
	for(int y = SIZE - 1; y >=0 ; y --)
	{
		cout << y + 1 << EMPTY;
		for(int x = 0; x < SIZE ; x ++)
		{
			curr = getPiece(x, y);
			if(NULL == curr)
			{
				printSpot(greenBG);
			}
			else
			{
				printSpot(greenBG, curr->getIsBlack(), curr->getType());
			}
			greenBG = !greenBG;
		}
		greenBG = !greenBG;
		cout << " " << y + 1 << "\n";
	}
	cout << "\n";
	cout << EDGE;
}

void Board::printSpot(bool greenBG, bool isBlack, string piece)
{
	string bg;
	if(greenBG)
	{
		bg = GREEN_BG;
	}
	else
	{
		bg = BLUE_BG;
	}
	string textColor;
	if(" " == piece)
	{
		textColor = NO_COLOR;
	}
	else
	{
		if(isBlack)
		{
			textColor = BLACK_TEXT;
		}
		else
		{
			textColor = WHITE_TEXT;
		}
	}

	cout << PREFIX << textColor << bg << SUFFIX << piece << PREFIX << NO_COLOR << SUFFIX;
}

ChessPiece * Board::getPiece(Point location)
{
	for(int j = 0; j < COLORS; j++)
	{
		for(int i = 0; i < PIECES; i ++)
		{
			if(!_pieces[j][i]->getIsDead())
			{
				if(_pieces[j][i]->getLocation() == location)
				{
					return _pieces[j][i];
				}
			}
		}
	}
	return NULL;
}

int Board::getPieceSerial(Point location, bool isBlack)
{
	for(int i = 0; i < PIECES; i ++)
	{
		if(!_pieces[isBlack][i]->getIsDead())
		{
			if(_pieces[isBlack][i]->getLocation() == location)
			{
				return i;
			}
		}
	}
	return 0;
}

ChessPiece * Board::getPiece(int x, int y)
{
	return getPiece(Point(x, y));
}

bool Board::underThreat(Point loc, bool fromWhites)
{
	char original = _model.getAtPoint(loc);
	_model.setAtPoint(loc, (fromWhites ? BLACK : WHITE));
	for(int i = 0; i < PIECES ; i ++)
	{
		if(_pieces[!fromWhites][i]->canMoveTo(loc))
		{
			_model.setAtPoint(loc, original);
			return true;
		}
	}
	_model.setAtPoint(loc, original);
	return false;
}

/**
 * if we move like so, will it expose the king of the blackTurn color?
 */
bool Board::willBeEaten(Point from, Point to, bool blackTurn)
{
	ChessPiece * target = getPiece(to);
	if(NULL != target)
	{
		target->setIsDead(true);
	}
	_model.move(from, to, blackTurn);
	bool beEaten;
	if(getKing(blackTurn) == from)
	{
		beEaten = underThreat(to, blackTurn);
	}
	else
	{
		beEaten = underThreat(getKing(blackTurn), blackTurn);
	}
	_model.move(to, from, blackTurn);
	if(NULL != target)
	{
		_model.setAtPoint(to, (blackTurn ? WHITE : BLACK));
		target->setIsDead(false);
	}
	return beEaten;
}

bool Board::canPlay(bool blackTurn)
{
	for(int i = 0; i < PIECES ; i ++)
	{
		if(!_pieces[blackTurn][i]->getIsDead())
		{
			for(int j = 0 ; j < SIZE ; j ++)
			{
				for(int h = 0 ; h < SIZE ; h ++)
				{
					if(_pieces[blackTurn][i]->canMoveTo(Point(j, h)) &&
							!willBeEaten(_pieces[blackTurn][i]->getLocation(),
									Point(j, h), blackTurn))
					{
						return true;
					}
				}
			}
		}
	}
	return false;
}

bool Board::doCasteling(Point from, Point to, bool blackTurn)
{
	int direction = (to.getX() - from.getX()) / abs(to.getX() - from.getX());
	const int ROOK_NUM = (5 + direction)/2; //(5-1)/2=2: the left rook, (5+1)/2 = 3: the right rook
	//checking neither had moved
	if(_pieces[blackTurn][0]->getHasMoved())
	{
		return false;
	}
	if(_pieces[blackTurn][ROOK_NUM]->getHasMoved())
	{
		return false;
	}
	//checking it is empty between them
	for(int i = 1; i < abs(_pieces[blackTurn][ROOK_NUM]->getLocation().getX() -
			_pieces[blackTurn][0]->getLocation().getX()) ; i ++)
	{
		if(EMPTY != _model.getAtPoint(from.getX() + i * direction, from.getY()))
		{
			return false;
		}
	}
	//checking it is not under threat
	Point tempLoc = from;
	for(int i = 0; i < CASTLING_GAP + 1; i ++)
	{
		if(underThreat(tempLoc, blackTurn))
		{
			return false;
		}
		tempLoc.setX(tempLoc.getX() + direction);
	}
	//move them to the required locations
	_pieces[blackTurn][0]->move(to);
	from.setX(from.getX() + direction);
	_pieces[blackTurn][ROOK_NUM]->move(from);
	return true;
}

bool Board::moveIfCan(Point from, Point to, bool blackTurn)
{
	if(!(from.inBoard() && to.inBoard()))
	{
		return false;
	}
	ChessPiece * piece = getPiece(from);
	if(NULL == piece)
	{
		return false;
	}
	if(piece->getIsBlack() != blackTurn)
	{
		return false;
	}
	/**
	 * the 'if' is checking if the move is castling:
	 */
	if( _pieces[blackTurn][0]->getLocation() == from && from.getY() == to.getY() &&
			(to.getX() + CASTLING_GAP == from.getX() || to.getX() - CASTLING_GAP == from.getX()) )
	{
		return doCasteling(from, to, blackTurn);
	}
	if(!piece->canMoveTo(to))
	{
		return false;
	}
	if(willBeEaten(from, to, blackTurn))
	{
		return false;
	}
	ChessPiece * target = getPiece(to);
	piece->move(to);
	if(NULL != target)
	{
		target->setIsDead(true);
	}
	if(piece->getType() == PAWN && to.getY() == 7 * (!blackTurn))
	{
		int serial = getPieceSerial(to, blackTurn);
		delete _pieces[blackTurn][serial];
		_pieces[blackTurn][serial] = new ChessPiece(Point(3, 7 * blackTurn), blackTurn, &_model,
				ALL_MOVES, SIZE - 1, QUEEN, ALL_DIR);
	}
	return true;
}

