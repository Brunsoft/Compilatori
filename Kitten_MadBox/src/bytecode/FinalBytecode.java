package bytecode;

/**
 * A bytecode of the intermediate Kitten language with no subsequent bytecode.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */


public abstract class FinalBytecode extends NonBranchingBytecode implements NonCallingBytecode {

	/**
	 * Constructs a final bytecode.
	 */

	protected FinalBytecode() {}
}