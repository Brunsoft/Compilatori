package syntactical;

import java.io.FileWriter;
import java.io.IOException;

import java_cup.runtime.Symbol;
import lexical.Lexer;
import absyn.ClassDefinition;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			System.out.println("You must specify a Kitten class name to compile");
		else
			try {
				String fileName = args[0];
				Parser parser = new Parser(new Lexer(fileName));
				Symbol symbol = parser.parse();
				System.out.println("End of the syntactical analysis");

				ClassDefinition absyn = (ClassDefinition) symbol.value;
				if (absyn != null) {
					String dotName = fileName.substring(0, fileName.length() - ".kit".length()) + ".dot";
					try (FileWriter file = new FileWriter(dotName)) {
						absyn.toDot(file);
					}
					System.out.println("Abstract syntax saved into " + dotName);
				}
				else
					System.out.println("Null semantical value");
			}
			catch (IOException e) {
				System.out.println("I/O error");
			}
			catch (Error e) {
				System.out.println("Unmatched input");
			}
	}
}