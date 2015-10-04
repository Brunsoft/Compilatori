package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import types.Type;

/**
 * A bytecode that stores the top of the stack inside a local variable.
 * <br><br>
 * ..., value -&gt; ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class STORE extends SequentialBytecode {

	/**
	 * The number of the local variable that is assigned.
	 */

	private final int varNum;

	/**
	 * The type of the value stored in the local variable.
	 */

	private final Type type;

	/**
	 * Constructs a bytecode that stores the value on top of the stack inside a local variable
	 *
	 * @param varNum the number of the local variable that is assigned
	 * @param type the type of the value stored inside the local variable
	 */

	public STORE(int varNum, Type type) {
		this.varNum = varNum;
		this.type = type;
	}

	/**
	 * Yields the type of the local variable that is assigned.
	 *
	 * @return the type of the local variable that is assigned
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "store " + varNum + " of type " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code istore varNum}, {@code fstore varNum} or
	 *         {@code astore varNum} bytecode, if {@link #type} is {@code int},
	 *         {@code float} or a class or array type, respectively.
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the choice between the three Java bytecodes
		return new InstructionList(InstructionFactory.createStore(type.toBCEL(), varNum));
	}
}