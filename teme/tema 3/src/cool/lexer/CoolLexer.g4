lexer grammar CoolLexer;

tokens { ERROR } 

@header{
    package cool.lexer;	
}

@members{    
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
}

/*
 *  Keywords
 */
CLASS : 'class';
INHERITS : 'inherits';
NEW : 'new';
IF : 'if';
THEN : 'then';
ELSE : 'else';
FI : 'fi';
ISVOID : 'isvoid';
LET : 'let';
IN : 'in';
WHILE : 'while';
LOOP : 'loop';
POOL : 'pool';
CASE : 'case';
OF : 'of';
ESAC : 'esac';
NOT : 'not';

BOOL : 'true' | 'false';

/*
 * Identifiers
 */
fragment LETTER : [a-zA-Z];
fragment UP_LETTER : [A-Z];
fragment LOW_LETTER : [a-z];
TYPE : (UP_LETTER)(LETTER | DIGIT | '_')*;
ID : (LOW_LETTER)(LETTER | '_' | DIGIT)*;

/*
 * Integers
 */
fragment DIGIT : [0-9];
INT : DIGIT+;

/*
 * String
 */
STRING : '"' ('\\"'
                | '\\\r\n'
                | '\\\n'
                | ( '\u0000' { raiseError("xString contains null characterx"); } )
                | .
             )*?
    (
    EOF { raiseError("EOF in string constant"); }
    | ('\n' | '\r\n') { raiseError("Unterminated string constant"); }
    | '"' {
        String s = getText();
        s = s.substring(1, s.length() - 1);
        s = s.replace("\\t", "\t");
        s = s.replace("\\b", "\b");
        s = s.replace("\\f", "\f");
        s = s.replace("\\\r\n", "\r\n");
        s = s.replace("\\\n", "\n");
        s = s.replace("\\n", "\n");

        StringBuilder s2 = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '\\') {
                s2.append(s.charAt(i));
            } else {
                if (s.charAt(i + 1) == '\\') {
                    s2.append(s.charAt(i + 1));
                    i++;
                }
            }
        }

        if (s2.toString().length() > 1024) {
            raiseError("String constant too long");
        } else {
            setText(s2.toString());
        }
    }
    )
;

SEMI : ';';
COMMA : ',';
ASSIGN : '<-';
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
EQUAL : '=';
LT : '<';
LE : '<=';
COLON : ':';
AT : '@';
DOT : '.';
RES : '=>';
NEG : '~';

LINE_COMMENT : '--' .*? ('\r\n' | '\n' | EOF) -> skip;

BLOCK_COMMENT
    : '(*'
      (BLOCK_COMMENT | .)*?
      (EOF { raiseError("EOF in comment"); }
      | '*)' { skip(); })
    ;

UNMATCHED_BLOCK_COMMENT_END : '*)' { raiseError("Unmatched *)"); };

WS : [ \n\f\r\t]+ -> skip;

INVALID_CHAR : .
    {
        String c = getText();
        raiseError("Invalid character: " + c);
    };
