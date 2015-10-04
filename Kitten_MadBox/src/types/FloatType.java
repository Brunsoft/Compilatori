package types;

import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;

/**
 * The {@code float} type of the Kitten language.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FloatType extends NumericalType {

	/**
	 * The float type. Always use this constant to refer to the float type,
	 * so that type comparison can be carried out by simple == tests.
	 */

	public final static FloatType INSTANCE = new FloatType();

	/**
	 * Builds a float type. This constructor is private, so that the only
	 * float type object is {@link #INSTANCE}.
	 */

	private FloatType() {}

	@Override
	public String toString() {
		return "float";
	}

	/**
	 * Determines if this type can be assigned to {@code other}.
	 * This is only true if {@code other} is a {@code float}.
	 *
	 * @param other the other type
	 * @return true if and only if {@code other} is {@code float}
	 */

	@Override
	public boolean canBeAssignedTo(Type other) {
		// a float can only be assigned to another float
		return this == other;
	}

	@Override
	public org.apache.bcel.generic.Type toBCEL() {
		return org.apache.bcel.generic.Type.FLOAT;
	}

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the the top two elements of the stack are equal.
	 * In this case, it adds an {@code fcmpl} Java bytecode followed
	 * by an {@code ifeq} Java bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 * @param yes to place where to jump
	 */

	@Override
	public void JB_if_cmpeq(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.FCMPL());
		il.append(new org.apache.bcel.generic.IFEQ(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the the top two elements of the stack are not equal.
	 * In this case, it adds an {@code fcmpl} Java bytecode followed
	 * by an {@code ifne} Java bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 * @param yes to place where to jump
	 */

	@Override
	public void JB_if_cmpne(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.FCMPL());
		il.append(new org.apache.bcel.generic.IFNE(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes which go to {@code yes}
	 * if the element under the top of the stack is less than the element
	 * at the top of the stack.
	 * In this case, it adds an {@code fcmpl} Java bytecode followed
	 * by an {@code iflt} Java bytecode.
	 *
	 * @param il the list of instructions which must be expanded
	 * @param yes to place where to jump
	 */

	@Override
	public void JB_if_cmplt(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.FCMPL());
		il.append(new org.apache.bcel.generic.IFLT(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the element under the top of the stack is greater than the element
	 * at the top of the stack.
	 * In this case, it adds an {@code fcmpl} Java bytecode followed
	 * by an {@code ifgt} Java bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 * @param yes to place where to jump
	 */

	@Override
	public void JB_if_cmpgt(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.FCMPL());
		il.append(new org.apache.bcel.generic.IFGT(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the element under the top of the stack is less than or equal to
	 * the element at the top of the stack.
	 * In this case, it adds an {@code fcmpl} Java bytecode followed
	 * by an {@code ifle} Java bytecode.
	 *
	 * @param il the list of instructions which must be expanded
	 * @param yes to place where to jump
	 */

	@Override
	public void JB_if_cmple(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.FCMPL());
		il.append(new org.apache.bcel.generic.IFLE(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the element under the top of the stack is greater than or equal to
	 * the element at the top of the stack.
	 * In this case, it adds an {@code fcmpl} Java bytecode followed
	 * by an {@code ifge} Java bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 * @param yes to place where to jump
	 */

	@Override
	public void JB_if_cmpge(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.FCMPL());
		il.append(new org.apache.bcel.generic.IFGE(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes that add two values of this
	 * type, namely an {@code fadd} bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	@Override
	public void add(InstructionList il) {
		il.append(new org.apache.bcel.generic.FADD());
	}

	/**
	 * Adds to {@code il} the Java bytecodes that multiply two values of this
	 * type, namely an {@code fmul} bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	@Override
	public void mul(InstructionList il) {
		il.append(new org.apache.bcel.generic.FMUL());
	}

	/**
	 * Adds to {@code il} the Java bytecodes that divide two values of this
	 * type, namely an {@code fdiv} bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	@Override
	public void div(InstructionList il) {
		il.append(new org.apache.bcel.generic.FDIV());
	}

	/**
	 * Adds to {@code il} the Java bytecodes that subtract two values of this
	 * type, namely an {@code fsub} bytecode.
	 *
	 * @param il the list of instructions which must be expanded
	 */

	@Override
	public void sub(InstructionList il) {
		il.append(new org.apache.bcel.generic.FSUB());
	}

	/**
	 * Adds to {@code il} the Java bytecodes that negate a value of this
	 * type, namely, an {@code fneg} Java bytecode.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	@Override
	public void neg(InstructionList il) {
		il.insert(new org.apache.bcel.generic.FNEG());
	}
}