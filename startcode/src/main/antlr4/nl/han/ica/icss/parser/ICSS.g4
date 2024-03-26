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




//--- PARSER:
stylesheet: (stylerule | variableAssignment)* EOF;

declaration: propertyName COLON (literal|variableReference|operation+) SEMICOLON;

elseclause: ELSE OPEN_BRACE (declaration|ifclause)+ CLOSE_BRACE;

ifclause: IF BOX_BRACKET_OPEN (literal|variableReference) BOX_BRACKET_CLOSE OPEN_BRACE (variableAssignment|declaration|ifclause)+ CLOSE_BRACE elseclause?;

literal: TRUE #booltrue | FALSE #boolfalse | PIXELSIZE #pixelsize | PERCENTAGE #percentage| SCALAR #scalar | COLOR #color;

operation:(multiplyOperation | addOperation | subtractOperation)+;

propertyName: LOWER_IDENT;

selector: classSelector | idSelector | tagSelector;

stylerule: selector OPEN_BRACE (declaration|ifclause|variableAssignment)+ CLOSE_BRACE;

variableAssignment: variableReference ASSIGNMENT_OPERATOR ((literal|variableReference)|operation+) SEMICOLON;

variableReference: CAPITAL_IDENT;

classSelector: CLASS_IDENT;

idSelector: ID_IDENT;

tagSelector: LOWER_IDENT;

addOperation: (literal | variableReference) PLUS (literal | variableReference| operation);
multiplyOperation: (literal | variableReference) MUL (literal | variableReference| operation);
subtractOperation: (literal | variableReference) MIN (literal | variableReference| operation);
