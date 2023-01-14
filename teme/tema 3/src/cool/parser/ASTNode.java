package cool.parser;

import cool.structures.Scope;
import cool.structures.Symbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.LinkedList;

public abstract class ASTNode {
    protected Token token;
    protected ParserRuleContext context;
    Symbol symbol;
    Scope scope;
    int flag;

    ASTNode(Token token, ParserRuleContext context) {
        this.token = token;
        this.context = context;
        this.symbol = null;
    }

    Token getToken() {
        return token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

class Program extends ASTNode {
    LinkedList<Class> stmts;

    Program(LinkedList<Class> stmts, Token token, ParserRuleContext context) {
        super(token, context);
        this.stmts = stmts;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

abstract class Expression extends ASTNode {
    Expression(Token token, ParserRuleContext context) {
        super(token, context);
    }
}

class Id extends Expression {

    Id(Token token, ParserRuleContext context) {
        super(token, context);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Int extends Expression {
    Int(Token token, ParserRuleContext context) {
        super(token, context);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Str extends Expression {
    Str(Token token, ParserRuleContext context) {
        super(token, context);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Bool extends Expression {
    Bool(Token token, ParserRuleContext context) {
        super(token, context);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}



class Assign extends Expression {
    Id id;
    Expression expr;

    Assign(Id id, Expression expr, Token token, ParserRuleContext context) {
        super(token, context);
        this.id = id;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Relational extends Expression {
    Expression left;
    Expression right;

    Relational(Expression left, Expression right, Token op, ParserRuleContext context) {
        super(op, context);
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Plus extends Expression {
    Expression left;
    Expression right;

    Plus(Expression left, Expression right, Token op, ParserRuleContext context) {
        super(op, context);
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Minus extends Expression {
    Expression left;
    Expression right;

    Minus(Expression left, Expression right, Token op, ParserRuleContext context) {
        super(op, context);
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Mult extends Expression {
    Expression left;
    Expression right;

    Mult(Expression left, Expression right, Token op, ParserRuleContext context) {
        super(op, context);
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Div extends Expression {
    Expression left;
    Expression right;

    Div(Expression left, Expression right, Token op, ParserRuleContext context) {
        super(op, context);
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Neg extends Expression {
    Expression expr;

    Neg(Expression expr, Token op, ParserRuleContext context) {
        super(op, context);
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Not extends Expression {
    Expression expr;

    Not(Expression expr, Token op, ParserRuleContext context) {
        super(op, context);
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Let extends Expression {
    LinkedList<Var> vars;
    Expression body;

    Let(LinkedList<Var> vars,
        Expression body,
        Token start,
        ParserRuleContext context) {
        super(start, context);
        this.vars = vars;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Var extends ASTNode {
    Id name;
    Token type;
    Expression value;

    Var(Token start, Id name, Token type, Expression value, ParserRuleContext context) {
        super(start, context);
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class While extends Expression {
    Expression cond;
    Expression body;

    While(Expression cond,
          Expression body,
          Token start,
          ParserRuleContext context) {
        super(start, context);
        this.cond = cond;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class If extends Expression {
    Expression cond;
    Expression thenBranch;
    Expression elseBranch;

    If(Expression cond,
       Expression thenBranch,
       Expression elseBranch,
       Token start,
       ParserRuleContext context) {
        super(start, context);
        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class IsVoid extends Expression {
    Expression e;

    IsVoid(Expression e, Token start, ParserRuleContext context) {
        super(start, context);
        this.e = e;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Block extends Expression {
    LinkedList<Expression> body;

    Block(LinkedList<Expression> body, Token start, ParserRuleContext context) {
        super(start, context);
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class New extends Expression {
    Token type;

    New(Token start, Token type, ParserRuleContext context) {
        super(start, context);
        this.type = type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Class extends ASTNode {
    Token name;
    Token parent;
    LinkedList<Feature> features;
    Class(Token start, Token name, Token parent, LinkedList<Feature> features, ParserRuleContext context) {
        super(start, context);
        this.name = name;
        this.parent = parent;
        this.features = features;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

abstract class Feature extends ASTNode {
    Feature(Token start, ParserRuleContext context) {
        super(start, context);
    }
}

class Formal extends ASTNode {
    Id name;
    Token type;
    Formal(Token token, Id name, Token type, ParserRuleContext context) {
        super(token, context);
        this.name = name;
        this.type = type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Method extends Feature {
    Id name;
    LinkedList<Formal> formals;
    Token type;
    Expression body;
    Method(Token start,
           Id name,
           LinkedList<Formal> formals,
           Token type,
           Expression body,
           ParserRuleContext context) {
        super(start, context);
        this.name = name;
        this.formals = formals;
        this.type = type;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Attribute extends Feature {
    Id name;
    Token type;
    Expression value;

    Attribute(Token start, Id name, Token type, Expression value, ParserRuleContext context) {
        super(start, context);
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ExplicitDispatch extends Expression {
    Expression entity;
    Token atType;
    Id method;
    LinkedList<Expression> params;

    ExplicitDispatch(Token start,
                     Expression entity,
                     Token atType,
                     Id method,
                     LinkedList<Expression> params,
                     ParserRuleContext context) {
        super(start, context);
        this.entity = entity;
        this.atType = atType;
        this.method = method;
        this.params = params;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ImplicitDispatch extends Expression {
    Id method;
    LinkedList<Expression> params;

    ImplicitDispatch(Token start, Id method, LinkedList<Expression> params, ParserRuleContext context) {
        super(start, context);
        this.method = method;
        this.params = params;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class CaseOpt extends ASTNode {
    Id name;
    Token type;
    Expression value;

    CaseOpt(Token start, Id name, Token type, Expression value, ParserRuleContext context) {
        super(start, context);
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Case extends Expression {
    Expression value;
    LinkedList<CaseOpt> options;

    Case(Token start, Expression value, LinkedList<CaseOpt> options, ParserRuleContext context) {
        super(start, context);
        this.value = value;
        this.options = options;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
