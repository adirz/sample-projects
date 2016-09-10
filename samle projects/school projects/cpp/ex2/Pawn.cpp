/*
 * Pawn.cpp
 *
 *  Created on: Jan 9, 2015
 *      Author: adirz
 *
 *      The pawn is a special chess piece because he doesn't have a normal moves so he need a
 *      special class
 *
 *      he can't eat where he moves or move where he eats,
 *      at first he can jumps up to two blocks, but later after just one he docks.
 *      when he goes into battle he'll never turn back
 *      that's it. he is not that special...
 *
 */

#include "ChessPiece.h"

class Pawn : public ChessPiece
{
public:
	Pawn(Point loc, bool isBlack, BoardModel * model, int numOfDirections, int maxMove,
			string type, const Point * moveDirections) :
				ChessPiece(loc, isBlack, model, numOfDirections, maxMove, type, moveDirections)
	{
		_numOfDirections = 1;
	}

	void move(Point loc)
	{
		_maxMove = 1;
		ChessPiece::move(loc);
	}

	bool canMoveTo(Point loc)
	{
		if(_dead)
		{
			return false;
		}
		if((_isBlack && _model->getAtPoint(loc) == WHITE) ||
				(!_isBlack && _model->getAtPoint(loc) == BLACK))
		{
			if((_location.getX() + _moveDirections[1].getX() == loc.getX() &&
					_location.getY() + _moveDirections[1].getY() == loc.getY() ) ||
					(_location.getX() + _moveDirections[2].getX() == loc.getX() &&
					_location.getY() + _moveDirections[2].getY() == loc.getY() ))
			{
				return true;
			}
			return false;
		}
		Point tempLoc = Point(_location.getX(), _location.getY() + _moveDirections[0].getY());
		for(int i = 0 ; i < _maxMove; i ++)
		{
			if(EMPTY != _model->getAtPoint(tempLoc))
			{
				return false;
			}
			if(tempLoc == loc)
			{
				return true;
			}
			tempLoc.setY(tempLoc.getY() + _moveDirections[0].getY());
		}
		return false;
	}
};
