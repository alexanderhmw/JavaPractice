package scoreEditor;

import jxl.CellView;
import jxl.Sheet;
import jxl.format.BorderLineStyle;
import jxl.format.Border;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class SheetInfo {

	protected String sheetTitle;
	protected String sheetSemester;
	protected String sheetDepartment;
	protected String sheetClassName;
	protected String sheetTeacher;
	protected String sheetCredit;
	protected String sheetCourseName;
	protected String sheetCourseType;
	protected String sheetExamType;
	protected String sheetDate;	
	protected String[] sheetInfoLables=new String[Scores.dataInfoNum];	
	protected String[] sheetScoreLabels=new String[Scores.dataScoreNum];
	protected String sheetEvaluationLable;
	
	protected CellView[] cellColView=new CellView[Scores.dataInfoNum+Scores.dataScoreNum+Scores.dataEvaluationNum];
	protected CellView[] cellRowView=new CellView[Scores.dataRowOffset];
		
	public void loadSheetInfo(Sheet sheet){
		sheetTitle=sheet.getCell(0,0).getContents();
		sheetSemester="";
		sheetDepartment="";
		sheetClassName=sheet.getCell(0,1).getContents();
		sheetTeacher="";
		sheetCredit="";
		sheetCourseName=sheet.getCell(3,1).getContents();
		sheetCourseType="";
		sheetExamType="";
		sheetDate="";
		
		for(int i=0;i<Scores.dataInfoNum+Scores.dataScoreNum+Scores.dataEvaluationNum;i++){
			cellColView[i]=sheet.getColumnView(i);
		}
		for(int i=0;i<Scores.dataRowOffset;i++){
			cellRowView[i]=sheet.getRowView(i);
		}
		
		for(int i=0;i<Scores.dataInfoNum;i++)
		{
			sheetInfoLables[i]=sheet.getCell(i,2).getContents();
		}
		for(int i=0;i<Scores.dataScoreNum;i++)
		{
			sheetScoreLabels[i]=sheet.getCell(i+Scores.dataInfoNum,2).getContents();
		}
		sheetEvaluationLable=sheet.getCell(Scores.dataInfoNum+Scores.dataScoreNum,2).getContents();
	}
		
	public void saveSheetInfo(WritableSheet sheet) throws RowsExceededException, WriteException{
		WritableFont titleFont = new WritableFont(WritableFont.createFont("黑体"), 16);
		WritableCellFormat titleFormat = new WritableCellFormat (titleFont);
		titleFormat.setAlignment(Alignment.CENTRE);
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
				
		WritableFont infoFont = new WritableFont(WritableFont.createFont("宋体"), 9);
		WritableCellFormat infoFormat = new WritableCellFormat (infoFont); 
		infoFormat.setAlignment(Alignment.LEFT);
		infoFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		WritableFont dataFont = new WritableFont(WritableFont.createFont("宋体"), 9);
		WritableCellFormat dataFormat = new WritableCellFormat (dataFont);
		dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		dataFormat.setAlignment(Alignment.CENTRE);
		dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		sheet.mergeCells(0, 0, 7, 0);		
		sheet.addCell(new Label(0,0,sheetTitle,titleFormat));
		
		for(int i=0;i<Scores.dataInfoNum+Scores.dataScoreNum+Scores.dataEvaluationNum;i++){
			sheet.setColumnView(i, cellColView[i]);
		}
		
		sheet.mergeCells(0, 1, 2, 1);
		sheet.addCell(new Label(0,1,sheetClassName,infoFormat));		
		sheet.mergeCells(3, 1, 7, 1);
		sheet.addCell(new Label(3,1,sheetCourseName,infoFormat));
					
		for(int i=0;i<Scores.dataInfoNum;i++)
		{
			sheet.addCell(new Label(i,2,sheetInfoLables[i],dataFormat));
		}
		for(int i=0;i<Scores.dataScoreNum;i++)
		{
			sheet.addCell(new Label(i+Scores.dataInfoNum,2,sheetScoreLabels[i],dataFormat));
		}
		sheet.addCell(new Label(Scores.dataInfoNum+Scores.dataScoreNum,2,sheetEvaluationLable,dataFormat));
		
		for(int i=0;i<Scores.dataRowOffset;i++){
			sheet.setRowView(i, cellRowView[i]);
		}
	}
}
