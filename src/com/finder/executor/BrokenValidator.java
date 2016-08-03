package com.finder.executor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.finder.base.Validator;
import com.finder.helper.FinderConstants;
import com.finder.helper.RequestData;
import com.finder.helper.RequestDataMapper;
import com.finder.helper.URLCollector;
import com.finder.helper.ValidationUtils;

public class BrokenValidator implements Validator{
	
	private static final Logger logger =Logger.getLogger(BrokenValidator.class);
	
	private RequestData requestData;
	
	public BrokenValidator(RequestData requestData){
		this.requestData = requestData;
	}

	
	public boolean checkStartUrl(List<String> allURLs, String proxyHost, String proxyPort){
		boolean valid = false;
		int invalidCount = 0;
		Proxy proxy = null;
		int responseCode = 0;
		if(proxyHost != null && proxyPort != null){
			int port = FinderConstants.DEFAULT_PROXY_PORT;
			try{
				port = Integer.parseInt(proxyPort);
			}catch(NumberFormatException e){
				port = FinderConstants.DEFAULT_PROXY_PORT;
				logger.error("ERROR : UNKNOWN FORMATTED PROXY PORT PROVIDED. Message : "+e.getMessage());
				logger.info("SETTING PORT TO DEFAULT = "+port);
			}
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
		}
		URL startlink;
		HttpURLConnection urlConn;
		for(String URL : allURLs){
			URL = URL.trim();
			URL = URL.replaceAll(" ", "%20");
		try{
			startlink = new URL(URL);
			if(proxy == null){
				urlConn = (HttpURLConnection) startlink.openConnection();
				logger.info("OPENING CONNECTION (without Proxy)...");
			}else{
				urlConn = (HttpURLConnection) startlink.openConnection(proxy);
				logger.info("OPENING CONNECTION (with Proxy)...");
			}
			urlConn.setConnectTimeout(FinderConstants.CONNECTION_TIMEOUT_TIME);
			urlConn.setReadTimeout(FinderConstants.CONNECTION_TIMEOUT_TIME);
			try{
				responseCode = urlConn.getResponseCode();
			}catch(IOException i){
				responseCode = 0;
			}
			if((responseCode == HttpURLConnection.HTTP_OK)
					|| (responseCode == HttpURLConnection.HTTP_ACCEPTED)
					|| (responseCode == HttpURLConnection.HTTP_CREATED)
					|| (responseCode == HttpURLConnection.HTTP_MOVED_TEMP)
					|| (responseCode == HttpURLConnection.HTTP_MOVED_PERM)){
				requestData.getCheckedList().add(URL);
				logger.info("START URL IS VALID.");
			}else{
				invalidCount++;
				Execute.invalidReason = "broken";
				logger.warn("START URL IS NOT FOUND. RESPONSE CODE = "+responseCode);
			}
		}catch(MalformedURLException m){
			invalidCount++;
			Execute.invalidReason = "malformed";
			logger.error("MALFORMED URL. Message : "+m.getMessage());
		}
		catch (IOException e){
			invalidCount++;
			Execute.invalidReason = "not responsive for "+ FinderConstants.CONNECTION_TIMEOUT_TIME
					+" SEC";
			logger.warn("NO RESPONSE FOUND WITHIN "+ FinderConstants.CONNECTION_TIMEOUT_TIME
					+" SEC: "+e.getMessage());
		}
		catch(SecurityException s){
			invalidCount++;
			Execute.invalidReason = "behind proxy authentication";
			logger.error("PROXY NEEDS AUTHENTICATION. Message : "+s.getMessage());
		}
		catch(IllegalArgumentException i){
			invalidCount++;
			Execute.invalidReason = "behind wrong proxy";
			logger.error("PROXY HAS WRONG TYPE. Message : "+i.getMessage());
		}
	}
		if(invalidCount == 0){
			valid = true;
		}
		return valid;
	}
	
	List<String> alllink = new ArrayList<String>();
	
	public void setAlllink (List<String> pAlllink)
	{
		alllink.addAll(pAlllink);
	}
	
	public int continiousBrokenCounter = 0;
	
	public int totalChecked = 0;
	
	public void checkedLink (String successURL)
	{
		requestData.getAlreadySuccess().add(successURL);
	}
	
	public String getDomain(String startURL){
		String domain = startURL;
		String[] splitedURL = startURL.split("://");
		if(splitedURL.length > 0 && splitedURL[1].contains("/")){
			char startURLchar[] = splitedURL[1].toCharArray();
			int URLcharCount = startURLchar.length;
			int lastChar = 0;
			for(int i=0; i<URLcharCount; i++){
				if(startURLchar[i] == '/'){
					lastChar = i;
					break;
				}
			}
			domain = splitedURL[0]+"://"+splitedURL[1].substring(0, lastChar);
		}
		return domain;
	}
	
