package absyn;

import types.IntType;
import types.Type;

/**
 * A node of abstract syntax representing the Kitten {@code int} type.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class IntTypeExpression extends TypeExpression {

	/**
	 * Constructs the abstract syntax of the Kitten {@code int} type.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 */

	public IntTypeExpression(int pos) {
		super(pos);
	}

	@Override
	protected Type typeCheckAux() {
		return IntType.INSTANCE;
	}

	@Override
	protected Type toTypeAux() {
		return IntType.INSTANCE;
	}

	@Override
	public String toString() {
		return "int";
	}
}