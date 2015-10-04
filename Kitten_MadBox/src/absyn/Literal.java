package absyn;

/**
 * A node of abstract syntax representing a <i>literal</i>, that is,
 * a syntactical representation of a run-time value.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Literal extends Expression {

	/**
	 * Builds the abstract syntax of a literal.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 */

	protected Literal(int pos) {
		super(pos);
	}
}