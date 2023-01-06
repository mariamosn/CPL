package cool.structures;

import java.util.*;

public class DefaultScope implements Scope {
    
    public Map<String, Symbol> types = new LinkedHashMap<>();
    public Map<String, Symbol> methods = new LinkedHashMap<>();
    // public Map<String, Symbol> attributes = new LinkedHashMap<>();
    public Map<String, Symbol> vars = new LinkedHashMap<>();
    
    private Scope parent;
    public String name;
    
    public DefaultScope(Scope parent) {
        this.parent = parent;
        this.name = "";
    }

    public DefaultScope(Scope parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    @Override
    public boolean add(Symbol sym, String type) {
        Map<String, Symbol> aux = new LinkedHashMap<>();
        if (type.equals("type")) {
            aux = types;
        } else if (type.equals("attr")) {
            //aux = attributes;
        } else if (type.equals("method")) {
            aux = methods;
        } else if (type.equals("var")) {
            aux = vars;
        }

        // Reject duplicates in the same scope.
        if (aux.containsKey(sym.getName()))
            return false;

        aux.put(sym.getName(), sym);
        
        return true;
    }

    @Override
    public Symbol lookup(String name, String type) {
        Map<String, Symbol> aux = new LinkedHashMap<>();
        if (type.equals("type")) {
            aux = types;
        } else if (type.equals("attr")) {
            //aux = attributes;
        } else if (type.equals("method")) {
            aux = methods;
        } else if (type.equals("var")) {
            aux = vars;
        }

        var sym = aux.get(name);
        
        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookup(name, type);
        
        return null;
    }

    @Override
    public Scope getParent() {
        return parent;
    }
    
    @Override
    public String toString() {
        LinkedList<Symbol> t = new LinkedList<>(types.values());
        LinkedList<Symbol> m = new LinkedList<>(methods.values());
        LinkedList<Symbol> v = new LinkedList<>(vars.values());

        Scope crt = parent;
        while (crt != null) {
            t.addAll(((DefaultScope) crt).types.values());
            m.addAll(((DefaultScope) crt).methods.values());
            v.addAll(((DefaultScope) crt).vars.values());
            crt = ((DefaultScope) crt).parent;
        }

        return "types:" + t +
                "methods:" + m +
                "var:" + v;
        /*
        return "types:" + types.values().toString() +
                //"attr:" + attributes.values().toString() +
                "methods:" + methods.values().toString() +
                "var:" + vars.values().toString();

         */
    }

}
