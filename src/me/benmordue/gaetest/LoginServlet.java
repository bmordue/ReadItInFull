package me.benmordue.gaetest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class LoginServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4751161450308044087L;
	
	private static final String OAUTH_KEY = "Mh4cpA1NhsrFo1RSuSNw";
	private static final String OAUTH_SECRET = "sJ0lYu4UCUVzWMvk42oOB9bkd1dFPKyBuDLzuShJk";

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		HttpSession session = req.getSession();

		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setIncludeEntitiesEnabled(true);

		RequestDispatcher rd = null;
		
		String twitterAuthUrl = "";
		
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		twitter.setOAuthConsumer(OAUTH_KEY, OAUTH_SECRET);
		RequestToken requestToken = null;
		try {
			requestToken = twitter.getOAuthRequestToken();

			String token = requestToken.getToken();
			String tokenSecret = requestToken.getTokenSecret();

			logger.info("Request token is " + token);
			logger.info("Request secret is " + tokenSecret);
			
			session.setAttribute("requestToken", requestToken);

			session.setAttribute("twitter", twitter);
			 
			String authUrl = requestToken.getAuthorizationURL();
			 
			req.setAttribute("authUrl", authUrl);
			rd = req.getRequestDispatcher("login.jsp");
			
		} catch (TwitterException e) {
			e.printStackTrace();
			//Ben: exception thrown if we can't get through to Twitter.

			logger.info("twitter.getOAuthRequestToken() threw a TwitterException.");
			logger.warning(e.toString());

			//show failure page
			rd = req.getRequestDispatcher("/failure.html");
		}
	
		//forward to login.jsp if successful; failure.html if not
		try {
			twitterAuthUrl = (String) req.getAttribute("authUrl");
		} catch (Exception e) {
			e.printStackTrace();
			rd = req.getRequestDispatcher("/failure.html");
		}
		
		rd.forward(req, resp);
	}

}
