import java_cup.runtime.ComplexSymbolFactory;
import org.objectweb.asm.Opcodes;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.declaration.method.StartMethodDCL;
import semantic.syntaxTree.expression.binaryoperation.relational.NotEqual;
import semantic.syntaxTree.expression.constValue.IntegerConst;
import semantic.syntaxTree.expression.identifier.SimpleVariable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.PrintFunction;
import semantic.syntaxTree.statement.assignment.DirectAssignment;
import semantic.syntaxTree.statement.assignment.PlusAssignment;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;
import semantic.syntaxTree.statement.controlflow.loop.RepeatUntil;
import semantic.typeTree.TypeTree;

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


        List<MethodDCL> methodDCLS = new ArrayList<>();
        Block body = new Block();
        methodDCLS.add(new StartMethodDCL("Tester", body));

//        Block repeatBlock = new Block();
//        repeatBlock.addBlockCode(new VariableDCL("x", "int", false, false));
//        repeatBlock.addBlockCode(new DirectAssignment(new SimpleVariable("x"), new IntegerConst(1)));
//        repeatBlock.addBlockCode(new PrintFunction(new StringConst("salam")));
//        repeatBlock.addBlockCode(new PrintFunction(new SimpleVariable("x")));
//        RepeatUntil repeatUntil = new RepeatUntil(new IntegerConst(1), repeatBlock);
//        body.addBlockCode(repeatUntil);

//        Multiply m = new Multiply(new IntegerConst(6), new IntegerConst(3));
//        Plus p = new Plus(new FloatConst(2.5), m);
//        Multiply m2 = new Multiply(new IntegerConst(3), p);
//        Plus p2 = new Plus(m2, new IntegerConst(2));
//        VariableDCL vara = new VariableDCL("a", "double", false, false);
//        body.addBlockCode(vara);
//        body.addBlockCode(new VariableDCL("a", "long", false, false));
//        body.addBlockCode(new VariableDCL("b", "A", false, false));
//        DirectAssignment assignment = new DirectAssignment(new SimpleVariable("a"), new BitwiseAnd(new IntegerConst(1), new LongConst(2)));
//        body.addBlockCode(assignment);

//        Block bodyM1 = new Block();
//        bodyM1.addBlockCode(new ReturnStatement());
//        List<Argument> argsM1 = new ArrayList<>();
//        argsM1.add(new Argument("x", TypeTree.FLOAT_NAME, 0));
//        argsM1.add(new Argument("y", TypeTree.INTEGER_NAME, 0));
//        body.addBlockCode(new MethodDCL("Tester", "test", argsM1, bodyM1, true));
//
//        Block bodyM2 = new Block();
//        bodyM2.addBlockCode(new ReturnStatement());
//        List<Argument> argsM2 = new ArrayList<>();
//        argsM2.add(new Argument("x", TypeTree.LONG_NAME, 0));
//        argsM2.add(new Argument("y", TypeTree.LONG_NAME, 0));
//        body.addBlockCode(new MethodDCL("Tester", "test", argsM2, bodyM2, true));
//
//        Block bodyM3 = new Block();
//        bodyM3.addBlockCode(new ReturnStatement());
//        List<Argument> argsM3 = new ArrayList<>();
//        argsM3.add(new Argument("x", TypeTree.INTEGER_NAME, 0));
//        argsM3.add(new Argument("y", TypeTree.INTEGER_NAME, 0));
////        body.addBlockCode(new MethodDCL("Tester", "test", argsM3, bodyM3, true));
////        ForLoop forLoop = new ForLoop(null, new And(new FloatConst(1.5f), new StringConst("b")), null, null);
////        ForLoop forLoop = new ForLoop(null, new NotEqual(new StringConst("a"), new StringConst("b")), null, null);
////        body.addBlockCode(forLoop);
//        List<Expression> parms = new ArrayList<>();
//        parms.add(new IntegerConst(1));
//        parms.add(new IntegerConst(1));
//        body.addBlockCode(new MethodCall("test", parms));

        body.addBlockCode(new VariableDCL("a", TypeTree.LONG_NAME, false, false));
        body.addBlockCode(new DirectAssignment(new SimpleVariable("a"), new IntegerConst(-10)));
//        body.addBlockCode(new VariableDCL("b", "A", false, false));
        Block body1 = new Block();
        body1.addBlockCode(new PrintFunction(new SimpleVariable("a")));
        body1.addBlockCode(new PlusAssignment(new SimpleVariable("a"), new IntegerConst(1)));
        RepeatUntil forLoop = new RepeatUntil(new NotEqual(new SimpleVariable("a"), new IntegerConst(0)), body1);
        body.addBlockCode(forLoop);

        body.addBlockCode(new ReturnStatement());
        ClassDCL clazz = new ClassDCL("Tester", null, methodDCLS, null);
        clazz.generateCode(null, null, null, null);
    }

}