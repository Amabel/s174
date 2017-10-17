package univ.waseda.weibin.proppatterns.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
		String path = TemplateGraphGenerator.class.getClassLoader().getResource(propFileName).getPath();
		
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
		
		String graphFileName = graphPathProperties.getProperty(pattern);
		String graphFilePath = TemplateGraphGenerator.class.getClassLoader().getResource(graphFileName).getPath();		
		srcFile = new File(graphFilePath);

		// file
		writeXmlFile();
		
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
		System.out.println(pattern);
		switch (pattern) {
		case "Absence":
		case "Existence":
		case "Universality":
		case "Bounded-Existence":
			replaceP();
			break;
		case "Response":
		case "Precedence":
			replaceP();
			replaceS();
		default:
			System.out.println("no matching pattern name");
			break;
		}
		
		// write into new file (dest file)
		
		return null;
	}
	
	private void replaceP() {
		String propertyP = this.params.get(0);
		String xPath = "//responsibilities/name";
		String srcString = "";
		// find P
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(srcFile);
			srcString = ((Element)(document.selectNodes(xPath).get(0))).getText();
			
			System.out.println(srcString);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// replace
		String replacedStr = "";
		if (srcString.equals("P()*")) {
			replacedStr = "P(" + propertyP +")*";
		} else if (srcString.equals("P()")) {
			replacedStr = "P(" + propertyP +")";
		} else {
			replacedStr = srcString.replace("P", propertyP);
		}
		
	}
	
	private void replaceS() {
		String propertyQ = this.params.get(1);
		
		
	}
	
}
