package univ.waseda.weibin.proppatterns.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
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
		
		webInfPath = this.getServletConfig().getServletContext().getRealPath("download/graph-templates/");
		
		System.out.println(webInfPath);
		
		String templateJson = request.getParameter("templateJson");
		
		// process templateJson
		TemplateJsonAnalyzer templateJsonAnalyzer = new TemplateJsonAnalyzer(webInfPath);
		templateJsonAnalyzer.analyze(templateJson);
		
		// get the gtaphTemplate file
		File graphTemplate = templateJsonAnalyzer.getGraphTemplate();   
		Gson gson = new Gson();
		System.out.println(graphTemplate.getPath());
		String graphTemplateJsonString = gson.toJson(graphTemplate.getName());
		// return results (graph template)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(graphTemplateJsonString);
        out.close();
	}

}
