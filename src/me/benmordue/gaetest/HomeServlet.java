package me.benmordue.gaetest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
//import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class HomeServlet extends HttpServlet {
//	private static final String OAUTH_KEY = "Mh4cpA1NhsrFo1RSuSNw";
//	private static final String OAUTH_SECRET = "sJ0lYu4UCUVzWMvk42oOB9bkd1dFPKyBuDLzuShJk";

    /**
	 * 
	 */
	private static final long serialVersionUID = 6948322944992560586L;
	private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HttpSession session = req.getSession();
		Twitter twitter = (Twitter) session.getAttribute("twitter");

		RequestToken requestToken = (RequestToken) session.getAttribute("requestToken");
		String verifier = (String) req.getParameter("oauth_verifier");
		AccessToken accessToken = null;
		try {
			accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			req.getSession().removeAttribute("requestToken");
		} catch (Exception e) {
			e.printStackTrace();
			//Ben: throw exception if we can't get through to Twitter.
			//TODO: better way to handle this?
			logger.info("twitter.getOAuthAccessToken() threw a TwitterException.");
			logger.warning(e.toString());
			//throw new ServletException();
		}
/*		
		twitter.setOAuthConsumer(OAUTH_KEY, OAUTH_SECRET);
		AccessToken accessToken = null;
		String token = null, verifier = null;
		try {
			token = (String) req.getParameter("oauth_token");
			verifier = (String) req.getParameter("oauth_verifier");
			logger.info("oauth_token is " + token);
			logger.info("oauth_verifier is " + verifier);
			accessToken = twitter.getOAuthAccessToken(verifier);
		} catch (TwitterException e) {
			e.printStackTrace();
			//Ben: throw exception if we can't get through to Twitter.
			//TODO: better way to handle this?
			logger.info("twitter.getOAuthAccessToken() threw a TwitterException.");
			logger.warning(e.toString());
			//throw new ServletException();
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.info("twitter.getOAuthAccessToken() threw a NPE.");
			logger.warning(e.toString());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			logger.info("twitter.getOAuthAccessToken() threw an IllegalStateException, no token available.");
			logger.warning(e.toString());
		}
		
*/		
		twitter.setOAuthAccessToken(accessToken);
		
		User user = null;
		try {
			user = twitter.verifyCredentials();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//Ben: throw exception if we can't get through to Twitter.
			//TODO: better way to handle this?
			logger.info("twitter.verifyCredentials() threw a TwitterException.");
			logger.warning(e.toString());
//			throw new ServletException();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			logger.info("twitter.verifyCredentials() threw a TwitterException.");
			logger.warning(e.toString());
		}
		
		
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
		if (user != null) {
			pw.println("Logged in as " + user.getName());
		} else {
			pw.println("Did not successfully log in.");
		}
		
		RequestDispatcher rd = req.getRequestDispatcher("/list");
		rd.forward(req, resp);

	}

}
