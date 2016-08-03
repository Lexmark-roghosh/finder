package com.finder.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.finder.executor.Execute;

public class UserRequestData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1867905670298634531L;
	
	private List<RequestData> requestList;

	public List<RequestData> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<RequestData> requestList) {
		this.requestList = requestList;
	}
	
	public void addRequestData(RequestData requestData) {
		if(requestList == null){
			requestList = new ArrayList<RequestData>();
		}
		requestList.add(requestData);
	}
	
	public boolean isAnyInProgressRequest(){
		boolean result = false;
		if(requestList != null && !requestList.isEmpty()){
			for(RequestData requestData:requestList){
				if(requestData.isStarted() && !requestData.isCompleted()){
					result = true;
					break;
				}
			}
		}
		return result;
	}
	
	public RequestData getInProgressRequest(){
		RequestData requestDataInProg = null;
		if(requestList != null && !requestList.isEmpty()){
			for(RequestData requestData:requestList){
				if(requestData.isStarted() && !requestData.isCompleted()){
					requestDataInProg = requestData;
					break;
				}
			}
		}
		return requestDataInProg;
	}
	
	public RequestData getLatestCompletedRequest(){
		RequestData requestDataCompleted_latest = null;
		if(requestList != null && !requestList.isEmpty()){
			List<RequestData> completedRequestList = new ArrayList<RequestData>();
			for(RequestData requestData:requestList){
				if(requestData.isCompleted()){
					completedRequestList.add(requestData);
				}
			}
			
			if(!completedRequestList.isEmpty()){
				for(RequestData completedRequestData : completedRequestList){
					if(requestDataCompleted_latest != null){
						if(completedRequestData.getCompletedDateTime().after(requestDataCompleted_latest.getCompletedDateTime())){
							requestDataCompleted_latest = completedRequestData;
						}
					}else{
						requestDataCompleted_latest = completedRequestData;
					}
				}
			}
			
		}
		
		return requestDataCompleted_latest;
	}
}
