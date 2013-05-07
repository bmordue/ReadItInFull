package me.benmordue.gaetest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class SendServlet extends HttpServlet {

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
		
		
		String user = req.getParameter("username");
		String pass = req.getParameter("password");
				
		String baseUrl = "https://www.instapaper.com/api/add?username=" + user + 
			"&password=" + pass;

		//TODO: Probably shouldn't be here... :-/
		try {
			UserManager.persistInstapaperCredentials(twitter.getScreenName(), user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		String[] articles = req.getParameterValues("article");
//		if (articles != null) {
	
		String[] favIdNumbers = req.getParameterValues("favId");
		if (favIdNumbers != null) {
	
			boolean removeFavourites = false;
			//test only
			String[] vals;
			vals = req.getParameterValues("removefavourites");
			
			if (vals != null) {
				removeFavourites = true;
			}
//			pw.println("<h3>req.getParameterValues('removefavourites')[0]: "
//					+ remove + "</h3>");

	
//			for (String article : articles) {
			for (String favId : favIdNumbers) {

				long favouriteId = 0;
				try {
					favouriteId = Long.valueOf(favId);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Status status;
				try {
					status = twitter.showStatus(favouriteId);
					String article = status.getURLEntities()[0].getURL()
							.toString();

					String urlEnc = URLEncoder.encode(article, "UTF-8");
					// URL url = new URL("http://bit.ly/fgS0Ji");
					String fullUrl = baseUrl + "&url=" + urlEnc;

					URL full = new URL(fullUrl);
					HttpURLConnection connect = (HttpURLConnection) full
							.openConnection();

					int respCode = connect.getResponseCode();
					
					String result = (respCode == 201 ? "OK"
							: "Not OK: " + Integer.toString(respCode));
					pw.println(article + " sent to Instapaper. Response: "
							+ result + "<br />");

					// Remove favourite
					// Only remove favourite if article was successfully saved
					// to Instapaper
					if (removeFavourites && (respCode == 201)) {
						twitter.destroyFavorite(favouriteId);
					}
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				catch (IllegalStateException e) {
					e.printStackTrace();
				}
				catch (SocketTimeoutException e) {
					e.printStackTrace();
				}
				catch (Exception e){
					e.printStackTrace();
				} // TODO Stub catch blocks


			}
		}

		pw.println("</body></html>");

	}


}
