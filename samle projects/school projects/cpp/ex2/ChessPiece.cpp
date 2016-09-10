/*
 * ChessPiece.cpp
 *
 *  Created on: Jan 8, 2015
 *      Author: adirz
 */

#include "ChessPiece.h"

using namespace std;

ChessPiece::ChessPiece()
{
	_hasMoved = false;
	_maxMove = 1;
	_isBlack = false;
	_model = NULL;
	_numOfDirections = 0;
	_dead = false;
	_moveDirections = NULL;
}

ChessPiece::ChessPiece(Point loc, bool isBlack, BoardModel * model, int numOfDirections,
		int maxMove, string type, const Point * moveDirections)
{
	_hasMoved = false;
	_location = loc;
	_maxMove = maxMove;
	_isBlack = isBlack;
	_model = model;
	_numOfDirections = numOfDirections;
	_dead = false;
	_moveDirections = moveDirections;
	_type = type;
}

ChessPiece::~ChessPiece()
{
}

void ChessPiece::move(Point loc)
{
	_model->move(_location, loc, _isBlack);
	_location = loc;
	_hasMoved = true;
}

bool ChessPiece::canMoveTo(Point loc)
{
	if(_dead)
	{
		return false;
	}
	for(int i = 0; i < _numOfDirections; i ++)
	{
		Point tempLoc;
		tempLoc = _location;
		for(int j = 0; j < _maxMove; j ++)
		{
			tempLoc.setX(tempLoc.getX() + _moveDirections[i].getX());
			tempLoc.setY(tempLoc.getY() + _moveDirections[i].getY());
			if(tempLoc.inBoard())
			{
				if(loc == tempLoc)
				{
					if( (BLACK != _model->getAtPoint(tempLoc) && _isBlack) ||
							(WHITE != _model->getAtPoint(tempLoc) && !_isBlack))
					{
						return true;
					}
				}
				if(EMPTY != _model->getAtPoint(tempLoc))
				{
					j = _maxMove;
				}
			}
			else
			{
				j = _maxMove;
			}
		}
	}
	return false;
}
