package absyn;

import types.NumericalType;
import types.Type;
import bytecode.BinOpBytecode;
import bytecode.MUL;

/**
 * A node of abstract syntax representing the multiplication of two expressions.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Multiplication extends ArithmeticBinOp {

	/**
	 * Constructs the abstract syntax of the multiplication of two expressions.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param left the abstract syntax of the left-hand side expression
	 * @param right the abstract syntax of the right-hand side expression
	 */

	public Multiplication(int pos, Expression left, Expression right) {
		super(pos, left, right);
	}

	/**
	 * A binary operation-specific bytecode that performs a binary
	 * computation on the left and right sides of this binary operation.
	 * Namely, a {@code mul} bytecode.
	 *
	 * @param type the type of the values of the left and right sides of this binary expression
	 * @return a {@code mul} bytecode
	 */

	@Override
	protected BinOpBytecode operator(Type type) {
		return new MUL((NumericalType) type);
	}
}