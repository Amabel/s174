package univ.waseda.weibin.proppatterns.model;

import java.util.List;

public class PatternTemplate {
	
	private String pattern;
	private List<Property>[] params;
	private String[] befores;
	private String[] afters;
	
	public String getPattern() {
		return pattern;
	}
	
	public List<Property>[] getParams() {
		return params;
	}
	public String[] getBefores() {
		return befores;
	}
	public String[] getAfters() {
		return afters;
	}
	
//	@Override
//	public String toString() {
//		String str = this.pattern + ": ";
//		
//		for (Property param : params) {
//			str += param + " ";
//		}
//		
//		str += "\nScope: after: ";
//		for(String af : afters) {
//			str += af + " ";
//		}
//		
//		str += " before: ";
//		for(String be : befores) {
//			str += be + " ";
//		}
//		
//		return str;
//	}
	
	
}
