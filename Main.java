import java_cup.runtime.ComplexSymbolFactory;
import org.objectweb.asm.*;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.declaration.record.Field;
import semantic.syntaxTree.declaration.record.RecordTypeDCL;
import semantic.syntaxTree.expression.binaryOperation.constValue.IntegerConst;
import semantic.syntaxTree.identifier.Variable;

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
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, "Tester", null, "java/lang/Object", null);
        MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mw.visitCode();
        mw.visitVarInsn(ALOAD, 0);
        mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mw.visitInsn(RETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();

//        List<Argument> args = new ArrayList<>();
//        args.add(new Argument("t", 0, "int"));
//        args.add(new Argument("i", 0, "int"));
//        Block block = new Block();
//        block.addStatement(new Statement() {
//            @Override
//            public void generateCode(ClassVisitor cv, MethodVisitor mv) {
//                Variable vart = new Variable("t");
//                Variable vari = new Variable("i");
//                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                vart.generateCode(cv, mv);
//                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
//                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                vari.generateCode(cv, mv);
//                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
//                vart.generateCode(cv, mv);
//            }
//        });
//        MethodDCL func1 = new MethodDCL("Tester", "testFunc", Constants.INTEGER_DSCP, args, block);
//        func1.generateCode(cw, mw);
//
//        mw = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
//        mw.visitCode();

//        Parser.list.get(Parser.list.size() - 1).generateCode(cw, mw);
//        Variable varx = new Variable("x", new DSCP(Constants.INTEGER_CODE, Constants.INTEGER_SIZE, 2));
//        Variable vart = new Variable("t", new DSCP(Constants.INTEGER_CODE, Constants.INTEGER_SIZE, 3));
//        Multiply mult1 = new Multiply(new IntegerConst(5), vart);
//        Multiply mult2 = new Multiply(new IntegerConst(3), mult1);
//        Multiply mult3 = new Multiply(varx, new IntegerConst(2));
//        Plus plus1 = new Plus(mult3, mult2);
//        Assignment ass1 = new MultiplyAssignment(vary, plus1);
//        ass1.generateCode(cw, mw);
//        Assignment forAssign = new DirectAssignment(vari, new IntegerConst(1));
//        VariableDCL variDCL = new VariableDCL("i", "int", false, new IntegerConst(1));
//        variDCL.generateCode(cw, mw);
//        Variable vari = new Variable("i");
//        Assignment ass1 = new DirectAssignment(vari, new IntegerConst(1));
//        Less less1 = new Less(vari, new IntegerConst(11));
//        Assignment ass2 = new PlusAssignment(vari, new IntegerConst(1));
//        Block block = new Block();
//        Statement st = new Statement() {
//            @Override
//            public void generateCode(ClassVisitor cv, MethodVisitor mv) {
//                Variable vari = new Variable("i");
//                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                vari.generateCode(cv, mv);
//                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
//            }
//        };
//        block.addStatement(st);
//        ForLoop forLoop = new ForLoop(ass1, less1, ass2, block);
//        forLoop.generateCode(cw, mw);
//        VariableDCL varjDCL = new VariableDCL("j", "int", false, new IntegerConst(1));
//        varjDCL.generateCode(cw, mw);
//        vari = new Variable("i");
//        mw.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        vari.generateCode(cw, mw);
//        mw.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);

//        List<Expression> parameters = new ArrayList<>();
//        parameters.add(new IntegerConst(5));
//        parameters.add(new IntegerConst(10));
//        MethodCall methodCall = new MethodCall(func1.getName(), parameters);
//
//        List<Expression> parameters2 = new ArrayList<>();
//        parameters2.add(methodCall);
//        parameters2.add(new IntegerConst(20));
//        MethodCall methodCall2 = new MethodCall(func1.getName(), parameters2);
//        methodCall2.generateCode(cw, mw);

        List<Field> fields = new ArrayList<>();
        fields.add(new Field("x", 0, "int", new IntegerConst(1)));
        fields.add(new Field("y", 0, "int"));
        fields.add(new Field("z", 0, "int"));
        fields.add(new Field("t", 0, "string"));
        RecordTypeDCL recordTypeDCL = new RecordTypeDCL("A", fields);
        recordTypeDCL.generateCode(cw, mw);

        VariableDCL vara = new VariableDCL("a", "A", false);
        vara.generateCode(cw, mw);

        

        mw.visitInsn(RETURN);
        mw.visitMaxs(0, 0);
        mw.visitEnd();
        cw.visitEnd();

        FileOutputStream fos = new FileOutputStream(Node.outputPath + "Tester.class");
        fos.write(cw.toByteArray());
        fos.close();
    }

}