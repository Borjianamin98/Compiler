package semantic.syntaxTree.statement.controlflow.loop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.ArrayDCL;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.binaryOperation.constValue.IntegerConst;
import semantic.syntaxTree.identifier.ArrayVariable;
import semantic.syntaxTree.identifier.SimpleVariable;
import semantic.syntaxTree.identifier.Variable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;
import semantic.syntaxTree.statement.assignment.DirectAssignment;
import semantic.syntaxTree.statement.controlflow.BreakStatement;
import semantic.syntaxTree.statement.controlflow.ContinueStatement;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;

public class Foreach extends Statement {
    private String identifierName;
    private Variable iterator;
    private Block body;

    public Foreach(String identifierName, Variable iterator, Block body) {
        this.identifierName = identifierName;
        this.iterator = iterator;
        this.body = body;
    }

    @Override
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv) {
        /***
         * Code like this:
         *
         * for ( Type Identifier : Expression ) Statement
         *
         * converted to:
         *
         * T[] a = Expression;
         * for (int i = 0; i < a.length; i++) {
         *     Type Identifier = a[i];
         *     Statement
         * }
         */
        SymbolTable top = Display.top();

        if (!(iterator.getDSCP().getType() instanceof ArrayTypeDSCP))
            throw new RuntimeException(iterator.getDSCP().getName() + " is not iterable");
        ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) iterator.getDSCP().getType();
        String varIteratorName = top.getTempName();
        String varCounterName = top.getTempName();
        SimpleVariable varIterator = new SimpleVariable(varIteratorName);
        SimpleVariable varCounter = new SimpleVariable(varCounterName);
        SimpleVariable varIdentifier = new SimpleVariable(identifierName);
        Label outLabel = new Label();
        Label conditionLabel = new Label();
        Label stepLabel = new Label();

        // create T[] a = Expression;
        ArrayDCL arrayDCL = new ArrayDCL(varIteratorName, arrayTypeDSCP.getBaseType().getName(), arrayTypeDSCP.getDimensions(), false, false);
        arrayDCL.generateCode(currentClass, currentMethod, cv, mv);
        DirectAssignment iteratorAssignment = new DirectAssignment(varIterator, iterator);
        iteratorAssignment.generateCode(currentClass, currentMethod, cv, mv);

        // create int i = 0;
        VariableDCL varCounterDCL = new VariableDCL(varCounterName, "int", false, false);
        varCounterDCL.generateCode(currentClass, currentMethod, cv, mv);
        DirectAssignment counterAssignment = new DirectAssignment(varCounter, new IntegerConst(0));
        counterAssignment.generateCode(currentClass, currentMethod, cv, mv);

        // create (i < a.length) condition
        mv.visitLabel(conditionLabel);
        varCounter.generateCode(currentClass, currentMethod, cv, mv);
        varIterator.generateCode(currentClass, currentMethod, cv, mv);
        mv.visitInsn(Opcodes.ARRAYLENGTH);
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, outLabel);

        // create Type Identifier;
        Declaration varIdentifierDCL;
        if (arrayTypeDSCP.getInternalType() instanceof ArrayTypeDSCP)
            varIdentifierDCL = new ArrayDCL(identifierName, arrayTypeDSCP.getBaseType().getName(),
                    arrayTypeDSCP.getInternalType().getDimensions(), false, false);
        else
            varIdentifierDCL = new VariableDCL(identifierName, arrayTypeDSCP.getInternalType().getName(), false, false);
        varIdentifierDCL.generateCode(currentClass, currentMethod, cv, mv);

        // create Identifier = a[i];
        ArrayVariable varId = new ArrayVariable(varIterator, varCounter);
        DirectAssignment identifierAssignment = new DirectAssignment(varIdentifier, varId);
        identifierAssignment.generateCode(currentClass, currentMethod, cv, mv);

        // create body
        Display.add(true);
        for (BlockCode blockCode : body.getBlockCodes()) {
            if (blockCode instanceof BreakStatement) {
                mv.visitJumpInsn(Opcodes.GOTO, outLabel);
                break; // other code in this block are unnecessary
            } else if (blockCode instanceof ContinueStatement) {
                mv.visitJumpInsn(Opcodes.GOTO, stepLabel);
                break; // other code in this block are unnecessary
            } else if (blockCode instanceof ReturnStatement) {
                blockCode.generateCode(currentClass, currentMethod, cv, mv);
                break; // other code in this block are unnecessary
            } else
                blockCode.generateCode(currentClass, currentMethod, cv, mv);
        }
        Display.pop();

        // create jump of for loop
        mv.visitLabel(stepLabel);
        mv.visitIincInsn(varCounter.getDSCP().getAddress(), 1);
        mv.visitJumpInsn(Opcodes.GOTO, conditionLabel);
        mv.visitLabel(outLabel);
    }
}
