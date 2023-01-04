package cool.parser;

import org.antlr.v4.runtime.Token;

public interface ASTVisitor<T> {
    T visit(Id id);
    T visit(Int intt);
    T visit(If iff);
    T visit(Str str);
    T visit(Bool bool);
    T visit(Assign assign);
    T visit(Relational rel);
    T visit(Plus plus);
    T visit(Minus minus);
    T visit(Mult mult);
    T visit(Div div);
    T visit(Neg neg);
    T visit(Not not);
    T visit(While w);
    T visit(Let let);
    T visit(Var v);
    T visit(IsVoid isVoid);
    T visit(Program program);
    T visit(Block block);
    T visit(New n);
    T visit(Class c);
    T visit(Method method);
    T visit(Attribute attribute);
    T visit(Formal formal);
    T visit(ExplicitDispatch explDisp);
    T visit(ImplicitDispatch implDisp);
    T visit(CaseOpt c);
    T visit(Case c);

    public static void error(Token token, String message) {
        System.err.println("line " + token.getLine()
                + ":" + (token.getCharPositionInLine() + 1)
                + ", " + message);
    }
}
