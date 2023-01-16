package cool.parser;

import cool.compiler.Compiler;
import cool.structures.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.*;
import java.lang.Math;

public class CodeGenVisitor implements ASTVisitor<ST> {
	TypeSymbol crtClass = null;
	int str_const_cnt = 0;
	int int_const_cnt = 0;
	int cnt = 0;
	int tagCnt = 0;
	String filename = "";
	static STGroupFile templates = new STGroupFile("cgen.stg");
	public Map<String, String> strToStrConst = new LinkedHashMap<>();
	public Map<Integer, String> intToIntConst = new LinkedHashMap<>();

	ST constStrs;
	ST constInts;
	ST classesConstStrNames;
	ST prototypesNames;
	ST prototypes;
	ST dispatchTables;
	ST initRoutines;
	ST methods;

	void addStringToConst(String string){
		if (!strToStrConst.containsKey(string)){
			StringBuilder str = new StringBuilder();
			str.append("str_const");
			str.append(str_const_cnt);
			cnt += 1;
			strToStrConst.put(string, str.toString());
		}
	}

	void addIntToConst(int n) {
		Integer number = n;
		if (!intToIntConst.containsKey(number)){
			StringBuilder str = new StringBuilder();
			str.append("int_const");
			str.append(int_const_cnt);
			cnt += 1;
			intToIntConst.put(number, str.toString());
		}
	}

	String addConstStr(String str_tmp) {
		if (strToStrConst.containsKey(str_tmp)) {
			return strToStrConst.get(str_tmp);
		}
		ST tmp;
		tmp = templates.getInstanceOf("constStr");
		tmp.add("orderNum", str_const_cnt);
		tmp.add("tag", TypeSymbol.STRING.tag);
		// size = 3 (antet) + 1 (lungime) + ceil([str.len + 1 (pentru '\0')] / 4 (bytes per word))
		Integer sz = (int) (3 + 1 + Math.ceil(((double)(str_tmp.length() + 1)) / 4));
		tmp.add("size", sz);
		addConstInt(str_tmp.length());
		tmp.add("int_len", intToIntConst.get(str_tmp.length()));
		tmp.add("str", str_tmp);
		constStrs.add("e", tmp);
		// adauga in map string-ul
		addStringToConst(str_tmp);
		str_const_cnt++;
		return strToStrConst.get(str_tmp);
	}

	String addConstInt(Integer n) {
		if (intToIntConst.containsKey(n))
			return intToIntConst.get(n);
		ST tmp;
		tmp = templates.getInstanceOf("constInt");
		tmp.add("orderNum", int_const_cnt);
		tmp.add("tag", TypeSymbol.INT.tag);
		tmp.add("val", n);
		constInts.add("e", tmp);
		addIntToConst(n);
		int_const_cnt++;
		return intToIntConst.get(n);
	}



	@Override
	public ST visit(Id id) {
		ST tmp = templates.getInstanceOf("tabLine");
		if (id.symbol.getName().equals("self")) {
			tmp.add("content", "move    $a0 $s0");
		} else if (((IdSymbol) id.symbol).isAttr) {
			tmp.add("content", "lw      $a0 " + ((IdSymbol)id.symbol).offset + "($s0)");
		} else {
			tmp.add("content", "lw      $a0 " + ((IdSymbol)id.symbol).offset + "($fp)");
		}
		return tmp;
	}

	@Override
	public ST visit(Int intt) {
		ST intST = templates.getInstanceOf("literal");
		intST.add("value", addConstInt(Integer.parseInt(intt.token.getText())));
		return intST;
	}

	@Override
	public ST visit(If iff) {
		ST tmp = templates.getInstanceOf("if");
		tmp.add("crt", tagCnt);
		tagCnt++;
		tmp.add("cond", iff.cond.accept(this));
		tmp.add("thenBranch", iff.thenBranch.accept(this));
		tmp.add("elseBranch", iff.elseBranch.accept(this));
		return tmp;
	}

	@Override
	public ST visit(Str str) {
		ST strST = templates.getInstanceOf("literal");
		strST.add("value", addConstStr(str.token.getText()));
		return strST;
	}

	@Override
	public ST visit(Bool bool) {
		ST boolST = templates.getInstanceOf("literal");
		int val;
		if (bool.token.getText().equals("true")) {
			val = 1;
		} else {
			val = 0;
		}
		boolST.add("value", "bool_const" + val);
		return boolST;
	}

