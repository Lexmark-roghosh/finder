package com.finder.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finder.executor.BrokenValidator;
import com.finder.executor.Execute;
import com.finder.helper.FinderConstants;
import com.finder.iofile.ExcelFile;

/**
 * Servlet implementation class BrokenLinkValidator
 */
public class BrokenLinkValidator extends HttpServlet implements Runnable{
	private static final long serialVersionUID = 1L;
    Execute exe;
    BrokenValidator valid;
    Thread travarse;
    int counter = 0;
    String downloadwithParentExcelPath;
    String downloadwithoutParentExcelPath;
    String proxyHost;
    String proxyPort;
    String startURL;
    boolean checkComplete, checkStart;
    
    public BrokenLinkValidator() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public void run() {
		// TODO Auto-generated method stub
    	checkStart = true;
    	//exe.traverseFullSite(startURL,proxyHost,proxyPort);
    	checkComplete = true;
    	counter++;
	}
    
    public void init() throws ServletException {
    	downloadwithParentExcelPath = System.getProperty("user.dir")+
        		"\\downloadedFiles\\"+new Date().getTime()+"result.xls";
    	downloadwithoutParentExcelPath = System.getProperty("user.dir")+
				"\\downloadedFiles\\"+new Date().getTime()+"result.xls";
    	proxyHost = null;
    	proxyPort = null;
    	/*exe = new Execute();
    	valid = new BrokenValidator();
    	travarse = new Thread(this);
    	checkComplete = false;
    	checkStart = false;*/
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if(!checkComplete){
				response.setIntHeader("Refresh", 3);
		}
				
		if(!checkStart){
		        startURL = request.getParameter(FinderConstants.START_PARAM_NAME);
		        //String findType = request.getParameter(FinderConstants.FIND_TYPE_RADIO_NAME);
		        String ifProxy = request.getParameter(FinderConstants.PROXY_RADIO_NAME_FOR_FULL);
		        if(FinderConstants.PROXY_YES_RADIO_VALUE_FOR_FULL.equals(ifProxy)){
		        	proxyHost = request.getParameter(FinderConstants.PROXY_HOST_NAME_FOR_FULL);
		        	proxyPort = request.getParameter(FinderConstants.PROXY_PORT_NAME_FOR_FULL);
		        }
		        valid = exe.validator;
		        travarse.start();
		}
		        //exe.traverseFullSite(startURL,proxyHost,proxyPort);
		        int checkedCount = valid.checkedList.size();
		        request.setAttribute("checked", checkedCount);
		        int totalValid = valid.validList.size();
		        request.setAttribute("valid", totalValid);
		        
		        int totalBrokenCount = getUniqueBroken(valid.css404ListC).size()
		        						+getUniqueBroken(valid.hyper404ListC).size()
		        						+getUniqueBroken(valid.img404ListC).size()
		        						+getUniqueBroken(valid.js404ListC).size();
		        request.setAttribute("totalbroken", totalBrokenCount);
		        int totalClientSideErrorCount = getUniqueBroken(valid.css40XListC).size()
		        								+getUniqueBroken(valid.hyper40XListC).size()
		        								+getUniqueBroken(valid.img40XListC).size()
		        								+getUniqueBroken(valid.js40XListC).size();
		        request.setAttribute("totalClientError", totalClientSideErrorCount);
		        int totalServerSideErrorCount = getUniqueBroken(valid.css50XListC).size()
												+getUniqueBroken(valid.hyper50XListC).size()
												+getUniqueBroken(valid.img50XListC).size()
												+getUniqueBroken(valid.js50XListC).size();
		        request.setAttribute("totalServerError", totalServerSideErrorCount);
		        int malformedErrorCount = getUniqueBroken(valid.MalformedURLs).size();
		        request.setAttribute("malformed", malformedErrorCount);
		        
		        int notConnectedErrorCount = getUniqueBroken(valid.NotConnectedURLs).size();
		        request.setAttribute("notconnected", notConnectedErrorCount);
		        
		        int totalError = totalBrokenCount+totalClientSideErrorCount+totalServerSideErrorCount
		        		+malformedErrorCount+notConnectedErrorCount;
		        request.setAttribute("totalError", totalError);
		        
		        request.setAttribute("all404broken", exe.getAll404Broken(valid));
		        
		        request.setAttribute("all404parent", exe.getAll404Parent(valid));
		        
		        request.setAttribute("all40Xbroken", exe.getAll40XBroken(valid));
		        
		        request.setAttribute("all40Xparent", exe.getAll40XParent(valid));
		        
		        request.setAttribute("all50Xbroken", exe.getAll50XBroken(valid));
		        
		        request.setAttribute("all50Xparent", exe.getAll50XParent(valid));
		if(checkComplete && counter == 1){
			counter++;
		        ExcelFile excel = new ExcelFile();
		        valid.MalformedURLs.add("--------------End of Malformed URL list-------------------");
		        valid.MalformedParentURLs.add("---------------------------");
		        valid.NotConnectedURLs.add("-----------End of URL list which could not be connected---------");
		        valid.NotConnectedParentURLs.add("-------------------------");
		        valid.hyper404ListC.add("---------END of Hyperlinked URL list with 404 error--------");
		        valid.hyper404ListP.add("---------------------------");
		        valid.hyper40XListC.add("------END of Hyperlinked URL list having Client side error------");
		        valid.hyper40XListP.add("--------------------------");
		        valid.hyper50XListC.add("------END of Hyperlinked URL list having Server side error------");
		        valid.hyper50XListP.add("--------------------------");
		        valid.img404ListC.add("---------END of Image URL list with 404 error--------");
		        valid.img404ListP.add("---------------------------");
		        valid.img40XListC.add("------END of Image URL list having Client side error------");
		        valid.img40XListP.add("--------------------------");
		        valid.img50XListC.add("------END of Image URL list having Server side error------");
		        valid.img50XListP.add("--------------------------");
		        valid.css404ListC.add("---------END of CSS URL list with 404 error--------");
		        valid.css404ListP.add("---------------------------");
		        valid.css40XListC.add("------END of CSS URL list having Client side error------");
		        valid.css40XListP.add("--------------------------");
		        valid.css50XListC.add("------END of CSS URL list having Server side error------");
		        valid.css50XListP.add("--------------------------");
		        valid.js404ListC.add("---------END of JS URL list with 404 error--------");
		        valid.js404ListP.add("---------------------------");
		        valid.js40XListC.add("------END of JS URL list having Client side error------");
		        valid.js40XListP.add("--------------------------");
		        valid.js50XListC.add("------END of JS URL list having Server side error------");
		        valid.js50XListP.add("--------------------------");
		        
		        List<String> hyperChild = new ArrayList<String>();
		        hyperChild.addAll(valid.hyper404ListC);
		        hyperChild.addAll(valid.hyper40XListC);
		        hyperChild.addAll(valid.hyper50XListC);
		        hyperChild.addAll(valid.MalformedURLs);
		        hyperChild.addAll(valid.NotConnectedURLs);
		        
		        List<String> hyperParent = new ArrayList<String>();
		        hyperParent.addAll(valid.hyper404ListP);
		        hyperParent.addAll(valid.hyper40XListP);
		        hyperParent.addAll(valid.hyper50XListP);
		        hyperParent.addAll(valid.MalformedParentURLs);
		        hyperParent.addAll(valid.NotConnectedParentURLs);
		        
		        List<String> imgChild = new ArrayList<String>();
		        imgChild.addAll(valid.img404ListC);
		        imgChild.addAll(valid.img40XListC);
		        imgChild.addAll(valid.img50XListC);
		        
		        List<String> imgParent = new ArrayList<String>();
		        imgParent.addAll(valid.img404ListP);
		        imgParent.addAll(valid.img40XListP);
		        imgParent.addAll(valid.img50XListP);
		        
		        List<String> cssChild = new ArrayList<String>();
		        cssChild.addAll(valid.css404ListC);
		        cssChild.addAll(valid.css40XListC);
		        cssChild.addAll(valid.css50XListC);
		        
		        List<String> cssParent = new ArrayList<String>();
		        cssParent.addAll(valid.css404ListP);
		        cssParent.addAll(valid.css40XListP);
		        cssParent.addAll(valid.css50XListP);
		        
		        List<String> jsChild = new ArrayList<String>();
		        jsChild.addAll(valid.js404ListC);
		        jsChild.addAll(valid.js40XListC);
		        jsChild.addAll(valid.js50XListC);
		        
		        List<String> jsParent = new ArrayList<String>();
		        jsParent.addAll(valid.js404ListP);
		        jsParent.addAll(valid.js40XListP);
		        jsParent.addAll(valid.js50XListP);
		        
		        try{
		        excel.WriteUrlWithParent(hyperParent, hyperChild, imgParent, imgChild, cssParent, cssChild, jsParent, jsChild, downloadwithParentExcelPath);
		        excel.WriteUrlWithoutParent(getUniqueBroken(hyperChild), getUniqueBroken(imgChild), getUniqueBroken(cssChild), getUniqueBroken(jsChild), downloadwithParentExcelPath);
		        request.setAttribute("downloadWithParent", downloadwithParentExcelPath);
		        request.setAttribute("downloadWithoutParent", downloadwithoutParentExcelPath);
		        }catch(Exception e){
		        	// pass any attribute to recognize any failure during write
		        }
		    
		    //response.reset();
		   }
				getServletContext().getRequestDispatcher("/pages/details.jsp").forward(request, response);
				
		        request.setAttribute("ref", counter);
		        //debug
		        String baseUrl = exe.getTraversableBaseLink(startURL);
		        String domain = exe.getDomain(startURL);
		        request.setAttribute("start", baseUrl);
		        request.setAttribute("domain", domain);
		        
		        response.setContentType("text/plain");
			    PrintWriter out = response.getWriter();
			    String title = "Checking...";
			    //out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\"");
			    out.println("<html>");
			    out.println("<head>");
			    out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
			    out.println("<title>"+title+"</title>");
			    out.println("</head>");
			    out.println("<body>");
			    out.println("<p>Link Checked :"+ checkedCount +"</p>");
			    out.println("</body>");
			    out.println("</html>");
			    
		        //debug
	
	}*/
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request,response);
	}
	
}
