/**
 * 
 */
package com.finder.base;

import java.util.List;

/**
 * @author Rohan
 *
 */
public interface Finder {
	
	public List<String> getAllHyperlinks(String url);
	
	public List<String> getAllImageLinks(String url);
	
	public List<String> getAllHeadLinks(String url);
	
	public List<String> getAllScriptLinks(String url);

}
