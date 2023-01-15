package cool.structures;

import java.io.File;

import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;

public class SymbolTable {
    public static Scope globals;
    
    private static boolean semanticErrors;
    
    public static void defineBasicClasses() {
        globals = new DefaultScope(null);
        semanticErrors = false;
        
        // Populate global scope.
        TypeSymbol.basicClassesSetup();
        addBasicClasses();
    }

    private static void addBasicClasses() {
        IdSymbol sym;

        // abort
        MethodSymbol abort = new MethodSymbol("abort");
        abort.parent = TypeSymbol.OBJECT;
        abort.type = TypeSymbol.OBJECT;
        TypeSymbol.OBJECT.meths.put("abort", abort);
        // type_name
        MethodSymbol type_name = new MethodSymbol("type_name");
        type_name.parent = TypeSymbol.OBJECT;
        type_name.type = TypeSymbol.STRING;
        TypeSymbol.OBJECT.meths.put("type_name", type_name);
        // copy
        MethodSymbol copy = new MethodSymbol("copy");
        copy.parent = TypeSymbol.OBJECT;
        copy.type = TypeSymbol.SELF_TYPE;
        TypeSymbol.OBJECT.meths.put("copy", copy);
        globals.add(TypeSymbol.OBJECT, "type");

        // out_string
        MethodSymbol out_string = new MethodSymbol("out_string");
        out_string.parent = TypeSymbol.IO;
        out_string.type = TypeSymbol.SELF_TYPE;
        sym = new IdSymbol("x", TypeSymbol.STRING);
        out_string.formals.put("x", sym);
        out_string.formals_list.add(sym);
        TypeSymbol.IO.meths.put("out_string", out_string);
        // out_int
        MethodSymbol out_int = new MethodSymbol("out_int");
        out_int.parent = TypeSymbol.IO;
        sym = new IdSymbol("x", TypeSymbol.INT);
        out_int.formals.put("x", sym);
        out_int.formals_list.add(sym);
        out_int.type = TypeSymbol.SELF_TYPE;
        TypeSymbol.IO.meths.put("out_int", out_int);
        // in_string
        MethodSymbol in_string = new MethodSymbol("in_string");
        in_string.parent = TypeSymbol.IO;
        in_string.type = TypeSymbol.STRING;
        TypeSymbol.IO.meths.put("in_string", in_string);
        // in_string
        MethodSymbol in_int = new MethodSymbol("in_int");
        in_int.parent = TypeSymbol.IO;
        in_int.type = TypeSymbol.INT;
        TypeSymbol.IO.meths.put("in_int", in_int);
        globals.add(TypeSymbol.IO, "type");

        globals.add(TypeSymbol.INT, "type");

        // length
        MethodSymbol length = new MethodSymbol("length");
        length.parent = TypeSymbol.STRING;
        length.type = TypeSymbol.INT;
        TypeSymbol.STRING.meths.put("length", length);
        // concat
        MethodSymbol concat = new MethodSymbol("concat");
        concat.parent = TypeSymbol.STRING;
        sym = new IdSymbol("s", TypeSymbol.STRING);
        concat.formals.put("s", sym);
        concat.formals_list.add(sym);
        concat.type = TypeSymbol.STRING;
        TypeSymbol.STRING.meths.put("concat", concat);
        // substr
        MethodSymbol substr = new MethodSymbol("substr");
        substr.parent = TypeSymbol.STRING;
        sym = new IdSymbol("i", TypeSymbol.INT);
        substr.formals.put("i", sym);
        substr.formals_list.add(sym);
        sym = new IdSymbol("l", TypeSymbol.INT);
        substr.formals.put("l", sym);
        substr.formals_list.add(sym);
        substr.type = TypeSymbol.STRING;
        TypeSymbol.STRING.meths.put("substr", substr);
        globals.add(TypeSymbol.STRING, "type");

        globals.add(TypeSymbol.BOOL, "type");

        globals.add(TypeSymbol.SELF_TYPE, "type");
    }
    
    /**
     * Displays a semantic error message.
     * 
     * @param ctx Used to determine the enclosing class context of this error,
     *            which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        while (!(ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();
        
        String message = "\"" + new File(Compiler.fileNames.get(ctx)).getName()
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static void error(String str) {
        String message = "Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}
