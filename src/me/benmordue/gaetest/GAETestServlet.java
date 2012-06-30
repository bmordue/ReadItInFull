package me.benmordue.gaetest;

//Using http://www.winterwell.com/software/jtwitter.php for twitter

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import oauth.signpost.OAuth;

//import winterwell.jtwitter.OAuthSignpostClient;
//import winterwell.jtwitter.Twitter;
//import winterwell.jtwitter.OAuthSignpostClient;

@SuppressWarnings("serial")
public class GAETestServlet extends HttpServlet {
	
	//twitter OAuth details for "Read in full", https://dev.twitter.com/apps/589076
//	private static final String OAUTH_KEY = "Mh4cpA1NhsrFo1RSuSNw";
//	private static final String OAUTH_SECRET = "sJ0lYu4UCUVzWMvk42oOB9bkd1dFPKyBuDLzuShJk";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		pw.println("<html><body>Hello, world.");

		//Credit Daniel Winterstein twitter jar
	//	pw.println("built using <a href=\"http://www.winterwell.com/software/jtwitter.php\">JTwitter</a>.");
		
		// Make an oauth client (you'll want to change this bit)
/*		OAuthSignpostClient oauthClient = new OAuthSignpostClient(
				OAUTH_KEY, OAUTH_SECRET, OAuth.OUT_OF_BAND);
		//"out of band" -- callback is out of band
		//TODO: add a callback
		
		// Open the authorisation page in the user's browser
		oauthClient.authorizeDesktop();
		// get the pin
		String v = oauthClient.askUser("Please enter the verification PIN from Twitter");
		oauthClient.setAuthorizationCode(v);
		// Store the authorisation token details for future use
		String[] accessToken = oauthClient.getAccessToken();
		// Next time we can use new OAuthSignpostClient(OAUTH_KEY, OAUTH_SECRET, 
//		      accessToken[0], accessToken[1]) to avoid authenticating again.

		// Make a Twitter object
//		Twitter twitter = new Twitter("my-name", oauthClient);
		// Print Daniel Winterstein's status
	//	System.out.println(twitter.getStatus("winterstein"));
//		pw.println(twitter.getStatus("winterstein"));
*/		
		pw.println("</body></html>");
	}
}
