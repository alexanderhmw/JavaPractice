package questionnaireCollector;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Form {
	
	static int configStartRow=2;
	File fileInput;
	File fileResult;
	
	WritableWorkbook wResult;
	
	int sheetNumber;
	Vector<String> config=new Vector<String>();
		
	Vector<String> formName=new Vector<String>();
	Vector<FormTemplate> formInput=new Vector<FormTemplate>();
	Vector<FormTemplate> formResult=new Vector<FormTemplate>();
	
	public boolean loadConfigFile(Component parent ) throws IOException, BiffException{
		JFileChooser chooser=new JFileChooser(".");
		FileNameExtensionFilter filter=new FileNameExtensionFilter("Config File (*.txt)", "txt");
		chooser.setFileFilter(filter);
		int result=chooser.showOpenDialog(parent);
		if(result==JFileChooser.CANCEL_OPTION){return false;}
		File file=chooser.getSelectedFile();
		    
		List<String> tmpConfig=Files.readAllLines(file.toPath(),Charset.defaultCharset());
		
		fileInput=new File(file.getParent(),tmpConfig.get(0));
		if(!fileInput.exists()){return false;}
		Workbook wInput=Workbook.getWorkbook(fileInput);
		
		fileResult=new File(file.getParent(),tmpConfig.get(1));
		Workbook wResult;
		if(fileResult.exists()){
			wResult=Workbook.getWorkbook(fileResult);
		}else{
			wResult=Workbook.getWorkbook(fileInput);
		}
		
		
		sheetNumber=tmpConfig.size()-configStartRow;
		config.setSize(sheetNumber);
		for(int i=0;i<sheetNumber;i++){
			config.set(i, tmpConfig.get(i+configStartRow));
		}
		
		formName.setSize(sheetNumber);
		formInput.setSize(sheetNumber);
		formResult.setSize(sheetNumber);
		for(int i=0;i<config.size();i++){
			Sheet sheetInput=wInput.getSheet(i);
			formName.set(i,sheetInput.getName());
			formInput.set(i,new FormTemplate(config.get(i),sheetInput,false));
			Sheet sheetResult=wResult.getSheet(i);
			formResult.set(i,new FormTemplate(config.get(i),sheetResult,true));
		}
		wInput.close();
		wResult.close();
		return true;
	}
	
	public void submitForm(){
		for(int i=0;i<sheetNumber;i++){
			formResult.get(i).accumulateResult(formInput.get(i));
			formInput.get(i).resetResult();
		}
	}
	
	public void saveForm() throws IOException, RowsExceededException, WriteException{
		WritableWorkbook wResult=Workbook.createWorkbook(fileResult);
		for(int i=0;i<sheetNumber;i++){
			WritableSheet sheet=wResult.createSheet(formName.get(i), i);
			formResult.get(i).saveForm(sheet);
		}
		wResult.write();
		wResult.close();
	}
}
