
/*
 * MinHeap.cpp
 *
 *  Created on: May 7, 2015
 *      Author: adirz
 */

#include <algorithm>
#include <vector>

using namespace std;

class MinHeap
{
	vector<int> minHeap;

	static bool comp(const int& a, const int& b)
	{
		return a<b?false:true;
	}
public:

	void Push(int val) {
		minHeap.push_back(val);
	    push_heap(minHeap.begin(), minHeap.end(), comp);
	}

	int Pop() {
	    int val = minHeap.front();

	    //This operation will move the smallest element to the end of the vector
	    pop_heap(minHeap.begin(), minHeap.end(), comp);

	    //Remove the last element from vector, which is the smallest element
	    minHeap.pop_back();
	    return val;
	}

	bool isEmpty()
	{
		return minHeap.empty();
	}
};
