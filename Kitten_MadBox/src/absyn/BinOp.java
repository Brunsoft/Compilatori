package absyn;

import java.io.FileWriter;

import translation.Block;
import types.Type;
import bytecode.BinOpBytecode;

/**
 * A node of abstract syntax representing a binary operation between two expressions.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BinOp extends Expression {

	/**
	 * The left-hand side expression of the binary operation.
	 */

	private final Expression left;

	/**
	 * The right-hand side expression of the binary operation.
	 */

	private final Expression right;

	/**
	 * Constructs the abstract syntax of a binary operation between two expressions.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param left the abstract syntax of the left-hand side expression
	 * @param right the abstract syntax of the right-hand side expression
	 */

	protected BinOp(int pos, Expression left, Expression right) {
		super(pos);

		this.left = left;
		this.right = right;
	}

	/**
	 * Yields the left-hand side expression of the binary operation.
	 *
	 * @return the left-hand side expression of the binary operation
	 */

	public Expression getLeft() {
		return left;
	}

	/**
	 * Yields the right-hand side expression of the binary operation.
	 *
	 * @return the right-hand side expression of the binary operation
	 */

	public Expression getRight() {
		return right;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of a binary operation between two
	 * expressions. This amounts to adding two arcs from the node for the
	 * binary operation to the abstract syntax for its two expressions.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("left", left.toDot(where), where);
		linkToNode("right", right.toDot(where), where);
	}

	/**
	 * Translates this expression into its intermediate Kitten code.
	 * The result is a piece of code which pushes onto the stack
	 * the value of the expression (namely, the translation of the
	 * left and right sides of the binary expression, followed by
	 * the binary bytecode returned by {@link #operator(Type)}
	 * and that performs a binary operation-specific
	 * computation on the values of the left and right-hand sides)
	 * followed by the given {@code continuation}.
	 * The original stack elements are not modified.
	 *
	 * @param continuation the code executed after this expression
	 * @return the code that evaluates this expression and continues
	 *         with {@code continuation}
	 */

	@Override
	public final Block translate(Block continuation) {
		Type type = getLeft().getStaticType().leastCommonSupertype(getRight().getStaticType());

		return getLeft().translateAs
			(type, getRight().translateAs(type, operator(type).followedBy(continuation)));
	}

	/**
	 * A binary operation-specific bytecode that performs a binary
	 * computation on the left and right-hand sides of this binary operator.
	 *
	 * @param type the type of the values computed by the left and right-hand
	 *             sides of this binary expression
	 * @return a binary bytecode that performs a binary operation-specific
	 *         computation on the values of the left and right-hand sides
	 */

	protected abstract BinOpBytecode operator(Type type);
}