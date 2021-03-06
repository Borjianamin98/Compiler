package semantic.syntaxTree.statement.controlflow.loop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.descriptor.type.ArrayTypeDSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.ArrayDCL;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.constValue.IntegerConst;
import semantic.syntaxTree.expression.identifier.ArrayVariable;
import semantic.syntaxTree.expression.identifier.SimpleLocalVariable;
import semantic.syntaxTree.expression.identifier.Variable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.Statement;
import semantic.syntaxTree.statement.assignment.DirectAssignment;
import semantic.syntaxTree.statement.controlflow.BreakStatement;
import semantic.syntaxTree.statement.controlflow.ContinueStatement;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;
import semantic.symbolTable.typeTree.TypeTree;

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
    public void generateCode(ClassDCL currentClass, MethodDCL currentMethod, ClassVisitor cv, MethodVisitor mv, Label breakLabel, Label continueLabel) {
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


        String varIteratorName = top.getTempName();
        String varCounterName = top.getTempName();
        SimpleLocalVariable varIterator = new SimpleLocalVariable(varIteratorName);
        SimpleLocalVariable varCounter = new SimpleLocalVariable(varCounterName);
        SimpleLocalVariable varIdentifier = new SimpleLocalVariable(identifierName);
        Label outLabel = new Label();
        Label conditionLabel = new Label();
        Label stepLabel = new Label();

        // we do this here because variable used as identifier in foreach
        // can shadow other variables in outside block
        Display.add(true);

        if (iterator.getDSCP().getType() instanceof ArrayTypeDSCP) {
            ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) iterator.getDSCP().getType();

            // create T[] a = Expression;
            ArrayDCL arrayDCL = new ArrayDCL(varIteratorName, arrayTypeDSCP.getBaseType().getName(),
                    arrayTypeDSCP.getDimensions(), false, iterator.getDSCP().isInitialized());
            arrayDCL.generateCode(currentClass, currentMethod, cv, mv, null, null);
            DirectAssignment iteratorAssignment = new DirectAssignment(varIterator, iterator);
            iteratorAssignment.generateCode(currentClass, currentMethod, cv, mv, null, null);
        } else if (TypeTree.isString(iterator.getDSCP().getType())) {

            // create String a = Expression;
            VariableDCL varDCL = new VariableDCL(varIteratorName, TypeTree.STRING_NAME, false, iterator.getDSCP().isInitialized());
            varDCL.generateCode(currentClass, currentMethod, cv, mv, null, null);
            DirectAssignment iteratorAssignment = new DirectAssignment(varIterator, iterator);
            iteratorAssignment.generateCode(currentClass, currentMethod, cv, mv, null, null);
        } else {
            throw new RuntimeException(iterator.getDSCP().getName() + " is not iterable");
        }

        // create int i = 0;
        VariableDCL varCounterDCL = new VariableDCL(varCounterName, TypeTree.INTEGER_NAME, false, false);
        varCounterDCL.generateCode(currentClass, currentMethod, cv, mv, null, null);
        DirectAssignment counterAssignment = new DirectAssignment(varCounter, new IntegerConst(0));
        counterAssignment.generateCode(currentClass, currentMethod, cv, mv, null, null);

        // create (i < a.length) condition
        mv.visitLabel(conditionLabel);
        varCounter.generateCode(currentClass, currentMethod, cv, mv, null, null);
        varIterator.generateCode(currentClass, currentMethod, cv, mv, null, null);
        if (iterator.getDSCP().getType() instanceof ArrayTypeDSCP)
            mv.visitInsn(Opcodes.ARRAYLENGTH);
        else if (TypeTree.isString(iterator.getDSCP().getType()))
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "length", "()I", false);
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, outLabel);

        if (iterator.getDSCP().getType() instanceof ArrayTypeDSCP) {
            ArrayTypeDSCP arrayTypeDSCP = (ArrayTypeDSCP) iterator.getDSCP().getType();

            // create Type Identifier;
            Declaration varIdentifierDCL;
            if (arrayTypeDSCP.getInternalType() instanceof ArrayTypeDSCP)
                varIdentifierDCL = new ArrayDCL(identifierName, arrayTypeDSCP.getBaseType().getName(),
                        arrayTypeDSCP.getInternalType().getDimensions(), false, false);
            else
                varIdentifierDCL = new VariableDCL(identifierName, arrayTypeDSCP.getInternalType().getName(), false, false);
            varIdentifierDCL.generateCode(currentClass, currentMethod, cv, mv, null, null);

            // create Identifier = a[i];
            ArrayVariable varId = new ArrayVariable(varIterator, varCounter);
            DirectAssignment identifierAssignment = new DirectAssignment(varIdentifier, varId);
            identifierAssignment.generateCode(currentClass, currentMethod, cv, mv, null, null);
        } else if (TypeTree.isString(iterator.getDSCP().getType())) {

            // create Type Identifier;
            Declaration varIdentifierDCL = new VariableDCL(identifierName, TypeTree.CHAR_NAME, false, false);
            varIdentifierDCL.generateCode(currentClass, currentMethod, cv, mv, null, null);

            // create Identifier = a.charAt(i);
            varIterator.generateCode(currentClass, currentMethod, cv, mv, null, null);
            varCounter.generateCode(currentClass, currentMethod, cv, mv, null, null);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "charAt", "(I)C", false);
            mv.visitVarInsn(Opcodes.ISTORE, varIdentifier.getDSCP().getAddress());
            varIdentifier.getDSCP().setInitialized(true);
        }

        // create body
        for (BlockCode blockCode : body.getBlockCodes()) {
            blockCode.generateCode(currentClass, currentMethod, cv, mv, outLabel, stepLabel);
            if (blockCode instanceof ReturnStatement ||
                    blockCode instanceof BreakStatement ||
                    blockCode instanceof ContinueStatement)
                break; // other code in this block are unnecessary
        }
        Display.pop();

        // create jump of for loop
        mv.visitLabel(stepLabel);
        mv.visitIincInsn(varCounter.getDSCP().getAddress(), 1);
        mv.visitJumpInsn(Opcodes.GOTO, conditionLabel);
        mv.visitLabel(outLabel);
    }

    public String getCodeRepresentation() {
        return "foreach (" + identifierName + " in " + iterator.getCodeRepresentation() + ") begin ... end";
    }
}
