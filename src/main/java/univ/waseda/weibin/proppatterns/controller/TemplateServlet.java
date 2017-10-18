package univ.waseda.weibin.proppatterns.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import univ.waseda.weibin.proppatterns.service.JsonAnalyzer;
import univ.waseda.weibin.proppatterns.service.TemplateJsonAnalyzer;

public class TemplateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1198035746274678001L;
	private String webInfPath;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		webInfPath = this.getServletConfig().getServletContext().getRealPath("WEB-INF");
		
		String templateJson = request.getParameter("templateJson");
		
		// process templateJson
		TemplateJsonAnalyzer templateJsonAnalyzer = new TemplateJsonAnalyzer(webInfPath);
		templateJsonAnalyzer.analyze(templateJson);
		
		// get the gtaphTemplate file
		File graphTemplate = templateJsonAnalyzer.getGraphTemplate();   
		Gson gson = new Gson();
		String graphTemplateJsonString = gson.toJson(graphTemplate.getAbsolutePath());
		// return results (graph template)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(graphTemplateJsonString);
        out.close();
	}

}
