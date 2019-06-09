import java_cup.runtime.ComplexSymbolFactory;
import org.objectweb.asm.*;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.declaration.method.StartMethodDCL;
import semantic.syntaxTree.expression.binaryOperation.conditional.Less;
import semantic.syntaxTree.expression.constValue.DoubleConst;
import semantic.syntaxTree.expression.constValue.IntegerConst;
import semantic.syntaxTree.expression.identifier.SimpleVariable;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.assignment.DirectAssignment;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;
import semantic.syntaxTree.statement.controlflow.loop.ForLoop;

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
        DirectAssignment assignment = new DirectAssignment(new SimpleVariable("a"), new IntegerConst(1));

        ForLoop forLoop = new ForLoop(null, new Less(new IntegerConst(1), new IntegerConst(2)), null, null);
        body.addBlockCode(forLoop);

        body.addBlockCode(new ReturnStatement());
        ClassDCL clazz = new ClassDCL("Tester", null, methodDCLS, null);
        clazz.generateCode(null, null, null, null);
    }

}