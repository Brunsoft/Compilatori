package absyn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import translation.Program;
import types.ClassMemberSignature;
import types.ClassType;
import types.MethodSignature;
import types.TypeList;

/**
 * The abstract syntax of the definition of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ClassDefinition extends Absyn {

    /**
     * The name of the class.
     */

    private final String name;

    /**
     * The name of the superclass.
     */

    private final String superclassName;

    /**
     * The sequence of fields or methods declarations. This might be {@code null}.
     */

    private final ClassMemberDeclaration declarations;

    /**
     * The class type of this class definition. This is {@code null} if
     * type-checking has not been performed yet.
     */

    private ClassType staticType;

    /**
     * Constructs the abstract syntax of the definition of a Kitten class.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     * @param name the name of the class
     * @param superclassName the name of the superclass
     * @param declarations the sequence of fields or methods declarations.
     *                     This might be {@code null}
     */

    public ClassDefinition(int pos, String name, String superclassName, ClassMemberDeclaration declarations) {
    	super(pos);

    	this.name = name;
    	this.superclassName = superclassName;
    	this.declarations = declarations;
    }

    /**
     * Yields the name of the class defined with this abstract syntax.
     *
     * @return the name of the class defined with this abstract syntax
     */

    public String getName() {
    	return name;
    }

    /**
     * Yields the name of the superclass of the class
     * defined with this abstract syntax.
     *
     * @return the name of the superclass of the class
     *         defined with this abstract syntax
     */

    public String getSuperclassName() {
    	return superclassName;
    }

    /**
     * Yields the abstract syntax of the sequence of fields and methods
     * declarations in this class definition, if any.
     *
     * @return the abstract syntax of the sequence of fields and methods
     *         declarations in this class definition, if any. Returns
     *         {@code null} if there is no declaration inside this class definition
     */

    public ClassMemberDeclaration getDeclarations() {
    	return declarations;
    }

    /**
     * Yields the static type of this class definition.
     *
     * @return the static type of this class definition. It yields
     *         {@code null} if type-checking has not been performed yet
     */

    public ClassType getStaticType() {
    	return staticType;
    }

    /**
     * Writes in the specified file a dot representation of the abstract
     * syntax of this class. It writes the prefix specifying the paper
     * size and orientation, then puts a node for this class definition,
     * with three children corresponding to {@link #name},
     * {@link #superclassName} and {@link #declarations}, if any. Then
     * it builds the subtrees rooted at those children.
     *
     * @param where the file where the dot representation must be written
     * @throws IOException if there is an error while writing into {@code where}
     */

    public final void toDot(FileWriter where) throws IOException {
    	where.write("digraph " + name + " {\n");

    	// the size of a standard A4 sheet (in inches)
    	where.write("size = \"11,7.5\";\n");

    	// landscape mode
    	where.write("rotate = 90\n");

    	// dumps in the file the name of the node in the dot file,
    	// followed by the label used to show the node to the user of dot
    	where.write(dotNodeName() + " [ label = \"" + label() + "\"];\n");

    	linkToNode("name", toDot(name, where), where);
    	if (superclassName != null)
    		linkToNode("superclassName", toDot(superclassName, where), where);
    	if (declarations != null)
    		linkToNode("declarations", declarations.toDot(where), where);

    	where.write("}");
    }

    /**
     * Writes in a file named as this class (plus the trailing {@code .dot})
     * a dot representation of the abstract syntax of this class.
     *
     * @param directory the directory where the file must be dumped
     * @throws IOException if the dot file cannot be created
     */

    public final void dumpDot(String directory) throws IOException {
    	try (FileWriter dot = new FileWriter(directory + File.separatorChar + name + ".dot")) {
    		toDot(dot);
    		dot.flush();
    	}
    }

    /**
     * Adds, to the given class, the signatures of the fields,
     * constructors and methods of this class definition.
     *
     * @param clazz the class where the signatures of fields,
     *              constructors and methods must be added
     */

    public void addMembersTo(ClassType clazz) {
    	if (declarations != null)
    		declarations.addMembersTo(clazz);
    }

    /**
     * Type-checks this class definition. Namely, it type-checks its members
     * and checks that the empty constructor exists.
     *
     * @param currentClass the semantical type of this class. This will be bound to the implicit
     *                     {@code this} parameter of all its constructors and methods
     */

    public void typeCheck(ClassType currentClass) {
    	staticType = currentClass;

    	if (declarations != null)
    		declarations.typeCheck(currentClass);
    }

    /**
     * Translates this class definition into intermediate Kitten code.
     * Only the methods reachable from {@code main} are compiled, if any.
     *
     * @return the program reachable from the {@code main} method of the class compiled by Kitten
     */

    public Program translate() {
    	Set<ClassMemberSignature> done = new HashSet<>();

    	// we look up for the main method, if any
    	MethodSignature main = staticType.methodLookup("main", TypeList.EMPTY);

    	// we translate everything that is reachable from the main method of this class (if any)
    	if (main != null)
    		main.getAbstractSyntax().translate(done);

    	return new Program(done, main);
    }
}