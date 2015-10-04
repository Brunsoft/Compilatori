package types;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.FieldInstruction;

import absyn.FieldDeclaration;

/**
 * The signature of a field of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FieldSignature extends ClassMemberSignature {

    /**
     * The type of the field.
     */

    private final Type type;

    /**
     * The name of the field.
     */

    private final String name;

    /**
     * Constructs the signature of a field with the given type and name,
     * declared in the given class.
     *
     * @param clazz the class where this field is defined
     * @param type the type of the field
     * @param name the name of the field
     * @param abstractSyntax the abstract syntax of this field declaration
     */

    public FieldSignature(ClassType clazz, Type type, String name, FieldDeclaration abstractSyntax) {
    	super(clazz, abstractSyntax);

    	this.type = type;
    	this.name = name;
    }

    @Override
    public boolean equals(Object other) {
    	if (other instanceof FieldSignature) {
    		FieldSignature otherF = (FieldSignature) other;

    		return otherF.getDefiningClass() == getDefiningClass() &&
    			otherF.name == name && otherF.type == type;
    	}
    	else
    		return false;
    }

    @Override
    public int hashCode() {
    	return getDefiningClass().hashCode() + name.hashCode() + type.hashCode();
    }

    /**
     * Yields the type of the field.
     *
     * @return the type of the field
     */

    public Type getType() {
    	return type;
    }

    /**
     * Yields the name of the field.
     *
     * @return the name of the field
     */

    public String getName() {
    	return name;
    }

    @Override
    public String toString() {
    	return getDefiningClass() + "." + name + ":" + type;
    }

    /**
     * Generates a {@code getfield} Java bytecode that reads the value of this field.
     *
     * @param classGen the class generator to be used to generate the {@code getfield}
     * @return a {@code getfield} Java bytecode that reads the value of this field
     */

    public FieldInstruction createGETFIELD(JavaClassGenerator classGen) {
    	return classGen.getFactory().createGetField
    		(getDefiningClass().toBCEL().toString(), name, type.toBCEL());
    }

    /**
     * Generates a {@code putfield} Java bytecode that writes a
     * value inside this field.
     *
     * @param classGen the class generator to be used to generate
     *                 the {@code putfield} Java bytecode
     * @return a {@code putfield} Java bytecode that writes a value inside this field
     */

    public FieldInstruction createPUTFIELD(JavaClassGenerator classGen) {
    	return classGen.getFactory().createPutField
   			(getDefiningClass().toBCEL().toString(), name, type.toBCEL());
    }

    /**
     * Creates a Java bytecode field and adds it the the given class with the
     * given constant pool.
     *
     * @param classGen the generator of the class where the field lives
     */

    public void createField(JavaClassGenerator classGen) {
    	classGen.addField(new FieldGen
   			(Constants.ACC_PUBLIC, // the field is public
			getType().toBCEL(), // type
			name,
			classGen.getConstantPool()) // constant pool where it must be stored
    		.getField());
    }
}