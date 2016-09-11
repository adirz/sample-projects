COMPILE=g++
CFLAGS=-Wall -std=c++11

TAR=tar
TARFLAGS=cvf
TARNAME=ex2.tar
TARSRCS= uthreads.cpp uthreads.h Makefile README

REMOVE=rm
RMFLAGS=-f

AR=ar
ARFLAGS=rcu
UTHREADSLIB=libuthreads.a
RANLIB=ranlib
    
all: $(UTHREADSLIB)	

$(UTHREADSLIB): uthreads.o
	$(AR) $(ARFLAGS) $(UTHREADSLIB) uthreads.o
	$(RANLIB) $(UTHREADSLIB)

	
uthreads.o: uthreads.h uthreads.cpp
	$(COMPILE) $(CFLAGS) -c uthreads.cpp -o uthreads.o
		
tar:
	$(REMOVE) $(RMFLAGS) $(TARNAME)
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)
	
clean:
	$(REMOVE) $(RMFLAGS) *.o *~ *core $(UTHREADSLIB) 


