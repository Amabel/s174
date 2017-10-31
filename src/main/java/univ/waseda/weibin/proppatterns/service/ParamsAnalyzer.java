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

import sun.awt.RepaintArea;

public class ParamsAnalyzer {
	
	private Map<String, String> patternMap;
	private String[] params;
	private String[] afters;
	private String[] befores;
	private List<String> replacedParams;
	private List<String> replacedAfters;
	private List<String> replacedBefores;
	
	public ParamsAnalyzer() {
		createMap();
	}
	
	public ParamsAnalyzer(String[] params, String[] afters, String[] befores) {
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
		for (String str : this.params) {
			Iterator iterator = patternMap.entrySet().iterator();
			do {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if (str.contains(key)) {
					str = str.replace(key, value);
				}					
			} while(iterator.hasNext());
			replacedParams.add(str);
		}
	}
	
	private void replaceAfters() {
		replacedAfters = new ArrayList<String>();
		for (String str : this.afters) {
			Iterator iterator = patternMap.entrySet().iterator();
			do {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if (str.contains(key)) {
					str = str.replace(key, value);
				}					
			} while(iterator.hasNext());
			replacedAfters.add(str);
		}
	}
	
	private void replaceBefores() {
		replacedBefores = new ArrayList<String>();
		for (String str : this.befores) {
			Iterator iterator = patternMap.entrySet().iterator();
			do {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if (str.contains(key)) {
					str = str.replace(key, value);
				}					
			} while(iterator.hasNext());
			replacedBefores.add(str);
		}
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
