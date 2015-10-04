package absyn;

import translation.Block;
import types.Type;
import bytecode.ComparisonBinOpBytecode;

/**
 * A node of abstract syntax representing a comparison binary operation
 * between two expressions, that is, a binary operation, such as
 * =, &lt; or &le;, that compares two values and returns a Boolean value.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ComparisonBinOp extends BinOp {

	/**
	 * Constructs the abstract syntax of a comparison
	 * binary operation between two expressions.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param left the abstract syntax of the left-hand side expression
	 * @param right the abstract syntax of the right-hand side expression
	 */

	protected ComparisonBinOp(int pos, Expression left, Expression right) {
		super(pos,left,right);
	}

	/**
	 * Translates this expression by routing control to one of two possible
	 * destinations, through an {@code ifcmp} bytecode.
	 *
	 * @param yes the continuation which is the <i>yes</i> destination
	 * @param no the continuation which is the <i>no</i> destination
	 * @return the code which evaluates the expression and on the basis
	 *         of its Boolean value routes the computation to the
	 *         {@code yes} or {@code no} continuation, respectively
	 */

	@Override
	public Block translateAsTest(Block yes, Block no) {
		// we compute the least common supertype of the two sides of this binary expression
		Type type = getLeft().getStaticType().leastCommonSupertype(getRight().getStaticType());

		return getLeft().translateAs(type,getRight().translateAs(type,
			(new Block(((ComparisonBinOpBytecode) operator(type)).toBranching(), yes, no))));
	}
}