	public List<String> validateAndreturnTraversableList(List<String> loop_links, String baseURL, String proxyHost, String proxyPort, int continuousBrokenCountByUser, int reqid){
		RequestData updatedRequestData = null;
		List<String> loopLinkList = new ArrayList<String>();
		Proxy proxy = null;		
		if(proxyHost != null && proxyPort != null){
			int port = FinderConstants.DEFAULT_PROXY_PORT;
			try{
				port = Integer.parseInt(proxyPort);
			}catch(NumberFormatException e){
				port = FinderConstants.DEFAULT_PROXY_PORT;
				logger.error("ERROR : UNKNOWN FORMATTED PROXY PORT PROVIDED. Message : "+e.getMessage());
				logger.info("SETTING PORT TO DEFAULT = "+port);
			}
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
		}
		for(String url : loop_links)
		{
			url = url.trim();
			url = url.replaceAll(" ", "%20");
			if(reqid != 0){
				//updatedRequestData = (RequestData) DatabaseHandler.getRequest(reqid);
					if(RequestDataMapper.interruptedMap.containsKey(reqid)){// || continiousBrokenCounter >= FinderConstants.CONTINIOUS_BROKEN_LIMIT){
						logger.info("INTERRUPTED BY USER");// or >50 CONTINIOUS BROKEN LINK FOUND");
						requestData.setInterrupted(true);
						requestData.setInterruptionType(FinderConstants.INTERRUPT_BY_USER);
						break;
					}else if(continuousBrokenCountByUser != 0 && continiousBrokenCounter == continuousBrokenCountByUser){
						logger.info("INTERRUPTED DUE TO "+continuousBrokenCountByUser+" BROKEN LINKS FOUND CONTINUOUSLY");
						requestData.setInterrupted(true);
						requestData.setInterruptionType(FinderConstants.INTERRUPT_FOR_BROKEN_LIMIT_REACHED);
						break;
					}
				
				
			}if(reqid == 0 && FinderConstants.MAX_ALLOWED_CRAWL_LIMIT == totalChecked){
				break;
			}
			String domain = getDomain(url);
			if(!alllink.contains(url))
			{	
				logger.info("FETCHING URL :"+url);
				Document doc = null;
				if(proxy != null){
					URL link;
					HttpURLConnection httpURLConnection = null;
					InputStream inStr = null;
					InputStreamReader r = null;
				try{
					link = new URL(url);
					httpURLConnection = (HttpURLConnection)link.openConnection(proxy);
					httpURLConnection.setConnectTimeout(FinderConstants.CONNECTION_TIMEOUT_TIME);
					httpURLConnection.setReadTimeout(FinderConstants.CONNECTION_TIMEOUT_TIME);
					httpURLConnection.connect();
					inStr = httpURLConnection.getInputStream();
					r = new InputStreamReader(inStr);
				}catch(MalformedURLException m){
					
					logger.error("MALFORMED URL. Message : "+m.getMessage());
					continue;
				}
				catch (IOException e){
					logger.warn("NO RESPONSE WITHIN "+FinderConstants.CONNECTION_TIMEOUT_TIME+" SEC FOR URL : "+url);
					continue;
				}
				catch(SecurityException s){
					
					logger.error("PROXY NEEDS AUTHENTICATION. Message : "+s.getMessage());
					continue;
				}
				catch(IllegalArgumentException i){
					
					logger.error("PROXY HAS WRONG TYPE. Message : "+i.getMessage());
					continue;
				}
				try{
					doc = Jsoup.parse(inStr,r.getEncoding(), domain);
				}catch (java.nio.charset.UnsupportedCharsetException u){
					logger.warn("UNSUPPORTED CHARACTER PRESENT WITHIN URL - "+url);
					continue;
				}catch (IOException io) {
					
					logger.error("ERROR EXTRACTING JSOUP-document. Message : "+io.getMessage());
					continue;
				}finally{
					if(inStr != null){
						try {
							inStr.close();
						} catch (IOException e) {
							logger.warn("CAN'T CLOSE INPUT STREAM. Message : "+e.getMessage());
						}
					}
					if(r != null){
						try {
							r.close();
						} catch (IOException e) {
							logger.warn("CAN'T CLOSE INPUT STREAM READER. Message : "+e.getMessage());
						}
					}
					//close connection
					if(httpURLConnection != null){
						httpURLConnection.disconnect();
					}
				}
				}else{
					try {
						doc = Jsoup.connect(url).timeout(FinderConstants.CONNECTION_TIMEOUT_TIME).get();
					} catch (IOException e) {
						logger.warn("NO RESPONSE WITHIN "+FinderConstants.CONNECTION_TIMEOUT_TIME+" SEC FOR URL: "+url);
						continue;
					}
				}
				//Elements hyperLinks = doc.select("a[href]");
				Elements hyperLinks = doc.select("a");
				Elements imgLinks = doc.select("img[src]");
				Elements cssLinks = doc.select("link[href]");
				Elements jsLinks = doc.select("script[src]");
				for (Element hyperlink : hyperLinks) 
				{
					if(reqid != 0){
						//updatedRequestData = (RequestData) DatabaseHandler.getRequest(reqid);
							if(RequestDataMapper.interruptedMap.containsKey(reqid)){// || continiousBrokenCounter >= FinderConstants.CONTINIOUS_BROKEN_LIMIT){
								logger.info("INTERRUPTED BY USER");// or >50 CONTINIOUS BROKEN LINK FOUND");
								requestData.setInterrupted(true);
								requestData.setInterruptionType(FinderConstants.INTERRUPT_BY_USER);
								break;
							}else if(continuousBrokenCountByUser != 0 && continiousBrokenCounter == continuousBrokenCountByUser){
								logger.info("INTERRUPTED DUE TO "+continuousBrokenCountByUser+" BROKEN LINKS FOUND CONTINIOUSLY");
								requestData.setInterrupted(true);
								requestData.setInterruptionType(FinderConstants.INTERRUPT_FOR_BROKEN_LIMIT_REACHED);
								break;
							}
					}if(reqid == 0 && FinderConstants.MAX_ALLOWED_CRAWL_LIMIT == totalChecked){
						break;
					}
					String filterLink = hyperlink.attr("abs:href");
					String anchorText = hyperlink.text();
					if(anchorText == null || anchorText.equals("") || anchorText.equals(" ") || anchorText.isEmpty()){
						anchorText = FinderConstants.BLANK_ANCHOR_TEXT;
					}
					filterLink = filterLink.trim();
					filterLink = filterLink.replaceAll(" ", "%20");
					if(!filterLink.contains("#") 
							&& !requestData.getAlreadySuccess().contains(filterLink) 
							&& filterLink != null 
							&& !filterLink.equals("") 
							&& filterLink.startsWith("http")
							&& !filterLink.equalsIgnoreCase("http://")
							&& !filterLink.equalsIgnoreCase("https://")){
						String redirectedURL = null;
						if(ValidationUtils.endsWithAnyOneWithin(filterLink, ValidationUtils.docFileExtentions)){
							setURLProperties(proxy, url, filterLink, FinderConstants.DOCLINK, anchorText, requestData.getUrlCollList());
						}else{
							redirectedURL = setURLProperties(proxy, url, filterLink, FinderConstants.HYPERLINK, anchorText, requestData.getUrlCollList());
							if(baseURL != null){
								if(redirectedURL == null && filterLink.startsWith(baseURL)){
									loopLinkList.add(filterLink);
								}if(redirectedURL != null && redirectedURL.startsWith(baseURL)){
									loopLinkList.add(filterLink);
								}
							}
						}
					}
				}
				for(Element imgLink : imgLinks)
				{
					if(reqid == 0 && FinderConstants.MAX_ALLOWED_CRAWL_LIMIT == totalChecked){
						break;
					}
					String filterLink = imgLink.attr("abs:src");
					String anchorText = FinderConstants.BLANK_ANCHOR_TEXT;
					
					filterLink = filterLink.replaceAll(" ", "%20");
					if(!requestData.getAlreadySuccess().contains(filterLink) && filterLink != null && !filterLink.equals("")){
						setURLProperties(proxy, url, filterLink, FinderConstants.IMAGELINK, anchorText, requestData.getUrlCollList());	
					}
				}
				for(Element cssLink : cssLinks)
	        	{
					if(reqid == 0 && FinderConstants.MAX_ALLOWED_CRAWL_LIMIT == totalChecked){
						break;
					}
        			String filterLink = cssLink.attr("abs:href");
        			String anchorText = FinderConstants.BLANK_ANCHOR_TEXT;
        			filterLink = filterLink.replaceAll(" ", "%20");
        			if(filterLink.endsWith(".css") && !requestData.getAlreadySuccess().contains(filterLink) && filterLink != null && !filterLink.equals("")){
        				setURLProperties(proxy, url, filterLink, FinderConstants.CSSLINK, anchorText, requestData.getUrlCollList());
					}
	        	}
				for(Element jsLink : jsLinks)
	        	{
					if(reqid == 0 && FinderConstants.MAX_ALLOWED_CRAWL_LIMIT == totalChecked){
						break;
					}
        			String filterLink = jsLink.attr("abs:src");
        			String anchorText = FinderConstants.BLANK_ANCHOR_TEXT;
        			filterLink = filterLink.replaceAll(" ", "%20");
        			if(filterLink.endsWith(".js") && !requestData.getAlreadySuccess().contains(filterLink) && filterLink != null && !filterLink.equals("")){
        				setURLProperties(proxy, url, filterLink, FinderConstants.JSLINK, anchorText, requestData.getUrlCollList());
					}
	        	}
			}
		}
		setAlllink(loop_links);
		return loopLinkList;
	}
	
