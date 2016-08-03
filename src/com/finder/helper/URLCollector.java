package com.finder.helper;

import java.io.Serializable;

public class URLCollector implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -590065250753653037L;

	private String URLType = null;
	
	private int URLResponseCode = 0;
	
	private String ParentURL = null;
	
	private String ChildURL = null;
	
	private String URLAnchorText = null;

	public String getURLType() {
		return URLType;
	}

	public void setURLType(String uRLType) {
		URLType = uRLType;
	}

	public int getURLResponseCode() {
		return URLResponseCode;
	}

	public void setURLResponseCode(int uRLResponseCode) {
		URLResponseCode = uRLResponseCode;
	}

	public String getParentURL() {
		return ParentURL;
	}

	public void setParentURL(String parentURL) {
		ParentURL = parentURL;
	}

	public String getChildURL() {
		return ChildURL;
	}

	public void setChildURL(String childURL) {
		ChildURL = childURL;
	}

	public String getURLAnchorText() {
		return URLAnchorText;
	}

	public void setURLAnchorText(String uRLAnchorText) {
		URLAnchorText = uRLAnchorText;
	}

}
