parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program : (classes+=class)* EOF;

class
    : CLASS name=TYPE
    (INHERITS parent=TYPE)?
    LBRACE (features+=feature)* RBRACE SEMI
    ;

feature
    : name=ID LPAREN (formals+=formal (COMMA formals+=formal)*)? RPAREN
    COLON type=TYPE LBRACE expr RBRACE SEMI                                     # method
    | name=ID COLON type=TYPE (ASSIGN value=expr)? SEMI                         # attr
    ;

formal : name=ID COLON type=TYPE;

var : name=ID COLON type=TYPE (ASSIGN value=expr)?;
caseOpt : name=ID COLON type=TYPE RES value=expr;

expr
    : WHILE cond=expr LOOP body=expr POOL                                                           # while
    | IF cond=expr THEN thenBranch=expr ELSE elseBranch=expr FI                                     # if
    | NEW type=TYPE                                                                                 # new
    | ISVOID e=expr                                                                                 # isvoid
    | CASE value=expr OF (options+=caseOpt SEMI)+ ESAC                                              # case
    | entity=expr (AT type=TYPE)? DOT method=ID LPAREN (params+=expr (COMMA params+=expr)*)? RPAREN # explicitDispatch
    | LBRACE (body+=expr SEMI)+ RBRACE                                                              # block
    | method=ID LPAREN (params+=expr (COMMA params+=expr)*)? RPAREN                                 # implicitDispatch
    | LET vars+=var (COMMA vars+=var)* IN body=expr                                                 # let
    | NEG e=expr                                                                                    # neg
    | a=expr op=(MULT | DIV) b=expr                                                                 # multDiv
    | a=expr op=(PLUS | MINUS) b=expr                                                               # plusMinus
    | a=expr op=(EQUAL | LT | LE) b=expr                                                            # relational
    | NOT e=expr                                                                                    # not
    | name=ID ASSIGN value=expr                                                                     # assign
    | ID                                                                                            # id
    | LPAREN e=expr RPAREN                                                                          # paren
    | BOOL                                                                                          # bool
    | INT                                                                                           # int
    | STRING                                                                                        # string
    ;
