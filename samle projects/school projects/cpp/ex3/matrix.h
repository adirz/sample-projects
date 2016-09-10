/*
 * matrix.h
 *
 *  Created on: Jan 20, 2015
 *      Author: Adir
 */

#ifndef MATRIX_H_
#define MATRIX_H_

#include <map>
#include <iostream>
#include <vector>
#include <iterator>

#define NOT_SQUARE_EXCEPTION "The matrix is not squared!\n"
#define MATRIXES_MATCH_EXCEPTION "Matrixes don't match!\n"
#define ILLEGAL_DIMENSIONS_EXCEPTION "Those dimensions are illegal for a matrix!\n"

#define PRINT_SEPERATOR " "

using namespace std;

template<typename T>

class Matrix
{
public:
	typedef vector<vector<T> > vecTMatrix;
	/**
	 * default constructor, constructs 1X1 Matrix with the default value for type T (the value
	 * assigned by default constructor of type T)
	 */
	inline Matrix() :
			_rows(1), _cols(1)
	{
		_matrix.push_back(vector<T>());
	}

	/**
	 * Matrix construct a matrix by size rowsXcols using values from cells.
	 *
	 * @param rows - 	number of rows in the matrix
	 * @param cols - 	number of columns in the matrix
	 * @param cells -	a vector containing the values of the cells in the matrix
	 */
	inline Matrix(unsigned int rows, unsigned int cols, const vector<T> &cells) :
			_rows(rows), _cols(cols)
	{
		if (_rows == 0 || _cols == 0 || cells.size()!=_rows*_cols)
		{
			throw ILLEGAL_DIMENSIONS_EXCEPTION;
		}
		int i = 0;
		int j = 0;
		_matrix.push_back(vector<T>());
		for (vector<T>::iterator it = cells.begin(); it != cells.end(); ++it)
		{
			if(j == _cols)
			{
				j = 0;
				i ++;
				_matrix.push_back(vector<T>());
			}
			_matrix[i].push_back(*it);
			j ++;
		}
	}

	/**
	 * Matrix copy constructor for Matrix class performs deep copy of the matrix.
	 *
	 * @param other	the Matrix to copy
	 */
	inline Matrix (const Matrix<T> &other) :
			_rows(other.getNumOfRows()), _cols(other.getNumOfCols())
	{
		int i = 0;
		int j = 0;
		for (iterator it = other->begin(); it != other->end(); ++it)
		{
			if(j == _cols)
			{
				j = 0;
				i ++;
			}
			_matrix[i].push_back(*it);
			j ++;
		}
	}

	/**
	 * Matrix move constructor for Matrix class copy the matrix.
	 *
	 * @param other	the Matrix to copy
	 */
	inline Matrix(Matrix<T> &&other) :
			_rows(other.getNumOfRows()), _cols(other.getNumOfCols())
	{
//		this = &other;
//		this = move(other);
		this->_matrix = move(other._matrix);
	}

	/**
	 * trace calculates the trace of the matrix. the trace of a matrix is the sum of it's main
	 * diagonal, the diagonal from the upper left to the lower right. if can't calculate trace
	 * (e,g: not square matrix) throws an exception.
	 *
	 * Returns the trace of the matrix
	 */
	inline T trace() const
	{
		if(_cols != _rows)
		{
			throw NOT_SQUARE_EXCEPTION;
		}
		int sum = 0;
		for(int i = 0; i < _cols; i ++)
		{
			sum += this(i, i);
		}
		return sum;
	}

	/**
	 * det calculates the determinant of the matrix if can't calculate the determinant (e,g: not
	 * square matrix) throws an exception.
	 *
	 * Returns the determinant of the matrix
	 */
	inline T det() const
	{
		if(_cols != _rows)
		{
			throw NOT_SQUARE_EXCEPTION;
		}
		if(_rows == 1)
		{
			return (*this)(0,0);
		}
		if(_rows == 2)
		{
			return (*this)(0, 0) * (*this)(1, 1) - (*this)(0, 1) * (*this)(1, 0);
		}
		T determinante;
		bool add = true;
		for(int i = 0; i < _rows; i ++)
		{
			vector<T> cells;
			for(int j = 0; j < _rows; j ++)
			{
				for(int h = 1 ; j != i && h < _cols; h ++)
				{
					cells.push_back((*this)(j, h));
				}
			}
			Matrix<T> temp(_rows - 1 ,  _cols - 1 , cells);
			if(add)
			{
				determinante += (*this)(i, 0) * temp.det();
			}
			else
			{
				determinante -= (*this)(i, 0) * temp.det();
			}
			add = !add;
		}
		return determinante;
	}

