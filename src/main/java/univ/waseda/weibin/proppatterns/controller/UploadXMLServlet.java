package univ.waseda.weibin.proppatterns.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import univ.waseda.weibin.ltlgen.formula.LTL;
import univ.waseda.weibin.ltlgen.service.LTLGenerator;

public class UploadXMLServlet extends HttpServlet {

	Logger logger = LogManager.getLogger();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		File file = null;  
        response.setCharacterEncoding("UTF-8");  
        request.setCharacterEncoding("UTF-8");  
        response.setContentType("text/html");  
        
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        ServletFileUpload upload = new ServletFileUpload(factory); 
        String hashedFileName = null;
        String uploadedFilePath = null;
        try {
			List<FileItem> fileItemList = upload.parseRequest(request);
			for (FileItem fileItem : fileItemList) {
				logger.debug(fileItem.getFieldName());
				if (fileItem.getFieldName().equals("fileInput")) {  
					String fileName = fileItem.getName();
					hashedFileName = DigestUtils.sha1Hex(fileName);
					String uploadDirPath = this.getServletConfig().getServletContext().getRealPath("upload/");
					logger.debug("upload path: " + uploadDirPath);
					file = new File(uploadDirPath, hashedFileName);
					file.getParentFile().mkdirs();  
					file.createNewFile();
					uploadedFilePath = file.getPath();
                    InputStream ins = fileItem.getInputStream();  
                    OutputStream ous = new FileOutputStream(file);  
                    
                    try {  
                        byte[] buffer = new byte[1024];  
                        int len = 0;  
                        while ((len = ins.read(buffer)) > -1)  
                            ous.write(buffer, 0, len);  
                    } finally {  
                        ous.close();  
                        ins.close();  
                    }  
                    
					logger.debug(fileItem.getSize());
					logger.debug(fileItem.getName());
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if (hashedFileName != null) {
        	// analyze xml
        	LTL ltl = new LTLGenerator(uploadedFilePath).generate();
        	logger.trace("ltl formula generated.");
    		logger.debug("ltl: " + ltl);
    		Gson gson = new Gson();
    		JsonObject retJson = new JsonObject();
    		String ltlJson = gson.toJson(ltl);
    		retJson.addProperty("result", "ok");
    		retJson.add("ltl", new JsonParser().parse(ltlJson));
    		response.getWriter().write(retJson.toString()); 
    		
        } else {
        	 JsonObject jsonObject = new JsonObject();    
             jsonObject.addProperty("result", "error");
             response.getWriter().write(jsonObject.toString()); 
        }
        
       
	}
}
