package absyn;

import java.io.FileWriter;
import java.io.IOException;

import semantical.TypeChecker;
import translation.Block;

/**
 * A node of abstract syntax representing a Kitten command.
 * Commands are syntactical structures that yield no value.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Command extends Absyn {

	/**
	 * The type-checker resulting after the last type-checking of this command.
	 * This is {@code null} if this command has not been type-checked yet.
	 */

	private TypeChecker checker;

	/**
	 * Constructs the abstract syntax of a command.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 */

	protected Command(int pos) {
		super(pos);
	}

	/**
	 * Yields the type-checker resulting from the last type-checking of this command.
	 *
	 * @return The type-checker resulting from the last type-checking of this
	 *         command. Yields {@code null} if this command has not been type-checked yet
	 */

	public TypeChecker getTypeChecker() {
		return checker;
	}

	/**
	 * Writes in the specified file a dot representation of the abstract
	 * syntax of this command. By default, it writes a single dot node for this
	 * node of abstract syntax and it calls the auxiliary
	 * {@code #toDotAux(FileWriter)} method. Subclasses should redefine the latter
	 * in order to consider components of commands.
	 * It then calls itself recursively on the subsequent command, if any.
	 *
	 * @param where the file where the dot representation must be written
	 * @return the name used to refer to this node in the dot file
	 * @throws IOException if there is a problem while writing the file
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
	 * arcs between this node and those for the abstract syntax of its components.
	 *
	 * @param where the file where the dot representation must be written
	 */

	protected void toDotAux(FileWriter where) throws java.io.IOException {
		// nothing, by default
	}

	/**
	 * Performs the type-checking of this command by using a given
	 * type-checker. It calls the command-specific type-checking method
	 * {@link #typeCheckAux(TypeChecker)} and then type-checks the subsequent commands,
	 * if any.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return a type-checker derived from {@code checker} by type-checking
	 *         this and the subsequent commands
	 */

	public final TypeChecker typeCheck(TypeChecker checker) {
		// we perform the command-specific type-checking and record
		// the resulting type-checker
		return this.checker = checker = typeCheckAux(this.checker = checker);
	}

	/**
	 * Performs the type-checking of this command by using a given type-checker.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the type-checker resulting from the type-checking of the command
	 */

	protected abstract TypeChecker typeCheckAux(TypeChecker checker);

	/**
	 * Checks that this command does not contain <i>dead-code</i>, that is,
	 * commands which can never be executed. This method considers a
	 * syntactical definition of dead-code. It is actually undecidable to
	 * determine if a piece of code will never be executed at run-time. The
	 * only guarantee provided by this method is that if some dead-code is
	 * found, then it really is dead-code. The converse might not hold
	 * (semantically correct but incomplete behavior).
	 *
	 * @return true if and only if every syntactical execution path in this
	 *         command ends with a {@code return} command
	 */

	public abstract boolean checkForDeadcode();

	/**
	 * Translates this command into its intermediate Kitten code. The result is
	 * a code that executes the command by leaving the stack unchanged, exactly as it
	 * were before the execution of the command.
	 *
	 * @param continuation the continuation to be executed after this command
	 * @return the code executing this command
	 */

	public abstract Block translate(Block continuation);

	/**
	 * Outputs an error message to the user, by using the type-checker
	 * used during the last type-checking.
	 *
	 * @param msg the message to be output
	 */

	protected void error(String msg) {
		error(checker, msg);
	}
}