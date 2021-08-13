IDIR = include
_DEPS = conf.h calc.h
DEPS = $(patsubst %,$(IDIR)/%,$(_DEPS))

ODIR = obj
_OBJ = main.o calc.o
OBJ = $(patsubst %,$(ODIR)/%,$(_OBJ))

CC = gcc
CFLAGS = -Wall -pedantic -std=c11 -lm -I$(IDIR)

$(ODIR)/%.o: %.c $(DEPS)
	mkdir -p $(ODIR)
	$(CC) -c -o $@ $< $(CFLAGS)

bcs: $(OBJ)
	$(CC) -o $@ $^ $(CFLAGS)

.PHONY: clean
clean:
	rm -rf $(ODIR) ./bcs *~ $(IDIR)/*~
