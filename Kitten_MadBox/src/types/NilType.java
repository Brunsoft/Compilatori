package types;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

/**
 * The type of the {@code nil} constant of the Kitten language.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NilType extends PrimitiveType {

	/**
	 * The {@code nil} type. Always use this constant to refer to the nil type,
	 * so that type comparison can be carried out by simple == tests.
	 */

	public final static NilType INSTANCE = new NilType();

	/**
     * Builds a {@code nil} type. This constructor is private, so that the only
     * {@code nil} type object is {@link #INSTANCE}.
     */

    private NilType() {}

    @Override
    public String toString() {
    	return "nil";
    }

    /**
     * Determines whether this {@code nil} type can be assigned to a given type.
     * To this purpose, the latter must be a {@code nil} type or a reference type.
     *
     * @param other what this {@code nil} type should be assigned to
     * @return true if {@code other} is a {@code nil} or reference type, false otherwise
     */

    @Override
    public boolean canBeAssignedTo(Type other) {
    	return other == this || other instanceof ReferenceType;
    }

    /**
     * Computes the least common supertype of a given type and this type.
     * Namely, it yields {@code other} if the latter is a {code nil} type or
     * a reference type. It yields {@code null} otherwise.
     *
     * @param other the type whose least supertype with this type must be found
     * @return the least common supertype of this type and {@code other},
     *         if it exists, {@code null} otherwise
     */

    @Override
    public Type leastCommonSupertype(Type other) {
    	if (other == this || other instanceof ReferenceType)
    		return other;
    	else
    		return null;
    }

    @Override
    public org.apache.bcel.generic.Type toBCEL() {
    	return org.apache.bcel.generic.Type.NULL;
    }

    /**
     * Adds to {@code il} the Java bytecodes that go to {@code yes}
     * if the the top two elements of the stack are equal.
     * In this case, it adds an {@code if_acmpeq} Java bytecode.
     *
     * @param il the list of instructions that must be expanded
     * @param yes to place where to jump
     */

    @Override
    public void JB_if_cmpeq(InstructionList il, InstructionHandle yes) {
    	il.append(new org.apache.bcel.generic.IF_ACMPEQ(yes));
    }

    /**
     * Adds to {@code il} the Java bytecodes that go to {@code yes}
     * if the the top two elements of the stack are not equal.
     * In this case, it adds an {@code if_acmpne} Java bytecode.
     *
     * @param il the list of instructions that must be expanded
     * @param yes to place where to jump
     */

    @Override
    public void JB_if_cmpne(InstructionList il, InstructionHandle yes) {
    	il.append(new org.apache.bcel.generic.IF_ACMPNE(yes));
    }
}