	/**
	 * transpose returns the transpose of the matrix.
	 *
	 * Returns the transpose of the matrix.
	 */
	inline Matrix transpose() const
	{
		vector<T> cells;
		for(int i = 0; i < _rows; i ++)
		{
			for(int j = 0 ; j < _cols; j ++)
			{
				cells.push_back((*this)(j, i));
			}
		}
		return Matrix(_rows ,  _cols , cells);
	}

	/**
	 * operator- returns value by value difference of matrixes
	 *
	 * @param other	the matrix to subtract from this matrix
	 *
	 * Returns value by value difference of matrixes
	 */
	inline Matrix operator-(const Matrix &other) const
	{
		if(_rows != other.getNumOfRows() || _cols != other.getNumOfCols())
		{
			throw MATRIXES_MATCH_EXCEPTION;
		}
		vector<T> cells;
		for(int i = 0; i < _rows; i ++)
		{
			for(int j = 0 ; j < _cols; j ++)
			{
				cells.push_back((*this)(i, j) - other(i, j));
			}
		}
		return Matrix(_rows ,  _cols , cells);
	}

	/**
	 * operator-= returns value by value difference of matrixes
	 *
	 * @param other	the matrix to subtract from this matrix
	 *
	 * Returns value by value difference of matrixes
	 */
	inline Matrix& operator-=(const Matrix &other)
	{
		this = (*this) - other;
		return *this;
	}

	/**
	 * operator+ returns value by value addition of matrixes
	 *
	 * @param other	the matrix to add to this matrix
	 *
	 * Returns value by value addition of matrixes
	 */
	inline Matrix operator+(const Matrix &other) const
	{
		if(_rows != other.getNumOfRows() || _cols != other.getNumOfCols())
		{
			throw MATRIXES_MATCH_EXCEPTION;
		}
		vector<T> cells;
		for(int i = 0; i < _rows; i ++)
		{
			for(int j = 0 ; j < _cols; j ++)
			{
				cells.push_back((*this)(i, j) + other(i, j));
			}
		}
		return Matrix(_rows ,  _cols , cells);
	}

	/**
	 * operator+= returns value by value addition of matrixes
	 *
	 * @param other	the matrix to add to this matrix
	 *
	 * Returns value by value addition of matrixes
	 */
	inline Matrix& operator+=(const Matrix &other)
	{
		this = (*this) + other;
		return *this;
	}

	/**
	 * operator* returns dot product of matrixes each cell in the resulting matrix is the sum of
	 * multiplies any cell in the equivalent column in the cell of the equivalent row.
	 * (e,g: c = a * b => c_{i, j} = sum(a_{i, k} * b_{k, j})
	 * if the matrixes can't be multiply (e,g: the number of columns in the left matrix is
	 * different than the number of rows in the right one) throws an exception.
	 *
	 * @param other	the matrix to multiply by this matrix
	 *
	 * Returns dot product of matrixes
	 */
	inline Matrix operator*(const Matrix &other) const
	{
		if(_cols != other.getNumOfRows())
		{
			throw MATRIXES_MATCH_EXCEPTION;
		}
		vector<T> cells;
		for(int i = 0; i < _rows; i ++)
		{
			for(int j = 0 ; j < _cols; j ++)
			{
				T dotProduct;
				for(int k = 0; k < _cols; k ++)
				{
					dotProduct += (*this)(i, k) * other(k, j);
				}
				cells.push_back(dotProduct);
			}
		}
		return Matrix(_rows, other.getNumOfCols(), cells);
	}

	/**
	 * operator*= dot product of matrixes
	 *
	 * @param other	the matrix to multiply by this matrix
	 *
	 * Returns this as the dot product of matrixes
	 */
	inline Matrix& operator*=(const Matrix<T>& other)
	{
		this = (*this) * other;
		return *this;
	}

