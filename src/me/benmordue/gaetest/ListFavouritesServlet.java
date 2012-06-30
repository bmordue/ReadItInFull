package me.benmordue.gaetest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.URLEntity;

public class ListFavouritesServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3630302006242080561L;



	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Twitter twitter = (Twitter) req.getSession().getAttribute("twitter");

		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();

		String pageParam=req.getParameter("page");
		if (pageParam != null) {
			writePage(twitter, Integer.valueOf(pageParam), writer);
		} else {
			//if no page specified, assume first page (0)
			writePage(twitter, 0, writer);
		}
	}

	
	public void writePage(Twitter twitter, int page, PrintWriter writer) {

		try {
			List<Status> statuses;
			URLEntity[] urlArray = null;

			statuses = twitter.getFavorites(page);
			for (Status status : statuses) {
				writer.write("<tr class='favorite'>");
				writer.write("<td class='name'>@"
						+ status.getUser().getScreenName() + "</td>");
				writer.write("<td class='links'>");

				urlArray = status.getURLEntities();
				if (urlArray != null) {
					for (int i = 0; i < urlArray.length; i++) {
						String urlString = status.getURLEntities()[i].getURL()
								.toString();

//In order to allow SendServlet to remove a favourite after the article has been 
//sent to Instapaper, send the Twitter status instead of the urlString in the form. 						
//						writer.write("<input type='checkbox' name='article' value='"
//										+ urlString + "' />");
						writer.write("<input type='checkbox' name='favId' value='"
							+ status.getId() + "' />");
						
						writer.write("<a href='" + urlString + "'>" + urlString
								+ "</a><br />");
						// writer.write("<a href=\" \">Add to instapaper</a>");
					}

				}
				writer.write("</td>");

				//The full text of the tweet
				writer.write("<td class='status'>" + status.getText()
						+ "</td>");

				writer.write("</tr>");
			}

		} catch (TwitterException e) {
			// Thrown by twitter.getFavorites
			e.printStackTrace();
		} catch (IllegalStateException e) {
			writer.write("Not logged in.");
			//e.printStackTrace()
		}

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
