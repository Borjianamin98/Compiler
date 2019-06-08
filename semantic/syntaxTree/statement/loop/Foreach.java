package semantic.syntaxTree.statement.loop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.hastype.VariableDSCP;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.ArrayDCL;
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.expression.binaryOperation.constValue.IntegerConst;
import semantic.syntaxTree.identifier.ArrayVariable;
import semantic.syntaxTree.identifier.SimpleVariable;
import semantic.syntaxTree.statement.Statement;

public class Foreach extends Statement {
    private String identifierName;
    private SimpleVariable iterator;
    private Block body;

    public Foreach(String identifierName, SimpleVariable iterator, Block body) {
        this.identifierName = identifierName;
        this.iterator = iterator;
        this.body = body;
    }

    @Override
    public void generateCode(ClassVisitor cv, MethodVisitor mv) {
//        SymbolTable top = Display.top();
//
//        if (!(iterator.getDSCP().getType() instanceof ArrayTypeDSCP))
//            throw new RuntimeException(iterator.getDSCP().getName() + " is not iterable");
//        ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) iterator.getDSCP().getType();
//        String varIteratorName = top.getTemp(arrayTypeDSCP);
//        SimpleVariable varIterator = new SimpleVariable(varIteratorName);
//        ArrayDCL arrayDCL = new ArrayDCL(varIteratorName, arrayTypeDSCP.getBaseType().getName(), false, arrayTypeDSCP.getDimensions(), iterator);
//        arrayDCL.generateCode(cv, mv);
//
//        Label outLabel = new Label();
//        Label conditionLabel = new Label();
//        VariableDCL varCounterDCL = new VariableDCL("temp$i", "int", false, new IntegerConst(0));
//        varCounterDCL.generateCode(cv, mv);
//        // TODO only support array which final type of them for foreach is primitive or record
//        VariableDCL varNamedDCL = new VariableDCL(identifierName, arrayTypeDSCP.getInternalType().getName(), false);
//        varNamedDCL.generateCode(cv, mv);
//        SimpleVariable varCounter = new SimpleVariable("temp$i");
//        SimpleVariable varNamed = new SimpleVariable(identifierName);
//        mv.visitLabel(conditionLabel);
//        varCounter.generateCode(cv, mv);
//        varIterator.generateCode(cv, mv);
//        mv.visitInsn(Opcodes.ARRAYLENGTH);
//        mv.visitJumpInsn(Opcodes.IF_ICMPGE, outLabel);
//        new ArrayVariable(varIterator, varCounter).generateCode(cv, mv);
//        mv.visitVarInsn(Utility.getOpcode(arrayTypeDSCP.getInternalType().getTypeCode(), "STORE"),
//                ((VariableDSCP) varNamed.getDSCP()).getAddress());
//        varNamed.getDSCP().setInitialized(true);
//
//        Display.add(true);
//        body.generateCode(cv, mv);
//        Display.pop();
//
//        mv.visitIincInsn(((VariableDSCP) varCounter.getDSCP()).getAddress(), 1);
//        mv.visitJumpInsn(Opcodes.GOTO, conditionLabel);
//        mv.visitLabel(outLabel);
    }
}
