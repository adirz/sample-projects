/**
 * A simple two dimensional Point class to represent a point fitting in this sized board
 */

#define SIZE 8

class Point
{
public:

	/**
	 * constructor
	 */
	Point(int a = 0, int b = 0) :
		_x(a) , _y(b)
	{
	}

	void operator=(const Point p)
	{
		_x = p._x;
		_y = p._y;
	}

	bool operator==(const Point p)
	{
		return (_x == p._x && _y == p._y);
	}

	/**
	 * checks if the point is fitting to board size
	 *
	 * return true if point is on board
	 */
	bool inBoard()
	{
		if(_x < 0 || _x > SIZE || _y < 0 || _y > SIZE)
		{
			return false;
		}
		return true;
	}
	int getX() const
	{
		return _x;
	}
	int setX(int a)
	{
		_x = a;
		return _x;
	}
	int getY() const
	{
		return _y;
	}
	int setY(int b)
	{
		_y = b;
		return _y;
	}
private:
	int _x;/** the x coordinate*/
	int _y;/** the y coordinate*/
};
