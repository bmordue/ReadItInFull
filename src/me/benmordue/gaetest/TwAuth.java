/**
 * 
 */
package me.benmordue.gaetest;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author ben
 * 
 */
public class TwAuth extends TagSupport {
	
	
	private static final Logger logger = Logger.getLogger(TwAuth.class.getName());

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5037275170002988427L;

	private static final String OAUTH_KEY = "Mh4cpA1NhsrFo1RSuSNw";
	private static final String OAUTH_SECRET = "sJ0lYu4UCUVzWMvk42oOB9bkd1dFPKyBuDLzuShJk";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	/*
	 * Twitter login tag logic flow: If there's a fully authenticated user, show
	 * user name. If not, check whether we have permission in the request
	 * parameters. Login and show user name. If we don't have user's permission,
	 * request it by show a redirect URL.
	 */
	@Override
	public int doStartTag() throws JspException {

		boolean bLoggedIn = false;
		JspWriter writer = pageContext.getOut();

		// check whether we're already logged in to Twitter by looking in the
		// web session
		HttpSession session = pageContext.getSession();

		Twitter twitter = (Twitter) session.getAttribute("twitter");
		if (twitter == null) {
			bLoggedIn = false;
			

		} else {
			twitter4j.User twUser = null;
			try {
				twUser = twitter.verifyCredentials();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				bLoggedIn = false;
				e.printStackTrace();
				logger.info("TwAuth tag: twitter.verifyCredentials() threw an exception");
			}
			if (twUser == null) {
				bLoggedIn = false;
			} else {
				bLoggedIn = true;
			}
		}

		if (!bLoggedIn) { // not logged in

			//check whether we have a stored access token
			AccessToken access = restoreAccessToken();
			if (access != null) {
				try {
					session.setAttribute("accessToken", access);
					twitter.setOAuthAccessToken(access);
					bLoggedIn = true;
				} catch (Exception e) {
					//could be more specific: TwitterException
					e.printStackTrace();
				}
			}
		}
			
			//Clumsy. Check again whether we're logged in yet.
		if (!bLoggedIn) {
			
			logger.info("TwAuth tag: still not logged in to Twitter.");
			
			// check whether we have the user permission already
			// String token =
			// pageContext.getRequest().getParameter("oauth_token");
			// Hmm. Apparently (according to functioning login though
			// HomeServlet),
			// token should be saved session token, instead of token sent by
			// twitter
			// as a parameter after obtaining user permission. Is that correct?
			RequestToken requestToken = (RequestToken) session
					.getAttribute("requestToken");
			String verifier = pageContext.getRequest().getParameter(
					"oauth_verifier");
			try {
				AccessToken accessToken = twitter.getOAuthAccessToken(
						requestToken, verifier);
				session.setAttribute("accessToken", accessToken);
				session.removeAttribute("requestToken");
				twitter.setOAuthAccessToken(accessToken);

				// we are now logged in
				bLoggedIn = true;

				// store the permanent accessToken in GAE data store
				saveAccessToken();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		if (bLoggedIn) {
			try {
				writer.write("<p>Signed in as " + twitter.getScreenName() + "</p>");
				writer.write("<p><a href='/logout'>Sign out</a></p>");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// still not logged in
			// as a final option, redirect to Twitter to request user permission
			String authUrl = twitterAuthentication();

			try {
				writer.write("<a id='signin' href='" + authUrl
						+ "'> Sign in using Twitter.</a>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return SKIP_BODY;
	}

	// save Twitter access token to GAE data store
	private void saveAccessToken() {
		// TODO Auto-generated method stub
		
	}

	// attempt to restore Twitter access token from GAE data store
	private AccessToken restoreAccessToken() {
		// TODO Auto-generated method stub
		return null;
	}

	// returns auth_url for Twitter authentication
	private String twitterAuthentication() {
		
		logger.info("Entering TwAuth.twitterAuthentication()");
		
		String authUrl = "/error"; // redirect URL for an error page

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setIncludeEntitiesEnabled(true);

		Twitter tw = new TwitterFactory(cb.build()).getInstance();

		tw.setOAuthConsumer(OAUTH_KEY, OAUTH_SECRET);

		HttpSession session = pageContext.getSession();
		try {
			RequestToken requestToken = tw.getOAuthRequestToken();

			session.setAttribute("requestToken", requestToken);
			session.setAttribute("twitter", tw);

			authUrl = requestToken.getAuthorizationURL();
		} catch (Exception e) {
			// TODO: catch more specific exception types and handle
			// appropriately
			e.printStackTrace();
		}

		return authUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}
