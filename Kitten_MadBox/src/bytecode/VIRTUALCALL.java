package bytecode;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.ClassType;
import types.CodeSignature;
import types.MethodSignature;

/**
 * A bytecode that calls a method of an object with dynamic lookup.
 * That object is the <i>receiver</i> of the call. If the receiver is
 * {@code nil}, the computation stops.
 * <br><br>
 * ..., receiver, par_1, ..., par_n -&gt; ..., returned value<br>
 * if the method return type is non-{@code void}<br><br>
 * ..., receiver, par_1, ..., par_n -&gt; ...<br>
 * if the method's return type is {@code void}
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class VIRTUALCALL extends CALL {

	/**
	 * Constructs a bytecode that calls a method of an object with dynamic
	 * lookup. The set of runtime targets is assumed to be that obtained
	 * from every subclass of the static type of the receiver.
	 *
	 * @param receiverType the static type of the receiver of this call
	 * @param staticTarget the signature of the static target of the call
	 */

	public VIRTUALCALL(ClassType receiverType, MethodSignature staticTarget) {
		// we compute the dynamic targets by assuming that the runtime
		// type of the receiver is any subclass of its static type
		super(receiverType, staticTarget, dynamicTargets(receiverType.getInstances(), staticTarget));
	}

	/**
	 * Yields the set of runtime receivers of this call. They are all
	 * methods with the same signature of the static target and that might
	 * be called from a given set of runtime classes for the receiver.
	 *
	 * @param possibleRunTimeClasses the set of runtime classes for the receiver
	 * @param staticTarget the static target of the call
	 * @return the set of method signatures that might be called
	 *         with the given set of classes as receiver
	 */

	private static Set<CodeSignature> dynamicTargets(List<ClassType> possibleRunTimeClasses, CodeSignature staticTarget) {
		Set<CodeSignature> dynamicTargets = new HashSet<CodeSignature>();

		for (ClassType rec: possibleRunTimeClasses) {
			// we look up for the method from the dynamic receiver
			MethodSignature candidate = rec.methodLookup(staticTarget.getName(), staticTarget.getParameters());

			// we add the dynamic target
			if (candidate != null)
				dynamicTargets.add(candidate);
		}

		return dynamicTargets;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode. Namely, it generates an
	 * {@code invokevirtual staticTarget} Java bytecode. The Java {@code invokevirtual} bytecode
	 * calls a method by using the runtime class of the receiver to look up for the method's implementation.
	 *
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java {@code invokevirtual staticTarget} bytecode
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		return new InstructionList(((MethodSignature) getStaticTarget()).createINVOKEVIRTUAL(classGen));
	}
}