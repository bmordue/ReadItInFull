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
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class CallbackServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(CallbackServlet.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -7278170200341996315L;

	/* (non-Javadoc)
	 *
	 * 	Save access token to GAE datastore
	 *	Save twitter object to session
	 *	Then redirect to favourites lister page
	 *
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		//get or create ReadItUser from GAE datastore
		HttpSession session = req.getSession();
		ReadItUser myUser = (ReadItUser) session.getAttribute("readItUser");
		
		//No existing ReadItUser in session, so build a new one
		if (myUser == null) {

			String verifier = req.getParameter("oauth_verifier");
			String tokenString = req.getParameter("oauth_token");
			
			Twitter twitter = (Twitter) session.getAttribute("twitter");
			
			try {
				RequestToken requestToken = (RequestToken) session.getAttribute("requestToken"); 
				AccessToken aToken = twitter.getOAuthAccessToken(requestToken, verifier);

				//get the authorized Twitter user using the token string
				// and verifier string in the twitter callback paramaters
				twitter4j.User twitterUser = twitter.verifyCredentials();
				
				if (twitterUser != null) {
					logger.info("Twitter authentication successful.");
					myUser = UserManager.getOrCreateUser(twitterUser.getScreenName());
					myUser.setTwitterAccessToken(aToken);
					
					//save ReadItUser to datastore
					UserManager.persistUser(myUser);
					
					session.setAttribute("readItUser", myUser);
				} else {
					//twitterUser is null
					logger.info("Twitter authentication failed.");
				}
				
			} catch (StringIndexOutOfBoundsException s) {
				//TODO deal with it!
				s.printStackTrace();
			} catch (TwitterException twException) {
				//TODO stub catch
				twException.printStackTrace();
			} catch (Exception e) {
				//TODO stub catch
				e.printStackTrace();
			}
			 
			
		}
		
		//Redirect to favourites lister page
		RequestDispatcher rd = req.getRequestDispatcher("/lister.jsp");
		rd.forward(req, resp);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
