package absyn;

import java.io.FileWriter;

import bytecode.NEWSTRING;
import bytecode.RETURN;
import bytecode.VIRTUALCALL;
import semantical.TypeChecker;
import translation.Block;
import types.ClassType;
import types.TypeList;

/**
 * A node of abstract syntax representing an assert command.
 *
 * @author <A HREF="mailto:info@madBox.it">MadBox</A>
 */

public class Assert extends Command {

	private Expression condition;

	public Assert(int pos, Expression condition) {
		super(pos);
		this.condition = condition;
	}

	@Override
	protected void toDotAux(FileWriter where) throws java.io.IOException {
			linkToNode("asserted", condition.toDot(where), where);
	}
	
	@Override
	protected TypeChecker typeCheckAux(TypeChecker checker) {
		// the condition of the assert must be a Boolean expression
		condition.mustBeBoolean(checker);
		if(!checker.isAssertOk())
			error("assert fallita!");

			return checker;
	}

	@Override
	public boolean checkForDeadcode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Block translate(Block continuation) {
		String out = creaMessaggioErrore();
		Block failed = new Block(new RETURN(ClassType.mk("String")));
	
		failed = new VIRTUALCALL(ClassType.mkFromFileName("String.kit"),
				ClassType.mkFromFileName("String.kit").methodLookup("output", TypeList.EMPTY)).followedBy(failed);
		failed = new NEWSTRING("assert " + out + "\n").followedBy(failed);
		failed = new NEWSTRING(out).followedBy(failed);
		
		return condition.translateAsTest(continuation, failed);
	}
	
	private String creaMessaggioErrore() {
		//String filename = getTypeChecker().getFileName();
		String pos = getTypeChecker().calcPos(getPos());
		return "failed at " + pos; 
		//return "\t\tAssert fallita @" + filename + ":" + pos + "\n";
	}
}
