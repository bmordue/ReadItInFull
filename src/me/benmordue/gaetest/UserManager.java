package me.benmordue.gaetest;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

//Utility class to manage ReadItUser objects stored in the App Engine data store.

public class UserManager {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
	
	public static ReadItUser getOrCreateUser(String screenName) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ReadItUser user = null;
		
		try {
			// Attempt to retrieve an existing user
			Query query = pm.newQuery(ReadItUser.class);
			query.setFilter("twitterScreenName == screenNameParam");
			// query.setOrdering("hireDate desc");
			query.declareParameters("String screenNameParam");

			try {
				List<ReadItUser> results = (List<ReadItUser>) 
					query.execute(screenName);
				if (results.size() == 1) {
					// Found exactly one hit; return that user
					user = results.get(0);

				} else if (results.isEmpty()) {
					// No existing ReadItUser, so create a new one
					user = new ReadItUser (screenName);
					pm.makePersistent(user);
				} else {
					// Error condition: more than one result
					// TODO: handle error condition
					logger.info("UserManager: more than one persistent " +
							"user with the same screen name.");
				}
			} catch (Exception e) {
				// TODO handle exception
				e.printStackTrace();
			} finally {
				query.closeAll();
			}

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		} finally {
			pm.close();
		}
		// TODO Auto-generated method stub
		return user;
	}
	
	
	public static void persistInstapaperCredentials
			(String screenName, String instapaperUsername, String instapaperPassword) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//TODO Need to check for existing user with this Twitter screen name 
		// before persisting a duplicate user (ie two entries with the same name)!
		try {
			ReadItUser user = getOrCreateUser(screenName);
			
			user.setInstapaperPassword(instapaperPassword);
			user.setInstapaperUsername(instapaperUsername);
			
			pm.makePersistent(user);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		
	}
	
	
	public static void persistUser (ReadItUser user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//TODO Need to check for existing user with this Twitter screen name 
		// before persisting a duplicate user (ie two entries with the same name)!
		try {
			pm.makePersistent(user);
		} finally {
			pm.close();
		}
		
	}

	
}