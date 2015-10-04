package semantical;

import tables.Table;
import types.Type;
import errorMsg.ErrorMsg;

/**
 * A type-checker. It maintains a symbol table and auxiliary information
 * needed to type-check Kitten source code.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class TypeChecker {

	/**
	 * The return type expected by this type-checker.
	 */

	private final Type returnType;

	/**
	 * The <i>environment</i>, i.e., a symbol table mapping variable names
	 * to their declared type.
	 */

	private final Table<TypeAndNumber> env;

	/**
	 * The number of local variables seen so far by this type-checker.
	 */

	private final int varNum;

	/**
	 * The error reporting utility used to signal errors in the source code.
	 */

	private final ErrorMsg errorMsg;

	/**
	 * The capability to contain assert command
	 * @HasGetter
	 * @HasSetter 
	 */
	private final boolean assertOk;
	
	/**
	 * Constructs a type-checker.
	 *
	 * @param returnType the return type expected by the type-checker
	 * @param env the environment of the type-checker
	 * @param varNum the number of local variables seen by the type-checker
	 * @param errorMsg the error reporting utility of the type-checker
	 */

	private TypeChecker(Type returnType, Table<TypeAndNumber> env, int varNum, ErrorMsg errorMsg, boolean assertOk) {
		this.returnType = returnType;
		this.env = env;
		this.varNum = varNum;
		this.errorMsg = errorMsg;
		this.assertOk = assertOk;

	}

	/**
	 * Constructs a type-checker having a given expected return type,
	 * a given error reporting utility, an empty symbol table and that
	 * has not seen any variable up to now.
	 *
	 * @param returnType the expected return type
	 * @param errorMsg the error reporting utility used to signal errors
	 */

	public TypeChecker(Type returnType, ErrorMsg errorMsg) {
		this.returnType = returnType;
		this.env = Table.empty();
		this.varNum = 0;
		this.errorMsg = errorMsg;
		this.assertOk = false;
	}
	
	/**
	 * Constructs a type-checker having a given expected return type,
	 * a given error reporting utility, an empty symbol table and that
	 * has not seen any variable up to now.
	 *
	 * @param returnType the expected return type
	 * @param errorMsg the error reporting utility used to signal errors
	 * @param assertAllowed 
	 */

	public TypeChecker(Type returnType, ErrorMsg errorMsg, boolean assertOk) {
		this.returnType = returnType;
		this.env = Table.empty();
		this.varNum = 0;
		this.errorMsg = errorMsg;
		this.assertOk = assertOk;
	}

	/**
	 * Yields the type expected by this type-checker for the {@code return} commands.
	 *
	 * @return the type expected by this type-checker for the {@code return} commands
	 */

	public Type getReturnType() {
		return returnType;
	}

	/**
	 * Yields a new type-checker identical to this but where a given variable
	 * has been bound to a given type.
	 *
	 * @param var the variable to be bound
	 * @param type the type to which {@code var} must be bound
	 * @return the new type-checker where {@code var} is bound to {@code type}
	 */

	public TypeChecker putVar(String var, Type type) {
		// note that in the new type-checker the number of local
		// variables is one more than in this type-checker
		return new TypeChecker(returnType,
			env.put(var, new TypeAndNumber(type, varNum)), varNum + 1, errorMsg, assertOk);
	}

	/**
	 * Yields the type bound to a given variable in this type-checker.
	 *
	 * @param var the variable
	 * @return the type bound to {@code var} in this type-checker.
	 *         Yields {@code null} if {@code var} is not bound in this type-checker
	 */

	public Type getVar(String var) {
		TypeAndNumber tan = env.get(var);

		return tan != null ? tan.getType() : null;
	}

	/**
	 * Yields the progressive number assigned to a given variable
	 * by this type-checker.
	 *
	 * @param var the variable
	 * @return the progressive number of {@code var} in some enumeration
	 *         of the variables seen so far by this type-checker. It is
	 *         guaranteed that progressive numbers are non-negative and that
	 *         two distinct variables have distinct progressive numbers. Yields
	 *         -1 if {@code var} is not bound in this type-checker
	 */

	public int getVarNum(String var) {
		TypeAndNumber tan = env.get(var);

		return tan != null ? tan.getNumber() : -1;
	}

	/**
	 * Reports an error through this type-checker.
	 *
	 * @param pos the position where the error must be reported
	 *            (number of characters from the beginning of the source file)
	 * @param msg the message to be reported
	 */

	public void error(int pos, String msg) {
		errorMsg.error(pos,msg);
	}

	/**
	 * Determines if any error has been reported up to now
	 *
	 * @return true if some error has been reported up to now, false otherwise
	 */

	public boolean anyErrors() {
		return errorMsg.anyErrors();
	}
	
	/**
	 * Yields the type expected by this type-checker for the {@code return} commands.
	 *
	 * @return the type expected by this type-checker for the {@code return} commands
	 */

	public boolean isAssertOk() {
		return assertOk;
	}
	
	public String calcPos(int pos) {
		return errorMsg.calcPos(pos);
	}
	
	/**
	 * Yields the name of the source file.
	 *
	 * @return the name of the source file
	 */

	public String getFileName() {
		return errorMsg.getFileName();
	}
}