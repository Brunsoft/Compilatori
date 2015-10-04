package bytecode;

import types.NumericalType;

/**
 * A bytecode that computes a binary arithmetic operation on the top two
 * elements of the stack.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 <i>op</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ArithmeticBinOpBytecode extends BinOpBytecode {

    /**
     * The semantical type of the top two elements of the stack.
     */

	private final NumericalType type;

	/**
	 * Constructs a bytecode that computes a binary arithmetic operation on the top two
	 * elements of the stack.
	 *
	 * @param type the semantical type of the top two values of the stack
	 */

	protected ArithmeticBinOpBytecode(NumericalType type) {
		this.type = type;
	}

	/**
	 * Yields the semantical type of the top two elements of the stack.
	 * This can only be {@code int} or {@code float}.
	 *
	 * @return the semantical type of the top two elements of stack
	 */

	public NumericalType getType() {
		return type;
	}

	@Override
	public String toString() {
		return super.toString() + " " + type;
	}
}