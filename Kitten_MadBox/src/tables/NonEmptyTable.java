package tables;

/**
 * A non-empty symbol table. It is organized as a binary search tree.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

final class NonEmptyTable<E> extends Table<E> {

	/**
	 * the key on top of the tree.
	 */

	private final String key;

	/**
	 * the value bound on key at the top of the tree.
	 */

	private final E value;

	/**
	 * the left subtree.
	 */

	private final Table<E> left;

	/**
	 * the right subtree.
	 */

	private final Table<E> right;

	/**
	 * Builds a non-empty table.
	 *
	 * @param key the key in the root of the tree
	 * @param value the value bound to <tt>key</tt>
	 * @param left the left subtree
	 * @param right the right subtree
	 */

	private NonEmptyTable(String key, E value, Table<E> left, Table<E> right) {
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
	}

	/**
	 * Builds a non-empty table having empty subtrees.
	 *
	 * @param key the key in the root of the tree
	 * @param value the value bound to {@code key}
	 */

	NonEmptyTable(String key, E value) {
		this.key = key;
		this.value = value;
		this.left = Table.empty();
		this.right = Table.empty();
	}

	@Override
	public E get(String key) {
		int comp = this.key.compareTo(key);

		if (comp < 0)
			return left.get(key);
		else if (comp == 0)
			return value;
		else
			return right.get(key);
	}

	@Override
	public Table<E> put(String key, E value) {
		int comp = this.key.compareTo(key);

		if (comp < 0) {
			Table<E> temp = left.put(key,value);
			if (temp == left)
				return this;
			else
				return new NonEmptyTable<E>(this.key, this.value, temp, right);
		}
		else if (comp == 0)
			if (value == this.value)
				return this;
			else
				return new NonEmptyTable<E>(this.key, value, left, right);
		else {
			Table<E> temp = right.put(key,value);
			if (temp == right)
				return this;
			else
				return new NonEmptyTable<E>(this.key, this.value, left, temp);
		}
	}
}