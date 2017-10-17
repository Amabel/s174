package univ.waseda.weibin.proppatterns.service;

import com.google.gson.Gson;

import univ.waseda.weibin.proppatterns.model.PatternTemplate;

public class TemplateJsonAnalyzer implements JsonAnalyzer {

	private PatternTemplate patternTemplate;
	private Gson gson;
	private ParamsAnalyzer paramsAnalyzer;
	
	public void analyze(String templateJson) {
		
		gson = new Gson();
		
		patternTemplate = gson.fromJson(templateJson, PatternTemplate.class);
		
		System.out.println(patternTemplate);
		
		// pattern, params
		// analyze params
		paramsAnalyzer = new ParamsAnalyzer();
		paramsAnalyzer.analyze(patternTemplate.getParameters());
		
	}
	
	private void findKeyWords() {
		
	}
	
	private void replaceKeyWords() {
		
	}
	
}
