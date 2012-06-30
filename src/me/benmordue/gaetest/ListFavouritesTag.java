package me.benmordue.gaetest;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import twitter4j.AccountTotals;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.URLEntity;

public class ListFavouritesTag extends TagSupport {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3630302006242080561L;


	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		
		Twitter twitter = (Twitter) pageContext.getSession().getAttribute("twitter");
		
		JspWriter writer = pageContext.getOut();
		
		//If no parameter is passed, write out all lines
		//Else get the page as passed as a tag parameter
		
		Integer pageNum = (Integer) pageContext.getAttribute("page");
		if (pageNum != null) {
			writePage(twitter, pageNum, writer);
		} else {

			try {

				AccountTotals totals = twitter.getAccountTotals();

				int maxPage = totals.getFavorites() / 20;

				for (int i = 0; i < maxPage; i++) {
					writePage(twitter, 0, writer);
				}

			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return Tag.SKIP_BODY;
	}
	
	// write HTML for one page of favourites 
	// TODO: include options, eg items/page
	public void writePage(Twitter twitter, int page, JspWriter writer) {

		try {
			List<Status> statuses = twitter.getFavorites(page);

			for (Status status : statuses) {
				writer.write("<tr class='favorite'>");
				writer.write("<td class='name'>@"
						+ status.getUser().getScreenName() + "</td>");
				writer.write("<td class='links'>");

				URLEntity[] urlArray = status.getURLEntities();
				if (urlArray != null) {
					writer.write("<ul>");
					for (int i = 0; i < urlArray.length; i++) {
						String urlString = status.getURLEntities()[i].getURL().toString();

						writer.write("<input type=\"checkbox\" name=\"article\" value=\""
										+ urlString + "\" />");

						writer.write("<a href=\"" + urlString + "\">" + urlString
								+ "</a>");
					}
					writer.write("</ul>");

				}
				writer.write("</td>");// close td tag for links
				writer.write("<td class='status'>" + status.getText() + "</td>");

				writer.write("</tr>");
			}
		} catch (TwitterException e) {
			// Thrown by twitter.getFavorites
			e.printStackTrace();
		} catch (IOException e) {
			// Thrown by writer.write()
			e.printStackTrace();
		}


	}


	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		return Tag.EVAL_PAGE;
	}

}
