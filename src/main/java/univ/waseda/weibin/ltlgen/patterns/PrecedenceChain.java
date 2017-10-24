package univ.waseda.weibin.ltlgen.patterns;

import java.util.List;

import univ.waseda.weibin.ltlgen.formula.LTL;

/**
 * @author  Weibin Luo
 * @version Created on 2017/07/18 20:24:31
 */
public class PrecedenceChain {
	private String propertyP;
	private String propertyS;
	private String propertyT;
	private List<String> scope;
	private int mode;
	private LTL ltl;

	public PrecedenceChain(String propertyP, String propertyS, String propertyT, List<String> scope, int pattern) {
		this.propertyP = propertyP;
		this.propertyS = propertyS;
		this.propertyT = propertyT;
		this.mode = pattern;
		this.scope = scope;
	}
	
	public LTL generateLTL() {
		String ltlScope = null;
		String pattern = "Precedence Chain";
		String ltlFormula = null;
		
		// analyze list P, S, T
		String p = "(" + propertyP + ")";
		String s = "(" + propertyS + ")";
		String t = "(" + propertyT + ")";
		String q = "(" + scope.get(0) + ")";
		String r = "(" + scope.get(1) + ")";
		if (mode == 1) {
			// S, T precedes P
			if (q.equals("()") && r.equals("()")) {
				// globally
				ltlFormula = "<>@P->(!@PU(@S&&!@P&&X(!@PU@T)))";
				ltlScope = "Globally";
			} else if (q.equals("()")) {
				// before
				ltlFormula = "<>@R->(!@PU(@R||(@S&&!@P&&X(!@PU@T))))";
				ltlScope = "Before R";
			} else if (r.equals("()")) {
				// after
				ltlFormula = "([]!@Q)||(!@QU(@Q&&<>@P->(!@PU(@S&&!@P&&X(!@PU@T))))";
				ltlScope = "After Q";
			} else {
				// between or until
				if (r.charAt(r.length() - 1) == '*') {
					// until
					ltlFormula="[](@Q->(<>@P->(!@PU(@R||(@S&&!@P&&X(!@PU@T))))))";
					ltlScope = "After Q until R";
				} else {
					//between
					ltlFormula = "[]((@Q&&<>@R)->(!@PU(@R||(@S&&!@P&&X(!@PU@T)))))";
					ltlScope = "Between Q and R";
				} 
			} 
		} else if (mode == 2) {
			// P precedes S, T
			if (q.equals("()") && r.equals("()")) {
				// globally
				ltlFormula = "(<>(@S&&X<>@T))->((!@S)U@P))";
				ltlScope = "Globally";
			} else if (q.equals("()")) {
				// before
				ltlFormula = "<>@R->((!(@S&&(!@R)&&X(!@RU(@T&&!@R))))U(@R||@P))";
				ltlScope = "Before R";
			} else if (r.equals("()")) {
				// after
				ltlFormula = "([]!@Q)||((!@Q)U(@Q&&((<>(@S&&X<>@T))->((!@S)U@P)))";
				ltlScope = "After Q";
			} else {
				// between or until
				if (r.charAt(r.length() - 1) == '*') {
					// until
					ltlFormula = "[](@Q->(!(@S&&(!@R)&&X(!@RU(@T&&!@R)))U(@R||@P)||[](!(@S&&X<>@T))))";
					ltlScope = "After Q until R";
				} else {
					//between
					ltlFormula = "[]((@Q&&<>@R)->((!(@S&&(!@R)&&X(!@RU(@T&&!@R))))U(@R||@P)))";
					ltlScope = "Between Q and R";
				} 
			} 
		}
		ltlFormula = replaceProperty(ltlFormula, p, q, r, s, t);
		ltl = new LTL(pattern, ltlScope, ltlFormula);
		return ltl;
	}
	
	private String replaceProperty(String ltlFormula, String p, String q, String r, String s, String t) {
		String ret = ltlFormula;
		ret = ret.replace("@P", p);
		ret = ret.replace("@Q", q);
		ret = ret.replace("@R", r);
		ret = ret.replace("@S", s);
		ret = ret.replace("@T", t);
		ret = ret.replace(" ", "");
		return ret;
	}
}
