package univ.waseda.weibin.proppatterns.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import univ.waseda.weibin.ltlgen.formula.LTL;
import univ.waseda.weibin.ltlgen.service.LTLGenerator;
import univ.waseda.weibin.proppatterns.service.TemplateJsonAnalyzer;

public class TemplateServlet extends HttpServlet {

	/**
	 * 
	 */
	// public Logger logger = LoggerHelper.getLogger(this.getClass().getName());
	public Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = -1198035746274678001L;
	private String templatesDirPath;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		logger.trace("submit template button clicked.");
		
		templatesDirPath = this.getServletConfig().getServletContext().getRealPath("download/graph-templates/");

		logger.debug("path to \"download/graph-templates/\": " + templatesDirPath);

		String templateJson = request.getParameter("templateJson");

		// process templateJson
		TemplateJsonAnalyzer templateJsonAnalyzer = new TemplateJsonAnalyzer(templatesDirPath);
		templateJsonAnalyzer.analyze(templateJson);

		// get the graphTemplate file
		File graphTemplate = templateJsonAnalyzer.getGraphTemplate();
		logger.debug("graph template generated, file path: " + graphTemplate.getPath());
		
		// ltlgen 
		LTL ltl = new LTLGenerator(graphTemplate.getPath()).generate();
		logger.trace("ltl formula generated.");
		logger.debug("ltl: " + ltl);
		
		Gson gson = new Gson();
		JsonObject retJson = new JsonObject();
		JsonElement graphTemplateFileName = new JsonParser().parse(graphTemplate.getName());
		
		logger.trace("get graph template filename.");
		
		retJson.add("graphTemplateFileName", new JsonParser().parse(templateJsonAnalyzer.getUMLetFileName()));
		
		logger.trace("filename added to retJson.");
		
		String ltlJson = gson.toJson(ltl);
		retJson.add("ltl", new JsonParser().parse(ltlJson));
		
		logger.trace("ltl added to retJson.");
		
		// return results (graph template)
		String retStr = gson.toJson(retJson);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(gson.toJson(retStr));
		out.close();
		logger.debug("write graphTemplateJsonString: " + retStr);
		logger.trace("retStr sent.");
	}

}
