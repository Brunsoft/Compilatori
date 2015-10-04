package types;

import absyn.ClassMemberDeclaration;

/**
 * The signature of a member of a class (such as fields, constructors,
 * methods, ...).
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ClassMemberSignature {

    /**
     * The class this member belongs to.
     */

    private final ClassType clazz;

    /**
     * The abstract syntax of the class member having this signature.
     */

    private final ClassMemberDeclaration abstractSyntax; 

    /**
     * Constructs a signature for a member of the given class.
     *
     * @param clazz the class this member belongs to
     * @param abstractSyntax the abstract syntax of this class member
     */

    protected ClassMemberSignature(ClassType clazz, ClassMemberDeclaration abstractSyntax) {
    	this.clazz = clazz;
    	this.abstractSyntax = abstractSyntax;
    }

    /**
     * Yields the class where this member is defined.
     *
     * @return the class where this member is defined
     */

    public ClassType getDefiningClass() {
    	return clazz;
    }

    /**
     * Yields the abstract syntax of this class member declaration.
     *
     * @return the abstract syntax of this class member declaration
     */

    public ClassMemberDeclaration getAbstractSyntax() {
    	return abstractSyntax;
    }
}