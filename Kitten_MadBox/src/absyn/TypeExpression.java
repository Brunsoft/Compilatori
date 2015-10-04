package absyn;

import java.io.FileWriter;
import java.io.IOException;

import types.Type;

/**
 * A node of abstract syntax representing a Kitten type.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class TypeExpression extends Absyn {

	/**
	 * The static semantical type of this type expression, as computed
	 * by the type-checker. It is {@code null} if type-checking
	 * has not been computed yet.
	 */

	private Type staticType;

	/**
	 * Constructs the abstract syntax of a Kitten type.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 */

	protected TypeExpression(int pos) {
		super(pos);
	}

	/**
	 * Yields a the string labeling the class of abstract syntax represented by
	 * this node. We redefine this method in order to add the static type
	 * of the type expression, if it has already been computed by a type-checker.
	 *
	 * @return a string describing the label of this node of abstract syntax,
	 *         following by the static type of the type expression, if already
	 *         computed through type-checking
	 */

	@Override
	protected String label() {
		// if this expression has not been type-checked yet, we do not report its static type
		if (staticType == null)
			return super.label();
		// otherwise we add its static type
		else
			return super.label() + " [" + staticType + "]";
	}

	/**
	 * Writes in the specified file a dot representation of the abstract syntax
	 * of this type. By default, it writes a single dot node for this
	 * node of abstract syntax and it calls the auxiliary
	 * {@link #toDotAux(FileWriter)} method. Subclasses should redefine the latter
	 * in order to consider components of types.
	 *
	 * @param where the file where the dot representation must be written
	 * @return the name used to refer to this node in the dot file,
	 *         as computed by {@link #dotNodeName()}
	 * @throws IOException if there is a problem while writing into the file
	 */

	public final String toDot(FileWriter where) throws IOException {
		// dumps in the file the name of the node in the dot file,
		// followed by the label used to show the node to the user of dot
		where.write(dotNodeName() + " [ label = \"" + label() + "\"];\n");

		toDotAux(where);

		return dotNodeName();
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax. This should usually build
	 * arcs between this node and those for the abstract syntax
	 * of its components.
	 *
	 * @param where the file where the dot representation must be written
	 */

	protected void toDotAux(FileWriter where) throws java.io.IOException {
		// nothing, by default
	}

	/**
	 * Type-checks this type expression. It calls the auxiliary method
	 * {@link #typeCheckAux()} and stores the resulting semantical type
	 * into {@link #staticType}.
	 *
	 * @return the semantical type corresponding to this type expression
	 */

	public final Type typeCheck() {
		return staticType = typeCheckAux();
	}

	/**
	 * Auxiliary method for type-checking. Performs the type-checking of this
	 * type expression.
	 *
	 * @return the semantical type corresponding to this type expression
	 */

	protected abstract Type typeCheckAux();

	/**
	 * Yields the semantical type corresponding to this type expression.
	 * It calls the auxiliary method {@link #toTypeAux()} and stores the
	 * resulting semantical type into {@link #staticType}. The difference with
	 * {@link #typeCheck()} is that class types occurring in this type
	 * expressions are not type-checked themselves.
	 *
	 * @return the semantical type corresponding to this type expression
	 */

	public final Type toType() {
		return staticType = toTypeAux();
	}

	/**
	 * Auxiliary method that yields the semantical type corresponding
	 * to this type expression. The difference with {@link #typeCheckAux()}
	 * is that class types occurring in this type expressions are not type-checked themselves.
	 *
	 * @return the semantical type corresponding to this type expression
	 */

	protected abstract Type toTypeAux();

	/**
	 * Yields the static semantical type of this type expression, as computed during
	 * last type-checking.
	 *
	 * @return the static semantical type of this type expression, if it has
	 *         already been type-checked. Yields {@code null} otherwise
	 */

	public final Type getStaticType() {
		return staticType;
	}

	@Override
	public abstract String toString();
}