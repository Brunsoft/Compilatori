package types;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lexical.Lexer;
import syntactical.Parser;
import translation.Program;
import absyn.ClassDefinition;
import errorMsg.ErrorMsg;

/**
 * The type of a class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public final class ClassType extends ReferenceType {

	/**
	 * The name of this class.
	 */

	private final String name;

	/**
	 * The superclass of this class, if any.
	 */

	private final ClassType superclass;

	/**
	 * The direct subclasses of this class, if any.
	 */

	private final List<ClassType> subclasses;

	/**
	 * The set of instances of this class. This is a cache for {@link #getInstances()}.
	 */

	private List<ClassType> instances;

	/**
	 * A map from field symbols to their signature.
	 */

	private final Map<String, FieldSignature> fields = new HashMap<>();

	/**
	 * The set of constructor signatures in this class.
	 */

	private final Set<ConstructorSignature> constructors = new HashSet<>();
	
	/**
	 * The set of fixtures signatures in this class.
	 */

	private Set<FixtureSignature> fixtures = new HashSet<>();
	
	/**
	 * The set of tests signatures in this class.
	 */

	private Set<TestSignature> tests = new HashSet<>();

	/**
	 * A map from method symbols to the set of signatures of the methods with
	 * that name. Because of overloading, more than one method might have a given name.
	 */

	private final Map<String, Set<MethodSignature>> methods = new HashMap<>();

	/**
	 * The utility for issuing errors about this class.
	 */
	
	private ErrorMsg errorMsg;

	/**
	 * The abstract syntax of this class.
	 */
	
	private final ClassDefinition abstractSyntax;

	/**
	 * True if and only if this class has been already type-checked.
	 */
	
	private boolean typeChecked;

	/**
	 * Constructs a class type with the given name. If the class
	 * cannot be found or contains a syntactical error, a fictitious class
	 * with no fields, no constructors and no methods is created.
	 *
	 * @param name the name of the class
	 */
	
	private ClassType(String name) {
		// we record its name
		this.name = name;
	
		// there are no subclasses at the moment
		this.subclasses = new ArrayList<>();
	
		// we record this object for future lookup
		memory.put(name, this);
	
		// we perform lexical and syntactical analysis. The result is
		// the abstract syntax of this class definition
		ClassDefinition abstractSyntax;

		try {
			Parser parser = new Parser(new Lexer(name));
			errorMsg = parser.getErrorMsg();
			abstractSyntax = (ClassDefinition) parser.parse().value;
		}
		catch (Exception e) {
			// there is a syntax error in the class text or the same class
			// cannot be found on the file system or cannot be type-checked:
			// we build a fictitious syntax for the class, so that the processing can go on
			if (name.equals("Object"))
				abstractSyntax = new ClassDefinition(0, name, null, null);
			else
				abstractSyntax = new ClassDefinition(0, name, "Object", null);
		}

		// we add the fields, constructors and methods of this class
		(this.abstractSyntax = abstractSyntax).addMembersTo(this);

		if (name.equals("Object")) {
			// if this is Object, we take note of the top of the hierarchy of the reference types
			setObjectType(this);
			superclass = null;
		}
		else
			// if this is not Object, we also create its superclass and take
			// note that we are a direct subclass of our superclass
			(superclass = mk(abstractSyntax.getSuperclassName())).subclasses.add(this);
	}

	/**
	 * Yields the superclass of this class type, if any.
	 *
	 * @return the superclass of this class type.
	 */

	public ClassType getSuperclass() {
		return superclass;
	}

	/**
	 * Yields the direct subclasses of this class.
	 *
	 * @return the direct subclasses of this class, if any
	 */

	public Iterable<ClassType> getSubclasses() {
		return subclasses;
	}

	/**
	 * Yields the name of this class.
	 * 
	 * @return the name of this class
	 */

	public final String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	/**
	 * Determines whether this class type can be assigned to a given type.
	 * The latter must be a class type, and it must be a (non-necessarily
	 * strict) superclass of this class.
	 *
	 * @param other what this class should be assigned to
	 * @return true if the assignment is possible, false otherwise
	 */

	@Override
	public final boolean canBeAssignedTo(Type other) {
		return other instanceof ClassType && this.subclass((ClassType) other);
	}

	/**
	 * Checks if this class is a (non-necessarily strict) subclass of another.
	 *
	 * @param other the other class
	 * @return true if this class is a (non-necessarily strict) subclass
	 *         of {@code other}, false otherwise
	 */

	public boolean subclass(ClassType other) {
		return this == other || (superclass != null && superclass.subclass(other));
	}

	/**
	 * Computes the least common supertype of a given type and this class type.
	 * That is, a common supertype which is the least amongst all possible supertypes.
	 * <ul>
	 * <li> If {@code other} is an array type, then {@code Object} is returned;
	 * <li> Otherwise, if {@code other} is a class type then the least common
	 *      superclass of this and {@code other} is returned;
	 * <li> Otherwise, if {@code other} is a {@code NilType} or an
	 *      {@code UnusedType}, then {@code this} is returned;
	 * <li> Otherwise, {@code null} is returned.
	 * </ul>
	 *
	 * @param other the type whose least supertype with this class must be found
	 * @return the least common supertype of this class and {@code other},
	 *         if it exists, {@code null} otherwise (for instance, there
	 *         is no least common supertype between {@code int} and any class type)
	 */

	@Override
	public Type leastCommonSupertype(Type other) {
		// between a class type and an array type, the least common supertype is Object
		if (other instanceof ArrayType)
			return getObjectType();
		else if (other instanceof ClassType)
			// we look in our superclasses for a superclass of other
			for (ClassType cursor = this; ; cursor = cursor.superclass)
				if (other.canBeAssignedTo(cursor))
					return cursor;

		// the supertype of a class type and null or an unused type is the class itself,
		// otherwise, there is no common supertype
		return other == NilType.INSTANCE || other == UnusedType.INSTANCE ? this : null;
	}

	/**
	 * Yields the set of strict and non-strict, direct and indirect
	 * subclasses of this class.
	 *
	 * @return the set of strict and non-strict, direct and indirect
	 *         subclasses of this class. This list is never empty
	 *         since this class is always an instance of itself
	 */

	public final List<ClassType> getInstances() {
		// we first check to see if we already computed the set of instances of this class
		if (instances != null)
			return instances;

		// we add this class itself
		List<ClassType> result = new ArrayList<>();
		result.add(this);

		// we add the instances of our subclasses
		for (ClassType sub: subclasses)
			result.addAll(sub.getInstances());

		// we take note of the set of instances, so that we do not recompute it next time
		return instances = result;
	}

	/**
	 * Adds a field to this class. If a field with the given name
	 * already existed, it is overwritten.
	 *
	 * @param name the name of the field
	 * @param sig the signature of the field
	 */

	public void addField(String name, FieldSignature sig) {
		fields.put(name,sig);
	}

	/**
	 * Adds a constructor to this class. If a constructor with the given
	 * signature already existed, it is overwritten.
	 *
	 * @param sig the signature of the constructor
	 */

	public final void addConstructor(ConstructorSignature sig) {
		constructors.add(sig);
	}

	/**
	 * Adds a method to this class. If a method with the given name
	 * and signature already existed, it is overwritten.
	 *
	 * @param name the name of the method
	 * @param sig the signature of the method
	 */

	public final void addMethod(String name, MethodSignature sig) {
		// we read all methods, in this class, with the given name
		Set<MethodSignature> set = methods.get(name);
		if (set == null)
			methods.put(name, set = new HashSet<>());

		// we add this new method
		set.add(sig);
	}

	/**
	 * Adds a fixture to this class. If a fixture with the given
	 * signature already existed, it is overwritten.
	 *
	 * @param cSig the signature of the fixture
	 */
	
	public void addFixture(FixtureSignature cSig) {
		fixtures.add(cSig);
		
	}
	
	/**
	 * Adds a test to this class. If a test with the given
	 * signature already existed, it is overwritten.
	 *
	 */

	public final void addTest(TestSignature sig) {
		tests.add(sig);
	}
	
	/**
	 * Yields the fields of this class.
	 *
	 * @return the fields
	 */

	public Map<String, FieldSignature> getFields() {
		return fields;
	}

	/**
	 * Yields the constructors of this class.
	 *
	 * @return the constructors
	 */

	public Set<ConstructorSignature> getConstructors() {
		return constructors;
	}

	/**
	 * Yields the methods of this class.
	 *
	 * @return the methods
	 */

	public Map<String, Set<MethodSignature>> getMethods() {
		return methods;
	}

	/**
	 * Yields the tests of this class.
	 *
	 * @return the methods
	 */

	public Set<TestSignature> getTests() {
		return tests;
	}

	/**
	 * Yields the fixtures of this class.
	 *
	 * @return the methods
	 */

	public Set<FixtureSignature> getFixtures() {
		return fixtures;
	}
	
	/**
	 * Looks up from this class for the signature of the field
	 * with the given name, if any.
	 *
	 * @param name the name of the field to look up for
	 * @return the signature of the field, as defined in this class or
	 *         in one of its superclasses. Yields {@code null} if no
	 *         such field has been found
	 */

	public final FieldSignature fieldLookup(String name) {
		FieldSignature result;

		// we first look in this signature
		if ((result = fields.get(name)) != null)
			return result;

		// otherwise we look in the signature of the superclass
		return superclass == null ? null : superclass.fieldLookup(name);
	}

	/**
	 * Looks up in this class for the signatures of all constructors
	 * with parameters types compatible with those provided, if any.
	 * It is guaranteed that in the resulting set no constructor signature
	 * is more specific than another, that is, they are not comparable.
	 *
	 * @param formals the types the formal parameters of the constructors
	 *                should be more general of
	 * @return the signatures of the resulting constructors.
	 *         Returns an empty set if no constructor has been found
	 */

	public final Set<ConstructorSignature> constructorsLookup(TypeList formals) {
		// we return the most specific constructors amongst those available
		// for this class and whose formal parameters are compatible with formals
		return mostSpecific(constructors, formals);
	}

	/**
	 * Looks up in this class for the signature of the constructor
	 * with exactly the given parameters types, if any.
	 *
	 * @param formals the types of the formal parameters of the constructor
	 * @return the signature of the constructor, as defined in this class.
	 *         Yields {@code null} if no such constructor has been found
	 */

	public ConstructorSignature constructorLookup(TypeList formals) {
		// we check all constructors in this class signature
		for (ConstructorSignature constructor: constructors)
			// we check if they have the same parameters types
			if (constructor.getParameters().equals(formals))
				// found!
				return constructor;

		// otherwise, we return <tt>null</tt>
		return null;
	}

	/**
	 * Looks up from this class for the signature of the method
	 * with exactly the given name and parameters types, if any.
	 *
	 * @param name the name of the method to look up for
	 * @param formals the types of the formal parameters of the method
	 * @return the signature of the method, as defined in this class or
	 *         in one of its superclasses. Yields {@code null} if no
	 *         such method has been found
	 */

	public final MethodSignature methodLookup(String name, TypeList formals) {
		// we check all methods in this signature having the given name
		Set<MethodSignature> candidates = methods.get(name);
		if (candidates != null)
			for (MethodSignature method: candidates)
				// we check if they have the same parameters types
				if (method.getParameters().equals(formals))
					// found!
					return method;

		// otherwise, we look up in the superclass, if any
		return superclass == null ? null : superclass.methodLookup(name, formals);
	}

	/**
	 * Looks up from this class for the signatures of all methods
	 * with the given name and parameters types compatible with those
	 * provided, if any. It is guaranteed that in the resulting set no
	 * method signature is more specific than another, that is, they are
	 * not comparable.
	 *
	 * @param name the name of the method to look up for
	 * @param formals the types the formal parameters of the methods
	 *                should be more general of
	 * @return the signatures of the resulting methods.
	 *         Returns an empty set if no method has been found
	 */

	public final Set<MethodSignature> methodsLookup(String name, TypeList formals) {
		// the set of candidates is initially the set of all methods
		// called name and defined in this class
		Set<MethodSignature> candidates = methods.get(name);
		if (candidates == null) candidates = new HashSet<>();

		if (superclass != null) {
			// if this class extends another class, we consider all possible
			// candidate targets in the superclass, so that we allow method inheritance
			Set<MethodSignature> superCandidates = superclass.methodsLookup(name,formals);

			// we remove from the inherited candidates those which are
			// redefined in this class, in order to model method overriding
			Set<MethodSignature> toBeRemoved = new HashSet<>();

			TypeList sigFormals, sig2Formals;
			for (MethodSignature sig: superCandidates) {
				sigFormals = sig.getParameters();

				for (MethodSignature sig2: candidates) {
					sig2Formals = sig2.getParameters();

					if (sigFormals.equals(sig2Formals))
						toBeRemoved.add(sig);
				}
			}

			superCandidates.removeAll(toBeRemoved);

			// we add the inherited and not overridden candidates
			candidates.addAll(superCandidates);
		}

		// we return the most specific methods amongst those called name
		// and whose formal parameters are compatible with formals
		return mostSpecific(candidates, formals);
	}

	/**
	 * Looks up from this class for the signature of the test
	 * with exactly the given name, if any.
	 *
	 * @param name the name of the test to look up for
	 * @return the signature of the test, as defined in this class.
	 * 		   Yields {@code null} if no such method has been found
	 */

	public final TestSignature testLookup(String name) {
		if(tests != null) {
			for(TestSignature test : tests) {
				if(test.getName().equals(name)) {
					return test;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Looks up from this class for the signatures of all fixtures, if any.
	 * 
	 * @return the signature of the resulting signatures.
	 *         Returns an empty set if no signature has been found
	 */
	public final Set<FixtureSignature> fixtureLookup() {
		return fixtures;
	}
	
	/**
	 * Yields the subset of a set of code signatures whose parameters
	 * are compatible with those provided and such that no two signatures in
	 * the subset are one more general than the other.
	 *
	 * @param sigs the original set of code signatures
	 * @param formals the parameters which are used to select the signatures
	 * @return the subset of {@code sigs} whose parameters
	 *         are compatible with {@code formals} and such that no two
	 *         signatures in this subset are one more general than the other
	 */

	private static <T extends CodeSignature> Set<T> mostSpecific(Set<T> sigs, TypeList formals) {
		Set<T> result = new HashSet<>();
		Set<T> toBeRemoved = new HashSet<>();

		// we scan all codes of this class
		for (T sig: sigs)
			// if the parameters of <tt>sig</tt> are compatible with
			// <tt>formals</tt>, we add it to the set of candidates
			if (formals.canBeAssignedTo(sig.getParameters())) result.add(sig);

		// we remove a candidate if it is less general than another
		for (T sig: result)
			for (T sig2: result)
				if (sig != sig2 && sig.getParameters().canBeAssignedTo(sig2.getParameters()))
					toBeRemoved.add(sig2);

		result.removeAll(toBeRemoved);

		return result;
	}

	/**
	 * Translates a Kitten type into its BCEL equivalent. It generates an
	 * {@code org.apache.bcel.generic.ObjectType} for the name of this class.
	 * For {@code String}, it generates one for {@code runTime.String}.
	 *
	 * @return the BCEL type corresponding to this Kitten type
	 */

	@Override
	public final org.apache.bcel.generic.Type toBCEL() {
		// we transform "String" into "runTime.String"
		if (name.equals("String"))
			return new org.apache.bcel.generic.ObjectType(runTime.String.class.getName());
		else
			return new org.apache.bcel.generic.ObjectType(name.toString());
	}
	/**
	 * A table which binds each symbol to its corresponding {@code KittenClassType}.
	 * This lets us have a unique {@code KittenClassType} for a given name.
	 */

	private final static Map<String, ClassType> memory = new HashMap<>();

	/**
	 * Yields a class type with the given name. If a class type object named
	 * <tt>name</tt> already exists, that object is returned. Otherwise, if a
	 * Kitten class named <tt>name.kit</tt> exists and contains no error, a
	 * <tt>KittenClassType</tt> is returned. Otherwise, a fictitious
	 * <tt>KittenClassType</tt> is returned, whose code has no fields nor
	 * constructors nor methods.
	 *
	 * @param name the name of the class
	 * @return the unique class type object for the class with the given name
	 */

	public static ClassType mk(String name) {
		ClassType result;

		// we first check to see if we already built this class type
		if ((result = memory.get(name)) != null)
			return result;
		else
			return new ClassType(name);
	}

	/**
	 * Yields a class type with the given file name. If a class type object
	 * with this name already exists, that object is returned. Otherwise, if a
	 * Kitten class named <tt>name</tt> exists and contains no syntax error, a
	 * type-checked <tt>KittenClassType</tt> is returned. Otherwise, a
	 * type-checked fictitious <tt>KittenClassType</tt> is returned, whose code
	 * contains no fields, nor constructors nor methods.
	 *
	 * @param fileName the name of the file of the class, including the
	 *                 <tt>.kit</tt> termination
	 * @return the unique Kitten class type object for the (type-checked)
	 *         class with the given name
	 */

	public static ClassType mkFromFileName(String fileName) {
		if (fileName.endsWith(".kit"))
			fileName = fileName.substring(0, fileName.length() - 4);

		ClassType result = mk(fileName);

		result.typeCheck();

		return result;
	}

	/**
	 * Yields {@code ClassType}'s that have been created so far.
	 *
	 * @return the types
	 */

	public final static Collection<ClassType> getAll() {
		return memory.values();
	}

	/**
	 * Yields the error reporting utility for this class.
	 *
	 * @return the error reporting utility for this class
	 */

	public ErrorMsg getErrorMsg() {
		return errorMsg;
	}

	/**
	 * Type-checks this class type, <i>i.e.</i>, its abstract syntax.
	 */

	public void typeCheck() {
		// this check is just to avoid repeated error messages
		if (!typeChecked) {
			// we are going to type-check this class now
			typeChecked = true;

			// we type-check the abstract syntax of this class
			abstractSyntax.typeCheck(this);

			// we continue by type-checking our superclass, if any
			ClassType superclass = getSuperclass();
			if (superclass != null)
				superclass.typeCheck();
		}
	}

	/**
	 * Translates this class into intermediate Kitten code.
	 * It is assumed that this class has been already type-checked.
	 *
	 * @return the program reachable from the empty constructor or the main of
	 *         this class, translated into Kitten code
	 */

	public Program translate() {
		return abstractSyntax.translate();
	}

	public void dumpDot() throws IOException {
		try (FileWriter file = new FileWriter(name + ".dot")) {
			abstractSyntax.toDot(file);
		}
	}
}