package univ.waseda.weibin.ltlgen.patterns;

import java.util.List;

import univ.waseda.weibin.ltlgen.formula.LTL;

public class Precedence {
	
	private String propertyP;
	private String propertyS;
	private List<String> scope;
	private LTL ltl;

	public Precedence(String propertyP, String propertyS, List<String> scope) {
		this.propertyP = propertyP;
		this.propertyS = propertyS;
		this.scope = scope;
	}
	
	public LTL generateLTL() {
		String ltlScope = null;
		String pattern = "Precedence";
		String ltlFormula = null;
		
		// analyze list P and S
		String p = "(" + propertyP + ")";
		String s = "(" + propertyS + ")";
		String q = "(" + scope.get(0) + ")";
		String r = "(" + scope.get(1) + ")";
		
		if (q.equals("()") && r.equals("()")) {
			// globally
			ltlFormula = "!@PW@S";
			ltlScope = "Globally";
		} else if (q.equals("()")) {
			// before
			ltlFormula = "<>@R->(!@PU(@S||@R))";
			ltlScope = "Before R";
		} else if (r.equals("()")) {
			// after
			ltlFormula = "[]!@Q||<>(@Q&&(!@PW@S))";
			ltlScope = "After Q";
		} else {
			// between or until
			if (r.charAt(r.length() - 1) == '*') {
				// until
				ltlFormula = "[](@Q&&!@R->(!@PW(@S||@R)))";
				ltlScope = "After Q until R";
			} else {
				//between
				ltlFormula = "[]((@Q&&!@R&&<>@R)->(!@PU(@S||@R)))";
				ltlScope = "Between Q and R";
			} 
		} 
		ltlFormula = replaceProperty(ltlFormula, p, q, r, s);
		ltl = new LTL(pattern, ltlScope, ltlFormula);
		return ltl;
	}
	
	private String replaceProperty(String ltlFormula, String p, String q, String r, String s) {
		String ret = ltlFormula;
		ret = ret.replace("@P", p);
		ret = ret.replace("@Q", q);
		ret = ret.replace("@R", r);
		ret = ret.replace("@S", s);
		ret = ret.replace(" ", "");
		return ret;
	}
}
