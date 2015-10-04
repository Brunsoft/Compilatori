package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.BooleanType;
import types.FloatType;
import types.IntType;
import types.NilType;
import types.Type;

/**
 * A bytecode that loads a {@code nil}, Boolean, {@code int} or {@code float}
 * constant on top of the stack.
 * <br><br>
 * ... -&gt; ..., constant
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class CONST extends SequentialBytecode {

	/**
	 * The constant that is loaded on top of the stack.
	 */

	private final Object constant;

	/**
	 * Constructs a bytecode that loads the given constant on top of the stack.
	 *
	 * @param constant the constant to be loaded on top of the stack
	 */

	private CONST(Object constant) {
		this.constant = constant;
	}

	/**
	 * Constructs a bytecode that loads a {@code nil} constant on top of the stack.
	 */

	public CONST() {
		this(null);
	}

	/**
	 * Constructs a bytecode that loads a Boolean constant on top of the stack.
	 *
	 * @param constant the Boolean constant that is loaded on top of the stack
	 */

	public CONST(boolean constant) {
		this(new Boolean(constant));
	}

	/**
	 * Constructs a bytecode that loads an integer constant on top of the stack.
	 *
	 * @param constant the integer constant that is loaded on top of the stack
	 */

	public CONST(int constant) {
		this(new Integer(constant));
	}

	/**
	 * Constructs a bytecode that loads a {@code float} constant on top of the stack.
	 *
	 * @param constant the {@code float} constant that is loaded on top of the stack
	 */

	public CONST(float constant) {
		this(new Float(constant));
	}

	/**
	 * Yields the constant that is loaded on top of the stack.
	 *
	 * @return the constant that is loaded on top of the stack
	 */

	protected Object getConstant() {
		return constant;
	}

	/**
	 * Yields the type of the constant that is loaded on top of the stack.
	 *
	 * @return the semantical type of the constant that is loaded on top of the stack
	 */

	public Type getType() {
		if (constant == null)
			return NilType.INSTANCE;
		else if (constant instanceof Boolean)
			return BooleanType.INSTANCE;
		else if (constant instanceof Integer)
			return IntType.INSTANCE;
		else
			return FloatType.INSTANCE;
	}

	@Override
	public String toString() {
		return "const " + constant;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code iconst}, {@code fconst}, {@code ldc},
	 *         {@code aconst_null} or {@code bipush} Java bytecode,
	 *         on the basis of the type and size of {@link #constant}
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		if (constant == null)
			return new InstructionList(new org.apache.bcel.generic.ACONST_NULL());
		else
			// the instruction factory will create the appropriate instruction
			return new InstructionList(classGen.getFactory().createConstant(constant));
	}
}