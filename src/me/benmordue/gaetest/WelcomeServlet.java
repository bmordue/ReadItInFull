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
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class WelcomeServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6464208852480325683L;

	private static final String OAUTH_KEY = "Mh4cpA1NhsrFo1RSuSNw";
	private static final String OAUTH_SECRET = "sJ0lYu4UCUVzWMvk42oOB9bkd1dFPKyBuDLzuShJk";

    private static final Logger logger = Logger.getLogger(WelcomeServlet.class.getName());

	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		HttpSession session = req.getSession();
	    
	    ReadItUser myUser = (ReadItUser) session.getAttribute("readItUser");
	    
	    if (myUser == null) {
		    //not signed in to GAE app, so redirect to Twitter login page
			RequestDispatcher rd = req.getRequestDispatcher("/login");
			rd.forward(req, resp);
	    } else {
	    	//check for existing Twitter authorisation token
	    	AccessToken aToken = myUser.getTwitterAccessToken();
	    	if (aToken != null) {
				ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setIncludeEntitiesEnabled(true);

				try {
					Twitter twitter = new TwitterFactory(cb.build())
							.getInstance();
					twitter.setOAuthAccessToken(aToken);
					
					//is this necessary if we already have the Twitter access token?
					twitter.setOAuthConsumer(OAUTH_KEY, OAUTH_SECRET);

					session.setAttribute("twitter", twitter);

				    //Fully logged in, so redirect to favorites lister
					RequestDispatcher rd = req.getRequestDispatcher("/lister.jsp");
					rd.forward(req, resp);

				} catch (Exception e) {
					// TODO stub catch
					e.printStackTrace();
				}
	    	} else {
			    //no Twitter access token, so redirect to Twitter login page
				RequestDispatcher rd = req.getRequestDispatcher("/login");
				rd.forward(req, resp);
	    	}
	    	
	    }
	    
	    

		
/*		ConfigurationBuilder cb = new ConfigurationBuilder();
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
		RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
		rd.forward(req, resp);
		*/
		
	}

}
