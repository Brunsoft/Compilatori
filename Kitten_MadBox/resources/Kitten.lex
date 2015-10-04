package lexical;

/**
 * A lexical analyser.
 *
 * This Java code is automatically generated from the file
 * {@code resources/Lexer.lex} through the JLex utility.
 * The following author hence is not completely responsible for its content
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

import java.io.FileInputStream;

import errorMsg.ErrorMsg;
import syntactical.sym;

@SuppressWarnings("unused")

public

%% 

%class Lexer
%function nextToken
%type java_cup.runtime.Symbol
%char

%{

/**
 *
 * Records that a new line character has been found in the source file
 *
 */

private void newline() {
  errorMsg.newline(yychar);
}

/**
 *
 * Reports an error at a given position in the source file
 *
 * @param pos the position where the message must be reported,
 *            from the beginning of the source file
 * @param msg the message to be reported
 *
 */

private void err(int pos, String msg) {
  errorMsg.error(pos,msg);
}

/**
 *
 * Reports an error at the current position in the source file
 *
 * @param msg the message to be reported
 *
 */

private void err(String msg) {
  err(yychar, msg);
}

/**
 *
 * Creates a token of a given kind and with a given lexical value
 *
 * @param kind the kind of token to be created, as enumerated in
 *             {@code syntactical/sym.java}
 * @param value the lexical value associated with the token.
 *              It may be {@code null}
 *
 */

private java_cup.runtime.Symbol tok(int kind, Object value) {
    return new java_cup.runtime.Symbol(kind, yychar, yychar + yylength(), value);
}

/**
 *
 * The error reporting utility used during the compilation
 *
 */

private ErrorMsg errorMsg;

/**
 * Yields the error reporting utility used during the lexical analysis.
 *
 * @return the error reporting utility
 */

public ErrorMsg getErrorMsg() {
  return errorMsg;
}

/**
 * Creates a lexical analyser for a given class name.
 *
 * @param fileName the name of the file to be lexically analysed
 *                 (with the trailing {@code .kit})
 * @throws java.io.FileNotFoundException if the source file cannot be found
 */

public Lexer(String fileName) throws java.io.FileNotFoundException {
  this();
  
  String className = fileName.endsWith(".kit") ? fileName.substring(0, fileName.length() - 4) : fileName;
  fileName = className + ".kit";
  errorMsg = new ErrorMsg(fileName);
  FileInputStream inp;

  try {
    inp = new FileInputStream(fileName);
  }
  catch (java.io.FileNotFoundException e) {
    errorMsg.error(-1, "Cannot find \"" + fileName + "\"");
    throw e;
  }

  yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(inp));
}

// ritorna il simbolo della classe che si sta parsando
public String parsedClass() {
  return errorMsg.getFileName().substring(0, errorMsg.getFileName().length() - 4);
}

int commentCount = 0;
int myNum;
String myString = "";
%}

%eofval{
	{
	  if (commentCount != 0) err("Unclosed comment");
	  return tok(sym.EOF, null);
        }
%eofval}       

%state STRING
%state COMMENT

