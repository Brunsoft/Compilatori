package absyn;

import types.NumericalType;
import types.Type;
import bytecode.BinOpBytecode;
import bytecode.SUB;

/**
 * A node of abstract syntax representing the subtraction of two expressions.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Subtraction extends ArithmeticBinOp {

	/**
	 * Constructs the abstract syntax of the subtraction of two expressions.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param left the abstract syntax of the left-hand side expression
	 * @param right the abstract syntax of the right-hand side expression
	 */

	public Subtraction(int pos, Expression left, Expression right) {
		super(pos, left, right);
	}

	/**
	 * A binary operation-specific bytecode which performs a binary
	 * computation on the left and right sides of this binary operation.
	 * Namely, a {@code sub} bytecode.
	 *
	 * @param type the type of the values of the left and right sides of this binary expression
	 * @return a {@code sub} bytecode
	 */

	@Override
	protected BinOpBytecode operator(Type type) {
		return new SUB((NumericalType) type);
	}
}