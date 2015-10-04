package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.NumericalType;

/**
 * A bytecode that subtracts the top element of the stack from the underlying element.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 - value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class SUB extends ArithmeticBinOpBytecode {

	/**
	 * Constructs a bytecode that subtracts the top element of the stack from the underlying element.
	 *
	 * @param type the semantical type of the values that are subtracted
	 */

	public SUB(NumericalType type) {
		super(type);
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the {@code isub} Java bytecode if {@link #type} is {@code int}
	 *         and the {@code fsub} Java bytecode if it is {@code float}
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		InstructionList il = new InstructionList();

		getType().sub(il);

		return il;
	}
}