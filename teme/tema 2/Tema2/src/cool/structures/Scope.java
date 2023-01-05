package cool.structures;

public interface Scope {
    public boolean add(Symbol sym, String type);
    
    public Symbol lookup(String str, String type);
    
    public Scope getParent();
}
