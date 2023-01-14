// Generated from /home/maria/Desktop/Maria/acs/an IV/CPL/teme/tema 1/Tema1/src/cool/parser/CoolParser.g4 by ANTLR 4.9.2

    package cool.parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CoolParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, WS=2, CLASS=3, TYPE=4, INHERITS=5, LBRACE=6, RBRACE=7, SEMI=8, 
		ID=9, LPAREN=10, COMMA=11, RPAREN=12, COLON=13, ASSIGN=14, RES=15, WHILE=16, 
		LOOP=17, POOL=18, IF=19, THEN=20, ELSE=21, FI=22, NEW=23, ISVOID=24, CASE=25, 
		OF=26, ESAC=27, LET=28, IN=29, NEG=30, NOT=31, BOOL=32, INT=33, STRING=34, 
		MULT=35, DIV=36, PLUS=37, MINUS=38, EQUAL=39, LT=40, LE=41, AT=42, DOT=43;
	public static final int
		RULE_program = 0, RULE_class = 1, RULE_feature = 2, RULE_formal = 3, RULE_var = 4, 
		RULE_caseOpt = 5, RULE_expr = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "class", "feature", "formal", "var", "caseOpt", "expr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ERROR", "WS", "CLASS", "TYPE", "INHERITS", "LBRACE", "RBRACE", 
			"SEMI", "ID", "LPAREN", "COMMA", "RPAREN", "COLON", "ASSIGN", "RES", 
			"WHILE", "LOOP", "POOL", "IF", "THEN", "ELSE", "FI", "NEW", "ISVOID", 
			"CASE", "OF", "ESAC", "LET", "IN", "NEG", "NOT", "BOOL", "INT", "STRING", 
			"MULT", "DIV", "PLUS", "MINUS", "EQUAL", "LT", "LE", "AT", "DOT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CoolParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CoolParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends ParserRuleContext {
		public ClassContext class;
		public List<ClassContext> classes = new ArrayList<ClassContext>();
		public TerminalNode EOF() { return getToken(CoolParser.EOF, 0); }
		public List<ClassContext> class() {
			return getRuleContexts(ClassContext.class);
		}
		public ClassContext class(int i) {
			return getRuleContext(ClassContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(17);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CLASS) {
				{
				{
				setState(14);
				((ProgramContext)_localctx).class = class();
				((ProgramContext)_localctx).classes.add(((ProgramContext)_localctx).class);
				}
				}
				setState(19);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(20);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassContext extends ParserRuleContext {
		public Token name;
		public Token parent;
		public FeatureContext feature;
		public List<FeatureContext> features = new ArrayList<FeatureContext>();
		public TerminalNode CLASS() { return getToken(CoolParser.CLASS, 0); }
		public TerminalNode LBRACE() { return getToken(CoolParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(CoolParser.RBRACE, 0); }
		public TerminalNode SEMI() { return getToken(CoolParser.SEMI, 0); }
		public List<TerminalNode> TYPE() { return getTokens(CoolParser.TYPE); }
		public TerminalNode TYPE(int i) {
			return getToken(CoolParser.TYPE, i);
		}
		public TerminalNode INHERITS() { return getToken(CoolParser.INHERITS, 0); }
		public List<FeatureContext> feature() {
			return getRuleContexts(FeatureContext.class);
		}
		public FeatureContext feature(int i) {
			return getRuleContext(FeatureContext.class,i);
		}
		public ClassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class; }
	}

	public final ClassContext class() throws RecognitionException {
		ClassContext _localctx = new ClassContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_class);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			match(CLASS);
			setState(23);
			((ClassContext)_localctx).name = match(TYPE);
			setState(26);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INHERITS) {
				{
				setState(24);
				match(INHERITS);
				setState(25);
				((ClassContext)_localctx).parent = match(TYPE);
				}
			}

			setState(28);
			match(LBRACE);
			setState(32);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(29);
				((ClassContext)_localctx).feature = feature();
				((ClassContext)_localctx).features.add(((ClassContext)_localctx).feature);
				}
				}
				setState(34);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(35);
			match(RBRACE);
			setState(36);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FeatureContext extends ParserRuleContext {
		public FeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_feature; }
	 
		public FeatureContext() { }
		public void copyFrom(FeatureContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class MethodContext extends FeatureContext {
		public Token name;
		public FormalContext formal;
		public List<FormalContext> formals = new ArrayList<FormalContext>();
		public Token type;
		public TerminalNode LPAREN() { return getToken(CoolParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CoolParser.RPAREN, 0); }
		public TerminalNode COLON() { return getToken(CoolParser.COLON, 0); }
		public TerminalNode LBRACE() { return getToken(CoolParser.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(CoolParser.RBRACE, 0); }
		public TerminalNode SEMI() { return getToken(CoolParser.SEMI, 0); }
		public TerminalNode ID() { return getToken(CoolParser.ID, 0); }
		public TerminalNode TYPE() { return getToken(CoolParser.TYPE, 0); }
		public List<FormalContext> formal() {
			return getRuleContexts(FormalContext.class);
		}
		public FormalContext formal(int i) {
			return getRuleContext(FormalContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(CoolParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CoolParser.COMMA, i);
		}
		public MethodContext(FeatureContext ctx) { copyFrom(ctx); }
	}
	public static class AttrContext extends FeatureContext {
		public Token name;
		public Token type;
		public ExprContext value;
		public TerminalNode COLON() { return getToken(CoolParser.COLON, 0); }
		public TerminalNode SEMI() { return getToken(CoolParser.SEMI, 0); }
		public TerminalNode ID() { return getToken(CoolParser.ID, 0); }
		public TerminalNode TYPE() { return getToken(CoolParser.TYPE, 0); }
		public TerminalNode ASSIGN() { return getToken(CoolParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AttrContext(FeatureContext ctx) { copyFrom(ctx); }
	}

	public final FeatureContext feature() throws RecognitionException {
		FeatureContext _localctx = new FeatureContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_feature);
		int _la;
		try {
			setState(66);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				_localctx = new MethodContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(38);
				((MethodContext)_localctx).name = match(ID);
				setState(39);
				match(LPAREN);
				setState(48);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(40);
					((MethodContext)_localctx).formal = formal();
					((MethodContext)_localctx).formals.add(((MethodContext)_localctx).formal);
					setState(45);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(41);
						match(COMMA);
						setState(42);
						((MethodContext)_localctx).formal = formal();
						((MethodContext)_localctx).formals.add(((MethodContext)_localctx).formal);
						}
						}
						setState(47);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(50);
				match(RPAREN);
				setState(51);
				match(COLON);
				setState(52);
				((MethodContext)_localctx).type = match(TYPE);
				setState(53);
				match(LBRACE);
				setState(54);
				expr(0);
				setState(55);
				match(RBRACE);
				setState(56);
				match(SEMI);
				}
				break;
			case 2:
				_localctx = new AttrContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(58);
				((AttrContext)_localctx).name = match(ID);
				setState(59);
				match(COLON);
				setState(60);
				((AttrContext)_localctx).type = match(TYPE);
				setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ASSIGN) {
					{
					setState(61);
					match(ASSIGN);
					setState(62);
					((AttrContext)_localctx).value = expr(0);
					}
				}

				setState(65);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormalContext extends ParserRuleContext {
		public Token name;
		public Token type;
		public TerminalNode COLON() { return getToken(CoolParser.COLON, 0); }
		public TerminalNode ID() { return getToken(CoolParser.ID, 0); }
		public TerminalNode TYPE() { return getToken(CoolParser.TYPE, 0); }
		public FormalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formal; }
	}

	public final FormalContext formal() throws RecognitionException {
		FormalContext _localctx = new FormalContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_formal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			((FormalContext)_localctx).name = match(ID);
			setState(69);
			match(COLON);
			setState(70);
			((FormalContext)_localctx).type = match(TYPE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarContext extends ParserRuleContext {
		public Token name;
		public Token type;
		public ExprContext value;
		public TerminalNode COLON() { return getToken(CoolParser.COLON, 0); }
		public TerminalNode ID() { return getToken(CoolParser.ID, 0); }
		public TerminalNode TYPE() { return getToken(CoolParser.TYPE, 0); }
		public TerminalNode ASSIGN() { return getToken(CoolParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public VarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var; }
	}

	public final VarContext var() throws RecognitionException {
		VarContext _localctx = new VarContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_var);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			((VarContext)_localctx).name = match(ID);
			setState(73);
			match(COLON);
			setState(74);
			((VarContext)_localctx).type = match(TYPE);
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(75);
				match(ASSIGN);
				setState(76);
				((VarContext)_localctx).value = expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CaseOptContext extends ParserRuleContext {
		public Token name;
		public Token type;
		public ExprContext value;
		public TerminalNode COLON() { return getToken(CoolParser.COLON, 0); }
		public TerminalNode RES() { return getToken(CoolParser.RES, 0); }
		public TerminalNode ID() { return getToken(CoolParser.ID, 0); }
		public TerminalNode TYPE() { return getToken(CoolParser.TYPE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public CaseOptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseOpt; }
	}

	public final CaseOptContext caseOpt() throws RecognitionException {
		CaseOptContext _localctx = new CaseOptContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_caseOpt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			((CaseOptContext)_localctx).name = match(ID);
			setState(80);
			match(COLON);
			setState(81);
			((CaseOptContext)_localctx).type = match(TYPE);
			setState(82);
			match(RES);
			setState(83);
			((CaseOptContext)_localctx).value = expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NewContext extends ExprContext {
		public Token type;
		public TerminalNode NEW() { return getToken(CoolParser.NEW, 0); }
		public TerminalNode TYPE() { return getToken(CoolParser.TYPE, 0); }
		public NewContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class PlusMinusContext extends ExprContext {
		public ExprContext a;
		public Token op;
		public ExprContext b;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(CoolParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(CoolParser.MINUS, 0); }
		public PlusMinusContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class BoolContext extends ExprContext {
		public TerminalNode BOOL() { return getToken(CoolParser.BOOL, 0); }
		public BoolContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class StringContext extends ExprContext {
		public TerminalNode STRING() { return getToken(CoolParser.STRING, 0); }
		public StringContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class IsvoidContext extends ExprContext {
		public ExprContext e;
		public TerminalNode ISVOID() { return getToken(CoolParser.ISVOID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public IsvoidContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class WhileContext extends ExprContext {
		public ExprContext cond;
		public ExprContext body;
		public TerminalNode WHILE() { return getToken(CoolParser.WHILE, 0); }
		public TerminalNode LOOP() { return getToken(CoolParser.LOOP, 0); }
		public TerminalNode POOL() { return getToken(CoolParser.POOL, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public WhileContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class ImplicitDispatchContext extends ExprContext {
		public Token method;
		public ExprContext expr;
		public List<ExprContext> params = new ArrayList<ExprContext>();
		public TerminalNode LPAREN() { return getToken(CoolParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CoolParser.RPAREN, 0); }
		public TerminalNode ID() { return getToken(CoolParser.ID, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(CoolParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CoolParser.COMMA, i);
		}
		public ImplicitDispatchContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class IntContext extends ExprContext {
		public TerminalNode INT() { return getToken(CoolParser.INT, 0); }
		public IntContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class NegContext extends ExprContext {
		public ExprContext e;
		public TerminalNode NEG() { return getToken(CoolParser.NEG, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NegContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class NotContext extends ExprContext {
		public ExprContext e;
		public TerminalNode NOT() { return getToken(CoolParser.NOT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NotContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class ParenContext extends ExprContext {
		public ExprContext e;
		public TerminalNode LPAREN() { return getToken(CoolParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CoolParser.RPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParenContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class MultDivContext extends ExprContext {
		public ExprContext a;
		public Token op;
		public ExprContext b;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MULT() { return getToken(CoolParser.MULT, 0); }
		public TerminalNode DIV() { return getToken(CoolParser.DIV, 0); }
		public MultDivContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class ExplicitDispatchContext extends ExprContext {
		public ExprContext entity;
		public Token type;
		public Token method;
		public ExprContext expr;
		public List<ExprContext> params = new ArrayList<ExprContext>();
		public TerminalNode DOT() { return getToken(CoolParser.DOT, 0); }
		public TerminalNode LPAREN() { return getToken(CoolParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CoolParser.RPAREN, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ID() { return getToken(CoolParser.ID, 0); }
		public TerminalNode AT() { return getToken(CoolParser.AT, 0); }
		public TerminalNode TYPE() { return getToken(CoolParser.TYPE, 0); }
		public List<TerminalNode> COMMA() { return getTokens(CoolParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CoolParser.COMMA, i);
		}
		public ExplicitDispatchContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class BlockContext extends ExprContext {
		public ExprContext expr;
		public List<ExprContext> body = new ArrayList<ExprContext>();
		public TerminalNode LBRACE() { return getToken(CoolParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(CoolParser.RBRACE, 0); }
		public List<TerminalNode> SEMI() { return getTokens(CoolParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(CoolParser.SEMI, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public BlockContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class LetContext extends ExprContext {
		public VarContext var;
		public List<VarContext> vars = new ArrayList<VarContext>();
		public ExprContext body;
		public TerminalNode LET() { return getToken(CoolParser.LET, 0); }
		public TerminalNode IN() { return getToken(CoolParser.IN, 0); }
		public List<VarContext> var() {
			return getRuleContexts(VarContext.class);
		}
		public VarContext var(int i) {
			return getRuleContext(VarContext.class,i);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(CoolParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CoolParser.COMMA, i);
		}
		public LetContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class RelationalContext extends ExprContext {
		public ExprContext a;
		public Token op;
		public ExprContext b;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode EQUAL() { return getToken(CoolParser.EQUAL, 0); }
		public TerminalNode LT() { return getToken(CoolParser.LT, 0); }
		public TerminalNode LE() { return getToken(CoolParser.LE, 0); }
		public RelationalContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class IdContext extends ExprContext {
		public TerminalNode ID() { return getToken(CoolParser.ID, 0); }
		public IdContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class IfContext extends ExprContext {
		public ExprContext cond;
		public ExprContext thenBranch;
		public ExprContext elseBranch;
		public TerminalNode IF() { return getToken(CoolParser.IF, 0); }
		public TerminalNode THEN() { return getToken(CoolParser.THEN, 0); }
		public TerminalNode ELSE() { return getToken(CoolParser.ELSE, 0); }
		public TerminalNode FI() { return getToken(CoolParser.FI, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public IfContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class CaseContext extends ExprContext {
		public ExprContext value;
		public CaseOptContext caseOpt;
		public List<CaseOptContext> options = new ArrayList<CaseOptContext>();
		public TerminalNode CASE() { return getToken(CoolParser.CASE, 0); }
		public TerminalNode OF() { return getToken(CoolParser.OF, 0); }
		public TerminalNode ESAC() { return getToken(CoolParser.ESAC, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<TerminalNode> SEMI() { return getTokens(CoolParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(CoolParser.SEMI, i);
		}
		public List<CaseOptContext> caseOpt() {
			return getRuleContexts(CaseOptContext.class);
		}
		public CaseOptContext caseOpt(int i) {
			return getRuleContext(CaseOptContext.class,i);
		}
		public CaseContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class AssignContext extends ExprContext {
		public Token name;
		public ExprContext value;
		public TerminalNode ASSIGN() { return getToken(CoolParser.ASSIGN, 0); }
		public TerminalNode ID() { return getToken(CoolParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AssignContext(ExprContext ctx) { copyFrom(ctx); }
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				_localctx = new WhileContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(86);
				match(WHILE);
				setState(87);
				((WhileContext)_localctx).cond = expr(0);
				setState(88);
				match(LOOP);
				setState(89);
				((WhileContext)_localctx).body = expr(0);
				setState(90);
				match(POOL);
				}
				break;
			case 2:
				{
				_localctx = new IfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(92);
				match(IF);
				setState(93);
				((IfContext)_localctx).cond = expr(0);
				setState(94);
				match(THEN);
				setState(95);
				((IfContext)_localctx).thenBranch = expr(0);
				setState(96);
				match(ELSE);
				setState(97);
				((IfContext)_localctx).elseBranch = expr(0);
				setState(98);
				match(FI);
				}
				break;
			case 3:
				{
				_localctx = new NewContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(100);
				match(NEW);
				setState(101);
				((NewContext)_localctx).type = match(TYPE);
				}
				break;
			case 4:
				{
				_localctx = new IsvoidContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(102);
				match(ISVOID);
				setState(103);
				((IsvoidContext)_localctx).e = expr(17);
				}
				break;
			case 5:
				{
				_localctx = new CaseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(104);
				match(CASE);
				setState(105);
				((CaseContext)_localctx).value = expr(0);
				setState(106);
				match(OF);
				setState(110); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(107);
					((CaseContext)_localctx).caseOpt = caseOpt();
					((CaseContext)_localctx).options.add(((CaseContext)_localctx).caseOpt);
					setState(108);
					match(SEMI);
					}
					}
					setState(112); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==ID );
				setState(114);
				match(ESAC);
				}
				break;
			case 6:
				{
				_localctx = new BlockContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(116);
				match(LBRACE);
				setState(120); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(117);
					((BlockContext)_localctx).expr = expr(0);
					((BlockContext)_localctx).body.add(((BlockContext)_localctx).expr);
					setState(118);
					match(SEMI);
					}
					}
					setState(122); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << ID) | (1L << LPAREN) | (1L << WHILE) | (1L << IF) | (1L << NEW) | (1L << ISVOID) | (1L << CASE) | (1L << LET) | (1L << NEG) | (1L << NOT) | (1L << BOOL) | (1L << INT) | (1L << STRING))) != 0) );
				setState(124);
				match(RBRACE);
				}
				break;
			case 7:
				{
				_localctx = new ImplicitDispatchContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(126);
				((ImplicitDispatchContext)_localctx).method = match(ID);
				setState(127);
				match(LPAREN);
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << ID) | (1L << LPAREN) | (1L << WHILE) | (1L << IF) | (1L << NEW) | (1L << ISVOID) | (1L << CASE) | (1L << LET) | (1L << NEG) | (1L << NOT) | (1L << BOOL) | (1L << INT) | (1L << STRING))) != 0)) {
					{
					setState(128);
					((ImplicitDispatchContext)_localctx).expr = expr(0);
					((ImplicitDispatchContext)_localctx).params.add(((ImplicitDispatchContext)_localctx).expr);
					setState(133);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(129);
						match(COMMA);
						setState(130);
						((ImplicitDispatchContext)_localctx).expr = expr(0);
						((ImplicitDispatchContext)_localctx).params.add(((ImplicitDispatchContext)_localctx).expr);
						}
						}
						setState(135);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(138);
				match(RPAREN);
				}
				break;
			case 8:
				{
				_localctx = new LetContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(139);
				match(LET);
				setState(140);
				((LetContext)_localctx).var = var();
				((LetContext)_localctx).vars.add(((LetContext)_localctx).var);
				setState(145);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(141);
					match(COMMA);
					setState(142);
					((LetContext)_localctx).var = var();
					((LetContext)_localctx).vars.add(((LetContext)_localctx).var);
					}
					}
					setState(147);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(148);
				match(IN);
				setState(149);
				((LetContext)_localctx).body = expr(12);
				}
				break;
			case 9:
				{
				_localctx = new NegContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(151);
				match(NEG);
				setState(152);
				((NegContext)_localctx).e = expr(11);
				}
				break;
			case 10:
				{
				_localctx = new NotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(153);
				match(NOT);
				setState(154);
				((NotContext)_localctx).e = expr(7);
				}
				break;
			case 11:
				{
				_localctx = new AssignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(155);
				((AssignContext)_localctx).name = match(ID);
				setState(156);
				match(ASSIGN);
				setState(157);
				((AssignContext)_localctx).value = expr(6);
				}
				break;
			case 12:
				{
				_localctx = new IdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(158);
				match(ID);
				}
				break;
			case 13:
				{
				_localctx = new ParenContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(159);
				match(LPAREN);
				setState(160);
				((ParenContext)_localctx).e = expr(0);
				setState(161);
				match(RPAREN);
				}
				break;
			case 14:
				{
				_localctx = new BoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(163);
				match(BOOL);
				}
				break;
			case 15:
				{
				_localctx = new IntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(164);
				match(INT);
				}
				break;
			case 16:
				{
				_localctx = new StringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(165);
				match(STRING);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(198);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(196);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
					case 1:
						{
						_localctx = new MultDivContext(new ExprContext(_parentctx, _parentState));
						((MultDivContext)_localctx).a = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(168);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(169);
						((MultDivContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MULT || _la==DIV) ) {
							((MultDivContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(170);
						((MultDivContext)_localctx).b = expr(11);
						}
						break;
					case 2:
						{
						_localctx = new PlusMinusContext(new ExprContext(_parentctx, _parentState));
						((PlusMinusContext)_localctx).a = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(171);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(172);
						((PlusMinusContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((PlusMinusContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(173);
						((PlusMinusContext)_localctx).b = expr(10);
						}
						break;
					case 3:
						{
						_localctx = new RelationalContext(new ExprContext(_parentctx, _parentState));
						((RelationalContext)_localctx).a = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(174);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(175);
						((RelationalContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUAL) | (1L << LT) | (1L << LE))) != 0)) ) {
							((RelationalContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(176);
						((RelationalContext)_localctx).b = expr(9);
						}
						break;
					case 4:
						{
						_localctx = new ExplicitDispatchContext(new ExprContext(_parentctx, _parentState));
						((ExplicitDispatchContext)_localctx).entity = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(177);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(180);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==AT) {
							{
							setState(178);
							match(AT);
							setState(179);
							((ExplicitDispatchContext)_localctx).type = match(TYPE);
							}
						}

						setState(182);
						match(DOT);
						setState(183);
						((ExplicitDispatchContext)_localctx).method = match(ID);
						setState(184);
						match(LPAREN);
						setState(193);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << ID) | (1L << LPAREN) | (1L << WHILE) | (1L << IF) | (1L << NEW) | (1L << ISVOID) | (1L << CASE) | (1L << LET) | (1L << NEG) | (1L << NOT) | (1L << BOOL) | (1L << INT) | (1L << STRING))) != 0)) {
							{
							setState(185);
							((ExplicitDispatchContext)_localctx).expr = expr(0);
							((ExplicitDispatchContext)_localctx).params.add(((ExplicitDispatchContext)_localctx).expr);
							setState(190);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==COMMA) {
								{
								{
								setState(186);
								match(COMMA);
								setState(187);
								((ExplicitDispatchContext)_localctx).expr = expr(0);
								((ExplicitDispatchContext)_localctx).params.add(((ExplicitDispatchContext)_localctx).expr);
								}
								}
								setState(192);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(195);
						match(RPAREN);
						}
						break;
					}
					} 
				}
				setState(200);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 6:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		case 2:
			return precpred(_ctx, 8);
		case 3:
			return precpred(_ctx, 15);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3-\u00cc\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\7\2\22\n\2\f\2\16\2"+
		"\25\13\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3\35\n\3\3\3\3\3\7\3!\n\3\f\3\16\3"+
		"$\13\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\7\4.\n\4\f\4\16\4\61\13\4\5\4\63"+
		"\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4B\n\4\3\4"+
		"\5\4E\n\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\5\6P\n\6\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\6\bq\n\b\r\b\16\br\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\6\b{\n\b\r\b\16\b|\3\b\3\b\3\b\3\b\3\b\3\b\3\b\7\b\u0086"+
		"\n\b\f\b\16\b\u0089\13\b\5\b\u008b\n\b\3\b\3\b\3\b\3\b\3\b\7\b\u0092\n"+
		"\b\f\b\16\b\u0095\13\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u00a9\n\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\5\b\u00b7\n\b\3\b\3\b\3\b\3\b\3\b\3\b\7\b\u00bf\n\b\f"+
		"\b\16\b\u00c2\13\b\5\b\u00c4\n\b\3\b\7\b\u00c7\n\b\f\b\16\b\u00ca\13\b"+
		"\3\b\2\3\16\t\2\4\6\b\n\f\16\2\5\3\2%&\3\2\'(\3\2)+\2\u00e7\2\23\3\2\2"+
		"\2\4\30\3\2\2\2\6D\3\2\2\2\bF\3\2\2\2\nJ\3\2\2\2\fQ\3\2\2\2\16\u00a8\3"+
		"\2\2\2\20\22\5\4\3\2\21\20\3\2\2\2\22\25\3\2\2\2\23\21\3\2\2\2\23\24\3"+
		"\2\2\2\24\26\3\2\2\2\25\23\3\2\2\2\26\27\7\2\2\3\27\3\3\2\2\2\30\31\7"+
		"\5\2\2\31\34\7\6\2\2\32\33\7\7\2\2\33\35\7\6\2\2\34\32\3\2\2\2\34\35\3"+
		"\2\2\2\35\36\3\2\2\2\36\"\7\b\2\2\37!\5\6\4\2 \37\3\2\2\2!$\3\2\2\2\""+
		" \3\2\2\2\"#\3\2\2\2#%\3\2\2\2$\"\3\2\2\2%&\7\t\2\2&\'\7\n\2\2\'\5\3\2"+
		"\2\2()\7\13\2\2)\62\7\f\2\2*/\5\b\5\2+,\7\r\2\2,.\5\b\5\2-+\3\2\2\2.\61"+
		"\3\2\2\2/-\3\2\2\2/\60\3\2\2\2\60\63\3\2\2\2\61/\3\2\2\2\62*\3\2\2\2\62"+
		"\63\3\2\2\2\63\64\3\2\2\2\64\65\7\16\2\2\65\66\7\17\2\2\66\67\7\6\2\2"+
		"\678\7\b\2\289\5\16\b\29:\7\t\2\2:;\7\n\2\2;E\3\2\2\2<=\7\13\2\2=>\7\17"+
		"\2\2>A\7\6\2\2?@\7\20\2\2@B\5\16\b\2A?\3\2\2\2AB\3\2\2\2BC\3\2\2\2CE\7"+
		"\n\2\2D(\3\2\2\2D<\3\2\2\2E\7\3\2\2\2FG\7\13\2\2GH\7\17\2\2HI\7\6\2\2"+
		"I\t\3\2\2\2JK\7\13\2\2KL\7\17\2\2LO\7\6\2\2MN\7\20\2\2NP\5\16\b\2OM\3"+
		"\2\2\2OP\3\2\2\2P\13\3\2\2\2QR\7\13\2\2RS\7\17\2\2ST\7\6\2\2TU\7\21\2"+
		"\2UV\5\16\b\2V\r\3\2\2\2WX\b\b\1\2XY\7\22\2\2YZ\5\16\b\2Z[\7\23\2\2[\\"+
		"\5\16\b\2\\]\7\24\2\2]\u00a9\3\2\2\2^_\7\25\2\2_`\5\16\b\2`a\7\26\2\2"+
		"ab\5\16\b\2bc\7\27\2\2cd\5\16\b\2de\7\30\2\2e\u00a9\3\2\2\2fg\7\31\2\2"+
		"g\u00a9\7\6\2\2hi\7\32\2\2i\u00a9\5\16\b\23jk\7\33\2\2kl\5\16\b\2lp\7"+
		"\34\2\2mn\5\f\7\2no\7\n\2\2oq\3\2\2\2pm\3\2\2\2qr\3\2\2\2rp\3\2\2\2rs"+
		"\3\2\2\2st\3\2\2\2tu\7\35\2\2u\u00a9\3\2\2\2vz\7\b\2\2wx\5\16\b\2xy\7"+
		"\n\2\2y{\3\2\2\2zw\3\2\2\2{|\3\2\2\2|z\3\2\2\2|}\3\2\2\2}~\3\2\2\2~\177"+
		"\7\t\2\2\177\u00a9\3\2\2\2\u0080\u0081\7\13\2\2\u0081\u008a\7\f\2\2\u0082"+
		"\u0087\5\16\b\2\u0083\u0084\7\r\2\2\u0084\u0086\5\16\b\2\u0085\u0083\3"+
		"\2\2\2\u0086\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088"+
		"\u008b\3\2\2\2\u0089\u0087\3\2\2\2\u008a\u0082\3\2\2\2\u008a\u008b\3\2"+
		"\2\2\u008b\u008c\3\2\2\2\u008c\u00a9\7\16\2\2\u008d\u008e\7\36\2\2\u008e"+
		"\u0093\5\n\6\2\u008f\u0090\7\r\2\2\u0090\u0092\5\n\6\2\u0091\u008f\3\2"+
		"\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094"+
		"\u0096\3\2\2\2\u0095\u0093\3\2\2\2\u0096\u0097\7\37\2\2\u0097\u0098\5"+
		"\16\b\16\u0098\u00a9\3\2\2\2\u0099\u009a\7 \2\2\u009a\u00a9\5\16\b\r\u009b"+
		"\u009c\7!\2\2\u009c\u00a9\5\16\b\t\u009d\u009e\7\13\2\2\u009e\u009f\7"+
		"\20\2\2\u009f\u00a9\5\16\b\b\u00a0\u00a9\7\13\2\2\u00a1\u00a2\7\f\2\2"+
		"\u00a2\u00a3\5\16\b\2\u00a3\u00a4\7\16\2\2\u00a4\u00a9\3\2\2\2\u00a5\u00a9"+
		"\7\"\2\2\u00a6\u00a9\7#\2\2\u00a7\u00a9\7$\2\2\u00a8W\3\2\2\2\u00a8^\3"+
		"\2\2\2\u00a8f\3\2\2\2\u00a8h\3\2\2\2\u00a8j\3\2\2\2\u00a8v\3\2\2\2\u00a8"+
		"\u0080\3\2\2\2\u00a8\u008d\3\2\2\2\u00a8\u0099\3\2\2\2\u00a8\u009b\3\2"+
		"\2\2\u00a8\u009d\3\2\2\2\u00a8\u00a0\3\2\2\2\u00a8\u00a1\3\2\2\2\u00a8"+
		"\u00a5\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a7\3\2\2\2\u00a9\u00c8\3\2"+
		"\2\2\u00aa\u00ab\f\f\2\2\u00ab\u00ac\t\2\2\2\u00ac\u00c7\5\16\b\r\u00ad"+
		"\u00ae\f\13\2\2\u00ae\u00af\t\3\2\2\u00af\u00c7\5\16\b\f\u00b0\u00b1\f"+
		"\n\2\2\u00b1\u00b2\t\4\2\2\u00b2\u00c7\5\16\b\13\u00b3\u00b6\f\21\2\2"+
		"\u00b4\u00b5\7,\2\2\u00b5\u00b7\7\6\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7"+
		"\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00b9\7-\2\2\u00b9\u00ba\7\13\2\2\u00ba"+
		"\u00c3\7\f\2\2\u00bb\u00c0\5\16\b\2\u00bc\u00bd\7\r\2\2\u00bd\u00bf\5"+
		"\16\b\2\u00be\u00bc\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be\3\2\2\2\u00c0"+
		"\u00c1\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c3\u00bb\3\2"+
		"\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c7\7\16\2\2\u00c6"+
		"\u00aa\3\2\2\2\u00c6\u00ad\3\2\2\2\u00c6\u00b0\3\2\2\2\u00c6\u00b3\3\2"+
		"\2\2\u00c7\u00ca\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9"+
		"\17\3\2\2\2\u00ca\u00c8\3\2\2\2\25\23\34\"/\62ADOr|\u0087\u008a\u0093"+
		"\u00a8\u00b6\u00c0\u00c3\u00c6\u00c8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}