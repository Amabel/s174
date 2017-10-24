package univ.waseda.weibin.ltlgen.patterns;

import java.util.List;

import univ.waseda.weibin.ltlgen.formula.LTL;

public class Response {
	
	private String propertyP;
	private String propertyS;
	private List<String> scope;
	private LTL ltl;

	public Response(String propertyP, String propertyS, List<String> scope) {
		this.propertyP = propertyP;
		this.propertyS = propertyS;
		this.scope = scope;
	}
	
	public LTL generateLTL() {
		String ltlScope = null;
		String pattern = "Response";
		String ltlFormula = null;
		
		// analyze list P and S
		String p = "(" + propertyP + ")";
		String s = "(" + propertyS + ")";
		String q = "(" + scope.get(0) + ")";
		String r = "(" + scope.get(1) + ")";
		
		if (q.equals("()") && r.equals("()")) {
			// globally
			ltlFormula = "[](@P-><>@S)";
			ltlScope = "Globally";
		} else if (q.equals("()")) {
			// before
			ltlFormula = "<>@R->(@P->(!@RU(@S&&!@R)))U@R";
			ltlScope = "Before R";
		} else if (r.equals("()")) {
			// after
			ltlFormula = "[](@Q->[](@P-><>@S))";
			ltlScope = "After Q";
		} else {
			// between or until
			if (r.charAt(r.length() - 1) == '*') {
				// until
				ltlFormula = "[](@Q&&!@R->((@P->(!@RU(@S&&!@R)))W@R)";
				ltlScope = "After Q until R";
			} else {
				//between
				ltlFormula = "[]((@Q&&!@R&&<>@R)->(@P->(!@RU(@S&&!@R)))U@R)";
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
