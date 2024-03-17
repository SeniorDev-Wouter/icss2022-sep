grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';




//--- PARSER: ---
stylesheet: cssdefinition*;

cssdefinition: cssclass | variabledefinition;
variabledefinition: variable ASSIGNMENT_OPERATOR expression SEMICOLON;
cssclass: ident OPEN_BRACE entry+ CLOSE_BRACE;
variable: LOWER_IDENT | CAPITAL_IDENT;
ident: LOWER_IDENT | CAPITAL_IDENT | ID_IDENT | CLASS_IDENT;
entry: (LOWER_IDENT COLON expression SEMICOLON) | ifstatement | elsestatement;
expression: value | operation+;
value: COLOR | PIXELSIZE | variable | TRUE | FALSE | SCALAR;
operation: (value operator)+ value;
operator: PLUS | MIN | MUL;
ifstatement: IF BOX_BRACKET_OPEN variable BOX_BRACKET_CLOSE OPEN_BRACE entry+ CLOSE_BRACE;
elsestatement: ELSE OPEN_BRACE entry+ CLOSE_BRACE;