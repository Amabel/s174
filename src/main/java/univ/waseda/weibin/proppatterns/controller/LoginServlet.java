package univ.waseda.weibin.proppatterns.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1355429612341976581L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Logger logger = LogManager.getLogger();
		
		String pwd = req.getParameter("pwdInput");
//		logger.debug(pwdJson);
//		String pwd = gson.fromJson(pwdJson, String.class);
		logger.debug(pwd);
		if (pwd.equalsIgnoreCase("57daa94045409ad53159fce8f2225fd3bd723452")) {
			logger.debug("login succeeded redirect to: " + req.getContextPath() + "/index.html");
			System.out.println(req.getContextPath() + "/index.html");
//			response.sendRedirect(request.getContextPath() + "/index.html");
			String url = this.getServletContext().getRealPath("WEB-INF");
			logger.debug(url);
			req.getRequestDispatcher("WEB-INF/index.html").forward(req, resp);
		}
	}
}
