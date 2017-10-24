package univ.waseda.weibin.ltlgen.patterns;

import java.util.List;

public abstract class Pattern {
	
	private String property;
	List<String> scope;
	
	public Pattern(String property, List<String> scope) {
		this.property = property;
		this.scope = scope;
	}
	
	private String generateLTL() {
		
		return "";
	}

	
}
