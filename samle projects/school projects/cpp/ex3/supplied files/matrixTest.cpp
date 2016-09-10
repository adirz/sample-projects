#include <iostream>
#include <cassert>
#include <complex.h>
#include <vector>
#include <cmath>
#include <algorithm>
#include <sstream>
#include <vector>

#include "matrix.h"

#define EPSILON .001
#define cmp(A,B) (abs((A)-(B))>EPSILON)

using namespace std;

bool testGet()
{
	Matrix<double> a;
	if(a(0,0) != 0 || a.getNumOfCols() != 1 || a.getNumOfRows() != 1)
		return false;

	for(int loop = 0; loop < 3000; loop++){
		unsigned int cols = 2+random()%50, rows = 2+random()%50;
		vector<double> v(rows*cols);
		for(unsigned int i = 0;i < rows; i++)
			for(unsigned int j = 0; j < cols; j++)
				v[i*cols + j] = random()/(random()/63.0);
		Matrix<double> b(rows, cols,(const vector<double>) v);
		Matrix<double> b2((const Matrix<double>)b);
		if(b2 != b || !(b2 == b))
			return false;
		if(b.getNumOfCols() != cols || b.getNumOfRows() != rows ||
				b2.getNumOfCols() != cols || b2.getNumOfRows() != rows)
			return false;
		//	cout<<b;
		//	for(auto i=v.begin();i!=v.end();i++)
		for(unsigned int i = 0;i < rows; i++)
			for(unsigned int j = 0; j < cols; j++)
				if (cmp(v[i*cols + j] ,b(i, j)))
					return false;

		vector<complex<double>> vec;
		for(unsigned int i=0;i < rows*cols; i++)
		{
			const complex<double> c(random()/(random()/63.0), random()/(random()/63.0));
			vec.push_back(c);
		}
		//	cout <<er<< vec.size() << " " <<cols << " "<<rows<<endl;
		Matrix<complex<double>> cMat(rows, cols, (const vector<complex<double>>)vec);
		if(cMat.getNumOfCols() != cols || cMat.getNumOfRows() != rows)
			return false;
		//	cout<<b;
		//	for(auto i=v.begin();i!=v.end();i++)
		for(unsigned int i = 0;i < rows; i++)
			for(unsigned int j = 0; j < cols; j++)
				if ((vec[i*cols + j] != cMat(i, j)))
					return false;
	}
	return true;
}

bool testSub()
{
	double arr1[] = {1, 0, 0, 1}, arr2[] = {1,2,3,4}, arr3[] = {5,6,7,8};
	double diff1 = 4, diff2[] = {0, -2, -3, -3}, diff3[] = {4, 6, 7, 7};
	vector<double> v1, v2, v3;
	v1.assign(arr1, arr1 + 4);
	v2.assign(arr2, arr2 + 4);
	v3.assign(arr3, arr3 + 4);
	Matrix<double> m1(2,2, (const vector<double>)v1), m2(2,2, (const vector<double>)v2),m3(2,2, (const vector<double>)v3);
	Matrix<double> n1 = -m1;
	Matrix<double> d1 = m3 - m2, d2 = m1 - m2, d3 = m3 - m1;
	m3-=m2;
	m1-=m2;
//	cout<<m1<<m2<<m3<<n1<<d1<<d2<<d3;
	for(int i=0;i<2;i++)
		for(int j=0;j<2;j++)
			if(cmp(d1(i, j), diff1) || cmp(d2(i, j), diff2[i*2+j])
					|| cmp(d3(i, j), diff3[i*2+j]) || cmp(m3(i, j), diff1)
					|| cmp(m1(i, j), diff2[i*2+j]) || cmp(n1(i, j), (i==j?-1:0)))
				return false;
	return true;
}

