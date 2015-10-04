package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import types.BooleanType;

/**
 * A branching bytecode that checks if the top of the stack is the Boolean value <i>false</i>.
 * It routes accordingly the computation at the end of a branching block of code.
 * <br><br>
 * ..., value -&gt; ...<br>
 * (checks if value is false)
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class IF_FALSE extends BranchingConstantComparisonBytecode {

	/**
	 * Constructs a branching bytecode that checks if the top of the stack is the
	 * Boolean value <i>false</i>. It routes accordingly the computation
	 * at the end of a conditional block of code.
	 */

	public IF_FALSE() {
		super(BooleanType.INSTANCE);
	}

	@Override
	public String toString() {
		return "if_false";
	}

	/**
	 * Yields a branching bytecode that expresses the opposite condition of this.
	 *
	 * @return an {@code if_true} bytecode
	 */

	@Override
	public BranchingBytecode negate() {
		return new IF_TRUE();
	}

	/**
	 * Auxiliary method that adds to the given list of instructions the code that goes
	 * to {@code yes} if the outcome of the test expressed by this branching bytecode is true.
	 * Namely, it generates the Java bytecode<br>
	 * <br>
	 * {@code ifeq yes}<br>
	 * <br>
	 * Note that in Java bytecodes Boolean values are represented as integers
	 * (0 = <i>false</i>, 1 = <i>true</i>), so that we use {@code ifeq} to check if the Boolean
	 * value on top of the stack is <i>true</i>.
	 *
	 * @param il the list of instructions which must be expanded
	 * @param classGen the class generator to be used to generate the code
	 * @param yes the target where one must go if the outcome of the test
	 *            expressed by this branching bytecode is true
	 */

	@Override
	protected void generateJavaBytecodeAux(InstructionList il, JavaClassGenerator classGen, InstructionHandle yes) {
		il.append(new org.apache.bcel.generic.IFEQ(yes));
	}
}