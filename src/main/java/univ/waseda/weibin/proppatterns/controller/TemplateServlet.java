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

		templatesDirPath = this.getServletConfig().getServletContext().getRealPath("download/graph-templates/");

		logger.trace("path of \"download/graph-templates/\": \n\t" + templatesDirPath);

		String templateJson = request.getParameter("templateJson");

		// process templateJson
		TemplateJsonAnalyzer templateJsonAnalyzer = new TemplateJsonAnalyzer(templatesDirPath);
		templateJsonAnalyzer.analyze(templateJson);

		// get the gtaphTemplate file
		File graphTemplate = templateJsonAnalyzer.getGraphTemplate();
		Gson gson = new Gson();
		String graphTemplateJsonString = gson.toJson(graphTemplate.getName());
		// return results (graph template)
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(graphTemplateJsonString);
		out.close();
	}

}
