/*
 * ChessPiece.h
 *
 *  Created on: Jan 5, 2015
 *      Author: Adir
 *
 * The chess pieces we play with
 * they can move, check if it is a part of their directional moves (not specials) and die
 */

#ifndef CHESSPIECE_H_
#define CHESSPIECE_H_

#include "BoardModel.cpp"
#include <string>
using namespace std;

class ChessPiece
{
public:
	/**
	 * basic constructor. its pretty much empty and need to be filled
	 */
	ChessPiece();

	/**
	 * a constructor
	 *
	 * @param loc - 			the location of the piece
	 * @param isBlack - 		is the piece black
	 * @param model -			a pointer to the shared representation of the board
	 * @param numOfDirections -	number of directions the piece have
	 * @param type -			which type of piece it is
	 * @param moveDirections -	an array of all legal movement directions
	 */
	ChessPiece(Point loc, bool isBlack, BoardModel * model, int numOfDirections, int maxMove,
			string type, const Point * moveDirections);

	/**
	 * basic destructor
	 */
	virtual ~ChessPiece();

	/**
	 * moves the location of the piece to the new location
	 * @param loc -	the location to move to
	 */
	void virtual move(Point loc);

	/**
	 * checks if this chess piece can move to the desired location.
	 *
	 * @param loc -	the location we want to move to.
	 *
	 * return true iff this piece can move to this location.
	 */
	bool virtual canMoveTo(Point loc);

	inline bool getIsBlack()
	{
		return _isBlack;
	};
	inline string getType()
	{
		return _type;
	};
	inline Point getLocation()
	{
		return _location;
	};
	inline bool getHasMoved()
	{
		return _hasMoved;
	};
	inline bool setHasMoved(bool hasMoved)
	{
		_hasMoved = hasMoved;
		return _hasMoved;
	};
	inline bool getIsDead()
	{
		return _dead;
	};
	inline bool setIsDead(bool isDead)
	{
		_dead = isDead;
		return _dead;
	};
protected:
	Point _location; /** the location of this piece on the board */
	BoardModel * _model; /** a pointer to the shared representation of the board */
	bool _isBlack; /** if the piece is black */
	int _maxMove; 	/** what is the maximum number of steps the piece can move */
	bool _hasMoved; /** if the piece has moved yet */
	int _numOfDirections; /** in how many direction can the piece move */
	const Point * _moveDirections; /** an array of points we can get to from 0,0 */
	string _type; /** the type of piece we use */
	bool _dead; /** if the piece is dead */
};


#endif /* CHESSPIECE_H_ */
