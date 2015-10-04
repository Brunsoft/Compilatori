package types;

/**
 * The {@code void} type of the Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class VoidType extends Type {

	/**
	 * The {@code void} type. Always use this constant to refer to the void type,
	 * so that type comparison can be carried out by simple == tests.
	 */

	public final static VoidType INSTANCE = new VoidType();

	/**
	 * Builds a {@code void} type. This constructor is private, so that the only
	 * {@code void} type object is {@link #INSTANCE}.
	 */

	private VoidType() {}

	@Override
	public String toString() {
		return "void";
	}

	/**
	 * The number of stack elements used on the Kitten abstract machine
	 * to hold a value of this type.
	 *
	 * @return 0
	 */

	@Override
	public int getSize() {
		return 0;
	}

	/**
	 * Determines whether this void type can be assigned to a given type.
	 * This is always false since type {@code void} cannot be assigned
	 * to any type, not even to itself.
	 *
	 * @param other what this type should be assigned to
	 * @return false
	 */

	@Override
	public boolean canBeAssignedTo(Type other) {
		return false;
	}

	/**
	 * Determines whether this void type can be assigned to a given type, by
	 * assuming that primitive types can only be assigned to the same,
	 * identical, primitive type. Moreover, it assumes that {@code void}
	 * can be assigned to {@code void}.
	 *
	 * @param other what this type should be assigned to
	 * @return true if {@code other} is {@code void}, false otherwise
	 */

	@Override
	public boolean canBeAssignedToSpecial(Type other) {
		return this == other;
	}

	@Override
	public org.apache.bcel.generic.Type toBCEL() {
		return org.apache.bcel.generic.Type.VOID;
	}
}