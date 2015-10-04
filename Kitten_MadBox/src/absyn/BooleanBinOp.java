package absyn;

import semantical.TypeChecker;
import types.BooleanType;
import types.Type;

/**
 * A node of abstract syntax representing a Boolean binary operation
 * between two expressions, that is, a binary operation, such logical
 * and or logical or, that operates on Booleans and returns a Boolean.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BooleanBinOp extends BinOp {

	/**
	 * Constructs the abstract syntax of a Boolean binary operation
	 * between two expressions.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param left the abstract syntax of the left-hand side expression
	 * @param right the abstract syntax of the right-hand side expression
	 */

	protected BooleanBinOp(int pos, Expression left, Expression right) {
		super(pos, left, right);
	}

	/**
	 * Performs the type-checking of a Boolean binary operation
	 * by using a given type-checker. It type-checks both sides of the
	 * binary operation and then checks that they have {@code boolean} type.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the semantical {@code boolean} type
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		getLeft().mustBeBoolean(checker);
		getRight().mustBeBoolean(checker);

		return BooleanType.INSTANCE;
	}
}