	@Override
	public ST visit(Assign assign) {
		ST expr =  assign.expr.accept(this);
		ST assignST = templates.getInstanceOf("assign");
		assignST.add("expr", expr);
		if (((IdSymbol) assign.id.symbol).isAttr) {
			assignST.add("var", ((IdSymbol) assign.id.symbol).offset + "($s0)");
		} else {
			assignST.add("var", ((IdSymbol) assign.id.symbol).offset + "($fp)");
		}
		//assignST.add("content", "lw      $a0 " + ((IdSymbol)assign.id.symbol).offset + "($s0)");
		return assignST;
	}

	@Override
	public ST visit(Relational rel) {
		return null;
	}

	@Override
	public ST visit(Plus plus) {
		ST tmp = templates.getInstanceOf("plus");
		tmp.add("left", plus.left.accept(this));
		tmp.add("right", plus.right.accept(this));
		return tmp;
	}

	@Override
	public ST visit(Minus minus) {
		ST tmp = templates.getInstanceOf("minus");
		tmp.add("left", minus.left.accept(this));
		tmp.add("right", minus.right.accept(this));
		return tmp;
	}

	@Override
	public ST visit(Mult mult) {
		ST tmp = templates.getInstanceOf("mult");
		tmp.add("left", mult.left.accept(this));
		tmp.add("right", mult.right.accept(this));
		return tmp;
	}

	@Override
	public ST visit(Div div) {
		ST tmp = templates.getInstanceOf("div");
		tmp.add("left", div.left.accept(this));
		tmp.add("right", div.right.accept(this));
		return tmp;
	}

	@Override
	public ST visit(Neg neg) {
		ST tmp = templates.getInstanceOf("neg");
		tmp.add("val", neg.expr.accept(this));
		return tmp;
	}

	@Override
	public ST visit(Not not) {
		ST tmp = templates.getInstanceOf("not");
		tmp.add("crt", tagCnt);
		tagCnt++;
		tmp.add("val", not.expr.accept(this));
		return tmp;
	}

	@Override
	public ST visit(While w) {
		return null;
	}

	@Override
	public ST visit(Let let) {
		ST tmp = templates.getInstanceOf("let");
		tmp.add("localsSize", 4 * let.vars.size());
		int offset = 1;
		ST vars = templates.getInstanceOf("sequence");
		for (Var v : let.vars) {
			ST crtVar = templates.getInstanceOf("localVar");
			crtVar.add("var", v.accept(this));
			crtVar.add("offset", offset * 4);
			offset++;
			vars.add("e", crtVar);
		}
		tmp.add("vars", vars);
		tmp.add("body", let.body.accept(this));
		return tmp;
	}

	@Override
	public ST visit(Var v) {
		ST res;
		if (v.value != null) {
			res = v.value.accept(this);
		} else if (v.type.getText().equals("Int")) {
			res = templates.getInstanceOf("literal");
			res.add("value", intToIntConst.get(0));
		} else if (v.type.getText().equals("String")) {
			res = templates.getInstanceOf("literal");
			res.add("value", strToStrConst.get(""));
		} else if (v.type.getText().equals("Bool")) {
			res = templates.getInstanceOf("literal");
			res.add("value", "bool_const0");
		} else {
			res = templates.getInstanceOf("literal");
			res.add("value", 0);
		}
		return res;
	}

	@Override
	public ST visit(IsVoid isVoid) {
		ST tmp = templates.getInstanceOf("isVoid");
		tmp.add("crt", tagCnt);
		tagCnt++;
		tmp.add("val", isVoid.e.accept(this));
		return tmp;
	}



	void addClassBasicInfo(TypeSymbol cls, ST attrs){
		// adauga numele clasei in constStrs
		//  si in classesConstStrNames
		//  si in prototypesNames (sub forma <clasa>_protObj, <clasa>_init)
		addConstStr(cls.getName());
		classesConstStrNames.add("e",
				templates.getInstanceOf("wordLine").add("name", strToStrConst.get(cls.getName())));
		prototypesNames.add("e",
				templates.getInstanceOf("wordLine").add("name", cls.getName() + "_protObj"));
		prototypesNames.add("e",
				templates.getInstanceOf("wordLine").add("name", cls.getName()+ "_init"));
		// construieste protoObj si adauga-l in prototypes
		addProtoType(cls);
		addDispTable(cls);
		// scrie rutina de init si adaug-o in initRoutines
		if (attrs != null)
			addInitClass(cls, attrs);
		else
			addInitClass(cls);
	}

