package cool.structures;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class CaseSymbol extends IdSymbol {
    public LinkedList<Symbol> case_opt = new LinkedList<>();

    public CaseSymbol() {
        super("case");
    }
}
