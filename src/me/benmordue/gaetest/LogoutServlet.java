package me.benmordue.gaetest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(CallbackServlet.class.getName());

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8458491760139029946L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//don't create a session if it doesn't already exist
		HttpSession session = req.getSession(false);
		
		session.removeAttribute("readItUser");
		logger.info("LogoutServlet: removed session attribute 'readItUser'.");
		
		// Clear Java session cookie
		Cookie[] cookies = req.getCookies();
		if (cookies != null)
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("JSESSIONID")) {
					cookies[i].setMaxAge(0);
					logger.info("LogoutServlet: SetMaxAge of JSESSIONID to 0.");
				}
			}
		
		//invalidate the session to logout
		session.invalidate();
		
		//redirect "home"
		RequestDispatcher rd = req.getRequestDispatcher("/");
		rd.forward(req, resp);


	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Simply redirect to doPost method
		doGet(req, resp);
	}

}
