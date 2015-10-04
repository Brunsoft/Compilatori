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


class Lexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Lexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Lexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Lexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int COMMENT = 2;
	private final int yy_state_dtrans[] = {
		0,
		77,
		86
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NOT_ACCEPT,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"46:9,2,5,46,2,0,46:18,2,37,1,46:3,40,48,27,28,4,34,25,35,33,3,45:10,42,26,3" +
"8,36,39,46:2,43:26,29,47,30,46,44,46,21,23,17,16,10,11,43,7,8,43:2,9,14,18," +
"12,43:2,13,19,15,20,24,6,22,43:2,31,41,32,46:2,49:2")[0];

	private int yy_rmap[] = unpackFromString(1,157,
"0,1:2,2,3,1,4,1:4,5,1:3,6,1:3,7,8,9,1:2,10,11,1:3,12,13,1,6,1:4,12:21,1:6,1" +
"4,1:7,6,15,1,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36" +
",37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61" +
",62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86" +
",87,88,89,90,12,91,92,93,94,95,96")[0];

	private int yy_nxt[][] = unpackFromString(97,50,
"-1,1,2,3,4,5,6,150,73,150,124,105,150,152,154,125,150,155,106,150:2,78,150," +
"156,126,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,150,74,25,74:3,2" +
"6,-1:54,27,-1:48,28,-1:52,150,127,150:17,-1:18,150:3,-1:34,31,-1:64,32,-1:4" +
"0,33,-1:49,34,-1:49,35,-1:49,36,-1:46,72,-1:11,25,-1:10,150:19,-1:18,150:3," +
"-1:10,150:13,140,150:5,-1:18,150:3,-1:52,66,-1:7,150:5,29,150:6,81,150:6,-1" +
":18,150:3,-1:5,61,80,-1:2,80,-1:9,62,-1:2,63,-1:26,82,-1,64,-1:6,70,-1:46,5" +
"8,59:3,60,59:41,75,59,26,-1:6,150:13,30,150:5,-1:18,150:3,-1:7,71,-1:48,80," +
"-1:2,80,-1:41,65,-1:8,150:9,37,150:9,-1:18,150:3,-1:49,84,-1:10,150:7,38,15" +
"0:11,-1:18,150:3,-1:49,67,-1:10,150:3,39,150:15,-1:18,150:3,-1:5,68:2,76,79" +
",69,68:43,26,-1:6,40,150:18,-1:18,150:3,-1:10,150:4,41,150:14,-1:18,150:3,-" +
"1:10,150:12,42,150:6,-1:18,150:3,-1:10,150:9,43,150:9,-1:18,150:3,-1:10,150" +
":4,44,150:14,-1:18,150:3,-1:10,150:10,45,150:8,-1:18,150:3,-1:10,150:4,46,1" +
"50:14,-1:18,150:3,-1:10,150:10,47,150:8,-1:18,150:3,-1:10,150:9,48,150:9,-1" +
":18,150:3,-1:10,150:4,49,150:14,-1:18,150:3,-1:10,150:13,50,150:5,-1:18,150" +
":3,-1:10,150:12,51,150:6,-1:18,150:3,-1:10,150:10,52,150:8,-1:18,150:3,-1:1" +
"0,150:9,53,150:9,-1:18,150:3,-1:10,150:13,54,150:5,-1:18,150:3,-1:10,150:4," +
"55,150:14,-1:18,150:3,-1:10,150:12,56,150:6,-1:18,150:3,-1:10,150:7,57,150:" +
"11,-1:18,150:3,-1:10,150:2,129,130,150:2,83,150:8,131,150:3,-1:18,150:3,-1:" +
"10,150:2,85,150,87,150:14,-1:18,150:3,-1:10,150:13,88,150:5,-1:18,150:3,-1:" +
"10,150:4,89,150:14,-1:18,150:3,-1:10,150:13,90,150:5,-1:18,150:3,-1:10,150:" +
"14,91,150:4,-1:18,150:3,-1:10,150:2,92,150:16,-1:18,150:3,-1:10,150:3,93,15" +
"0:15,-1:18,150:3,-1:10,150:3,94,150:15,-1:18,150:3,-1:10,150:15,95,150:3,-1" +
":18,150:3,-1:10,150:13,96,150:5,-1:18,150:3,-1:10,150:13,97,150:5,-1:18,150" +
":3,-1:10,150:7,98,150:11,-1:18,150:3,-1:10,150:6,99,150:12,-1:18,150:3,-1:1" +
"0,150:7,100,150:11,-1:18,150:3,-1:10,150:10,101,150:8,-1:18,150:3,-1:10,150" +
":7,102,150:11,-1:18,150:3,-1:10,150:15,103,150:3,-1:18,150:3,-1:10,150:6,10" +
"4,150:12,-1:18,150:3,-1:10,150:3,107,150:12,128,150:2,-1:18,150:3,-1:10,150" +
",108,150:2,109,150:2,110,150:11,-1:18,150:3,-1:10,150:6,111,150:12,-1:18,15" +
"0:3,-1:10,150:2,112,150:16,-1:18,150:3,-1:10,150:9,135,150:9,-1:18,150:3,-1" +
":10,150:4,113,150:11,136,150:2,-1:18,150:3,-1:10,150:6,114,150:12,-1:18,150" +
":3,-1:10,150:3,115,150:15,-1:18,150:3,-1:10,150:15,116,150:3,-1:18,150:3,-1" +
":10,150:12,139,150:6,-1:18,150:3,-1:10,150:6,141,150:12,-1:18,150:3,-1:10,1" +
"50:4,142,150:14,-1:18,150:3,-1:10,150:9,143,150:9,-1:18,150:3,-1:10,150:14," +
"117,150:4,-1:18,150:3,-1:10,150,118,150:17,-1:18,150:3,-1:10,150:13,144,150" +
":5,-1:18,150:3,-1:10,150:4,119,150:14,-1:18,150:3,-1:10,150:3,145,150:15,-1" +
":18,150:3,-1:10,150:12,120,150:6,-1:18,150:3,-1:10,150:14,121,150:4,-1:18,1" +
"50:3,-1:10,150:9,146,150:9,-1:18,150:3,-1:10,150:4,122,150:14,-1:18,150:3,-" +
"1:10,150:7,147,150:11,-1:18,150:3,-1:10,150:14,148,150:4,-1:18,150:3,-1:10," +
"150:11,149,150:7,-1:18,150:3,-1:10,150:9,123,150:9,-1:18,150:3,-1:10,150:9," +
"137,150:9,-1:18,150:3,-1:10,150:4,151,150:14,-1:18,150:3,-1:10,150:9,138,15" +
"0:9,-1:18,150:3,-1:10,150:4,153,150:14,-1:18,150:3,-1:10,150:3,132,150:2,13" +
"3,150:12,-1:18,150:3,-1:10,150:6,134,150:12,-1:18,150:3,-1:4");

	public java_cup.runtime.Symbol nextToken ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

	{
	  if (commentCount != 0) err("Unclosed comment");
	  return tok(sym.EOF, null);
        }
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						{myString=""; yybegin(STRING);}
					case -2:
						break;
					case 2:
						{}
					case -3:
						break;
					case 3:
						{return tok(sym.DIVIDE, null);}
					case -4:
						break;
					case 4:
						{return tok(sym.TIMES, null);}
					case -5:
						break;
					case 5:
						{newline();}
					case -6:
						break;
					case 6:
						{return tok(sym.ID, yytext());}
					case -7:
						break;
					case 7:
						{return tok(sym.COMMA, null);}
					case -8:
						break;
					case 8:
						{return tok(sym.SEMICOLON, null);}
					case -9:
						break;
					case 9:
						{return tok(sym.LPAREN, null);}
					case -10:
						break;
					case 10:
						{return tok(sym.RPAREN, null);}
					case -11:
						break;
					case 11:
						{return tok(sym.LBRACK, null);}
					case -12:
						break;
					case 12:
						{return tok(sym.RBRACK, null);}
					case -13:
						break;
					case 13:
						{return tok(sym.LBRACE, null);}
					case -14:
						break;
					case 14:
						{return tok(sym.RBRACE, null);}
					case -15:
						break;
					case 15:
						{return tok(sym.DOT, null);}
					case -16:
						break;
					case 16:
						{return tok(sym.PLUS, null);}
					case -17:
						break;
					case 17:
						{return tok(sym.MINUS, null);}
					case -18:
						break;
					case 18:
						{return tok(sym.EQ, null);}
					case -19:
						break;
					case 19:
						{return tok(sym.NOT, null);}
					case -20:
						break;
					case 20:
						{return tok(sym.LT, null);}
					case -21:
						break;
					case 21:
						{return tok(sym.GT, null);}
					case -22:
						break;
					case 22:
						{return tok(sym.AND, null);}
					case -23:
						break;
					case 23:
						{return tok(sym.OR, null);}
					case -24:
						break;
					case 24:
						{err("Unmatched input");}
					case -25:
						break;
					case 25:
						{return tok(sym.INTEGER, new Integer(yytext()));}
					case -26:
						break;
					case 26:
						
					case -27:
						break;
					case 27:
						{commentCount++; yybegin(COMMENT);}
					case -28:
						break;
					case 28:
						{err("Unopen comment");}
					case -29:
						break;
					case 29:
						{return tok(sym.IF, null);}
					case -30:
						break;
					case 30:
						{return tok(sym.AS, null);}
					case -31:
						break;
					case 31:
						{return tok(sym.ARRAYSYMBOL, null);}
					case -32:
						break;
					case 32:
						{return tok(sym.FLOATING, new Float(yytext()));}
					case -33:
						break;
					case 33:
						{return tok(sym.NEQ, null);}
					case -34:
						break;
					case 34:
						{return tok(sym.LE, null);}
					case -35:
						break;
					case 35:
						{return tok(sym.GE, null);}
					case -36:
						break;
					case 36:
						{return tok(sym.ASSIGN, null);}
					case -37:
						break;
					case 37:
						{return tok(sym.INT, null);}
					case -38:
						break;
					case 38:
						{return tok(sym.FOR, null);}
					case -39:
						break;
					case 39:
						{return tok(sym.NIL, null);}
					case -40:
						break;
					case 40:
						{return tok(sym.NEW, null);}
					case -41:
						break;
					case 41:
						{return tok(sym.ELSE, null);}
					case -42:
						break;
					case 42:
						{return tok(sym.THEN, null);}
					case -43:
						break;
					case 43:
						{return tok(sym.TEST, null);}
					case -44:
						break;
					case 44:
						{return tok(sym.TRUE, null);}
					case -45:
						break;
					case 45:
						{return tok(sym.VOID, null);}
					case -46:
						break;
					case 46:
						{return tok(sym.WHILE, null);}
					case -47:
						break;
					case 47:
						{return tok(sym.FIELD, null);}
					case -48:
						break;
					case 48:
						{return tok(sym.FLOAT, null);}
					case -49:
						break;
					case 49:
						{return tok(sym.FALSE, null);}
					case -50:
						break;
					case 50:
						{return tok(sym.CLASS, null);}
					case -51:
						break;
					case 51:
						{return tok(sym.RETURN, null);}
					case -52:
						break;
					case 52:
						{return tok(sym.METHOD, null);}
					case -53:
						break;
					case 53:
						{return tok(sym.ASSERT, null);}
					case -54:
						break;
					case 54:
						{return tok(sym.EXTENDS, null);}
					case -55:
						break;
					case 55:
						{return tok(sym.FIXTURE, null);}
					case -56:
						break;
					case 56:
						{return tok(sym.BOOLEAN, null);}
					case -57:
						break;
					case 57:
						{return tok(sym.CONSTRUCTOR, null);}
					case -58:
						break;
					case 58:
						{yybegin(YYINITIAL); return tok(sym.STRING, myString);}
					case -59:
						break;
					case 59:
						{myString += yytext();}
					case -60:
						break;
					case 60:
						{newline(); myString += "\n";}
					case -61:
						break;
					case 61:
						{myString += "\"";}
					case -62:
						break;
					case 62:
						{myString+="\t";}
					case -63:
						break;
					case 63:
						{myString+="\n";}
					case -64:
						break;
					case 64:
						{myString += "\\";}
					case -65:
						break;
					case 65:
						{}
					case -66:
						break;
					case 66:
						{myString += "'";}
					case -67:
						break;
					case 67:
						{myNum=(yytext().charAt(1)-48)*100+
			   				(yytext().charAt(2)-48)*10+
                            (yytext().charAt(3)-48);
                        if (myNum>255) err("Overflow in ASCII Code");
                        else myString += (char)myNum;}
					case -68:
						break;
					case 68:
						{}
					case -69:
						break;
					case 69:
						{newline();}
					case -70:
						break;
					case 70:
						{commentCount++;}
					case -71:
						break;
					case 71:
						{commentCount--;
                         if (commentCount==0) yybegin(YYINITIAL);}
					case -72:
						break;
					case 73:
						{return tok(sym.ID, yytext());}
					case -73:
						break;
					case 74:
						{err("Unmatched input");}
					case -74:
						break;
					case 75:
						{myString += yytext();}
					case -75:
						break;
					case 76:
						{}
					case -76:
						break;
					case 78:
						{return tok(sym.ID, yytext());}
					case -77:
						break;
					case 79:
						{}
					case -78:
						break;
					case 81:
						{return tok(sym.ID, yytext());}
					case -79:
						break;
					case 83:
						{return tok(sym.ID, yytext());}
					case -80:
						break;
					case 85:
						{return tok(sym.ID, yytext());}
					case -81:
						break;
					case 87:
						{return tok(sym.ID, yytext());}
					case -82:
						break;
					case 88:
						{return tok(sym.ID, yytext());}
					case -83:
						break;
					case 89:
						{return tok(sym.ID, yytext());}
					case -84:
						break;
					case 90:
						{return tok(sym.ID, yytext());}
					case -85:
						break;
					case 91:
						{return tok(sym.ID, yytext());}
					case -86:
						break;
					case 92:
						{return tok(sym.ID, yytext());}
					case -87:
						break;
					case 93:
						{return tok(sym.ID, yytext());}
					case -88:
						break;
					case 94:
						{return tok(sym.ID, yytext());}
					case -89:
						break;
					case 95:
						{return tok(sym.ID, yytext());}
					case -90:
						break;
					case 96:
						{return tok(sym.ID, yytext());}
					case -91:
						break;
					case 97:
						{return tok(sym.ID, yytext());}
					case -92:
						break;
					case 98:
						{return tok(sym.ID, yytext());}
					case -93:
						break;
					case 99:
						{return tok(sym.ID, yytext());}
					case -94:
						break;
					case 100:
						{return tok(sym.ID, yytext());}
					case -95:
						break;
					case 101:
						{return tok(sym.ID, yytext());}
					case -96:
						break;
					case 102:
						{return tok(sym.ID, yytext());}
					case -97:
						break;
					case 103:
						{return tok(sym.ID, yytext());}
					case -98:
						break;
					case 104:
						{return tok(sym.ID, yytext());}
					case -99:
						break;
					case 105:
						{return tok(sym.ID, yytext());}
					case -100:
						break;
					case 106:
						{return tok(sym.ID, yytext());}
					case -101:
						break;
					case 107:
						{return tok(sym.ID, yytext());}
					case -102:
						break;
					case 108:
						{return tok(sym.ID, yytext());}
					case -103:
						break;
					case 109:
						{return tok(sym.ID, yytext());}
					case -104:
						break;
					case 110:
						{return tok(sym.ID, yytext());}
					case -105:
						break;
					case 111:
						{return tok(sym.ID, yytext());}
					case -106:
						break;
					case 112:
						{return tok(sym.ID, yytext());}
					case -107:
						break;
					case 113:
						{return tok(sym.ID, yytext());}
					case -108:
						break;
					case 114:
						{return tok(sym.ID, yytext());}
					case -109:
						break;
					case 115:
						{return tok(sym.ID, yytext());}
					case -110:
						break;
					case 116:
						{return tok(sym.ID, yytext());}
					case -111:
						break;
					case 117:
						{return tok(sym.ID, yytext());}
					case -112:
						break;
					case 118:
						{return tok(sym.ID, yytext());}
					case -113:
						break;
					case 119:
						{return tok(sym.ID, yytext());}
					case -114:
						break;
					case 120:
						{return tok(sym.ID, yytext());}
					case -115:
						break;
					case 121:
						{return tok(sym.ID, yytext());}
					case -116:
						break;
					case 122:
						{return tok(sym.ID, yytext());}
					case -117:
						break;
					case 123:
						{return tok(sym.ID, yytext());}
					case -118:
						break;
					case 124:
						{return tok(sym.ID, yytext());}
					case -119:
						break;
					case 125:
						{return tok(sym.ID, yytext());}
					case -120:
						break;
					case 126:
						{return tok(sym.ID, yytext());}
					case -121:
						break;
					case 127:
						{return tok(sym.ID, yytext());}
					case -122:
						break;
					case 128:
						{return tok(sym.ID, yytext());}
					case -123:
						break;
					case 129:
						{return tok(sym.ID, yytext());}
					case -124:
						break;
					case 130:
						{return tok(sym.ID, yytext());}
					case -125:
						break;
					case 131:
						{return tok(sym.ID, yytext());}
					case -126:
						break;
					case 132:
						{return tok(sym.ID, yytext());}
					case -127:
						break;
					case 133:
						{return tok(sym.ID, yytext());}
					case -128:
						break;
					case 134:
						{return tok(sym.ID, yytext());}
					case -129:
						break;
					case 135:
						{return tok(sym.ID, yytext());}
					case -130:
						break;
					case 136:
						{return tok(sym.ID, yytext());}
					case -131:
						break;
					case 137:
						{return tok(sym.ID, yytext());}
					case -132:
						break;
					case 138:
						{return tok(sym.ID, yytext());}
					case -133:
						break;
					case 139:
						{return tok(sym.ID, yytext());}
					case -134:
						break;
					case 140:
						{return tok(sym.ID, yytext());}
					case -135:
						break;
					case 141:
						{return tok(sym.ID, yytext());}
					case -136:
						break;
					case 142:
						{return tok(sym.ID, yytext());}
					case -137:
						break;
					case 143:
						{return tok(sym.ID, yytext());}
					case -138:
						break;
					case 144:
						{return tok(sym.ID, yytext());}
					case -139:
						break;
					case 145:
						{return tok(sym.ID, yytext());}
					case -140:
						break;
					case 146:
						{return tok(sym.ID, yytext());}
					case -141:
						break;
					case 147:
						{return tok(sym.ID, yytext());}
					case -142:
						break;
					case 148:
						{return tok(sym.ID, yytext());}
					case -143:
						break;
					case 149:
						{return tok(sym.ID, yytext());}
					case -144:
						break;
					case 150:
						{return tok(sym.ID, yytext());}
					case -145:
						break;
					case 151:
						{return tok(sym.ID, yytext());}
					case -146:
						break;
					case 152:
						{return tok(sym.ID, yytext());}
					case -147:
						break;
					case 153:
						{return tok(sym.ID, yytext());}
					case -148:
						break;
					case 154:
						{return tok(sym.ID, yytext());}
					case -149:
						break;
					case 155:
						{return tok(sym.ID, yytext());}
					case -150:
						break;
					case 156:
						{return tok(sym.ID, yytext());}
					case -151:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
