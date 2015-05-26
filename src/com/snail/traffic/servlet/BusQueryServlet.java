package com.snail.traffic.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import com.snail.traffic.control.*;
import com.snail.traffic.persistence.*;

import net.sf.json.*;

/**
 * Servlet implementation class BusExchange
 */
@WebServlet(urlPatterns = { "/search.jsp" })
public class BusQueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BusQueryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		int queryType = 0;
		JSONObject responseJSONObject = null;
		PrintWriter out = null;
		
		try {
			queryType = Integer.parseInt(request.getParameter("query-type"));
			
			switch (queryType) {
			case 1:
				String startStation = request.getParameter("bus-exchange-start");
				String destStation = request.getParameter("bus-exchange-dest");
				responseJSONObject = this.queryBusExchange(startStation, destStation);
				break;
			case 2:
				String lineName = request.getParameter("bus-line-no"); 
				responseJSONObject = this.queryBusLine(lineName);
				break;
			case 3:
				String stationName = request.getParameter("bus-station-name");
				responseJSONObject = this.queryBusStation(stationName);
				break;
			default:
				queryType = 0; // Wrong
			}
			
			if (responseJSONObject == null) {
				responseJSONObject = new JSONObject();
				queryType = -1;
			}
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			
			responseJSONObject.put("type", queryType);
			
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

	/**
	 * 
	 * @param startStation Start bus station name
	 * @param destStation Destination bus station name
	 * @throws IOException
	 */
	protected JSONObject queryBusExchange(String startStation, String destStation) throws IOException {
		/**
		 * 
		 * 
		 */
		
		return null;
	}
	
	/**
	 * 
	 * @param lineName
	 * @throws IOException
	 */
	protected JSONObject queryBusLine(String lineName) throws IOException {
		InfoStruct ret = QueryBus.queryBusLine(lineName);
		return ret.toJSON();
	}
	
	/**
	 * 
	 * @param stationName
	 * @throws IOException
	 */
	protected JSONObject queryBusStation(String stationName) throws IOException {
		InfoStruct ret = QueryBus.queryBusSite(stationName);
		return ret.toJSON();
	}
}