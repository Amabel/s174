package univ.waseda.weibin.ltlgen.service;
import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import univ.waseda.weibin.ltlgen.formula.LTL;

/**
 * @author  Weibin Luo
 * @version Created on 2017/05/26 17:35:26
 */
public class LTLGenerator {
	
	private String fileName;
	
	public LTLGenerator(String fileName) {
		this.fileName = fileName;
	}
	
	public LTL generate() {
		LTL ltl = null;
		// start jobs
		Document xmlFile = readXMLFile();
		if (xmlFile != null) {
			XMLAnalyzer xmlAnalyzer = new XMLAnalyzer(xmlFile);
			ltl = xmlAnalyzer.analyze();
			
		}
		
		return ltl;
	}
	
	private Document readXMLFile() {
		Document xmlFile = null;
		String fileName = this.fileName;
		SAXReader saxReader = new SAXReader();
		
		try {
			File inputFile = new File(fileName);
			xmlFile = saxReader.read(inputFile);
//			System.out.println(xmlFile);
		} catch (DocumentException e2) {
//			e2.printStackTrace();
			System.out.println("connot find file: " + fileName);
		}
		return xmlFile;
	}
}
