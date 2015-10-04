package absyn;

import translation.Block;

/**
 * A node of abstract syntax representing a <i>leftvalue</i>, that is,
 * an expression which refers to the container of a value, and which
 * can hence be used on the left of an assignment.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Lvalue extends Expression {

	/**
	 * Constructs the abstract syntax of a leftvalue.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 */

	protected Lvalue(int pos) {
		super(pos);
	}

	/**
	 * Generates the intermediate Kitten code which must be executed before
	 * the evaluation of the rightvalue which is going to be assigned to this leftvalue.
	 *
	 * @param continuation the code which must be executed after this expression. Usually,
	 *                     this is the code that evaluates a rightvalue in an assignment
	 * @return a code that is executed before the evaluation of a rightvalue that
	 *         is going to be assigned to this leftvalue, followed by {@code continuation}
	 */

	public abstract Block translateBeforeAssignment(Block continuation);

	/**
	 * Generates the intermediate Kitten code that must be executed after the
	 * evaluation of the rightvalue which is going to be assigned to this leftvalue.
	 *
	 * @param continuation the code which must be executed after this expression
	 * @return a code that is executed after the evaluation of a rightvalue that
	 *         is going to be assigned to this leftvalue, followed by {@code continuation}
	 */

	public abstract Block translateAfterAssignment(Block continuation);
}