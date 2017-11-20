package univ.waseda.weibin.ltlgen.patterns;

import java.util.List;

import univ.waseda.weibin.ltlgen.formula.LTL;

/**
 * @author  Weibin Luo
 * @version Created on 2017/07/16 23:38:42
 */
public class BoundedExistence extends Pattern {
	
	private String property;
	private List<String> scope;
	private LTL ltl;
	private int count = 2;

	public BoundedExistence(String property, List<String> scope, String g3) {
		super(property, scope);
		this.property = property;
		this.scope = scope;
		if (g3 != null && !g3.equals("")) {
			count = Integer.valueOf(g3);
		}
	}
	
	public LTL generateLTL() {
		String ltlScope = null;
		String pattern = "Bounded Existence";
		String ltlFormula = null;
		
		String p = "(" + property + ")";
		String q = "(" + scope.get(0) + ")";
		String r = "(" + scope.get(1) + ")";
		
		if (count == 2) {
			if (q.equals("()") && r.equals("()")) {
				// globally
//				ltlFormula = "(!" + p + "W(" + p + "W(!" + p + "W(" + p + "W[]!" + p + "))))";
				ltlFormula = "(!@P W (@P W (!@P W (@P W []!@P))))";
				ltlFormula = replaceProperty(ltlFormula, p, q, r);
				ltlScope = "Globally";
			} else if (q.equals("()")) {
				// before
				ltlFormula = "<>@R -> ((!@P && !@R) U (@R || ((@P && !@R) U (@R || ((!@P && !@R) U (@R || ((@P && !@R) U (@R || (!@P U @R)))))))))";
				ltlFormula = replaceProperty(ltlFormula, p, q, r);
				ltlScope = "Before R";
			} else if (r.equals("()")) {
				// after
				ltlFormula = "<>@Q -> (!@Q U (@Q && (!@P W (@P W (!@P W (@P W []!@P))))))";
				ltlFormula = replaceProperty(ltlFormula, p, q, r);
				ltlScope = "After Q";
			} else {
				// between or until
				if (r.charAt(r.length() - 2) == '*') {
					// until
					r = r.substring(0, r.length()-2) + ")";
					ltlFormula = "[](@Q -> ((!@P && !@R) U (@R || ((@P && !@R) U (@R || ((!@P && !@R) U (@R || ((@P && !@R) U (@R || (!@P W @R) || []@P)))))))))";
					ltlFormula = replaceProperty(ltlFormula, p, q, r);
					ltlScope = "After Q until R";
				} else {
					//between
					ltlFormula = "[]((@Q && <>@R) -> ((!@P && !@R) U (@R || ((@P && !@R) U (@R || ((!@P && !@R) U (@R || ((@P && !@R) U (@R || (!@P U @R))))))))))";
					ltlFormula = replaceProperty(ltlFormula, p, q, r);
					ltlScope = "Between Q and R";
				}
			}
		} else {
			// error: be count must be 2
			System.out.println("count of BE must be 2");
			return null;
		}
//		System.out.println(ltlFormula);
		ltl = new LTL(pattern, ltlScope, ltlFormula);
		return ltl;
	}
	
	private String replaceProperty(String ltlFormula, String p, String q, String r) {
		String ret = ltlFormula;
		ret = ret.replace("@P", p);
		ret = ret.replace("@Q", q);
		ret = ret.replace("@R", r);
		ret = ret.replace(" ", "");
		return ret;
	}

}
