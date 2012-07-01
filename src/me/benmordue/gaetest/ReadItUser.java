//Class containing the details for a user of ReadItInFull
//This class associates instapaper login details, twitter username, and twitter OAuth 
// authorisation token

//TODO: limitation: twitter <--> instapaper account is a one-to-one relationship; this isn't ideal.

package me.benmordue.gaetest;

import java.util.Date;
import java.util.Calendar;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import twitter4j.auth.AccessToken;

import com.google.appengine.api.datastore.Key;

import org.apache.commons.codec.binary.Base64;

@PersistenceCapable
public class ReadItUser implements java.io.Serializable {


		/**
	 */
	private static final long serialVersionUID = 7202668824812260051L;

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
		private String instapaperUsername;
		
		//TODO: better security for Instapaper password
	    //obfuscated with Base64encode
	    @Persistent
		private String instapaperPassword;
	    

	    
	    //data user was added
	    @Persistent
	    private Date dateCreated;


	    public ReadItUser(String twScreenName) {
	    	dateCreated = Calendar.getInstance().getTime();
	    	
	    	twitterScreenName = twScreenName;
	    }

	    /**
		 * @param twitterAccessToken
		 * @param instapaperUserName
		 * @param instapaperPassword
		 */
		public ReadItUser(AccessToken twitterAccessToken,
				String instapaperUsername, String instapaperPassword) {

			this.twitterAccessToken = twitterAccessToken;
			
			this.instapaperUsername = instapaperUsername;
			
			//takes care of Base64 encoding to hide password
			setInstapaperPassword(instapaperPassword);
			
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
		public String getInstapaperUsername() {
			return instapaperUsername;
		}

		/**
		 * @param instapaperUserName the instapaperUserName to set
		 */
		public void setInstapaperUsername(String instapaperUserName) {
			this.instapaperUsername = instapaperUserName;
		}

		/**
		 * @return the instapaperPassword
		 */
		public String getInstapaperPassword() {
			return new String (
				Base64.decodeBase64(instapaperPassword.getBytes()) );
		}

		/**
		 * @param instapaperPassword the instapaperPassword to set
		 */
		public void setInstapaperPassword(String instapaperPassword) {
			
			//true: URL safe encoding
			this.instapaperPassword = new String (
				Base64.encodeBase64(instapaperPassword.getBytes(), true) );
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
	