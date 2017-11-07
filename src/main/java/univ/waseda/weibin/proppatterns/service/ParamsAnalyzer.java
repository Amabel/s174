package univ.waseda.weibin.proppatterns.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import univ.waseda.weibin.proppatterns.model.Property;

public class ParamsAnalyzer {
	
	private Map<String, String> patternMap;
	private List<Property>[] params;
	private List<Property> afters;
	private List<Property> befores;
	private List<String> replacedParams;
	private List<String> replacedAfters;
	private List<String> replacedBefores;
	Logger logger = LogManager.getLogger();
	
	public ParamsAnalyzer() {
		createMap();
	}
	
	public ParamsAnalyzer(List<Property>[] params, List<Property> afters, List<Property> befores) {
		this.params = params;
		this.afters = afters;
		this.befores = befores;
		createMap();
	}

	public void analyze() {
		replaceParams();
		replaceAfters();
		replaceBefores();
	}
	
	private void replaceParams() {
		replacedParams = new ArrayList<String>();
		for (int i=0; i<params.length; i++) {
//			replacedParams[i] = new ArrayList<Property>();
			for (Property property : this.params[i]) { 
				Iterator iterator = patternMap.entrySet().iterator();
				do {
					Map.Entry entry = (Map.Entry) iterator.next();
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();
					String op1 = property.getOp1();
					String op2 = property.getOp2();
					if (op1.contains(key)) {
						property.setOp1(op1.replace(key, value));
					}	
					if (op2.contains(key)) {
						property.setOp2(op2.replace(key, value));
					}
				} while(iterator.hasNext());
//				replacedParams[i].add(property);
			}
		}
		connectParams();
	}
	
	private void connectParams() {
		// connect each properties with "and / or"
		for (int i=0; i<params.length; i++) {
			String rp = "";
			for (Property property : params[i]) {
				rp += property.getConnector() + "(" + property.getOp1() + property.getOp() + property.getOp2() + ")";
			}
			System.out.println(rp);
			replacedParams.add(rp);
		}
	}
	
	private void connectAfters(List<Property> properties) {
		// connect each properties with "and / or"
		for (int i=0; i<properties.size(); i++) {
			String rp = "";
			for (Property property : properties) {
				rp += property.getConnector() + "(" + property.getOp1() + property.getOp() + property.getOp2() + ")";
			}
			System.out.println(rp);
			replacedAfters.add(rp);
		}
	}
	
	private void connectBefores(List<Property> properties) {
		// connect each properties with "and / or"
		for (int i=0; i<properties.size(); i++) {
			String rp = "";
			for (Property property : properties) {
				rp += property.getConnector() + "(" + property.getOp1() + property.getOp() + property.getOp2() + ")";
			}
			System.out.println(rp);
			replacedBefores.add(rp);
		}
	}
	
	private void replaceAfters() {
		replacedAfters = new ArrayList<String>();
		for (Property property : this.afters) {
			Iterator iterator = patternMap.entrySet().iterator();
			do {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				String op1 = property.getOp1();
				String op2 = property.getOp2();
				if (op1.contains(key)) {
					property.setOp1(op1.replace(key, value));
				}	
				if (op2.contains(key)) {
					property.setOp2(op2.replace(key, value));
				}					
			} while(iterator.hasNext());
		}
		connectAfters(afters);
	}
	
	private void replaceBefores() {
		replacedBefores = new ArrayList<String>();
		for (Property property : this.befores) {
			Iterator iterator = patternMap.entrySet().iterator();
			do {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				String op1 = property.getOp1();
				String op2 = property.getOp2();
				if (op1.contains(key)) {
					property.setOp1(op1.replace(key, value));
				}	
				if (op2.contains(key)) {
					property.setOp2(op2.replace(key, value));
				}					
			} while(iterator.hasNext());
		}
		connectBefores(befores);
	}
	
	private void createMap() {
		// construct map from .properties
		Properties patternMappingProperties = new Properties();
		String propFileName = "pattern-mapping.properties";
		
		String path = ParamsAnalyzer.class.getClassLoader().getResource(propFileName).getPath();
		
		try {
			FileInputStream in = new FileInputStream(path);
			patternMappingProperties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patternMap = new HashMap<String, String>((Map)patternMappingProperties);
//		traverseMap();
	}

	public List<String> getReplacedParams() {
		return replacedParams;
	}

	public List<String> getReplacedAfters() {
		return replacedAfters;
	}

	public List<String> getReplacedBefores() {
		return replacedBefores;
	}

	private void traverseMap() {
		Iterator iterator = patternMap.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
			System.out.println(entry.getKey() + " : "+ entry.getValue());
			
		}
	}
}