	void findAttributes(List<IdSymbol> attrs, TypeSymbol typeClass){
		if (typeClass == TypeSymbol.OBJECT) {
			attrs.addAll(typeClass.attrs.values());
		} else {
			findAttributes(attrs, typeClass.parent);
			attrs.addAll(typeClass.attrs.values());
		}
	}

	void addProtoType(TypeSymbol typeClass) {
		ST tmp;
		String attributesString;

		ST  attributes = templates.getInstanceOf("sequence");
		List<IdSymbol> attrs = new LinkedList<>();
		findAttributes(attrs, typeClass);

		if (typeClass == TypeSymbol.INT || typeClass == TypeSymbol.BOOL) {
			attributes.add("e", templates.getInstanceOf("wordLine").add("name", 0));
		} else if (typeClass == TypeSymbol.STRING) {
			attributes.add("e",
					templates.getInstanceOf("wordLine").add("name", "int_const0"));
			attributes.add("e", templates.getInstanceOf("asciiLine").add("name", "\"\""));
			attributes.add("e", templates.getInstanceOf("alignLine").add("name", 2));
		} else {
			for (int i = 0; i < attrs.size(); i++) {
				// pt fiecare atribut iau type si adaug in seq
				 TypeSymbol typeAttr = attrs.get(i).type;
				 if (typeAttr == TypeSymbol.INT) {
					 attributes.add("e",
							 templates.getInstanceOf("wordLine").add("name", "int_const0"));
				 } else if (typeAttr == TypeSymbol.BOOL) {
					 attributes.add("e",
							 templates.getInstanceOf("wordLine").add("name", "bool_const0"));
				 } else if (typeAttr == TypeSymbol.STRING) {
					 attributes.add("e",
							 templates.getInstanceOf("wordLine").add("name", "str_const0"));
				 } else {
					 attributes.add("e", templates.getInstanceOf("wordLine").add("name", "0"));
				 }
			}
		}

		// parcurg attribute si iau tipul recursiv dupa parinti
		tmp = templates.getInstanceOf("prototype");
		tmp.add("className", typeClass.getName());
		tmp.add("tag", typeClass.tag);
		// 3 + nr. attr (+ 1 dc. are val. default)
		Integer nrWords = 3 + attrs.size();
		if (typeClass == TypeSymbol.INT || typeClass == TypeSymbol.BOOL) {
			nrWords++;
		} else if (typeClass == TypeSymbol.STRING) {
			nrWords += 2;
		}
		tmp.add("size", nrWords);
		if (attrs.size() != 0 || typeClass == TypeSymbol.INT ||
				typeClass == TypeSymbol.STRING || typeClass == TypeSymbol.BOOL) {
			tmp.add("attrs", attributes);
		} else {
			tmp.add("attrs", null);
		}
		prototypes.add("e", tmp);

	}

	void addInitClass(TypeSymbol typeClass) {
		ST tmp;
		tmp = templates.getInstanceOf("initClass");
		tmp.add("className", typeClass.getName());
		if (typeClass.parent != null) {
			tmp.add("parentName", typeClass.parent.getName());
		} else {
			tmp.add("parentName", null);
		}
		initRoutines.add("e", tmp);
	}

	void addInitClass(TypeSymbol typeClass, ST attrs) {
		ST tmp;
		tmp = templates.getInstanceOf("initClass");
		tmp.add("className", typeClass.getName());
		if (typeClass.parent != null) {
			tmp.add("parentName", typeClass.parent.getName());
		} else {
			tmp.add("parentName", null);
		}
		tmp.add("attrs", attrs);
		initRoutines.add("e", tmp);
	}
	@Override
	public ST visit(Program program) {
		constStrs = templates.getInstanceOf("sequence");
		constInts = templates.getInstanceOf("sequence");
		classesConstStrNames = templates.getInstanceOf("sequence");;
		prototypesNames = templates.getInstanceOf("sequence");;
		prototypes = templates.getInstanceOf("sequence");;
		dispatchTables = templates.getInstanceOf("sequence");
		initRoutines = templates.getInstanceOf("sequence");
		methods = templates.getInstanceOf("sequence");

		addConstStr("");

		// adauga informatie legata de clasele default (Object, IO, Int, String, Bool)
		addClassBasicInfo(TypeSymbol.OBJECT, null);
		addClassBasicInfo(TypeSymbol.IO, null);
		addClassBasicInfo(TypeSymbol.INT, null);
		addClassBasicInfo(TypeSymbol.STRING, null);
		addClassBasicInfo(TypeSymbol.BOOL, null);

		// TODO: implemntare pentru metodele din clasele default

		// viziteaza clasele
		for (var c : program.stmts) {
			c.accept(this);
		}

		ST programST = templates.getInstanceOf("program");
		programST.add("constStrs", constStrs);
		programST.add("constInts", constInts);
		programST.add("classesConstStrNames", classesConstStrNames);
		programST.add("prototypesNames", prototypesNames);
		programST.add("prototypes", prototypes);
		programST.add("dispatchTables", dispatchTables);
		programST.add("initRoutines", initRoutines);
		programST.add("methods", methods);
		return programST;
	}

