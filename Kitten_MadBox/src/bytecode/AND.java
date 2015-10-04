package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

/**
 * A bytecode that performs a logical <i>and</i> operation on the top
 * two elements of the stack.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 <i>and</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class AND extends BooleanBinOpBytecode {

	/**
	 * Constructs a bytecode that performs a logical <i>and</i> operation
	 * on the top two elements of the stack.
	 */

	public AND() {}

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		return new InstructionList(new org.apache.bcel.generic.IAND());
	}
}