package univ.waseda.weibin.proppatterns.service;

import java.util.List;

import com.google.gson.Gson;

import univ.waseda.weibin.proppatterns.model.PatternTemplate;

public class TemplateJsonAnalyzer implements JsonAnalyzer {

	private PatternTemplate patternTemplate;
	private Gson gson;
	private ParamsAnalyzer paramsAnalyzer;
	private TemplateGraphGenerator templateGraphGenerator;
	
	public void analyze(String templateJson) {
		
		gson = new Gson();
		
		patternTemplate = gson.fromJson(templateJson, PatternTemplate.class);
		
		System.out.println(patternTemplate);
		
		// pattern, params
		// analyze params
		paramsAnalyzer = new ParamsAnalyzer();
		List<String> replacedParams = paramsAnalyzer.analyze(patternTemplate.getParameters());
		
		// pattern, replacedParams
		// generate pattern graph
		
		templateGraphGenerator = new TemplateGraphGenerator(patternTemplate.getPattern(), replacedParams);
		templateGraphGenerator.generateGraphFile();
		
	}
	
	private void findKeyWords() {
		
	}
	
	private void replaceKeyWords() {
		
	}
	
}
