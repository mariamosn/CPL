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
        if (c.scope == null || c.scope.lookup(c.name.getText()) == null)
            return null;

        if (c.parent != null && c.scope.lookup(c.parent.getText()) == null) {
            SymbolTable.error(c.context, c.parent,
                    "Class " + c.name.getText() + " has undefined parent " + c.parent.getText());
            return null;
        } else if (c.parent != null) {
            ((TypeSymbol) c.symbol).parent = (TypeSymbol) c.scope.lookup(c.parent.getText());
        }
        ((TypeSymbol) c.symbol).scope = c.scope;

        for (Feature f : c.features) {
            if (f instanceof Attribute && f.symbol != null) {
                ((IdSymbol) f.symbol).parent = c.symbol;
            }
            f.accept(this);
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Attribute attribute) {
        if (attribute.flag != 0 &&
                (attribute.scope.lookup(attribute.type.getText()) == null ||
                !(attribute.scope.lookup(attribute.type.getText()) instanceof TypeSymbol))
            ){
            SymbolTable.error(attribute.context, attribute.type,
                    "Class " + ((DefaultScope) attribute.scope).name + " has attribute " +
                            attribute.name.token.getText() + " with undefined type " + attribute.type.getText());
            return null;
        }
        if (attribute.symbol != null)
            ((IdSymbol) attribute.symbol).type = (TypeSymbol) attribute.scope.lookup(attribute.type.getText());
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
