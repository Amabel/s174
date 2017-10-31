package univ.waseda.weibin.proppatterns.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class TemplateGraphGenerator {
	
	File srcFile;
	File destFile;
	String pattern;
	List<String> params;
	String after;
	String before; 
	Document document;
	
	public TemplateGraphGenerator(String pattern, List<String> params, String after, String before) {
		this.pattern = pattern;
		this.params = params;
		this.after = after;
		this.before = before;
		System.out.println(before);
	}
	
	public File generateGraphFile(String destPath) {
		// get props of graph template paths
		Properties graphPathProperties = new Properties();
		String propFileName = "graph-template-path.properties";
		String path = TemplateGraphGenerator.class.getClassLoader().getResource(propFileName).getPath();
//		System.out.println(path);
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
		return writeXmlFile(destPath);
	}
	
	private File writeXmlFile(String destPath) {
		// replace before after
		replaceProperties();
		replaceAfter();
		replaceBefore();
		// write into new file (dest file)
		File destFile = writeDocumentToFile(destPath);
		return destFile;
	}
	
	private File writeDocumentToFile(String destDirPath) {
		
		// create dir
		File destDir = new File(destDirPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		
		// write document to dest file
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String destFileName = "prop-pattern-" + df.format(new Date()) + ".z151";
        String destFilePath = destDirPath + destFileName;
        File destFile = new File(destFilePath);
		
		OutputFormat format = OutputFormat.createPrettyPrint();  
        format.setEncoding("UTF-8");
        XMLWriter writer;
		try {
			writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(destFilePath)), format);
	        writer.write(document);  
	        writer.close();  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return destFile;
	}
	
	private void replaceProperties() {
		String xPath = "//responsibilities/name";
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(srcFile);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// get nodes
		List<Node> nodes = document.selectNodes(xPath);
		switch (nodes.size()) {
		case 3:
			replaceT(nodes);
		case 2:
			replaceS(nodes);
		case 1:
			replaceP(nodes);
			break;
		default:
			System.out.println("Error: incorrect number of node: " + nodes.size());
			break;
		}
	}
	
	private void replaceP(List<Node> nodes) {
		String propertyP = this.params.get(0);
		String srcString = nodes.get(0).getText();
		// replace
		String replacedStr = "";
		if (srcString.equals("P()*")) {
			replacedStr = "P(" + propertyP +")*";
		} else if (srcString.equals("P()")) {
			replacedStr = "P(" + propertyP +")";
		} else {
			replacedStr = srcString.replace("P", propertyP);
		}
		nodes.get(0).setText(replacedStr);
		
	}
	
	private void replaceS(List<Node> nodes) {
		String propertyS = this.params.get(1);
		String srcString = nodes.get(1).getText();
		// replace
		String replacedStr = "";
		if (srcString.equals("S()*")) {
			replacedStr = "S(" + propertyS +")*";
		} else if (srcString.equals("S()")) {
			replacedStr = "S(" + propertyS +")";
		} else {
			replacedStr = srcString.replace("S", propertyS);
		}
		nodes.get(1).setText(replacedStr);
	}
	
	private void replaceT(List<Node> nodes) {
		String propertyT = this.params.get(2);
		String srcString = nodes.get(2).getText();
		
//		nodes.get(2).setText(replacedStr);
		
	}
	
	private void replaceAfter() {
		String xPathStartPointName = "//ucmMaps/nodes[@*[name()='xsi:type']='StartPoint']/name";
		document.selectNodes(xPathStartPointName).get(0).setText(after);
	}
	
	private void replaceBefore() {		
		String xPathEndPointName = "//ucmMaps/nodes[@*[name()='xsi:type']='EndPoint']/name";
		document.selectNodes(xPathEndPointName).get(0).setText(before);
	}
}
