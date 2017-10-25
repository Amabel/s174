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

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	Logger logger = LogManager.getLogger();
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		String pwd = request.getParameter("pwdInput");
//		logger.debug(pwdJson);
//		String pwd = gson.fromJson(pwdJson, String.class);
		logger.debug(pwd);
		if (pwd.equalsIgnoreCase(DigestUtils.sha1Hex("dntowaseda"))) {
			logger.debug("login succeeded redirect to: " + request.getContextPath() + "/index.html");
//			response.sendRedirect(request.getContextPath() + "/index.html");
			request.getRequestDispatcher("/WEB-INF/index.html").forward(request, response);
		} else {
			request.getRequestDispatcher("/WEB-INF/login.html").forward(request, response);
		}
		
	}

}
