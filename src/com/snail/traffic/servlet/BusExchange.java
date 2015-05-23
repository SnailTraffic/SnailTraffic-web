package com.snail.traffic.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BusExchange
 */
@WebServlet(
		urlPatterns = {
				"/search.jsp"
		})
public class BusExchange extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BusExchange() {
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
		try {
			int queryType = Integer.parseInt(request.getParameter("queryType"));
			
			switch (queryType) {
			case 0:
				String startStation = request.getParameter("start");
				String destStation = request.getParameter("dest");
				this.queryBusExchange(startStation, destStation);
				break;
			case 1:
				String lineName = request.getParameter("line"); 
				this.queryBusLine(lineName);
				break;
			case 2:
				String stationName = request.getParameter("station");
				this.queryBusStation(stationName);
				break;
			case 3:
				String currentLocation = request.getParameter("currentLoc");
				String homeLocation = request.getParameter("homeLoc");
				this.queryBusExchange(currentLocation, homeLocation);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		PrintWriter pw = response.getWriter();
		pw.write("ajax!");
		pw.close();
	}

	/**
	 * 
	 * @param startStation Start bus station name
	 * @param destStation Destination bus station name
	 * @throws IOException
	 */
	protected void queryBusExchange(String startStation, String destStation) throws IOException {
		/**
		 * 
		 * 
		 */
	}
	
	/**
	 * 
	 * @param lineName
	 * @throws IOException
	 */
	protected void queryBusLine(String lineName) throws IOException {
		/**
		 * 	Station bla = // query database thingy.
		 * 
		 *  
		 * 
		 */
		
	}
	
	/**
	 * 
	 * @param stationName
	 * @throws IOException
	 */
	protected void queryBusStation(String stationName) throws IOException {
		/**
		 * 
		 * 
		 */
	}
}
