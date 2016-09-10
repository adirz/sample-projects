/*
 * BoardModel.cpp
 *
 *  Created on: Jan 1, 2015
 *      Author: Adir
 *
 *      a representation model of the board. it is easier to work with and does not envolve
 *      self-referencing are sup-referencing
 */

#include "Point.cpp"

#define PIECES 16
#define COLORS 2
#define BLACK 'b'
#define WHITE 'w'
#define EMPTY ' '

/**
 * a model to represent the board with a lot less complexity and no self-refrencing.
 * just keep what color is where
 */
class BoardModel
{
public:
	//constructor
	BoardModel()
	{
		for(int i = 0; i < SIZE ; i++)
		{
			for(int j = 0; j < 2 ; j++)
			{
				_color[i][j] = WHITE;
			}
		}
		for(int i = 0; i < SIZE ; i++)
		{
			for(int j = 2; j < 6 ; j++)
			{
				_color[i][j] = EMPTY;
			}
		}
		for(int i = 0; i < SIZE ; i++)
		{
			for(int j = 6; j < SIZE ; j++)
			{
				_color[i][j] = BLACK;
			}
		}
	}
	/**
	 * move two colors on the board model
	 *
	 * @param from -	the point we make vacant
	 * @param to -		the point we put the color we moved
	 * @param isBlack -	if we want to put black in 'to'
	 */
	void move(Point from, Point to, bool isBlack)
	{
		_color[from.getX()][from.getY()] = EMPTY;
		if(isBlack)
		{
			_color[to.getX()][to.getY()] = BLACK;
		}
		else
		{
			_color[to.getX()][to.getY()] = WHITE;
		}
	}

	char getAtPoint(Point loc)
	{
		return _color[loc.getX()][loc.getY()];
	}
	char getAtPoint(int x, int y)
	{
		return _color[x][y];
	}
	char setAtPoint(Point loc, char toSet)
	{
		_color[loc.getX()][loc.getY()] = toSet;
		return _color[loc.getX()][loc.getY()];
	}
private:
	char _color[SIZE][SIZE]; /** A simple representation of the board by colors */
};


