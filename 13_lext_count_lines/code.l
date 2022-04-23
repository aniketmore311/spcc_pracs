%{
  #include<stdio.h>
  int line_count = 0;
  int char_count = 0;
  int word_count = 0;
%}

%%

\n {
  line_count++;
  word_count++;
}
 
([ ])+ {
  word_count++;
}

. {
  char_count++;
}

%%
int yywrap(){
}

int main(){
  printf("enter the text: \n");
  yylex();
  printf("line: %d, words: %d, characters: %d\n",line_count, word_count, char_count);
  return 0;
}
