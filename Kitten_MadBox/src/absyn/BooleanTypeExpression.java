package absyn;

import types.BooleanType;
import types.Type;

/**
 * A node of abstract syntax representing the Kitten {@code boolean} type.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class BooleanTypeExpression extends TypeExpression {

    /**
     * Constructs the abstract syntax of the Kitten {@code boolean} type.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     */

	public BooleanTypeExpression(int pos) {
		super(pos);
	}

	@Override
	protected Type typeCheckAux() {
		return BooleanType.INSTANCE;
	}

	@Override
	protected Type toTypeAux() {
		return BooleanType.INSTANCE;
	}

	@Override
	public String toString() {
		return "boolean";
	}
}