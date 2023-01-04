package cool.structures;

import org.antlr.v4.runtime.Token;

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

        this.parent = (TypeSymbol) SymbolTable.globals.lookup(parent.getText());
    }

    // Symboluri aferente tipurilor, definite global
    public static final TypeSymbol OBJECT   = new TypeSymbol("Object");
    public static final TypeSymbol IO = new TypeSymbol("IO", OBJECT);
    public static final TypeSymbol INT   = new TypeSymbol("Int", OBJECT);
    public static final TypeSymbol STRING = new TypeSymbol("String", OBJECT);
    public static final TypeSymbol BOOL  = new TypeSymbol("Bool", OBJECT);
}
