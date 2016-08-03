package com.finder.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.finder.executor.BrokenValidator;

public class RequestData implements Serializable,Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6829185919151712134L;

	private boolean started;
	
	private boolean completed;
	
	private boolean interrupted;
	
	private Date completedDateTime;
	
	private String startURL;
	
	private String groupName;
	
	private String requestType;
	
	private String interruptionType = null;
	
	public String getInterruptionType() {
		return interruptionType;
	}

	public void setInterruptionType(String interruptionType) {
		this.interruptionType = interruptionType;
	}


	public int refreshCounter = 0;
	
	public boolean isInterrupted() {
		return interrupted;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}
	
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	
	private List<URLCollector> urlCollList = new ArrayList<URLCollector>();
	
	public List<URLCollector> getUrlCollList() {
		return urlCollList;
	}

	public void setUrlCollList(List<URLCollector> urlCollList) {
		this.urlCollList = urlCollList;
	}

	private List<String> groupLinks = new ArrayList<String>();
	
	private List<String> MalformedURLs = new ArrayList<String>();
	private List<String> MalformedParentURLs = new ArrayList<String>();
	private List<String> NotConnectedURLs = new ArrayList<String>();
	private List<String> NotConnectedParentURLs = new ArrayList<String>();
	private List<String> validList = new ArrayList<String>();
	private List<String> checkedList = new ArrayList<String>();
	private List<String> alreadySuccess = new ArrayList<String>();

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public Date getCompletedDateTime() {
		return completedDateTime;
	}

	public void setCompletedDateTime(Date completedDateTime) {
		this.completedDateTime = completedDateTime;
	}

	public String getStartURL() {
		return startURL;
	}

	public void setStartURL(String startURL) {
		this.startURL = startURL;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<String> getGroupLinks() {
		return groupLinks;
	}

	public void setGroupLinks(List<String> groupLinks) {
		this.groupLinks = groupLinks;
	}

	public List<String> getMalformedURLs() {
		return MalformedURLs;
	}

	public void setMalformedURLs(List<String> malformedURLs) {
		MalformedURLs = malformedURLs;
	}

	public List<String> getMalformedParentURLs() {
		return MalformedParentURLs;
	}

	public void setMalformedParentURLs(List<String> malformedParentURLs) {
		MalformedParentURLs = malformedParentURLs;
	}

	public List<String> getNotConnectedURLs() {
		return NotConnectedURLs;
	}

	public void setNotConnectedURLs(List<String> notConnectedURLs) {
		NotConnectedURLs = notConnectedURLs;
	}

	public List<String> getNotConnectedParentURLs() {
		return NotConnectedParentURLs;
	}

	public void setNotConnectedParentURLs(List<String> notConnectedParentURLs) {
		NotConnectedParentURLs = notConnectedParentURLs;
	}

	public List<String> getValidList() {
		return validList;
	}

	public void setValidList(List<String> validList) {
		this.validList = validList;
	}

	public List<String> getCheckedList() {
		return checkedList;
	}

	public void setCheckedList(List<String> checkedList) {
		this.checkedList = checkedList;
	}

	public List<String> getAlreadySuccess() {
		return alreadySuccess;
	}

	public void setAlreadySuccess(List<String> alreadySuccess) {
		this.alreadySuccess = alreadySuccess;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	
}
