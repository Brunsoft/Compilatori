package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;
import types.FloatType;
import types.IntType;
import types.Type;
import bytecode.NEG;

/**
 * A node of abstract syntax representing the unary minus of an expression.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Minus extends Expression {

	/**
	 * The abstract syntax of the expression that is minus'ed.
	 */

	private final Expression expression;

	/**
	 * Constructs the abstract syntax of a unary minus expression.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param expression the abstract syntax of the expression that is minus'ed
	 */

	public Minus(int pos, Expression expression) {
		super(pos);

		this.expression = expression;
	}

	/**
	 * Yields the abstract syntax of the expression that is minus'ed.
	 *
	 * @return the abstract syntax of the expression that is minus'ed
	 */

	public Expression getExpression() {
		return expression;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of a unary minus of an expression.
	 * This amounts to adding an arc from the node for the unary minus
	 * to the abstract syntax for {@link #expression}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("expression", expression.toDot(where), where);
	}

	/**
	 * Performs the type-checking of the unary minus of an expression
	 * by using a given type-checker. It type-checks the expression and
	 * then checks that its static type is {@code int} or {@code float}.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the static type of the minus'ed expression
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		Type expressionType = expression.typeCheck(checker);

		// we can only negate integers or floats
		if (expressionType != IntType.INSTANCE && expressionType != FloatType.INSTANCE)
			error("integer or float expected");

		return expressionType;
	}

	/**
	 * Translates this expression into its intermediate Kitten code.
	 * The result is a piece of code that pushes onto the stack
	 * the value of the expression (namely, the translation of
	 * {@link #expression}, followed by the {@code neg} bytecode)
	 * followed by the given {@code continuation}.
	 *
	 * @param continuation the code executed after this expression
	 * @return the code that evaluates this expression and continues with {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		return expression.translate(new NEG(IntType.INSTANCE).followedBy(continuation));
	}
}