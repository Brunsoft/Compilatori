package types;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InvokeInstruction;

import translation.Block;
import absyn.CodeDeclaration;

/**
 * The signature of a piece of code of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class CodeSignature extends ClassMemberSignature {

    /**
     * The name of this code object.
     */

    private final String name;

    /**
     * The return type of this code object.
     */

    private final Type returnType;

    /**
     * The parameters of this code object.
     */

    private final TypeList parameters;

    /**
     * The intermediate Kitten code for this constructor or method.
     * This is {@code null} if this constructor or method has not been
     * translated yet.
     */

    private Block code;

    /**
     * Builds a signature for a code object.
     *
     * @param clazz the class where this code is defined
     * @param returnType the return type of this code
     * @param parameters the types of the parameters of this code
     * @param name the name of this code
     * @param abstractSyntax the abstract syntax of the declaration of this code
     */

    protected CodeSignature(ClassType clazz, Type returnType, TypeList parameters,
    		String name, CodeDeclaration abstractSyntax) {

    	super(clazz,abstractSyntax);

    	this.parameters = parameters;
    	this.name = name;
    	this.returnType = returnType;
    }

    @Override
    public boolean equals(Object other) {
    	if (getClass() == other.getClass()) {
    		CodeSignature otherM = (CodeSignature) other;

    		return otherM.getDefiningClass() == getDefiningClass() &&
    				otherM.name == name &&
    				otherM.parameters.equals(parameters) &&
    				otherM.returnType == returnType;
    	}
    	else
    		return false;
    }

    @Override
    public int hashCode() {
    	return getDefiningClass().hashCode()
   			+ name.hashCode() + parameters.hashCode() + returnType.hashCode();
    }

    @Override
    public String toString() {
    	return getDefiningClass() + "."
   			+ getName() + "(" + getParameters() + "):" + getReturnType();
    }

    /**
     * Yields the types of the parameters.
     *
     * @return the types of the parameters
     */

    public TypeList getParameters() {
    	return parameters;
    }

    /**
     * Yields the return type of this code object.
     *
     * @return the return type of this code object
     */

    public Type getReturnType() {
    	return returnType;
    }

    /**
     * Yields the name of this code object.
     *
     * @return the name of this code object
     */

    public String getName() {
    	return name;
    }

    /**
     * Yields the abstract syntax of this constructor or method declaration.
     *
     * @return the abstract syntax of this constructor or method declaration
     */

    @Override
    public CodeDeclaration getAbstractSyntax() {
    	return (CodeDeclaration) super.getAbstractSyntax();
    }

    /**
     * Yields the types of the stack elements which must be on top of the
     * stack when one calls this method or constructor.
     *
     * @return the types of the top stack elements at the moment of the call
     *         to this method or constructor
     */

    public TypeList requiredStackTypes() {
    	return parameters.push(getDefiningClass());
    }

    /**
     * Yields the block where the Kitten bytecode of this method or
     * constructor starts.
     *
     * @return the block where the Kitten bytecode of this method or
     *         constructor starts
     */

    public Block getCode() {
    	return code;
    }

    /**
     * Sets the Kitten code of this constructor or method, adding
     * automatically the prefix expected for it.
     *
     * @param code the Kitten code
     */

    public void setCode(Block code) {
    	this.code = addPrefixToCode(code);
    }

    /**
     * Adds a prefix to the Kitten bytecode generated for this constructor or
     * method. This allows for instance constructors to add a call to the
     * constructor to the superclass.
     *
     * @param code the code already compiled for this constructor or method
     * @return {@code code} with a prefix
     */

    protected abstract Block addPrefixToCode(Block code);

    /**
     * Generates an invocation instruction that calls this method or
     * constructor.
     *
     * @param classGen the class generator to be used to generate the
     *                 invocation instruction
     * @param invocationType the type of invocation required, as enumerated
     *                       inside {@code org.apache.bcel.Constants}
     * @return an invocation instruction that calls this method or constructor
     */

    protected InvokeInstruction createInvokeInstruction(JavaClassGenerator classGen, short invocationType) {
    	// we use the instruction factory in order to put automatically inside
    	// the constant pool a reference to the Java signature of this method or constructor
    	return classGen.getFactory().createInvoke
   			(getDefiningClass().toBCEL().toString(), // name of the class
			getName().toString(), // name of the method or constructor
			getReturnType().toBCEL(), // return type
			getParameters().toBCEL(), // parameters types
			invocationType); // the type of invocation (static, special, ecc.)
    }
}