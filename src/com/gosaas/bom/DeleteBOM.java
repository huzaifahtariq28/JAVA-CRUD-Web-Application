package com.gosaas.bom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.gosaas.constants.ProjectConstants;
import com.gosaas.database.Connect;
import com.gosaas.utils.GetJson;
import com.gosaas.utils.Logger;

/**
 * Servlet implementation class DeleteBOM
 */
@WebServlet("/DeleteBOM")
public class DeleteBOM extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteBOM() {
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
		final String DELETE_BOM_SQL = "DELETE FROM bom WHERE bomId = ?";
		
		JSONObject responseObject = new JSONObject();

		Logger logger = null;
		
	    Connect conn = new Connect();
	    Connection db = conn.getConnection();
	    PreparedStatement preparedStatement;
	    
		try {
			logger = new Logger("Assignment", ProjectConstants.LOGS_PATH);
			
			JSONObject req = GetJson.getParamsFromRequest(request);
			
			preparedStatement = db.prepareStatement(DELETE_BOM_SQL);
		
            preparedStatement.setLong(1, (long) req.get("bomId"));
            System.out.println(preparedStatement);
            
            preparedStatement.executeUpdate();
            
	        responseObject.putIfAbsent("status", "success");
	        
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