package cool.parser;

import org.antlr.v4.runtime.Token;

public class ASTToStringVisitor implements ASTVisitor<String> {
    private int crtIdent;

    public ASTToStringVisitor() {
        crtIdent = 0;
    }

    @Override
    public String visit(Id id) {
        return "  ".repeat(crtIdent) + id.token.getText() + "\n";
    }

    @Override
    public String visit(Int intt) {
        return "  ".repeat(crtIdent) + intt.token.getText() + "\n";
    }

    @Override
    public String visit(If iff) {
        String str;
        str = "  ".repeat(crtIdent) + iff.token.getText() + "\n";

        crtIdent++;
        str += iff.cond.accept(this);
        str += iff.thenBranch.accept(this);
        str += iff.elseBranch.accept(this);
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Str str) {
        return "  ".repeat(crtIdent) + str.token.getText() + "\n";
    }

    @Override
    public String visit(Bool bool) {
        return "  ".repeat(crtIdent) + bool.token.getText() + "\n";
    }

    @Override
    public String visit(Assign assign) {
        String str;
        str = "  ".repeat(crtIdent) + assign.token.getText() + "\n";

        crtIdent++;
        str += assign.id.accept(this);//"  ".repeat(crtIdent) + assign.id.token.getText() + "\n";
        str += assign.expr.accept(this);//"  ".repeat(crtIdent) + assign.expr.token.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Relational rel) {
        String str;
        str = "  ".repeat(crtIdent) + rel.token.getText() + "\n";

        crtIdent++;
        str += rel.left.accept(this);//"  ".repeat(crtIdent) + rel.left.token.getText() + "\n";
        str += rel.right.accept(this);//"  ".repeat(crtIdent) + rel.right.token.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Plus plus) {
        String str;
        str = "  ".repeat(crtIdent) + plus.token.getText() + "\n";

        crtIdent++;
        str += plus.left.accept(this);//"  ".repeat(crtIdent) + plus.left.token.getText() + "\n";
        str += plus.right.accept(this);//"  ".repeat(crtIdent) + plus.right.token.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Minus minus) {
        String str;
        str = "  ".repeat(crtIdent) + minus.token.getText() + "\n";

        crtIdent++;
        str += minus.left.accept(this);//"  ".repeat(crtIdent) + minus.left.token.getText() + "\n";
        str += minus.right.accept(this);//"  ".repeat(crtIdent) + minus.right.token.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Mult mult) {
        String str;
        str = "  ".repeat(crtIdent) + mult.token.getText() + "\n";

        crtIdent++;
        str += mult.left.accept(this);//"  ".repeat(crtIdent) + mult.left.token.getText() + "\n";
        str += mult.right.accept(this);//"  ".repeat(crtIdent) + mult.right.token.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Div div) {
        String str;
        str = "  ".repeat(crtIdent) + div.token.getText() + "\n";

        crtIdent++;
        str += div.left.accept(this);//"  ".repeat(crtIdent) + div.left.token.getText() + "\n";
        str += div.right.accept(this);//"  ".repeat(crtIdent) + div.right.token.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Neg neg) {
        String str;
        str = "  ".repeat(crtIdent) + neg.token.getText() + "\n";

        crtIdent++;
        str += neg.expr.accept(this);//"  ".repeat(crtIdent) + neg.expr.token.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Not not) {
        String str;
        str = "  ".repeat(crtIdent) + not.token.getText() + "\n";

        crtIdent++;
        str += not.expr.accept(this);//"  ".repeat(crtIdent) + not.expr.token.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(While w) {
        String str;
        str = "  ".repeat(crtIdent) + w.token.getText() + "\n";

        crtIdent++;
        str += w.cond.accept(this);//w.cond.token.getText();
        str += w.body.accept(this);//w.body.token.getText();
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Let let) {
        String str;
        str = "  ".repeat(crtIdent) + let.token.getText() + "\n";

        crtIdent++;
        for (Var v : let.vars) {
            str += v.accept(this);
        }
        str += let.body.accept(this);
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Var v) {
        String str;
        str = "  ".repeat(crtIdent) + "local" + "\n";

        crtIdent++;
        str += v.name.accept(this);
        str += "  ".repeat(crtIdent) + v.type.getText() + "\n";
        if (v.value != null) {
            str += v.value.accept(this);
        }
        crtIdent--;

        return str;
    }

