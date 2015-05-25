package com.snail.traffic.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.io.output.*;

import com.snail.traffic.control.Administration;

/**
 * Servlet implementation class FileUpload
 */
@WebServlet("/upload.jsp")
public class FileUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUpload() {
        super();
        // TODO Auto-generated constructor stub
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
		request.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();;
		File file;
		int maxFileSize = 10000 * 1024;
		int maxMemSize = 10000 * 1024;
		
		String contentType = request.getContentType();
		
		if (contentType.indexOf("multipart/form-data") >= 0) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String filePath = request.getRealPath("/uploads");
			
			factory.setRepository(new File(filePath));
			factory.setSizeThreshold(maxMemSize);
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			Administration admin = new Administration();
			
			try {
				List<?> list = upload.parseRequest(request);
				Iterator<?> i = list.iterator();
				boolean isDone = false;
				
				while (i.hasNext()) {
					FileItem fi = (FileItem)i.next();
					if (!fi.isFormField()) {
						String fieldName = fi.getFieldName();
						String fileName = fi.getName();
						boolean isInMemory = fi.isInMemory();
						long sizeInBytes = fi.getSize();
						
						if (fileName.lastIndexOf("\\") >= 0) {
							fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						}
						file = new File(filePath, fileName);
						fi.write(file);
						/*
						System.out.println("Uploaded to Path: " + filePath);
						System.out.println("Uploaded File Name: " + fileName);
						*/
						isDone = admin.importExcelData(file.getAbsolutePath());
					}
				}
				
				if (isDone) {
					out.write("<script>parent.uploadSuccess()</script>");
				} else {
					out.write("<script>parent.uploadFailed()</script>");
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		out.close();
	}
}
