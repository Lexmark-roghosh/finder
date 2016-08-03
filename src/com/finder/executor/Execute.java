package com.finder.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.finder.helper.FinderConstants;
import com.finder.helper.RequestData;
import com.finder.helper.ValidationUtils;


public class Execute {
	
	private static final Logger logger =Logger.getLogger(Execute.class);
	
	private BrokenValidator validator;
	
	private RequestData requestData;

	public Execute(RequestData requestData){
		if(requestData == null){
			throw new RuntimeException("RequestData is null");
		}
		this.requestData = requestData;
		this.validator = new BrokenValidator(requestData);
	}
	
	public String getTraversableBaseLink(String startURL){
		//http://www.lexmark.com/en_us/solutions.html -> http://www.lexmark.com/en_us/
		//http://www.lexmark.com/en_us/               -> http://www.lexmark.com/en_us/
		//http://www.lexmark.com/en_us				  -> http://www.lexmark.com/
		//http://www.lexmark.com					  -> http://www.lexmark.com/
		if(!startURL.endsWith("/")){
			String baseURL = "";
			if(ValidationUtils.endsWithAnyOneWithin(startURL, ValidationUtils.webPageExtentions)){
				
				char startURLchar[] = startURL.toCharArray();
				int URLcharCount = startURLchar.length;
				int lastChar = 0;
				for(int i=URLcharCount-1; i>=0; i--){
					if(startURLchar[i] == '.'){
						lastChar = i;
						break;
					}
				}
				baseURL = startURL.substring(0, lastChar);
				baseURL = baseURL.concat("/");
			}else{
				baseURL = startURL.concat("/");
			}
			return baseURL;
		}
		else{
			return startURL;
		}
	}
	public void executeRequest(String startURL, String proxyHost, String proxyPort, int continuousBrokenCount, int reqid){
		requestData.setStarted(true);
		
		if(requestData.getRequestType().equals(FinderConstants.REQUEST_TYPE_FULL_SITE)){
			List<String> loop_link = new ArrayList<String>();
			LinkedHashSet<String> tempLinkMap= new LinkedHashSet<String>();
			String baseLink = getTraversableBaseLink(startURL);
			//String domain = getDomain(startURL);
			loop_link.add(startURL);
			int length_before = 0;
			int length_after = 0;
			for(int i = 0; i<50; i++)
			{
				tempLinkMap.addAll(loop_link);
				loop_link.clear();
				loop_link.addAll(tempLinkMap);
				length_before = loop_link.size();
				loop_link.addAll(validator.validateAndreturnTraversableList(loop_link, baseLink, proxyHost, proxyPort, continuousBrokenCount, reqid));
				length_after = loop_link.size();
				if(length_before == length_after || requestData.isInterrupted())
				{
					break;
				}
				
			}
			tempLinkMap.clear();
		
		}if(requestData.getRequestType().equals(FinderConstants.REQUEST_TYPE_GROUP_LINK)){
			logger.info("STARTING GROUP LINK EXECUTION");
			List<String> groupURLs = convertGroupLinkStringToList(startURL);
			String baseURL = null;
			
			if(groupURLs.size() > 0){
				validator.validateAndreturnTraversableList(groupURLs, baseURL, proxyHost, proxyPort, continuousBrokenCount, reqid);
			}
		}
		requestData.setCompleted(true);
		requestData.setCompletedDateTime(new Date());
		
		logger.info("END OF VALIDATION PROCESS. TOTAL LINK CHECKED : "+requestData.getCheckedList().size());
		
	}
	
	public boolean validateStartUrl(List<String> startURLs, String proxyHost, String proxyPort){
		boolean valid;
		//if start url is anything other than web page (ex - pdf, image, css etc..) return false
		valid = validator.checkStartUrl(startURLs, proxyHost, proxyPort);
		if(valid && startURLs.size()==1){
			if(ValidationUtils.endsWithAnyOneWithin(startURLs.get(0), ValidationUtils.audioFileExtentions)
					|| ValidationUtils.endsWithAnyOneWithin(startURLs.get(0), ValidationUtils.docFileExtentions)
					|| ValidationUtils.endsWithAnyOneWithin(startURLs.get(0), ValidationUtils.audioFileExtentions)
					|| ValidationUtils.endsWithAnyOneWithin(startURLs.get(0), ValidationUtils.imageFileExtentions)
					|| ValidationUtils.endsWithAnyOneWithin(startURLs.get(0), ValidationUtils.videoFileExtentions)){
				valid = false;
			}else{
				valid = true;
			}
		}
		return valid;
	}
	
	public static String invalidReason = null;
	
	public List<String> convertGroupLinkStringToList(String links){
		List<String> alllinks = new ArrayList<String>();
		String splitedLinks[] = links.split("[\r\n]+");
		if(splitedLinks.length > 0){
			for(String link : splitedLinks){
				link = link.trim();
				logger.info("link = "+link);
				if(link.startsWith("http") && link.contains("://")){
					logger.info("INDIVIDUAL LINK = "+link);
					alllinks.add(link);
				}else{
					link = "http://"+link;
					if(!link.equalsIgnoreCase("http://") || !link.equalsIgnoreCase("https://")){
						logger.info("INDIVIDUAL LINK = "+link);
						alllinks.add(link);
					}
				}
			}
		}
		return alllinks;
	}
	
	public BrokenValidator getValidator() {
		return validator;
	}

	public RequestData getRequestData() {
		return requestData;
	}

}
