package absyn;

import semantical.TypeChecker;
import translation.Block;
import types.NilType;
import types.Type;
import bytecode.CONST;

/**
 * A node of abstract syntax representing the {@code nil} constant.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Nil extends Literal {

	/**
	 * Constructs the abstract syntax of the {@code nil} constant.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 */

	public Nil(int pos) {
		super(pos);
	}

	/**
	 * Performs the type-checking of the {@code nil} constant
	 * by using a given type-checker.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the semantical {@code nil} type
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		return NilType.INSTANCE;
	}

	/**
	 * Translates this expression into its intermediate Kitten code.
	 * The result is a piece of code which pushes onto the stack
	 * the value of the expression (namely, a {@code const} bytecode
	 * that loads on the stack the value {@code nil})
	 * followed by the given {@code continuation}.
	 *
	 * @param continuation the code executed after this expression
	 * @return the code which evaluates this expression and continues with {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		return new CONST().followedBy(continuation);
	}
}