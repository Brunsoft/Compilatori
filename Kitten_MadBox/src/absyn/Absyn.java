package absyn;

import java.io.FileWriter;
import java.io.IOException;

import semantical.TypeChecker;

/**
 * A node of abstract syntax.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Absyn {

	/**
	 * The position in the source file of the beginning of the concrete
	 * syntax represented by this abstract syntax.
	 */

	private final int pos;

	/**
	 * The unique identifier of this node of abstract syntax.
	 */

	private final int identifier;

	/**
	 * A counter used to assign distinct values to the <tt>identifier</tt>
	 * field.
	 */

	private static int counter = 0;

	/**
	 * A type error that occurred in this node during type-checking.
	 */

	private String typeError;

	/**
	 * Constructs a node of abstract syntax whose concrete syntax starts
	 * at the given position from the beginning of the source file.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 */

	protected Absyn(int pos) {
		this.pos = pos;

		// we assign a unique identifier to this node of abstract syntax
		this.identifier = counter++;

		// no type-checking error at the beginning
		this.typeError = null;
	}

	/**
	 * Yields the starting position in the source file of
	 * the concrete syntax represented by this abstract syntax.
	 *
	 * @return the starting position in the source file of
	 *         the concrete syntax represented by this abstract syntax.
	 */

	public int getPos() {
		return pos;
	}

	/**
	 * Yields a the string for labeling the class of abstract syntax represented by
	 * this node. By default, this is the name of the class of abstract
	 * syntax. But it can be redefined in order to provide abridged names.
	 *
	 * @return the label, as a string, describing this node of abstract syntax
	 */

	protected String label() {
		// yields the Java class name of this node. If a type-error occurred in this
		// node of abstract syntax, we report it
		return getClass().getSimpleName() + (typeError == null ? "" : " !!" + typeError + "!!");
	}

	/**
	 * Yields the name of this node of abstract syntax used in a
	 * dot file dumped for inspection.
	 *
	 * @return the name used to refer to this node in the dot file
	 */

	protected final String dotNodeName() {
		return "node" + identifier;
	}

	/**
	 * Writes in a file the node for the dot representation of the given symbol.
	 *
	 * @param symbol the symbol
	 * @param where the file where the dot representation must be written
	 * @return the string representing this node in the dot file
	 */

	protected String toDot(String symbol, FileWriter where) throws IOException {
		String id = "symbol_" + symbol.toString();
		where.write(id + " [label = \"" + symbol.toString() + "\" fontname = \"Times-Italic\" shape = box]\n");

		return id;
	}

	/**
	 * Writes in a dot file a labeled arc between the node for this abstract
	 * syntax and that specified as a parameter.
	 *
	 * @param name the label of the arc
	 * @param to the name of the destination of the arc, as used in the dot file
	 * @param where the dot file where this arc should be written
	 */

	protected final void linkToNode(String name, String to, FileWriter where) throws java.io.IOException {
		where.write(dotNodeName() + " -> " + to + " [label = \"" + name + "\" fontsize = 8]\n");
	}

	/**
	 * Writes in a dot file a bold labeled arc
	 * between the node for this abstract
	 * syntax and that specified as a parameter.
	 *
	 * @param name the label of the arc
	 * @param to the name of the destination of the arc, as used in the dot file
	 * @param where the dot file where this arc should be written
	 */

	protected final void boldLinkToNode(String name, String to, FileWriter where) throws java.io.IOException {
		where.write(dotNodeName() + " -> " + to + " [label = \"" + name + "\" fontsize = 8 style = bold]\n");
	}

	/**
	 * Outputs an error message to the user, during the type-checking
	 * performed with a given type checker.
	 *
	 * @param checker the type checker which signals the error
	 * @param msg the message to be output
	 */

	protected void error(TypeChecker checker, String msg) {
		checker.error(pos, msg);

		typeError = msg;
	}
}