package com.snail.traffic.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.snail.traffic.control.query.QueryBusAPI;

/**
 * Servlet implementation class VagueSearchServlet
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/vaguesearch.jsp" }, 
	initParams = { @WebInitParam(name = "amount", value = "100") })

public class VagueSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VagueSearchServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pattern = request.getParameter("pattern");
		int amount;
		try {
			amount = Integer.parseInt(request.getParameter("amount"));
		} catch (Exception e) {
			amount = 100;
		}
		Vector<String> v = QueryBusAPI.fuzzySearch(pattern);
		
		JSONObject responseJSONObject = new JSONObject();
		JSONArray arr = new JSONArray();
		
		if ( v != null) {
			for (int i = 0; i < amount && i < v.size(); i++) {
				arr.add(v.elementAt(i));
			}
		}
		
		responseJSONObject.put("list", arr);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		
		PrintWriter out = null;
		try { 
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
}
