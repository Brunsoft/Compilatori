package types;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.MethodGen;

import absyn.MethodDeclaration;
import translation.Block;

/**
 * The signature of a method of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class MethodSignature extends CodeSignature {

	/**
	 * Constructs the signature of a method with the given name, return type
	 * and parameters types.
	 *
	 * @param clazz the class where this method is defined
	 * @param returnType the return type of this method
	 * @param parameters the types of the parameters of this method
	 * @param name the name of the method
	 * @param abstractSyntax the abstract syntax of the declaration of this method
	 */

	public MethodSignature(ClassType clazz, Type returnType,
		TypeList parameters, String name, MethodDeclaration abstractSyntax) {

		super(clazz, returnType, parameters, name, abstractSyntax);
	}

	/**
	 * Generates an {@code invokevirtual} Java bytecode that calls this
	 * method. The Java {@code invokevirtual} bytecode calls a method by using
	 * the run-time class of the receiver to look up for the method's implementation.
	 *
	 * @param classGen the class generator to be used to generate
	 *                 the {@code invokevirtual} Java bytecode
	 * @return an {@code invokevirtual} Java bytecode that calls this method
	 */

	public INVOKEVIRTUAL createINVOKEVIRTUAL(JavaClassGenerator classGen) {
		return (INVOKEVIRTUAL) createInvokeInstruction(classGen, Constants.INVOKEVIRTUAL);
	}

	/**
	 * Adds the the given class generator a Java bytecode method for this method.
	 *
	 * @param classGen the generator of the class where the method lives
	 */

	public void createMethod(JavaClassGenerator classGen) {
		MethodGen methodGen;
		if (getName().equals("main"))
			methodGen = new MethodGen
				(Constants.ACC_PUBLIC | Constants.ACC_STATIC, // public and static
				org.apache.bcel.generic.Type.VOID, // return type
				new org.apache.bcel.generic.Type[] // parameters
					{ new org.apache.bcel.generic.ArrayType("java.lang.String", 1) },
				null, // parameters names: we do not care
				"main", // method's name
				classGen.getClassName(), // defining class
				classGen.generateJavaBytecode(getCode()), // bytecode of the method
				classGen.getConstantPool()); // constant pool
		else
			methodGen = new MethodGen
				(Constants.ACC_PUBLIC, // public
				getReturnType().toBCEL(), // return type
				getParameters().toBCEL(), // parameters types, if any
				null, // parameters names: we do not care
				getName(), // method's name
				classGen.getClassName(), // defining class
				classGen.generateJavaBytecode(getCode()), // bytecode of the method
				classGen.getConstantPool()); // constant pool

		// we must always call these methods before the getMethod()
		// method below. They set the number of local variables and stack
		// elements used by the code of the method
		methodGen.setMaxStack();
		methodGen.setMaxLocals();

		// we add a method to the class that we are generating
		classGen.addMethod(methodGen.getMethod());
	}

	/**
	 * Adds a prefix to the Kitten bytecode generated for this method.
	 *
	 * @param code the code already compiled for this method
	 * @return {@code code} itself
	 */

	@Override
	protected Block addPrefixToCode(Block code) {
		return code;
	}
}