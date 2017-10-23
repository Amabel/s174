package univ.waseda.weibin.proppatterns.service;

import java.io.File;
import java.util.List;

import com.google.gson.Gson;

import univ.waseda.weibin.proppatterns.model.PatternTemplate;

public class TemplateJsonAnalyzer implements JsonAnalyzer {

	private PatternTemplate patternTemplate;
	private Gson gson;
	private ParamsAnalyzer paramsAnalyzer;
	private TemplateGraphGenerator templateGraphGenerator;
	private String downloadDirPath;
	private File graphTemplate;

	public TemplateJsonAnalyzer(String downloadDirPath) {
		this.downloadDirPath = downloadDirPath;
	}
	
	public void analyze(String templateJson) {
		
		gson = new Gson();
		
		patternTemplate = gson.fromJson(templateJson, PatternTemplate.class);
		
		// pattern, params
		// analyze params
		paramsAnalyzer = new ParamsAnalyzer();
		List<String> replacedParams = paramsAnalyzer.analyze(patternTemplate.getParameters());
		
		// pattern, replacedParams
		// generate pattern graph
		templateGraphGenerator = new TemplateGraphGenerator(patternTemplate.getPattern(), replacedParams);
		
//		String destPath = webInfPath + "/temps/generated-graphs/";
		
		graphTemplate = templateGraphGenerator.generateGraphFile(downloadDirPath);
		
	}
	
	public File getGraphTemplate() {
		return this.graphTemplate;
	}
	
	
	private void findKeyWords() {
		
	}
	
	private void replaceKeyWords() {
		
	}
	
}
