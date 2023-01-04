package cool.parser;

import cool.structures.*;

public class DefinitionPassVisitor implements ASTVisitor<Void> {
    private Scope currentScope = null;

    @Override
    public Void visit(Program program) {
        currentScope = SymbolTable.globals;
        program.scope = currentScope;

        for (var c : program.stmts) {
            c.accept(this);
        }

        return null;
    }

    @Override
    public Void visit(Class c) {
        if (c.name.getText().equals("SELF_TYPE")) {
            SymbolTable.error(c.context, c.name,
                    "Class has illegal name SELF_TYPE");
            return null;
        }

        TypeSymbol sym = new TypeSymbol(c.name.getText(), c.parent);
        if (!currentScope.add(sym)) {
            SymbolTable.error(c.context, c.name,
                    "Class " + c.name.getText() + " is redefined");
            return null;
        }

        c.symbol = sym;

        if (c.parent != null &&
                (c.parent.getText().equals("Int") ||
                        c.parent.getText().equals("String") ||
                        c.parent.getText().equals("Bool") ||
                        c.parent.getText().equals("SELF_TYPE"))
        ) {
            SymbolTable.error(c.context, c.parent,
                    "Class " + c.name.getText() + " has illegal parent " + c.parent.getText());
            c.parent = null;
            return null;
        } else if (c.parent == null) {
            ((TypeSymbol) c.symbol).parent = TypeSymbol.OBJECT;
        }

        currentScope = new DefaultScope(currentScope, c.name.getText());
        c.scope = currentScope;

        for (Feature f : c.features) {
            f.accept(this);
        }

        currentScope = currentScope.getParent();

        return null;
    }

    @Override
    public Void visit(Attribute attribute) {
        attribute.scope = currentScope;
        if (attribute.name.token.getText().equals("self")) {
            SymbolTable.error(attribute.context, attribute.name.token,
                    "Class " + ((DefaultScope) currentScope).name + " has attribute with illegal name self");
            attribute.flag = 0;
            return null;
        }

        IdSymbol sym = new IdSymbol(attribute.name.getToken().getText());
        if (!currentScope.add(sym)) {
            SymbolTable.error(attribute.context, attribute.name.token,
                    "Class " + ((DefaultScope) currentScope).name + " redefines attribute " +
                            attribute.name.token.getText());
            attribute.flag = 0;
            return null;
        }

        attribute.symbol = sym;
        attribute.flag = 1;

        return null;
    }

    @Override
    public Void visit(Method method) {
        return null;
    }

    @Override
    public Void visit(Id id) {
        return null;
    }

    @Override
    public Void visit(Int intt) {
        return null;
    }

    @Override
    public Void visit(If iff) {
        return null;
    }

    @Override
    public Void visit(Str str) {
        return null;
    }

    @Override
    public Void visit(Bool bool) {
        return null;
    }

    @Override
    public Void visit(Assign assign) {
        return null;
    }

    @Override
    public Void visit(Relational rel) {
        return null;
    }

    @Override
    public Void visit(Plus plus) {
        return null;
    }

    @Override
    public Void visit(Minus minus) {
        return null;
    }

    @Override
    public Void visit(Mult mult) {
        return null;
    }

    @Override
    public Void visit(Div div) {
        return null;
    }

    @Override
    public Void visit(Neg neg) {
        return null;
    }

    @Override
    public Void visit(Not not) {
        return null;
    }

    @Override
    public Void visit(While w) {
        return null;
    }

    @Override
    public Void visit(Let let) {
        return null;
    }

    @Override
    public Void visit(Var v) {
        return null;
    }

    @Override
    public Void visit(IsVoid isVoid) {
        return null;
    }

    @Override
    public Void visit(Block block) {
        return null;
    }

    @Override
    public Void visit(New n) {
        return null;
    }

    @Override
    public Void visit(Formal formal) {
        return null;
    }

    @Override
    public Void visit(ExplicitDispatch explDisp) {
        return null;
    }

    @Override
    public Void visit(ImplicitDispatch implDisp) {
        return null;
    }

    @Override
    public Void visit(CaseOpt c) {
        return null;
    }

    @Override
    public Void visit(Case c) {
        return null;
    }
}
