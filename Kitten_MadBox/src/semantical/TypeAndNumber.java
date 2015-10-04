package semantical;

import types.Type;

/**
 * A pair made of a type and an integer number. They represent the static
 * type and the progressive number of a local variable inside a method
 * or constructor.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class TypeAndNumber {

	/**
	 * The type of the variable.
	 */

	private final Type type;

	/**
	 * The progressive number of the variable.
	 */

	private final int number;

	/**
	 * Constructs a pair of the type and progressive number
	 * of a local variable inside a method or constructor,
	 *
	 * @param type the type
	 * @param number the progressive number
	 */

	public TypeAndNumber(Type type, int number) {
		this.type = type;
		this.number = number;
	}

	/**
	 * Yields the type of the variable.
	 *
	 * @return the type
	 */

	public Type getType() {
		return type;
	}

	/**
	 * Yields the progressive number of the variable.
	 *
	 * @return the progressive number
	 */

	public int getNumber() {
		return number;
	}
}