package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;
import types.Type;

/**
 * A node of abstract syntax representing an assignment command.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Assignment extends Command {

    /**
     * The left-hand side of the assignment. Note that this is
     * an {@link #absyn.Lvalue} rather than, more generally, an expression, since
     * it is meaningless to assign a value to, say, an integer or addition.
     */

    private final Lvalue lvalue;

    /**
     * The right-hand side of the assignment.
     */

    private final Expression rvalue;

    /**
     * Constructs the abstract syntax of an assignment command.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param lvalue the left-hand side of the assignment
     * @param rvalue the right-hand side of the assignment
     */

    public Assignment(int pos, Lvalue lvalue, Expression rvalue) {
    	super(pos);

    	this.lvalue = lvalue;
    	this.rvalue = rvalue;
    }

    /**
     * Yields the abstract syntax of the left-hand side of the assignment.
     *
     * @return the abstract syntax of the left-hand side of the assignment
     */

    public Lvalue getLvalue() {
    	return lvalue;
    }

    /**
     * Yields the abstract syntax of the right-hand side of the assignment.
     *
     * @return the abstract syntax of the right-hand side of the assignment
     */

    public Expression getRvalue() {
    	return rvalue;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of the assignment command.
     * This amounts to adding two arcs from the node for the assignment
     * command to the abstract syntax for its {@link #lvalue} and
     * {@link #rvalue} components.
     *
     * @param where the file where the dot representation must be written
     */

    @Override
    protected void toDotAux(FileWriter where) throws java.io.IOException {
    	linkToNode("lvalue", lvalue.toDot(where), where);
    	linkToNode("rvalue", rvalue.toDot(where), where);
    }

    /**
     * Performs the type-checking of this assignment by using a given
     * type-checker. Namely, it type-checks both sides of the assignment
     * and then checks that the type of the right-hand side
     * can be assigned to that of the left-hand side.
     *
     * @param checker the type-checker to be used for type-checking
     * @return {@code checker}
     */

    @Override
    protected TypeChecker typeCheckAux(TypeChecker checker) {
    	// we type-check the left-hand side
    	Type left = lvalue.typeCheck(checker);

    	// we type-check the right-hand side
    	Type right = rvalue.typeCheck(checker);

    	// if the right-hand side cannot be assigned to the left-hand side
    	// then the assignment is illegal
    	if (!right.canBeAssignedTo(left))
    		error(right + " cannot be assigned to " + left);

    	// the type-checker is not modified
    	return checker;
    }

    /**
     * Checks that this assignment does not contain <i>dead-code</i>, that is,
     * commands which can never be executed. This is always true for assignments.
     *
     * @return false, since this command never terminates with a {@code return}
     */

    @Override
    public boolean checkForDeadcode() {
    	return false;
    }

    /**
     * Translates this command into intermediate
     * Kitten bytecode. Namely, it returns a code that starts with
     * <br>
     * <i>translation of {@link #lvalue} through
     *    {@code translateBeforeAssignment()}</i><br>
     * <i>translation of {@link #rvalue} through
     *    {@code translateAs(lvalue.getStaticType())}</i><br>
     * <i>translation of {@link #lvalue} through
     *    {@code translateAfterAssignment()}</i><br>
     * <br>
     * and continues with {@code continuation}.
     *
     * @param continuation the continuation to be executed after this command
     * @return the code executing this command and then {@code continuation}
     */

    @Override
    public Block translate(Block continuation) {
    	return lvalue.translateBeforeAssignment
   			(rvalue.translateAs(lvalue.getStaticType(),
   				lvalue.translateAfterAssignment(continuation)));
    }
}