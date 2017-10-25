package univ.waseda.weibin.proppatterns.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

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
		Gson gson = new Gson();
		
		String pwd = req.getParameter("pwdInput");
//		logger.debug(pwdJson);
//		String pwd = gson.fromJson(pwdJson, String.class);
		logger.debug(pwd);
		if (pwd.equalsIgnoreCase(DigestUtils.sha1Hex("dntowaseda"))) {
			logger.debug("login succeeded redirect to: " + req.getContextPath() + "/index.html");
//			response.sendRedirect(request.getContextPath() + "/index.html");
			req.getRequestDispatcher("/WEB-INF/index.html").forward(req, resp);
		}
	}
}