bool testAdd()
{
	double arr1[] = {1, 0, 0, 1}, arr2[] = {4,3,2,1}, arr3[] = {5,6,7,8};
	double res1 = 9, res2[] = {5, 3, 2, 2}, res3[] = {6, 6, 7, 9};
	vector<double> v1, v2, v3;
	v1.assign(arr1, arr1 + 4);
	v2.assign(arr2, arr2 + 4);
	v3.assign(arr3, arr3 + 4);
	Matrix<double> m1(2,2, (const vector<double>)v1), m2(2,2, (const vector<double>)v2),m3(2,2, (const vector<double>)v3);
	Matrix<double> d1 = m3 + m2, d2 = m1 + m2, d3 = m3 + m1;
	m3+=m2;
	m1+=m2;
//	cout<<m1<<m2<<m3<<d1<<d2<<d3;
	for(int i=0;i<2;i++)
		for(int j=0;j<2;j++)
			if(cmp(d1(i, j), res1) || cmp(d2(i, j), res2[i*2+j])
					|| cmp(d3(i, j), res3[i*2+j]) || cmp(m3(i, j), res1)
					|| cmp(m1(i, j), res2[i*2+j]))
				return false;
	return true;
}

bool testMul()
{
	double arr1[] = {1, 0, 0, 1}, arr2[] = {4,3,2,1}, arr3[] = {5,6,7,8};
	double res2[] = {41, 48, 17, 20}, res3[] = {32, 21, 44, 29};
	vector<double> v1, v2, v3;
	v1.assign(arr1, arr1 + 4);
	v2.assign(arr2, arr2 + 4);
	v3.assign(arr3, arr3 + 4);
	Matrix<double> m1(2,2, (const vector<double>)v1), m2(2,2, (const vector<double>)v2),m3(2,2, (const vector<double>)v3);
//	cout<<m1<<m2<<m3;
	Matrix<double> d1 = m2 * m3,
			d2 = m1 * m2,
			d3 = m3 * m1;
	m3*=m2;
	m1*=m2;
//	cout<<m1<<m2<<m3<<d1<<d2<<d3;
	for(int i=0;i<2;i++)
		for(int j=0;j<2;j++)
			if(cmp(d1(i, j), res2[i*2+j]) || cmp(d2(i, j), arr2[i*2+j])
					|| cmp(d3(i, j), arr3[i*2+j]) || cmp(m3(i, j), res3[i*2+j])
					|| cmp(m1(i, j), arr2[i*2+j]))
				return false;
	return true;
}

bool testDet()
{
	double arr1[] = {1, 0, 0, 1}, arr2[] = {4,3,2,1}, arr3[] = {8,7,7,8};
	double res1 = 1, res2 = -2, res3 = 15;
	vector<double> v1, v2, v3;
	v1.assign(arr1, arr1 + 4);
	v2.assign(arr2, arr2 + 4);
	v3.assign(arr3, arr3 + 4);
	Matrix<double> m1(2,2, (const vector<double>)v1), m2(2,2, (const vector<double>)v2),m3(2,2, (const vector<double>)v3);
	//	cout<<m1<<m2<<m3;
	return m1.det() == res1 && m2.det() == res2 && m3.det() == res3;
}

bool testTrace()
{
	double arr1[] = {1, 0, 0, 1}, arr2[] = {4,3,2,1}, arr3[] = {5,6,7,8};
	double res1 = 2, res2 = 5, res3 = 13;
	vector<double> v1, v2, v3;
	v1.assign(arr1, arr1 + 4);
	v2.assign(arr2, arr2 + 4);
	v3.assign(arr3, arr3 + 4);
	Matrix<double> m1(2,2, (const vector<double>)v1), m2(2,2, (const vector<double>)v2),m3(2,2, (const vector<double>)v3);
	//	cout<<m1<<m2<<m3;
	return m1.trace() == res1 && m2.trace() == res2 && m3.trace() == res3;

}

bool testTranspose()
{
	double arr1[] = {1, 0, 0, 1}, arr2[] = {4,3,2,1}, arr3[] = {5,6,7,8};
	double res2[] = {4, 2, 3, 1}, res3[] = {5, 7, 6, 8};
	vector<double> v1, v2, v3;
	v1.assign(arr1, arr1 + 4);
	v2.assign(arr2, arr2 + 4);
	v3.assign(arr3, arr3 + 4);
	vector<double> r2, r3;
	r2.assign(res2, res2 + 4);
	r3.assign(res3, res3 + 4);
	Matrix<double> m1(2,2, (const vector<double>)v1), m2(2,2, (const vector<double>)v2),m3(2,2, (const vector<double>)v3);
	Matrix<double> t2(2,2,r2), t3(2,2,r3);
//	cout<<m2.adjoint()<<m2<<m2.transpose();
	return  m1.transpose() == m1
			&& m2.transpose() == t2
			&& m3.transpose() == t3;
}

