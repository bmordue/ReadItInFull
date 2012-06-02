//Class containing the details for a user of ReadItInFull
//This class associates instapaper login details, twitter username, and twitter OAuth 
// authorisation token

//TODO: limitation: twitter <--> instapaper account is a one-to-one relationship; this isn't ideal.

package me.benmordue.readitinfull;

import java.util.Date;
import java.util.Calendar;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import twitter4j.auth.AccessToken;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable
public class ReadItUser {


	    @PrimaryKey
	    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	    private Key key;

	    @Persistent
	    private String twitterScreenName;
	    
	    @Persistent
	    private long twitterUserId;
	    
	    
		//OAuth access token for Twitter authorisation
		//Twitter4j.AccessToken implements serializable; will be stored in JDO as a 'blob'
	    @Persistent(serialized = "true")
		AccessToken twitterAccessToken;
		
	    @Persistent
		private String instapaperUserName;
		
		//TODO: better security for Instapaper password
	    @Persistent
		private String instapaperPassword;
	    
	    //data user was added
	    @Persistent
	    private Date dateCreated;


	    public ReadItUser() {
	    	dateCreated = Calendar.getInstance().getTime();
	    }

	    /**
		 * @param twitterAccessToken
		 * @param instapaperUserName
		 * @param instapaperPassword
		 */
		public ReadItUser(AccessToken twitterAccessToken,
				String instapaperUserName, String instapaperPassword) {

			this.twitterAccessToken = twitterAccessToken;
			this.instapaperUserName = instapaperUserName;
			this.instapaperPassword = instapaperPassword;
			twitterScreenName = twitterAccessToken.getScreenName();
			twitterUserId = twitterAccessToken.getUserId();

	    	dateCreated = Calendar.getInstance().getTime();

		}

		public Key getKey() {
	        return key;
	    }

		/**
		 * @return the twitterAccessToken
		 */
		public AccessToken getTwitterAccessToken() {
			return twitterAccessToken;
		}

		/**
		 * @param twitterAccessToken the twitterAccessToken to set
		 */
		public void setTwitterAccessToken(AccessToken twitterAccessToken) {
			this.twitterAccessToken = twitterAccessToken;
			
			//does this make twitterScreenName and twitterUserId members redundant?
			//don't think so, cos we could get screenName/userId before access token
			twitterScreenName = twitterAccessToken.getScreenName();
			twitterUserId = twitterAccessToken.getUserId();
		}

		/**
		 * @return the instapaperUserName
		 */
		public String getInstapaperUserName() {
			return instapaperUserName;
		}

		/**
		 * @param instapaperUserName the instapaperUserName to set
		 */
		public void setInstapaperUserName(String instapaperUserName) {
			this.instapaperUserName = instapaperUserName;
		}

		/**
		 * @return the instapaperPassword
		 */
		public String getInstapaperPassword() {
			return instapaperPassword;
		}

		/**
		 * @param instapaperPassword the instapaperPassword to set
		 */
		public void setInstapaperPassword(String instapaperPassword) {
			this.instapaperPassword = instapaperPassword;
		}

		/**
		 * @return the twitterScreenName
		 */
		public String getTwitterScreenName() {
			return twitterScreenName;
		}

		/**
		 * @return the twitterUserId
		 */
		public long getTwitterUserId() {
			return twitterUserId;
		}

		/**
		 * @return the dateCreated
		 */
		public Date getDateCreated() {
			return dateCreated;
		}

		/**
		 * @return whether this user has a stored OAuth access token for Twitter
		 */
		public boolean hasTwitterAccessToken() {
			if (twitterAccessToken == null) {
				return false;
			} else {
				return true;
			}
		}

		
}
	