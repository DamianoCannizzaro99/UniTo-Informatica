CC = gcc
CFLAGS = -I. -I./unity -Wall -Wextra

all: main_ex1 test_ex1

main_ex1: main_ex1.c es1.c es1.h 
	$(CC) $(CFLAGS) -o main_ex1 main_ex1.c es1.c  -w

test_ex1: test_ex1.c es1.c es1.h unity.c unity.h
	$(CC) $(CFLAGS) -o test_ex1 test_ex1.c es1.c 

clean:
	rm -f main_ex1 test_ex1

.PHONY: all clean
