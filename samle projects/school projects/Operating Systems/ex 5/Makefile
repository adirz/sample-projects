
all: clftp srftp

clftp : clftp.cpp
	g++ -std=c++11 clftp.cpp -o clftp

srftp : srftp.cpp
	g++ -std=c++11 srftp.cpp -pthread -o srftp


tar:
	tar cvf ex5.tar Makefile README srftp.cpp clftp.cpp performance.jpg performance_-log_scale.jpg

clean:
	rm -f *.o

