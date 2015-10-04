package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.Type;

/**
 * A bytecode that duplicates the top element of the stack.
 * <br><br>
 * ..., value -&gt; ..., value, value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class DUP extends SequentialBytecode {

	/**
	 * The type of the element which is duplicated.
	 */

	private final Type type;

	/**
	 * Constructs a bytecode that duplicates the top element of the stack.
	 *
	 * @param type the type of the element that is duplicated
	 */

	public DUP(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "dup " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code dup} bytecode
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		return new InstructionList(new org.apache.bcel.generic.DUP());
	}
}