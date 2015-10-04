package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.NumericalType;

/**
 * A bytecode that divides the top element of the stack by the underlying element.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 / value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class DIV extends ArithmeticBinOpBytecode {

	/**
	 * Constructs a bytecode that divides the top element of the stack by the underlying element.
	 *
	 * @param type the semantical type of the values that are divided
	 */

	public DIV(NumericalType type) {
		super(type);
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the {@code idiv} Java bytecode if {@link #type} is {@code int}
	 *         and the {@code fdiv} Java bytecode {@link #type} is {@code float}
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		InstructionList il = new InstructionList();

		getType().div(il);

		return il;
	}
}