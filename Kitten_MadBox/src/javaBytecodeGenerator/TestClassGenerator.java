package javaBytecodeGenerator;

import java.util.Set;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

import translation.Block;
import types.ClassMemberSignature;
import types.ClassType;
import types.FixtureSignature;
import types.TestSignature;

/**
 * A Java bytecode generator. It transforms the Kitten intermediate language
 * into Java bytecode that can be dumped to Java class files and run.
 * It uses the BCEL library to represent Java classes and dump them on the file-system.
 *
 * @author <A HREF="mailto:info@MadBox.it">MaxBox</A>
 */


@SuppressWarnings("serial")
public class TestClassGenerator extends JavaClassGenerator {

	ClassType clazz;
	
	public TestClassGenerator(ClassType clazz, Set<ClassMemberSignature> sigs) {
		super(clazz.getName() + "Test", // name of the class
				// the superclass of the Kitten Object class is set to be the Java java.lang.Object class
				clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : "java.lang.Object",
				clazz.getName() + ".kit" // source file
				);

		this.clazz = clazz;
		// we add the tests
		for (FixtureSignature fix: clazz.getFixtures())
			fix.createFixture(this);
		// we add the fixtures
		for (TestSignature test: clazz.getTests())
			test.createTest(this);
		
		this.createMain();
	}
	
	public void createMain() {
		MethodGen methodGen;
			methodGen = new MethodGen
				(Constants.ACC_PUBLIC | Constants.ACC_STATIC, // public and static
				org.apache.bcel.generic.Type.VOID, // return type
				new org.apache.bcel.generic.Type[] // parameters
					{ new org.apache.bcel.generic.ArrayType("java.lang.String", 1) },
				null, // parameters names: we do not care
				"main", // method's name
				this.getClassName(), // defining class
				new InstructionList(),
				this.getConstantPool()); // constant pool
		
		// variabili locali
		LocalVariableGen time = methodGen.addLocalVariable("time", Type.INT, null, null);
		LocalVariableGen testTime = methodGen.addLocalVariable("testTime", Type.INT, null, null);
		LocalVariableGen count = methodGen.addLocalVariable("count", Type.INT, null, null);
		
		// instructions
		InstructionList iList = new InstructionList();
		
		// inizializzo var locali
		iList.append(InstructionFactory.ICONST_0);
		iList.append(new ISTORE(count.getIndex()));
		
		iList.append(InstructionFactory.ICONST_0);
		iList.append(new ISTORE(time.getIndex()));
		
		iList.append(InstructionFactory.ICONST_0);
		iList.append(new ISTORE(testTime.getIndex()));	
		
		iList.append(getFactory().createGetStatic("java/lang/System", "out", 
				Type.getType(java.io.PrintStream.class)));
		
		// costruisco stringbuffer
		iList.append(getFactory().createNew(Type.STRINGBUFFER));
		iList.append(InstructionFactory.DUP);
		iList.append(new LDC(getConstantPool().
				addString("\nTest execution for class " + this.clazz.getName() + "\n")));
		iList.append(getFactory().createInvoke("java.lang.StringBuffer", "<init>",
	                Type.VOID, new Type[]{Type.STRING},
	                Constants.INVOKESPECIAL));

		
		InstructionHandle inizioTempoTot = iList.getEnd();
		for (TestSignature test: clazz.getTests()) {
			InstructionList ilTest = new InstructionList();
			
			// concat test name to stringbuffer
			ilTest.append(new LDC(getConstantPool().addString("\t- " + test.getName() + ": ")));
			ilTest.append(getFactory().createInvoke("java.lang.StringBuffer", "append",
	                Type.STRINGBUFFER, 
	                new Type[]{Type.STRING},
	                Constants.INVOKEVIRTUAL));

			// create test
			ilTest.append(instructionTest(test, clazz.getFixtures()));
			
			// result
			ilTest.append(getFactory().createInvoke("runTime/String", "toString",
                Type.STRING, Type.NO_ARGS,
	                Constants.INVOKEVIRTUAL));
			ilTest.append(InstructionFactory.DUP);

			// confronto toString con passed
			ilTest.append(new LDC(getConstantPool().addString("passed")));
			ilTest.append(getFactory().createInvoke("java/lang/String", "equals",
	                Type.BOOLEAN, 
	                new Type[]{Type.OBJECT},
	                Constants.INVOKEVIRTUAL));
		
			// if passed result = 1 -> INDEX + 1
			ilTest.append(new ILOAD(count.getIndex()));
			ilTest.append(InstructionFactory.IADD);
			ilTest.append(new ISTORE(count.getIndex()));
						
			// concat test result to stringbuffer
			ilTest.append(getFactory().createInvoke("java.lang.StringBuffer", "append",
	                Type.STRINGBUFFER, 
	                new Type[]{Type.STRING},
	                Constants.INVOKEVIRTUAL));
			
			calcTime(ilTest, ilTest.getStart(), ilTest.getEnd(), testTime.getIndex());
			iList.append(ilTest);
		}
		
		// print the last line "n tests passed, m failed [xyz ms]"
		
		// # tests passed
		iList.append(new ILOAD(count.getIndex()));
		iList.append(getFactory().createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, new Type[]{Type.INT},
                Constants.INVOKEVIRTUAL));

