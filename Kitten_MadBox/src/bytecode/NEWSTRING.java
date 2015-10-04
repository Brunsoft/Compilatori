package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

/**
 * A bytecode that creates a string and pushes a reference to it on top of the stack.
 * <br><br>
 * ... -&gt; ..., new string
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NEWSTRING extends SequentialBytecode {

	/**
	 * The lexical value of the string that is created.
	 */

	private final String value;

	/**
	 * Constructs a bytecode that creates a string and pushes a reference to it un the stack.
	 *
	 * @param value the lexical value of string
	 */

	public NEWSTRING(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "newstring " + value.replaceAll("\n","\\\\\\\\n");
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode. Kitten strings
	 * are emulated through {@code runTime.String} wrappers of Java bytecode strings. This way,
	 * all methods over Kitten strings can be emulated through Java methods
	 * inside that class (see there). Namely, this method generates the Java bytecode<br>
	 * <br>
	 * {@code new runTime.String}<br>
	 * {@code dup}<br>
	 * {@code ldc value}<br>
	 * {@code invokespecial runTime.String.&lt;init&gt;}<br>
	 * <br>
	 * that creates a {@code runTime.String} objects and initialises it with
	 * the lexical value {@link #value} of the Kitten string we want to create.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return a Java bytecode that creates a {@code runTime.String}
	 *         object initialised with the lexical {@link #value} of the
	 *         Kitten string that we want to create.
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		InstructionFactory factory = classGen.getFactory();
		InstructionList il = new InstructionList();
		String kittenStringName = runTime.String.class.getName();

		// we create the invokespecial to the constructor
		il.insert(factory.createInvoke
			(kittenStringName, // class name of the method
			Constants.CONSTRUCTOR_NAME, // name of the method
			org.apache.bcel.generic.Type.VOID, // return type
			new org.apache.bcel.generic.Type[] // parameters types
				{ org.apache.bcel.generic.Type.getType("Ljava/lang/String;") },
			Constants.INVOKESPECIAL)); // invokespecial

		// we prepend the rest of the code
		il.insert(factory.createConstant(value));
		il.insert(InstructionFactory.DUP);
		il.insert(factory.createNew(kittenStringName));

		return il;
	}
}