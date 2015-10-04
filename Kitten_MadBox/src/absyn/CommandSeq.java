package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;

/**
 * A node of abstract syntax representing a sequence of two commands.
 * Local declarations are not visible after the sequence.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class CommandSeq extends Command {

	/**
	 * The first command in the sequence.
	 */

	private final Command first;

	/**
	 * The second command in the sequence.
	 */

	private final Command second;

	/**
	 * Constructs the abstract syntax of a sequence of two commands.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param first the first component of the sequence
	 * @param second the second component of the sequence
	 */

	public CommandSeq(int pos, Command first, Command second) {
		super(pos);

		this.first = first;
		this.second = second;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of a sequence of two commands.
	 * This amounts to adding arcs from the node for the sequence
	 * command to the abstract syntax for {@link #first} and {@link #second}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("first", first.toDot(where), where);
		linkToNode("second", second.toDot(where), where);
	}

	/**
	 * Performs the type-checking of the sequence of two commands
	 * by using a given type-checker. It type-checks the second command in
	 * the type-checker resulting from the type-checking of the first command.
	 * It returns the type-checker resulting from the type-checking of the second command.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the type-checker resulting from the type-checking of the second command
	 */

	@Override
	protected TypeChecker typeCheckAux(TypeChecker checker) {
		return second.typeCheck(first.typeCheck(checker));
	}

	/**
	 * Checks that this conditional does not contain <i>dead-code</i>, that is,
	 * commands which can never be executed. It checks that there is no
	 * dead-code in any of the two commands. If the first command ends with
	 * a {@code return}, it issues an error.
	 *
	 * @return true if and only if every execution path in both branches of the
	 *         conditional ends with a {@code return} command
	 */

	@Override
	public boolean checkForDeadcode() {
		if (first.checkForDeadcode())
			error("dead-code after this statement");

		return second.checkForDeadcode();
	}

	/**
	 * Translates this command into intermediate Kitten bytecode. Namely, it
	 * translates the first command by using as continuation the translation of
	 * the second command by using the given {@code continuation}.
	 *
	 * @param continuation the continuation to be executed after this sequence of commands
	 * @return the code executing this sequence of commands and then {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		return first.translate(second.translate(continuation));
	}
}