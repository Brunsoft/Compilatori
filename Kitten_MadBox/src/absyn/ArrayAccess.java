package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;
import types.ArrayType;
import types.Type;
import bytecode.ARRAYLOAD;
import bytecode.ARRAYSTORE;

/**
 * A node of abstract syntax representing the access to an element of an array.
 * Its concrete syntax is the {@code array[index]} notation.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ArrayAccess extends Lvalue {

	/**
	 * The abstract syntax of the {@code array} expression in the
	 * {@code array[index]} notation.
	 */

	private Expression array;

	/**
	 * The abstract syntax of the {@code index} expression in the
	 * {@code array[index]} notation.
	 */

	private Expression index;

	/**
	 * Constructs the abstract syntax of an access to an array element.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param array the abstract syntax of the {@code array} expression in the
	 *              {@code array[index]} notation
	 * @param index the abstract syntax of the {@code index} expression in the
	 *              {@code array[index]} notation
	 */

	public ArrayAccess(int pos, Expression array, Expression index) {
		super(pos);

		this.array = array;
		this.index = index;
	}

	/**
	 * Yields the abstract syntax of the {@code array} expression in the
	 * {@code array[index]} notation.
	 *
	 * @return the abstract syntax of the {@code array} expression in the
	 *         {@code array[index]} notation
	 */

	public Expression getArray() {
		return array;
	}

	/**
	 * Yields the abstract syntax of the {@code index} expression in the
	 * {@code array[index]} notation.
	 *
	 * @return the abstract syntax of the {@code index} expression in the
	 *         [@code array[index]} notation
	 */

	public Expression getIndex() {
		return index;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of an access to an array element.
	 * This amounts to adding arcs from the node for the array access
	 * to the abstract syntax for its {@link #array} and {@link #index} components.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("array", array.toDot(where), where);
		linkToNode("index", index.toDot(where), where);
	}

	/**
	 * Performs the type-checking of an array access expression,
	 * by using a given type-checker. It type-checks {@link #array} and
	 * {@link #index} of the {@code array[index]} notation.
	 * It requires the former to have array type and the latter to be {@code int}.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the static type of the elements of the array type
	 *         which is the static type of {@link #array}
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		// we type-check the reference to the array
		Type arrayType = array.typeCheck(checker);

		// we type-check the index and require it to have integer type
		index.mustBeInt(checker);

		// the array expression must have array type
		if (!(arrayType instanceof ArrayType))
			return error("array type required");

		// we return the static type of the elements of the array
		return ((ArrayType) arrayType).getElementsType();
	}

	/**
	 * Translates this expression into its intermediate Kitten code.
	 * The result is a piece of code which pushes onto the stack
	 * the value of an element of an array. Namely, the code which is generated is<br>
	 * <br>
	 * <i>translation of {@code array}</i><br>
	 * <i>translation of {@code index}</i><br>
	 * <i>{@code arrayload} type of the elements of the array</i><br>
	 * <br>
	 * followed by the given {@code continuation}.
	 *
	 * @param continuation the code executed after this expression
	 * @return the code that evaluates this expression and continues
	 *         with {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		return array.translate(index.translate
			(new ARRAYLOAD(getStaticType()).followedBy(continuation)));
	}

	/**
	 * Generates the intermediate Kitten code that must be executed before
	 * the evaluation of the rightvalue which is going to be assigned to this
	 * variable. Namely, it translates the {@code array} and
	 * the {@code index} of this array element access.
	 *
	 * @param continuation the code which must be executed after this expression
	 * @return the evaluation of {@code array} followed by that of
	 *         {@code index} followed by the given {@code continuation}
	 */

	@Override
	public Block translateBeforeAssignment(Block continuation) {
		return array.translate(index.translate(continuation));
	}

	/**
	 * Generates the intermediate Kitten code that must be executed after
	 * the evaluation of the rightvalue which is going to be assigned to this
	 * variable. Namely, it generates an {@code arraystore} bytecode.
	 *
	 * @param continuation the code which must be executed after this expression
	 * @return an {@code arraystore} bytecode followed by {@code continuation}
	 */

	@Override
	public Block translateAfterAssignment(Block continuation) {
		return new ARRAYSTORE(getStaticType()).followedBy(continuation);
	}
}