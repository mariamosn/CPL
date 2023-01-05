package cool.parser;

import cool.structures.*;

import java.util.LinkedHashMap;

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
                if (c.scope != null && c.scope.lookup(attribute.name.token.getText(), "var") != null) {
                    SymbolTable.error(attribute.context, attribute.token,
                            "Class " + ((DefaultScope) attribute.scope).name + " redefines inherited attribute " +
                                    attribute.name.token.getText());
                    return null;
                }
                c = c.parent;
            }
        }
/*
        if (attribute.value != null)
            attribute.value.accept(this);

         */

        return null;
    }

    @Override
    public TypeSymbol visit(Method method) {
        if (method.symbol != null) {
            TypeSymbol c = (TypeSymbol) ((IdSymbol) method.symbol).parent;
            c = c.parent;
            while (c != null) {
                if (c.scope != null && c.scope.lookup(method.name.token.getText(), "method") != null) {
                    MethodSymbol m = (MethodSymbol) c.scope.lookup(method.name.token.getText(), "method");
                    if (method.formals.size() != m.formals.size()) {
                        SymbolTable.error(method.context, method.token,
                                "Class " + ((DefaultScope) method.scope.getParent()).name + " overrides method " +
                                method.name.token.getText() + " with different number of formal parameters");
                        return null;
                    }

                    for (int i = 0; i < method.formals.size(); i++) {
                        if (!method.formals.get(i).type.getText().equals(
                                ((IdSymbol) m.formals_list.get(i)).type.getName()
                        )) {
                            SymbolTable.error(method.context, method.formals.get(i).type,
                                    "Class " + ((DefaultScope) method.scope.getParent()).name +
                                            " overrides method " + method.name.token.getText() +
                                            " but changes type of formal parameter " +
                                            method.formals.get(i).name.token.getText() + " from " +
                                            ((IdSymbol) m.formals_list.get(i)).type.getName() + " to " +
                                            method.formals.get(i).type.getText());
                            return null;
                        }
                    }

                    if (m.type != null && !method.type.getText().equals(m.type.getName())) {
                        SymbolTable.error(method.context, method.type,
                                "Class " + ((DefaultScope) method.scope.getParent()).name + " overrides method " +
                                        method.name.token.getText() + " but changes return type from " +
                                m.type.getName() + " to " + method.type.getText());
                        return null;
                    }
                    return null;
                }
                c = c.parent;
            }
        }
        if (method.body != null)
            method.body.accept(this);
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
    public TypeSymbol visit(Case c) {
        return null;
    }

    @Override
    public TypeSymbol visit(CaseOpt c) {
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
}
