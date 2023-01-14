package cool.parser;

import java.util.LinkedList;

public class ASTConstructionVisitor extends CoolParserBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
        LinkedList<Class> stmts = new LinkedList<>();
        for (var child : ctx.children) {
            Class stmt = (Class) visit(child);
            if (stmt != null) {
                stmts.add(stmt);
            }
        }

        return new Program(stmts, ctx.start);
    }

    @Override
    public ASTNode visitClass(CoolParser.ClassContext ctx) {
        LinkedList<Feature> features = new LinkedList<>();
        for (var node : ctx.features) {
            Feature feature = (Feature) visit(node);
            if (feature != null) {
                features.add(feature);
            }
        }

        return new Class(ctx.start, ctx.name, ctx.parent, features);
    }

    @Override
    public ASTNode visitMethod(CoolParser.MethodContext ctx) {
        LinkedList<Formal> formals = new LinkedList<>();
        for (var node : ctx.formals) {
            formals.add((Formal) visit(node));
        }
        Expression body = (Expression) visit(ctx.expr());

        return new Method(ctx.start, new Id(ctx.name), formals, ctx.type, body);
    }

    @Override
    public ASTNode visitAttr(CoolParser.AttrContext ctx) {
        Expression e = null;
        if (ctx.value != null) {
            e = (Expression) visit(ctx.value);
        }
        return new Attribute(ctx.start, new Id(ctx.name), ctx.type, e);
    }

    @Override
    public ASTNode visitFormal(CoolParser.FormalContext ctx) {
        return new Formal(ctx.start, new Id(ctx.name), ctx.type);
    }

    @Override public ASTNode visitVar(CoolParser.VarContext ctx) {
        Expression e = null;
        if (ctx.value != null) {
            e = (Expression) visit(ctx.value);
        }
        return new Var(ctx.start, new Id(ctx.name), ctx.type, e);
    }

    @Override
    public ASTNode visitNew(CoolParser.NewContext ctx) {
        return new New(ctx.start, ctx.type);
    }

    @Override
    public ASTNode visitPlusMinus(CoolParser.PlusMinusContext ctx) {
        if (ctx.op.getText().equals("+")) {
            return new Plus((Expression)visit(ctx.a),
                    (Expression)visit(ctx.b),
                    ctx.op);
        }
        else if (ctx.op.getText().equals("-")) {
            return new Minus((Expression)visit(ctx.a),
                    (Expression)visit(ctx.b),
                    ctx.op);
        } else {
            return null;
        }
    }

    @Override
    public ASTNode visitBool(CoolParser.BoolContext ctx) {
        return new Bool(ctx.BOOL().getSymbol());
    }

    @Override public ASTNode visitString(CoolParser.StringContext ctx) {
        return new Str(ctx.STRING().getSymbol());
    }

    @Override
    public ASTNode visitIsvoid(CoolParser.IsvoidContext ctx) {
        return new IsVoid((Expression) visit(ctx.e), ctx.ISVOID().getSymbol());
    }

    @Override
    public ASTNode visitWhile(CoolParser.WhileContext ctx) {
        return new While((Expression)visit(ctx.cond),
                (Expression)visit(ctx.body),
                ctx.start);
    }

    @Override
    public ASTNode visitImplicitDispatch(CoolParser.ImplicitDispatchContext ctx) {
        LinkedList<Expression> params = new LinkedList<>();
        for (var param : ctx.params) {
            params.add((Expression) visit(param));
        }

        return new ImplicitDispatch(ctx.start, new Id(ctx.start), params);
    }

    @Override
    public ASTNode visitInt(CoolParser.IntContext ctx) {
        return new Int(ctx.INT().getSymbol());
    }

    @Override
    public ASTNode visitNeg(CoolParser.NegContext ctx) {
        return new Neg((Expression)visit(ctx.e),
                ctx.start);
    }

    @Override
    public ASTNode visitNot(CoolParser.NotContext ctx) {
        return new Neg((Expression)visit(ctx.e),
                ctx.NOT().getSymbol());
    }

    @Override
    public ASTNode visitParen(CoolParser.ParenContext ctx) {
        return visit(ctx.e);
    }

    @Override
    public ASTNode visitMultDiv(CoolParser.MultDivContext ctx) {
        if (ctx.op.getText().equals("*")) {
            return new Mult((Expression)visit(ctx.a),
                    (Expression)visit(ctx.b),
                    ctx.op);
        }
        else if (ctx.op.getText().equals("/")) {
            return new Div((Expression)visit(ctx.a),
                    (Expression)visit(ctx.b),
                    ctx.op);
        } else {
            return null;
        }
    }

    @Override
    public ASTNode visitExplicitDispatch(CoolParser.ExplicitDispatchContext ctx) {
        LinkedList<Expression> params = new LinkedList<>();
        for (var param : ctx.params) {
            params.add((Expression) visit(param));
        }

        return new ExplicitDispatch(ctx.start, (Expression) visit(ctx.entity),
                ctx.type, new Id(ctx.method), params);
    }

    @Override
    public ASTNode visitLet(CoolParser.LetContext ctx) {
        LinkedList<Var> vars = new LinkedList<>();
        for (var node : ctx.vars) {
            vars.add((Var) visit(node));
        }
        return new Let(vars, (Expression) visit(ctx.body), ctx.start);
    }

    @Override
    public ASTNode visitBlock(CoolParser.BlockContext ctx) {
        LinkedList<Expression> body = new LinkedList<>();
        for (var node : ctx.body) {
            Expression expr = (Expression) visit(node);
            body.add(expr);
        }

        return new Block(body, ctx.start);
    }

    @Override
    public ASTNode visitRelational(CoolParser.RelationalContext ctx) {
        return new Relational((Expression) visit(ctx.a),
                (Expression) visit(ctx.b),
                ctx.op);
    }

    @Override
    public ASTNode visitId(CoolParser.IdContext ctx) {
        return new Id(ctx.ID().getSymbol());
    }

    @Override
    public ASTNode visitIf(CoolParser.IfContext ctx) {
        return new If((Expression)visit(ctx.cond),
                (Expression)visit(ctx.thenBranch),
                (Expression)visit(ctx.elseBranch),
                ctx.start);
    }

    @Override
    public ASTNode visitCaseOpt(CoolParser.CaseOptContext ctx) {
        return new CaseOpt(ctx.start, new Id(ctx.name), ctx.type, (Expression) visit(ctx.value));
    }

    @Override
    public ASTNode visitCase(CoolParser.CaseContext ctx) {
        LinkedList<CaseOpt> options = new LinkedList<>();
        for (var opt : ctx.options) {
            options.add((CaseOpt) visit(opt));
        }
        return new Case(ctx.start, (Expression) visit(ctx.value), options);
    }

    @Override
    public ASTNode visitAssign(CoolParser.AssignContext ctx) {
        return new Assign(new Id(ctx.name),
                (Expression)visit(ctx.value),
                ctx.ASSIGN().getSymbol());
    }
}
