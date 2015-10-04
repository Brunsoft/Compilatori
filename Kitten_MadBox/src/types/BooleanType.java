package types;

/**
 * The {@code boolean} type of the Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class BooleanType extends PrimitiveType {

	/**
	 * The unique instance of Boolean type. Always use this constant to refer
	 * to the Boolean type, so that type comparison can be carried out by simple == tests.
	 */

	public final static BooleanType INSTANCE = new BooleanType();

	private BooleanType() {
		// this constructor is private, the only Boolean type object is INSTANCE
	}

	@Override
	public String toString() {
		return "boolean";
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other == BooleanType.INSTANCE;
	}

	@Override
	public org.apache.bcel.generic.Type toBCEL() {
		return org.apache.bcel.generic.Type.BOOLEAN;
	}
}