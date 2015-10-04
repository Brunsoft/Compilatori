package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import types.ClassType;
import types.MethodSignature;
import types.Type;
import types.TypeList;
import types.VoidType;

/**
 * A node of abstract syntax representing the declaration of a method of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class MethodDeclaration extends CodeDeclaration {

	/**
	 * The abstract syntax of the return type of the method.
	 */

	private final TypeExpression returnType;

	/**
	 * The name of the method.
	 */

	private final String name;

	/**
	 * Constructs the abstract syntax of a method declaration.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 * @param returnType the abstract syntax of the return type of the method
	 * @param name the name of the method
	 * @param formals the abstract syntax of the formal parameters of the method
	 * @param body the abstract syntax of the body of the method
	 * @param next the abstract syntax of the declaration of the
	 *             subsequent class member, if any
	 */

	public MethodDeclaration(int pos, TypeExpression returnType, String name,
			FormalParameters formals, Command body, ClassMemberDeclaration next) {
		super(pos, formals, body, next);

		this.name = name;
		this.returnType = returnType;
	}

	/**
	 * Yields the abstract syntax of the return type of the method.
	 *
	 * @return the abstract syntax of the return type of the method
	 */

	public TypeExpression getReturnType() {
		return returnType;
	}

	/**
	 * Yields the name of this method.
	 *
	 * @return the name of this method
	 */

	public String getName() {
		return name;
	}

	/**
	 * Yields the signature of this method declaration.
	 *
	 * @return the signature of this method declaration. Yields {@code null}
	 *         if type-checking has not been performed yet
	 */

	@Override
	public MethodSignature getSignature() {
		return (MethodSignature) super.getSignature();
	}

	/**
	 * Adds arcs between the dot node for this piece of abstract syntax
	 * and those representing [@link {@link #returnType}, {@link #name},
	 * {@link #formals} and {@link #body}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("returnType", returnType.toDot(where), where);
		linkToNode("name", toDot(name, where), where);

		if (getFormals() != null)
			linkToNode("formals", getFormals().toDot(where), where);

		linkToNode("body", getBody().toDot(where), where);
	}

	/**
	 * Adds the signature of this method declaration to the given class.
	 *
	 * @param clazz the class where the signature of this method declaration must be added
	 */

	@Override
	protected void addTo(ClassType clazz) {
		Type rt = returnType.toType();
		TypeList pars = getFormals() != null ? getFormals().toType() : TypeList.EMPTY;
		MethodSignature mSig = new MethodSignature(clazz, rt, pars, name, this);

		clazz.addMethod(name, mSig);

		// we record the signature of this method inside this abstract syntax
		setSignature(mSig);	
	}

	/**
	 * Type-checks this method declaration.
	 * It first checks that if this method overrides a method of a superclass
	 * then the return type is a subtype of that of the overridden method.
	 * Then it builds a type-checker whose only variable in scope is
	 * {@code this} of type {@code clazz} and the parameters of the method,
	 * and where return instructions of type {@code returnType} are allowed.
	 * It then type-checks the body of the method in that type-checker.
	 * It finally checks that if this method does not return {@code void},
	 * then every execution path ends with a {@code return} command.
	 *
	 * @param clazz the semantical type of the class where this method occurs
	 */

	@Override
	protected void typeCheckAux(ClassType clazz) {
		TypeChecker checker;
		ClassType superclass;
		MethodSignature overridden;
		Type rt = returnType.typeCheck();

		// we build a type-checker which signals errors for the source code
		// of the class where this method is defined,
		// whose only variables in scope is this of type
		// clazz and the parameters of the method, and
		// where return instructions of type returnType are allowed
		checker = new TypeChecker(rt,clazz.getErrorMsg());

		// the main method is the only <i>static</i> method, where there is no this variable
		if (!getSignature().getName().equals("main"))
			checker = checker.putVar("this", clazz);

		// we enrich the type-checker with the formal parameters
		checker = getFormals() != null ? getFormals().typeCheck(checker) : checker;

		TypeList pars = getFormals() != null ? getFormals().typeCheck() : null;

		// we check if this method overrides a method of some superclass
		superclass = clazz.getSuperclass();
		if (superclass != null) {
			overridden = superclass.methodLookup(name, pars);

			if (overridden != null)
				// it does override a method of a superclass. We check
				// that its return type has been refined. We use the
				// canBeAssignedToSpecial method so that
				// void can be overridden into void
				if (!rt.canBeAssignedToSpecial(overridden.getReturnType()))
					error(checker, "illegal return type for overriding method \"" +
							name + "\". Was " + overridden.getReturnType());
		}

		// we type-check the body of the method in the resulting type-checker
		getBody().typeCheck(checker);

		// we check that there is no dead-code in the body of the method
		boolean stopping = getBody().checkForDeadcode();

		// we check that if the method does not return void then
		// every syntactical execution path in the method ends with
		// a return command (continue and break are forbidden in this position)
		if (rt != VoidType.INSTANCE && !stopping)
			error(checker, "missing return statement");
	}
}