package univ.waseda.weibin.proppatterns.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class TemplateGraphGenerator {
	
	File srcFile;
	File destFile;
	String pattern;
	List<String> params;
	
	public TemplateGraphGenerator(String pattern, List<String> params) {
		this.pattern = pattern;
		this.params = params;
	}
	
	public File generateGraphFile() {
		
		Properties graphPathProperties = new Properties();
		String propFileName = "graph-template-path.properties";
		String path = ParamsAnalyzer.class.getClassLoader().getResource(propFileName).getPath();
		
		try {
			FileInputStream in = new FileInputStream(path);
			graphPathProperties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// search graph file using pattern name
		String graphPath = graphPathProperties.getProperty(pattern);
		
		srcFile = new File(graphPath);
		
		System.out.println(srcFile.getPath());
		
		// file
		
		
		return null;
	}
	
	private File writeXmlFile() {
		
		File destFile;
		
		// replace keywords in src file
		try {
			BufferedReader br = new BufferedReader(new FileReader(srcFile));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		switch (pattern) {
//		case "Absence":
//		case "Existence":
//		case "Universality":
//		case "Bounded-Existence":
//			replaceP();
//			break;
//		case "Response":
//		case "Precedence":
//			replaceP();
//			replaceS();
//		default:
//			System.out.println("no matching pattern name");
//			break;
//		}
		
		// write into new file (dest file)
		
		return null;
	}
	
	private void replaceP() {
		String str = this.params.get(0);
		String xPath = "//responsibilities/name";
		
	}
	
	private void replaceS() {
		String str = this.params.get(1);
	}
	
}
