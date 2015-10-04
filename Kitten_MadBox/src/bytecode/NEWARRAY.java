package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.ArrayType;
import types.Type;

/**
 * A bytecode that creates an array of a given type and size and then
 * pushes on top of the stack a reference to that array.
 * <br><br>
 * ..., dim_11, ..., dim_n -&gt; ..., array reference
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NEWARRAY extends SequentialBytecode {

	/**
	 * The type of the elements of the array.
	 */

	private final Type elementsType;

	/**
	 * The number of dimensions of the array.
	 */

	private final int dimensions;

	/**
	 * Constructs a bytecode that creates an array of the given type and dimensions.
	 * Note that the size of the array is provided at runtime through the stack.
	 *
	 * @param elementsType the type of the elements of the array that is created by this bytecode
	 * @param dimensions the number of dimensions of the array
	 */

	public NEWARRAY(Type elementsType, int dimensions) {
		this.elementsType = elementsType;
		this.dimensions = dimensions;
	}

	/**
	 * Constructs a bytecode that creates an array of a given type and
	 * one dimension. Note that the size of the array
	 * is provided at runtime through the stack.
	 *
	 * @param elementsType the type of the elements of the array that is created by this bytecode
	 */

	public NEWARRAY(Type elementsType) {
		this(elementsType, 1);
	}

	/**
	 * Yields the number of dimensions of the array that is created by this bytecode.
	 *
	 * @return the number of dimensions
	 */

	public int getDimensions() {
		return dimensions;
	}

	@Override
	public String toString() {
		if (dimensions == 1)
			return "newarray of " + elementsType + " of 1 dimension";
		else
			return "newarray of " + elementsType + " of " + dimensions + " dimensions";
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the {@code newarray elementsType} Java bytecode, if
	 *         {@code elementsType} is a primitive types, and the
	 *         {@code anewarray elementsType} bytecode, if
	 *         {@code elementsType} is a reference type
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the choice between
		// the newarray and anewarray Java bytecode. Moreover, it automatically
		// puts the type of the elements of the array inside the constant pool

		// this is to cope with a bug in BCEL
		Type t = elementsType;
		if (dimensions > 1)
			t = ArrayType.mk(t);

		return new InstructionList(classGen.getFactory().createNewArray(t.toBCEL(), (short) dimensions));
	}
}