	@Override
	public ST visit(Block block) {
		// creez o noua secventa
		ST blockST = templates.getInstanceOf("sequence");
		ST sym = null;
		for (Expression e : block.body) {
			sym = e.accept(this);
			blockST.add("e", sym);
		}
		return blockST;
	}

	@Override
	public ST visit(New n) {
		ST tmp;
		if (!n.type.getText().equals("SELF_TYPE")) {
			tmp = templates.getInstanceOf("new");
			tmp.add("name", n.type.getText());
		} else {
			tmp = templates.getInstanceOf("new_self_type");
		}
		return tmp;
	}

	public void addDispTable(TypeSymbol cls) {
		// adauga metodele in dispatchtable
		ST dispTable = templates.getInstanceOf("sequence");
		dispTable.add("e", cls.getName() + "_dispTab:");
		Map<String, String> methodToClass = new LinkedHashMap<>();
		LinkedList<String> methodNames = new LinkedList<>();
		Stack<TypeSymbol> st = new Stack<>();
		TypeSymbol crt = cls;
		while (crt != null) {
			st.push(crt);
			crt = crt.parent;
		}
		while (!st.isEmpty()) {
			crt = st.pop();
			for (String meth : crt.meths.keySet()) {
				if (!methodToClass.containsKey(meth)) {
					methodToClass.put(meth, crt.getName());
					methodNames.add(meth);
				} else {
					methodToClass.replace(meth, crt.getName());
				}
			}
		}
		int offset = 0;
		for (String meth : methodNames) {
			String clsName = methodToClass.get(meth);
			String entry = clsName + "." + meth;
			dispTable.add("e", templates.getInstanceOf("wordLine").add("name", entry));
			cls.methodOffset.put(meth, offset);
			offset += 4;
		}

		// adauga dispatchTable-ul la dispatchTables
		dispatchTables.add("e", dispTable);
	}

	private Integer backupMethodOff(TypeSymbol cls, String method) {
		Map<String, String> methodToClass = new LinkedHashMap<>();
		LinkedList<String> methodNames = new LinkedList<>();
		Stack<TypeSymbol> st = new Stack<>();
		TypeSymbol crt = cls;
		while (crt != null) {
			st.push(crt);
			crt = crt.parent;
		}
		while (!st.isEmpty()) {
			crt = st.pop();
			for (String meth : crt.meths.keySet()) {
				if (!methodToClass.containsKey(meth)) {
					methodToClass.put(meth, crt.getName());
					methodNames.add(meth);
				} else {
					methodToClass.replace(meth, crt.getName());
				}
			}
		}
		int offset = 0;
		for (String meth : methodNames) {
			if (meth.equals(method)) {
				return offset;
			}
			offset += 4;
		}

		return null;
	}

	@Override
	public ST visit(Class c) {
		TypeSymbol old_self_type = crtClass;
		if (c.scope == null || c.scope.lookup(c.name.getText(), "type") == null)
			return null;
		crtClass = (TypeSymbol) c.scope.lookup(c.name.getText(), "type");

		ST attrs = templates.getInstanceOf("sequence");
		int cnt = 0;

		// viziteaza atributele
		for (Feature f : c.features) {
			if (f instanceof Attribute) {
				ST res = f.accept(this);
				if (res != null) {
					attrs.add("e", res);
					ST auxLine = templates.getInstanceOf("swLine");
					auxLine.add("offset", ((IdSymbol) f.symbol).offset);
					attrs.add("e", auxLine);
					cnt++;
				}
			}
		}

		if (cnt == 0) {
			addClassBasicInfo((TypeSymbol) c.symbol, null);
		} else {
			addClassBasicInfo((TypeSymbol) c.symbol, attrs);
		}

		// viziteaza metodele
		for (Feature m : c.features) {
			if (m instanceof Method) {
				ST res = m.accept(this);
				// adauga ce intoarece metoda.accept(this) in methods
				methods.add("e", res);
			}
		}

		crtClass = old_self_type;

		return null;
	}

