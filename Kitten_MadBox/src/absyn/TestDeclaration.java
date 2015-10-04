package absyn;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;


import bytecode.NEWSTRING;
import bytecode.RETURN;
import semantical.TypeChecker;
import translation.Block;
import types.ClassMemberSignature;
import types.ClassType;
import types.TestSignature;
import types.VoidType;

/**
 * A node of abstract syntax representing the declaration of a test
 * of a Kitten class.
 *
 * @author <A HREF="mailto:info@madBox.it">MadBox</A>
 */

public class TestDeclaration extends CodeDeclaration {

	/**
	 * The name of the test.
	 */

	private String name;

	/**
	 * Constructs the abstract syntax of a test declaration.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 * @param name the name of the test
	 * @param body the abstract syntax of the body of the test
	 * @param next the abstract syntax of the declaration of the
	 *             subsequent class member, if any
	 */

	public TestDeclaration(int pos, String name, Command body, ClassMemberDeclaration next) {
		super(pos, null, body, next);
		this.name = name;
	}
	
	/**
	 * Yields the name of this test.
	 *
	 * @return the name of this test
	 */
    public String getName() {
    	return name;
    }
    
	/**
	 * Yields the signature of this test declaration.
	 *
	 * @return the signature of this test declaration. Yields {@code null}
	 *         if type-checking has not been performed yet
	 */

	@Override
	public TestSignature getSignature() {
		return (TestSignature) super.getSignature();
	}
    
	/**
	 * Adds arcs between the dot node for this piece of abstract syntax
	 * and those representing the formal parameters and body of the fixture.
	 *
	 * @param where the file where the dot representation must be written
	 */

	protected void toDotAux(FileWriter where) throws java.io.IOException {
		//linkToNode("name", toDot(name, where), where);
		linkToNode("body", getBody().toDot(where), where);
	}
	
	/**
	 * Adds the signature of this test declaration to the given class.
	 *
	 * @param clazz the class where the signature of this fixture
	 *              declaration must be added
	 */

	@Override 
	protected void addTo(ClassType clazz) {
		TestSignature cSig = new TestSignature (clazz, name, this);

		clazz.addTest(cSig);

		// we record the signature of this constructor inside this abstract syntax

		setSignature(cSig);
	}

	/**
	 * Type-checks this test declaration. Namely, it builds a type-checker
	 * whose only variable in scope is {@code this} of the defining class of the
	 * fixture, and where only return instructions of type {@code void} are allowed.
	 * It then type-checks the body of the fixture in that type-checker
	 * and checks that it does not contain any dead-code.
	 *
	 * @param clazz the semantical type of the class where this constructor occurs.
	 */

	@Override
	protected void typeCheckAux(ClassType clazz) {
		
		TypeChecker checker = new TypeChecker(VoidType.INSTANCE, clazz.getErrorMsg(), true);
		checker = checker.putVar("this", clazz);

		// we type-check the body of the test in the resulting type-checker
		getBody().typeCheck(checker);

		// we check that there is no dead-code in the body of the test
		getBody().checkForDeadcode();

		// tests return nothing, so that we do not check whether
		// a return statement is always present at the end of every
		// syntactical execution path in the body of a test
	}
	
    /**
     * Translates this constructor or method into intermediate Kitten code.
     * This amounts to translating its body with a continuation containing
     * a {@code return} bytecode. This way, if a method does not have an
     * explicit {@code return} statement, it is automatically put at its end.
     *
     * @param done the set of code signatures that have been already translated
     */

    public void translate(Set<ClassMemberSignature> done) {
    	if (done.add(getSignature())) {
    		
    		translateAll(getSignature().getDefiningClass(), done);
    		// we translate the body of the constructor or
    		// method with a block containing RETURN as continuation. This way,
    		// all methods returning void and
    		// with some missing return command are correctly
    		// terminated anyway. If the method is not void, this
    		// precaution is useless since we know that every execution path
    		// ends with a return command, as guaranteed by
    		// checkForDeadCode() (see typeCheck() in MethodDeclaration.java)
	
    		Block post = new Block(new RETURN(ClassType.mk("String")));
    		post = new NEWSTRING("passed").followedBy(post);	

    		getSignature().setCode(getBody().translate(post));

    		translateReferenced(getSignature().getCode(), done, new HashSet<Block>()); 
    	}
    }
}