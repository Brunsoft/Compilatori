package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import translation.Block;
import types.ClassType;
import types.FieldSignature;
import types.Type;
import bytecode.GETFIELD;
import bytecode.PUTFIELD;

/**
 * A node of abstract syntax representing the access to the field of an object.
 * Its concrete syntax is the {@code receiver.f} notation.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FieldAccess extends Lvalue {

    /**
     * The abstract syntax of the <i>receiver</i> of the field access,
     * that is, the expression that is on the left of the dot in the
     * {@code receiver.f} notation.
     */

    private final Expression receiver;

    /**
     * The name of the field which is accessed, that is, the symbol {@code f}
     * on the right of the dot in the {@code receiver.f} notation.
     */

    private final String name;

    /**
     * The signature of the field which is accessed. This is {@code null} if
     * type-checking has not been performed yet.
     */

    private FieldSignature field;

    /**
     * Constructs the abstract syntax of a field access expression.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param receiver the abstract syntax of the <i>receiver</i> of the field
     *                 access, that is, of the expression that is on the left
     *                 of the dot in the {@code receiver.f} notation
     * @param name the name of the field which is accessed, that is, the symbol
     *             {@code f} on the right of the dot in the {@code receiver.f} notation
     */

    public FieldAccess(int pos, Expression receiver, String name) {
    	super(pos);

    	this.receiver = receiver;
    	this.name = name;
    }

    /**
     * Yields the abstract syntax of the <i>receiver</i> of the field access,
     * that is, of the expression that is on the left
     * of the dot in the {@code receiver.f} notation.
     *
     * @return the abstract syntax of the receiver of the field access
     */

    public Expression getReceiver() {
    	return receiver;
    }

    /**
     * Yields the name of the field which is accessed, that is, the symbol {@code f}
     * on the right of the dot in the {@code receiver.f} notation.
     *
     * @return the name of the field which is accessed
     */

    public String getName() {
    	return name;
    }

    /**
     * Yields a the string labeling the class of abstract syntax represented by
     * this node. We redefine this method in order to add the field referenced
     * by this access, if it has already been computed by a type-checker.
     *
     * @return a string describing the kind of this node of abstract syntax,
     *         followed by the static type of the expression, if already
     *         computed through type-checking, and the field referenced by this access
     */

    @Override
    protected String label() {
    	// if this expression has not been type-checked yet, we do not
    	// report the field referenced
    	if (field == null)
    		return super.label();
    	// otherwise we add the field referenced
    	else
    		return super.label() + "\\nreferences " + field;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of a field access expression.
     * This amounts to adding arcs from the node for the field access
     * to the abstract syntax for {@link #receiver} and {@link #name}.
     *
     * @param where the file where the dot representation must be written
     */

    @Override
    protected void toDotAux(FileWriter where) throws java.io.IOException {
    	linkToNode("receiver", receiver.toDot(where), where);
    	linkToNode("name", toDot(name, where), where);
    }

    /**
     * Performs the type-checking of a field access expression,
     * by using a given type-checker. It type-checks [@link #receiver}
     * and checks if its static type is a class type and has a field
     * called {@link #name}.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the declared type of the field {@link #name} in the class
     *         that is the static type of {@link #receiver}
     */

    @Override
    protected Type typeCheckAux(TypeChecker checker) {
    	Type receiverType = receiver.typeCheck(checker);

    	// the receiver must have class type!
    	if (!(receiverType instanceof ClassType))
    		return error("class type required");

    	ClassType receiverClass = (ClassType) receiverType;

    	// we read the signature of a field called name in the static class of the receiver
    	if ((field = receiverClass.fieldLookup(name)) == null)
    		// there is no such field!
    		return error("unknown field " + name);

    	// we return the static type of the field name in the class of the receiver
    	return field.getType();
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack the value of
     * a field of an object. Namely, the code that is generated is<br>
     * <br>
     * translation of {@code receiver}<br>
     * {@code getfield field}<br>
     * <br>
     * followed by the given {@code continuation}.
     *
     * @param continuation the code executed after this expression
     * @return the code that evaluates this expression and continues
     *         with {@code continuation}
     */

    @Override
    public Block translate(Block continuation) {
    	return receiver.translate(new GETFIELD(field).followedBy(continuation));
    }

    /**
     * Generates the intermediate Kitten code which must be executed before
     * the evaluation of the rightvalue which is going to be assigned to this
     * variable. Namely, it translates {@link #receiver}.
     *
     * @param continuation the code which must be executed after this expression
     * @return the evaluation of {@link #receiver} followed by the given {@code continuation}
     */

    @Override
    public Block translateBeforeAssignment(Block continuation) {
    	return receiver.translate(continuation);
    }

    /**
     * Generates the intermediate Kitten code which must be executed after
     * the evaluation of the rightvalue which is going to be assigned to this
     * variable. Namely, it generates a [@code putfield} bytecode.
     *
     * @param continuation the code which must be executed after this expression
     * @return a {@code putfield} bytecode followed by {@code continuation}
     */

    @Override
    public Block translateAfterAssignment(Block continuation) {
    	return new PUTFIELD(field).followedBy(continuation);
    }
}