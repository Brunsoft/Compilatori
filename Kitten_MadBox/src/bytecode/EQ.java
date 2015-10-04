package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import types.BooleanType;
import types.ComparableType;
import types.FloatType;
import types.IntType;

/**
 * A bytecode which checks if the top two elements of the stack are the same.
 * It leaves on the stack the Boolean result of the comparison.
 * <br><br>
 * ..., value1, value2 -&gt; ..., value1 = value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class EQ extends ComparisonBinOpBytecode {

	/**
	 * Constructs a bytecode that checks if the top two elements of the stack are the same.
	 *
	 * @param type the semantical type of the values which are added
	 */

	public EQ(ComparableType type) {
		super(type);
	}

	/**
	 * Yields a branching version of this bytecode.
	 *
	 * @return an {@code if_cmpeq} bytecode for the same type as this
	 */

	@Override
	public BranchingComparisonBytecode toBranching() {
		return new IF_CMPEQ(getType());
	}

	/**
	 * Generates Java bytecode that leaves on the stack the Boolean value resulting from
	 * an equality comparison of two values. Namely, it generates the Java bytecode<br>
	 * <br>
	 * {@code if_icmpeq after}<br>
	 * {@code iconst 0}<br>
	 * {@code goto follow}<br>
	 * {@code after: iconst 1}<br>
	 * {@code follow: nop}<br>
	 * <br>
	 * if {@link #type} is {@code int} or {@code boolean} (Booleans are represented as
	 * integers in Java bytecode, with the assumption that 0 = <i>false</i> and 1 = <i>true</i>),<br>
	 * <br>
	 * {@code if_acmpeq after}<br>
	 * {@code iconst 0}<br>
	 * {@code goto follow}<br>
	 * {@code after: iconst 1}<br>
	 * {@code follow: nop}<br>
	 * <br>
	 * if {@link #type} is a class or array type, and<br>
	 * <br>
	 * {@code fcmpl}<br>
	 * {@code ifeq after}<br>
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

		InstructionHandle end = il.getStart();
		InstructionHandle after = il.insert(InstructionFactory.ICONST_1);
		il.insert(new org.apache.bcel.generic.GOTO(end));
		il.insert(InstructionFactory.ICONST_0);

		if (getType() == IntType.INSTANCE || getType() == BooleanType.INSTANCE)
			il.insert(new org.apache.bcel.generic.IF_ICMPEQ(after));
		else if (getType() == FloatType.INSTANCE) {
			il.insert(new org.apache.bcel.generic.IFEQ(after));
			il.insert(InstructionFactory.FCMPL);
		}
		else // classes or arrays
			il.insert(new org.apache.bcel.generic.IF_ACMPEQ(after));

		return il;
	}
}