package univ.waseda.weibin.proppatterns.model;

public class PatternTemplate {
	
	private String pattern;
	private String[] params;
	private String[] before;
	private String[] after;
	
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
		
		str += "\nScope: before: ";
		for(String be : before) {
			str += be + " ";
		}
		str += " after: ";
		for(String af : after) {
			str += af + " ";
		}
		
		return str;
	}
	
	
}
