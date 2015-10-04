package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import types.IntType;
import types.NumericalType;

/**
 * A bytecode that checks if the one but last element of the stack is less than
 * its last element. It leaves on the stack the Boolean result of the comparison.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 &lt; value2
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class LT extends ComparisonNumericalBinOpBytecode {

	/**
	 * Constructs a bytecode that checks if the element under the top of the stack
	 * is less than the element on top of the stack.
	 *
	 * @param type the semantical type of the values that are compared
	 */

	public LT(NumericalType type) {
		super(type);
	}

	/**
	 * Yields a branching version of this bytecode.
	 *
	 * @return an {@code if_cmplt} bytecode for the same type as this
	 */

	@Override
	public BranchingComparisonBytecode toBranching() {
		return new IF_CMPLT(getType());
	}

	/**
	 * Generates the Java bytecode that leaves on the stack the Boolean value resulting
	 * from a &lt; comparison of two values. Namely, it generates the Java bytecode<br>
	 * <br>
	 * {@code if_icmplt after}<br>
	 * {@code iconst 0}<br>
	 * {@code goto follow}<br>
	 * {@code after: iconst 1}<br>
	 * {@code follow: nop}<br>
	 * <br>
	 * if {@link #type} is {@code int}, and<br>
	 * <br>
	 * {@code fcmpl}<br>
	 * {@code iflt after}<br>
	 * {@code iconst 0}<br>
	 * {@code goto follow}<br>
	 * {@code after: iconst 1}<br>
	 * {@code follow: nop}<br>
	 * <br>
	 * if {@link #type} is {@code float}. The {@code fcmpl} Java bytecode operates over
	 * two {@code float} values on top of the stack and produces an
	 * {@code int} value at their place, as it follows:<br>
	 * <br>
	 * ..., value1, value2 -&gt; ..., 1   if value1 &gt; value2<br>
	 * ..., value1, value2 -&gt; ..., 0   if value1 = value2<br>
	 * ..., value1, value2 -&gt; ..., -1  if value1 &lt; value2
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java bytecode as above, depending on {@link #type}
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		InstructionList il = new InstructionList(InstructionFactory.NOP);

		InstructionHandle follow = il.getStart();
		InstructionHandle after = il.insert(InstructionFactory.ICONST_1);
		il.insert(new org.apache.bcel.generic.GOTO(follow));
		il.insert(InstructionFactory.ICONST_0);

		if (getType() == IntType.INSTANCE)
			il.insert(new org.apache.bcel.generic.IF_ICMPLT(after));
		else {
			il.insert(new org.apache.bcel.generic.IFLT(after));
			il.insert(InstructionFactory.FCMPL);
		}

		return il;
	}
}