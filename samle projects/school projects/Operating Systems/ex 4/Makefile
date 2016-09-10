
CachingFileSystem : CachingFileSystem.o
	g++ -g -o CachingFileSystem CachingFileSystem.o `pkg-config fuse --libs`


CachingFileSystem.o : CachingFileSystem.cpp Logger.cpp Cache.cpp
	g++ -g -Wall -std=c++11 `pkg-config fuse --cflags` -c CachingFileSystem.cpp Logger.cpp Cache.cpp


tar:
	tar cvf ex4.tar Makefile README CachingFileSystem.cpp Cache.cpp Logger.cpp


clean:
	rm -f CachingFileSystem *.o


