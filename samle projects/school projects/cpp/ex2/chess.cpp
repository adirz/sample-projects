
/**
 * ============================================================================
 *  Name        : chess.cpp
 *  Author      : adirz
 *  Version     : 2.3
 *  Copyright   : GNU free
 *  Description : A platform for a game of Chess.
 *  				first enter just follow instructions after running.
 *  				supports all ties excepts repeating move.
 *  				winnig is done only by threatening a king and there is no
 *  				option at all for him to avoid being eaten.
 * ============================================================================
 */

#include "Board.h"
using namespace std;

#define MOVE_LEN 4
#define FIRST_CHAR 'A'
#define FIRST_NUM '1'

const string MSG_ONE = "Enter white player name:\n";
const string MSG_TWO = "Enter black player name:\n";
const string MSG_THREE = ": Please enter your move:\n";
const string MSG_DRAW = "It's A draw!\n";
const string GAME_WIN = " won!\n";
const string CLR_SCR = "\33[2J";
const string CHECK_WARNING = "\33[37;41mCheck!\33[0m\n";
const string ILLEGAL_MOVE = "\33[37;41millegal move\33[0m\n";

string gNames[COLORS];

/**
 * converts the input move to points of movement of type Point
 *
 * @param move-	a string we recieve to convert
 * @param from-	the first point on board, we change it
 * @param to-	the second point on board, we change it
 *
 * return true if it is of bad length, false otherwise
 */
bool convertMove(string move, Point & from, Point & to)
{
	if(move.length() != MOVE_LEN)
	{
		return true;
	}
	from = Point(move[0] - FIRST_CHAR, move[1] - FIRST_NUM);
	to = Point(move[2] - FIRST_CHAR, move[3] - FIRST_NUM);
	return false;
}

int main()
{
	string move;
	cout << MSG_ONE;
	getline(cin, gNames[0]);
	cout << MSG_TWO;
	getline(cin, gNames[1]);
	cout << CLR_SCR;
	Board game;
	game.printBoard();
	bool blackTurn = false;
	Point from;
	Point to;
	while(game.canPlay(blackTurn))
	{
		if(game.underThreat(game.getKing(blackTurn), blackTurn) )
		{
			cout << CHECK_WARNING;
		}
		cout << gNames[blackTurn] << MSG_THREE;
		getline(cin, move);
		while(convertMove(move, from, to) || !game.moveIfCan(from, to, blackTurn))
		{
			cout << ILLEGAL_MOVE;
			cout << CLR_SCR;
			game.printBoard();
			cout << gNames[blackTurn] << MSG_THREE;
			getline(cin, move);
		}
		blackTurn = !blackTurn;
		cout << CLR_SCR;
		game.printBoard();
	}
	if(game.underThreat(game.getKing(blackTurn), blackTurn))
	{
		cout << gNames[!blackTurn] << GAME_WIN;
	}
	else
	{
		cout << MSG_DRAW;
	}
	return 0;
}
