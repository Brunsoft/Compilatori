package absyn;

import types.NumericalType;
import types.Type;
import bytecode.ADD;
import bytecode.BinOpBytecode;

/**
 * A node of abstract syntax representing the addition of two expressions.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Addition extends ArithmeticBinOp {

	/**
	 * Constructs the abstract syntax of the addition of two expressions.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param left the abstract syntax of the left-hand side expression
	 * @param right the abstract syntax of the right-hand side expression
	 */

	public Addition(int pos, Expression left, Expression right) {
		super(pos, left, right);
	}

	/**
	 * A binary operation-specific bytecode that performs a binary
	 * computation on the left and right sides of this binary operation.
	 * Namely, an {@code add} bytecode.
	 *
	 * @param type the type of the values of the left and right sides of this
	 *             binary expression
	 * @return an {@code add} bytecode
	 */

	@Override
	protected BinOpBytecode operator(Type type) {
		return new ADD((NumericalType) type);
	}
}