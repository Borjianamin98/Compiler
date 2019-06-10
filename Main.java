import java_cup.runtime.ComplexSymbolFactory;
import org.objectweb.asm.Opcodes;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.VariableDCL;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.declaration.method.StartMethodDCL;
import semantic.syntaxTree.declaration.record.Field;
import semantic.syntaxTree.declaration.record.RecordTypeDCL;
import semantic.syntaxTree.declaration.record.SimpleFieldDCL;
import semantic.syntaxTree.expression.Cast;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.constValue.BooleanConst;
import semantic.syntaxTree.expression.constValue.DoubleConst;
import semantic.syntaxTree.expression.constValue.IntegerConst;
import semantic.syntaxTree.expression.identifier.ArrayVariable;
import semantic.syntaxTree.expression.identifier.MemberVariable;
import semantic.syntaxTree.expression.identifier.SimpleVariable;
import semantic.syntaxTree.expression.instance.NewArrayInstruction;
import semantic.syntaxTree.expression.instance.NewRecordInstruction;
import semantic.syntaxTree.expression.operation.unary.MinusMinusPostfix;
import semantic.syntaxTree.expression.operation.unary.MinusMinusPrefix;
import semantic.syntaxTree.expression.operation.unary.PlusPlusPostfix;
import semantic.syntaxTree.expression.operation.unary.PlusPlusPrefix;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.PrintFunction;
import semantic.syntaxTree.statement.assignment.DirectAssignment;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;
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

//        body.addBlockCode(new VariableDCL("a", TypeTree.STRING_NAME, false, false));
//        body.addBlockCode(new DirectAssignment(new SimpleVariable("a"), new StringConst("ali")));
//////        body.addBlockCode(new VariableDCL("b", "A", false, false));
//        Block body1 = new Block();
//        body1.addBlockCode(new PrintFunction(new IntegerConst(1)));
//        Block body2 = new Block();
//        body2.addBlockCode(new PrintFunction(new IntegerConst(2)));
////        body1.addBlockCode(new PlusAssignment(new SimpleVariable("a"), new IntegerConst(1)));
////        RepeatUntil forLoop = new RepeatUntil(new NotEqual(new SimpleVariable("a"), new IntegerConst(0)), body1);
////        body.addBlockCode(forLoop);
//
//        body.addBlockCode(new IfElseThen(new Not(new Plus(new SimpleVariable("a"), new IntegerConst(1))), body1, body2));

//        List<Argument> args = new ArrayList<>();
//        args.add(new Argument("x", TypeTree.LONG_NAME, 2));
//        Block body1 = new Block();
//        body1.addBlockCode(new ReturnStatement());
//        body.addBlockCode(new MethodDCL("Tester", "test", args, body1 , true));
//
//        List<Argument> args2 = new ArrayList<>();
//        args2.add(new Argument("x", TypeTree.LONG_NAME, 0));
//        Block body2 = new Block();
//        body2.addBlockCode(new ReturnStatement());
//        body.addBlockCode(new MethodDCL("Tester", "test", args2, body2 , true));
//
//        List<Expression> parms = new ArrayList<>();
//        List<Expression> dims = new ArrayList<>();
//        dims.add(new IntegerConst(1));
//        dims.add(new IntegerConst(1));
//        parms.add(new NewArrayInstruction(TypeTree.LONG_NAME, dims));
//        body.addBlockCode(new MethodCall("test", parms));

//        List<Field> fields = new ArrayList<>();
//        List<Expression> dim = new ArrayList<>();
//        dim.add(new IntegerConst(10));
//        fields.add(new Field("x", TypeTree.INTEGER_NAME, 1, new NewArrayInstruction(TypeTree.INTEGER_NAME, dim), false, false));
//        body.addBlockCode(new RecordTypeDCL("A", fields));
//        body.addBlockCode(new VariableDCL("a", "A", false, false));
////        body.addBlockCode(new ScannerFunction(TypeTree.INTEGER_DSCP));
//        body.addBlockCode(new DirectAssignment(new SimpleVariable("a"), new NewRecordInstruction("A")));
////        body.addBlockCode(new DirectAssignment(new MemberVariable(new SimpleVariable("a"), "x"), new IntegerConst(1)));
//        body.addBlockCode(new PrintFunction(new PlusPlusPrefix(
//                new ArrayVariable(new MemberVariable(new SimpleVariable("a"), "x"), new IntegerConst(1))
//        )));
//
//        body.addBlockCode(new SimpleFieldDCL("Tester", "name", TypeTree.STRING_NAME, false, false, true));

        body.addBlockCode(new VariableDCL("a", TypeTree.LONG_NAME, false, false));
        body.addBlockCode(new DirectAssignment(new SimpleVariable("a"), new Cast(TypeTree.LONG_NAME ,new IntegerConst(2))));

        body.addBlockCode(new ReturnStatement());
        ClassDCL clazz = new ClassDCL("Tester", null, methodDCLS, null);
        clazz.generateCode(null, null, null, null);
    }

}