bool testMap()
{
	for(int q = 0; q<500; q++)
	{
		int cols = 2 + random() % 50, rows = 2 + random() % 50;
		vector<double> vec(cols*rows);
		generate(vec.begin(), vec.end(), rand);
		Matrix<double> mat(rows, cols, (const vector<double>)vec), sqrts = ((const Matrix<double>)mat).map(sqrt),
				logs = mat.map(log);
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
				if(cmp(mat(i, j), sqrts(i, j) * sqrts(i, j)) ||
						cmp(mat(i, j) , exp(logs(i, j))))
					return false;
	}
	return true;
}

bool testPrint()
{
	string str = "feel this\n0\t1\n2\t3\n", str2 = "feelthis";
	ostringstream out;
	vector<char> vec(str2.begin(), str2.end());
	Matrix<char> cmat(2, 4, vec);
	out<<cmat;
	vector<int> v(4);
	for(int i = 0; i < 4; i++)
		v[i] = i;
	Matrix<int> mat(2, 2, v);
	out<<mat;
//	cout<<out.str();
	return out.str() == str;
}

bool testIter()
{
    for(int q = 0; q<500; q++)
    {
        unsigned int cols = 2 + random() % 50, rows = 2 + random() % 50;
        vector<double> vec(cols*rows);
        generate(vec.begin(), vec.end(), rand);
//        for(int i =0;i<4;i++)
//            cout<<vec[i]<<" ";
//        cout<<endl;
        Matrix<double> mat(rows, cols, (const vector<double>&)vec);
//        cout<<mat;
        unsigned int j = 0;
        for(auto i = mat.begin(); i != mat.end(); ++i, ++j)
        {
            if(cmp(vec[j], (*i)) || cmp(*i, mat(j/cols, j%cols)) || j >= vec.size())
                return false;
        }
        if(j != vec.size())
            return false;

        //test rowIterator
        j=0;
        for(auto i = mat.rowIteratorBegin(); i != mat.rowIteratorEnd(); i++, j++)
        {
            auto start = vec.begin() + j*cols, end = start + cols;
            if(i->size() != cols)
                return false;
            int l = 0;
            for(auto k = i->begin(); k != i->end() && start != end; ++k, ++start, l++)
                if(*k != *start || (cmp(mat(j, l), (*k))))
                    return false;
        }
        if(j != rows)
            return false;

        //test colIterator
        j=0;
        for(auto i = mat.colIteratorBegin(); i != mat.colIteratorEnd(); ++i, j++)
        {
            auto start = vec.begin() + j, end = start + (cols)*(rows - 1);
            if(i->size() != rows)
                return false;
            int l = 0;
            for(auto k = i->begin(); k != i->end() && start != end; ++k, start += cols, l++)
                if(*k != *start || cmp(mat(l, j), (*k)))
                    return false;
        }
        if(j != cols)
            return false;
    }
    return true;
}

int main(int, char **)
{
	bool fine = true;
	if(!testGet())
	{
		cerr << "basic getters or seters problem, exiting..." <<endl;
		return 1;
	}

	if(!testSub())
	{
		cout<<"operator - is not working well!"<<endl;
		fine = false;
	}

	if(!testAdd())
	{
		cout<<"operator + is not working well!"<<endl;
		fine = false;
	}

	if(!testMul())
	{
		cout<<"operator * is not working well!"<<endl;
		fine = false;
	}

	if(!testDet())
	{
		cout<<"determinate problem!"<<endl;
		fine = false;
	}

	if(!testTrace())
	{
		cout<<"you do not calculate the trace as expected!"<<endl;
		fine = false;
	}

	if(!testTranspose())
	{
		cout<<"transpose test failed!"<<endl;
		fine = false;
	}

	if(!testMap())
	{
		cout<<"map test failed!"<<endl;
		fine = false;
	}

	if(!testPrint())
	{
		cout<<"<< operator test failed!"<<endl;
		fine = false;
	}

    if(!testIter())
    {
        cout<<"iterators test failed!"<<endl;
        fine = false;
    }

    if(fine)
		cout<<"your code seems to pass all basic matrix tests!"<<endl;

	return 0;
}

