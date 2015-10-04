package javaBytecodeGenerator;

import errorMsg.ErrorMsg;
import types.ClassType;
import translation.Program;

public class Main {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("You must specify a Kitten class name to compile");
			return;
		}

		long totalTime = System.currentTimeMillis();

		// we build the class type for the file name passed as a parameter.
		// This triggers type-checking of that class and of all classes referenced from it
		long time = System.currentTimeMillis();
		ClassType clazz = ClassType.mkFromFileName(args[0]);
		ErrorMsg errorMsg = clazz.getErrorMsg();
		System.out.println("Parsing and type-checking completed     \t[" + (System.currentTimeMillis() - time) + "ms]");

		// if this class did not parse and type-check correctly,
		// we cannot translate the program into intermediate Kitten code
		if (errorMsg != null && !errorMsg.anyErrors()) {
			time = System.currentTimeMillis();
			// we translate this class into Kitten bytecode
			Program program = clazz.translate();

			System.out.println("Translation into Kitten bytecode completed \t[" + (System.currentTimeMillis() - time) + "ms]");

			time = System.currentTimeMillis();
			program.dumpCodeDot();

			System.out.println("Kitten bytecode dumping in dot format completed\t[" + (System.currentTimeMillis() - time) + "ms]");

			// we translate, into Java bytecode, the Kitten code of every class member which
		    // is reachable from the class we translated. This also generates the class files
		    program.generateJavaBytecode();
		    program.generateJavaBytecodeForTest();

		    System.out.println("Java bytecode generation completed       \t[" + (System.currentTimeMillis() - time) + "ms]");
		}

		System.out.println("Total compilation time was " + (System.currentTimeMillis() - totalTime) + "ms");
	}
}