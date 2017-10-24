package univ.waseda.weibin.ltlgen.patterns;

import java.util.List;

import univ.waseda.weibin.ltlgen.formula.LTL;

public class Universality extends Pattern {
	
	private String property;
	private List<String> scope;
	private LTL ltl;
	
	public Universality(String property, List<String> scope) {
		super(property, scope);
		this.property = property;
		this.scope = scope;
	}
	
	public LTL generateLTL() {
		String ltlScope = null;
		String pattern = "Universality";
		String ltlFormula = null;
		
		String p = "(" + property + ")";
		String q = "(" + scope.get(0) + ")";
		String r = "(" + scope.get(1) + ")";
		
		if (q.equals("()") && r.equals("()")) {
			// globally
			ltlFormula = "[](" + p + ")";
			ltlScope = "Globally";
		} else if (q.equals("()")) {
			// before
			ltlFormula = "<>" + r + "->(" + p + "U" + r + ")";
			ltlScope = "Before R";
		} else if (r.equals("()")) {
			// after
			ltlFormula = "[](" + q + "->[](" + p + "))";
			ltlScope = "After Q";
		} else {
			// between or until
			if (r.charAt(r.length() - 1) == '*') {
				// until
				ltlFormula = "[](" + q + "&&!" + r + "->(" + p + "W" + r + "))";
				ltlScope = "After Q until R";
			} else {
				//between
				ltlFormula = "[](" + q + "&&!" + r + "&&<>" + r + "->" + p + "U" + r + ")";
				ltlScope = "Between Q and R";
			}
		}
		
//		System.out.println(ltlFormula);
		ltl = new LTL(pattern, ltlScope, ltlFormula);
		return ltl;
	}
}
