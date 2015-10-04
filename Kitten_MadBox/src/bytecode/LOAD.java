package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import types.Type;

/**
 * A bytecode that loads the value of a local variable on top of the stack.
 * <br><br>
 * ... -&gt; ..., value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class LOAD extends SequentialBytecode {

	/**
	 * The number of the local variable which is loaded on the stack.
	 */

	private final int varNum;

	/**
	 * The type of the value contained in the local variable.
	 */

	private final Type type;

	/**
	 * Constructs a bytecode that loads the value of a local variable on top of the stack,
	 * followed by the given list of instructions.
	 *
	 * @param varNum the number of the local variable which is read
	 * @param type the type of the value loaded on top of the stack
	 */

	public LOAD(int varNum, Type type) {
		this.varNum = varNum;
		this.type = type;
	}

	/**
	 * Yields the number of the variable which is modified by this bytecode.
	 *
	 * @return the number of the variable modified by this bytecode
	 */

	public int getVarNum() {
		return varNum;
	}

	/**
	 * Yields the type of the loaded value.
	 *
	 * @return the type
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "load " + varNum + " of type " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this Java bytecode generation
	 * @return the {@code iload varNum}, {@code fload varNum} and {@code aload varNum} Java bytecode,
	 *         if {@link #type} is {@code int}, {@code float} or a reference type, respectively
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the choice between the three Java bytecode
		return new InstructionList(InstructionFactory.createLoad(type.toBCEL(), varNum));
	}
}