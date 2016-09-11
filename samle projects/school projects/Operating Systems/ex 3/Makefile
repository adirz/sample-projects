all : libblockchain.a

blockchain.o : blockchain.cpp blockchain.h
	g++ -Wall -std=c++11 -g blockchain.cpp -c -L. -lhash -lcrypto -lpthread

Block.o : Block.cpp
	g++ -Wall -std=c++11 -g Block.cpp -c
	
	
MinHeap.o : MinHeap.cpp
	g++ -Wall -std=c++11 -g MinHeap.cpp -c 

libblockchain.a: Block.o MinHeap.o blockchain.o 
	ar rcs libblockchain.a Block.o MinHeap.o blockchain.o 
	
clean:
	rm -f *.a *.o tar

tar:
	tar cvf ex3.tar blockchain.cpp MinHeap.cpp Block.cpp Makefile README

.PHONY: all clean tar
