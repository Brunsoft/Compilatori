package types;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

import translation.Block;
import absyn.ConstructorDeclaration;
import bytecode.CONSTRUCTORCALL;
import bytecode.LOAD;

/**
 * The signature of a constructor of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ConstructorSignature extends CodeSignature {

	/**
	 * Constructs a signature for a constructor, given its parameters types
	 * and the class it belongs to.
	 *
	 * @param clazz the class this constructor belongs to
	 * @param parameters the types of the parameters of the constructor
	 * @param abstractSyntax the abstract syntax of the declaration of this constructor
	 */

	public ConstructorSignature(ClassType clazz, TypeList parameters, ConstructorDeclaration abstractSyntax) {
		// a constructor always returns void and its name is by default init
		super(clazz, VoidType.INSTANCE, parameters, "<init>", abstractSyntax);
	}

	@Override
	public String toString() {
		return getDefiningClass() + "(" + getParameters() + ")";
	}

	/**
	 * Generates an {@code invokespecial} Java bytecode that calls this
	 * constructor. The Java {@code invokespecial} bytecode calls a method by
	 * using a hard-wired class name to look up for the method's implementation
	 * and has a receiver.
	 *
	 * @param classGen the class generator to be used to generate
	 *                 the {@code invokespecial} Java bytecode
	 * @return an {@code invokespecial} Java bytecode that calls this constructor
	 */

	public INVOKESPECIAL createINVOKESPECIAL(JavaClassGenerator classGen) {
		return (INVOKESPECIAL) createInvokeInstruction(classGen, Constants.INVOKESPECIAL);
	}

	/**
	 * Adds the the given class generator a Java bytecode constructor for
	 * this constructor.
	 *
	 * @param classGen the generator of the class where the constructor lives
	 */

	public void createConstructor(JavaClassGenerator classGen) {
		InstructionList il = classGen.generateJavaBytecode(getCode());

		// we add the following code at the beginning of the empty constructor
		// for the Kitten Object class:
		//
		// aload 0   [ load "this" ]
		// invokespecial java.lang.Object.<init>()
		//
		// In this way we respect the constraint of the Java bytecode
		// that each constructor must call a constructor of the superclass
		if (getDefiningClass().getName().equals("Object")) {
			il.insert(classGen.getFactory().createInvoke
				("java.lang.Object", // the name of the class
				Constants.CONSTRUCTOR_NAME, // <init>
				org.apache.bcel.generic.Type.VOID, // return type
				org.apache.bcel.generic.Type.NO_ARGS, // parameters
				Constants.INVOKESPECIAL)); // the type of call
			il.insert(InstructionFactory.ALOAD_0);
		}

		// we create a method generator: constructors are just methods
		// in Java bytecode, with special name <init>
		MethodGen methodGen = new MethodGen
			(Constants.ACC_PUBLIC, // public
			org.apache.bcel.generic.Type.VOID, // return type
			getParameters().toBCEL(), // parameters types, if any
			null, // parameters names: we do not care
			Constants.CONSTRUCTOR_NAME, // <init>
			classGen.getClassName(), // name of the class
			il, // bytecode of the constructor
			classGen.getConstantPool()); // constant pool

		// we must always call these methods before the getMethod()
		// method below. They set the number of local variables and stack
		// elements used by the code of the method
		methodGen.setMaxStack();
		methodGen.setMaxLocals();

		// we add a method (actually, constructor) to the class that we are generating
		classGen.addMethod(methodGen.getMethod());
	}

	/**
	 * Adds a prefix to the Kitten bytecode generated for this constructor.
	 * That is a call to the empty constructor of the superclass (if any)
	 *
	 * @param code the code already compiled for this constructor
	 * @return {@code code} prefixed with a call to the empty constructor of the superclass
	 */

	@Override
	protected Block addPrefixToCode(Block code) {
		// we prefix a piece of code that calls the constructor of
		// the superclass (if any)
		if (!getDefiningClass().getName().equals("Object")) {
			ClassType superclass = getDefiningClass().getSuperclass();

			code = new LOAD(0, getDefiningClass()).followedBy
				(new CONSTRUCTORCALL(superclass.constructorLookup(TypeList.EMPTY))
				.followedBy(code));
		}

		return code;
	}
}