	@Override
	public ST visit(Method method) {
		ST meth = templates.getInstanceOf("methodImpl");
		meth.add("className", ((TypeSymbol)((MethodSymbol)method.symbol).parent).getName());
		meth.add("methodName", ((MethodSymbol)method.symbol).getName());
		meth.add("body", method.body.accept(this));
		int nrStackFree = method.formals.size()*4;
		meth.add("cntParams", nrStackFree);
		return meth;
	}

	@Override
	public ST visit(Attribute attribute) {
		ST res = null;
		if (attribute.value != null) {
			res = attribute.value.accept(this);
		}
		// TODO: aici trebuie adaugat cumva si offset-ul ala [ex. sw      $a0 16($s0)]
		return res;
	}

	@Override
	public ST visit(Formal formal) {
		return null;
	}

	@Override
	public ST visit(ExplicitDispatch explDisp) {
		LinkedList<ST> params_list = new LinkedList<>();
		for (Expression p : explDisp.params) {
			ST tmp = templates.getInstanceOf("paramOnStack");
			tmp.add("parameter", p.accept(this));
			params_list.add(tmp);
		}
		Collections.reverse(params_list);
		ST params = templates.getInstanceOf("sequence");
		for (ST p_ST : params_list) {
			params.add("e", p_ST);
		}

		ST tmp;
		tmp = templates.getInstanceOf("dispatchExplicit");
		tmp.add("params", params);
		tmp.add("dispEntity", explDisp.entity.accept(this));
		tmp.add("crt", tagCnt);
		tagCnt++;
		getFilename(explDisp.context);
		tmp.add("fileName", strToStrConst.get(filename));
		tmp.add("crtLine", explDisp.token.getLine());
		if (explDisp.atType != null)
			tmp.add("atType", explDisp.atType.getText());
		Integer off = ((DispSymbol)explDisp.symbol).type.methodOffset.get(explDisp.method.token.getText());
		if (off != null)
			tmp.add("offsetInDispTable", off);
		else
			tmp.add("offsetInDispTable",
					backupMethodOff(((DispSymbol)explDisp.symbol).type, explDisp.method.token.getText()));
		return tmp;
	}

	@Override
	public ST visit(ImplicitDispatch implDisp) {
		LinkedList<ST> params_list = new LinkedList<>();
		for (Expression p : implDisp.params) {
			ST tmp = templates.getInstanceOf("paramOnStack");
			tmp.add("parameter", p.accept(this));
			params_list.add(tmp);
		}
		Collections.reverse(params_list);
		ST params = templates.getInstanceOf("sequence");
		for (ST p_ST : params_list) {
			params.add("e", p_ST);
		}

		ST tmp;
		tmp = templates.getInstanceOf("dispatchImplicit");
		tmp.add("params", params);
		tmp.add("crt", tagCnt);
		tagCnt++;
		getFilename(implDisp.context);
		tmp.add("fileName", strToStrConst.get(filename));
		tmp.add("crtLine", implDisp.token.getLine());
		Integer off = crtClass.methodOffset.get(implDisp.method.token.getText());
		if (off != null)
			tmp.add("offsetInDispTable", off);
		else
			tmp.add("offsetInDispTable", backupMethodOff(crtClass, implDisp.method.token.getText()));

		return tmp;
	}

	@Override
	public ST visit(CaseOpt c) {
		return null;
	}

	@Override
	public ST visit(Case c) {
		return null;
	}

	private void getFilename(ParserRuleContext context) {
		if (!filename.equals(""))
			return;
		ParserRuleContext ctx = context;
		while (! (ctx.getParent() instanceof CoolParser.ProgramContext))
			ctx = ctx.getParent();
		filename = new File(Compiler.fileNames.get(ctx)).getName();
		addConstStr(filename);
	}
}
