package types;

import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;

/**
 * A numerical type.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class NumericalType extends PrimitiveType {

    /**
     * Builds a numerical type.
     */

	protected NumericalType() {}

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the element under the top of the stack is less than the element
	 * at the top of the stack.
	 * In this case, it adds an {@code if_icmplt} Java bytecode.
	 * This method is redefined for the {@code float} type.
	 *
	 * @param il the list of instructions that must be expanded
	 * @param yes to place where to jump
	 */

	public void JB_if_cmplt(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.IF_ICMPLT(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the element under the top of the stack is greater than the element
	 * at the top of the stack.
	 * In this case, it adds an {@code if_icmpgt} Java bytecode.
	 * This method is redefined for the {@code float} type.
	 *
	 * @param il the list of instructions that must be expanded
	 * @param yes to place where to jump
	 */

	public void JB_if_cmpgt(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.IF_ICMPGT(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the element under the top of the stack is less than or equal to
	 * the element at the top of the stack.
	 * In this case, it adds an [@code if_icmple} Java bytecode.
	 * This method is redefined for the {@code float} type.
	 *
	 * @param il the list of instructions that must be expanded
	 * @param yes to place where to jump
	 */

	public void JB_if_cmple(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.IF_ICMPLE(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the element under the top of the stack is greater than or equal to
	 * the element at the top of the stack.
	 * In this case, it adds an {@code if_icmpge} Java bytecode.
	 * This method is redefined for the {@code float} type.
	 *
	 * @param il the list of instructions that must be expanded
	 * @param yes to place where to jump
	 */

	public void JB_if_cmpge(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.IF_ICMPGE(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes that add two values of this type.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	public abstract void add(InstructionList il);

	/**
	 * Adds to {@code il} the Java bytecodes that multiply two values of this type.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	public abstract void mul(InstructionList il);

	/**
	 * Adds to {@code il} the Java bytecodes that divide two values of this type.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	public abstract void div(InstructionList il);

	/**
	 * Adds to {@code il} the Java bytecodes that subtract two values of this type.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	public abstract void sub(InstructionList il);

	/**
	 * Adds to {@code il} the Java bytecodes that negate a value of this type.
	 *
	 * @param il the list of instructions that must be expanded
	 */

	public abstract void neg(InstructionList il);
}