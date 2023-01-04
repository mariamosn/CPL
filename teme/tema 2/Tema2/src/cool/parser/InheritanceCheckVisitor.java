package cool.parser;

import cool.structures.*;

public class InheritanceCheckVisitor implements ASTVisitor<TypeSymbol> {

    @Override
    public TypeSymbol visit(Program program) {
        for (var c : program.stmts) {
            c.accept(this);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Class c) {
        TypeSymbol sym = (TypeSymbol) c.symbol;
        while (sym != null) {
            sym = sym.parent;
            if (sym == (TypeSymbol) c.symbol) {
                SymbolTable.error(c.context, c.name,
                        "Inheritance cycle for class " + c.name.getText());
                return null;
            }
        }

        for (Feature f : c.features) {
            f.accept(this);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Attribute attribute) {
        if (attribute.symbol != null) {
            TypeSymbol c = (TypeSymbol) ((IdSymbol) attribute.symbol).parent;
            c = c.parent;
            while (c != null) {
                if (c.scope != null && c.scope.lookup(attribute.name.token.getText()) != null) {
                    SymbolTable.error(attribute.context, attribute.token,
                            "Class " + ((DefaultScope) attribute.scope).name + " redefines inherited attribute " +
                                    attribute.name.token.getText());
                    return null;
                }
                c = c.parent;
            }
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Id id) {
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
    public TypeSymbol visit(Let let) {
        return null;
    }

    @Override
    public TypeSymbol visit(Var v) {
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
        return null;
    }

    @Override
    public TypeSymbol visit(Method method) {
        return null;
    }

    @Override
    public TypeSymbol visit(Formal formal) {
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

    @Override
    public TypeSymbol visit(CaseOpt c) {
        return null;
    }

    @Override
    public TypeSymbol visit(Case c) {
        return null;
    }
}
