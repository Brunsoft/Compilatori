package absyn;

import java.io.FileWriter;
import java.util.Set;

import semantical.TypeChecker;
import translation.Block;
import types.ClassType;
import types.ConstructorSignature;
import types.Type;
import types.TypeList;
import bytecode.CONSTRUCTORCALL;
import bytecode.DUP;
import bytecode.NEW;

/**
 * A node of abstract syntax representing the creation of an object.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NewObject extends Expression {

	/**
	 * The name of the class that in instantiated.
	 */

	private final String className;

	/**
	 * The abstract syntax of the actual parameters passed to the constructor.
	 */

	private final ExpressionSeq actuals;

	/**
	 * The signature of the constructor which is called upon creation.
	 * This is {@code null} if type-checking has not been performed yet.
	 */

	private ConstructorSignature constructor;

	/**
	 * Constructs the abstract syntax of an object creation.
	 *
	 * @param pos the position in the source file where it starts
	 *            the concrete syntax represented by this abstract syntax
	 * @param className the name of the class which is instantiated
	 * @param actuals the abstract syntax of the actual parameters passed
	 *                to a constructor of {@link #className}
	 */

	public NewObject(int pos, String className, ExpressionSeq actuals) {
		super(pos);

		this.className = className;
		this.actuals = actuals;
	}

	/**
	 * Yields the name of the class that is instantiated.
	 *
	 * @return the name of the class that is instantiated
	 */

	public String getClassName() {
		return className;
	}

	/**
	 * Yields the abstract syntax of the actual parameters passed to a
	 * constructor of {@link #className}.
	 *
	 * @return the abstract syntax of the actual parameters passed to a
	 *         constructor of {@link #className}.
	 */

	public ExpressionSeq getActuals() {
		return actuals;
	}

	/**
	 * Yields a the string labeling the class of abstract syntax represented by
	 * this node. We redefine this method in order to add the constructor referenced
	 * by this object creation, if it has already been computed by a type-checker.
	 *
	 * @return a string describing the kind of this node of abstract syntax,
	 *         followed by the static type of the expression and by the constructor
	 *         referenced by this object creation, if already computed by a type-checker
	 */

	@Override
	protected String label() {
		// if this expression has not been type-checked yet, we do not
		// report the constructor referenced
		if (constructor == null)
			return super.label();
		// otherwise we add the constructor referenced
		else
			return super.label() + "\\nreferences " + constructor;
	}

	/**
	 * Adds abstract syntax class-specific information in the dot file
	 * representing the abstract syntax of the object creation.
	 * This amounts to adding arcs from the node for the object creation
	 * to the abstract syntax for {@link #className} and {@link #actuals}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("className", toDot(className, where), where);
		if (actuals != null)
			linkToNode("actuals", actuals.toDot(where), where);
	}

	/**
	 * Performs the type-checking of an object creation expression
	 * by using a given type-checker. It type-checks the
	 * formal parameters of the call. Then it checks that
	 * there is exactly one possible target constructor.
	 * It returns the semantical class type {@link #className}.
	 *
	 * @param checker the type-checker to be used for type-checking
	 * @return the semantical class type {@link #className}
	 */

	@Override
	protected Type typeCheckAux(TypeChecker checker) {
		ClassType target = ClassType.mk(className);

		target.typeCheck();

		TypeList actualsTypes = actuals != null ? actuals.typeCheck(checker) : TypeList.EMPTY;

		// we collect the set of constructors which are compatible
		// with the static types of the parameters, and have no other
		// compatible constructor which is more specific than them
		Set<ConstructorSignature> constructors = target.constructorsLookup(actualsTypes);

		if (constructors.isEmpty())
			// there is no matching constructor!
			error("no matching constructor for \"" + className + "\"");
		else if (constructors.size() >= 2)
			// more than two matching constructors, and none of them is
			// more specific of the other? Ambiguous call
			error("call to constructor of \"" + className + "\" is ambiguous");
		else
			// there is only one candidate, that's fine
			constructor = (ConstructorSignature)constructors.iterator().next();

		// we return the semantical class type className
		return target;
	}

	/**
	 * Translates this command into intermediate Kitten bytecode. Namely, it returns
	 * a code that creates a new object and then calls the constructor
	 * identified during the type-checking. Namely, it returns the code
	 * <br>
	 * {@code new constructor.getDefiningClass()}<br>
	 * {@code dup}<br>
	 * <i>translation of the parameters of the constructor</i><br>
	 * {@code constructorcall constructor}<br>
	 * <br>
	 * followed by the given {@code continuation}.
	 *
	 * @param continuation the continuation to be executed after this command
	 * @return the code executing this command and then {@code continuation}
	 */

	@Override
	public Block translate(Block continuation) {
		continuation = new CONSTRUCTORCALL(constructor).followedBy(continuation);
		if (actuals != null)
			continuation = actuals.translateAs(constructor.getParameters(), continuation);

		return new NEW(constructor.getDefiningClass()).followedBy
			(new DUP(constructor.getDefiningClass()).followedBy(continuation));
	}
}