		iList.append(new LDC(getConstantPool().addString(" test(s) passed ")));
		iList.append(getFactory().createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, new Type[]{Type.STRING},
                Constants.INVOKEVIRTUAL));
				
		
		// # tests failed		
		iList.append(new LDC(getConstantPool().addInteger(clazz.getTests().size())));
		iList.append(new ILOAD(count.getIndex()));
		iList.append(InstructionFactory.ISUB);
		iList.append(getFactory().createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, new Type[]{Type.INT},
                Constants.INVOKEVIRTUAL));
		
		iList.append(new LDC(getConstantPool().addString(" failed")));
		iList.append(getFactory().createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, new Type[]{Type.STRING},
                Constants.INVOKEVIRTUAL));
				
		calcTime(iList, inizioTempoTot, iList.getEnd(), time.getIndex());
	
        iList.append(getFactory().createInvoke("java.lang.StringBuffer", "toString",
                Type.STRING, Type.NO_ARGS,
                Constants.INVOKEVIRTUAL));
        
		iList.append(getFactory().createInvoke(
				"java/io/PrintStream", 
				"print", 
				Type.VOID, 
				new org.apache.bcel.generic.Type[]{org.apache.bcel.generic.Type.STRING},
				org.apache.bcel.Constants.INVOKEVIRTUAL
		));
		iList.append(InstructionFactory.createReturn(Type.VOID));
		
			
		methodGen.setInstructionList(iList);
		// we must always call these methods before the getMethod()
		// method below. They set the number of local variables and stack
		// elements used by the code of the method
		methodGen.setMaxStack();
		methodGen.setMaxLocals();

		// we add a method to the class that we are generating
		this.addMethod(methodGen.getMethod());
	}
	// nanoTime
	private InvokeInstruction currentMillis() {
		return getFactory().createInvoke(
				"java/lang/System", 
				"currentTimeMillis",	
				org.apache.bcel.generic.Type.LONG, 
				new org.apache.bcel.generic.Type[]{},
				org.apache.bcel.Constants.INVOKESTATIC
		);
	}
	
	private void calcTime(InstructionList il, InstructionHandle start, InstructionHandle end, int index) {
		InstructionList prima = new InstructionList();
		prima.append(currentMillis());				// getCurrentMillis LONG
		prima.append(InstructionConstants.L2I);		// current time INT
		prima.append(new ISTORE(index));			// current time -> index

		InstructionList dopo = new InstructionList();
		// calc time
		dopo.append(currentMillis());				// getCurrentMillis LONG
		dopo.append(InstructionConstants.L2I);		// current time INT
		dopo.append(new ILOAD(index));				// <- index (start current time)				
		dopo.append(InstructionFactory.ISUB);		// current time - start current time		
		dopo.append(new ISTORE(index));				// result -> index
		
		// append time
		dopo.append(new LDC(getConstantPool().addString(" [")));
		dopo.append(getFactory().createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, 
                new Type[]{Type.STRING},
                Constants.INVOKEVIRTUAL));
		
		dopo.append(new ILOAD(index));
		dopo.append(getFactory().createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, 
                new Type[]{Type.INT},
                Constants.INVOKEVIRTUAL));
		
		dopo.append(new LDC(getConstantPool().addString("ms]\n")));
		dopo.append(getFactory().createInvoke("java.lang.StringBuffer", "append",
                Type.STRINGBUFFER, 
                new Type[]{Type.STRING},
                Constants.INVOKEVIRTUAL));
		
		il.append(start, prima);
		il.append(end, dopo);
	}
	
	private InstructionList instructionTest(TestSignature test, Set<FixtureSignature> fixtures) {
		InstructionList il = new InstructionList();
		//il.append(instructionPrint("\t- Test: " + test.getName() + " "));
		
		// Creo un nuovo oggetto
		il.append(getFactory().createNew(this.clazz.getName()));
		// stack:	obj
		
		// dup
		il.append(InstructionFactory.DUP);
		// stack:	obj
		//			obj

		// chiamo il costruttore
		il.append(getFactory().createInvoke(
				this.clazz.getName(), 
				"<init>", 
				org.apache.bcel.generic.Type.VOID, 
				new org.apache.bcel.generic.Type[]{},
				org.apache.bcel.Constants.INVOKESPECIAL
		));
		// stack:	obj
		
		for (FixtureSignature fixture: clazz.getFixtures()) {		
			// dup
			il.append(InstructionFactory.DUP);
			// stack:	obj
			//			obj
			il.append(getFactory().createInvoke(
					this.clazz.getName() + "Test", 
					fixture.getName(), 
					org.apache.bcel.generic.Type.VOID, 
					new org.apache.bcel.generic.Type[]{clazz.toBCEL()},
					org.apache.bcel.Constants.INVOKESTATIC
			));
		}
		
		// chiamo il test
		il.append(getFactory().createInvoke(
				this.clazz.getName() + "Test", 
				test.getName(), 
				new org.apache.bcel.generic.ObjectType(runTime.String.class.getName()),
				new org.apache.bcel.generic.Type[]{clazz.toBCEL()},
				org.apache.bcel.Constants.INVOKESTATIC
		));
		
		return il;
	}
}