package cool.parser;

import cool.structures.*;

public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {
    TypeSymbol crtClass = null;

    @Override
    public TypeSymbol visit(Program program) {
        for (var c : program.stmts) {
            c.accept(this);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Class c) {
        TypeSymbol old_self_type = crtClass;
        if (c.scope == null || c.scope.lookup(c.name.getText(), "type") == null)
            return null;
        crtClass = (TypeSymbol) c.scope.lookup(c.name.getText(), "type");

        if (c.parent != null && c.scope.lookup(c.parent.getText(), "type") == null) {
            SymbolTable.error(c.context, c.parent,
                    "Class " + c.name.getText() + " has undefined parent " + c.parent.getText());
            return null;
        } else if (c.parent != null) {
            ((TypeSymbol) c.symbol).parent = (TypeSymbol) c.scope.lookup(c.parent.getText(), "type");
            ((DefaultScope) c.scope).mergeScope(((DefaultScope) ((TypeSymbol) c.symbol).parent.scope));
        }
        ((TypeSymbol) c.symbol).scope = c.scope;

        for (Feature f : c.features) {
            if (f.symbol != null)
                ((IdSymbol) f.symbol).parent = c.symbol;
            f.accept(this);
            if (f instanceof Attribute && f.symbol != null) {
                ((TypeSymbol) c.symbol).attrs.put(f.symbol.getName(), (IdSymbol)f.symbol);
            } else if (f.symbol != null) {
                ((TypeSymbol) c.symbol).meths.put(f.symbol.getName(), (MethodSymbol)f.symbol);
            }
        }

        crtClass = old_self_type;

        return null;
    }

    @Override
    public TypeSymbol visit(Attribute attribute) {
        TypeSymbol attr_type = (TypeSymbol) attribute.scope.lookup(attribute.type.getText(), "type");
        if (attribute.flag != 0 && attr_type == null) {
            SymbolTable.error(attribute.context, attribute.type,
                    "Class " + ((DefaultScope) attribute.scope).name + " has attribute " +
                            attribute.name.token.getText() + " with undefined type " + attribute.type.getText());
            return null;
        }

        if (attribute.symbol != null) {
            ((IdSymbol) attribute.symbol).type = attr_type;
        }
        if (attribute.name.symbol != null) {
            ((IdSymbol) attribute.name.symbol).type = attr_type;
        }

        TypeSymbol val_type;
        if (attribute.value != null) {
            val_type = attribute.value.accept(this);
            if (!attr_type.isDesc(val_type) &&
                    !(attr_type == TypeSymbol.SELF_TYPE && crtClass.isDesc(val_type)) &&
                    !(val_type == TypeSymbol.SELF_TYPE && attr_type.isDesc(crtClass))) {
                SymbolTable.error(attribute.context, attribute.value.token,
                        "Type " + val_type + " of initialization expression of attribute " +
                        attribute.name.token.getText() + " is incompatible with declared type " + attr_type);
            }
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Method method) {
        TypeSymbol ret_type;
        if (method.symbol == null)
            return null;
        ret_type = (TypeSymbol) method.scope.lookup(method.type.getText(), "type");

        ((MethodSymbol) method.symbol).type = ret_type;
        ((MethodSymbol) method.symbol).parent = crtClass;
        if (ret_type == null) {
            SymbolTable.error(method.context, method.token,
                    "Class " + ((DefaultScope) method.scope.getParent()).name + " has method " +
                    method.name.token.getText() + " with undefined return type " +
                    method.type.getText());
            return null;
        }

        for (Formal f : method.formals) {
            f.accept(this);
        }

        TypeSymbol body_type = method.body.accept(this);
        if (ret_type != null && ret_type != body_type &&
                ((!ret_type.isDesc(body_type) || ret_type == TypeSymbol.SELF_TYPE && !crtClass.isDesc(body_type))) &&
                !(body_type == TypeSymbol.SELF_TYPE && ret_type.isDesc(crtClass))) {
            SymbolTable.error(method.context, method.body.token,
                    "Type " + body_type + " of the body of method " + method.name.token.getText() +
                    " is incompatible with declared return type " + method.type.getText());
        }

        if (ret_type == TypeSymbol.SELF_TYPE) {
            ret_type = crtClass;
        }

        return ret_type;
    }

    @Override
    public TypeSymbol visit(Formal formal) {;
        ((IdSymbol) formal.symbol).type = (TypeSymbol) formal.scope.lookup(formal.type.getText(), "type");
        for (Symbol f : ((MethodSymbol) ((IdSymbol) formal.symbol).parent).formals_list) {
            if (f.getName().equals(formal.name.token.getText())) {
                ((IdSymbol) f).type = ((IdSymbol) formal.symbol).type;
            }
        }

        if (formal.scope.lookup(formal.type.getText(), "type") == null &&
                !formal.type.getText().equals("SELF_TYPE")) {
            SymbolTable.error(formal.context, formal.type,
                    "Method " + ((DefaultScope) formal.scope).name + " of class " +
                            ((DefaultScope) formal.scope.getParent()).name +
                            " has formal parameter " + formal.name.token.getText() +
                            " with undefined type " + formal.type.getText());
            return null;
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Let let) {
        for (Var v : let.vars) {
            v.accept(this);
        }

        return let.body.accept(this);
    }

    @Override
    public TypeSymbol visit(Case c) {
        TypeSymbol value_type = null;
        if (c.value != null) {
            c.value.accept(this);
            if (c.value.symbol != null)
                ((IdSymbol) c.value.symbol).type = value_type;
        }

        TypeSymbol sym = null;
        for (CaseOpt co : c.options) {
            TypeSymbol co_type = co.accept(this);
            sym = TypeSymbol.lub(sym, co_type);
        }
        return sym;
    }

    @Override
    public TypeSymbol visit(CaseOpt c) {
        if (c.type.getText().equals("SELF_TYPE")) {
            SymbolTable.error(c.context, c.type,
                    "Case variable " + c.name.token.getText() + " has illegal type SELF_TYPE");
            return null;
        }

        if (c.scope != null)
            ((IdSymbol) c.symbol).type = (TypeSymbol) c.scope.lookup(c.type.getText(), "type");
        if (c.symbol != null && ((IdSymbol) c.symbol).type == null) {
            SymbolTable.error(c.context, c.type,
                    "Case variable " + c.name.token.getText() + " has undefined type " + c.type.getText());
            return null;
        }

        c.value.scope = c.scope;
        TypeSymbol val_type = c.value.accept(this);
        return val_type;
    }

    @Override
    public TypeSymbol visit(Var v) {
        TypeSymbol id_type = (TypeSymbol) v.scope.lookup(v.type.getText(), "type");

        if (id_type == null) {
            SymbolTable.error(v.context, v.type,
                    "Let variable " + v.name.token.getText() + " has undefined type " + v.type.getText());
        } else if (v.name.symbol != null) {
            ((IdSymbol) v.name.symbol).type = id_type;
            ((IdSymbol) v.symbol).type = id_type;
        }

        TypeSymbol val_type = null;
        if (v.value != null) {
            val_type = v.value.accept(this);
        }

        if (id_type != null &&
                !id_type.isDesc(val_type) &&
                !(id_type == TypeSymbol.SELF_TYPE && crtClass.isDesc(val_type))) {
            SymbolTable.error(v.context, v.value.token,
                    "Type " + val_type + " of initialization expression of identifier " +
                    v.name.token.getText() + " is incompatible with declared type " + id_type);
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Id id) {
        if (id.token.getText().equals("self")) {
            id.symbol = new IdSymbol("self");
            ((IdSymbol) id.symbol).type = TypeSymbol.SELF_TYPE;
            return ((IdSymbol) id.symbol).type;
        }
        IdSymbol sym = null;
        if (!id.token.getText().equals("self") && id.scope != null &&
                id.scope.lookup(id.token.getText(), "var") == null) {
            SymbolTable.error(id.context, id.token,
                    "Undefined identifier " + id.token.getText());
            return null;
        }
        if (id.scope != null) {
            sym = (IdSymbol) id.scope.lookup(id.token.getText(), "var");
            id.symbol = sym;
        }
        if (sym != null)
            return ((IdSymbol) id.symbol).type;
        return null;
    }

    @Override
    public TypeSymbol visit(Int intt) {
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(If iff) {
        TypeSymbol cond_type = iff.cond.accept(this);
        if (cond_type != TypeSymbol.BOOL) {
            SymbolTable.error(iff.context, iff.cond.token,
                    "If condition has type " + cond_type + " instead of Bool");
        }

        TypeSymbol then_type = iff.thenBranch.accept(this);
        TypeSymbol else_type = iff.elseBranch.accept(this);

        return TypeSymbol.lub(then_type, else_type);
    }

    @Override
    public TypeSymbol visit(Str str) {
        return TypeSymbol.STRING;
    }

    @Override
    public TypeSymbol visit(Bool bool) {
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(Assign assign) {
        TypeSymbol id_type = assign.id.accept(this);
        TypeSymbol val_type = assign.expr.accept(this);

        if (id_type != null && !id_type.isDesc(val_type) &&
                !(id_type == TypeSymbol.SELF_TYPE && crtClass.isDesc(val_type)) &&
                !(val_type == TypeSymbol.SELF_TYPE && id_type.isDesc(crtClass))) {
            SymbolTable.error(assign.context, assign.expr.token,
                    "Type " + val_type + " of assigned expression is incompatible with declared type " +
                            id_type + " of identifier " + assign.id.token.getText());
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Relational rel) {
        TypeSymbol op1_type = rel.left.accept(this);
        if ((rel.token.getText().equals("<") || rel.token.getText().equals("<="))
                && op1_type != null && op1_type != TypeSymbol.INT) {
            SymbolTable.error(rel.context, rel.left.token,
                    "Operand of " + rel.token.getText() + " has type " + op1_type.getName() + " instead of Int");
            return TypeSymbol.BOOL;
        }

        TypeSymbol op2_type = rel.right.accept(this);
        if ((rel.token.getText().equals("<") || rel.token.getText().equals("<="))
                && op2_type != null && op2_type != TypeSymbol.INT) {
            SymbolTable.error(rel.context, rel.right.token,
                    "Operand of " + rel.token.getText() + " has type " + op2_type.getName() + " instead of Int");
            return TypeSymbol.BOOL;
        }

        if (rel.token.getText().equals("=") && op1_type != op2_type &&
                (op1_type == TypeSymbol.INT || op1_type == TypeSymbol.BOOL || op1_type == TypeSymbol.STRING ||
                        op2_type == TypeSymbol.INT || op2_type == TypeSymbol.BOOL || op2_type == TypeSymbol.STRING)) {
            SymbolTable.error(rel.context, rel.token,
                    "Cannot compare " + op1_type + " with " + op2_type);
        }

        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(Plus plus) {
        TypeSymbol op1_type = plus.left.accept(this);
        if (op1_type != null && op1_type != TypeSymbol.INT) {
            SymbolTable.error(plus.context, plus.left.token,
                    "Operand of + has type " + op1_type.getName() + " instead of Int");
        }

        TypeSymbol op2_type = plus.right.accept(this);
        if (op2_type != null && op2_type != TypeSymbol.INT) {
            SymbolTable.error(plus.context, plus.right.token,
                    "Operand of + has type " + op2_type.getName() + " instead of Int");
        }
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Minus minus) {
        TypeSymbol op1_type = minus.left.accept(this);
        if (op1_type != null && op1_type != TypeSymbol.INT) {
            SymbolTable.error(minus.context, minus.left.token,
                    "Operand of - has type " + op1_type.getName() + " instead of Int");
        }

        TypeSymbol op2_type = minus.right.accept(this);
        if (op2_type != null && op2_type != TypeSymbol.INT) {
            SymbolTable.error(minus.context, minus.right.token,
                    "Operand of - has type " + op2_type.getName() + " instead of Int");
        }
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Mult mult) {
        TypeSymbol op1_type = mult.left.accept(this);
        if (op1_type != null && op1_type != TypeSymbol.INT) {
            SymbolTable.error(mult.context, mult.left.token,
                    "Operand of * has type " + op1_type.getName() + " instead of Int");
        }

        TypeSymbol op2_type = mult.right.accept(this);
        if (op2_type != null && op2_type != TypeSymbol.INT) {
            SymbolTable.error(mult.context, mult.right.token,
                    "Operand of * has type " + op2_type.getName() + " instead of Int");
        }
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Div div) {
        TypeSymbol op1_type = div.left.accept(this);
        if (op1_type != null && op1_type != TypeSymbol.INT) {
            SymbolTable.error(div.context, div.left.token,
                    "Operand of / has type " + op1_type.getName() + " instead of Int");
        }

        TypeSymbol op2_type = div.right.accept(this);
        if (op2_type != null && op2_type != TypeSymbol.INT) {
            SymbolTable.error(div.context, div.right.token,
                    "Operand of / has type " + op2_type.getName() + " instead of Int");
        }
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Neg neg) {
        TypeSymbol op_type = neg.expr.accept(this);
        if (op_type != null && op_type != TypeSymbol.INT) {
            SymbolTable.error(neg.context, neg.expr.token,
                    "Operand of ~ has type " + op_type.getName() + " instead of Int");
        }

        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Not not) {
        TypeSymbol op_type = not.expr.accept(this);
        if (op_type != null && op_type != TypeSymbol.BOOL) {
            SymbolTable.error(not.context, not.expr.token,
                    "Operand of not has type " + op_type.getName() + " instead of Bool");
        }

        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(While w) {
        TypeSymbol cond_type = w.cond.accept(this);
        if (cond_type != TypeSymbol.BOOL) {
            SymbolTable.error(w.context, w.cond.token,
                    "While condition has type " + cond_type + " instead of Bool");
        }
        return TypeSymbol.OBJECT;
    }

    @Override
    public TypeSymbol visit(IsVoid isVoid) {
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(Block block) {
        TypeSymbol sym = null;
        for (Expression e : block.body) {
            sym = e.accept(this);
        }
        return sym;
    }

    @Override
    public TypeSymbol visit(New n) {
        TypeSymbol sym = (TypeSymbol) n.scope.lookup(n.type.getText(), "type");
        if (sym == null) {
            SymbolTable.error(n.context, n.type,
                    "new is used with undefined type " + n.type.getText());
            return null;
        }
        return sym;
    }

    @Override
    public TypeSymbol visit(ExplicitDispatch explDisp) {
        TypeSymbol entity_type = explDisp.entity.accept(this);
        if (entity_type == TypeSymbol.SELF_TYPE) {
            entity_type = crtClass;
        }
        TypeSymbol at_type = null;
        TypeSymbol actual_type = entity_type;
        MethodSymbol m_sym = null;

        if (explDisp.atType != null && !explDisp.atType.getText().equals("SELF_TYPE")) {
            at_type = (TypeSymbol) explDisp.scope.lookup(explDisp.atType.getText(), "type");
            if (at_type == null) {
                SymbolTable.error(explDisp.context, explDisp.atType,
                        "Type " + explDisp.atType.getText() + " of static dispatch is undefined");
                return null;
            }

            if (!at_type.isDesc(entity_type)) {
                SymbolTable.error(explDisp.context, explDisp.atType,
                        "Type " + at_type + " of static dispatch is not a superclass of type " + entity_type);
                return null;
            }
            actual_type = at_type;
        }
        ((DispSymbol) explDisp.symbol).type = actual_type;

        if (actual_type == null)
            return null;

        TypeSymbol crt = actual_type;
        while (crt != null) {
            if (crt.scope != null) {
                m_sym = (MethodSymbol) crt.scope.lookup(explDisp.method.token.getText(), "method");
                if (m_sym != null) {
                    break;
                }
            }
            crt = crt.parent;
        }

        if (m_sym == null) {
            TypeSymbol aux = actual_type;
            while (aux != null) {
                aux = aux.parent;
            }
            SymbolTable.error(explDisp.context, explDisp.method.token,
                    "Undefined method " + explDisp.method.token.getText() + " in class " + actual_type);
            return null;
        }

        ((DispSymbol) explDisp.symbol).method = m_sym;

        if (explDisp.params.size() != m_sym.formals_list.size()) {
            SymbolTable.error(explDisp.context, explDisp.method.token,
                    "Method " + explDisp.method.token.getText() + " of class " + actual_type +
                            " is applied to wrong number of arguments");
            return null;
        }

        for (Expression p : explDisp.params) {
            TypeSymbol p_type = p.accept(this);
            ((DispSymbol) explDisp.symbol).param_types.add(p_type);
        }

        if (m_sym.type == TypeSymbol.SELF_TYPE && entity_type != crtClass)
            return entity_type;

        if (m_sym.type != null)
            return (TypeSymbol) SymbolTable.globals.lookup(m_sym.type.getName(), "type");
        return null;
    }

    @Override
    public TypeSymbol visit(ImplicitDispatch implDisp) {

        MethodSymbol m_sym = null;

        TypeSymbol crt = crtClass;
        while (crt != null) {
            if (crt.scope != null) {
                m_sym = (MethodSymbol) crt.scope.lookup(implDisp.method.token.getText(), "method");
                if (m_sym != null) {
                    break;
                }
            }
            crt = crt.parent;
        }
        if (m_sym == null) {
            SymbolTable.error(implDisp.context, implDisp.method.token,
                    "Undefined method " + implDisp.method.token.getText() + " in class " + crtClass);
            return null;
        }

        ((DispSymbol) implDisp.symbol).method = m_sym;
        ((DispSymbol) implDisp.symbol).type = crtClass;

        if (implDisp.params.size() != m_sym.formals_list.size()) {
            SymbolTable.error(implDisp.context, implDisp.method.token,
                    "Method " + implDisp.method.token.getText() + " of class " + crtClass +
                            " is applied to wrong number of arguments");
            return null;
        }

        for (Expression p : implDisp.params) {
            TypeSymbol p_type = p.accept(this);
            ((DispSymbol) implDisp.symbol).param_types.add(p_type);
        }

        if (m_sym.type != null)
            return (TypeSymbol) SymbolTable.globals.lookup(m_sym.type.getName(), "type");


        return null;
    }
}
