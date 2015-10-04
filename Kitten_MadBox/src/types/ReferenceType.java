package types;

import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;

/**
 * A reference type, <i>i.e.</i>, a type whose values can be <i>referenced</i>
 * from a pointer, such as classes and arrays.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ReferenceType extends ComparableType {

	/**
	 * Adds to {@code il} the Java bytecodes which go to {@code yes}
	 * if the the top two elements of the stack are equal.
	 * In this case, it adds an {@code if_acmpeq} Java bytecode.
	 *
	 * @param il the list of instructions which must be expanded
	 * @param yes to place where to jump
	 */

	@Override
	public void JB_if_cmpeq(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.IF_ACMPEQ(yes));
	}

	/**
	 * Adds to {@code il} the Java bytecodes which go to {@code yes}
	 * if the the top two elements of the stack are not equal.
	 * In this case, it adds an {@code if_acmpne} Java bytecode.
	 *
	 * @param il the list of instructions which must be expanded
	 * @param yes to place where to jump
	 */

	@Override
	public void JB_if_cmpne(InstructionList il, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.IF_ACMPNE(yes));
	}
}