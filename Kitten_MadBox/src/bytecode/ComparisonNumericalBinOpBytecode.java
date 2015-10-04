package bytecode;

import types.NumericalType;

/**
 * A bytecode that computes a numerical binary comparison operation between
 * the top two elements of the stack.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 <i>comp</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ComparisonNumericalBinOpBytecode extends ComparisonBinOpBytecode {

	/**
	 * Constructs a list of instructions made up of a single bytecode that computes
	 * a numerical binary comparison operation on the top two elements of the stack.
	 *
	 * @param type the semantical type of the top two values of the stack
	 */

	protected ComparisonNumericalBinOpBytecode(NumericalType type) {
		super(type);
	}

	/**
	 * Yields the semantical type of the top two elements of the stack.
	 *
	 * @return the semantical type of the top two elements of stack
	 */

	@Override
	public NumericalType getType() {
		return (NumericalType) super.getType();
	}
}