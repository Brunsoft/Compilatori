package absyn;

import semantical.TypeChecker;
import translation.Block;
import types.ClassType;
import types.Type;
import bytecode.NEWSTRING;

/**
 * A node of abstract syntax representing a string literal.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class StringLiteral extends Literal {

	/**
	 * The lexical value of the string literal.
	 */

	private final String value;

	/**
	 * Constructs the abstract syntax of a string literal.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param value the lexical value of the string literal
	 */

	public StringLiteral(int pos, String value) {
		super(pos);

		this.value = value;
	}

	/**
	 * Yields the lexical value of the string literal.
	 *
	 * @return the lexical value of the string literal
	 */

	public String getValue() {
		return value;
	}

	/**
	 * Yields a the string labeling the class of abstract syntax represented
	 * by this string literal. We redefine this method in order to include the lexical value
	 * of the string literal.
	 *
	 * @return a string describing the kind of this node of abstract syntax,
	 *         followed by the lexical value of this string literal
	 */

	@Override
	protected String label() {
		// in the string literal, we substitute the newline character with
		// an escape sequence so that it is literally reported by the dot compiler
		return super.label() + ": " + value.replaceAll("\n","\\\\\\\\n");
	}

	/**
	 * Performs the type-checking of a string literal. There is nothing to check.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the semantical {@code String} class type
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		// we type-check the String type since it is the only
		// class type that can be used in a program without
		// an explicit reference to its name (through constants like this)
		ClassType result = ClassType.mk("String");

		// normally, String.kit should be found
		if (result != null)
			result.typeCheck();

		return result;
	}

	/**
	 * Translates this expression into its intermediate Kitten code.
	 * The result is a piece of code which pushes onto the stack
	 * the value of the expression (namely, a {@code newstring} bytecode
	 * which loads on the stack the lexical value of this string
	 * literal expression) followed by the given {@code continuation}.
	 * The original stack elements are not modified.
	 *
	 * @param continuation the code executed after this expression
	 * @return the code which evaluates this expression and continues with {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		return new NEWSTRING(value).followedBy(continuation);
	}
}