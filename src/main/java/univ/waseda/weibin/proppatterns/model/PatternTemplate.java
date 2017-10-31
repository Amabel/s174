package univ.waseda.weibin.proppatterns.model;

public class PatternTemplate {
	
	private String pattern;
	private String[] params;
	private String[] befores;
	private String[] afters;
	
	public String getPattern() {
		return pattern;
	}
	
	public String[] getParams() {
		return params;
	}
	public String[] getBefores() {
		return befores;
	}
	public String[] getAfters() {
		return afters;
	}
	
	@Override
	public String toString() {
		String str = this.pattern + ": ";
		
		for (String param : params) {
			str += param + " ";
		}
		
		str += "\nScope: after: ";
		for(String af : afters) {
			str += af + " ";
		}
		
		str += " before: ";
		for(String be : befores) {
			str += be + " ";
		}
		
		return str;
	}
	
	
}
