package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import types.BooleanType;
import types.NumericalType;
import types.Type;

/**
 * A bytecode that negates the top element of the stack. It works on
 * numerical values as well as on Boolean values.
 * <br><br>
 * ..., value -&gt; ..., negation of value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NEG extends SequentialBytecode {

	/**
	 * The type of the element on top of the stack.
	 */

	private final Type type;

	/**
	 * Constructs a bytecode that negates the Boolean top element of the stack
	 *
	 * @param type the Boolean type
	 */

	public NEG(BooleanType type) {
		this.type = type;
	}

	/**
	 * Constructs a bytecode that negates the numerical top element of the stack.
	 *
	 * @param type the type of the numerical constant that is negated
	 */

	public NEG(NumericalType type) {
		this.type = type;
	}

	/**
	 * Yields the type of the value that is negated by this bytecode.
	 * This is either the Boolean type or a numerical type.
	 *
	 * @return the type of the value that is negated by this bytecode
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "neg " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode. Namely,
	 * it generates an {@code ineg} Java bytecode if {@link #type} is {@code int},
	 * an {@code fneg} Java bytecode if {@link #type} is {@code float} and the code
	 * <br>
	 * {@code ifeq} <i>after</i><br>
	 * {@code iconst 0}<br>
	 * {@code goto} <i>end</i><br>
	 * <i>after:</i> {@code iconst 1}<br>
	 * <i>end:</i> {@code nop}
	 * <br>
	 * if {@link #type} is {@code boolean}, since in the Java bytecode the integer constant
	 * <i>0</i> is used for <i>false</i> and <i>1</i> is used for <i>true</i>.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code ineg} or similar bytecode, if {@link #type} is
	 *         the corresponding numerical type, or the sequence seen above
	 *         if {@link #type} is {@code boolean}
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		InstructionList il = new InstructionList();

		if (type == BooleanType.INSTANCE) {
			// the negation of a Boolean value: it becomes an alternative of 0 or 1 on the stack
			InstructionHandle end = il.insert(InstructionFactory.NOP);
			InstructionHandle after = il.insert(InstructionFactory.ICONST_1);
			il.insert(new org.apache.bcel.generic.GOTO(end));
			il.insert(InstructionFactory.ICONST_0);
			il.insert(new org.apache.bcel.generic.IFEQ(after));
		}
		else
			((NumericalType) type).neg(il);

		return il;
	}
}