package semantic.syntaxTree.declaration.record;

import semantic.exception.SymbolNotFoundException;
import semantic.symbolTable.Display;
import semantic.symbolTable.Utility;
import semantic.symbolTable.descriptor.DSCP;
import semantic.symbolTable.descriptor.TypeDSCP;

import java.util.Optional;

public class Field {
    private int arrayLevels;
    private String type;
    private String name;

    public Field(String name, int arrayLevels, String type) {
        this.name = name;
        this.arrayLevels = arrayLevels;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public TypeDSCP getFieldType() {
        Optional<DSCP> typeDSCP = Display.find(type);
        if (!typeDSCP.isPresent() || !(typeDSCP.get() instanceof TypeDSCP))
            throw new SymbolNotFoundException(type + " is not declared");
        return ((TypeDSCP) typeDSCP.get());
    }

    public String getDescriptor() {
//        StringBuilder desc = new StringBuilder();
//        for (int i = 0; i < arrayLevels; i++) {
//            desc.append('[');
//        }
//        TypeDSCP typeDSCP = getFieldType();
//        if (typeDSCP.isPrimitive()) {
//            desc.append(Utility.getPrimitiveTypeDescriptor(typeDSCP.getType()));
//        } else {
//            desc.append('L').append(type);
//        }
//        return desc.toString();
        return null;
    }
}
