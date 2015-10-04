package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;

/**
 * A node of abstract syntax representing a local scope, <i>i.e.</i>, a block
 * of code whose local declarations are not visible after the block.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class LocalScope extends Command {

	/**
	 * The body of the local scope, <i>i.e.</i>, the command that is
	 * executed in the local scope.
	 */

	private final Command body;

	/**
	 * Constructs the abstract syntax of a local scope, <i>i.e.</i>, a block
	 * of code whose local declarations are not visible after the block.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param body the body of the local scope, <i>i.e.</i>, the command
	 *             that is executed in the local scope
	 */

	public LocalScope(int pos, Command body) {
		super(pos);

		this.body = body;
	}

	/**
	 * Yields the body of the local scope.
	 *
	 * @return the body of the local scope
	 */

	public Command getBody() {
		return body;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of a local scope. This amounts
	 * to adding arcs from the node for the local scope to {@link #body}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("body", body.toDot(where), where);
	}

	/**
	 * Performs the type-checking of a local scope.
	 * Namely, it type-checks the body of the local scope, and returns the
	 * original type-checker, so that local declarations inside the body of the
	 * local scope are not visible after.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the type-checker {@code checker} itself
	 */

	@Override
	protected TypeChecker typeCheckAux(TypeChecker checker) {
		body.typeCheck(checker);

		return checker;
	}

	/**
	 * Checks that this local scope does not contain <i>dead-code</i>, that is,
	 * commands that can never be executed. This is reduced to the problem
	 * of finding dead-code in its body.
	 *
	 * @return true if and only if every syntactical execution path in the body of this
	 *         local declaration ends with a {@code return} command
	 */

	@Override
	public boolean checkForDeadcode() {
		return body.checkForDeadcode();
	}

	/**
	 * Translates this command into intermediate
	 * Kitten bytecode. Namely, it translates the {@link #body} of the
	 * local scope with the given {@code continuation}.
	 *
	 * @param continuation the continuation to be executed after this command
	 * @return the code executing this command and then the {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		// we translate the body of the local scope with the given continuation
		return body.translate(continuation);
	}
}