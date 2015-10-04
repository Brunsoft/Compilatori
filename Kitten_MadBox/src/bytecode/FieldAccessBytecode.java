package bytecode;

import types.FieldSignature;

/**
 * A bytecode that accesses a field.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class FieldAccessBytecode extends SequentialBytecode {

	/**
	 * Builds a bytecode that accesses a field.
	 */

	protected FieldAccessBytecode() {}

	/**
	 * Yields the field signature of this field access bytecode.
	 *
	 * @return the field signature
	 */

	public abstract FieldSignature getField();
}