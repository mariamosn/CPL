package cool.parser;

import cool.structures.*;

public class DefinitionPassVisitor implements ASTVisitor<Void> {
    private Scope currentScope = null;

    @Override
    public Void visit(Program program) {
        currentScope = SymbolTable.globals;
        program.scope = currentScope;
        program.symbol = new Symbol("program");

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
        sym.tag = TypeSymbol.nextTag;
        TypeSymbol.nextTag++;
        if (!currentScope.add(sym, "type")) {
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

        for (Feature f : c.features) {
            f.accept(this);
        }
        c.scope = currentScope;
        ((TypeSymbol) c.symbol).scope = currentScope;

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
        if (!currentScope.add(sym, "var")) {
            SymbolTable.error(attribute.context, attribute.name.token,
                    "Class " + ((DefaultScope) currentScope).name + " redefines attribute " +
                            attribute.name.token.getText());
            attribute.flag = 0;
            return null;
        }

        attribute.symbol = sym;
        attribute.flag = 1;

        attribute.name.accept(this);
        if (attribute.value != null) {
            attribute.value.accept(this);
        }

        return null;
    }

    @Override
    public Void visit(Method method) {
        MethodSymbol sym = new MethodSymbol(method.name.getToken().getText());
        sym.type = new TypeSymbol(method.type.getText());
        if (!currentScope.add(sym, "method")) {
            SymbolTable.error(method.context, method.name.token,
                    "Class " + ((DefaultScope) currentScope).name + " redefines method " +
                            method.name.token.getText());
            return null;
        }

        currentScope = new DefaultScope(currentScope, method.name.getToken().getText());
        method.scope = currentScope;
        method.symbol = sym;

        for (Formal f : method.formals) {
            f.accept(this);
            ((MethodSymbol) method.symbol).formals.put(f.name.token.getText(), f.symbol);
            ((MethodSymbol) method.symbol).formals_list.add(f.symbol);
            ((IdSymbol) f.symbol).parent = method.symbol;
        }

        if (method.body != null)
            method.body.accept(this);

        currentScope = currentScope.getParent();
        return null;
    }

    @Override
    public Void visit(Formal formal) {
        formal.scope = currentScope;
        formal.symbol = new IdSymbol(formal.name.getToken().getText());

        if (formal.name.token.getText().equals("self")) {
            SymbolTable.error(formal.context, formal.token,
                    "Method " + ((DefaultScope) formal.scope).name + " of class " +
                            ((DefaultScope) formal.scope.getParent()).name +
                            " has formal parameter with illegal name self");
            return null;
        }

        if (((DefaultScope) currentScope).vars.containsKey(formal.name.token.getText())) {
            SymbolTable.error(formal.context, formal.token,
                    "Method " + ((DefaultScope) formal.scope).name + " of class " +
                            ((DefaultScope) formal.scope.getParent()).name +
                            " redefines formal parameter " + formal.name.token.getText());
            return null;
        }

        if (formal.type.getText().equals("SELF_TYPE")) {
            SymbolTable.error(formal.context, formal.type,
                    "Method " + ((DefaultScope) formal.scope).name + " of class " +
                            ((DefaultScope) formal.scope.getParent()).name +
                            " has formal parameter " + formal.name.token.getText() + " with illegal type SELF_TYPE");
            return null;
        }

        if (!currentScope.add(formal.symbol, "var")) {
            return null;
        }
        return null;
    }

    @Override
    public Void visit(Let let) {
        MethodSymbol sym = new MethodSymbol("let");

        currentScope = new DefaultScope(currentScope, "let");
        let.scope = currentScope;
        let.symbol = sym;

        for (Var v : let.vars) {
            v.accept(this);
            currentScope = new DefaultScope(currentScope, "let");
            currentScope.add(v.symbol, "var");
        }

        let.body.accept(this);

        var aux = currentScope;
        currentScope = let.scope;
        let.scope = aux;

        currentScope = currentScope.getParent();

        return null;
    }

    @Override
    public Void visit(Var v) {
        IdSymbol sym = new IdSymbol(v.name.getToken().getText());
        v.symbol = sym;
        v.scope = currentScope;
        if (v.name.token.getText().equals("self")) {
            SymbolTable.error(v.context, v.token,
                    "Let variable has illegal name self");
            return null;
        }
        v.name.accept(this);
        if (v.value != null)
            v.value.accept(this);
        return null;
    }

    @Override
    public Void visit(Case c) {
        CaseSymbol sym = new CaseSymbol();

        currentScope = new DefaultScope(currentScope, "case");
        c.scope = currentScope;
        c.symbol = sym;

        c.value.accept(this);

        for (CaseOpt co : c.options) {
            co.accept(this);
            ((CaseSymbol) c.symbol).case_opt.add(co.symbol);
        }

        currentScope = currentScope.getParent();
        return null;
    }

    @Override
    public Void visit(CaseOpt c) {
        IdSymbol sym = new IdSymbol("caseOpt");
        c.scope = new DefaultScope(currentScope);
        c.symbol = sym;

        if (c.name.token.getText().equals("self")) {
            SymbolTable.error(c.context, c.token,
                    "Case variable has illegal name self");
            return null;
        }

        c.scope.add(sym, "var");

        return null;
    }

    @Override
    public Void visit(Id id) {
        IdSymbol sym = new IdSymbol(id.token.getText());
        id.symbol = sym;
        id.scope = currentScope;
        return null;
    }

    @Override
    public Void visit(Int intt) {
        intt.scope = currentScope;
        intt.symbol = new Symbol(intt.token.getText());
        return null;
    }

    @Override
    public Void visit(If iff) {
        iff.symbol = new Symbol("if");
        iff.scope = currentScope;
        iff.cond.accept(this);
        iff.thenBranch.accept(this);
        iff.elseBranch.accept(this);
        return null;
    }

    @Override
    public Void visit(Str str) {
        str.symbol = new Symbol(str.token.getText());
        str.scope = currentScope;
        return null;
    }

    @Override
    public Void visit(Bool bool) {
        bool.symbol = new Symbol(bool.token.getText());
        bool.scope = currentScope;
        return null;
    }

    @Override
    public Void visit(Assign assign) {
        assign.symbol = new Symbol("assign");
        assign.scope = currentScope;
        if (assign.id.token.getText().equals("self")) {
            SymbolTable.error(assign.context, assign.id.token,
                    "Cannot assign to self");
        }
        assign.id.scope = currentScope;
        assign.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(Relational rel) {
        rel.symbol = new Symbol("rel");
        rel.scope = currentScope;
        rel.left.accept(this);
        rel.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Plus plus) {
        plus.symbol = new Symbol("plus");
        plus.scope = currentScope;
        plus.left.accept(this);
        plus.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Minus minus) {
        minus.symbol = new Symbol("minus");
        minus.scope = currentScope;
        minus.left.accept(this);
        minus.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Mult mult) {
        mult.symbol = new Symbol("mult");
        mult.scope = currentScope;
        mult.left.accept(this);
        mult.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Div div) {
        div.symbol = new Symbol("div");
        div.scope = currentScope;
        div.left.accept(this);
        div.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Neg neg) {
        neg.symbol = new Symbol("neg");
        neg.scope = currentScope;
        neg.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(Not not) {
        not.symbol = new Symbol("not");
        not.scope = currentScope;
        not.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(While w) {
        w.symbol = new Symbol("while");
        w.scope = currentScope;
        w.cond.accept(this);
        w.body.accept(this);
        return null;
    }

    @Override
    public Void visit(IsVoid isVoid) {
        isVoid.symbol = new Symbol("isVoid");
        isVoid.scope = currentScope;
        isVoid.e.accept(this);
        return null;
    }

    @Override
    public Void visit(Block block) {
        block.symbol = new Symbol("block");
        block.scope = currentScope;
        for (Expression e : block.body) {
            e.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(New n) {
        IdSymbol sym = new IdSymbol("new");
        n.symbol = sym;
        n.scope = currentScope;
        return null;
    }

    @Override
    public Void visit(ExplicitDispatch explDisp) {
        explDisp.symbol = new DispSymbol("explDisp");
        explDisp.scope = currentScope;

        if (explDisp.atType != null && explDisp.atType.getText().equals("SELF_TYPE")) {
            SymbolTable.error(explDisp.context, explDisp.atType,
                    "Type of static dispatch cannot be SELF_TYPE");
        }

        explDisp.entity.accept(this);
        explDisp.method.accept(this);

        for (Expression e : explDisp.params) {
            e.accept(this);
        }

        return null;
    }

    @Override
    public Void visit(ImplicitDispatch implDisp) {
        implDisp.symbol = new DispSymbol("implDisp");
        implDisp.scope = currentScope;

        implDisp.method.accept(this);
        for (Expression e : implDisp.params) {
            e.accept(this);
        }

        return null;
    }
}
