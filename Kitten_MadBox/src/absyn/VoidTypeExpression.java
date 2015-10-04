package absyn;

import types.Type;
import types.VoidType;

/**
 * A node of abstract syntax representing the Kitten {@code void} type.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class VoidTypeExpression extends TypeExpression {

	/**
	 * Constructs the abstract syntax of the Kitten {@code void} type.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 */

	public VoidTypeExpression(int pos) {
		super(pos);
	}

	@Override
	protected Type typeCheckAux() {
		return VoidType.INSTANCE;
	}

	@Override
	protected Type toTypeAux() {
		return VoidType.INSTANCE;
	}

	@Override
	public String toString() {
		return "void";
	}
}