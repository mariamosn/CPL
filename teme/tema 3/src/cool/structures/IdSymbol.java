package cool.structures;

public class IdSymbol extends Symbol {
    public TypeSymbol type;
    public Symbol parent;
    public Integer offset;
    public Boolean isAttr = false;

    public IdSymbol(String name) {
        super(name);
        this.type = null;
    }

    public IdSymbol(String name, TypeSymbol type) {
        super(name);
        this.type = type;
    }
}
