package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;
import types.ArrayType;
import types.Type;
import bytecode.NEWARRAY;

/**
 * A node of abstract syntax representing an array creation expression.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NewArray extends Expression {

	/**
	 * The abstract syntax of the type of the elements of the array.
	 */

	private final TypeExpression elementsType;

	/**
	 * The abstract syntax of the expression representing the size of the array.
	 */

	private final Expression size;

	/**
	 * Constructs the abstract syntax of an array creation expression.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param elementsType the abstract syntax of the type of the elements of the array
	 * @param size the abstract syntax of the expression representing the size of the array
	 */

	public NewArray(int pos, TypeExpression elementsType, Expression size) {
		super(pos);

		this.elementsType = elementsType;
		this.size = size;
	}

	/**
	 * Yields the abstract syntax of the type of the elements of the array.
	 *
	 * @return the abstract syntax of the type of the elements of the array
	 */

	public TypeExpression getElementsType() {
		return elementsType;
	}

	/**
	 * Yields the abstract syntax of the expression representing the size of the array.
	 *
	 * @return the abstract syntax of the expression representing the size of the array
	 */

	public Expression getSize() {
		return size;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of an array creation expression.
	 * This amounts to adding two arcs from the node for the array creation to the
	 * abstract syntax for its {@link #elementsType} and {@link #size}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("elementsType", elementsType.toDot(where), where);
		linkToNode("size", size.toDot(where), where);
	}

	/**
	 * Performs the type-checking of the array creation expression
	 * by using a given type-checker. It type-checks the expression
	 * representing the size of the array and requires that it has
	 * integer type. It then type-checks the type of the elements of the array.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the array type for the type of the elements of the array
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		size.mustBeInt(checker);

		return ArrayType.mk(elementsType.typeCheck());
	}

	/**
	 * Translates this command into intermediate
	 * Kitten bytecode. Namely, it returns a code which pushes onto the stack
	 * a reference to a new array of the prescribed {@link #elementsType}
	 * and {@link #size}. The original stack elements are not
	 * modified. Namely, it returns a code which starts with<br>
	 * <br>
	 * <i>translation of {@link #size}<br>
	 * {@code newarray elementsType.getStaticType()}</i><br>
	 * <br>
	 * and continues with the given {@code continuation}.
	 *
	 * @param continuation the continuation to be executed after this command
	 * @return the code executing this command and then {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		return size.translate(new NEWARRAY(elementsType.getStaticType()).followedBy(continuation));
	}
}