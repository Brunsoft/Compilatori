package absyn;

import java.io.FileWriter;
import java.io.IOException;

import semantical.TypeChecker;
import types.TypeList;

/**
 * A node of abstract syntax representing the formal parameters of a Kitten method.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FormalParameters extends Absyn {

	/**
	 * The abstract syntax of the type of the first parameter.
	 */

	private final TypeExpression type;

	/**
	 * The name of the first parameter.
	 */

	private final String name;

	/**
	 * The next parameters, if any.
	 */

	private final FormalParameters next;

	/**
	 * Constructs a node of abstract syntax for a sequence
	 * of the formal parameters of a method.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 * @param type the abstract syntax of the type of the first parameter
	 * @param name the name of the first parameter
	 * @param next the declaration of the next parameters, if any
	 */

	public FormalParameters(int pos, TypeExpression type, String name, FormalParameters next) {
		super(pos);

		this.type = type;
		this.name = name;
		this.next = next;
	}

	/**
	 * Yields the abstract syntax of the type of the first parameter.
	 *
	 * @return the abstract syntax of the type of the first parameter
	 */

	public TypeExpression getType() {
		return type;
	}

	/**
	 * Yields the name of the first parameter.
	 *
	 * @return the name of the first parameter
	 */

	public String getName() {
		return name;
	}

	/**
	 * Yields the abstract syntax of the next parameters, if any.
	 *
	 * @return the abstract syntax of the next parameters, if any
	 */

	public FormalParameters getNext() {
		return next;
	}

	/**
	 * Writes in the specified file a dot representation of the abstract
	 * syntax of the formal parameters of a method.
	 *
	 * @param where the file where the dot representation must be written
	 * @return the name used to refer to this node in the dot file
	 * @throws IOException if there is a problem while writing into the file
	 */

	public final String toDot(FileWriter where) throws IOException {
		// dumps in the file the name of the node in the dot file,
		// followed by the label used to show the node to the user of dot
		where.write(dotNodeName() + " [ label = \"" + label() + "\"];\n");

		// we add arcs between the dot node for this object and those for
		// the type, name and next fields
		linkToNode("type", type.toDot(where), where);
		linkToNode("name", toDot(name, where), where);
		if (next != null)
			boldLinkToNode("next", next.toDot(where), where);

		return dotNodeName();
	}

	/**
	 * Computes the list of semantical types of these parameters.
	 * It type-checks the first parameter and then, recursively, {@link #next}, if any.
	 *
	 * @return the list of semantical types of these parameteres
	 */

	public TypeList typeCheck() {
		TypeList result = next != null ? next.typeCheck() : TypeList.EMPTY;

		return result.push(type.typeCheck());
	}

	/**
	 * Type-checks the parameters of a Kitten method. That is, it adds
	 * the parameters in the symbol table of the type-checker, with their declared type.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return a type-checker derived from {@code checker}, but
	 *         whose symbol table has been enriched with
	 *         these parameters, bound to their respective type
	 */

	public TypeChecker typeCheck(TypeChecker checker) {
		// we add this parameter in the symbol table of the type-checker
		// and then continue recursively with the other parameters
		checker = checker.putVar(name,type.typeCheck());

		// we continue with the next parameters, if any
		if (next != null)
			checker = next.typeCheck(checker);

		return checker;
	}

	/**
	 * Computes the list of semantical types of these parameters.
	 * It builds the semantical type of the first parameter and then,
	 * recursively, those of {@link #next}. The difference with {@link #typeCheck()}
	 * is that class types occurring in these parameters are not type-checked.
	 *
	 * @return the list of semantical types of these parameteres.
	 */

	public TypeList toType() {
		TypeList result = next != null ? next.toType() : TypeList.EMPTY;

		return result.push(type.toType());
	}
}