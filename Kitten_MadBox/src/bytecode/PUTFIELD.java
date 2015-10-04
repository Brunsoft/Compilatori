package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.FieldSignature;

/**
 * A bytecode that writes a value into a given field of an object,
 * called <i>receiver</i>. If the receiver is {@code nil}, the computation stops.
 * <br><br>
 * ..., receiver object, value -&gt; ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class PUTFIELD extends FieldAccessBytecode {

	/**
	 * The signature of the field that is written by this bytecode.
	 */

	private final FieldSignature field;

	/**
	 * Constructs a bytecode that writes into a field of an object.
	 *
	 * @param field the signature of the field that is written
	 */

	public PUTFIELD(FieldSignature field) {
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
		return "putfield " + field;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code putfield field} bytecode
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		return new InstructionList(field.createPUTFIELD(classGen));
	}
}