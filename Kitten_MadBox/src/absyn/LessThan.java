package absyn;

import types.NumericalType;
import types.Type;
import bytecode.BinOpBytecode;
import bytecode.LT;

/**
 * A node of abstract syntax representing a &lt; comparison binary operation
 * between two expressions.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class LessThan extends NumericalComparisonBinOp {

	/**
	 * Constructs the abstract syntax of a &lt; comparison
	 * binary operation between two expressions.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param left the abstract syntax of the left-hand side expression
	 * @param right the abstract syntax of the right-hand side expression
	 */

	public LessThan(int pos, Expression left, Expression right) {
		super(pos, left, right);
	}

	/**
	 * A binary operation-specific bytecode which performs a binary
	 * computation on the left and right sides of this binary operation.
	 * Namely, an {@code lt} bytecode.
	 *
	 * @param type the type of the values of the left and right sides of this binary expression
	 * @return an {@code lt} bytecode
	 */

	@Override
	protected BinOpBytecode operator(Type type) {
		return new LT((NumericalType) type);
	}
}