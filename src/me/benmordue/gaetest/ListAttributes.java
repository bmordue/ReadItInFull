package me.benmordue.gaetest;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class ListAttributes extends TagSupport {
	private static final long serialVersionUID = -2305196525813686887L;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {

		JspWriter writer = pageContext.getOut();
		
		try {
		//request headers
		writer.write("<h3>Request headers</h3>");
		writer.write("<ul>");
		Enumeration<String> parameters = pageContext.getRequest().getParameterNames();
		while (parameters.hasMoreElements()) {
			//print name : value
			String param = parameters.nextElement();
			writer.write("<li>"+ param +" : " + 
					pageContext.getRequest().getParameter(param) + "</li>");
		}
		writer.write("</ul>");
		
		//request attributes
		writer.write("<h3>Request attributes</h3>");
		writer.write("<ul>");
		Enumeration<String> reqAttrs = pageContext.getAttributeNamesInScope(PageContext.REQUEST_SCOPE);
		while (reqAttrs.hasMoreElements()) {
			//print name : value
			String attrName = reqAttrs.nextElement();
			writer.write("<li>"+ attrName +" : " + 
					pageContext.getAttribute(attrName, PageContext.REQUEST_SCOPE) + "</li>");
		}
		writer.write("</ul>");
		
		//session attributes
		writer.write("<h3>Session attributes</h3>");
		writer.write("<ul>");
		Enumeration<String> sessAttrs = pageContext.getAttributeNamesInScope(PageContext.SESSION_SCOPE);
		while (sessAttrs.hasMoreElements()) {
			//print name : value
			String attrName = sessAttrs.nextElement();
			writer.write("<li>"+ attrName +" : " + 
					pageContext.getAttribute(attrName, PageContext.SESSION_SCOPE) + "</li>");
		}
		writer.write("</ul>");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Tag.SKIP_BODY;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		return Tag.EVAL_PAGE;
	}


}
