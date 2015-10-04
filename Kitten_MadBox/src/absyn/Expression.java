package absyn;

import java.io.FileWriter;
import java.io.IOException;

import semantical.TypeChecker;
import types.BooleanType;
import types.FloatType;
import types.IntType;
import types.Type;
import types.CodeSignature;
import translation.Block;
import bytecode.CAST;
import bytecode.IF_TRUE;

/**
 * A node of abstract syntax representing a Kitten expression.
 * Expressions are syntactical structures which yield a value.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Expression extends Absyn {

	/**
	 * The static type of this expression. This is {@code null} if type-checking
	 * has not yet been computed.
	 */

	private Type staticType;

	/**
	 * The type-checker used during the last type-checking of this
	 * expression. This is {@code null} if this expression has never
	 * been type-checked before.
	 */

	private TypeChecker checker;

	/**
	 * Constructs the abstract syntax of an expression.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 */

	protected Expression(int pos) {
		super(pos);
	}

	/**
	 * Yields a the string labeling the class of abstract syntax represented by
	 * this node. We redefine this method in order to add the static type
	 * of the expression, if it has already been computed by a type-checker.
	 *
	 * @return a string describing the label of this node of abstract syntax,
	 *         followed by the static type of the expression, if already
	 *         computed through type-checking
	 */

	@Override
	protected String label() {
		// if this expression has not been type-checked yet, we do not report its static type
		if (staticType == null)
			return super.label();
		// otherwise we add its static type
		else
			return super.label() + " [" + staticType + "]";
	}

	/**
	 * Type-checks this expression and checks that it has Boolean type.
	 * Signals an error otherwise.
	 *
	 * @param checker the type-checker to be used for type-checking
	 */

	protected void mustBeBoolean(TypeChecker checker) {
		if (typeCheck(checker) != BooleanType.INSTANCE)
			error("boolean expected");
	}

	/**
	 * Type-checks this expression and checks that it has integer type.
	 * Signals an error otherwise.
	 *
	 * @param checker the type-checker to be used for type-checking
	 */

	protected void mustBeInt(TypeChecker checker) {
		if (typeCheck(checker) != IntType.INSTANCE)
			error("integer expected");
	}

	/**
	 * Returns the static semantical type of this expression, as computed during
	 * last type-checking.
	 *
	 * @return the static semantical type of this expression, if it has
	 *         already been type-checked. Yields {@code null} otherwise
	 */

	public final Type getStaticType() {
		return staticType;
	}

	/**
	 * Yields the type-checker used during the last type-checking of this expression.
	 *
	 * @return the type-checker used during the last type-checking of this
	 *         expression. Yields {@code null} if this expression has not
	 *         been type-checked yet
	 */

	public final TypeChecker getTypeChecker() {
		return checker;
	}

	/**
	 * Writes in the specified file a dot representation of the abstract syntax
	 * of this expression. By default, it writes a single dot node for this
	 * node of abstract syntax and it calls the auxiliary method
	 * {@link #toDotAux(FileWriter)}. Subclasses should redefine the latter
	 * in order to consider components of expressions.
	 *
	 * @param where the file where the dot representation must be written
	 * @return the name used to refer to this node in the dot file,
	 *         as computed by {@link #dotNodeName()}
	 * @throws IOException if there is a problem while writing into the file
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
	 * Wrapper function that performs the type-checking of this expression
	 * by using a given type-checker. It calls the expression-specific
	 * type-checking method {@link #typeCheckAux(TypeChecker)} and then stores
	 * the static type of the expression into {@link #staticType}. It records
	 * the type-checker inside {@link #checker}.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the static type of the expression, as computed by the type-checker
	 */

	public final Type typeCheck(TypeChecker checker) {
		return staticType = typeCheckAux(this.checker = checker);
	}

	/**
	 * Performs the type-checking of this expression by using a given type-checker.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the static type of the expression, as computed by the type-checker
	 */

	protected abstract Type typeCheckAux(TypeChecker checker);

	/**
	 * Translates this expression into its intermediate Kitten code.
	 * The result is a piece of code which pushes onto the stack
	 * the value of the expression (the original stack elements are not
	 * modified) followed by a {@code continuation}
	 *
	 * @param continuation the code executed after this expression
	 * @return the code which evaluates this expression and continues
	 *         with {@code continuation}
	 */

	public abstract Block translate(Block continuation);

	/**
	 * Translates this expression by requiring that it leaves onto the
	 * stack a value of the given type. The only difference with
	 * {@link #translate(CodeSignature, Block)} is that it checks if
	 * the value left on top of the stack needs a type promotion.
	 *
	 * @param type the type of the value which must be left onto the
	 *             stack by the translation of this expression
	 * @param continuation the continuation to be executed after this expression
	 * @return the code which evaluates this expression, followed by a
	 *         possible type promotion to {@code type}, if needed
	 */

	public final Block translateAs(Type type, Block continuation) {
		if (staticType == IntType.INSTANCE && type == FloatType.INSTANCE)
			// type promotion
			continuation = new CAST(IntType.INSTANCE, FloatType.INSTANCE).followedBy(continuation);

		return translate(continuation);
	}

	/**
	 * Translates this expression by assuming that it has {@code boolean} type.
	 * This must have been guaranteed by a previous type-checking.
	 * Depending on the truth of that {@code boolean} value, control is routed
	 * to one of two possible destinations, through an {@code if_true}
	 * bytecode. Subclasses may redefine to get more improved code.
	 *
	 * @param yes the continuation that is the <i>yes</i> destination
	 * @param no the continuation that is the <i>no</i> destination
	 * @return the code that evaluates the expression and, on the basis
	 *         of its {@code boolean} value, routes the computation to the
	 *         {@code yes} or {@code no} continuation, respectively
	 */

	public Block translateAsTest(Block yes, Block no) {
		return translate(new Block(new IF_TRUE(), yes, no));
	}

	/**
	 * Outputs an error message to the user, by using the type-checker
	 * used during the last type-checking. Returns a default type
	 * which is used to continue the type-checking anyway.
	 *
	 * @param msg the message to be output
	 * @return the {@code int} type
	 */

	protected Type error(String msg) {
		error(checker, msg);

		// this type is fine for most cases
		return IntType.INSTANCE;
	}
}