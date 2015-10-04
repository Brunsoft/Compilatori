package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.ClassType;

/**
 * A bytecode that creates an object of a given class and pushes it on top of the stack.
 * Note that the constructor is not called on the newly created object.
 * <br><br>
 * ... -&gt; ..., new object
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NEW extends SequentialBytecode {

	/**
	 * The class type that is instantiated by this bytecode.
	 */

	private final ClassType clazz;

	/**
	 * Constructs a bytecode that creates an object of a given class.
	 *
	 * @param clazz the class type that is instantiated by this bytecode
	 */

	public NEW(ClassType clazz) {
		this.clazz = clazz;
	}

	/**
	 * Yields the class that is instantiated by this bytecode.
	 *
	 * @return the class instantiated by this bytecode
	 */

	public ClassType getType() {
		return clazz;
	}

	@Override
	public String toString() {
		return "new " + clazz;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code new clazz}
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		return new InstructionList(classGen.getFactory().createNew(clazz.toBCEL().toString()));
	}
}