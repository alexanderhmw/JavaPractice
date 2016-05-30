package scoreEditor;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.Sheet;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class PersonalInfo {
	
	protected String[] pInfo=new String[Scores.dataInfoNum];
	protected double[] pScores=new double[Scores.dataScoreNum];
	protected double pTotal;
	static String[] pEvaluationFilter={"免修","已修","缺考","作弊"};
	protected boolean pEvaluationFlag;
	protected String pEvaluation;
	protected enum EvaluationType{
		GRADE5,GRADE2,SCORE
	}
	static String[] pEvaluationTypeList={"五级制","P/F制","记分制"};

	protected void updateEvaluation(EvaluationType evaType, double[] scoreFactors){
		pTotal=0;
		for(int i=0;i<Scores.dataScoreNum;i++)
		{
			pTotal+=pScores[i]*scoreFactors[i];
		}
		int pTotalInt=(int)(pTotal+0.5);
		if(pEvaluationFlag){
			switch(evaType){
			case GRADE5:
				if(pTotalInt<=100&&pTotalInt>=95){
					pEvaluation="A+";
				}else if(pTotalInt<95&&pTotalInt>=90){
					pEvaluation="A";
				}else if(pTotalInt<90&&pTotalInt>=85){
					pEvaluation="A-";
				}else if(pTotalInt<85&&pTotalInt>=80){
					pEvaluation="B+";
				}else if(pTotalInt<80&&pTotalInt>=75){
					pEvaluation="B";
				}else if(pTotalInt<75&&pTotalInt>=70){
					pEvaluation="B-";
				}else if(pTotalInt<70&&pTotalInt>=60){
					pEvaluation="C";
				}else if(pTotalInt<60&&pTotalInt>=0){
					pEvaluation="D";
				}else{
					pEvaluation="Error";
				}
				break;
			case GRADE2:
				if(pTotalInt<=100&&pTotalInt>=60){
					pEvaluation="P";
				}else if(pTotalInt<60&&pTotalInt>=0){
					pEvaluation="F";
				}else{
					pEvaluation="Error";
				}
				break;
			case SCORE:
				pEvaluation=""+pTotal;
				break;
			}
		}
	}
	
	public void loadPersonalInfo(Sheet sheet, int colId, int rowId, EvaluationType evaType, double[] scoreFactors){
		for(int i=0;i<Scores.dataInfoNum;i++){
			pInfo[i]=sheet.getCell(colId+i,rowId).getContents();
		}
		for(int i=0;i<Scores.dataScoreNum;i++){
			Cell cell=sheet.getCell(colId+Scores.dataInfoNum+i,rowId);
			if(cell.getType()==CellType.NUMBER){
				pScores[i]=Double.parseDouble(cell.getContents());
			}else{
				pScores[i]=0;
			}
		}
		Cell cell=sheet.getCell(colId+Scores.dataInfoNum+Scores.dataScoreNum,rowId);
		pEvaluationFlag=true;
		if(cell.getType()==CellType.LABEL){
			pEvaluation=cell.getContents();
			for(int i=0;i<pEvaluationFilter.length;i++){
				if(pEvaluation.compareTo(pEvaluationFilter[i])==0){
					pEvaluationFlag=false;
					break;
				}
			}
		}
		updateEvaluation(evaType, scoreFactors);
	}
	
	public void savePersonalInfo(WritableSheet sheet, int colId, int rowId) throws RowsExceededException, WriteException{
		WritableFont dataFont = new WritableFont(WritableFont.createFont("宋体"), 9);
		WritableCellFormat dataFormatInfo = new WritableCellFormat (dataFont);
		dataFormatInfo.setBorder(Border.ALL, BorderLineStyle.THIN);
		dataFormatInfo.setWrap(true);
		dataFormatInfo.setAlignment(Alignment.CENTRE);
		dataFormatInfo.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		WritableCellFormat dataFormatScore = new WritableCellFormat (dataFont);
		dataFormatScore.setBorder(Border.ALL, BorderLineStyle.THIN);
		dataFormatScore.setAlignment(Alignment.CENTRE);
		dataFormatScore.setVerticalAlignment(VerticalAlignment.CENTRE);
		
		for(int i=0;i<Scores.dataInfoNum;i++){
			sheet.addCell(new Label(colId+i,rowId,pInfo[i],dataFormatInfo));
		}
		for(int i=0;i<Scores.dataScoreNum;i++){
			sheet.addCell(new Number(colId+Scores.dataInfoNum+i,rowId,pScores[i],dataFormatScore));
		}
		sheet.addCell(new Label(colId+Scores.dataInfoNum+Scores.dataScoreNum,rowId,pEvaluation,dataFormatScore));
	}
}
