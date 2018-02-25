package univ.waseda.weibin.proppatterns.service;

import java.io.File;
import java.util.List;

import com.google.gson.Gson;

import univ.waseda.weibin.proppatterns.model.PatternTemplate;

public class TemplateJsonAnalyzer implements JsonAnalyzer {

	private PatternTemplate patternTemplate;
	private Gson gson;
	private ParamsAnalyzer paramsAnalyzer;
	private UMLetTemplateGraphGenerator umlTemplateGraphGenerator;
	private TemplateGraphGenerator templateGraphGenerator;
	private String downloadDirPath;
	private String umletFileName;
	private File graphTemplate;

	public TemplateJsonAnalyzer(String downloadDirPath) {
		this.downloadDirPath = downloadDirPath;
	}
	
	public void analyze(String templateJson) {
		gson = new Gson();
		
		patternTemplate = gson.fromJson(templateJson, PatternTemplate.class);
		System.out.println(patternTemplate);
		// pattern, params
		// analyze params
		paramsAnalyzer = new ParamsAnalyzer(patternTemplate.getParams(), patternTemplate.getAfters(), patternTemplate.getBefores());
		paramsAnalyzer.analyze();
		
		// pattern, replacedParams
		// generate pattern graph
		templateGraphGenerator = new TemplateGraphGenerator(patternTemplate.getPattern(), paramsAnalyzer.getReplacedParams(), paramsAnalyzer.getReplacedAfters(), paramsAnalyzer.getReplacedBefores());
		umlTemplateGraphGenerator = new UMLetTemplateGraphGenerator(patternTemplate.getPattern(), paramsAnalyzer.getReplacedParams(), paramsAnalyzer.getReplacedAfters(), paramsAnalyzer.getReplacedBefores());
		umletFileName = umlTemplateGraphGenerator.generateGraphFile(downloadDirPath).getName();
//		String destPath = webInfPath + "/temps/generated-graphs/";
		
		graphTemplate = templateGraphGenerator.generateGraphFile(downloadDirPath);
		System.out.println("download path: " + downloadDirPath);
		
	}
	
	public File getGraphTemplate() {
		return this.graphTemplate;
	}
	
	public String getUMLetFileName() {
		return umletFileName;
	}
	
	
	private void findKeyWords() {
		
	}
	
	private void replaceKeyWords() {
		
	}
	
}
