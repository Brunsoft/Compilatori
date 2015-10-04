package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;
import types.BooleanType;
import types.Type;
import bytecode.NEG;

/**
 * A node of abstract syntax representing the negation of a Boolean expression.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Not extends Expression {

	/**
	 * The abstract syntax of the negated expression.
	 */

	private final Expression expression;

	/**
	 * Constructs the abstract syntax of the negation of a Boolean expression.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param expression the negated Boolean expression
	 */

	public Not(int pos, Expression expression) {
		super(pos);

		this.expression = expression;
	}

	/**
	 * Yields the abstract syntax of the negated expression.
	 *
	 * @return the abstract syntax of the negated expression
	 */

	public Expression getExpression() {
		return expression;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of the negation of an expression.
	 * This amounts to adding an arc from the node for the negation to
	 * the abstract syntax of the negated expression [@link #expression}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("expression", expression.toDot(where), where);
	}

	/**
	 * Performs the type-checking of the negation of a Boolean expression
	 * by using a given type-checker. Namely, it checks that the expression
	 * has actually Boolean type.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the semantical Boolean type
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		// we check that the negated expression has Boolean type
		expression.mustBeBoolean(checker);

		// the negation of an expression has Boolean type
		return BooleanType.INSTANCE;
	}

	/**
	 * Translates this expression into its intermediate Kitten code.
	 * The result is a piece of code which pushes onto the stack
	 * the value of the expression (namely, the translation of the
	 * {@link #expression} that is negated, followed by the
	 * {@code neg} bytecode) followed by the given {@code continuation}.
	 * The original stack elements are not modified.
	 *
	 * @param continuation the code executed after this expression
	 * @return the code which evaluates this expression and continues
	 *         with {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		return expression.translate(new NEG(BooleanType.INSTANCE).followedBy(continuation));
	}
}