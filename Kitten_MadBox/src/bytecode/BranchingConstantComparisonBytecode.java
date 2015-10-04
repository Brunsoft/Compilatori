package bytecode;

import types.ComparableType;

/**
 * A bytecode that compares the top element of the stack with a constant.
 * It is used to route the computation at the end of a branching block of code.
 * <br><br>
 * ..., value -&gt; ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BranchingConstantComparisonBytecode extends BranchingBytecode {

	/**
	 * The semantical type of the top element of the stack, that is compared
	 * with a constant, and of this constant itself.
	 */

	private final ComparableType type;

	/**
	 * Constructs a bytecode that compares the top element of the
	 * stack with a constant.
	 *
	 * @param type the semantical type of the value and of the constant
	 *             that are compared
	 */

	protected BranchingConstantComparisonBytecode(ComparableType type) {
		this.type = type;
	}

	/**
	 * Yields the semantical type of the top element of the stack.
	 * 
	 * @return the semantical type
	 */

	public ComparableType getType() {
		return type;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName().toLowerCase() + " " + type;
	}
}