package cool.structures;

import java.util.LinkedList;

public class DispSymbol extends Symbol {
    public TypeSymbol type;
    public Symbol parent;
    public Symbol method;
    public LinkedList<TypeSymbol> param_types = new LinkedList<>();

    public DispSymbol(String name) {
        super(name);
        this.type = null;
    }

    public DispSymbol(String name, TypeSymbol type) {
        super(name);
        this.type = type;
    }
}