%%
<YYINITIAL>"\""         {myString=""; yybegin(STRING);}
<YYINITIAL>[ \t\f]		{}
<YYINITIAL>"/*"         {commentCount++; yybegin(COMMENT);}
<YYINITIAL>\n	        {newline();}
<YYINITIAL>while        {return tok(sym.WHILE, null);}
<YYINITIAL>for          {return tok(sym.FOR, null);}
<YYINITIAL>method       {return tok(sym.METHOD, null);}
<YYINITIAL>field        {return tok(sym.FIELD, null);}
<YYINITIAL>constructor  {return tok(sym.CONSTRUCTOR, null);}
<YYINITIAL>if           {return tok(sym.IF, null);}
<YYINITIAL>then         {return tok(sym.THEN, null);}
<YYINITIAL>else         {return tok(sym.ELSE, null);}
<YYINITIAL>as           {return tok(sym.AS, null);}
<YYINITIAL>nil          {return tok(sym.NIL, null);}
<YYINITIAL>class        {return tok(sym.CLASS, null);}
<YYINITIAL>extends      {return tok(sym.EXTENDS, null);}
<YYINITIAL>new          {return tok(sym.NEW, null);}
<YYINITIAL>return       {return tok(sym.RETURN, null);}
<YYINITIAL>boolean      {return tok(sym.BOOLEAN, null);}
<YYINITIAL>int          {return tok(sym.INT, null);}
<YYINITIAL>float        {return tok(sym.FLOAT, null);}
<YYINITIAL>void         {return tok(sym.VOID, null);}
<YYINITIAL>true         {return tok(sym.TRUE, null);}
<YYINITIAL>false        {return tok(sym.FALSE, null);}
<YYINITIAL>test         {return tok(sym.TEST, null);}
<YYINITIAL>assert       {return tok(sym.ASSERT, null);}
<YYINITIAL>fixture      {return tok(sym.FIXTURE, null);}
<YYINITIAL>","	        {return tok(sym.COMMA, null);}
<YYINITIAL>";"          {return tok(sym.SEMICOLON, null);}
<YYINITIAL>"("          {return tok(sym.LPAREN, null);}
<YYINITIAL>")"          {return tok(sym.RPAREN, null);}
<YYINITIAL>"[]"         {return tok(sym.ARRAYSYMBOL, null);}
<YYINITIAL>"["          {return tok(sym.LBRACK, null);}
<YYINITIAL>"]"          {return tok(sym.RBRACK, null);}
<YYINITIAL>"{"          {return tok(sym.LBRACE, null);}
<YYINITIAL>"}"          {return tok(sym.RBRACE, null);}
<YYINITIAL>"."          {return tok(sym.DOT, null);}
<YYINITIAL>"+"          {return tok(sym.PLUS, null);}
<YYINITIAL>"-"          {return tok(sym.MINUS, null);}
<YYINITIAL>"*"          {return tok(sym.TIMES, null);}
<YYINITIAL>"/"          {return tok(sym.DIVIDE, null);}
<YYINITIAL>"="          {return tok(sym.EQ, null);}
<YYINITIAL>"!="         {return tok(sym.NEQ, null);}
<YYINITIAL>"<"          {return tok(sym.LT, null);}
<YYINITIAL>"<="         {return tok(sym.LE, null);} 
<YYINITIAL>">="         {return tok(sym.GE, null);}
<YYINITIAL>">"          {return tok(sym.GT, null);}
<YYINITIAL>"&"          {return tok(sym.AND, null);}
<YYINITIAL>"|"          {return tok(sym.OR, null);}
<YYINITIAL>"!"          {return tok(sym.NOT, null);}
<YYINITIAL>":="         {return tok(sym.ASSIGN, null);}
<YYINITIAL>"*/"         {err("Unopen comment");}
<YYINITIAL>[a-zA-Z][a-zA-Z0-9_]*
                        {return tok(sym.ID, yytext());}
<YYINITIAL>[0-9]+       {return tok(sym.INTEGER, new Integer(yytext()));}
<YYINITIAL>[0-9]*"."[0-9]+
                        {return tok(sym.FLOATING, new Float(yytext()));}
<YYINITIAL>.            {err("Unmatched input");}

<STRING>\\n             {myString+="\n";}
<STRING>\\t             {myString+="\t";}
<STRING>\\[0-9][0-9][0-9]
                        {myNum=(yytext().charAt(1)-48)*100+
			   				(yytext().charAt(2)-48)*10+
                            (yytext().charAt(3)-48);
                        if (myNum>255) err("Overflow in ASCII Code");
                        else myString += (char)myNum;}
<STRING>\\\\            {myString += "\\";}
<STRING>\\[ \t\f\n]+\\  {}
<STRING>"\\""           {myString += "\"";}
<STRING>"\\'"           {myString += "'";}
<STRING>"\""            {yybegin(YYINITIAL); return tok(sym.STRING, myString);}
<STRING>\n	  		    {newline(); myString += "\n";}
<STRING>.               {myString += yytext();}

<COMMENT>"*/"           {commentCount--;
                         if (commentCount==0) yybegin(YYINITIAL);}
<COMMENT>"/*"           {commentCount++;}
<COMMENT>\n             {newline();}
<COMMENT>.              {}
