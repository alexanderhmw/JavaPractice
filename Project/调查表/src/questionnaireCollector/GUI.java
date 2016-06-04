package questionnaireCollector;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class GUI {

	Form form=new Form();
	int curSheet;
	
	private JFrame frame;
	private JPanel panelResult;
	private JPanel panelInput;

	FormTemplate formInput;
	FormTemplate formResult;
	private Box horizontalBox;
	private JButton btnPrev;
	private JButton btnNext;
	private JButton btnSubmit;
	private Box horizontalBox_1;
	private JButton btnLoad;
	private JButton button;
	private Component horizontalGlue;
	private Component horizontalGlue_1;
	private JLabel lblCurSheet;
	
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
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		panelResult = new JPanel();
		splitPane.setLeftComponent(panelResult);
		panelResult.setLayout(new BorderLayout(0, 0));
		
		panelInput = new JPanel();
		panelInput.setMinimumSize(new Dimension(380, 10));
		splitPane.setRightComponent(panelInput);
		panelInput.setLayout(new BorderLayout(0, 0));
				
		horizontalBox = Box.createHorizontalBox();
		frame.getContentPane().add(horizontalBox, BorderLayout.SOUTH);
		
		btnPrev = new JButton("\u4E0A\u4E00\u9875");
		btnPrev.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				curSheet=curSheet-1;
				curSheet=curSheet>=0?curSheet:0;
				setupPanel();
			}
		});
		
		horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		btnPrev.setMaximumSize(new Dimension(400, 100));
		btnPrev.setMinimumSize(new Dimension(200, 23));
		horizontalBox.add(btnPrev);
		
		btnNext = new JButton("\u4E0B\u4E00\u9875");
		btnNext.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curSheet=curSheet+1;
				curSheet=curSheet<form.sheetNumber?curSheet:form.sheetNumber-1;
				setupPanel();
			}
		});
		
		lblCurSheet = new JLabel("\u9875\u7801");
		lblCurSheet.setFont(new Font("微软雅黑", Font.BOLD, 20));
		horizontalBox.add(lblCurSheet);
		btnNext.setMinimumSize(new Dimension(200, 23));
		btnNext.setMaximumSize(new Dimension(400, 100));
		horizontalBox.add(btnNext);
		
		btnSubmit = new JButton("\u786E\u5B9A\u5E76\u63D0\u4EA4");
		btnSubmit.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				form.submitForm();
				curSheet=0;
				setupPanel();
			}
		});
		btnSubmit.setMinimumSize(new Dimension(200, 23));
		btnSubmit.setMaximumSize(new Dimension(400, 100));
		horizontalBox.add(btnSubmit);
		
		horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue_1);
		
		horizontalBox_1 = Box.createHorizontalBox();
		frame.getContentPane().add(horizontalBox_1, BorderLayout.NORTH);
		
		btnLoad = new JButton("\u6253\u5F00\u914D\u7F6E\u6587\u4EF6...");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(form.loadConfigFile(frame)){
						curSheet=0;
						setupPanel();					
					}
				} catch (BiffException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		horizontalBox_1.add(btnLoad);
		
		button = new JButton("\u4FDD\u5B58\u7ED3\u679C");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					form.saveForm();
				} catch (WriteException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		horizontalBox_1.add(button);
	}

	protected void setupPanel(){
		panelInput.removeAll();
		
		JLabel labelInput = new JLabel(form.formName.get(curSheet));
		labelInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelInput.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		labelInput.setHorizontalAlignment(SwingConstants.CENTER);
		labelInput.setFont(new Font("微软雅黑", Font.BOLD, 20));
		
		JScrollPane scrollPaneInput=new JScrollPane(form.formInput.get(curSheet).panel);
		scrollPaneInput.getViewport().setViewPosition(new Point(0,0));
		panelInput.add(scrollPaneInput,BorderLayout.CENTER);
		panelInput.add(labelInput, BorderLayout.NORTH);
		panelInput.validate();
		panelInput.repaint();
		
		JLabel labelResult = new JLabel(form.formName.get(curSheet));
		labelResult.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelResult.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		labelResult.setHorizontalAlignment(SwingConstants.CENTER);
		labelResult.setFont(new Font("微软雅黑", Font.BOLD, 20));
		
		panelResult.removeAll();
		panelResult.add(new JScrollPane(form.formResult.get(curSheet).panel),BorderLayout.CENTER);
		panelResult.add(labelResult, BorderLayout.NORTH);
		panelResult.validate();
		panelResult.repaint();
		
		lblCurSheet.setText(" "+(curSheet+1)+" / " + form.sheetNumber+" 页 ");
	}
}
