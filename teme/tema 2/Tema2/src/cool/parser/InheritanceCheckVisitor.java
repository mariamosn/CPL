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
        attribute.name.accept(this);
        if (attribute.value != null)
            attribute.value.accept(this);

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
        for (Var v : let.vars) {
            v.accept(this);
        }
        let.body.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Var v) {
        v.name.accept(this);
        if (v.value != null)
            v.value.accept(this);
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
        c.name.accept(this);
        c.value.accept(this);
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
        iff.cond.accept(this);
        iff.thenBranch.accept(this);
        iff.elseBranch.accept(this);
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
        assign.id.accept(this);
        assign.expr.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Relational rel) {
        rel.left.accept(this);
        rel.right.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Plus plus) {
        plus.left.accept(this);
        plus.right.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Minus minus) {
        minus.left.accept(this);
        minus.right.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Mult mult) {
        mult.left.accept(this);
        mult.right.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Div div) {
        div.left.accept(this);
        div.right.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Neg neg) {
        neg.expr.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Not not) {
        not.expr.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(While w) {
        w.cond.accept(this);
        w.body.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(IsVoid isVoid) {
        isVoid.e.accept(this);
        return null;
    }

    @Override
    public TypeSymbol visit(Block block) {
        for (Expression e : block.body) {
            e.accept(this);
        }
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
        MethodSymbol m_sym = (MethodSymbol) ((DispSymbol) explDisp.symbol).method;
        if (m_sym != null && ((DispSymbol) explDisp.symbol).param_types.size() != 0) {
            for (int i = 0; i < explDisp.params.size(); i++) {
                TypeSymbol param_type = ((DispSymbol) explDisp.symbol).param_types.get(i);
                TypeSymbol formal_type = ((IdSymbol) m_sym.formals_list.get(i)).type;
                if (!formal_type.isDesc(param_type)) {
                    SymbolTable.error(explDisp.context, explDisp.params.get(i).token,
                            "In call to method " + explDisp.method.token.getText() + " of class " +
                                    ((DispSymbol) explDisp.symbol).type +
                                    ", actual type " + param_type + " of formal parameter " +
                                    m_sym.formals_list.get(i).getName() +
                                    " is incompatible with declared type " + formal_type);
                }
            }
        }

        explDisp.entity.accept(this);
        explDisp.method.accept(this);
        for (Expression p : explDisp.params) {
            p.accept(this);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(ImplicitDispatch implDisp) {
        MethodSymbol m_sym = (MethodSymbol) ((DispSymbol) implDisp.symbol).method;
        if (m_sym != null && ((DispSymbol) implDisp.symbol).param_types.size() != 0) {
            for (int i = 0; i < implDisp.params.size(); i++) {
                TypeSymbol param_type = ((DispSymbol) implDisp.symbol).param_types.get(i);
                TypeSymbol formal_type = ((IdSymbol) m_sym.formals_list.get(i)).type;
                if (!formal_type.isDesc(param_type)) {
                    SymbolTable.error(implDisp.context, implDisp.params.get(i).token,
                            "In call to method " + implDisp.method.token.getText() + " of class " +
                                    ((DispSymbol) implDisp.symbol).type +
                                    ", actual type " + param_type + " of formal parameter " +
                                    m_sym.formals_list.get(i).getName() +
                                    " is incompatible with declared type " + formal_type);
                }
            }
        }

        implDisp.method.accept(this);
        for (Expression p : implDisp.params) {
            p.accept(this);
        }
        return null;
    }
}
