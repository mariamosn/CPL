package cool.structures;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class MethodSymbol extends IdSymbol {
    public Map<String, Symbol> formals = new LinkedHashMap<>();
    public LinkedList<Symbol> formals_list = new LinkedList<>();

    public MethodSymbol(String name) {
        super(name);
    }
}
