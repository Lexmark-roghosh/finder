package com.finder.base;

import java.net.Proxy;
import java.util.List;

import com.finder.helper.URLCollector;

public interface Validator {
	
	public String setURLProperties(Proxy proxy, String parentURL, String childURL,
			String linkType, String anchorText, List<URLCollector> urlCollectors);

}
