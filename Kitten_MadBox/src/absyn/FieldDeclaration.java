package absyn;

import java.io.FileWriter;

import types.FieldSignature;
import types.ClassType;

/**
 * A node of abstract syntax representing the declaration of a field of a Kitten class.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FieldDeclaration extends ClassMemberDeclaration {

	/**
	 * The abstract syntax of the type of the field.
	 */

	private final TypeExpression type;

	/**
	 * The name of the field.
	 */

	private final String name;

	/**
	 * The signature of this method. This is {@code null} if type-checking
	 * has not been performed yet.
	 */

	private FieldSignature sig;

	/**
	 * Constructs the abstract syntax of a field declaration.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 * @param type the abstract syntax of the type of the field
	 * @param name the name of the field
	 * @param next the abstract syntax of the declaration of the
	 *             subsequent class member, if any
	 */

	public FieldDeclaration(int pos, TypeExpression type, String name, ClassMemberDeclaration next) {
		super(pos,next);

		this.type = type;
		this.name = name;
	}

	/**
	 * Yields the abstract syntax of the type of the field.
	 *
	 * @return the abstract syntax of the type of the field
	 */

	public TypeExpression getType() {
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

	/**
	 * Adds arcs between the dot node for this piece of abstract syntax
	 * and those representing {@link #type} and {@link #name}.
	 *
	 * @param where the file where the dot representation must be written
	 */

	protected void toDotAux(FileWriter where) throws java.io.IOException {
		linkToNode("type", type.toDot(where), where);
		linkToNode("name", toDot(name, where), where);
	}

	/**
	 * Adds the signature of this field declaration to the given class.
	 *
	 * @param clazz the class where the signature of this field
	 *              declaration must be added
	 */

	@Override
	protected void addTo(ClassType clazz) {
		this.sig = new FieldSignature(clazz, type.toType(), name, this);
		clazz.addField(name, this.sig);
	}

	/**
	 * Type-checks this field declaration. This amounts to type-check its declared type.
	 *
	 * @param currentClass the semantical type of the class where this member occurs.
	 */

	@Override
	protected void typeCheckAux(ClassType currentClass) {
		type.typeCheck();
	}

	/**
	 * Yields the signature of this field declaration.
	 *
	 * @return the signature of this field declaration. Yields {@code null}
	 *         if type-checking has not been performed yet
	 */

	@Override
	public FieldSignature getSignature() {
		return sig;
	}
}