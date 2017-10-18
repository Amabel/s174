package univ.waseda.weibin.proppatterns.service;

import java.util.List;

import com.google.gson.Gson;

import univ.waseda.weibin.proppatterns.model.PatternTemplate;

public class TemplateJsonAnalyzer implements JsonAnalyzer {

	private PatternTemplate patternTemplate;
	private Gson gson;
	private ParamsAnalyzer paramsAnalyzer;
	private TemplateGraphGenerator templateGraphGenerator;
	private String webInfPath;

	public TemplateJsonAnalyzer(String webInfPath) {
		this.webInfPath = webInfPath;
	}
	
	public void analyze(String templateJson) {
		
		gson = new Gson();
		
		patternTemplate = gson.fromJson(templateJson, PatternTemplate.class);
		
		System.out.println("TemplateJsonAnalyzer: 27: " + patternTemplate);
		
		// pattern, params
		// analyze params
		paramsAnalyzer = new ParamsAnalyzer();
		List<String> replacedParams = paramsAnalyzer.analyze(patternTemplate.getParameters());
		
		// pattern, replacedParams
		// generate pattern graph
		
		templateGraphGenerator = new TemplateGraphGenerator(patternTemplate.getPattern(), replacedParams);
		
		String destPath = webInfPath + "/temps/generated-graphs/";
		
		templateGraphGenerator.generateGraphFile(destPath);
		
	}
	
	private void findKeyWords() {
		
	}
	
	private void replaceKeyWords() {
		
	}
	
}
