package cool.parser;

import cool.structures.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.lang.Math;

public class CodeGenVisitor implements ASTVisitor<ST>{
	int str_const_cnt = 0;
	int int_const_cnt = 0;
	int cnt = 0;
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

	void  addIntToConst(int n) {
		Integer number = n;
		if (!intToIntConst.containsKey(number)){
			StringBuilder str = new StringBuilder();
			str.append("int_const");
			str.append(int_const_cnt);
			cnt += 1;
			intToIntConst.put(number, str.toString());
		}
	}

	void addConstStr(String str_tmp) {
		if (strToStrConst.containsKey(str_tmp))
			return;
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
	}

	void addConstInt(Integer n) {
		if (intToIntConst.containsKey(n))
			return;
		ST tmp;
		tmp = templates.getInstanceOf("constInt");
		tmp.add("orderNum", int_const_cnt);
		tmp.add("tag", TypeSymbol.INT.tag);
		tmp.add("val", n);
		constInts.add("e", tmp);
		addIntToConst(n);
		int_const_cnt++;
	}



	@Override
	public ST visit(Id id) {
		return null;
	}

	@Override
	public ST visit(Int intt) {
		return null;
	}

	@Override
	public ST visit(If iff) {
		return null;
	}

	@Override
	public ST visit(Str str) {
		return null;
	}

	@Override
	public ST visit(Bool bool) {
		return null;
	}

	@Override
	public ST visit(Assign assign) {
		return null;
	}

	@Override
	public ST visit(Relational rel) {
		return null;
	}

	@Override
	public ST visit(Plus plus) {
		return null;
	}

	@Override
	public ST visit(Minus minus) {
		return null;
	}

	@Override
	public ST visit(Mult mult) {
		return null;
	}

	@Override
	public ST visit(Div div) {
		return null;
	}

	@Override
	public ST visit(Neg neg) {
		return null;
	}

	@Override
	public ST visit(Not not) {
		return null;
	}

	@Override
	public ST visit(While w) {
		return null;
	}

	@Override
	public ST visit(Let let) {
		return null;
	}

	@Override
	public ST visit(Var v) {
		return null;
	}

	@Override
	public ST visit(IsVoid isVoid) {
		return null;
	}



	void addClassBasicInfo(String nameClass){
		addConstStr(nameClass);
		classesConstStrNames.add("e",
				templates.getInstanceOf("wordLine").add("name", strToStrConst.get(nameClass)));
		prototypesNames.add("e",
				templates.getInstanceOf("wordLine").add("name", nameClass + "_protObj"));
		prototypesNames.add("e",
				templates.getInstanceOf("wordLine").add("name", nameClass+ "_init"));
	}

	void findAttributes(List<IdSymbol> attrs, TypeSymbol typeClass){
		if (typeClass == TypeSymbol.OBJECT) {
			attrs.addAll(typeClass.attrs.values());
		} else {
			findAttributes(attrs, typeClass.parent);
			attrs.addAll(typeClass.attrs.values());
		}
	}

	void addProtoType(TypeSymbol typeClass){
		ST tmp;
		String attributesString;

		ST  attributes = templates.getInstanceOf("sequence");
		List<IdSymbol> attrs = new LinkedList<>();
		findAttributes(attrs, typeClass);

		if (typeClass == TypeSymbol.INT || typeClass == TypeSymbol.BOOL) {
			attributes.add("e", templates.getInstanceOf("wordLine").add("name", 0));
		} else if (typeClass == TypeSymbol.STRING) {
			attributes.add("e",
					templates.getInstanceOf("wordLine").add("name", "string_const0"));
			attributes.add("e", templates.getInstanceOf("asciiLine").add("name", "\"\""));
			attributes.add("e", templates.getInstanceOf("allignLine").add("name", 2));
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
		if (attrs.size() != 0) {
			tmp.add("attrs", attributes);
		}
		prototypes.add("e", tmp);

	}

	void addInitClass(TypeSymbol typeClass){
		ST tmp;
		tmp = templates.getInstanceOf("initClass");
		tmp.add("className", typeClass.getName());
		if(typeClass.parent!=null) {
			tmp.add("parentName", typeClass.parent.getName());
		}else{
			tmp.add("parentName", TypeSymbol.OBJECT.getName());
		}
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
		addClassBasicInfo("Object");
		addProtoType(TypeSymbol.OBJECT);
		addDispTable(TypeSymbol.OBJECT);
		addInitClass(TypeSymbol.OBJECT);
		addClassBasicInfo("IO");
		addProtoType(TypeSymbol.IO);
		addDispTable(TypeSymbol.IO);
		addInitClass(TypeSymbol.IO);
		addClassBasicInfo("Int");
		addProtoType(TypeSymbol.INT);
		addDispTable(TypeSymbol.INT);
		addInitClass(TypeSymbol.INT);
		addClassBasicInfo("String");
		addProtoType(TypeSymbol.STRING);
		addDispTable(TypeSymbol.STRING);
		addInitClass(TypeSymbol.STRING);
		addClassBasicInfo("Bool");
		addProtoType(TypeSymbol.BOOL);
		addDispTable(TypeSymbol.BOOL);
		addInitClass(TypeSymbol.BOOL);


		// TODO: implemntare pentru metodele din clasele default
		// TODO: rutinele de initializare ale claselor


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
		return null;
	}

	@Override
	public ST visit(New n) {
		return null;
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
		for (String meth : methodNames) {
			String clsName = methodToClass.get(meth);
			String entry = clsName + "." + meth;
			dispTable.add("e", templates.getInstanceOf("wordLine").add("name", entry));
		}

		// adauga dispatchTable-ul la dispatchTables
		dispatchTables.add("e", dispTable);
	}

	@Override
	public ST visit(Class c) {
		// adauga numele clasei in constStrs
		//  si in classesConstStrNames
		//  si in prototypesNames (sub forma <clasa>_protObj, <clasa>_init)
		addClassBasicInfo(c.name.getText());

		addDispTable((TypeSymbol) c.symbol);

		// viziteaza feature-urile
		for (Feature f : c.features) {
			ST res = f.accept(this);
			if (f instanceof Method) {
				// adauga ce intoarece metoda.accept(this) in methods
				methods.add("e", res);
			}
		}

		// construieste protoObj si adauga-l in prototypes
		addProtoType((TypeSymbol) c.symbol);

		// TODO: scrie rutina de init si adaug-o in initRoutines
		addInitClass((TypeSymbol) c.symbol);
		return null;
	}

	@Override
	public ST visit(Method method) {
		return null;
	}

	@Override
	public ST visit(Attribute attribute) {
		return null;
	}

	@Override
	public ST visit(Formal formal) {
		return null;
	}

	@Override
	public ST visit(ExplicitDispatch explDisp) {
		return null;
	}

	@Override
	public ST visit(ImplicitDispatch implDisp) {
		return null;
	}

	@Override
	public ST visit(CaseOpt c) {
		return null;
	}

	@Override
	public ST visit(Case c) {
		return null;
	}
}
