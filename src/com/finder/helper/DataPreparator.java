package com.finder.helper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class DataPreparator {
	
	private List<URLCollector> listOfUrlCollector = new ArrayList<URLCollector>();
	
	public DataPreparator(List<URLCollector> uc){
		this.listOfUrlCollector = uc;
	}
	public int getChildURLCount(String linkType){
		int i = -1;
		if(linkType != null){
			LinkedHashSet<String> brokenSet= new LinkedHashSet<String>();
			for(URLCollector uc : listOfUrlCollector){
				if(linkType.equals(uc.getURLType())){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						brokenSet.add(uc.getChildURL());
					}
				}
			}
			i = brokenSet.size();
		}
		return i;
	}
	public int getChildURLCount(int responseCode){
		int i = -1;
		if(responseCode > 0){
			LinkedHashSet<String> brokenSet= new LinkedHashSet<String>();
			for(URLCollector uc : listOfUrlCollector){
				if(responseCode == uc.getURLResponseCode()){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						brokenSet.add(uc.getChildURL());
					}
				}
			}
			i = brokenSet.size();
		}
		return i;
	}
	public int getChildURLCount(String linkType, int responseCode){
		int i = -1;
		if(linkType != null && responseCode > 0){
			LinkedHashSet<String> brokenSet = new LinkedHashSet<String>();
			for(URLCollector uc : listOfUrlCollector){
				if((linkType.equals(uc.getURLType())) && (responseCode == uc.getURLResponseCode())){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						brokenSet.add(uc.getChildURL());
					}
				}
			}
			i = brokenSet.size();
		}
		return i;
	}
	public List<String> getChildURLlist(String linkType){
		List<String> brokenList = new ArrayList<String>();
		if(linkType != null){
			for(URLCollector uc : listOfUrlCollector){
				if(linkType.equals(uc.getURLType())){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						brokenList.add(uc.getChildURL());
					}
				}
			}
		}
		return brokenList;
	}
	public List<String> getChildURLlist(int responseCode){
		List<String> brokenList = new ArrayList<String>();
		if(responseCode > 0){
			for(URLCollector uc : listOfUrlCollector){
				if(responseCode == uc.getURLResponseCode()){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						brokenList.add(uc.getChildURL());
					}
				}
			}
		}
		return brokenList;
	}
	public List<String> getChildURLlist(String linkType, int responseCode){
		List<String> brokenList = new ArrayList<String>();
		if(linkType != null && responseCode > 0){
			for(URLCollector uc : listOfUrlCollector){
				if(linkType.equals(uc.getURLType()) && responseCode == uc.getURLResponseCode()){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						brokenList.add(uc.getChildURL());
					}
				}
			}
		}
		return brokenList;
	}
	public List<String> getParentURLlist(String linkType){
		List<String> parentList = new ArrayList<String>();
		if(linkType != null){
			for(URLCollector uc : listOfUrlCollector){
				if(linkType.equals(uc.getURLType())){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						parentList.add(uc.getParentURL());
					}
				}
			}
		}
		return parentList;
	}
	public List<String> getParentURLlist(int responseCode){
		List<String> parentList = new ArrayList<String>();
		if(responseCode > 0){
			for(URLCollector uc : listOfUrlCollector){
				if(responseCode == uc.getURLResponseCode()){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						parentList.add(uc.getParentURL());
					}
				}
			}
		}
		return parentList;
	}
	public List<String> getParentURLlist(String linkType, int responseCode){
		List<String> parentList = new ArrayList<String>();
		if(linkType != null && responseCode > 0){
			for(URLCollector uc : listOfUrlCollector){
				if(linkType.equals(uc.getURLType()) && responseCode == uc.getURLResponseCode()){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						parentList.add(uc.getParentURL());
					}
				}
			}
		}
		return parentList;
	}
	public List<String> getChildURLgroup(List<String> childLists){
		List<String> childList = new ArrayList<String>();
		childList.addAll(childLists);
		List<String> brokenList = new ArrayList<String>();
		if(childList.size() > 0){
			int i = 0;
			while(childList.size() > 0){
				brokenList.add(i, childList.get(0));
				for(int j=1; j<childList.size(); j++){
					if(childList.get(0).equals(childList.get(j))){
						childList.remove(j);
						j = j-1;
					}
				}
				childList.remove(0);
				i++;
			}
		}
		return brokenList;
	}
	public List<List<String>> getParentURLgroup(List<String> childLists, List<String> parentLists){
		List<List<String>> parentListofList = new ArrayList<List<String>>();
		List<String> childList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		childList.addAll(childLists);
		parentList.addAll(parentLists);
		if(childList.size() == parentList.size()){
			if(childList.size() > 0){
				int i = 0;
				while(childList.size() > 0){
					List<String> par = new ArrayList<String>();
					par.add(parentList.get(0));
					for(int j=1; j<childList.size(); j++){
						if(childList.get(0).equals(childList.get(j))){
							par.add(parentList.get(j));
							parentList.remove(j);
							childList.remove(j);
							j = j-1;
						}
					}
					parentListofList.add(i, par);
					parentList.remove(0);
					childList.remove(0);
					i++;
				}
			}
		}else{
			throw new ArrayIndexOutOfBoundsException("Parent & Child list size not matched");
		}
		return parentListofList;
	}
	public List<List<String>> getAnchorTextgroup(List<String> childLists, List<String> parentLists, List<String> anchorTexts){
		List<List<String>> anchorListofList = new ArrayList<List<String>>();
		List<String> childList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		List<String> anchorList = new ArrayList<String>();
		childList.addAll(childLists);
		parentList.addAll(parentLists);
		anchorList.addAll(anchorTexts);
		if(childList.size() == anchorList.size()){
			if(childList.size() > 0){
				int i = 0;
				while(childList.size() > 0){
					List<String> par = new ArrayList<String>();
					par.add(anchorList.get(0));
					for(int j=1; j<childList.size(); j++){
						if(childList.get(0).equals(childList.get(j))){
							par.add(anchorList.get(j));
							anchorList.remove(j);
							childList.remove(j);
							j = j-1;
						}
					}
					anchorListofList.add(i, par);
					anchorList.remove(0);
					childList.remove(0);
					i++;
				}
			}
		}else{
			throw new ArrayIndexOutOfBoundsException("Anchor & Child list size not matched");
		}
		return anchorListofList;
	}
	public List<String> getAnchorTextList(String linkType){
		List<String> anchorList = new ArrayList<String>();
		if(linkType != null){
			for(URLCollector uc : listOfUrlCollector){
				if(linkType.equals(uc.getURLType())){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						anchorList.add(uc.getURLAnchorText());
					}
				}
			}
		}
		return anchorList;
	}
	public List<String> getAnchorTextList(int responseCode){
		List<String> anchorList = new ArrayList<String>();
		if(responseCode > 0){
			for(URLCollector uc : listOfUrlCollector){
				if(responseCode == uc.getURLResponseCode()){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						anchorList.add(uc.getURLAnchorText());
					}
				}
			}
		}
		return anchorList;
	}
	public List<String> getAnchorTextList(String linkType, int responseCode){
		List<String> anchorList = new ArrayList<String>();
		if(linkType != null && responseCode > 0){
			for(URLCollector uc : listOfUrlCollector){
				if(linkType.equals(uc.getURLType()) && responseCode == uc.getURLResponseCode()){
					if((uc.getChildURL() == null) || (uc.getChildURL().isEmpty()) || (uc.getChildURL().equals(""))){
						
					}else{
						anchorList.add(uc.getURLAnchorText());
					}
				}
			}
		}
		return anchorList;
	}
}
