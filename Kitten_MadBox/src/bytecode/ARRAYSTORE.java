package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import types.Type;

/**
 * A bytecode that writes a value into an element of an array.
 * If the reference to the array is {@code nil}, the computation stops.
 * <br><br>
 * ..., array reference, index, value -&gt; ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ARRAYSTORE extends SequentialBytecode {

	/**
	 * The type of the elements of the array which is modified.
	 */

	private final Type type;

	/**
	 * Constructs a bytecode that writes a value into an element of an array. The index, where
	 * the array is modified, is provided at runtime through the stack.
	 *
	 * @param type the type of the elements of the array that is created by this bytecode
	 */

	public ARRAYSTORE(Type type) {
		this.type = type;
	}

	/**
	 * Yields the type of the elements of the array.
	 *
	 * @return the type of the elements of the array
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "store into array of " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode. Namely,
	 * it generates an {@code iastore} if {@link #type} is {@code nil}, an {@code fastore} if
	 * {@link #type} is {@code float} and an {@code aastore} if {@link #type} is a class or array type.
	 *
	 * @param classGen the Java class generator to be used for this Java bytecode generation
	 * @return the Java {@code iastore}, {@code fastore} or {@code aastore}
	 *         bytecode, depending on {@link #type}
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the choice among the possible Java bytecodes
		return new InstructionList(InstructionFactory.createArrayStore(type.toBCEL()));
	}
}