package bytecode;

import java.util.Set;

import types.CodeSignature;
import types.Type;

/**
 * A bytecode that calls a method of a <i>receiver</i>.
 * <br><br>
 * ..., receiver, par_1, ..., par_n -&gt; ..., returned value<br>
 * if the method return type is non-{@code void}<br><br>
 * ..., receiver, par_1, ..., par_n -&gt; ...<br>
 * if the method's return type is {@code void}.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class CALL extends SequentialBytecode {

	/**
	 * The static type of the receiver of this call. The dynamic type
	 * might be this type or every subclass of this type.
	 */

	private final Type receiverType;

	/**
	 * The signature of the static target method or constructor of the call. The dynamic target
	 * of the call might be every redefinition of this in {@link #receiverType} and its subclasses.
	 */

	private final CodeSignature staticTarget;

	/**
	 * The signatures of the dynamic target methods or constructors of this call. They are
	 * redefinitions of {@link #staticTarget} in {@link #receiverType} and its subclasses.
	 */

	private final Set<CodeSignature> dynamicTargets;	

	/**
	 * Constructs a bytecode that calls a method.
	 *
	 * @param receiverType the static type of the receiver of this call
	 * @param staticTarget the signature of the static target of the call
	 * @param dynamicTargets the set of dynamic targets for the call
	 */

	protected CALL(Type receiverType, CodeSignature staticTarget, Set<CodeSignature> dynamicTargets) {
		this.receiverType = receiverType;
		this.staticTarget = staticTarget;
		this.dynamicTargets = dynamicTargets;
	}

	/**
	 * Yields the type of the receiver of this call.
	 *
	 * @return the type of the receiver of this call
	 */

	public final Type getReceiverType() {
		return receiverType;
	}

	/**
	 * Yields the static target of this method call instruction.
	 *
	 * @return the static target
	 */

	public final CodeSignature getStaticTarget() {
		return staticTarget;
	}

	/**
	 * Yields the set of dynamic targets of this instruction.
	 *
	 * @return the set of dynamic targets
	 */

	public final Set<CodeSignature> getDynamicTargets() {
		return dynamicTargets;
	}

	@Override
	public String toString() {
		return "call " + staticTarget + " " + dynamicTargets;
	}
}