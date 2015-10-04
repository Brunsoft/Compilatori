package types;

import java.util.HashMap;
import java.util.Map;

/**
 * A (mono-dimensional) array type of the Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ArrayType extends ReferenceType {

	/**
	 * A map from elements type to the unique array type for that elements type.
	 * It is used in order to avoid duplication of array types for
	 * the same elements type. In this way, comparison of array types
	 * can be performed through simple == tests.
	 */

	private final static Map<Type, ArrayType> memory = new HashMap<>();

	/**
	 * The type of the elements of the array.
	 */

	private final Type elementsType;

	/**
	 * Builds an array type for the given type of elements.
	 *
	 * @param elementsType the type of the elements of the array
	 */

	private ArrayType(Type elementsType) {
		this.elementsType = elementsType;
	}

	/**
	 * Returns the unique {@code ArrayType} object with the given elements type.
	 *
	 * @param elementsType the type of the elements of the array
	 * @return the unique type
	 */

	public static ArrayType mk(Type elementsType) {
		ArrayType result = memory.get(elementsType);
		if (result == null)
			memory.put(elementsType, result = new ArrayType(elementsType));

		return result;
	}

	/**
	 * Returns the unique {@code ArrayType} object for the given elements
	 * type and dimensions.
	 *
	 * @param elementsType the type of the elements of the array
	 * @param dimensions the number of dimensions
	 * @return the unique {@code ArrayType} with elements of type
	 *         {@code elementsType} and dimensions {@code dimensions}
	 */

	public static ArrayType mk(Type elementsType, int dimensions) {
		if (dimensions == 1)
			return mk(elementsType);
		else
			return ArrayType.mk(ArrayType.mk(elementsType,dimensions - 1));
	}

	/**
	 * Returns the type of the elements of this array.
	 *
	 * @return the type of the elements of this array
	 */

	public Type getElementsType() {
		return elementsType;
	}

	@Override
	public String toString() {
		return elementsType + "[]";
	}

	/**
	 * Determines whether this array type can be assigned to a given type.
	 * An array type can be assigned to another array type provided their
	 * elements types are the same, or rather those of {@code other}
	 * are supertypes of those of {@code this}. Note that
	 * if the elements are primitive types, they must be <i>the same type</i>.
	 * We also allow every array to be assigned to {@code Object}.
	 *
	 * @param other what this type should be assigned to
	 * @return true if the assignment is possible, false otherwise
	 */

	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof ArrayType)
			return elementsType.canBeAssignedToSpecial(((ArrayType) other).elementsType);
		else
			return other == getObjectType();
	}

	/**
	 * Computes the least common supertype of a given type and this array type.
	 * That is, a common supertype which is the least amongst all possible supertypes.
	 * <ul>
	 * <li> If {@code other} is a class type, then class type {@code Object} is returned;
	 * <li> Otherwise, if [@code Object} is an array of primitive types and
	 *      {@code this} is not the same array type, then {@code Object} is returned;
	 * <li> Otherwise, if {@code other} is an array type and the least
	 *      common supertype <i>lcs</i> between its elements and the elements
	 *      of {@code this} exists, an array type of <i>lcs</i> is returned;
	 * <li> Otherwise, if {@code other} is an array type, then {@code Object} is returned;
	 * <li> Otherwise, if {@code other} is a {@code NilType} or an {@code UnusedType},
	 *      then {@code this} is returned;
	 * <li> Otherwise, {@code null} is returned.
	 * </ul>
	 *
	 * @param other the type whose least supertype with this array type must be found
	 * @return the least common supertype of this array type and {@code other},
	 *         if it exists, or {@code null} otherwise (for instance, there
	 *         is no least common supertype between {@code int} and
	 *         an array of {@code boolean})
	 */

	@Override
	public Type leastCommonSupertype(Type other) {
		// between array and class, the least common supertype is Object
		if (other instanceof ClassType)
			return getObjectType();
		else if (other instanceof ArrayType)
			// an array of primitive types can only be compared with itself.
			// Otherwise, the least common supertype is Object
			if (elementsType instanceof PrimitiveType)
				return this == other ? this : getObjectType();
			else {
				Type lcs = elementsType.leastCommonSupertype(((ArrayType) other).elementsType);

				return lcs == null ? getObjectType() : mk(lcs);
			}

		// the least common supertype of an array and null or an UnusedType is the array
		if (other == NilType.INSTANCE || other == UnusedType.INSTANCE)
			return this;
		else
			// no common supertype exists
			return null;
	}

	@Override
	public org.apache.bcel.generic.Type toBCEL() {
		return new org.apache.bcel.generic.ArrayType(elementsType.toBCEL(), 1);
	}
}