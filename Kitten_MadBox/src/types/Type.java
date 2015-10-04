package types;

/**
 * A type of the Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Type {

	/**
	 * The top of the hierarchy of the reference types.
	 */

	private static ClassType objectType;

	/**
	 * Builds a type object.
	 */

	protected Type() {}

	/**
	 * Yields the top type of the hierarchy of the reference types.
	 *
	 * @return the top type
	 */

	public static final ClassType getObjectType() {
		return objectType;
	}

	/**
	 * Sets the top type of the hierarchy of the reference types.
	 */

	protected static final void setObjectType(ClassType objectType) {
		Type.objectType = objectType;
	}

	/**
	 * The number of stack elements used on the Kitten abstract machine
	 * to hold a value of this type. This is always 1 for Kitten types that
	 * are not {@code void}.
	 *
	 * @return the number of stack elements
	 */

	public int getSize() {
		return 1;
	}

	@Override
	public abstract String toString();

	/**
	 * Determines whether this type can be assigned to a given type.
	 * Type {@code void} cannot be assigned to any type, not even to itself.
	 *
	 * @param other what this type should be assigned to
	 * @return true if the assignment is possible, false otherwise
	 */

	public abstract boolean canBeAssignedTo(Type other);

	/**
	 * Determines whether this is a (non-strict) subtype of a given type.
	 * It is exactly the same as {@link #canBeAssignedTo(Type)}.
	 *
	 * @param other what this type should be a (non-strict) subtype of
	 * @return true if this type is a (non-strict) subtype of {@code other},
	 *         false otherwise
	 */

	public final boolean subtypeOf(Type other) {
		return canBeAssignedTo(other);
	}

	/**
	 * Determines whether this type can be assigned to a given type, by
	 * assuming that primitive types can only be assigned to the same,
	 * identical, primitive type. Moreover, it assumes that {@code void}
	 * is a subtype of itself.
	 *
	 * @param other what this type should be assigned to
	 * @return true if the assignment is possible, false otherwise
	 */

	public boolean canBeAssignedToSpecial(Type other) {
		// primitive types and void should redefine this
		return canBeAssignedTo(other);
	}

	/**
	 * Computes the least common supertype of a given type and this type.
	 * That is, a common supertype which is the least amongst all possible supertypes.
	 *
	 * @param other the type whose least supertype with this type must be found
	 * @return the least common supertype of this type and {@code other},
	 *         if it exists, {@code null} otherwise (for instance, there
	 *         is no least common supertype between {@code boolean} and {@code int})
	 */

	public Type leastCommonSupertype(Type other) {
		// the  least upper bound with an unused type is this
		if (other == UnusedType.INSTANCE)
			return this;
		// this works fine for primitive types. Class and array types
		// will have to redefine this behaviour
		else if (this.canBeAssignedTo(other))
			return other;
		else if (other.canBeAssignedTo(this))
			return this;
		else
			// there is no least common supertype
			return null;
	}

	/**
	 * Translates a Kitten type into its BCEL equivalent.
	 *
	 * @return the BCEL type corresponding to this Kitten type
	 */

	public abstract org.apache.bcel.generic.Type toBCEL();
}