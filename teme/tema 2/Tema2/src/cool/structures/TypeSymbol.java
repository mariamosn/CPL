package cool.structures;

import org.antlr.v4.runtime.Token;

import java.util.HashSet;

public class TypeSymbol extends Symbol {
    public TypeSymbol parent;
    public Scope scope = null;
    public TypeSymbol(String name) {
        super(name);
        this.parent = null;
    }

    public TypeSymbol(String name, TypeSymbol parent) {
        super(name);
        if (parent == null) {
            this.parent = TypeSymbol.OBJECT;
        } else {
            this.parent = parent;
        }
    }

    public TypeSymbol(String name, Token parent) {
        super(name);
        if (parent == null) {
            this.parent = TypeSymbol.OBJECT;
            return;
        }

        this.parent = (TypeSymbol) SymbolTable.globals.lookup(parent.getText(), "type");
    }

    // Symboluri aferente tipurilor, definite global
    public static final TypeSymbol OBJECT   = new TypeSymbol("Object");
    public static final TypeSymbol IO = new TypeSymbol("IO", OBJECT);
    public static final TypeSymbol INT   = new TypeSymbol("Int", OBJECT);
    public static final TypeSymbol STRING = new TypeSymbol("String", OBJECT);
    public static final TypeSymbol BOOL  = new TypeSymbol("Bool", OBJECT);

    public static TypeSymbol lub(TypeSymbol a, TypeSymbol b) {
        if (a == null)
            return b;
        if (b == null)
            return a;

        HashSet<TypeSymbol> a_parents = new HashSet<>();
        while (a != null) {
            a_parents.add(a);
            a = a.parent;
        }

        while (b != null) {
            if (a_parents.contains(b)) {
                return b;
            }
            b = b.parent;
        }

        return TypeSymbol.OBJECT;
    }
}
