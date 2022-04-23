#!/bin/bash -ex

lex code.l
yacc -d code.y
gcc lex.yy.c y.tab.h y.tab.c -lfl
./a.out
