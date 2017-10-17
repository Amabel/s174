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

public class ParamsAnalyzer {
	
	Map<String, String> patternMap;
	
	public ParamsAnalyzer() {
		createMap();
	}

	public List<String> analyze(String[] params) {
		return replaceKeyWords(params);
	}
	
	private List<String> replaceKeyWords(String[] srcParams) {
		
		List<String> res = new ArrayList<String>();
		
		for (String str : srcParams) {
			Iterator iterator = patternMap.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				str = str.replace(key, value);
				System.out.println(str);
				res.add(str);
			}
		}
		
		return res;
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
	}
	
	
	
	private void traverseMap() {
		Iterator iterator = patternMap.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
			System.out.println(entry.getKey() + " : "+ entry.getValue());
			
		}
	}

}
