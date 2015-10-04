package lexical;

import java.io.IOException;

import JLex.CLexGen;

public class Generator {

	public static void main(String[] args) throws IOException {
		new CLexGen("resources/Kitten.lex", "src/lexical/Lexer").generate();
	}
}