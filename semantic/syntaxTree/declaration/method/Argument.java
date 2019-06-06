package semantic.syntaxTree.declaration.method;

import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.TypeDSCP;

import java.util.Optional;

public class Argument {
    private int arrayLevels;
    private String type;
    private String name;

    public Argument(String name, int arrayLevels, String type) {
        this.name = name;
        this.arrayLevels = arrayLevels;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public TypeDSCP getType() {
        Optional<DSCP> typeDSCP = Display.find(type);
        if (!typeDSCP.isPresent() || !(typeDSCP.get() instanceof TypeDSCP))
            throw new SymbolNotFoundException(type + " is not declared");
        return ((TypeDSCP) typeDSCP.get());
    }

    public String getDescriptor() {
        StringBuilder desc = new StringBuilder();
        for (int i = 0; i < arrayLevels; i++) {
            desc.append('[');
        }
        TypeDSCP typeDSCP = getType();
        if (typeDSCP.isPrimitive()) {
            desc.append(Utility.getPrimitiveTypeDescriptor(typeDSCP.getTypeCode()));
        } else {
            desc.append('L').append(type).append(";");
        }
        return desc.toString();
    }
}
