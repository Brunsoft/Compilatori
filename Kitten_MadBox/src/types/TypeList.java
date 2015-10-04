package types;

/**
 * A list of stack Kitten types. It is assumed that each type
 * uses as many elements as its size.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class TypeList {

	/**
	 * The empty list of types. This is used to terminate all lists of types,
	 * so that no list of types is <tt>null</tt>.
	 */

	public final static TypeList EMPTY = new TypeList(UnusedType.INSTANCE, null);

	/**
	 * The head of the list.
	 */

	private final Type head;

	/**
	 * The tail of the list.
	 */

	private final TypeList tail;

	/**
	 * Builds a list of types.
	 *
	 * @param head the first type in the list
	 * @param tail the tail of the list
	 */

	private TypeList(Type head, TypeList tail) {
		this.head = head;
		this.tail = tail;
	}

	/**
	 * Yields the first element of this list of types.
	 *
	 * @return the first element
	 */

	public Type getHead() {
		return head;
	}

	/**
	 * Yields the tail of this list of types.
	 *
	 * @return the tail
	 */

	public TypeList getTail() {
		return tail;
	}

	/**
	 * Computes a comma-separated string of the types in this list.
	 *
	 * @return the comma-separated string of the types in this list
	 */

	@Override
	public String toString() {
		TypeList cursor;
		String s = "";

		for (cursor = this; cursor != EMPTY; cursor = cursor.tail) {
			s += cursor.head.toString();
			if (cursor.tail != EMPTY)
				s += ",";
		}

		return s;
	}

	/**
	 * Determines if this list of type is equal to another.
	 *
	 * @param other the other list of types
	 * @return true if and only if this list of type is the same as {@code other}
	 */

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TypeList))
			return false;

		TypeList otherTL = (TypeList) other, cursor = this;
		while (cursor != EMPTY && otherTL != EMPTY)
			if (cursor.head != otherTL.head)
				return false;
			else {
				cursor = cursor.tail;
				otherTL = otherTL.tail;
			}

		// trailing unused elements do not affect the equality
		while (cursor != null && cursor.head == UnusedType.INSTANCE)
			cursor = cursor.tail;
		while (otherTL != null && otherTL.head == UnusedType.INSTANCE)
			otherTL = otherTL.tail;

		return cursor == otherTL;
	}

	@Override
	public int hashCode() {
		TypeList cursor = this;
		Type t;
		int code = 0;

		for (int i = 0; cursor != EMPTY; cursor = cursor.tail, i++)
			if ((t = cursor.head) != UnusedType.INSTANCE)
				code += (t.hashCode() << i);

		return code;
	}

	/**
	 * The number of stack elements used on the Kitten abstract machine
	 * to hold a list of values of the types contained in this list.
	 *
	 * @return the number of stack elements
	 */

	public int getSize() {
		int s = 0;
		for (TypeList cursor = this; cursor != EMPTY; cursor = cursor.tail)
			s++;

		return s;
	}

	/**
	 * Checks if this list of types can be element-wise assigned to another.
	 * This entails that they have the same length.
	 *
	 * @param others the other list of types
	 * @return true if and only if this list of types has the same
	 *         length as {@code other} and can be element-wise
	 *         assigned to it. Returns false otherwise
	 */

	public boolean canBeAssignedTo(TypeList others) {
		TypeList cursor = this;
		while (cursor != EMPTY && others != EMPTY)
			if (!cursor.head.canBeAssignedTo(others.head))
				return false;
			else {
				cursor = cursor.tail;
				others = others.tail;
			}

		return cursor == others;
	}

	/**
	 * Yields a list of types equal to this but first [@code count} elements
	 * have been removed.
	 *
	 * @param count the number of elements which must be removed from this list
	 * @return the resulting list of types
	 */

	private TypeList pop(int count) {
		TypeList result = this;

		while (count-- > 0) result = result.tail;

		return result;
	}

	/**
	 * Yields a list of types equal to this but whose head has been removed.
	 *
	 * @return the resulting list of types
	 */

	public TypeList pop() {
		return pop(1);
	}

	/**
	 * Yields a list of types equal to this without as many elements at its
	 * beginning as the size of {@code type}.
	 *
	 * @param type the type which must be popped from this list of types
	 * @return the resulting list of types
	 */

	public TypeList pop(Type type) {
		return pop(type.getSize());
	}

	/**
	 * Yields a list of types equal to this but beginning with the given type.
	 *
	 * @param type the type which must be pushed
	 * @return the resulting list of types
	 */

	public TypeList push(Type type) {
		TypeList result = this;

		int s = type.getSize();
		while (s-- > 0)
			result = new TypeList(type,result);

		return result;
	}

	/**
	 * Yields the first element in this list.
	 *
	 * @return the first element in this list. If the list is too short
	 *         to contain a first element, it yields the {@code unused} type
	 */

	public Type getTop() {
		if (this == EMPTY)
			return UnusedType.INSTANCE;
		else
			return head;
	}

	/**
	 * Converts this list of types into an array of BCEL types.
	 *
	 * @return an array of BCEL types corresponding to this list of Kitten types
	 */

	public org.apache.bcel.generic.Type[] toBCEL() {
		TypeList cursor;
		int pos;

		// we first count them :-(
		for (pos = 0, cursor = this; cursor != TypeList.EMPTY; pos++)
			cursor = cursor.pop(cursor.head);

		org.apache.bcel.generic.Type[] result = new org.apache.bcel.generic.Type[pos];

		// then we translate each of them into the corresponding BCEL type
		for (pos = 0, cursor = this; cursor != TypeList.EMPTY; pos++) {
			result[pos] = cursor.head.toBCEL();	 
			cursor = cursor.pop(cursor.head);
		}

		return result;
	}
}