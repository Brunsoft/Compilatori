package types;

import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;

/**
 * A type whose values can be compared with each other.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ComparableType extends Type {

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the the top two elements of the stack are equal.
	 *
	 * @param il the list of instructions which must be expanded
	 * @param yes to place where to jump
	 */

	public abstract void JB_if_cmpeq(InstructionList il, InstructionHandle yes);

	/**
	 * Adds to {@code il} the Java bytecodes that go to {@code yes}
	 * if the the top two elements of the stack, of this type, are not equal.
	 *
	 * @param il the list of instructions which must be expanded
	 * @param yes to place where to jump
	 */

	public abstract void JB_if_cmpne(InstructionList il, InstructionHandle yes);
}