	@Override
	public String setURLProperties(Proxy proxy, String parentURL,
			String childURL, String linkType, String anchorText, List<URLCollector> urlCollectors) {
		// TODO Auto-generated method stub
		logger.info("CONTINIOUS BROKEN = "+continiousBrokenCounter);
		String redirectedURL = null;
		URL urlChk = null;
		HttpURLConnection urlConn = null;
		InputStream is = null;
		int resCode = 0;
		URLCollector uc = new URLCollector();
		try{
			urlChk = new URL(childURL);
		
			if(urlChk.getHost() == null || urlChk.getHost().isEmpty() 
					|| urlChk.getProtocol() == null || urlChk.getProtocol().isEmpty()){
				
			}else{
				requestData.getCheckedList().add(childURL);
				totalChecked++;
			logger.info("CHECKING URL = "+childURL);
			try{
				if(proxy != null){
					urlConn = (HttpURLConnection) urlChk.openConnection(proxy);
				}else{
					urlConn = (HttpURLConnection) urlChk.openConnection();
				}
			
			urlConn.setConnectTimeout(FinderConstants.CONNECTION_TIMEOUT_TIME);
			urlConn.setReadTimeout(FinderConstants.CONNECTION_TIMEOUT_TIME);
			urlConn.setInstanceFollowRedirects(false);
			try{
				resCode = urlConn.getResponseCode();
			}catch(IOException e){
				resCode = 0;
			}
			uc.setURLType(linkType);
			uc.setURLResponseCode(resCode);
			uc.setURLAnchorText(anchorText);
			uc.setChildURL(childURL);
			uc.setParentURL(parentURL);
			
			urlCollectors.add(uc);
			
				if(resCode == HttpURLConnection.HTTP_OK){
					checkedLink(childURL);
					requestData.getValidList().add(childURL);
					continiousBrokenCounter = 0;
				}else if(resCode == HttpURLConnection.HTTP_MOVED_PERM
						|| resCode == HttpURLConnection.HTTP_MOVED_TEMP){
					urlConn.connect();
					is = urlConn.getInputStream();
					//URL redirURL = urlConn.getURL();
					redirectedURL = urlConn.getHeaderField("Location");
					checkedLink(childURL);
					requestData.getValidList().add(childURL);
					continiousBrokenCounter = 0;
				}else if(resCode == HttpURLConnection.HTTP_BAD_REQUEST
						|| resCode == HttpURLConnection.HTTP_UNAUTHORIZED
						|| resCode == HttpURLConnection.HTTP_FORBIDDEN
						|| resCode == HttpURLConnection.HTTP_BAD_METHOD
						|| resCode == HttpURLConnection.HTTP_INTERNAL_ERROR
						|| resCode == HttpURLConnection.HTTP_NOT_IMPLEMENTED
						|| resCode == HttpURLConnection.HTTP_BAD_GATEWAY
						|| resCode == HttpURLConnection.HTTP_UNAVAILABLE
						|| resCode == HttpURLConnection.HTTP_GATEWAY_TIMEOUT){
					continiousBrokenCounter++;
				}
			}catch(IOException io){
				uc.setURLType(linkType);
				uc.setURLResponseCode(FinderConstants.NOT_CONNECTED_URL_RESPONSE_CODE);
				uc.setURLAnchorText(anchorText);
				uc.setChildURL(childURL);
				uc.setParentURL(parentURL);
				
				urlCollectors.add(uc);
				logger.error("CAN'T OPEN CONNECTION WITHIN "+FinderConstants.CONNECTION_TIMEOUT_TIME+" SEC. Message : "+io.getMessage());
			}
		}
	}catch(MalformedURLException m){
		uc.setURLType(linkType);
		uc.setURLResponseCode(FinderConstants.MALFORMED_URL_RESPONSE_CODE);
		uc.setURLAnchorText(anchorText);
		uc.setChildURL(childURL);
		uc.setParentURL(parentURL);
			
		urlCollectors.add(uc);
	}
		finally{
			if(urlConn != null){
				urlConn.disconnect();
			}if(is != null){
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			
		}
		return redirectedURL;
	}
}
