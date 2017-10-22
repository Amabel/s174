package univ.waseda.weibin.proppatterns.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadTemplateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4928627326839999379L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String filename = request.getParameter("filename");  
//        System.out.println(filename);
        
//        response.setContentType(getServletContext().getMimeType(filename));
        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+filename);
        
        String filePath = this.getServletContext().getRealPath("/download/graph-templates/" + filename);
        
        InputStream in = new FileInputStream(filePath);
        OutputStream out = response.getOutputStream();
        byte[] buff = new byte[2048];
        int len = 0;
        while((len = in.read(buff))>0){  
            out.write(buff, 0, len);  
        }  
        out.close();  
        in.close();  
        
        
	}
}
