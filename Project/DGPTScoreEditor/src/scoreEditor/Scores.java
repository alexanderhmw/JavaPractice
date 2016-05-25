package scoreEditor;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import jxl.*;
import jxl.read.biff.*;
import jxl.write.*;
import jxl.write.biff.*;
import scoreEditor.PersonalInfo.EvaluationType;

public class Scores {
	
	protected String excelFileName;
	protected SheetInfo sheetInfo=new SheetInfo();
	protected int personalRowCount;
	protected int personalTotalCount;
	protected PersonalInfo[] personalInfo={};
	protected CellView[] cellRowView={};
	
	static int dataInfoNum=3;
	static int dataScoreNum=4;
	protected double[] scoreFactors=new double[dataScoreNum];
	static int dataEvaluationNum=1;
	protected EvaluationType evaType=EvaluationType.GRADE5;
	static int dataRowOffset=3;
	static String titleSeparator="：";
	static String dataSeparator=",";
	
	public Scores(){
		for(int i=0;i<dataScoreNum;i++){
			scoreFactors[i]=1.0;
		}
	}
	
	public void importExcelData(File excelFile, int sheetId) throws IOException{
		excelFileName=excelFile.getName();
		try{
			Workbook w=Workbook.getWorkbook(excelFile);
			Sheet sheet=w.getSheet(sheetId);
			
			sheetInfo.loadSheetInfo(sheet);			
			
			personalRowCount=0;
			while(personalRowCount+dataRowOffset<sheet.getRows()){
				if(sheet.getCell(0,personalRowCount+dataRowOffset).getType()==CellType.EMPTY){					
					break;
				}else{
					personalRowCount++;
				}
			}
			personalTotalCount=personalRowCount;
			personalInfo=new PersonalInfo[personalTotalCount];
			cellRowView=new CellView[personalRowCount];
			for(int i=0;i<personalRowCount;i++){
				cellRowView[i]=sheet.getRowView(i+dataRowOffset);
			}
			
			for(int i=0;i<personalTotalCount;i++)
			{
				personalInfo[i]=new PersonalInfo();
				int colId=0;
				int rowId=i+dataRowOffset;
				personalInfo[i].loadPersonalInfo(sheet, colId, rowId, evaType, scoreFactors);
			}
			
			w.close();
		}catch(BiffException e){
			e.printStackTrace();
		}
	}
	
	public void exportExcelData(File excelFile, int sheetId) throws WriteException, IOException{
		WritableWorkbook w=Workbook.createWorkbook(excelFile);
		WritableSheet sheet=w.createSheet("Sheet1", sheetId);
		
		sheetInfo.saveSheetInfo(sheet);
		
		for(int i=0;i<personalTotalCount;i++)
		{
			int colId=0;
			int rowId=i+dataRowOffset;
			personalInfo[i].savePersonalInfo(sheet, colId, rowId);
			sheet.setRowView(rowId, cellRowView[rowId-dataRowOffset]);
		}
		
		w.write();
		w.close();
	}
	
	public void updateEvaluations(){
		for(int i=0;i<personalInfo.length;i++){
			personalInfo[i].updateEvaluation(evaType, scoreFactors);
		}
	}
	
	public Vector getExcelHeader(){
		Vector data=new Vector();
		for(int i=0;i<dataInfoNum;i++){
			data.add(sheetInfo.sheetInfoLables[i]);
		}
		for(int i=0;i<dataScoreNum;i++){
			data.add(sheetInfo.sheetScoreLabels[i]);
		}
		data.add(sheetInfo.sheetEvaluationLable);
		return data;
	}
	
	public Vector getExcelData(){
		Vector data=new Vector();
		for(int i=0;i<personalTotalCount;i++){
			Vector rowData=new Vector();
			for(int j=0;j<dataInfoNum;j++){
				rowData.add(personalInfo[i].pInfo[j]);
			}
			for(int j=0;j<dataScoreNum;j++){
				rowData.add(new Double(personalInfo[i].pScores[j]));
			}
			rowData.add(personalInfo[i].pEvaluation);
			data.add(rowData);
		}
		return data;
	}
}
