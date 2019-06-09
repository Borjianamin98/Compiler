import java_cup.runtime.ComplexSymbolFactory;
import org.objectweb.asm.*;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.Declaration;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.declaration.method.StartMethodDCL;
import semantic.syntaxTree.declaration.record.ArrayFieldDCL;
import semantic.syntaxTree.declaration.record.Field;
import semantic.syntaxTree.declaration.record.RecordTypeDCL;
import semantic.syntaxTree.declaration.record.SimpleFieldDCL;
import semantic.syntaxTree.expression.binaryOperation.constValue.IntegerConst;
import semantic.syntaxTree.expression.binaryOperation.constValue.StringConst;
import semantic.syntaxTree.identifier.SimpleField;
import semantic.syntaxTree.identifier.SimpleVariable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.PrintFunction;
import semantic.syntaxTree.statement.ReturnStatement;
import semantic.syntaxTree.statement.assignment.DirectAssignment;
import semantic.syntaxTree.statement.loop.Foreach;
import semantic.syntaxTree.statement.loop.RepeatUntil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main implements Opcodes {

    public static void main(String[] args) throws IOException {
        ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
//        LexicalAnalyzer scanner = new LexicalAnalyzer(new FileReader("test.txt"), symbolFactory);
//        ComplexSymbolFactory.ComplexSymbol symbol;
//        while ((symbol = (ComplexSymbolFactory.ComplexSymbol) scanner.next_token()) != null) {
//            System.out.println(symbol.getName() + " " + Token.getWithSym(symbol.sym) + " " + symbol.value);
//        }
        parseInput(symbolFactory);
    }

    private static void parseInput(ComplexSymbolFactory symbolFactory) throws IOException {
//        Parser parser = new Parser(new LexicalAnalyzer(new FileReader("test.txt"), symbolFactory), symbolFactory);
//        try {
//            parser.parse();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//
//        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, "Tester", null, "java/lang/Object", null);
//        MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
//        mw.visitCode();
//        mw.visitVarInsn(ALOAD, 0);
//        mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
//        mw.visitInsn(RETURN);
//        mw.visitMaxs(0, 0);
//        mw.visitEnd();
//
//
//        mw = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
//        mw.visitCode();
//
//        List<Field> f1 = new ArrayList<>();
//        f1.add(new Field("x", "int", 0, new IntegerConst(1), false, false));
//        f1.add(new Field("y", "double", 0, false, false));
//        RecordTypeDCL r1 = new RecordTypeDCL("A", f1);
//        r1.generateCode(cw, mw);
//
//        List<Field> f2 = new ArrayList<>();
//        f2.add(new Field("x", "A", 2, false, false));
//        f2.add(new Field("y", "int", 1, false, false));
//        RecordTypeDCL r2 = new RecordTypeDCL("B", f2);
//        r2.generateCode(cw, mw);
//
////        VariableDCL varaDCL = new VariableDCL("a", "B", false, false);
////        varaDCL.generateCode(cw, mw);
////        SimpleVariable vara = new SimpleVariable("a");
////        DirectAssignment ass = new DirectAssignment(vara, new NewRecordInstruction("B"));
////        ass.generateCode(cw, mw);
//
////        MemberVariable vara$1 = new MemberVariable(vara, "x");
////        List<Expression> dims = new ArrayList<>();
////        dims.add(new IntegerConst(5));
////        dims.add(new IntegerConst(5));
////        ass = new DirectAssignment(vara$1, new NewArrayInstruction("A", dims));
////        ass.generateCode(cw, mw);
////        ArrayVariable vara$2 = new ArrayVariable(vara$1, new IntegerConst(1));
////        ArrayVariable vara$3 = new ArrayVariable(vara$2, new IntegerConst(2));
////        ass = new DirectAssignment(vara$3, new NewRecordInstruction("A"));
////        ass.generateCode(cw, mw);
////        MemberVariable vara$4 = new MemberVariable(vara$3, "y");
////        DirectAssignment ass2 = new DirectAssignment(vara$4, new DoubleConst(1));
////        ass2.generateCode(cw, mw);
////        PrintFunction printFunction = new PrintFunction(vara$4);
////        printFunction.generateCode(cw, mw);
////
////        List<Expression> d = new ArrayList<>();
////        d.add(new IntegerConst(2));
////        d.add(new IntegerConst(3));
//////        d.add(new IntegerConst(4));
//////        ArrayDCL arrayDCL = new ArrayDCL("x", "B", 2, false, false);
//////        arrayDCL.generateCode(cw, mw);
////        VariableDCL varDCL = new VariableDCL("x", "B", false, false);
////        varDCL.generateCode(cw, mw);
////        Variable var = new SimpleVariable("x");
////        NewRecordInstruction newIns = new NewRecordInstruction("B");
////        DirectAssignment ass = new DirectAssignment(var, newIns);
////        ass.generateCode(cw, mw);
//////        ArrayVariable vary$2 = new ArrayVariable(vary$1, new IntegerConst(2));
//////        PrintFunction printFunction = new PrintFunction(vary$2);
//////        printFunction.generateCode(cw, mw);
////
////
//
////
////        Declaration s = new ArrayFieldDCL("Tester", "ab", "int", 2, false, false, true);
////        s.generateCode(cw, mw);
////
//////        DirectAssignment ass3 = new DirectAssignment(, new IntegerConst(1));
//////        ass3.generateCode(cw, mw);
//////        new PrintFunction(new SimpleField("Tester", "ab", true)).generateCode(cw, mw);
////
////        Block body = new Block();
////        Block body2 = new Block();
////        Block body3 = new Block();
////        Foreach foreach2 = new Foreach("i", new SimpleVariable("t"), body2);
//////        Foreach foreach3 = new Foreach("m", new SimpleVariable("i"), body3);
//////        body2.addStatement(foreach3);
////        body.addStatement(foreach2);
////        Foreach foreach = new Foreach("t", new SimpleField("Tester", "ab", true), body);
////        foreach.generateCode(cw, mw);
//
////
////        List<Argument> args = new ArrayList<>();
////        args.add(new Argument("x", "int", 0));
////        args.add(new Argument("y", "B", 2));
//////        args.add(new Argument("z", "A", 1));
////        Block mBody = new Block();
////        mBody.addStatement(new PrintFunction(new SimpleVariable("x")));
////        mBody.addStatement(new ReturnStatement());
////        MethodDCL m = new MethodDCL("Tester", "test", args, mBody);
////        m.generateCode(cw, mw);
////
////        List<Expression> params = new ArrayList<>();
////        params.add(new IntegerConst(1));
////        params.add(var);
////        MethodCall methodCall = new MethodCall("Tester.test", params);
////        methodCall.generateCode(cw, mw);
//
//        mw.visitInsn(RETURN);
//        mw.visitMaxs(0, 0);
//        mw.visitEnd();
//        cw.visitEnd();
//
//        FileOutputStream fos = new FileOutputStream(Node.outputPath + "Tester.class");
//        fos.write(cw.toByteArray());
//        fos.close();

        List<MethodDCL> methodDCLS = new ArrayList<>();
        Block body = new Block();
        methodDCLS.add(new StartMethodDCL("Tester", body));

        Block repeatBlock = new Block();
        repeatBlock.addStatement(new PrintFunction(new StringConst("salam")));
        RepeatUntil repeatUntil = new RepeatUntil(new IntegerConst(1), repeatBlock);
        body.addStatement(repeatUntil);
        body.addStatement(new ReturnStatement());

        ClassDCL clazz = new ClassDCL("Tester", null, methodDCLS , null);
        clazz.generateCode(null, null);
    }

}