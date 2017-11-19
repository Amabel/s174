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
	private String replacedAfters;
	private String replacedBefores;
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
				if (property.getConnector().equals("")) {
					rp += property.getOp1() + property.getOp() + property.getOp2();
				} else {
					rp += property.getConnector() + "(" + property.getOp1() + property.getOp() + property.getOp2() + ")";					
				}
			}
			System.out.println(rp);
			replacedParams.add(rp);
		}
	}
	
	private void connectAfters() {
		// connect each properties with "and / or"
		Property after0 = afters.get(0);
		if (after0.getOp().equals("") && after0.getOp1().equals("") && after0.getOp2().equals("")) {
			replacedAfters = "";
		} else {
			for (int i=0; i<afters.size(); i++) {
				String rp = "";
				for (Property property : afters) {
					if (property.getConnector().equals("")) {
						rp += property.getOp1() + property.getOp() + property.getOp2();
					} else {
						rp += property.getConnector() + "(" + property.getOp1() + property.getOp() + property.getOp2() + ")";						
					}
				}
				System.out.println(rp);
				replacedAfters = rp;
			}
		}
	}
	
	private void connectBefores() {
		// connect each properties with "and / or"
		Property before0 = befores.get(0);
		if (before0.getOp().equals("") && before0.getOp1().equals("") && before0.getOp2().equals("")) {
			replacedBefores = "";
		} else {
			for (int i=0; i<befores.size(); i++) {
				String rp = "";
				for (Property property : befores) {
					if (property.getConnector().equals("")) {
						rp += property.getOp1() + property.getOp() + property.getOp2();
					} else {
						rp += property.getConnector() + "(" + property.getOp1() + property.getOp() + property.getOp2() + ")";						
					}
				}
				System.out.println(rp);
				replacedBefores = rp;
			}
		}
	}
	
	private void replaceAfters() {
		replacedAfters = "";
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
		connectAfters();
	}
	
	private void replaceBefores() {
		replacedBefores = "";
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
		connectBefores();
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

	public String getReplacedAfters() {
		return replacedAfters;
	}

	public String getReplacedBefores() {
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
