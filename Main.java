import java_cup.runtime.ComplexSymbolFactory;
import lexical.LexicalAnalyzer;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.syntaxTree.BlockCode;
import semantic.syntaxTree.block.Block;
import semantic.syntaxTree.declaration.AutoVariableDCL;
import semantic.syntaxTree.declaration.method.Argument;
import semantic.syntaxTree.declaration.method.MethodDCL;
import semantic.syntaxTree.declaration.method.StartMethodDCL;
import semantic.syntaxTree.declaration.record.Field;
import semantic.syntaxTree.expression.Expression;
import semantic.syntaxTree.expression.call.MethodCall;
import semantic.syntaxTree.expression.identifier.SimpleVariable;
import semantic.syntaxTree.expression.operation.relational.*;
import semantic.syntaxTree.program.ClassDCL;
import semantic.syntaxTree.statement.controlflow.ReturnStatement;
import semantic.typeTree.TypeTree;
import syntax.Parser;

import java.io.FileReader;
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

        // order of initialization is important
        Display.init();
        TypeTree.init();
        parseInput(symbolFactory);
    }

    private static void parseInput(ComplexSymbolFactory symbolFactory) throws IOException {
        Parser parser = new Parser(new LexicalAnalyzer(new FileReader("test.txt"), symbolFactory), symbolFactory);
        try {
            parser.debug_parse();

            List<MethodDCL> methodDCLS = new ArrayList<>();
            Block body = (Block) Parser.finalR;
            methodDCLS.add(new StartMethodDCL("Tester", body));

//        body.addBlockCode((BlockCode) Parser.result);

            body.addBlockCode(new ReturnStatement());
//            List<Field> fields = new ArrayList<>();
//        fields.add(new Field())
            ClassDCL clazz = new ClassDCL("Tester", null, methodDCLS, null);
            clazz.generateCode(null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}