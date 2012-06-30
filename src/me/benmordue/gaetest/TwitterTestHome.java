package me.benmordue.gaetest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TwitterTestHome
 */
public class TwitterTestHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TwitterTestHome() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		
		pw.write("<html><body><head><title>TwitterTestHome</title></head>");
		pw.write("<h3>Twitter Test Home</h3>");
		
		pw.write("<ul>");
		
		Enumeration<String> attrNames = (Enumeration<String>) request.getAttributeNames();
		
		while (attrNames.hasMoreElements()) {
			String attrName = attrNames.nextElement();
			pw.write("<li>" + attrName + " : " 
					+ request.getAttribute(attrName) + "</li>");
		}
		
		
		pw.write("</ul>");
		
		pw.write("</body></html>");
		
	}

}
