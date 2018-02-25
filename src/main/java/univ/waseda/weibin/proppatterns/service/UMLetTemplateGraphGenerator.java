package univ.waseda.weibin.proppatterns.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class UMLetTemplateGraphGenerator {
	
	File srcFile;
	File destFile;
	String pattern;
	List<String> params;
	String after;
	String before; 
	Document document;
	String scope;
	String stringBuffer;
	
	public UMLetTemplateGraphGenerator(String pattern, List<String> params, String after, String before) {
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
		String path = UMLetTemplateGraphGenerator.class.getClassLoader().getResource(propFileName).getPath();
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
		String propertyKey = pattern;
		if (!this.after.equals("") && this.after != null && !this.before.equals("") && this.before != null) {
			propertyKey += "-between";
			this.scope = "between";
		} else if (!this.after.equals("") && this.before.equals("")) {
			propertyKey += "-after";
			this.scope = "after";
		} else if (!this.before.equals("") && this.after.equals("")) {
			propertyKey += "-before";
			this.scope = "before";
		} else {
			System.out.println("-----scope : global");
			propertyKey += "-global";
			this.scope = "global";
		}
		System.out.println("Property key : " + propertyKey);
		
		String graphFileName = graphPathProperties.getProperty(propertyKey);
		String graphFilePath = UMLetTemplateGraphGenerator.class.getClassLoader().getResource(graphFileName).getPath();		
		srcFile = new File(graphFilePath);
		// file
		return writeXmlFile(destPath);
	}
	
	private File writeXmlFile(String destPath) {
		try {
			stringBuffer = FileUtils.readFileToString(srcFile, (Charset)null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// replace before after
		replaceProperties();
		if (this.scope.equals("between")) {
			replaceAfter();
			replaceBefore();
		} else if (this.scope.equals("after")) {
			replaceAfter();
		} else if (this.scope.equals("before")) {
			replaceBefore();
		}
		
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
		String destFileName = "prop-pattern-" + df.format(new Date()) + ".uxf";
        String destFilePath = destDirPath + destFileName;
        File destFile = new File(destFilePath);
        
        try {
			FileUtils.write(destFile, stringBuffer, (Charset)null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		OutputFormat format = OutputFormat.createPrettyPrint();  
//        format.setEncoding("UTF-8");
//        XMLWriter writer;
//		try {
//			writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(destFilePath)), format);
//			// System.out.println(stringBuffer);
//			try {
//				document = DocumentHelper.parseText(stringBuffer);
//			} catch (DocumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        writer.write(document);  
//	        writer.close();  
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return destFile;
	}
	
	private void replaceProperties() {
		
		if (stringBuffer.contains("*Placeholder P*")) {
			replaceP();
		}
		if (stringBuffer.contains("*Placeholder S*")) {
			replaceS();
		}
		if (stringBuffer.contains("*Placeholder T*")) {
			replaceT();
		}

	}
	
	private void replaceP() {
		String propertyP = this.params.get(0);
		stringBuffer = stringBuffer.replace("*Placeholder P*", propertyP);
	}
	
	private void replaceS() {
		String propertyS = this.params.get(1);
		stringBuffer = stringBuffer.replace("*Placeholder S*", propertyS);
	}
	
	private void replaceT() {
		String propertyT = this.params.get(2);
		stringBuffer = stringBuffer.replace("*Placeholder T*", propertyT);
	}
	
	private void replaceAfter() {
		stringBuffer = stringBuffer.replace("*Placeholder Q*", after);
	}
	
	private void replaceBefore() {		
		stringBuffer = stringBuffer.replace("*Placeholder R*", before);
	}
}
