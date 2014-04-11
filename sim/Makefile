CC = /usr/bin/g++
LIBS = -lm

sim: main.o rv.o event.o
	$(CC) -o sim main.o rv.o event.o

main.o: rv.h event.h main.cpp
	$(CC) -c main.cpp

rv.o: rv.h rv.cpp
	$(CC) -c rv.cpp

event.o: event.h event.cpp
	$(CC) -c event.cpp

