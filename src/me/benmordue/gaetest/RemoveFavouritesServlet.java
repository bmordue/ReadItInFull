package me.benmordue.gaetest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;


public class RemoveFavouritesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4654387992160537763L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PrintWriter pw = resp.getWriter();
		resp.setContentType("text/html");
		pw.println("<html><body>");

		Twitter twitter = null;
		try {
			twitter = (Twitter) req.getSession().getAttribute("twitter");
		} catch (Exception e) {
//			pw.println("<h3>Problems: exception.</h3>");
			// send the user back to the login page
			RequestDispatcher rd = req.getRequestDispatcher("/login");
			rd.forward(req, resp);
			return;
		}
		if (twitter == null) {
			pw.println("<h3>Problems: twitter object is null.</h3>");
			return;
		}
	
		String[] favIdNumbers = req.getParameterValues("favId");
		if (favIdNumbers != null) {
	
			for (String favId : favIdNumbers) {

				long favouriteId = 0;
				try {
					favouriteId = Long.valueOf(favId);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					twitter.destroyFavorite(favouriteId);
				} catch (TwitterException e) {
					e.printStackTrace();
				}

			}
		}

		pw.println("</body></html>");

	}


}
