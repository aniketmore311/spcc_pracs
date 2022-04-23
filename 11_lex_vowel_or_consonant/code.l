%{
  #include<stdio.h>
%}

%%

[aeiou] {
  printf("%s is vowel",yytext);
}

[a-z] {
  printf("%s is consonant",yytext);
}

%%

int yywrap(){}

int main(){
  printf("enter a character: \n");
  yylex();
  return 0;
}


