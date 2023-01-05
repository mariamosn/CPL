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
        TypeSymbol attr_type = (TypeSymbol) attribute.scope.lookup(attribute.type.getText(), "type");
        if (attribute.flag != 0 && attr_type == null) {
            SymbolTable.error(attribute.context, attribute.type,
                    "Class " + ((DefaultScope) attribute.scope).name + " has attribute " +
                            attribute.name.token.getText() + " with undefined type " + attribute.type.getText());
            return null;
        }
        //attribute.name.accept(this);
        if (attribute.symbol != null) {
            ((IdSymbol) attribute.symbol).type = attr_type;
        }
        if (attribute.name.symbol != null) {
            ((IdSymbol) attribute.name.symbol).type = attr_type;
        }

        TypeSymbol val_type = null;
        if (attribute.value != null) {
            val_type = attribute.value.accept(this);
            if (!attr_type.isDesc(val_type)) {
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
        if (ret_type != null && !ret_type.isDesc(body_type)) {
            SymbolTable.error(method.context, method.body.token,
                    "Type " + body_type + " of the body of method " + method.name.token.getText() +
                    " is incompatible with declared return type " + ret_type);
        }

        return ret_type;
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
        }

        TypeSymbol val_type = null;
        if (v.value != null) {
            val_type = v.value.accept(this);
        }

        if (id_type != null && !id_type.isDesc(val_type)) {
            SymbolTable.error(v.context, v.value.token,
                    "Type " + val_type + " of initialization expression of identifier " +
                    v.name.token.getText() + " is incompatible with declared type " + id_type);
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Id id) {
        IdSymbol sym = null;
        if (!id.token.getText().equals("self") && id.scope != null &&
                //id.scope.lookup(id.token.getText(), "attr") == null &&
                id.scope.lookup(id.token.getText(), "var") == null) {
            SymbolTable.error(id.context, id.token,
                    "Undefined identifier " + id.token.getText());
            return null;
        }
        if (id.scope != null) {
            sym = (IdSymbol) id.scope.lookup(id.token.getText(), "var");
            /*
            if (sym == null)
                sym = (IdSymbol) id.scope.lookup(id.token.getText(), "attr");

             */
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

        if (id_type != null && !id_type.isDesc(val_type)) {
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
        return null;
    }

    @Override
    public TypeSymbol visit(ImplicitDispatch implDisp) {
        return null;
    }
}
