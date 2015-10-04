package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import types.Type;

/**
 * A bytecode which reads an element of an array and pushes its value on the
 * stack. If the reference to the array is {@code nil}, the computation stops.
 * <br><br>
 * ..., array reference, index -&gt; ..., value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ARRAYLOAD extends SequentialBytecode {

	/**
	 * The type of the elements of the array which is accessed.
	 */

	private final Type type;

	/**
	 * Constructs a bytecode that reads an element of an array and pushes
	 * its value on the stack. The index, where the array
	 * is read, is provided at runtime through the stack.
	 *
	 * @param type the type of the elements of the array that is read by this bytecode
	 */

	public ARRAYLOAD(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "load from array of " + type;
	}

	/**
	 * Yields the type of the elements of the array.
	 *
	 * @return the type of the elements of the array
	 */

	public Type getType() {
		return type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode. Namely,
	 * it generates an {@code iaload} if {@link #type} is {@code int}, an {@code faload} if
	 * {@link #type} is {@code float} and an {@code aaload} if {@link #type} is a class or array type.
	 *
	 * @param classGen the Java class generator to be used for this Java bytecode generation
	 * @return the Java {@code iaload}, {@code faload} or {@code aaload}
	 *         bytecode, depending on {@link #type}
	 */

	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the choice among more possible Java bytecodes
		return new InstructionList(InstructionFactory.createArrayLoad(type.toBCEL()));
	}
}