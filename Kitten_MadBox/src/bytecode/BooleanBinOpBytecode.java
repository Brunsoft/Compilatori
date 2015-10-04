package bytecode;

/**
 * A bytecode that computes a Boolean operation on the top two
 * elements of the stack.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 <i>op</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BooleanBinOpBytecode extends BinOpBytecode {

	/**
	 * Constructs a Boolean binary operation bytecode that
	 * computes a Boolean operation on the top two elements of the stack.
	 */

	protected BooleanBinOpBytecode() {
	}
}