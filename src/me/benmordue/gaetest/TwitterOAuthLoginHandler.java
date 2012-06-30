/**
 * 
 */
package me.benmordue.gaetest;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author ben
 *
 */
public class TwitterOAuthLoginHandler extends TagSupport {
	private static final long serialVersionUID = 6568834392410974626L;

	private static final String OAUTH_KEY = "Mh4cpA1NhsrFo1RSuSNw";
	private static final String OAUTH_SECRET = "sJ0lYu4UCUVzWMvk42oOB9bkd1dFPKyBuDLzuShJk";

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		
		boolean bLoggedIn = false;
		JspWriter writer = pageContext.getOut();
		
		//check whether we're already logged in to Twitter by looking in the web session
		HttpSession session = pageContext.getSession();
		Twitter twitter = (Twitter) session.getAttribute("twitter");
		if (twitter == null) {
			bLoggedIn = false;
			
		} else {
			twitter4j.User twUser = null;
			try {
				twUser = twitter.verifyCredentials();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (twUser == null) {
				bLoggedIn = false;
			} else {
				bLoggedIn = true;
			}
		}
		
		if (bLoggedIn) {
			try {
				writer.write("Signed in as " + twitter.getScreenName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {	//not logged in
			String authUrl = twitterAuthentication();
			
			try {
				writer.write("<a href='" + authUrl +  "'> Sign in to Twitter.</a>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return SKIP_BODY;
	}
	
	//returns auth_url for Twitter authentication
	private String twitterAuthentication() {
		String authUrl = "error";	//redirect URL for an error page
		
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
			//TODO: catch more specific exception types and handle appropriately
			e.printStackTrace();
		}
		
		return authUrl;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}