	/**
	 * operator= do a deep copy of another matrix into this one
	 *
	 * @param other	the matrix to copy
	 *
	 * Returns this, after copying other content.
	 */
	inline Matrix& operator=(Matrix &other)
	{
		_rows = other.getNumOfRows();
		_cols = other.getNumOfCols();
		_matrix.clear();
		int i = 0;
		int j = 0;
		for (vector<T>::iterator it = other.begin(); it != other.end(); ++it)
		{
			if(j == _cols)
			{
				j = 0;
				i ++;
			}
			_matrix[i].push_back(*it);
			j ++;
		}
//		swap(other);
		return *this;
	}

	/**
	 * operator!= returns true iff two matrixes are different
	 *
	 * @param other	the other matrix to compare with this one
	 *
	 * Returns true iff two matrixes are different
	 */
	inline bool operator!=(const Matrix &other) const
	{
		if(_rows != other.getNumOfRows() || _cols != other.getNumOfCols())
		{
			return true;
		}
		for(int i = 0 ; i < _rows ; i ++)
		{
			for(int j = 0; j < _cols ; j ++)
			{
				if(this(i, j) != other(i, j))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * operator== returns true iff two matrixes are equal
	 *
	 * @param other	the other matrix to compare with this one
	 *
	 * Returns true iff two matrixes are equal
	 */
	inline bool operator==(const Matrix &other) const
	{
		return !(this != other);
	}

	/**
	 * getNumOfRows get the number of rows in this matrix
	 *
	 * Returns the number of rows in this matrix
	 */
	inline unsigned int getNumOfRows() const
	{
		return _rows;
	}

	/**
	 * getNumOfCols get the number of columns in this matrix
	 *
	 * Returns the number of columns in this matrix
	 */
	inline unsigned int getNumOfCols() const
	{
		return _cols;
	}

	/**
	 * operator() returns the value in the [row, col] cell of the matrix
	 *
	 * @param row the row
	 * @param col the column
	 *
	 * Returns the value in the [row, col] cell of the matrix
	 */
	inline T operator()(int row, int col) const
	{
		return _matrix[row][col];
	}

	/**
	 * operator() returns a reference to the value in the [row, col] cell of the matrix
	 *
	 * @param row the row
	 * @param col the column
	 *
	 * Returns a reference to the value in the [row, col] cell of the matrix
	 */
	inline T& operator()(int row, int col)
	{
		return *_matrix[row][col];
	}

	/**
	 * map run function on every item of the matrix. returns a matrix of item item result.
	 *
	 * @param func - a function to run on all matrix items
	 *
	 * Returns a matrix of item item result of function func
	 */
	inline Matrix map(T(*func)(const T)) const
	{
		vector<T> cells;
		for(int i = 0; i < _rows; i ++)
		{
			for(int j = 0 ; j < _cols; j ++)
			{
				cells.push_back(func((*this)(i, j)));
			}
		}
		return Matrix(_rows ,  _cols , cells);
	}

	class iterator
	{
		inline iterator() :
			_iterRow(0), _iterCol(0)
		{
		}
		inline iterator(iterator it) :
			_iterRow(it._iterRow), _iterCol(it._iterCol)
		{
		}
		inline iterator& operator=(iterator it)
		{
			_iterRow = it._iterRow;
			_iterCol = it._iterCol;
			return this;
		}
		inline bool operator!=(const iterator &it) const
        {
			return (_iterRow != it._iterRow ||_iterCol != it._iterCol);
        }
		inline bool operator==(const iterator &it) const
        {
			return !(this == it);
        }
		inline T operator++()
		{
			if(_iterCol == _cols && _iterRow == _rows)
			{
				return NULL;
			}
			if(_iterCol == _cols)
			{
				_iterCol = 0;
				_iterRow ++;
			}
			return *_matrix[_iterRow][_iterCol];
		}
		inline T operator* ()
		{
			return *_matrix[_iterRow][_iterCol];
		}
	private:
		T _value;
		unsigned int _iterRow;
		unsigned int _iterCol;
	};
	template<typename V>
	class const_iterator
	{
	public:
		/**
		 * default constructor
		 */
		inline const_iterator(vector<V> values, bool end) :
		_iterLoc(0)
		{
			_values = values;
		}
		/**
		 * TODO
		 * shouldn't be, but it is part of needed
		 */
		inline iterator& operator=(const_iterator it)
		{
			_iterLoc = it._iterLoc;
			return this;
		}
		inline bool operator!=(const const_iterator &it) const
        {
			if(_iterLoc != it._iterLoc || it->getSize() != _values.size())
			{
				return true;
			}
			unsigned int iterLoc = 0;
			for(const_iterator i = it->begin(); i != it->end(); ++i)
			{
				if(*i != _values[iterLoc])
				{
					return true;
				}
				iterLoc ++;
			}
			return false;
        }
		inline bool operator==(const const_iterator &it) const
        {
			return !(this == it);
        }
		inline V operator++()
		{
			_iterLoc ++;
			if(_iterLoc == _values.size)
			{
				return NULL;
			}
			return _matrix[_iterLoc];
		}
		inline V operator* ()
		{
			if(_iterLoc == _values.size)
			{
				return NULL;
			}
			return _values[_iterLoc];
		}
		inline int getSize()
		{
			return _values.size();
		}
	private:
		vector<V> _values;
		unsigned int _iterLoc;
	};

	/**
	 * begin return an iterator over all the matrix cells, start from the first row
	 * (e,g: matrix(0, 0), matrix(0, 1) .. matrix(0, col - 1), matrix(1, 0) ...) this iterator
	 * support the copy, assignment, increment, equality/inequality and dereferencing operators.
	 *
	 * Returns an iterator over all the matrix cells
	 */
	inline iterator begin()
	{
		return iterator();
	}

	/**
	 * end returns an iterator pointing to the past-the-end cell in the matrix
	 *
	 * Returns an iterator pointing to the past-the-end cell in the matrix
	 */
	inline iterator end()
	{
		return NULL;
	}

	/**
	 * rowIteratorBegin returns an iterator over all the matrix rows
	 *
	 * Returns an iterator over all the matrix rows
	 */
	inline const_iterator<vector<T> > rowIteratorBegin() const
	{
		return const_iterator<vector<T> >(_matrix);
	}

	/**
	 *
	 */
	inline const_iterator<vector<T> > rowIteratorEnd() const
	{
	}

	/**
	 * colIteratorBegin return an iterator over all the matrix columns
	 *
	 * Returns an iterator over all the matrix columns
	 */
	inline const_iterator<vector<T> > colIteratorBegin() const
	{
	}

	/**
	 * colIteratorEnd returns an iterator pointing to the past-the-end column in the matrix
	 *
	 * Returns an iterator pointing to the past-the-end column in the matrix
	 */
	inline const_iterator<vector<T> > colIteratorEnd() const
	{
	}

	/**
	 * operator<< print a matrix assume that type T has << operator by itself. print each cell in
	 * the matrix, with a tab ('\t') between cells, and new line between columns (and at the end
	 * of the matrix) except for Matrix<char>, than this operator prints no spaces between cells,
	 * and a single space between columns.
	 *
	 * @param out the ostream object to output to.
	 * @param matrix the matrix to output.
	 *
	 * Returns out, for latter processing
	 */
	friend ostream& operator<<(ostream &out, const Matrix<T> &matrix)
	{
		for (unsigned int i = 0; i < matrix.getNumOfRows() ; ++i)
		{
			for (unsigned int j = 0; j < matrix.getNumOfCols() - 1; ++j)
			{
				out << (*this)(i, j);
				out << PRINT_SEPERATOR;
			}
			out << (*this)(i, matrix.getNumOfCols() - 1);
			out << endl;
		}
		return out;
	}

	/**
	 * operator- returns value by value negatives of a matrix
	 *
	 * @param self the matrix to negate
	 *
	 * Returns value by value negatives of a matrix (as another matrix)
	 */
	friend Matrix<T> operator-(const Matrix<T> &self)
	{
		for(int i = 0; i < _rows; i ++)
		{
			for(int j = 0 ; j < _cols; j ++)
			{
				self(i, j) = - self(i, j);
			}
		}
		return self;
	}
private:
	unsigned int _rows;
	unsigned int _cols;
	vecTMatrix _matrix;
};

#endif /* MATRIX_H_ */
