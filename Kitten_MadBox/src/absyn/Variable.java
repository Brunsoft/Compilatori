package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;
import types.Type;
import bytecode.LOAD;
import bytecode.STORE;

/**
 * A node of abstract syntax representing a reference to a program variable.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Variable extends Lvalue {

	/**
	 * The name of the variable.
	 */

	private final String name;

	/**
	 * Constructs the abstract syntax of a reference to a program variable.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param name the name of the variable
	 */

	public Variable(int pos, String name) {
		super(pos);

		this.name = name;
	}

	/**
	 * Yields the name of the variable.
	 *
	 * @return the name of the variable
	 */

	public String getName() {
		return name;
	}

	/**
	 * Yields the unique number of this variable inside the method where
	 * it occurs. Type-checking must have been completed before calling this method.
	 *
	 * @return the unique number of this variable inside the method where it occurs
	 */

	public int getVarNum() {
		return getTypeChecker().getVarNum(name);
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of the reference to a variable.
	 * This amounts to adding an arc from the node for the variable
	 * to the node for {@link #name}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("name", toDot(name, where), where);
	}

	/**
	 * Performs the type-checking of the reference to a program variable,
	 * by using a given type-checker.
	 * It just checks that the variable is known to the type-checker.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the static type that {@code checker} assigns to this variable
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		// we ask the type-checker for the type of this variable
		Type result = checker.getVar(name);

		if (result == null)
			// if the type-checker does not know this variable, we issue an error
			return error("undefined variable " + name);

		// we return the type assigned by the type-checker to this variable
		return result;
	}

	/**
	 * Translates this expression into its intermediate Kitten code.
	 * The result is a piece of code that starts with a {@code load}
	 * bytecode that loads on the stack the value of this variable, and
	 * continues with the given {@code continuation}.
	 *
	 * @param continuation the code executed after this expression
	 * @return the code that evaluates this expression and continues with {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		return new LOAD(getVarNum(), getStaticType()).followedBy(continuation);
	}

	/**
	 * Generates the intermediate Kitten code which must be executed before
	 * the evaluation of the rightvalue which is going to be assigned to this
	 * variable. There is nothing to generate in this case.
	 *
	 * @param continuation the code that must be executed after this expression
	 * @return {@code continuation} itself
	 */

	@Override
	public Block translateBeforeAssignment(Block continuation) {
		return continuation;
	}

	/**
	 * Generates the intermediate Kitten code which must be executed after
	 * the evaluation of the rightvalue which is going to be assigned to this
	 * variable. Namely, it generates a {@code store} bytecode.
	 *
	 * @param continuation the code which must be executed after this expression
	 * @return a {@code store} bytecode followed by {@code continuation}
	 */

	@Override
	public Block translateAfterAssignment(Block continuation) {
		return new STORE(getVarNum(), getStaticType()).followedBy(continuation);
	}
}