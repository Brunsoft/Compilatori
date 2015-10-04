package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;
import types.Type;
import types.VoidType;
import bytecode.RETURN;

/**
 * A node of abstract syntax representing a {@code return} command.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Return extends Command {

	/**
	 * The abstract syntax of the expression whose value is returned. It might be {@code null}.
	 */

	private Expression returned;

	/**
	 * Constructs the abstract syntax of a {@code return} command.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param returned the abstract syntax of the expression whose value
	 *                 is returned. It might be {@code null}
	 */

	public Return(int pos, Expression returned) {
		super(pos);

		this.returned = returned;
	}

	/**
	 * Yields the abstract syntax of the expression whose value is returned, if any.
	 *
	 * @return the abstract syntax of the expression whose value is returned,
	 *         if any. Yields {@code null} if there is no such expression
	 */

	public Expression getReturned() {
		return returned;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of the {@code return} command.
	 * This amounts to adding an arc from the node for the {@code return}
	 * command to the abstract syntax for {@link #returned}, if any.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		if (returned != null)
			linkToNode("returned", returned.toDot(where), where);
	}

	/**
	 * Performs the type-checking of the {@code return} command
	 * by using a given type-checker. It type-checks the expression whose
	 * value is returned, if it is not {@code null}, and checks that
	 * its static type can be assigned to the type expected by the
	 * type-checker for the {@code return} instructions.
	 * If no returned expression is present, then the it checks that
	 * the type-checker expects {@code void} as a
	 * return type. It returns the same type-checker passed as a parameter.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the type-checker {@code checker} itself
	 */

	@Override
	protected TypeChecker typeCheckAux(TypeChecker checker) {
		// we get from the type-checker the expected type for the return instructions
		Type expectedReturnType = checker.getReturnType();

		// a return command without expression is legal only inside a void method
		if (returned == null && expectedReturnType != VoidType.INSTANCE)
			error("missing return value");

		// if there is a returned expression, we check that its static
		// type can be assigned to the expected return type
		Type returnedType;
		if (returned != null && (returnedType = returned.typeCheck(checker)) != null &&
				!returnedType.canBeAssignedTo(expectedReturnType))
			error("illegal return type: " + expectedReturnType + " expected");

		return checker;
	}

	/**
	 * Checks that this {@code return} command does not contain <i>dead-code</i>, that is,
	 * commands that can never be executed. This is always true for {@code return} commands.
	 *
	 * @return true, since this command always terminates with a {@code return} command (itself)
	 */

	@Override
	public boolean checkForDeadcode() {
		return true;
	}

	/**
	 * Translates this command into intermediate Kitten bytecode. Namely,
	 * it returns a code which starts with the evaluation of {@link #returned},
	 * if any, and continues with a {@code return} bytecode for the type returned
	 * by the current method.
	 *
	 * @param continuation the continuation to be executed after this command
	 * @return the code executing this command and then the {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		// we get the type which must be returned by this the current method
		Type returnType = getTypeChecker().getReturnType();

		// we get a code which is made of a block containing the bytecode return
		continuation = new Block(new RETURN(returnType));

		// if there is an initialising expression, we translate it
		if (returned != null)
			continuation = returned.translateAs(returnType, continuation);

		return continuation;
	}
}