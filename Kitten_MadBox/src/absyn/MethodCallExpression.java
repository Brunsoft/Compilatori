package absyn;

import java.io.FileWriter;
import java.util.Set;

import semantical.TypeChecker;
import translation.Block;
import types.ClassType;
import types.MethodSignature;
import types.Type;
import types.TypeList;
import bytecode.VIRTUALCALL;

/**
 * A node of abstract syntax representing a method call expression, that is,
 * a method call whose returned value must exist and is used.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class MethodCallExpression extends Expression {

	/**
	 * The abstract syntax of the receiver of the call, that is,
	 * of the expression whose value
	 * is used to select at run-time the target of the call.
	 */

	private final Expression receiver;

	/**
	 * The name of the method which is called.
	 */

	private final String name;

	/**
	 * The abstract syntax of the actual parameters of the call.
	 */

	private final ExpressionSeq actuals;

	/**
	 * The signature of the <i>static target</i> method of the call.
	 * The actual <i>dynamic target</i> method which will be called at runtime will
	 * be this static target or one of its redefinitions in subclasses.
	 * This field is {@code null} if type-checking has not been performed yet.
	 */

	private MethodSignature method;

	/**
	 * Constructs the abstract syntax of a method call expression.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param receiver the abstract syntax of the receiver of the call,
	 *                 that is, of the expression whose value
	 *                 is used to select at run-time the target of the call
	 * @param name the name of the method which is called
	 * @param actuals the abstract syntax of the actual parameters of the call.
	 */

	public MethodCallExpression(int pos, Expression receiver, String name, ExpressionSeq actuals) {
		super(pos);

		this.receiver = receiver;
		this.name = name;
		this.actuals = actuals;
	}

	/**
	 * Yields the abstract syntax of the receiver of the method call
	 * expression, that is, of the expression whose value
	 * is used to select at run-time the target of the call.
	 *
	 * @return the abstract syntax of the receiver of the method call
	 *         expression, that is, of the expression whose value
	 *         is used to select at run-time the target of the call
	 */

	public Expression getReceiver() {
		return receiver;
	}

	/**
	 * Yields the name of the method which is called.
	 *
	 * @return the name of the method which is called
	 */

	public String getName() {
		return name;
	}

	/**
	 * Yields the abstract syntax of the actual parameters of the call.
	 *
	 * @return the abstract syntax of the actual parameters of the call.
	 */

	public ExpressionSeq getActuals() {
		return actuals;
	}

	/**
	 * Yields a the string labeling the class of abstract syntax represented by
	 * this node. We redefine this method in order to add the method referenced
	 * by this call, if it has already been computed by a type-checker.
	 *
	 * @return a string describing the label of this node of abstract syntax,
	 *         followed by the static type of the expression and by the method
	 *         referenced by this call, if already computed by a type-checker
	 */

	@Override
	protected String label() {
		// if this expression has not been type-checked yet, we do not
		// report the method referenced
		if (method == null)
			return super.label();
		// otherwise we add the method referenced
		else
			return super.label() + "\\nreferences " + method;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of the method call expression.
	 * This amounts to adding arcs from the node for the method call
	 * expression to the abstract syntax for {@link #receiver}, {@link #name}
	 * and {@link #actuals}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("receiver", receiver.toDot(where), where);
		linkToNode("name", toDot(name, where), where);

		if (actuals != null)
			linkToNode("actuals", actuals.toDot(where), where);
	}

	/**
	 * Performs the type-checking of a method call expression
	 * by using a given type-checker. It type-checks the receiver and the
	 * formal parameters of the call. Then it checks that the receiver has
	 * class type and that there is exactly one possible static target
	 * method for the call. It returns the return type of the static target method.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the return type of the static target method
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		Type receiverType = receiver.typeCheck(checker);
		TypeList actualsTypes = actuals != null ? actuals.typeCheck(checker) : TypeList.EMPTY;

		// the receiver must have class type. Hence we cannot call methods of an
		// array. This is fine since arrays are subclasses of Object which has no methods
		if (!(receiverType instanceof ClassType))
			return error("class type required");
		else {
			// we collect the set of methods which are compatible with the
			// static types of the parameters, and have no other compatible method
			// that is more specific than them
			Set<MethodSignature> methods = ((ClassType) receiverType).methodsLookup(name,actualsTypes);

			if (methods.isEmpty())
				// there is no matching method!
				return error("no matching method for call to \"" + name + "\"");
			else if (methods.size() >= 2)
				// more than two matching methods, and none of them is
				// more specific of the other? Ambiguous call
				return error("call to method \"" + name + "\" is ambiguous");
			else
				// there is only one candidate: we return its return type
				return (method = (MethodSignature) methods.iterator().next()).getReturnType();
		}
	}

	/**
	 * Auxiliary method which translates this command into intermediate
	 * Kitten bytecode. Namely, it returns a code which starts with
	 * <br>
	 * <i>translation of {@link #receiver}</i><br>
	 * <i>translation of the first actual parameter</i><br>
	 * ...<br>
	 * <i>translation of the last actual parameter</i><br>
	 * {@code virtualcall method}<br>
	 * <br>
	 * and continues with the given {@code continuation}.
	 *
	 * @param continuation the continuation to be executed after this command
	 * @return the code executing this command and then {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		// we put an instruction which calls the method
		continuation = new VIRTUALCALL((ClassType) receiver.getStaticType(), method)
			.followedBy(continuation);

		// we translate the actual parameters
		if (actuals != null)
			continuation = actuals.translateAs(method.getParameters(), continuation);

		// we translate the receiver of the call
		return receiver.translate(continuation);
	}
}