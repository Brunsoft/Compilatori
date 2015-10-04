package absyn;

import semantical.TypeChecker;
import types.BooleanType;
import types.ComparableType;
import types.Type;
import bytecode.BinOpBytecode;
import bytecode.EQ;

/**
 * A node of abstract syntax representing an equality test between two expressions.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Equal extends ComparisonBinOp {

	/**
	 * Constructs the abstract syntax of an equality test between
	 * two expressions.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param left the abstract syntax of the left-hand side expression
	 * @param right the abstract syntax of the right-hand side expression
	 */

	public Equal(int pos, Expression left, Expression right) {
		super(pos, left, right);
	}

	/**
	 * Performs the type-checking of an equality test between two expressions.
	 * It type-checks both expressions and then checks that the static type of
	 * one of them is a subtype of the static type of the other. If this is
	 * not true, then the two expressions will never contain at run-time the same
	 * value, and this test is hence useless and considered as type incorrect.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the semantical {@code boolean} type
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		Type leftType = getLeft().typeCheck(checker);
		Type rightType = getRight().typeCheck(checker);

		// we must be able to assign the left-hand side to the right-hand side or vice versa
		if (!leftType.canBeAssignedTo(rightType) && !rightType.canBeAssignedTo(leftType))
			error("illegal comparison");

		// the result is always boolean
		return BooleanType.INSTANCE;
	}

	/**
	 * A binary operation-specific bytecode that performs a binary
	 * computation on the left and right sides of this binary operation.
	 * Namely, an {@code eq} bytecode.
	 *
	 * @param type the type of the values of the left and right sides of this binary expression
	 * @return an {@code eq} bytecode
	 */

	@Override
	protected BinOpBytecode operator(Type type) {
		return new EQ((ComparableType)type);
	}
}