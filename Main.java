import java_cup.runtime.ComplexSymbolFactory;
import lexical.LexicalAnalyzer;
import org.objectweb.asm.Opcodes;
import semantic.symbolTable.Display;
import semantic.syntaxTree.Node;
import semantic.syntaxTree.program.ClassDCL;
import semantic.symbolTable.typeTree.TypeTree;
import syntax.Parser;

import java.io.FileReader;
import java.io.IOException;

public class Main implements Opcodes {

    public static void main(String[] args) throws IOException {
        ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
//        LexicalAnalyzer scanner = new LexicalAnalyzer(new FileReader("test.txt"), symbolFactory);
//        ComplexSymbolFactory.ComplexSymbol symbol;
//        while ((symbol = (ComplexSymbolFactory.ComplexSymbol) scanner.next_token()) != null) {
//            System.out.println(symbol.getName() + " " + Token.getWithSym(symbol.sym) + " " + symbol.value);
//        }

        // order of initialization is important
        Node.init();
        Display.init();
        TypeTree.init();
        parseInput(symbolFactory);
    }

    private static void parseInput(ComplexSymbolFactory symbolFactory) throws IOException {
        Parser parser = new Parser(new LexicalAnalyzer(new FileReader("test.txt"), symbolFactory), symbolFactory);
        try {
            parser.parse();

            ClassDCL clazz = Parser.finalResult;
            clazz.generateCode(null, null, null, null, null, null);
            System.out.println("Code compiled successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}