    @Override
    public String visit(IsVoid isVoid) {
        String str;
        str = "  ".repeat(crtIdent) + isVoid.token.getText() + "\n";

        crtIdent++;
        str += isVoid.e.accept(this);//isVoid.name.accept(this);//"  ".repeat(crtIdent) + isVoid.name.token.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Program program) {
        String str;
        str = "  ".repeat(crtIdent) + "program" + "\n";

        crtIdent++;
        for (var stmt : program.stmts) {
            str += visit(stmt);
        }
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Block block) {
        String str;
        str = "  ".repeat(crtIdent) + "block" + "\n";

        crtIdent++;
        for (Expression e : block.body) {
            str += e.accept(this);
        }
        crtIdent--;

        return str;
    }

    @Override
    public String visit(New n) {
        String str;
        str = "  ".repeat(crtIdent) + n.token.getText() + "\n";

        crtIdent++;
        str += "  ".repeat(crtIdent) + n.type.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Class c) {
        String str;
        str = "  ".repeat(crtIdent) + c.token.getText() + "\n";

        crtIdent++;
        str += "  ".repeat(crtIdent) + c.name.getText() + "\n";
        Token parent = c.parent;
        if (parent != null) {
            str += "  ".repeat(crtIdent) + parent.getText() + "\n";
        }
        for (Feature f : c.features) {
            str += f.accept(this);
        }
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Method method) {
        String str;
        str = "  ".repeat(crtIdent) + "method" + "\n";

        crtIdent++;
        str += method.name.accept(this);//"  ".repeat(crtIdent) + method.name.token.getText() + "\n";
        for (Formal f : method.formals) {
            str += f.accept(this);
        }
        str += "  ".repeat(crtIdent) + method.type.getText() + "\n";
        str += method.body.accept(this);
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Attribute attribute) {
        String str;
        str = "  ".repeat(crtIdent) + "attribute" + "\n";

        crtIdent++;
        str += attribute.name.accept(this);//"  ".repeat(crtIdent) + attribute.name.token.getText() + "\n";
        str += "  ".repeat(crtIdent) + attribute.type.getText() + "\n";
        Expression e = attribute.value;
        if (e != null) {
            str += e.accept(this);
        }
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Formal formal) {
        String str;
        str = "  ".repeat(crtIdent) + "formal" + "\n";

        crtIdent++;
        str += formal.name.accept(this);//"  ".repeat(crtIdent) + formal.name.token.getText() + "\n";
        str += "  ".repeat(crtIdent) + formal.type.getText() + "\n";
        crtIdent--;

        return str;
    }

    @Override
    public String visit(ExplicitDispatch explDisp) {
        String str;
        str = "  ".repeat(crtIdent) + "." + "\n";

        crtIdent++;
        str += explDisp.entity.accept(this);
        if (explDisp.atType != null) {
            str += "  ".repeat(crtIdent) + explDisp.atType.getText() + "\n";
        }
        str += explDisp.method.accept(this);
        for (Expression param : explDisp.params) {
            str += param.accept(this);
        }
        crtIdent--;

        return str;
    }

    @Override
    public String visit(ImplicitDispatch implDisp) {
        String str;
        str = "  ".repeat(crtIdent) + "implicit dispatch" + "\n";

        crtIdent++;
        str += implDisp.method.accept(this);
        for (Expression param : implDisp.params) {
            str += param.accept(this);
        }
        crtIdent--;

        return str;
    }

    @Override
    public String visit(CaseOpt c) {
        String str;
        str = "  ".repeat(crtIdent) + "case branch" + "\n";

        crtIdent++;
        str += c.name.accept(this);
        str += "  ".repeat(crtIdent) + c.type.getText() + "\n";
        str += c.value.accept(this);
        crtIdent--;

        return str;
    }

    @Override
    public String visit(Case c) {
        String str;
        str = "  ".repeat(crtIdent) + c.token.getText() + "\n";

        crtIdent++;
        str += c.value.accept(this);
        for (CaseOpt op : c.options) {
            str += op.accept(this);
        }
        crtIdent--;

        return str;
    }
}
