package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.FieldSignature;

/**
 * A bytecode that reads the value of a given field of an object,
 * called <i>receiver</i>. If the receiver is {@code nil}, the computation stops.
 * <br><br>
 * ..., receiver object -&gt; ..., value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class GETFIELD extends FieldAccessBytecode {

	/**
	 * The signature of the field that is read by this bytecode.
	 */

	private final FieldSignature field;

	/**
	 * Constructs a bytecode that reads a field of an object.
	 *
	 * @param field the signature of the field that is read
	 */

	public GETFIELD(FieldSignature field) {
		this.field = field;
	}

	/**
	 * Yields the field signature of this field access bytecode.
	 *
	 * @return the field signature
	 */

	@Override
	public FieldSignature getField() {
		return field;
	}

	@Override
	public String toString() {
		return "getfield " + field;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code getfield field} bytecode
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		return new InstructionList(field.createGETFIELD(classGen));
	}
}