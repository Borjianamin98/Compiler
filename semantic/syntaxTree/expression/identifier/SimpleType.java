package semantic.syntaxTree.expression.identifier;

import semantic.symbolTable.Display;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.type.TypeDSCP;

import java.util.Optional;

/**
 * represent a type like primitive types (int, float, ...) or user defined type
 */
public class SimpleType {
    private String name;
    private TypeDSCP typeDSCP;

    public SimpleType(String name) {
        this.name = name;
    }

    public TypeDSCP getTypeDSCP() {
        if (typeDSCP == null) {
            Optional<DSCP> fetchedDSCP = Display.find(getName());
            if (!fetchedDSCP.isPresent())
                throw new RuntimeException("Type " + name + " is not declared");
            if (fetchedDSCP.get() instanceof TypeDSCP) {
                typeDSCP = (TypeDSCP) fetchedDSCP.get();
            } else
                throw new RuntimeException(name + " is not a type");
        }
        return typeDSCP;
    }

    public String getName() {
        return name;
    }
}
