package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.F2I;
import org.apache.bcel.generic.I2F;
import org.apache.bcel.generic.InstructionList;

import types.FloatType;
import types.IntType;
import types.NumericalType;
import types.ReferenceType;
import types.Type;

/**
 * A bytecode that casts the top of the stack into a given type.
 * If the cast is not possible, the program stops.
 * A reference value can only be cast towards its type or a subtype of
 * its type. In that case, the cast value is original value, unmodified.
 * The value {@code nil} can be cast towards any reference type and remains
 * unmodified. A numerical type can be cast into any numerical type through
 * a type conversion. No other casts are possible.
 * <br><br>
 * ..., value -&gt; ..., cast value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class CAST extends SequentialBytecode {

	/**
	 * The original, static type of the top of the stack.
	 */

	private final Type fromType;

	/**
	 * The type the top of the stack is cast into.
	 */

	private final Type intoType;

	/**
	 * Constructs a bytecode that casts the top of the stack into the given type.
	 *
	 * @param fromType the declared semantical type of the top of the stack
	 * @param intoType the semantical type the top of the stack is cast into
	 */

	public CAST(Type fromType, ReferenceType intoType) {
		this.fromType = fromType;
		this.intoType = intoType;
	}

	/**
	 * Constructs a bytecode that casts the top of the stack into the given type.
	 * They are both numericla types.
	 *
	 * @param fromType the declared semantical type of the top of the stack
	 * @param intoType the semantical type the top of the stack is cast into
	 */

	public CAST(NumericalType fromType, NumericalType intoType) {
		this.fromType = fromType;
		this.intoType = intoType;
	}

	/**
	 * Yields the type from which the value is cast.
	 *
	 * @return the type from which the value is cast
	 */

	public Type getFromType() {
		return fromType;
	}

	/**
	 * Yields the type towards which the value is cast.
	 *
	 * @return the type towards which the value is cast
	 */

	public Type getIntoType() {
		return intoType;
	}

	@Override
	public String toString() {
		return "cast " + fromType + " into " + intoType;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code checkcast intoType} bytecode for casts between
	 *         reference types and a type conversion bytecode such as {@code i2f}
	 *         for conversions between numercial types
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		if (intoType instanceof ReferenceType)
			// we use the instruction factory to simplify the addition of the type to the constant pool
			return new InstructionList
				(classGen.getFactory().createCheckCast
					((org.apache.bcel.generic.ReferenceType) intoType.toBCEL()));
		else if (fromType == IntType.INSTANCE && intoType == FloatType.INSTANCE)
			return new InstructionList(new I2F());
		else // it must be float into int
			return new InstructionList(new F2I());
	}
}