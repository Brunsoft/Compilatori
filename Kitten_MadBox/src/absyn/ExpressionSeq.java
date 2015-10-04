package absyn;

import java.io.FileWriter;
import java.io.IOException;

import semantical.TypeChecker;
import translation.Block;
import types.TypeList;

/**
 * A node of abstract syntax representing a sequence (list) of Kitten expressions.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ExpressionSeq extends Absyn {

	/**
	 * The head of the list.
	 */

	private final Expression head;

	/**
	 * The tail of the list.
	 */

	private final ExpressionSeq tail;

	/**
	 * Constructs the abstract syntax of a non-empty sequence (list) of expressions.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 * @param head the head of the list
	 * @param tail the tail of the list. This might be {@code null}
	 */

	public ExpressionSeq(int pos, Expression head, ExpressionSeq tail) {
		super(pos);

		this.head = head;
		this.tail = tail;
	}

	/**
	 * Yields the head of this sequence of expressions.
	 *
	 * @return the head of this sequence of expressions
	 */

	public Expression getHead() {
		return head;
	}

	/**
	 * Yields the tail of this sequence of expressions, if any.
	 *
	 * @return the tail of this sequence of expressions, if any. Yields {@code null}
	 *         if there is no tail
	 */

	public ExpressionSeq getTail() {
		return tail;
	}

	/**
	 * Writes in the specified file a dot representation of the abstract syntax
	 * of this sequence of expressions.
	 *
	 * @param where the file where the dot representation must be written
	 * @return the name used to refer to this node in the dot file
	 * @throws IOException if there is a problem while writing into the file
	 */

	public final String toDot(FileWriter where) throws IOException {
		// dumps in the file the name of the node in the dot file,
		// followed by the label used to show the node to the user of dot
		where.write(dotNodeName() + " [ label = \"" + label() + "\"];\n");

		// links this node with the node for the abstract syntax of the head
		linkToNode("head", head.toDot(where), where);

		// links this node to the node for the abstract syntax of the tail
		if (tail != null)
			boldLinkToNode("tail", tail.toDot(where), where);

		return dotNodeName();
	}

	/**
	 * Type-checks the expressions in this sequence.
	 *
	 * @param checker the type-checker used for type-checking
	 * @return the list of the Kitten types of the elements in this sequence
	 */

	public TypeList typeCheck(TypeChecker checker) {
		TypeList result = tail != null ? tail.typeCheck(checker) : TypeList.EMPTY;

		return result.push(head.typeCheck(checker));
	}

	/**
	 * Translates this sequence of expressions into intermediate Kitten
	 * code, by requiring that each
	 * expression leaves on the stack a value of the corresponding type
	 * in the list of types passed as a parameter. The result
	 * is an intermediate Kitten code which loads on the stack the values
	 * of the expressions, with the value of the last expression on top, and
	 * then continues with the given {@code continuation}.
	 * This methods calls itself recursively on {@link #tail} and then calls
	 * {@code absyn.Expression.translateAs} on {@link #head}.
	 *
	 * @param types the list of types of the values which must be left onto the
	 *              stack by the translation of this sequence of expressions
	 * @param continuation the code which must follow the translation of
	 *                     these expressions
	 * @return the code that evaluates the expressions in sequence and then
	 *         continues with {@code continuation}
	 */

	public Block translateAs(TypeList types, Block continuation) {
		return head.translateAs(types.getHead(),
			tail != null ? tail.translateAs(types.getTail(), continuation) : continuation);
	}
}