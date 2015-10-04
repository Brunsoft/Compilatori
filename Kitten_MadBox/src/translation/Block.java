package translation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import types.CodeSignature;
import bytecode.BranchingBytecode;
import bytecode.Bytecode;
import bytecode.BytecodeList;
import bytecode.CALL;
import bytecode.FinalBytecode;
import bytecode.NOP;

/**
 * A block of code of the Kitten intermediate language. There is no jump
 * to or from internal points of the code inside the block.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Block {

	/**
	 * The successors of this block. This should not be {@code null}.
	 */

	private List<Block> follows;

	/**
	 * The bytecode inside this block.
	 */

	private BytecodeList bytecode;

	/**
	 * The unique identifier of this block.
	 */

	private final int id;

	/**
	 * True if this block can be merged when prefixed with another block.
	 */

	private boolean mergeable;

	/**
	 * The number of blocks created so far.
	 */

	private static int counter = 0;

	/**
	 * Builds a block of code with no predecessors and with the given bytecode and successors.
	 *
	 * @param bytecode the code inside the block
	 * @param follows the list of successors of this block
	 */

	private Block(BytecodeList bytecode, List<Block> follows) {
		this.bytecode = bytecode;
		this.follows = follows;
		this.mergeable = true;

		// we assign a unique identifier to this block
		this.id = counter++;
	}

	/**
	 * Builds a block of code containing a final bytecode and having no
	 * predecessors nor successors.
	 *
	 * @param bytecode the final bytecode
	 */

	public Block(FinalBytecode bytecode) {
		this(new BytecodeList(bytecode), new ArrayList<Block>());
	}

	/**
	 * Builds a block of code containing {@code nop}, with no predecessors
	 * and two successors. The branching to the two successors is decided
	 * on the basis of a Boolean {@code condition}. The {@code condition}
	 * and its negation are prefixed to the code of the two following blocks.
	 *
	 * @param condition the branching bytecode that decides the branching
	 * @param yes the block of code to be executed if the {@code condition}
	 *            holds. This should not be {@code null}
	 * @param no the block of code to be executed if the {@code condition}
	 *           does not hold. This should not be {@code null}
	 */

	public Block(BranchingBytecode condition, Block yes, Block no) {
		this(new BytecodeList(new NOP()), new ArrayList<Block>());

		// we prefix the condition and its negation to the code of the following blocks
		follows.add(no.prefixedBy(condition.negate()));
		follows.add(yes.prefixedBy(condition));
	}

	/**
	 * Builds a block of code containing {@code nop} and with no
	 * successors nor predecessors.
	 */

	public Block() {
		// we use nop for the initial code of a pivot
		this(new BytecodeList(new NOP()), new ArrayList<Block>());

		// a pivot cannot be merged, otherwise cycles cannot be built
		mergeable = false;
	}

	/**
	 * Builds a new block of code with no predecessors, no successors and with
	 * the given bytecode.
	 *
	 * @param bytecode the code inside the block
	 */

	private Block(BytecodeList bytecode) {
		this(bytecode, new ArrayList<Block>());
	}

	/**
	 * Builds a new block of code with no predecessors and with the
	 * given bytecode and successor.
	 *
	 * @param bytecode the code inside the block
	 * @param follow the only successor of this block
	 */

	private Block(BytecodeList bytecode, Block follow) {
		this(bytecode);

		follows.add(follow);
	}

	/**
	 * Yields the unique identifier of this block.
	 *
	 * @return the unique identifier of this block
	 */

	int getId() {
		return id;
	}

	/**
	 * Yields the successors of this block.
	 *
	 * @return the list of successors
	 */

	public List<Block> getFollows() {
		return follows;
	}

	/**
	 * Adds a successor to this block.
	 *
	 * @param follow the successor to be added to this block. This should not be {@code null}
	 */

	public void linkTo(Block follow) {
		follows.add(follow);
	}

	/**
	 * Specifies that this block cannot be merged when prefixed with a bytecode.
	 * See {@link #prefixedBy(Bytecode)}.
	 */

	public void doNotMerge() {
		mergeable = false;
	}

	/**
	 * Yields the bytecode inside this block.
	 *
	 * @return the bytecode inside this block
	 */

	public BytecodeList getBytecode() {
		return bytecode;
	}

	/**
	 * Adds a bytecode before this block. This results in the same
	 * block being modified or in a new block linked to {@code this}.
	 *
	 * @param bytecode the bytecode which must be prefixed to this block
	 * @return the result of prefixing {@code bytecode} to this block
	 */

	public Block prefixedBy(Bytecode bytecode) {
		// we can expand our code if we have no predecessors,
		// or otherwise we will also affect the view that our predecessors have of us
		if (mergeable) {
			this.bytecode = new BytecodeList(bytecode).append(this.bytecode);
			return this;
		}
		else
			return new Block(new BytecodeList(bytecode), this);
	}

	/**
	 * Yields a string identifying this node in a dot file.
	 *
	 * @return a string identifying this node in a dot file
	 */

	public String dotNodeName() {
		return "codeblock_" + id;
	}

	/**
	 * Cleans-up this block and all those reachable from it, also in the
	 * methods called from this block. It removes useless {@code nop}'s and merges a block, with only
	 * one successor which has only one predecessor, with that successor.
	 *
	 * @param program the program which is being cleaned-up
	 */

	void cleanUp(Program program) {
		// the start method of the program is definitely called
		program.getSigs().add(program.getStart());

		cleanUp(new HashSet<Block>(), program);
	}

	/**
	 * Auxiliary method that cleans-up this block and all those reachable
	 * from it. It removes useless {@code nop}'s and merges a block, with only
	 * one successor which has only one predecessor, with that successor.
	 *
	 * @param done the set of blocks which have been already cleaned-up
	 * @param program the program which is being cleaned-up
	 */

	private void cleanUp(Set<Block> done, Program program) {
		if (!done.contains(this)) {
			done.add(this);

			List<Block> newFollows = new ArrayList<>();

			// we consider each successor and remove isolated nop's
			for (Block follow: follows)
				if (follow != this && follow.bytecode.getHead() instanceof NOP &&
				follow.bytecode.getTail() == null)
					newFollows.addAll(follow.follows);
				else
					newFollows.add(follow);

			follows = newFollows;

			// we continue with the successors
			for (Block follow: follows)
				follow.cleanUp(done,program);

			// if the bytecode contains a reference to a field or to a
			// constructor or to a method, we add it to the signatures
			// for the program and update its statistics
			for (BytecodeList bs = bytecode; bs != null; bs = bs.getTail()) {
				Bytecode bytecode = bs.getHead();

				// we take note that the program contains the bytecodes in the block
				program.storeBytecode(bytecode);

				if (bytecode instanceof CALL)
					// we continue by cleaning the dynamic targets
					for (CodeSignature target: ((CALL) bytecode).getDynamicTargets())
						target.getCode().cleanUp(done,program);
			}
		}
	}
}