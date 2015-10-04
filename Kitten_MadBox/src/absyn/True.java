package absyn;

import semantical.TypeChecker;
import translation.Block;
import types.BooleanType;
import types.Type;
import bytecode.CONST;

/**
 * A node of abstract syntax representing a {@code true} Boolean constant.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class True extends Literal {

	/**
	 * Constructs the abstract syntax of a {@code true} Boolean constant.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 */

	public True(int pos) {
		super(pos);
	}

	/**
	 * Performs the type-checking of the {@code true} Boolean constant
	 * by using a given type-checker.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the semantical Boolean type
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		return BooleanType.INSTANCE;
	}

	/**
	 * Translates this expression into its intermediate Kitten code.
	 * The result is a piece of code which pushes onto the stack
	 * the value of the expression (namely, a {@code const} bytecode
	 * that loads on the stack the Boolean value <i>true</i>)
	 * followed by the given {@code continuation}.
	 *
	 * @param continuation the code executed after this expression
	 * @return the code which evaluates this expression and continues with {@code continuation}
	 */

	@Override
	public final Block translate(Block continuation) {
		return new CONST(true).followedBy(continuation);
	}
}