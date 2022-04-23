#!/bin/bash -ex

lex code.l
gcc lex.yy.c -lfl
./a.out
