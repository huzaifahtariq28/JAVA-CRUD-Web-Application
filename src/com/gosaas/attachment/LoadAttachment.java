package com.gosaas.attachment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.gosaas.constants.ProjectConstants;
import com.gosaas.database.Connect;
import com.gosaas.utils.GetJson;
import com.gosaas.utils.Logger;

/**
 * Servlet implementation class LoadAttachment
 */
@WebServlet("/LoadAttachment")
public class LoadAttachment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadAttachment() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String SELECT_ATTACHMENTS_USERS = "select * from attachment where itemId = ?";
	    
	    JSONObject responseObject = new JSONObject();
		JSONArray array = new JSONArray();
		
		Logger logger = null;
	    
	    
	    Connect conn = new Connect();
	    Connection db = conn.getConnection();
	    PreparedStatement preparedStatement;
	    
		try {
			try {

				logger = new Logger("Assignment", ProjectConstants.LOGS_PATH);
				
				JSONObject req = GetJson.getParamsFromRequest(request);
				
				preparedStatement = db.prepareStatement(SELECT_ATTACHMENTS_USERS);
	            preparedStatement.setString(1, req.get("itemId").toString());
				System.out.println(preparedStatement);
				
		        ResultSet rs = preparedStatement.executeQuery();
		        
		        while (rs.next()) {
		        	JSONObject record = new JSONObject();
		        	
		        	record.put("attachmentId", rs.getLong("attachmentId"));
		        	record.put("itemId", rs.getString("itemId"));
	        		record.put("attachmentName", rs.getString("attachmentName"));
	        		record.put("attachmentDescription", rs.getString("attachmentDescription"));
	        		record.put("attachmentFile", rs.getBlob("attachmentFile"));
			  		
			  		array.add(record);
		        }
		        
		        responseObject.put("Attachment_data", array);
		        responseObject.putIfAbsent("status", "success");
		        	
			} catch (SQLException e1) {
				logger.error(e1.getLocalizedMessage());
				System.out.println(e1);
				responseObject.put("status", "error");
	    		responseObject.put("message",
						e1.getLocalizedMessage() == null ? e1
								: e1.getLocalizedMessage().contains("code 401:")
										? "Your session has been expired. Please launch Application from Oracle Cloud again"
										: e1.getLocalizedMessage());
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			System.out.println(e);
			responseObject.put("status", "error");
    		responseObject.put("message",
					e.getLocalizedMessage() == null ? e
							: e.getLocalizedMessage().contains("code 401:")
									? "Your session has been expired. Please launch Application from Oracle Cloud again"
									: e.getLocalizedMessage());
		}
			
		String jsonResponse = new Gson().toJson(responseObject);
		response.setContentType("application/json");
		response.getWriter().println(jsonResponse);	
	}

}
