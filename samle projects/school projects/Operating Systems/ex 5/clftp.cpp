/*
 * crftp.cpp
 *
 *  Created on: Jun 12, 2015
 *      Author: Adir
 */

#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <sys/time.h>
#include <iostream>
#include <string.h>
#include <netdb.h>
#include <unistd.h>
#include <errno.h>

using namespace std;

#define NUM_OF_ARGS 5

//#define BASE_TO_NANO 100000000
//#define MICRO_TO_NANO 1000
//#define REPETITIONS 10
#define USAGE_ERROR "Usage: clftp server-port server-hostname file-to-transfer filename-in-server."
#define BUFFER_SIZE 4096

struct sockaddr_in serv_addr;
struct hostent *server = NULL;

//struct timeval tv;
//struct timezone tz;
//struct timeval tvNew;
//struct timezone tzNew;


void sys_err(const char* sys_call) {
	cerr<<"Error: function:"<<sys_call<<" errno:"<<errno<<".\n";
	return;
}

bool senddata(int sock, void *buf, int buflen)
{
	char *pbuf = (char *) buf;

	while (buflen > 0)
	{
		int num = send(sock, pbuf, buflen, 0);
		if (num == SO_ERROR)
		{
			sys_err("send");
			return false;
		}

		pbuf += num;
		buflen -= num;
	}

	return true;
}

bool sendlong(int sock, long value)
{
	return senddata(sock, &value, sizeof(value));
}

bool sendname(int sock, long name_size, char * name)
{
	if (!senddata(sock, name, name_size))
		return false;
	return true;
}

bool sendfile(int sock, FILE *f, long filesize)
{
	char buffer[BUFFER_SIZE];

	while (filesize > 0){
		int num;
		if(filesize < BUFFER_SIZE){
			num = filesize;
		}else{
			num = BUFFER_SIZE;
		}
		num = fread(buffer, 1, num, f);
		if (num < 1) {
			sys_err("fread");
			return false;
		}

		if (!senddata(sock, buffer, num))
			return false;
		filesize -= num;
	}

	return true;
}


int main(int argc, char* argv[]){

	if(argc != NUM_OF_ARGS){
		cout<<USAGE_ERROR<<endl;
		exit(-1);
	}

	int _port = atoi(argv[1]);
	if (_port <= 0 || _port > 65535) {
		cout<<USAGE_ERROR<<endl;
		exit(-1);
	}
	server = gethostbyname(argv[2]);
	string tmp = argv[3];
	char* f_name = (char*)malloc(tmp.size()+1);
	memcpy (f_name, tmp.c_str(), tmp.size()+1);

	tmp = argv[4];
	long name_size = tmp.length();
	char* srvr_f_name = (char*)malloc(tmp.size()+1);
	memcpy (srvr_f_name, tmp.c_str(), tmp.size()+1);

	FILE *f_toSend = fopen(f_name, "rb");
	if (f_toSend == NULL) {
		cout<<USAGE_ERROR<<endl;
		exit(-1);
	}

	int out_socket = socket(AF_INET, SOCK_STREAM, 0);
	if (out_socket < 0) {
		sys_err("socket");
		exit(-1);
	}
	memset((char *) &serv_addr, '\0', sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_port = htons(_port);
	memcpy((char *)server->h_addr,(char *)&serv_addr.sin_addr.s_addr, server->h_length);

	int con = connect(out_socket,((struct sockaddr*)&serv_addr), sizeof(serv_addr));
	if (con < 0)
	{
		sys_err("connect");
		exit(-1);
	}


	//sends len of file
	fseek(f_toSend, 0, SEEK_END);
	long fileSize = ftell(f_toSend);
	rewind(f_toSend);


	if (!sendlong(out_socket, fileSize))
		exit(-1);
	//sends len of name
	if (!sendlong(out_socket, name_size))
		exit(-1);

	//check if it is ok
	bool ans;
	int rec = recv(out_socket, &ans, sizeof(ans), 0);
	if(rec == SO_ERROR || rec == 0){
		sys_err("recv");
		exit(-1);
	}
	if(ans){
		cout<<"Transmission failed: too big file"<<endl;
		exit(-1);
	}

	if (!sendname(out_socket, name_size, srvr_f_name))
		exit(-1);

//	long timeDif;
	if (fileSize > 0)
	{
		//sends file
//		int getTimeOk = gettimeofday(&tv, &tz);
//		for(int i = 0; i < REPETITIONS; i++){
		if (!sendfile(out_socket, f_toSend, fileSize))
			exit(-1);
/**		}
//		getTimeOk = gettimeofday(&tvNew, &tzNew);
//		timeDif = (tvNew.tv_sec) * BASE_TO_NANO + (tvNew.tv_usec) * MICRO_TO_NANO
				- (tv.tv_sec) * BASE_TO_NANO - (tv.tv_usec) * MICRO_TO_NANO;*/
	}
//	double avgTime = ((double)timeDif)/REPETITIONS;
//	cout<<"size of file: "<<fileSize<<endl;
//	cout<<"transfer duration: "<<avgTime<<endl;
	close(out_socket);
	fclose(f_toSend);

	free (srvr_f_name);
	free (f_name);

}


