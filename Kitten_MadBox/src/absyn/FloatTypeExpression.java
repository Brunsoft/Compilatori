package absyn;

import types.FloatType;
import types.Type;

/**
 * A node of abstract syntax representing the Kitten {@code float} type.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FloatTypeExpression extends TypeExpression {

	/**
	 * Constructs the abstract syntax of the Kitten {@code float} type.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 */

	public FloatTypeExpression(int pos) {
		super(pos);
	}

	@Override
	protected Type typeCheckAux() {
		return FloatType.INSTANCE;
	}

	@Override
	protected Type toTypeAux() {
		return FloatType.INSTANCE;
	}

	@Override
	public String toString() {
		return "float";
	}
}