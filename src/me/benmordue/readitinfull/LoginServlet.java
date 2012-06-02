package me.benmordue.readitinfull;

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
	private static final long serialVersionUID = 3085784586648178364L;

	private static final String OAUTH_KEY = "Mh4cpA1NhsrFo1RSuSNw";
	private static final String OAUTH_SECRET = "sJ0lYu4UCUVzWMvk42oOB9bkd1dFPKyBuDLzuShJk";

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setIncludeEntitiesEnabled(true);
		
		
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		twitter.setOAuthConsumer(OAUTH_KEY, OAUTH_SECRET);
		RequestToken requestToken = null;
		try {
			requestToken = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			e.printStackTrace();
			//Ben: throw exception if we can't get through to Twitter.
			//TODO: better way to handle this?
			logger.info("twitter.getOAuthRequestToken() threw a TwitterException.");
			logger.warning(e.toString());
		}
		//NPE if try/catch above failed; requestToken is null ? 
		String token = requestToken.getToken();
		String tokenSecret = requestToken.getTokenSecret();

		logger.info("Request token is " + token);
		logger.info("Request secret is " + tokenSecret);
		
		
		HttpSession session = req.getSession();
		session.setAttribute("requestToken", requestToken);
		session.setAttribute("twitter", twitter);
		 
		String authUrl = requestToken.getAuthorizationURL();
		 
		req.setAttribute("authUrl", authUrl);
		// why go through this extra intermediary page? Just redirect to authUrl
	//	RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
		RequestDispatcher rd = req.getRequestDispatcher(authUrl);
		rd.forward(req, resp);
	}

}
