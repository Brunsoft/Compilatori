package bytecode;

/**
 * A bytecode that performs a binary operation on the top two elements of the stack.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 <i>op</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BinOpBytecode extends SequentialBytecode {

	/**
	 * Constructs a list of instructions made up of a single bytecode that
	 * performs a binary operation on the top two elements of the stack.
	 */

	protected BinOpBytecode() {}
}