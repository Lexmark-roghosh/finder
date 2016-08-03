package com.finder.iofile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.log4j.Logger;

import com.finder.helper.FinderConstants;
import com.finder.helper.RequestData;
import com.finder.helper.URLCollector;

public class ExcelFile {

	private static final Logger logger =Logger.getLogger(ExcelFile.class);
	public String[] sheetNames = {"Hyperlinks","Documents","Images","CSS","JS"};
	public String[] columnNames = {"Response Code","Child Pages","Parent Pages","Anchor Text"};
		
	public String Write(RequestData data){
		List<URLCollector> ucList = data.getUrlCollList();
		String nOutputFile = "/home/roghosh/jboss-4.2.3.GA/downloadedFiles/"+new Date().getTime()+"result.xls";
		File file = new File(nOutputFile);
		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(file);
			WritableSheet excelSheet = null;
			for(int sheet=0; sheet<5; sheet++){
				workbook.createSheet(sheetNames[sheet], sheet);
				excelSheet = workbook.getSheet(sheet);
				//add column headings 
				for(int column=0; column<4; column++){
					Label heading = new Label(column,0,columnNames[column]);
					try{
						excelSheet.addCell(heading);
					}catch (WriteException e) {
						logger.error("FOUND ERROR IN WRITING CELL VALUES : "+e.getMessage());
					}
				}
			}
			int hRow=1;
			int dRow=1;
			int iRow=1;
			int cRow=1;
			int jRow=1;
			for(URLCollector uc : ucList){
				String linkType = uc.getURLType();
				switch(linkType){
				case FinderConstants.HYPERLINK:
					excelSheet = workbook.getSheet(0);
					Label HresCode = new Label(0,hRow,String.valueOf(uc.getURLResponseCode()));
					Label HbrokenLink = new Label(1,hRow,uc.getChildURL());
					Label HparentLink = new Label(2,hRow,uc.getParentURL());
					Label Hanchor = new Label(3,hRow,uc.getURLAnchorText().equals(FinderConstants.BLANK_ANCHOR_TEXT)?" - ":uc.getURLAnchorText());
					try {
						excelSheet.addCell(HresCode);
						excelSheet.addCell(HbrokenLink);
						excelSheet.addCell(HparentLink);
						excelSheet.addCell(Hanchor);
						hRow++;
					} catch (WriteException e) {
						logger.error("FOUND ERROR IN WRITING CELL VALUES : "+e.getMessage());
					}
					break;
				case FinderConstants.DOCLINK:
					excelSheet = workbook.getSheet(1);
					Label DresCode = new Label(0,dRow,String.valueOf(uc.getURLResponseCode()));
					Label DbrokenLink = new Label(1,dRow,uc.getChildURL());
					Label DparentLink = new Label(2,dRow,uc.getParentURL());
					Label Danchor = new Label(3,dRow,uc.getURLAnchorText().equals(FinderConstants.BLANK_ANCHOR_TEXT)?" - ":uc.getURLAnchorText());
					try {
						excelSheet.addCell(DresCode);
						excelSheet.addCell(DbrokenLink);
						excelSheet.addCell(DparentLink);
						excelSheet.addCell(Danchor);
						dRow++;
					} catch (WriteException e) {
						logger.error("FOUND ERROR IN WRITING CELL VALUES : "+e.getMessage());
					}
					break;
				case FinderConstants.IMAGELINK:
					excelSheet = workbook.getSheet(2);
					Label IresCode = new Label(0,iRow,String.valueOf(uc.getURLResponseCode()));
					Label IbrokenLink = new Label(1,iRow,uc.getChildURL());
					Label IparentLink = new Label(2,iRow,uc.getParentURL());
					try {
						excelSheet.addCell(IresCode);
						excelSheet.addCell(IbrokenLink);
						excelSheet.addCell(IparentLink);
						iRow++;
					} catch (WriteException e) {
						logger.error("FOUND ERROR IN WRITING CELL VALUES : "+e.getMessage());
					}
					break;
				case FinderConstants.CSSLINK:
					excelSheet = workbook.getSheet(3);
					Label CresCode = new Label(0,cRow,String.valueOf(uc.getURLResponseCode()));
					Label CbrokenLink = new Label(1,cRow,uc.getChildURL());
					Label CparentLink = new Label(2,cRow,uc.getParentURL());
					try {
						excelSheet.addCell(CresCode);
						excelSheet.addCell(CbrokenLink);
						excelSheet.addCell(CparentLink);
						cRow++;
					} catch (WriteException e) {
						logger.error("FOUND ERROR IN WRITING CELL VALUES : "+e.getMessage());
					}
					break;
				case FinderConstants.JSLINK:
					excelSheet = workbook.getSheet(4);
					Label JresCode = new Label(0,jRow,String.valueOf(uc.getURLResponseCode()));
					Label JbrokenLink = new Label(1,jRow,uc.getChildURL());
					Label JparentLink = new Label(2,jRow,uc.getParentURL());
					try {
						excelSheet.addCell(JresCode);
						excelSheet.addCell(JbrokenLink);
						excelSheet.addCell(JparentLink);
						jRow++;
					} catch (WriteException e) {
						logger.error("FOUND ERROR IN WRITING CELL VALUES : "+e.getMessage());
					}
					break;
				default:
					throw new RuntimeException("INVALID LINK TYPE PASSED !");
				}
			}
			workbook.write();
			workbook.close();
			logger.info("File Written Successfully");
		}catch(WriteException | IOException e){
			logger.error("FILE COULD NOT BE WRITTEN : "+e.getMessage());
		}
		return nOutputFile;
	}
}
