package bytecode;

import types.ComparableType;

/**
 * A bytecode that computes a binary comparison operation between the top two
 * elements of the stack. It pushes the Boolean result of the comparison on the stack.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 <i>comp</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ComparisonBinOpBytecode extends BinOpBytecode {

	/**
	 * The semantical type of the top two elements of the stack.
	 */

	private final ComparableType type;

	/**
	 * Constructs a list of instructions made up of a single bytecode that
	 * computes a binary comparison operation on the top two elements of the stack.
	 *
	 * @param type the semantical type of the top two values of the stack
	 */

	protected ComparisonBinOpBytecode(ComparableType type) {
		this.type = type;
	}

	/**
	 * Yields the semantical type of the top two elements of the stack.
	 *
	 * @return the semantical type of the top two elements of stack
	 */

	public ComparableType getType() {
		return type;
	}

	@Override
	public String toString() {
		return super.toString() + " " + type;
	}

	/**
	 * Yields a branching version of this bytecode. For instance, for {@code eq} it yields
	 * an {@code if_cmpeq} bytecode and so on.
	 *
	 * @return the branching bytecode corresponding to this comparison bytecode
	 */

	public abstract BranchingComparisonBytecode toBranching();
}