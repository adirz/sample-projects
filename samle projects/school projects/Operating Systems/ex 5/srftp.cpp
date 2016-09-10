/*
 * srftp.cpp
 *
 *  Created on: Jun 12, 2015
 *      Author: Adir & Katya
 *
 *      Data transfer order:
 *      	Client sends:
 *      		size of name 	- int size
 *      		size of file 	- int size
 *      	Server sends:
 *      		Is size legal 	- boolean size
 *      	Client sends:
 *      		name			- "size of name" bits
 *      		data			- rest of transfer
 */

#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/time.h>
#include <vector>
#include <pthread.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <fstream>
#include <errno.h>


using namespace std;

#define NUM_OF_ARGS 3
#define PORT_ARG 1
#define MAX_SIZE_ARG 2
#define LISTEN true
#define BUFFER_SIZE 4096
#define USAGE_ERROR "Usage: srftp server-port max-file-size"
#define MAX_SOCKETS 5

int g_max_size;


void sys_err(const char* sys_call) {
	cerr<<"Error: function:"<<sys_call<<" errno:"<<errno<<".\n";
	return;
}

bool readData(int sock, char *buf, int buflen)
{
	memset(buf, '\0', buflen);
	int recieved = recv(sock, buf, buflen, 0);
	if (recieved < 0) {
		sys_err("recv");
	}
	return true;
}


bool writefile(int sock, ofstream &f, int filesize)
{
	char buffer[BUFFER_SIZE];
	while (filesize > 0){
		int num;
		if(filesize < BUFFER_SIZE){
			num = filesize;
		}
		else{
			num = BUFFER_SIZE;
		}
		if (!readData(sock, buffer, num))
			return false;

		for(int i = 0; i < num; i++){
			f<<buffer[i];
		}

		filesize -= num;
	}
	return true;
}



long readLong(int sock)
{
	int size = (long)sizeof(long);
	int received = 0;
	int curr_received;
	char buf[size];
	bzero(buf, size);
	while(received < size)
	{
		curr_received = recv(sock, buf + received, size - received, 0);
		if (curr_received == -1)
		{
			sys_err("recv");
			pthread_exit(NULL);
		}
		received += curr_received;
	}
	return *((long*)buf);
}


void * getData (void *dummyPt)
{
	int * sock = (int*)(dummyPt);
	int filesize, namesize;
	filesize = readLong(*sock);
	if (filesize < 0){
		cerr<<"Wrong reading of long from stream.\n";
		pthread_exit(NULL);

	}
	namesize = readLong(*sock);
	if (namesize < 0) {
		cerr<<"Wrong reading of long from stream.\n";
		pthread_exit(NULL);
	}

	bool okData = (filesize + namesize > g_max_size);
	char *pbuf = (char *) (&okData);
	if(okData){
		long num = send(*sock, pbuf, sizeof(okData), 0);
		if(num == SO_ERROR)
		{
			sys_err("send");
			pthread_exit(NULL);
		}
	}
	else {
		long num = send(*sock, pbuf, sizeof(okData), 0);
		if(num == SO_ERROR)
		{
			sys_err("send");
			pthread_exit(NULL);
		}
		char fname[namesize+1];
		fname[namesize] = '\0';
		readData(*sock, fname, namesize);
		ofstream f;
		f.open(fname, ios::binary);
		if (filesize > 0)
		{
			writefile(*sock, f, filesize);
		}
		f.close();
	}
	delete sock;
	pthread_exit(NULL);
	return NULL;
}

int main(int argc, char **argv){
	if(argc != NUM_OF_ARGS){
		cout<<USAGE_ERROR<<endl;
		exit(-1);
	}

	int _port = atoi(argv[PORT_ARG]);
	if (_port <= 0 || _port > 65535) {
		cout<<USAGE_ERROR<<endl;
		exit(-1);
	}

	g_max_size = atoi(argv[MAX_SIZE_ARG]);
	if (g_max_size < 0) {
		cout<<USAGE_ERROR<<endl;
		exit(-1);
	}

	int listenFd;
	socklen_t len; //store size of the address
	struct sockaddr_in svrAdd, clntAdd;

	listenFd = socket(AF_INET, SOCK_STREAM, 0);
	if (listenFd < 0) {
		sys_err("socket");
		exit(-1);
	}

	bzero((char *)&svrAdd, sizeof(svrAdd));
	svrAdd.sin_family = AF_INET;
	svrAdd.sin_port = htons(_port);
	svrAdd.sin_addr.s_addr = INADDR_ANY;


	int yes = 1;
	if ( setsockopt(listenFd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int)) == -1 )
	{
		sys_err("setsockopt");
		exit(-1);
	}

	int binding = bind(listenFd, (struct sockaddr *)&svrAdd, sizeof(svrAdd));

	if(binding < 0){
		sys_err("bind");
		exit(-1);
	}

	listen(listenFd, MAX_SOCKETS+1);
	len = sizeof(clntAdd);

	while(true){
		pthread_t connections;
		int *sock = new int();// the file descriptor of the new socket open by accept
		*sock = accept(listenFd, (struct sockaddr *)&clntAdd, &len);

		if (sock >= 0)
		{
			pthread_create(&connections, NULL, getData, sock);
		}
		else if (*sock == EAGAIN || *sock == EWOULDBLOCK)
		{
			continue;
		}
		else
		{
			sys_err("accept");
			exit(-1);
		}

	}
}
