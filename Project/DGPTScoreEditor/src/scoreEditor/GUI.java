package scoreEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class GUI {

	File excelFile=null;
	private Scores scores=new Scores();
	
	private JFrame frame;
	private JTextField txfExcelName;
	private JTextField txfDepartment;
	private JTextField txfCourseName;
	private JTextField txfClassName;
	private JTextField txfCourseType;
	private JTextField txfTeacher;
	private JTextField txfExamType;
	private JTextField txfCredit;
	private JTextField txfDate;
	private JTable tblScores;
	private JTextField txfOrder;
	private JTextField txfId;
	private JTextField txfName;
	private JFormattedTextField txfAttendance;
	private JFormattedTextField txfPerform;
	private JFormattedTextField txfPractice;
	private JFormattedTextField txfFinal;
	private JTextField txfTotal;
	private JTextField txfEvaluation;
	
	private JLabel[] scoreLabels=new JLabel[Scores.dataScoreNum];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("东莞职业技术学院成绩单编辑器");
		frame.setFont(new Font("SimHei", Font.BOLD, 12));
		frame.setBounds(100, 100, 1007, 661);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel sheetLoadPanel = new JPanel();
		frame.getContentPane().add(sheetLoadPanel, BorderLayout.SOUTH);
		
		JButton btnLoad = new JButton("载入成绩单...");
		btnLoad.setFocusable(false);
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser=new JFileChooser(".");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel File (*.xls)", "xls");
				chooser.setFileFilter(filter);
				int result=chooser.showOpenDialog(frame);
				if(result==JFileChooser.CANCEL_OPTION){
					return;
				}
				try{
					excelFile=chooser.getSelectedFile();
					scores.importExcelData(excelFile, 0);
					if(scores.personalInfo.length>0){
						DefaultTableModel model=new DefaultTableModel(scores.getExcelData(),scores.getExcelHeader());
						tblScores.setModel(model);
						tblScores.setRowSelectionInterval(0, 0);
						txfExcelName.setText(scores.excelFileName);
						
						String[] tmpStr;
						
						tmpStr=scores.sheetInfo.sheetDepartment.split(Scores.titleSeparator,2);
						txfDepartment.setText(tmpStr.length>=2?tmpStr[1]:"");
						
						tmpStr=scores.sheetInfo.sheetClassName.split(Scores.titleSeparator,2);
						txfClassName.setText(tmpStr.length>=2?tmpStr[1]:"");
						
						tmpStr=scores.sheetInfo.sheetTeacher.split(Scores.titleSeparator,2);
						txfTeacher.setText(tmpStr.length>=2?tmpStr[1]:"");
						
						tmpStr=scores.sheetInfo.sheetCredit.split(Scores.titleSeparator,2);
						txfCredit.setText(tmpStr.length>=2?tmpStr[1]:"");
						
						tmpStr=scores.sheetInfo.sheetCourseName.split(Scores.titleSeparator,2);
						txfCourseName.setText(tmpStr.length>=2?tmpStr[1]:"");
						
						tmpStr=scores.sheetInfo.sheetCourseType.split(Scores.titleSeparator,2);
						txfCourseType.setText(tmpStr.length>=2?tmpStr[1]:"");
						
						tmpStr=scores.sheetInfo.sheetExamType.split(Scores.titleSeparator,2);
						txfExamType.setText(tmpStr.length>=2?tmpStr[1]:"");
						
						tmpStr=scores.sheetInfo.sheetDate.split(Scores.titleSeparator,2);
						txfDate.setText(tmpStr.length>=2?tmpStr[1]:"");
						
						for(int i=0;i<Scores.dataScoreNum;i++){
							scoreLabels[i].setText(scores.sheetInfo.sheetScoreLabels[i]+Scores.titleSeparator);
						}
					}
				}catch(Exception except){
					except.printStackTrace();
				}
			}
		});
		sheetLoadPanel.setLayout(new BoxLayout(sheetLoadPanel, BoxLayout.X_AXIS));
		
		JComboBox cbbEvaluationType = new JComboBox(PersonalInfo.pEvaluationTypeList);
		cbbEvaluationType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				scores.evaType=PersonalInfo.EvaluationType.values()[cbbEvaluationType.getSelectedIndex()];
				scores.updateEvaluations();
				int rowId=tblScores.getSelectedRow();
				if(rowId>=0){
					DefaultTableModel model=new DefaultTableModel(scores.getExcelData(),scores.getExcelHeader());
					tblScores.setModel(model);
					tblScores.setRowSelectionInterval(rowId, rowId);
				}
			}
		});
		sheetLoadPanel.add(cbbEvaluationType);
		sheetLoadPanel.add(btnLoad);
		
		JLabel lblExcelLabel = new JLabel("成绩单文件名称：");
		sheetLoadPanel.add(lblExcelLabel);
		
		txfExcelName = new JTextField();
		txfExcelName.setEditable(false);
		sheetLoadPanel.add(txfExcelName);
		txfExcelName.setColumns(10);
		
		JButton btnSave = new JButton("保存成绩单");
		btnSave.setFocusable(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(excelFile!=null){
					try {
						scores.exportExcelData(excelFile, 0);
					} catch (Exception except) {
						except.printStackTrace();
					}
				}				
			}
		});
		sheetLoadPanel.add(btnSave);
		
		JButton btnSaveAs = new JButton("另存为...");
		btnSaveAs.setFocusable(false);
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser=new JFileChooser(".");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel File (*.xls)", "xls");
				chooser.setFileFilter(filter);
				int result=chooser.showSaveDialog(frame);
				if(result==JFileChooser.CANCEL_OPTION){
					return;
				}
				excelFile=chooser.getSelectedFile();
				if(!excelFile.getName().endsWith(".xls")){
					excelFile=new File(excelFile.getPath()+".xls");
				}
				try {
					scores.exportExcelData(excelFile, 0);
				} catch (Exception except) {
					except.printStackTrace();
				}
			}
		});
		sheetLoadPanel.add(btnSaveAs);
		
		JPanel sheetInfoPanel = new JPanel();
		frame.getContentPane().add(sheetInfoPanel, BorderLayout.NORTH);
		sheetInfoPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sheetInfoPanel.setLayout(new BoxLayout(sheetInfoPanel, BoxLayout.X_AXIS));
		
		Box verticalBox = Box.createVerticalBox();
		sheetInfoPanel.add(verticalBox);
		
		JLabel lblDepartment = new JLabel("开课部门：");
		verticalBox.add(lblDepartment);
		lblDepartment.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JLabel lblCourseName = new JLabel("课程名称：");
		verticalBox.add(lblCourseName);
		lblCourseName.setFont(new Font("Dialog", Font.BOLD, 12));
		
		Box verticalBox_1 = Box.createVerticalBox();
		sheetInfoPanel.add(verticalBox_1);
		
		txfDepartment = new JTextField();
		txfDepartment.setEditable(false);
		verticalBox_1.add(txfDepartment);
		txfDepartment.setColumns(10);
		
		txfCourseName = new JTextField();
		txfCourseName.setEditable(false);
		verticalBox_1.add(txfCourseName);
		txfCourseName.setColumns(10);
		
		Box verticalBox_2 = Box.createVerticalBox();
		sheetInfoPanel.add(verticalBox_2);
		
		JLabel lblClass = new JLabel("班级：");
		lblClass.setFont(new Font("Dialog", Font.BOLD, 12));
		verticalBox_2.add(lblClass);
		
		JLabel lblCourseType = new JLabel("课程性质：");
		lblCourseType.setFont(new Font("Dialog", Font.BOLD, 12));
		verticalBox_2.add(lblCourseType);
		
		Box verticalBox_3 = Box.createVerticalBox();
		sheetInfoPanel.add(verticalBox_3);
		
		txfClassName = new JTextField();
		txfClassName.setEditable(false);
		txfClassName.setColumns(10);
		verticalBox_3.add(txfClassName);
		
		txfCourseType = new JTextField();
		txfCourseType.setEditable(false);
		txfCourseType.setColumns(10);
		verticalBox_3.add(txfCourseType);
		
		Box verticalBox_4 = Box.createVerticalBox();
		sheetInfoPanel.add(verticalBox_4);
		
		JLabel lblTeacher = new JLabel("任课老师：");
		lblTeacher.setFont(new Font("Dialog", Font.BOLD, 12));
		verticalBox_4.add(lblTeacher);
		
		JLabel lblExamType = new JLabel("考核方式：");
		lblExamType.setFont(new Font("Dialog", Font.BOLD, 12));
		verticalBox_4.add(lblExamType);
		
		Box verticalBox_6 = Box.createVerticalBox();
		sheetInfoPanel.add(verticalBox_6);
		
		txfTeacher = new JTextField();
		txfTeacher.setEditable(false);
		txfTeacher.setColumns(10);
		verticalBox_6.add(txfTeacher);
		
		txfExamType = new JTextField();
		txfExamType.setEditable(false);
		txfExamType.setColumns(10);
		verticalBox_6.add(txfExamType);
		
		Box verticalBox_7 = Box.createVerticalBox();
		sheetInfoPanel.add(verticalBox_7);
		
		JLabel lblCreadit = new JLabel("学分：");
		lblCreadit.setFont(new Font("Dialog", Font.BOLD, 12));
		verticalBox_7.add(lblCreadit);
		
		JLabel lblDate = new JLabel("填表日期：");
		lblDate.setFont(new Font("Dialog", Font.BOLD, 12));
		verticalBox_7.add(lblDate);
		
		Box verticalBox_5 = Box.createVerticalBox();
		sheetInfoPanel.add(verticalBox_5);
		
		txfCredit = new JTextField();
		txfCredit.setEditable(false);
		txfCredit.setColumns(10);
		verticalBox_5.add(txfCredit);
		
		txfDate = new JTextField();
		txfDate.setEditable(false);
		txfDate.setColumns(10);
		verticalBox_5.add(txfDate);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Dialog", Font.PLAIN, 12));
		splitPane.setLeftComponent(scrollPane);
		
		tblScores = new JTable(0,8);
		tblScores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblScores.setFont(new Font("Dialog", Font.PLAIN, 12));
		tblScores.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"\u5E8F\u53F7", "\u5B66\u53F7", "\u59D3\u540D", "\u6210\u7EE91", "\u6210\u7EE92", "\u6210\u7EE93", "\u6210\u7EE94", "\u603B\u8BC4"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, Double.class, Double.class, Double.class, Double.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblScores.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						int rowId=tblScores.getSelectedRow();
						if(rowId<0){
							txfOrder.setText("");
							txfId.setText("");
							txfName.setText("");
							txfAttendance.setText("");
							txfPerform.setText("");
							txfPractice.setText("");
							txfFinal.setText("");
							txfTotal.setText("");
							txfEvaluation.setText("");
						}else{
							txfOrder.setText(scores.personalInfo[rowId].pInfo[0]);
							txfId.setText(scores.personalInfo[rowId].pInfo[1]);
							txfName.setText(scores.personalInfo[rowId].pInfo[2]);
							txfAttendance.setText(""+scores.personalInfo[rowId].pScores[0]);
							txfPerform.setText(""+scores.personalInfo[rowId].pScores[1]);
							txfPractice.setText(""+scores.personalInfo[rowId].pScores[2]);
							txfFinal.setText(""+scores.personalInfo[rowId].pScores[3]);
							txfTotal.setText(""+(int)(scores.personalInfo[rowId].pTotal+0.5));
							txfEvaluation.setText(scores.personalInfo[rowId].pEvaluation);
						}						
					}
				});
		scrollPane.setViewportView(tblScores);
		
		Box verticalBox_8 = Box.createVerticalBox();
		splitPane.setRightComponent(verticalBox_8);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		verticalBox_8.add(verticalGlue_1);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setFocusable(true);
		separator_3.setMinimumSize(new Dimension(0, 20));
		separator_3.setMaximumSize(new Dimension(32767, 20));
		separator_3.setBackground(Color.BLACK);
		verticalBox_8.add(separator_3);
		
		Box horizontalBox = Box.createHorizontalBox();
		verticalBox_8.add(horizontalBox);
		
		Box verticalBox_9 = Box.createVerticalBox();
		verticalBox_9.setAlignmentX(Component.CENTER_ALIGNMENT);
		horizontalBox.add(verticalBox_9);
		
		Box horizontalBox_3 = Box.createHorizontalBox();
		verticalBox_9.add(horizontalBox_3);
		
		JLabel lblNewLabel = new JLabel("序号：");
		horizontalBox_3.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 26));
		
		txfOrder = new JTextField();
		txfOrder.setHorizontalAlignment(SwingConstants.CENTER);
		horizontalBox_3.add(txfOrder);
		txfOrder.setMaximumSize(new Dimension(2147483647, 40));
		txfOrder.setFont(new Font("Dialog", Font.PLAIN, 26));
		txfOrder.setEditable(false);
		txfOrder.setColumns(6);
		
		Box horizontalBox_4 = Box.createHorizontalBox();
		verticalBox_9.add(horizontalBox_4);
		
		JLabel lblNewLabel_1 = new JLabel("学号：");
		horizontalBox_4.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 26));
		
		txfId = new JTextField();
		txfId.setHorizontalAlignment(SwingConstants.CENTER);
		horizontalBox_4.add(txfId);
		txfId.setMaximumSize(new Dimension(2147483647, 40));
		txfId.setFont(new Font("Dialog", Font.PLAIN, 26));
		txfId.setEditable(false);
		txfId.setColumns(6);
		
		Box horizontalBox_5 = Box.createHorizontalBox();
		verticalBox_9.add(horizontalBox_5);
		
		JLabel lblNewLabel_2 = new JLabel("姓名：");
		horizontalBox_5.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD, 26));
		
		txfName = new JTextField();
		txfName.setHorizontalAlignment(SwingConstants.CENTER);
		horizontalBox_5.add(txfName);
		txfName.setMaximumSize(new Dimension(2147483647, 40));
		txfName.setFont(new Font("Dialog", Font.PLAIN, 26));
		txfName.setEditable(false);
		txfName.setColumns(6);
		
		JSeparator separator = new JSeparator();
		separator.setFocusable(true);
		separator.setBackground(Color.BLACK);
		separator.setMinimumSize(new Dimension(0, 20));
		separator.setMaximumSize(new Dimension(32767, 20));
		verticalBox_8.add(separator);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		horizontalBox_1.setMaximumSize(new Dimension(2147483647, 160));
		verticalBox_8.add(horizontalBox_1);
		
		Box verticalBox_12 = Box.createVerticalBox();
		verticalBox_12.setFocusCycleRoot(true);
		horizontalBox_1.add(verticalBox_12);
		
		Box horizontalBox_6 = Box.createHorizontalBox();
		verticalBox_12.add(horizontalBox_6);
		
		JLabel lblScore1 = new JLabel("成绩1：");
		horizontalBox_6.add(lblScore1);
		lblScore1.setFont(new Font("Dialog", Font.BOLD, 26));
		scoreLabels[0]=lblScore1;
		
		txfAttendance = new JFormattedTextField(NumberFormat.getNumberInstance());
		txfAttendance.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)e.getSource()).selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(((JTextField)e.getSource()).getText().length()==0){
					((JTextField)e.getSource()).setText("0");
				}
				int rowId=tblScores.getSelectedRow();
				if(rowId>=0){
					int scoreId=0;
					scores.personalInfo[rowId].pScores[scoreId]=Double.valueOf(((JTextField)e.getSource()).getText());
					scores.personalInfo[rowId].updateEvaluation(scores.evaType, scores.scoreFactors);
					tblScores.getModel().setValueAt(new Double(scores.personalInfo[rowId].pScores[scoreId]), rowId, Scores.dataInfoNum+scoreId);
					tblScores.getModel().setValueAt(scores.personalInfo[rowId].pEvaluation, rowId, Scores.dataInfoNum+Scores.dataScoreNum);
					txfTotal.setText(""+(int)(scores.personalInfo[rowId].pTotal+0.5));
					txfEvaluation.setText(scores.personalInfo[rowId].pEvaluation);
				}
			}
		});
		txfAttendance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Component)e.getSource()).transferFocus();
			}
		});
		txfAttendance.setFont(new Font("Dialog", Font.PLAIN, 26));
		horizontalBox_6.add(txfAttendance);
		txfAttendance.setMaximumSize(new Dimension(2147483647, 40));
		txfAttendance.setColumns(2);
		
		JCheckBox ckbAttendance = new JCheckBox("录入");
		ckbAttendance.setFocusable(false);
		ckbAttendance.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				txfAttendance.setEditable(ckbAttendance.isSelected());
				txfAttendance.setFocusable(ckbAttendance.isSelected());
			}
		});
		
		JSpinner spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(80, 20));
		spinner.setFont(new Font("Dialog", Font.BOLD, 20));
		spinner.setFocusable(false);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scores.scoreFactors[0]=Double.valueOf(((JSpinner)e.getSource()).getValue().toString());
				scores.updateEvaluations();
				int rowId=tblScores.getSelectedRow();
				if(rowId>=0){
					DefaultTableModel model=new DefaultTableModel(scores.getExcelData(),scores.getExcelHeader());
					tblScores.setModel(model);
					tblScores.setRowSelectionInterval(rowId, rowId);
				}
			}
		});
		spinner.setModel(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.1));
		horizontalBox_6.add(spinner);
		ckbAttendance.setSelected(true);
		horizontalBox_6.add(ckbAttendance);
		
		Box horizontalBox_7 = Box.createHorizontalBox();
		verticalBox_12.add(horizontalBox_7);
		
		JLabel lblScore2 = new JLabel("成绩2：");
		horizontalBox_7.add(lblScore2);
		lblScore2.setFont(new Font("Dialog", Font.BOLD, 26));
		scoreLabels[1]=lblScore2;
		
		txfPerform = new JFormattedTextField(NumberFormat.getNumberInstance());
		txfPerform.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)e.getSource()).selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(((JTextField)e.getSource()).getText().length()==0){
					((JTextField)e.getSource()).setText("0");
				}
				int rowId=tblScores.getSelectedRow();
				if(rowId>=0){
					int scoreId=1;
					scores.personalInfo[rowId].pScores[scoreId]=Double.valueOf(((JTextField)e.getSource()).getText());
					scores.personalInfo[rowId].updateEvaluation(scores.evaType, scores.scoreFactors);
					tblScores.getModel().setValueAt(new Double(scores.personalInfo[rowId].pScores[scoreId]), rowId, Scores.dataInfoNum+scoreId);
					tblScores.getModel().setValueAt(scores.personalInfo[rowId].pEvaluation, rowId, Scores.dataInfoNum+Scores.dataScoreNum);
					txfTotal.setText(""+(int)(scores.personalInfo[rowId].pTotal+0.5));
					txfEvaluation.setText(scores.personalInfo[rowId].pEvaluation);
				}
			}
		});
		txfPerform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Component)e.getSource()).transferFocus();
			}
		});
		txfPerform.setFont(new Font("Dialog", Font.PLAIN, 26));
		horizontalBox_7.add(txfPerform);
		txfPerform.setMaximumSize(new Dimension(2147483647, 40));
		txfPerform.setColumns(2);
		
		JCheckBox ckbPerform = new JCheckBox("录入");
		ckbPerform.setFocusable(false);
		ckbPerform.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				txfPerform.setEditable(ckbPerform.isSelected());
				txfPerform.setFocusable(ckbPerform.isSelected());
			}
		});
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setPreferredSize(new Dimension(80, 20));
		spinner_1.setFont(new Font("Dialog", Font.BOLD, 20));
		spinner_1.setFocusable(false);
		spinner_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scores.scoreFactors[1]=Double.valueOf(((JSpinner)e.getSource()).getValue().toString());
				scores.updateEvaluations();
				int rowId=tblScores.getSelectedRow();
				if(rowId>=0){
					DefaultTableModel model=new DefaultTableModel(scores.getExcelData(),scores.getExcelHeader());
					tblScores.setModel(model);
					tblScores.setRowSelectionInterval(rowId, rowId);
				}
			}
		});
		spinner_1.setModel(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.1));
		horizontalBox_7.add(spinner_1);
		ckbPerform.setSelected(true);
		horizontalBox_7.add(ckbPerform);
		
		Box horizontalBox_8 = Box.createHorizontalBox();
		verticalBox_12.add(horizontalBox_8);
		
		JLabel lblScore3 = new JLabel("成绩3：");
		horizontalBox_8.add(lblScore3);
		lblScore3.setFont(new Font("Dialog", Font.BOLD, 26));
		scoreLabels[2]=lblScore3;
		
		txfPractice = new JFormattedTextField(NumberFormat.getNumberInstance());
		txfPractice.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)e.getSource()).selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(((JTextField)e.getSource()).getText().length()==0){
					((JTextField)e.getSource()).setText("0");
				}
				int rowId=tblScores.getSelectedRow();
				if(rowId>=0){
					int scoreId=2;
					scores.personalInfo[rowId].pScores[scoreId]=Double.valueOf(((JTextField)e.getSource()).getText());
					scores.personalInfo[rowId].updateEvaluation(scores.evaType, scores.scoreFactors);
					tblScores.getModel().setValueAt(new Double(scores.personalInfo[rowId].pScores[scoreId]), rowId, Scores.dataInfoNum+scoreId);
					tblScores.getModel().setValueAt(scores.personalInfo[rowId].pEvaluation, rowId, Scores.dataInfoNum+Scores.dataScoreNum);
					txfTotal.setText(""+(int)(scores.personalInfo[rowId].pTotal+0.5));
					txfEvaluation.setText(scores.personalInfo[rowId].pEvaluation);
				}
			}
		});
		txfPractice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Component)e.getSource()).transferFocus();
			}
		});
		txfPractice.setFont(new Font("Dialog", Font.PLAIN, 26));
		horizontalBox_8.add(txfPractice);
		txfPractice.setMaximumSize(new Dimension(2147483647, 40));
		txfPractice.setText("");
		txfPractice.setColumns(2);
		
		JCheckBox ckbPractice = new JCheckBox("录入");
		ckbPractice.setFocusable(false);
		ckbPractice.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				txfPractice.setEditable(ckbPractice.isSelected());
				txfPractice.setFocusable(ckbPractice.isSelected());
			}
		});
		
		JSpinner spinner_2 = new JSpinner();
		spinner_2.setPreferredSize(new Dimension(80, 20));
		spinner_2.setFont(new Font("Dialog", Font.BOLD, 20));
		spinner_2.setFocusable(false);
		spinner_2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scores.scoreFactors[2]=Double.valueOf(((JSpinner)e.getSource()).getValue().toString());
				scores.updateEvaluations();
				int rowId=tblScores.getSelectedRow();
				if(rowId>=0){
					DefaultTableModel model=new DefaultTableModel(scores.getExcelData(),scores.getExcelHeader());
					tblScores.setModel(model);
					tblScores.setRowSelectionInterval(rowId, rowId);
				}
			}
		});
		spinner_2.setModel(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.1));
		horizontalBox_8.add(spinner_2);
		ckbPractice.setSelected(true);
		horizontalBox_8.add(ckbPractice);
		
		Box horizontalBox_9 = Box.createHorizontalBox();
		verticalBox_12.add(horizontalBox_9);
		
		JLabel lblScore4 = new JLabel("成绩4：");
		horizontalBox_9.add(lblScore4);
		lblScore4.setFont(new Font("Dialog", Font.BOLD, 26));
		scoreLabels[3]=lblScore4;
		
		txfFinal = new JFormattedTextField(NumberFormat.getNumberInstance());
		txfFinal.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)e.getSource()).selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(((JTextField)e.getSource()).getText().length()==0){
					((JTextField)e.getSource()).setText("0");
				}
				int rowId=tblScores.getSelectedRow();
				if(rowId>=0){
					int scoreId=3;
					scores.personalInfo[rowId].pScores[scoreId]=Double.valueOf(((JTextField)e.getSource()).getText());
					scores.personalInfo[rowId].updateEvaluation(scores.evaType, scores.scoreFactors);
					tblScores.getModel().setValueAt(new Double(scores.personalInfo[rowId].pScores[scoreId]), rowId, Scores.dataInfoNum+scoreId);
					tblScores.getModel().setValueAt(scores.personalInfo[rowId].pEvaluation, rowId, Scores.dataInfoNum+Scores.dataScoreNum);
					txfTotal.setText(""+(int)(scores.personalInfo[rowId].pTotal+0.5));
					txfEvaluation.setText(scores.personalInfo[rowId].pEvaluation);
				}
			}
		});
		txfFinal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				((Component)e.getSource()).transferFocus();
			}
		});
		txfFinal.setFont(new Font("Dialog", Font.PLAIN, 26));
		horizontalBox_9.add(txfFinal);
		txfFinal.setMaximumSize(new Dimension(2147483647, 40));
		txfFinal.setColumns(2);
		
		JCheckBox ckbFinal = new JCheckBox("录入");
		ckbFinal.setFocusable(false);
		ckbFinal.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				txfFinal.setEditable(ckbFinal.isSelected());
				txfFinal.setFocusable(ckbFinal.isSelected());
			}
		});
		
		JSpinner spinner_3 = new JSpinner();
		spinner_3.setPreferredSize(new Dimension(80, 20));
		spinner_3.setFont(new Font("Dialog", Font.BOLD, 20));
		spinner_3.setFocusable(false);
		spinner_3.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scores.scoreFactors[3]=Double.valueOf(((JSpinner)e.getSource()).getValue().toString());
				scores.updateEvaluations();
				int rowId=tblScores.getSelectedRow();
				if(rowId>=0){
					DefaultTableModel model=new DefaultTableModel(scores.getExcelData(),scores.getExcelHeader());
					tblScores.setModel(model);
					tblScores.setRowSelectionInterval(rowId, rowId);
				}
			}
		});
		spinner_3.setModel(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.1));
		horizontalBox_9.add(spinner_3);
		ckbFinal.setSelected(true);
		horizontalBox_9.add(ckbFinal);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setMinimumSize(new Dimension(0, 20));
		separator_4.setMaximumSize(new Dimension(32767, 20));
		separator_4.setBackground(Color.BLACK);
		verticalBox_12.add(separator_4);
		
		Box horizontalBox_11 = Box.createHorizontalBox();
		verticalBox_12.add(horizontalBox_11);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_11.add(horizontalGlue);
		
		JCheckBox ckbQuick = new JCheckBox("快速录入");
		ckbQuick.setFocusable(false);
		horizontalBox_11.add(ckbQuick);
		
		JButton btnNext = new JButton("下一个");
		btnNext.setBackground(new Color(220,0,0));
		btnNext.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JButton)e.getSource()).setBackground(new Color(0,220,0));
				if(ckbQuick.isSelected()){
					int rowCount=tblScores.getRowCount();
					if(rowCount>0){
						int rowId=tblScores.getSelectedRow()+1;
						if(rowId<rowCount&&rowId>=0){
							tblScores.setRowSelectionInterval(rowId, rowId);
							((Component)e.getSource()).transferFocus();
						}else{
							JOptionPane.showMessageDialog(frame, "已完成全部成绩录入");
						}
					}
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				((JButton)e.getSource()).setBackground(new Color(220,0,0));
			}
		});
		btnNext.setFont(new Font("Dialog", Font.BOLD, 20));
		horizontalBox_11.add(btnNext);
		verticalBox_12.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txfAttendance, txfPerform, txfPractice, txfFinal, btnNext}));
		btnNext.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					int rowCount=tblScores.getRowCount();
					if(rowCount>0){
						int rowId=tblScores.getSelectedRow()+1;
						if(rowId<rowCount&&rowId>=0){
							tblScores.setRowSelectionInterval(rowId, rowId);
							((Component)e.getSource()).transferFocus();
						}else{
							JOptionPane.showMessageDialog(frame, "已完成全部成绩录入");
						}
					}
				}
			}
		});
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowCount=tblScores.getRowCount();
				if(rowCount>0){
					int rowId=tblScores.getSelectedRow()+1;
					if(rowId<rowCount&&rowId>=0){
						tblScores.setRowSelectionInterval(rowId, rowId);
						((Component)e.getSource()).transferFocus();
					}else{
						JOptionPane.showMessageDialog(frame, "已完成全部成绩录入");
					}
				}
			}
		});
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setFocusable(true);
		separator_2.setMaximumSize(new Dimension(20, 32767));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBackground(Color.BLACK);
		horizontalBox_1.add(separator_2);
		
		Box verticalBox_10 = Box.createVerticalBox();
		horizontalBox_1.add(verticalBox_10);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		verticalBox_10.add(horizontalBox_2);
		
		JLabel lblNewLabel_3 = new JLabel("总分");
		lblNewLabel_3.setFont(new Font("Dialog", Font.BOLD, 26));
		horizontalBox_2.add(lblNewLabel_3);
		
		txfTotal = new JTextField();
		horizontalBox_2.add(txfTotal);
		txfTotal.setToolTipText("");
		txfTotal.setMaximumSize(new Dimension(2147483647, 80));
		txfTotal.setHorizontalAlignment(SwingConstants.CENTER);
		txfTotal.setFont(new Font("Dialog", Font.PLAIN, 30));
		txfTotal.setEditable(false);
		txfTotal.setColumns(2);
		
		Box horizontalBox_10 = Box.createHorizontalBox();
		verticalBox_10.add(horizontalBox_10);
		
		JLabel lblNewLabel_8 = new JLabel("总评");
		lblNewLabel_8.setFont(new Font("Dialog", Font.BOLD, 26));
		horizontalBox_10.add(lblNewLabel_8);
		
		txfEvaluation = new JTextField();
		horizontalBox_10.add(txfEvaluation);
		txfEvaluation.setToolTipText("");
		txfEvaluation.setMaximumSize(new Dimension(2147483647, 80));
		txfEvaluation.setHorizontalAlignment(SwingConstants.CENTER);
		txfEvaluation.setFont(new Font("Dialog", Font.PLAIN, 30));
		txfEvaluation.setEditable(false);
		txfEvaluation.setColumns(2);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setFocusable(true);
		separator_1.setMinimumSize(new Dimension(0, 20));
		separator_1.setMaximumSize(new Dimension(32767, 20));
		separator_1.setBackground(Color.BLACK);
		verticalBox_8.add(separator_1);
		
		Box hbxSpecialEva = Box.createHorizontalBox();
		verticalBox_8.add(hbxSpecialEva);
		
		Component glueSELeft = Box.createHorizontalGlue();
		hbxSpecialEva.add(glueSELeft);
		
		for(int i=0;i<PersonalInfo.pEvaluationFilter.length;i++){
			JButton btnTmpSE = new JButton(PersonalInfo.pEvaluationFilter[i]);
			btnTmpSE.setBackground(new Color(220,220,0));
			btnTmpSE.setFont(new Font("Dialog", Font.BOLD, 20));
			btnTmpSE.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int rowCount=tblScores.getRowCount();
					if(rowCount>0){
						int rowId=tblScores.getSelectedRow();
						if(rowId>=0){
							scores.personalInfo[rowId].pEvaluationFlag=false;
							scores.personalInfo[rowId].pEvaluation=((JButton)e.getSource()).getText();
							tblScores.getModel().setValueAt(scores.personalInfo[rowId].pEvaluation, rowId, Scores.dataInfoNum+Scores.dataScoreNum);
							txfEvaluation.setText(scores.personalInfo[rowId].pEvaluation);
							btnNext.requestFocusInWindow();
						}
					}
				}
			});
			hbxSpecialEva.add(btnTmpSE);
		}
		
		JButton btnTmpSEC= new JButton("取消特殊评价");
		btnTmpSEC.setBackground(new Color(0,220,0));
		btnTmpSEC.setFont(new Font("Dialog", Font.BOLD, 20));
		btnTmpSEC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowCount=tblScores.getRowCount();
				if(rowCount>0){
					int rowId=tblScores.getSelectedRow();
					if(rowId>=0){
						scores.personalInfo[rowId].pEvaluationFlag=true;
						scores.personalInfo[rowId].updateEvaluation(scores.evaType, scores.scoreFactors);
						tblScores.getModel().setValueAt(scores.personalInfo[rowId].pEvaluation, rowId, Scores.dataInfoNum+Scores.dataScoreNum);
						txfEvaluation.setText(scores.personalInfo[rowId].pEvaluation);
						btnNext.requestFocusInWindow();
					}
				}
			}
		});
		hbxSpecialEva.add(btnTmpSEC);
		
		Component glueSERight = Box.createHorizontalGlue();
		hbxSpecialEva.add(glueSERight);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setFocusable(true);
		separator_5.setMinimumSize(new Dimension(0, 20));
		separator_5.setMaximumSize(new Dimension(32767, 20));
		separator_5.setBackground(Color.BLACK);
		verticalBox_8.add(separator_5);
		
		Component verticalGlue = Box.createVerticalGlue();
		verticalBox_8.add(verticalGlue);
	}
}
