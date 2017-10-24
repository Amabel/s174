package univ.waseda.weibin.ltlgen.service;
import java.util.Scanner;

import univ.waseda.weibin.ltlgen.formula.LTL;


/**
 * @author  Weibin Luo
 * @version Created on 2017/05/22 15:27:45
 */
public class LTLGenController {
	
	private LTLGenerator ltlGenerator;

	public void launch() {
		int debugMode = 0;
		
		String fileName = null;
		
		if (debugMode == 1) {
			// for debug
			fileName = "examples/test/test-resp.z151";
		} else {
			// input mode
			fileName = readInputFileName();
		}

		ltlGenerator = new LTLGenerator(fileName);
		LTL ltl = ltlGenerator.generate();
		
		printResults(ltl);
		
	}
	
	private String readInputFileName() {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String fileName = "examples/verify/" + in.nextLine();
		return fileName;
	}
	
	private void printResults(LTL ltl) {
		if (ltl != null) {
			System.out.println("Pattern: " + ltl.getPattern());
			System.out.println("Scope:   " + ltl.getScope());
			System.out.println("LTLFormula: " + ltl.getFomula());
		}
	}
}
