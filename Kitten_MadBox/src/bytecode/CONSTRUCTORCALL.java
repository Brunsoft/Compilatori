package bytecode;

import java.util.Collections;
import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;
import types.ConstructorSignature;

/**
 * A bytecode that calls a constructor of an object. That object is the <i>receiver</i> of the call.
 * If the receiver is {@code nil}, the computation stops.
 * <br><br>
 * ..., receiver, par_1, ..., par_n -&gt; ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class CONSTRUCTORCALL extends CALL {

	/**
	 * Constructs a bytecode that calls a constructor of an object.
	 *
	 * @param constructor the signature of the constructor which is called
	 */

	public CONSTRUCTORCALL(ConstructorSignature constructor) {
		// there is only only dynamic target: the constructor itself
		super(constructor.getDefiningClass(), constructor, Collections.<CodeSignature> singleton(constructor));
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code invokespecial method} bytecode, that calls
	 *         a method by using a hard-wired class name to look up for the
	 *         method's implementation
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		return new InstructionList(((ConstructorSignature) getStaticTarget()).createINVOKESPECIAL(classGen));
	}
}