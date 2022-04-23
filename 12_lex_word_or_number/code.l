%{
  #include<stdio.h>
%}

%%

^[0-9]+ {
  printf("%s is number",yytext);
}

^[a-z]+ {
  printf("%s is word",yytext);
}

%%

int yywrap(){
}

int main(){
  printf("enter word or number\n");
  yylex();
  return 0;
}
