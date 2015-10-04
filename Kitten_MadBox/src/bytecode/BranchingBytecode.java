package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

/**
 * A bytecode with two subsequent bytecodes. It checks a condition and consequently routes
 * the computation at the end of a branching block of code.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BranchingBytecode extends Bytecode implements NonCallingBytecode {

	/**
	 * Constructs a branching bytecode.
	 */

	protected BranchingBytecode() {}

	/**
	 * Yields a branching bytecode that expresses the opposite condition of this.
	 *
	 * @return a branching bytecode that expresses the opposite condition of this
	 */

	public abstract BranchingBytecode negate();

	/**
	 * Generates the Java bytecode that checks the condition expressed by this
	 * bytecode and goes to one of two possible targets depending on the
	 * outcome of that check. Namely, it generates the code
	 * <br><br>
	 * {@code generateJavaBytecodeAux(...yes)}<br>
	 * {@code goto no}<br>
	 *
	 * @param classGen the Java class generator to be used for this code generation
	 * @param yes the target if the check is satisfied
	 * @param no the target if the check in not satisfied
	 * @return the Java bytecode that checks the condition expressed by this bytecode and
	 *         goes to {@code yes} or {@code no} depending on the outcome of that check
	 */

	public final InstructionList generateJavaBytecode(JavaClassGenerator classGen, InstructionHandle yes, InstructionHandle no) {
		InstructionList il = new InstructionList();

		// builds the instructions that go to yes if the test is true
		generateJavaBytecodeAux(il, classGen, yes);

		il.append(new org.apache.bcel.generic.GOTO(no));

		return il;
	}

	/**
	 * Auxiliary method that adds to the given list of instructions the code that goes
	 * to {@code yes} if the outcome of the test expressed by this branching bytecode is true.
	 *
	 * @param il the list of instructions that must be expanded
	 * @param classGen the class generator to be used to generate the code
	 * @param yes the target where one must go if the outcome of the test
	 *            expressed by this branching bytecode is true
	 */

	protected abstract void generateJavaBytecodeAux(InstructionList il, JavaClassGenerator classGen, InstructionHandle yes);
}