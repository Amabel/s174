package univ.waseda.weibin.proppatterns.model;

public class PatternTemplate {
	
	private String pattern;
	private String[] params;
	
	public String getPattern() {
		return pattern;
	}
	public String[] getParameters() {
		return params;
	}
	
	@Override
	public String toString() {
		String str = this.pattern + ": ";
		
		for (String param : params) {
			str += param + " ";
		}
		
		return str;
	}
	
	
}
