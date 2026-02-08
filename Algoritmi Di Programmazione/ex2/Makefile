
CC = gcc
CFLAGS = -Wall -Wextra -I.


MAIN_SRC = main_ex2.c es2.c es2dinamico.c
TEST_SRC = test_ex2.c  es2.c es2dinamico.c


HEADERS = es2.h es2dinamico.h 


MAIN_EXEC = main_ex2
TEST_EXEC = test_ex2


all: $(MAIN_EXEC) $(TEST_EXEC)


$(MAIN_EXEC): $(MAIN_SRC) $(HEADERS)
	$(CC) $(CFLAGS) -o $@ $(MAIN_SRC)


$(TEST_EXEC): $(TEST_SRC) $(HEADERS)
	$(CC) $(CFLAGS) -o $@ $(TEST_SRC)

clean:
	rm -f $(MAIN_EXEC) $(TEST_EXEC) *.o

.PHONY: all clean
