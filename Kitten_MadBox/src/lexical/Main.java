package lexical;

import java.io.FileNotFoundException;
import java.io.IOException;

import java_cup.runtime.Symbol;

import syntactical.sym;
import lexical.Lexer;

public class Main {
	public static void main(String args[]) {
		if (args.length == 0) {
			System.out.println("You must specify a Kitten class name to compile");
			return;
		}

		String fileName = args[0];
		Lexer lexer;
		Symbol tok;

		try {
			lexer = new Lexer(fileName);
		}
		catch (FileNotFoundException e) {
			System.out.println("Cannot find " + fileName);
			return;
		}

		do {
			try {
				tok = lexer.nextToken();
			}
			catch (Error e) {
				System.out.println("Lexical error: unmatched input");
				return;
			}
			catch (IOException e) {
				System.out.println("I/O error");
				return;
			}

			System.out.print(tok.sym + ":\t " + symnames[tok.sym]);

			if (tok.value != null)
				System.out.print("(" + tok.value + ")");

			System.out.println(" from " + tok.left + " to " + (tok.right - 1));
		}
		while (tok.sym != sym.EOF);
	}

	static String symnames[] = new String[100];

	static {     
		symnames[sym.EOF] = "EOF";
		symnames[sym.INT] = "INT";
		symnames[sym.GT] = "GT";
		symnames[sym.DIVIDE] = "DIVIDE";
		symnames[sym.ELSE] = "ELSE";
		symnames[sym.OR] = "OR";
		symnames[sym.NIL] = "NIL";
		symnames[sym.GE] = "GE";
		symnames[sym.error] = "error";
		symnames[sym.LT] = "LT";
		symnames[sym.AS] = "AS";
		symnames[sym.MINUS] = "MINUS";
		symnames[sym.ARRAYSYMBOL] = "ARRAYSYMBOL";
		symnames[sym.FOR] = "FOR";
		symnames[sym.TIMES] = "TIMES";
		symnames[sym.COMMA] = "COMMA";
		symnames[sym.LE] = "LE";
		symnames[sym.ASSIGN] = "ASSIGN";
		symnames[sym.STRING] = "STRING";
		symnames[sym.DOT] = "DOT";
		symnames[sym.LPAREN] = "LPAREN";
		symnames[sym.RPAREN] = "RPAREN";
		symnames[sym.IF] = "IF";
		symnames[sym.SEMICOLON] = "SEMICOLON";
		symnames[sym.ID] = "ID";
		symnames[sym.WHILE] = "WHILE";
		symnames[sym.LBRACK] = "LBRACK";
		symnames[sym.RBRACK] = "RBRACK";
		symnames[sym.AND] = "AND";
		symnames[sym.NOT] = "NOT";
		symnames[sym.PLUS] = "PLUS";
		symnames[sym.LBRACE] = "LBRACE";
		symnames[sym.RBRACE] = "RBRACE";
		symnames[sym.THEN] = "THEN";
		symnames[sym.EQ] = "EQ";
		symnames[sym.NEQ] = "NEQ";
		symnames[sym.RETURN] = "RETURN";
		symnames[sym.VOID] = "VOID";
		symnames[sym.FLOAT] = "FLOAT";
		symnames[sym.FLOATING] = "FLOATING";
		symnames[sym.BOOLEAN] = "BOOLEAN";
		symnames[sym.INTEGER] = "INTEGER";
		symnames[sym.CLASS] = "CLASS";
		symnames[sym.FIELD] = "FIELD";
		symnames[sym.METHOD] = "METHOD";
		symnames[sym.CONSTRUCTOR] = "CONSTRUCTOR";
		symnames[sym.EXTENDS] = "EXTENDS";
		symnames[sym.TRUE] = "TRUE";
		symnames[sym.FALSE] = "FALSE";
		symnames[sym.NEW] = "NEW";
		symnames[sym.TEST] = "TEST";

	}
}