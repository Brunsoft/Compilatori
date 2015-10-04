package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import types.Type;

/**
 * A bytecode that pops the top value of the stack.
 * <br><br>
 * ..., value -&gt; ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class POP extends SequentialBytecode {

	/**
	 * The type of the value that is popped from the stack.
	 */

	private final Type type;

	/**
	 * Constructs a bytecode that pops the top value of the stack.
	 *
	 * @param type the type of the value that is popped from the stack
	 */

	public POP(Type type) {
		this.type = type;
	}

	/**
	 * Yields the type of the value that is popped off the stack.
	 *
	 * @return the type of the value that is popped off the stack
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "pop " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return a Java {@code pop} bytecode
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		return new InstructionList(InstructionFactory.POP);
	}
}