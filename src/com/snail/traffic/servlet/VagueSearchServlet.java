package com.snail.traffic.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.snail.traffic.control.QueryBus;

/**
 * Servlet implementation class VagueSearchServlet
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/vaguesearch.jsp" })
public class VagueSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VagueSearchServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Leave empty
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pattern = request.getParameter("pattern");
		Vector<String> v = QueryBus.fuzzySearch(pattern);
		
		JSONObject responseJSONObject = new JSONObject();
		JSONArray arr = new JSONArray();
		
		if ( v != null) {
			for (int i = 0; i < 10 && i < v.size(); i++) {
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
