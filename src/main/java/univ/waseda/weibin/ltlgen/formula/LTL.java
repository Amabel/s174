package univ.waseda.weibin.ltlgen.formula;
/**
 * @author  Weibin Luo
 * @version Created on 2017/07/16 23:07:36
 */
public class LTL {
	String pattern;
	String scope;
	String formula;
	
	public LTL(String pattern, String scope, String formula) {
		this.pattern = pattern;
		this.scope = scope;
		this.formula = formula;
	}
	
	public String getPattern() {
		return pattern;
	}

	public String getScope() {
		return scope;
	}

	public String getFomula() {
		return formula;
	}
	
	@Override
	public String toString() {
		return "pattern: " + this.pattern + " scope: " + this.scope + " formula: " + this.formula;
		
	}
}
