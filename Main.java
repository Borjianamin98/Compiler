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
        // order of initialization is important
        Node.init();
        Display.init();
        TypeTree.init();
        parseInput();
    }

    private static void parseInput() throws IOException {
        ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
        Parser parser = new Parser(new LexicalAnalyzer(new FileReader("test.txt"), symbolFactory), symbolFactory);
        try {
            parser.parse();

            ClassDCL clazz = Parser.finalResult;
            clazz.generateCode(null, null, null, null, null, null);
            System.out.println("Code compiled successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}