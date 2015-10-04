package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;
import types.NumericalType;
import types.ReferenceType;
import types.Type;
import bytecode.CAST;

/**
 * A node of abstract syntax representing a cast expression.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Cast extends Expression {

    /**
     * The abstract syntax of the type the expression is cast into.
     */

    private final TypeExpression type;

    /**
     * The abstract syntax of the expression which is cast into {@link #type}.
     */

    private final Expression expression;

    /**
     * Constructs the abstract syntax of a cast expression.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param type the abstract syntax of the type {@code expression} is cast into
     * @param expression the abstract syntax of the expression to be cast into {@code type}
     */

    public Cast(int pos, TypeExpression type, Expression expression) {
    	super(pos);

    	this.type = type;
    	this.expression = expression;
    }

    /**
     * Yields the abstract syntax of the type {@link #expression} is cast into.
     *
     * @return the abstract syntax of the type {@link #expression} is cast into
     */

    public TypeExpression getType() {
    	return type;
    }

    /**
     * Yields the abstract syntax of the expression which is cast into {@link #type}.
     *
     * @return the abstract syntax of the expression which is cast into {@link #type}
     */

    public Expression getExpression() {
    	return expression;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of a cast expression.
     * This amounts to adding two arcs from the node for the cast expression
     * to the abstract syntax for its {@link #expression} and {@link #type}.
     *
     * @param where the file where the dot representation must be written
     */

    @Override
    protected void toDotAux(FileWriter where) throws java.io.IOException {
    	linkToNode("type", type.toDot(where), where);
    	linkToNode("expression", expression.toDot(where), where);
    }

    /**
     * Performs the type-checking of the cast expression
     * by using a given type-checker. It type-checks the type and the
     * expression, and then checks that the cast is legal:
     * <br><br>
     * <lu>
     * <li> the two types must be different, otherwise the cast would be useless
     * <li> a reference type can be cast into any of its subtypes
     *      but a run-time check is required for reference types
     * <li> no other cast is legal.
     * </lu>
     *
     * @param checker the type-checker to be used for type-checking
     * @return the semantical type of {@link #type}
     */

    @Override
    protected Type typeCheckAux(TypeChecker checker) {
    	Type fromType = expression.typeCheck(checker);
    	Type intoType = type.typeCheck();

    	if (fromType == intoType)
    		error("You do not need to cast a " + fromType + " into itself");
    	// the semantical type of <tt>expression</tt> must be more
    	// general than the semantical type of <tt>type</tt>
    	else if (!intoType.canBeAssignedTo(fromType))
    		error(fromType + " cannot be cast into " + intoType);

    	// the static type of the cast expression is the semantical type of type
    	return intoType;
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of {@link #expression}, provided that it has the specified
     * {@link #type}. The original stack elements are not
     * modified. Namely, it generates the code which starts with<br>
     * <br>
     * <i>translation of {@link #expression}</i><br>
     * {@code cast from expression.getStaticType() into getStaticType()}</tt><br>
     * <br>
     * and continues with the given {@code continuation}.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with {@code continuation}
     */

    @Override
    public Block translate(Block continuation) {
    	Type fromType = expression.getStaticType();

    	// we get the type towards which this expression is cast
    	Type intoType = getStaticType();

    	if (intoType instanceof NumericalType)
    		return expression.translate
   				(new CAST((NumericalType) fromType, (NumericalType) intoType).followedBy(continuation));
    	else
    		return expression.translate
   				(new CAST((ReferenceType) fromType, (ReferenceType) intoType).followedBy(continuation));
    }
}