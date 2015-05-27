package com.snail.traffic.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class LineEditServlet
 */
@WebServlet({ "/line-edit.jsp", "/admin.jsp?line-edit" })
public class LineEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LineEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		
		JSONObject responseJSONObject = new JSONObject();		
		PrintWriter out = null;
		
		try {
			boolean isSuccess = false;
			String errorMsg = "";
			
			int mode = Integer.parseInt(request.getParameter("mode"));
			String lineName = request.getParameter("title");
			
			if (mode == -1) {
				// Remove line
				isSuccess = this.removeBusLine(lineName);
			} else if (mode == 0 || mode == 1) { 
				// Add / Alter
				String[] leftStr  = request.getParameterValues("left");
				String[] rightStr = request.getParameterValues("right");
				
				if (mode == 0) {
					// Alter
					String lineNameNew = request.getParameter("title-new");
					isSuccess = this.alterBusLine(lineName, lineNameNew, leftStr, rightStr);
				} else {
					isSuccess = this.addBusLine(lineName, leftStr, rightStr);
				}
			} else {
				isSuccess = false;
				errorMsg = "´íÎóµÄ²Ù×÷£¡";
			}
			
			responseJSONObject.put("success", isSuccess);
			responseJSONObject.put("msg", errorMsg);
			
			out = response.getWriter();
			out.append(responseJSONObject.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) { 
				out.close();
			}
		}
	}

	public boolean removeBusLine(String lineName) {
		//...
		return true;
	}
	
	public boolean addBusLine(String lineName, String[] leftStations, String[] rightStations) {
		//...
		return true;
	}
	
	public boolean alterBusLine(String oldName, String newName, String[] leftStations, String[] rightStations) {
		//...
		return true;
	}
}
