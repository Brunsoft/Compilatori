package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import types.Type;

/**
 * A bytecode that terminates the execution of a method or constructor,
 * and gives back the control to the caller. If there is a return value on top
 * of the stack, the stack elements that are under that value are just discarded.
 * <br><br>
 * ..., value -&gt; value
 * <br>
 * if this {@code return} instruction returns a non-{@code void} value, and
 * <br><br>
 * ... -&gt; emptystack
 * <br>
 * if it returns {@code void}.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class RETURN extends FinalBytecode {

	/**
	 * The semantical type of the value that is returned.
	 */

	private final Type type;

	/**
	 * Constructs a {@code return} bytecode that returns a value of the given type.
	 *
	 * @param type the type of the value returned by the {@code return} bytecode
	 */

	public RETURN(Type type) {
		this.type = type;
	}

	/**
	 * Yields the type of the value returned by this bytecode.
	 *
	 * @return the type of the value returned by this bytecode
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "return " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the {@code return} Java bytecode if {@link #type} is {@code void},
	 *         the {@code ireturn} Java bytecode if {@link #type} is {@code int} or {@code boolean}
	 *         (Booleans are represented as integers in Java bytecode, with the assumption
	 *         that 0 = <i>false</i> and 1 = <i>true</i>), the {@code freturn}
	 *         Java bytecode if {@link #type} is {@code float} and the {@code areturn} Java bytecode
	 *         if {@link #type} is a reference type
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the selection of the
		// right return bytecode, depending on type
		return new InstructionList(InstructionFactory.createReturn(type.toBCEL()));
	}
}