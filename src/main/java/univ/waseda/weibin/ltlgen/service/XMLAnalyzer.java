package univ.waseda.weibin.ltlgen.service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import univ.waseda.weibin.ltlgen.formula.LTL;
import univ.waseda.weibin.ltlgen.patterns.Absence;
import univ.waseda.weibin.ltlgen.patterns.BoundedExistence;
import univ.waseda.weibin.ltlgen.patterns.Existence;
import univ.waseda.weibin.ltlgen.patterns.Precedence;
import univ.waseda.weibin.ltlgen.patterns.PrecedenceChain;
import univ.waseda.weibin.ltlgen.patterns.Response;
import univ.waseda.weibin.ltlgen.patterns.ResponseChain;
import univ.waseda.weibin.ltlgen.patterns.Universality;

public class XMLAnalyzer {
	
	Document xmlFile;
	
	public XMLAnalyzer(Document xmlFile) {
		this.xmlFile = xmlFile;
	}
	
	public LTL analyze() {
		LTL ltl = analyzeXMLFile();
		return ltl;
	}

	
	private LTL analyzeXMLFile() {
		LTL ltl = null;
		// find scope;
		// scope.get(0): startPoint, scope.get(1): endPoint
		List<String> scope = findScope();
		
		// select attribute: [@*[name()='xsi:type']]
		String xPathNodes = "//ucmMaps/nodes[@*[name()='xsi:type']]";
		List<?> nodesList = xmlFile.selectNodes(xPathNodes);
		
		// # of <nodes>
//		System.out.println("Found <nodes> with attribute @xsi:type: " + nodesList.size());
		
		// # of <nodes> with @xsi:type=DirectionArrow
		int count = findNode(nodesList, "type", "DirectionArrow").size();
//		System.out.println("//nodes[@xsi:type=DirectionArrow] count: " + count);

		if (count == 0) {
			// no direction arrow
			// absence, existence, bounded existence, universality
			
			// find //nodes@xsi:type=RespRef
			List<Element> nodesRespRefList = findNode(nodesList, "type", "RespRef");
			int nodesRespRefCount = nodesRespRefList.size();
			if (nodesRespRefCount != 1) {
				// error: too many respRefs
				System.out.println("too mant respRefs");
			} else {
				// decide the pattern
				
				// find //responsibilities/name
				String xPathRespName = "//responsibilities/name";
				String respName = ((Element)(xmlFile.selectNodes(xPathRespName).get(0))).getText();
				
//				System.out.println(respName);
				
				// regular expression
				// absence: not(P)
				// existence: exist(P)
				// bounded existence: P(..m)
				// universality: univ(P)
				Pattern pattern = Pattern.compile("(.*)\\((.*)\\)\\[*(.*?)\\]*");
				Matcher m = pattern.matcher(respName);
				if (!m.matches()) {
					// error incorrect resp name 
					System.out.println("no matches with RE.");
					return null;
				} else {
					// match the pattern [not, exist, be, univ]
					// e.g.: not(P)
					// group(1): not, group(2): P
					String g1 = m.group(1);
					String g2 = m.group(2);
					String g3 = m.group(3);
//					System.out.println("g3: " + g3);
					
					switch (g1) {
					case "not":
						// absence
						ltl = new Absence(g2, scope).generateLTL();
						break;
					case "exist":
						// existence
						ltl = new Existence(g2, scope).generateLTL();
						break;
					case "be":
					case "bounded existence":
					case "bounde":
						// bounded existence
						ltl = new BoundedExistence(g2, scope, g3).generateLTL();
						break;
					case "univ":
						// universality
						ltl = new Universality(g2, scope).generateLTL();
						break;
					default:
						// error: no matching patterns
						System.out.println("no matching patterns");
						System.out.println("g1: " + g1 + ", g2: " + g2); 
						return null;
					}
				}
			}
			
		} else if (count == 1) {
			// 1 direction arrow
			// precedence, response, chain precedence, chain response
			List<Element> nodesRespRefList = findNode(nodesList, "type", "RespRef");
			int nodesRespRefCount = nodesRespRefList.size();
			if (nodesRespRefCount == 2) {
				// 2 respRefs
				// response, precedence
				
				// find the name of 2 respRefs
				String xPathRespName = "//responsibilities/name";
				String pName = ((Element)(xmlFile.selectNodes(xPathRespName).get(0))).getText();
				String sName = ((Element)(xmlFile.selectNodes(xPathRespName).get(1))).getText();
				
				// find property P and S
				Pattern pattern = Pattern.compile("(.*?)\\((.*)\\)(\\*)??");
				Matcher mp = pattern.matcher(pName);
				Matcher ms = pattern.matcher(sName);
				
				if (!mp.matches() || !ms.matches()) {
					// error: incorrect resp name 
					System.out.println("no matches with RE.");
					return null;
				}
				// pName and sName matches
				String pg1 = mp.group(1);
				String sg1 = ms.group(1);
//				System.out.println(mp.group(2));
//				System.out.println(ms.group(2));
				// find the pattern (response or precedence) by the position of '*'
				if (pName.charAt(pName.length()-1) == '*' && sName.charAt(sName.length()-1) != '*') {
					// response
					// P* -> Q
					if (pg1.equals("P") && sg1.equals("S")) {
						String pg2 = null;
						String sg2 = null;
						if (mp.group(2).equals("")) {
							pg2 = pg1;
						} else {
							pg2 = mp.group(2);
						}
						if (ms.group(2).equals("")) {
							sg2 = sg1;
						} else {
							sg2 = ms.group(2);
						}
						ltl = new Response(pg2, sg2, scope).generateLTL();
					} else {
						// error cannot find P and S
						System.out.println("cannot find P and S");
						return null;
					}
				} else if (sName.charAt(sName.length()-1) == '*' && pName.charAt(pName.length()-1) != '*') {
					// precedence
					// P -> Q*
					if (pg1.equals("P") && sg1.equals("S")) {
						String pg2 = null;
						String sg2 = null;
						if (mp.group(2).equals("")) {
							pg2 = pg1;
						} else {
							pg2 = mp.group(2);
						}
						if (ms.group(2).equals("")) {
							sg2 = sg1;
						} else {
							sg2 = ms.group(2);
						}
						ltl = new Precedence(pg2, sg2, scope).generateLTL();
					} else {
						// error cannot find P and S
						System.out.println("cannot find P and S");
						return null;
					}
				} else {
					// both with '*' or no '*'
					// error
					System.out.println("cannot find '*'");
					return null;
				}
			} else if (nodesRespRefCount == 3) {
				// 3 or more respRefs
				// chain response, chain precedence
				
				// find the pattern 
				// response: P* -> S, T  or S*, T* -> P
				// precedence: P -> S*, T* or S, T -> P*
				
				// find the respRef left to the arrow
				// find the id of arrow
				String xPathDirectionArrowId = "//ucmMaps/nodes[@*[name()='xsi:type']='DirectionArrow']/id";
				int directionArrowId = Integer.valueOf(((Element)(xmlFile.selectNodes(xPathDirectionArrowId).get(0))).getText());
//				System.out.println("id = " + directionArrowId);
				
				// find connections
				String xPathConnections = "//ucmMaps/connections";
				List<?> connectionsList = xmlFile.selectNodes(xPathConnections);
				int leftRespRefId = -1;
				for (Object o : connectionsList) {
					Element e = (Element)o;
					int targetId = Integer.valueOf(e.elements().get(3).getText());
					if (targetId == directionArrowId) {
						leftRespRefId = Integer.valueOf(e.elements().get(4).getText());
						break;
					}
				}
				if (leftRespRefId != -1) {
					String leftRespName = null;
					String pName =null;
					String sName =null;
					String tName =null;
					// find the name of leftRespRef
					String xPathResp = "//responsibilities";
					List<?> RespList = xmlFile.selectNodes(xPathResp);
					for (Object o : RespList) {
						Element e = (Element)o;
						// set pName, sName, tName
						String name = e.elements().get(1).getText();
						if (name.charAt(0) == 'P') {
							pName = name;
						} else if (name.charAt(0) == 'S') {
							sName = name;
						} else if (name.charAt(0) == 'T') {
							tName = name;
						}
						int respId = Integer.valueOf(e.elements().get(4).getText());
						if (respId == leftRespRefId) {
							leftRespName = e.elements().get(1).getText();
						}
					}
					
					// find property P and S
					Pattern pattern = Pattern.compile("(.*?)\\((.*)\\)(\\*)??");
					Matcher mp = pattern.matcher(pName);
					Matcher ms = pattern.matcher(sName);
					Matcher mt = pattern.matcher(tName);
					if (!mp.matches() || !ms.matches() || !mt.matches()) {
						// error: incorrect resp name 
						System.out.println("no matches with RE.");
						return null;
					}
					String pg1 = mp.group(1);
					String sg1 = ms.group(1);
					String tg1 = mt.group(1);
					String pg2 = null;
					String sg2 = null;
					String tg2 = null;
					
					if (pg1.equals("P") && sg1.equals("S") && tg1.equals("T")) {
						if (mp.group(2).equals("")) {
							pg2 = pg1;
						} else {
							pg2 = mp.group(2);
						}
						if (ms.group(2).equals("")) {
							sg2 = sg1;
						} else {
							sg2 = ms.group(2);
						}
						if (mt.group(2).equals("")) {
							tg2 = tg1;
						} else {
							tg2 = mt.group(2);
						}
					} else {
						// error cannot find P, S and T
						System.out.println("cannot find P, S and T");
						return null;
					}
					
					// analyze the name of the left resp
					// if contains '*' then response, otherwise precedence
					if (leftRespName.charAt(leftRespName.length() - 1) == '*') {
						// response
						// if contains P then P* -> S, T
						// otherwise S*, T* -> P
						if (leftRespName.charAt(0) == 'P') {
							ltl = new ResponseChain(pg2, sg2, tg2, scope, 2).generateLTL();
						} else if (leftRespName.charAt(0) == 'T'){
							ltl = new ResponseChain(pg2, sg2, tg2, scope, 1).generateLTL();
						} else {
							// error: cannot find P or T
							System.out.println("cannot find P or T");
						}
						
					} else {
						// precedence
						// if contains P then P -> S*, T*
						// otherwise S, T -> P*
						if (leftRespName.charAt(0) == 'P') {
							ltl = new PrecedenceChain(pg2, sg2, tg2, scope, 2).generateLTL();
						} else if (leftRespName.charAt(0) == 'T'){
							ltl = new PrecedenceChain(pg2, sg2, tg2, scope, 1).generateLTL();
						} else {
							// error: cannot find P or T
							System.out.println("cannot find P or T");
						}
					}
					
				} else {
					// error: cannot find leftRespRef
					System.out.println("cannot find leftRespRef");
					return null;
				}
				
				
				
				
			} else if (nodesRespRefCount > 3) {
				// error: too many respRefs
				System.out.println("too many respRefs: " + nodesRespRefCount);
				return null;
			}
		} else {
			// error: too many direction arrows
			System.out.println("too many direction arrows");
			return null;
		}
		
		return ltl;
	}
	
	private List<String> findScope() {
		List<String> list = new ArrayList<String>();
		// find the name of StartPoint and EndPoint
		String xPathStartPointName = "//ucmMaps/nodes[@*[name()='xsi:type']='StartPoint']/name";
		String startPointName = ((Element)(xmlFile.selectNodes(xPathStartPointName).get(0))).getText();
//		startPointName = replaceEscChar(startPointName);		
		list.add(startPointName);
		// endPoint
		String xPathEndPointName = "//ucmMaps/nodes[@*[name()='xsi:type']='EndPoint']/name";
		String endPointName = ((Element)(xmlFile.selectNodes(xPathEndPointName).get(0))).getText();
//		endPointName = replaceEscChar(endPointName);
		list.add(endPointName);
		return list;
	}
	
	private List<Element> findNode(List<?> list, String attributeName, String attributeValue) {
		List<Element> ret = new ArrayList<>();
		for (Object o : list) {
			Element element = (Element)o;
//			System.out.println(element.attribute(attributeName).getValue());
			Attribute attribute = element.attribute(attributeName);
			if (attribute != null) {
				String value = attribute.getValue();
				if (value.equals(attributeValue)) {
					ret.add(element);					
				}
			}
		}
		return ret;
	}
}
