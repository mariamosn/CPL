package cool.parser;

import cool.structures.*;

public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {

    @Override
    public TypeSymbol visit(Program program) {
        for (var c : program.stmts) {
            c.accept(this);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Class c) {
        if (c.scope == null || c.scope.lookup(c.name.getText(), "type") == null)
            return null;

        if (c.parent != null && c.scope.lookup(c.parent.getText(), "type") == null) {
            SymbolTable.error(c.context, c.parent,
                    "Class " + c.name.getText() + " has undefined parent " + c.parent.getText());
            return null;
        } else if (c.parent != null) {
            ((TypeSymbol) c.symbol).parent = (TypeSymbol) c.scope.lookup(c.parent.getText(), "type");
        }
        ((TypeSymbol) c.symbol).scope = c.scope;

        for (Feature f : c.features) {
            if (f.symbol != null)
                ((IdSymbol) f.symbol).parent = c.symbol;
            f.accept(this);
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Attribute attribute) {
        if (attribute.flag != 0 &&
                (attribute.scope.lookup(attribute.type.getText(), "type") == null ||
                !(attribute.scope.lookup(attribute.type.getText(), "type") instanceof TypeSymbol))
            ){
            SymbolTable.error(attribute.context, attribute.type,
                    "Class " + ((DefaultScope) attribute.scope).name + " has attribute " +
                            attribute.name.token.getText() + " with undefined type " + attribute.type.getText());
            return null;
        }
        if (attribute.symbol != null)
            ((IdSymbol) attribute.symbol).type = (TypeSymbol) attribute.scope.lookup(attribute.type.getText(),
                    "type");

        if (attribute.value != null) {
            attribute.value.accept(this);
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Method method) {
        if (method.symbol == null)
            return null;
        if (method.scope != null &&
                method.scope.lookup(method.type.getText(), "type") == null) {
            SymbolTable.error(method.context, method.token,
                    "Class " + ((DefaultScope) method.scope.getParent()).name + " has method " +
                    method.name.token.getText() + " with undefined return type " +
                    method.type.getText());
            return null;
        } else if (method.scope != null) {
            ((MethodSymbol) method.symbol).type = (TypeSymbol) method.scope.lookup(method.type.getText(), "type");
        }

        for (Formal f : method.formals) {
            f.accept(this);
        }
        if (method.body != null);
            method.body.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Formal formal) {
        ((IdSymbol) formal.symbol).type = (TypeSymbol) formal.scope.lookup(formal.type.getText(), "type");

        if (formal.scope.lookup(formal.type.getText(), "type") == null &&
                !formal.type.getText().equals("SELF_TYPE")) {
            SymbolTable.error(formal.context, formal.type,
                    "Method " + ((DefaultScope) formal.scope).name + " of class " +
                            ((DefaultScope) formal.scope.getParent()).name +
                            " has formal parameter " + formal.name.token.getText() +
                            " with undefined type " + formal.type.getText());
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Let let) {
        for (Var v : let.vars) {
            v.accept(this);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Case c) {
        c.value.accept(this);
        for (CaseOpt co : c.options) {
            co.accept(this);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(CaseOpt c) {
        if (c.type.getText().equals("SELF_TYPE")) {
            SymbolTable.error(c.context, c.type,
                    "Case variable " + c.name.token.getText() + " has illegal type SELF_TYPE");
            return null;
        }

        if (c.scope.lookup(c.type.getText(), "type") == null) {
            SymbolTable.error(c.context, c.type,
                    "Case variable " + c.name.token.getText() + " has undefined type " + c.type.getText());
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Var v) {
        if (v.scope.lookup(v.type.getText(), "type") == null) {
            SymbolTable.error(v.context, v.type,
                    "Let variable " + v.name.token.getText() + " has undefined type " + v.type.getText());
        }
        v.value.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Id id) {
        // System.out.println(id.token.getText() + " " + id.scope);
        if (!id.token.getText().equals("self") && id.scope != null &&
                //id.scope.lookup(id.token.getText(), "attr") == null &&
                id.scope.lookup(id.token.getText(), "var") == null) {
            SymbolTable.error(id.context, id.token,
                    "Undefined identifier " + id.token.getText());
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Int intt) {
        return null;
    }

    @Override
    public TypeSymbol visit(If iff) {
        return null;
    }

    @Override
    public TypeSymbol visit(Str str) {
        return null;
    }

    @Override
    public TypeSymbol visit(Bool bool) {
        return null;
    }

    @Override
    public TypeSymbol visit(Assign assign) {
        visit(assign.id);
        return null;
    }

    @Override
    public TypeSymbol visit(Relational rel) {
        return null;
    }

    @Override
    public TypeSymbol visit(Plus plus) {
        return null;
    }

    @Override
    public TypeSymbol visit(Minus minus) {
        return null;
    }

    @Override
    public TypeSymbol visit(Mult mult) {
        return null;
    }

    @Override
    public TypeSymbol visit(Div div) {
        return null;
    }

    @Override
    public TypeSymbol visit(Neg neg) {
        return null;
    }

    @Override
    public TypeSymbol visit(Not not) {
        return null;
    }

    @Override
    public TypeSymbol visit(While w) {
        return null;
    }

    @Override
    public TypeSymbol visit(IsVoid isVoid) {
        return null;
    }

    @Override
    public TypeSymbol visit(Block block) {
        return null;
    }

    @Override
    public TypeSymbol visit(New n) {
        /*
        if (n.scope.lookup(n.type.getText(), "type") == null) {
            SymbolTable.error(n.context, n.token,
                    "new is used with undefined type " + n.type.getText());
        }

         */
        return null;
    }

    @Override
    public TypeSymbol visit(ExplicitDispatch explDisp) {
        return null;
    }

    @Override
    public TypeSymbol visit(ImplicitDispatch implDisp) {
        return null;
    }
}
