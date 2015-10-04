package absyn;

import types.Type;
import bytecode.AND;
import bytecode.BinOpBytecode;

/**
 * A node of abstract syntax representing a logical <i>and</i> operation.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class And extends BooleanBinOp {

	/**
	 * Constructs the abstract syntax of a binary <i>and</i> logical operation.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param left the abstract syntax of the left-hand side expression
	 * @param right the abstract syntax of the right-hand side expression
	 */

	public And(int pos, Expression left, Expression right) {
		super(pos, left, right);
	}

	/**
	 * A binary operation-specific bytecode that performs a binary
	 * computation on the left and right sides of this binary operation.
	 *
	 * @param type the type of the values of the left and right sides of this
	 *             binary expression
	 * @return an {@code and} bytecode
	 */

	@Override
	protected BinOpBytecode operator(Type type) {
		return new AND();
	}
}