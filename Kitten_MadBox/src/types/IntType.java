package types;

import org.apache.bcel.generic.InstructionList;

/**
 * The {@code int} type of the Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class IntType extends IntegralType {

	/**
	 * The integer type. Always use this constant to refer to the integer type,
	 * so that type comparison can be carried out by simple == tests.
	 */

	public final static IntType INSTANCE = new IntType();

	/**
	 * Builds an {@code int} type. This constructor is private, so that the only
	 * int type object is {@link #INSTANCE}.
	 */

	private IntType() {}

	@Override
	public String toString() {
		return "int";
	}

	/**
	 * Determines whether this type can be assigned to a given type.
	 * Type {@code int} can only be assigned to itself and to {@code float}.
	 *
	 * @param other what this type should be assigned to
	 * @return true if and only if {@code other} is {@code int} or {@code float}
	 */

	@Override
	public boolean canBeAssignedTo(Type other) {
		// an int can only be assigned to an int or to a float
		return other == this || other == FloatType.INSTANCE;
	}

	@Override
	public org.apache.bcel.generic.Type toBCEL() {
		return org.apache.bcel.generic.Type.INT;
	}

	/**
	 * Adds to {@code il} the Java bytecodes that add two values of this
	 * type, namely an {@code iadd} bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	@Override
	public void add(InstructionList il) {
		il.append(new org.apache.bcel.generic.IADD());
	}

	/**
	 * Adds to {@code il} the Java bytecodes that multiply two values of this
	 * type, namely an {@code imul} bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	@Override
	public void mul(InstructionList il) {
		il.append(new org.apache.bcel.generic.IMUL());
	}

	/**
	 * Adds to {@code il} the Java bytecodes that divide two values of this
	 * type, namely an {@code idiv} bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	@Override
	public void div(InstructionList il) {
		il.append(new org.apache.bcel.generic.IDIV());
	}

	/**
	 * Adds to {@code il} the Java bytecodes that subtract two values of this
	 * type, namely an {@code isub} bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	@Override
	public void sub(InstructionList il) {
		il.append(new org.apache.bcel.generic.ISUB());
	}

	/**
	 * Adds to {@code il} the Java bytecodes that negate a value of this
	 * type, namely, an {@code ineg} Java bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	@Override
	public void neg(InstructionList il) {
		il.insert(new org.apache.bcel.generic.INEG());
	}
}