/*
 * osm.cpp
 *
 *  Created on: Mar 9, 2015
 *      Authors: katyac, adirz
 */

#include "osm.h"
#include <unistd.h>
#include <sys/time.h>

#define ITER_NUM 50000
#define BASE_TO_NANO 100000000
#define MICRO_TO_NANO 1000
#define ERROR_CODE -1
#define ACTION_PER_LOOP 10

timeMeasurmentStructure measure;
struct timeval tv ;
struct timezone tz;
struct timeval tvNew;
struct timezone tzNew;

long LOOP_TIME;
double INSERT_TIME;

/* Initialization function that the user must call
 * before running any other library function.
 * Returns 0 upon success and -1 on failure.
 */
int osm_init()
{

	int returnVal = 0;
	int isOk = gethostname(measure.machineName, HOST_NAME_MAX);

	if (isOk == ERROR_CODE)
	{
		returnVal = ERROR_CODE;
		measure.machineName[0] = '\0';
	}

	measure.functionTimeNanoSecond = osm_function_time(measure.numberOfIterations);
	measure.instructionTimeNanoSecond = osm_operation_time(measure.numberOfIterations);
	measure.trapTimeNanoSecond = osm_syscall_time(measure.numberOfIterations);

	if (measure.functionTimeNanoSecond == ERROR_CODE ||
			measure.instructionTimeNanoSecond == ERROR_CODE)
	{
		returnVal = ERROR_CODE;
		measure.functionInstructionRatio = ERROR_CODE;
	}

	else
	{
		measure.functionInstructionRatio = measure.functionTimeNanoSecond /
				measure.instructionTimeNanoSecond;
	}

	if (measure.functionTimeNanoSecond == ERROR_CODE ||
			measure.trapTimeNanoSecond == ERROR_CODE)
	{
		returnVal = ERROR_CODE;
		measure.trapInstructionRatio = ERROR_CODE;
	}

	else
	{
		measure.trapInstructionRatio = measure.trapTimeNanoSecond /
				measure.instructionTimeNanoSecond;
	}

	return returnVal;
}

/**
 * Calculates the time it took for an action to run
 */
double timeDiff(unsigned int loopIterations)
{
	time_t timeDif = (tvNew.tv_sec) * BASE_TO_NANO + (tvNew.tv_usec) * MICRO_TO_NANO\
			- (tv.tv_sec) * BASE_TO_NANO - (tv.tv_usec) * MICRO_TO_NANO - LOOP_TIME;

	return (double)timeDif / (ACTION_PER_LOOP * loopIterations);
}


/**
 * our empty function to call to
 */
void emptyFunc()
{
}


/**
 * Calculates how much time it takes for an empty loop to run
 */
long loopTime(unsigned int osm_iterations)
{
	unsigned int i = 0;
	int getTimeOk = gettimeofday(&tv, &tz);
	if(getTimeOk)
	{
		return getTimeOk;
	}
	unsigned int loopIterations = osm_iterations / ACTION_PER_LOOP;
	for(; i < loopIterations; i ++)
	{
	}
	getTimeOk = gettimeofday(&tvNew, &tzNew);
	if(getTimeOk)
	{
		return getTimeOk;
	}
	long timeDif = (tvNew.tv_sec) * BASE_TO_NANO + (tvNew.tv_usec) * MICRO_TO_NANO\
			- (tv.tv_sec) * BASE_TO_NANO - (tv.tv_usec) * MICRO_TO_NANO;
	return timeDif;
}

/**
 * Calculates how muck time it takes to insert a value into a variable
 */
double insertTime(unsigned int osm_iterations)
{
	unsigned int i = 0;
	int getTimeOk = gettimeofday(&tv, &tz);
	if(getTimeOk)
	{
		return getTimeOk;
	}
	int a;
	unsigned int loopIterations = osm_iterations / ACTION_PER_LOOP;
	for(; i < loopIterations; i ++)
	{
		a = 0;
		a = 0;
		a = 0;
		a = 0;
		a = 0;
		a = 0;
		a = 0;
		a = 0;
		a = 0;
		a = 0;
	}
	getTimeOk = gettimeofday(&tvNew, &tzNew);
	if(getTimeOk)
	{
		return getTimeOk;
	}

	return timeDiff(loopIterations) + a;
}


/* Time measurement function for an empty function call.
   returns time in nano-seconds upon success,
   and -1 upon failure.
   Zero iterations number is invalid.
   */
double osm_function_time(unsigned int osm_iterations)
{
	unsigned int i = 0;
	int getTimeOk = gettimeofday(&tv, &tz);
	if(getTimeOk)
	{
		return getTimeOk;
	}

	unsigned int loopIterations = osm_iterations / ACTION_PER_LOOP;
	for(; i < loopIterations; i ++)
	{
		emptyFunc();
		emptyFunc();
		emptyFunc();
		emptyFunc();
		emptyFunc();
		emptyFunc();
		emptyFunc();
		emptyFunc();
		emptyFunc();
		emptyFunc();
	}
	getTimeOk = gettimeofday(&tvNew, &tzNew);
	if(getTimeOk)
	{
		return getTimeOk;
	}

	return timeDiff(loopIterations);
}

/* Time measurement function for an empty trap into the operating system.
   returns time in nano-seconds upon success,
   and -1 upon failure.
   Zero iterations number is invalid.

   */
double osm_syscall_time(unsigned int osm_iterations)
{
	unsigned int i = 0;
	int getTimeOk = gettimeofday(&tv, &tz);
	if(getTimeOk)
	{
		return getTimeOk;
	}
	unsigned int loopIterations = osm_iterations / ACTION_PER_LOOP;
	for(; i < loopIterations; i ++)
	{
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
		OSM_NULLSYSCALL;
	}
	getTimeOk = gettimeofday(&tvNew, &tzNew);
	if(getTimeOk)
	{
		return getTimeOk;
	}

	return timeDiff(loopIterations);
}


/* Time measurement function for a simple arithmetic operation.
   returns time in nano-seconds upon success,
   and -1 upon failure.
   Zero iterations number is invalid.
   */
double osm_operation_time(unsigned int osm_iterations)
{
	unsigned int i = 0;
	int a;
	int b = 5;
	int c = 9;
	int getTimeOk = gettimeofday(&tv, &tz);
	if(getTimeOk)
	{
		return getTimeOk;
	}
	unsigned int loopIterations = osm_iterations / ACTION_PER_LOOP;

	for(; i < loopIterations; i ++)
	{
		a = b + c;
		a = b + c;
		a = b + c;
		a = b + c;
		a = b + c;
		a = b + c;
		a = b + c;
		a = b + c;
		a = b + c;
		a = b + c;
	}

	getTimeOk = gettimeofday(&tvNew, &tzNew);
	if(getTimeOk)
	{
		return getTimeOk;
	}

	// Otherwise a is not used so compiling with -Wall results in warning.
	a = 0;
	return timeDiff(loopIterations) - INSERT_TIME + a;
}


timeMeasurmentStructure measureTimes (unsigned int osm_iterations)
{
	unsigned int iterationNum;
	if (osm_iterations <= 0)
	{
		iterationNum = ITER_NUM;
	}
	else
	{
		iterationNum = osm_iterations;
	}
	LOOP_TIME = loopTime(iterationNum);
	INSERT_TIME = insertTime(iterationNum);
	measure.numberOfIterations = iterationNum;
	osm_init();
	return measure;
}
