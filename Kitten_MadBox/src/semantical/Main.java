package semantical;

import java.io.IOException;

import types.ClassType;
import errorMsg.ErrorMsg;

public class Main {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("You must specify a Kitten class name to compile");
			return;
		}

		// we build the class type for the file name passed as a parameter.
		// This triggers type-checking of that class and all those referenced from it
		ErrorMsg errorMsg = ClassType.mkFromFileName(args[0]).getErrorMsg();

		System.out.println("End of the semantical analysis");

		// we dump the set of classes that have been created
		if (errorMsg != null && !errorMsg.anyErrors())
			for (ClassType clazz: ClassType.getAll())
				try {
					clazz.dumpDot();
					System.out.println("Dumped " + clazz + ".dot");
				}
				catch (IOException e) {
					System.out.println("Cannot dump " + clazz + ".dot");
				}
	}
}