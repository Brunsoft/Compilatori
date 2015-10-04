package bytecode;

import types.ComparableType;

/**
 * A bytecode that compares the top two elements of the stack. It is used to route
 * the computation at the end of a branching block of code.
 * <br><br>
 * ..., value1, value2 -&gt; ...
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BranchingComparisonBytecode extends BranchingBytecode {

	/**
	 * The semantical type of the top two elements of the stack, that are compared.
	 */

	private ComparableType type;

	/**
	 * Constructs a bytecode that compares the top two elements of the
	 * stack to decide where to branch.
	 *
	 * @param type the semantical type of the values that are compared
	 */

	protected BranchingComparisonBytecode(ComparableType type) {
		this.type = type;
	}

	/**
	 * Yields the semantical type of the top two elements of the stack.
	 * 
	 * @return the semantical type
	 */

	public ComparableType getType() {
		return type;
	}

	@Override
	public String toString() {
		return super.toString() + " " + type;
	}
}