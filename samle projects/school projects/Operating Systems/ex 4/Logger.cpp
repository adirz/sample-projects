/*
 * Logger.cpp
 *
 *  Created on: May 25, 2015
 *     Authors: adirz, katyac
 */

#include <ios>
#include <fstream>
#include <iostream>
#include <time.h>
#include "Cache.cpp"

using namespace std;

#define PATH_MAX 4096
#define LOG_PATH "/filesystem.log"

class Logger{

public:
	ofstream log_file;
	char * _path;

	Logger(char* rootDir) {
		_path = new char[PATH_MAX];
		strcpy(_path, rootDir);
		strcat(_path, LOG_PATH);
		const char* log_path = _path;
		log_file.open(log_path, fstream::app|fstream::out);
	}
	~Logger()
	{
		log_file.close();
		delete[] _path;
	}

	/**
	 * write to log the function and time
	 */
	void write_func(const char* name)
	{
		log_file << time(NULL);
		log_file << " " << name << endl;

	}

	/**
	 * write to log the ioctl
	 */
	void write_stat(Cache *c_stat)
	{
		c_stat->print_blocks(&log_file);
	}
};
