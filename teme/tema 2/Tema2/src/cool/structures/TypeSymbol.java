package cool.structures;

import org.antlr.v4.runtime.Token;
import org.stringtemplate.v4.ST;

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
    public static final TypeSymbol SELF_TYPE = new TypeSymbol("SELF_TYPE", OBJECT);

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

    public boolean isDesc (TypeSymbol a) {
        if (a == null)
            return true;

        while (a != null) {
            if (a == this) {
                return true;
            }
            a = a.parent;
        }

        return false;
    }

    public static void basicClassesSetup() {
        objectSetup();
        ioSetup();
        INT.scope = new DefaultScope(OBJECT.scope, "Int");
        stringSetup();
        BOOL.scope = new DefaultScope(OBJECT.scope, "Bool");
    }

    private static void objectSetup() {
        OBJECT.scope = new DefaultScope(SymbolTable.globals, "Object");

        MethodSymbol abortSym = new MethodSymbol("abort");
        abortSym.type = OBJECT;
        abortSym.parent = OBJECT;
        OBJECT.scope.add(abortSym, "method");

        MethodSymbol type_nameSym = new MethodSymbol("type_name");
        type_nameSym.type = STRING;
        type_nameSym.parent = OBJECT;
        OBJECT.scope.add(type_nameSym, "method");

        MethodSymbol copySym = new MethodSymbol("copy");
        copySym.type = SELF_TYPE;
        copySym.parent = OBJECT;
        OBJECT.scope.add(copySym, "method");
    }

    private static void ioSetup() {
        IO.scope = new DefaultScope(OBJECT.scope, "IO");

        MethodSymbol out_stringSym = new MethodSymbol("out_string");
        IdSymbol formal1 = new IdSymbol("x", STRING);
        formal1.parent = out_stringSym;
        out_stringSym.type = SELF_TYPE;
        out_stringSym.parent = IO;
        out_stringSym.formals_list.add(formal1);
        out_stringSym.formals.put("x", formal1);
        IO.scope.add(out_stringSym, "method");

        MethodSymbol out_intSym = new MethodSymbol("out_int");
        IdSymbol formal2 = new IdSymbol("x", INT);
        formal2.parent = out_intSym;
        out_intSym.type = SELF_TYPE;
        out_intSym.parent = IO;
        out_intSym.formals_list.add(formal2);
        out_intSym.formals.put("x", formal2);
        IO.scope.add(out_intSym, "method");

        MethodSymbol in_stringSym = new MethodSymbol("in_string");
        in_stringSym.type = STRING;
        in_stringSym.parent = IO;
        IO.scope.add(in_stringSym, "method");

        MethodSymbol in_intSym = new MethodSymbol("in_int");
        in_intSym.type = INT;
        in_intSym.parent = IO;
        IO.scope.add(in_intSym, "method");
    }

    private static void stringSetup() {
        STRING.scope = new DefaultScope(OBJECT.scope, "String");

        MethodSymbol lengthSym = new MethodSymbol("length");
        lengthSym.type = INT;
        lengthSym.parent = STRING;
        STRING.scope.add(lengthSym, "method");

        MethodSymbol concatSym = new MethodSymbol("concat");
        IdSymbol formal1 = new IdSymbol("s", STRING);
        formal1.parent = concatSym;
        concatSym.type = STRING;
        concatSym.parent = STRING;
        concatSym.formals_list.add(formal1);
        concatSym.formals.put("s", formal1);
        STRING.scope.add(concatSym, "method");

        MethodSymbol substrSym = new MethodSymbol("substr");
        IdSymbol formal2 = new IdSymbol("i", INT);
        IdSymbol formal3 = new IdSymbol("l", INT);
        formal2.parent = substrSym;
        formal3.parent = substrSym;
        substrSym.type = STRING;
        substrSym.parent = STRING;
        substrSym.formals_list.add(formal2);
        substrSym.formals_list.add(formal3);
        substrSym.formals.put("i", formal2);
        substrSym.formals.put("l", formal3);
        STRING.scope.add(substrSym, "method");
    }
}
