package boundary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import control.ApplicantLogic;
import control.CourseLogic;
import control.GradesLogic;
import control.InstitutionLogic;
import entity.Applicant;
import entity.ApplicantStatus;
import entity.BackgroundEducation;
import entity.PassingGrade;
import entity.RemedialCourse;

public class FrmAfterInterview extends JPanel {

	private JPanel applicantsPnl;
	private JPanel applicantBackGrounds;
	private JPanel applicantsInformationPnl;
	private JPanel remedialCoursesPnl;

	// comboBox
	private JComboBox<String> statusComboBox,comboBoxRemedial,comboBoxGrade;
	private GridBagConstraints gbc2, gbc;
	private JLabel applicantStatusLbl, applicantIdlbl, applicantIdTxtlbl, err;
	private JLabel title,titleRemedial,savedGradeLbl,remedialCourseIdLbl,PassingGradeLbl,remedialCourseName,remedialCourseNameTxt;

	private JTextField passingGradeTxt;

	private JButton editBtn,addRemedialBtn,deleteRemedialBtn,updateRemedialBtn,resetBtn;
	// soundPlayer
	SoundPlayer btnSound;

	private JTable tableBackGrounds, tableRemedialCourses,tableApplicants;
	private Object[] rowBackgrounds;
	private Object[] rowApplicants;
	private Object[] rowRemedialCourses;


	private JScrollPane scrollPaneBackGrounds,scrollRemedialCourses, scrollPaneApplicant;
	private DefaultTableModel modelBackGrounds, modelApplicant,modelRemedialCourse;
	private HashMap<Integer,RemedialCourse> remedialCourses;
	private List<Applicant> applicants;
	ActionListener editButtonListener;


		public  FrmAfterInterview() {

			fetchAndRefresh();
			initComponents();
			createEvents();

		}

		private void createEvents() {
			// TODO Auto-generated method stub
			
			
			comboBoxRemedial.addActionListener(e -> {
			    String selectedValue =comboBoxRemedial.getSelectedItem().toString();
			    int courseId=Integer.parseInt(selectedValue);
			    fetchAndRefresh();
			    
			    RemedialCourse c =remedialCourses.get(courseId);
			    
			    /*remedialCourseIdLbl,PassingGradeLbl,remedialCourseName,remedialCourseNameTxt;private JTextField passingGradeTxt;*/
			    remedialCourseNameTxt.setText(c.getCourseName());
			    passingGradeTxt.setText(String.valueOf(c.getMinRequiredGrade()));
			});
			

			
			editButtonListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int selectedRowIndex = tableApplicants.getSelectedRow(); // Fetch the latest selected row index
					//only after changing status does the panel opens 
					
					if (selectedRowIndex >= 0) {
						String selectedApplicantId = String.valueOf(modelApplicant.getValueAt(selectedRowIndex, 0));

						String selectedValue =getComboStatus();

						if (!selectedValue.isEmpty()) {		
						fetchAndRefresh();
						String status=ApplicantLogic.getInstance().getApplicants().get(selectedApplicantId).getStatus();

				
						
						// Updating the stage for the selected applicant
						Boolean editApplicant = ApplicantLogic.getInstance().updateStatusForApplicant(selectedApplicantId,
								selectedValue);
						fetchAndRefresh();
						if (ApplicantStatus.conditionally_accepted.toString().equals(selectedValue)) {
					    	remedialCoursesPnl.setVisible(true);
					    	remedialCoursesPnl.setVisible(true);
					    	addRemedialBtn.setVisible(true);
					    	deleteRemedialBtn.setVisible(true);
					    	updateRemedialBtn.setVisible(true);
					    }else {
					    	remedialCoursesPnl.setVisible(false);
					    	remedialCoursesPnl.setVisible(false);
					    	addRemedialBtn.setVisible(false);
					    	deleteRemedialBtn.setVisible(false);
					    	updateRemedialBtn.setVisible(false);
					    }
					

						if (editApplicant) {
							modelApplicant.setValueAt(
									ApplicantLogic.getInstance().getApplicants().get(selectedApplicantId).getStatus(),
									selectedRowIndex, 5);
							JOptionPane.showMessageDialog(null, "Status Updated", "Success",
									JOptionPane.INFORMATION_MESSAGE);
								
						}
						
					} else
							JOptionPane.showMessageDialog(null, "Please select a status", "ERROR",
									JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Please select a row", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			};
			
			tableRemedialCourses.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int i = tableRemedialCourses.getSelectedRow();
					if (i >= 0) {
						fetchAndRefresh();

						// sets the id of choosen applicant
						 String selectedValue =modelRemedialCourse.getValueAt(i, 0).toString();
						 int courseId=Integer.parseInt(selectedValue);
						 fetchAndRefresh();
						 RemedialCourse c =remedialCourses.get(courseId);
						 /*remedialCourseIdLbl,PassingGradeLbl,remedialCourseName,remedialCourseNameTxt;private JTextField passingGradeTxt;*/
						 remedialCourseNameTxt.setText(c.getCourseName());
						 passingGradeTxt.setText(String.valueOf(c.getMinRequiredGrade()));

					} else {
						JOptionPane.showMessageDialog(null, "Please select a row", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			

		
			
			
			updateRemedialBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnSound.clickSound();
					int i = tableRemedialCourses.getSelectedRow();
					if (i >= 0) {
					
					  if(!applicantIdTxtlbl.getText().isEmpty()){
						  String selectedValue =modelRemedialCourse.getValueAt(i, 0).toString();;
						    int courseId=Integer.parseInt(selectedValue);
						    fetchAndRefresh();
						    RemedialCourse c =remedialCourses.get(courseId);
						    String ApplicantId=applicantIdTxtlbl.getText().toString();
						    Double grade=Double.parseDouble(passingGradeTxt.getText().toString());
						    	if(grade>=c.getMinRequiredGrade()&& grade<100)
						    	{
						    		Boolean Success= GradesLogic.getInstance().editPassingGrade(applicantIdTxtlbl.getText().toString(),courseId , grade);
						    		fetchAndRefresh();
						    		//GradesLogic.getInstance().getPassingGrade().containsKey(ApplicantId+c.getCourseId())
						    		//"remedial Id","Passing Grade","semesterA",
									//"semesterB", "semesterC"
						    		PassingGrade pass=GradesLogic.getInstance().getPassingGrade().get(ApplicantId+c.getCourseId());
						    		
						    		modelRemedialCourse.setValueAt(pass.getRemedialCourseId(), i, 0);
						    		modelRemedialCourse.setValueAt(pass.getPassingGrade(), i, 1);
						    		modelRemedialCourse.setValueAt(c.getSemesterA(), i, 2);
						    		modelRemedialCourse.setValueAt(c.getSemesterB(), i, 3);
						    		modelRemedialCourse.setValueAt(c.getSemesterSummer(), i, 4);

						    		//modelRemedialCourse.addRow(remedialTable);
						    		
						    		if(Success)
						    			JOptionPane.showMessageDialog(null, "Updated conditional Course!", "Success", JOptionPane.INFORMATION_MESSAGE);
						    
						    	}else 
						    		JOptionPane.showMessageDialog(null, "The Grade you inputed is lower than min required grade for this course.", "ERROR",
											JOptionPane.ERROR_MESSAGE);
						

					} else
						JOptionPane.showMessageDialog(null, "Please Choose Applicant First.",
								"ERROR", JOptionPane.ERROR_MESSAGE);
						    
				
					} else {
					JOptionPane.showMessageDialog(null, "Please select a row", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				}
			});
			
			
			
			
			
			
			
			//addRemedialBtn,deleteRemedialBtn,updateRemedialBtn

			deleteRemedialBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnSound.clickSound();
					int i = tableRemedialCourses.getSelectedRow();
					if (i >= 0) {
						fetchAndRefresh();
						// sets the id of choosen applicant
						 String selectedValue =modelRemedialCourse.getValueAt(i, 0).toString();
						 int courseId=Integer.parseInt(selectedValue);
						 fetchAndRefresh();
						 /*remedialCourseIdLbl,PassingGradeLbl,remedialCourseName,remedialCourseNameTxt;private JTextField passingGradeTxt;*/
						 Boolean Success= GradesLogic.getInstance().removePassingGrade(applicantIdTxtlbl.getText().toString(),courseId);
						 modelRemedialCourse.removeRow(i);
						 if(Success)
				    			JOptionPane.showMessageDialog(null, "Deleted conditional Course!", "Success", JOptionPane.INFORMATION_MESSAGE);

						 
					} else {
						JOptionPane.showMessageDialog(null, "Please select a row", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}		
			
			});

			
			
			resetBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					// Clear all existing rows
					modelApplicant.setRowCount(0);
					modelBackGrounds.setRowCount(0);
					fetchAndRefresh();
					// Iterate over the updated list of Applicants
					for (Applicant a : applicants) {
						// Create a new row array with the correct number of columns
						// Object[] row = new Object[8];
						rowApplicants[0] = a.getApplicantId();
						rowApplicants[1] = a.getFirstName();
						rowApplicants[2] = a.getLastName();
						rowApplicants[3] = a.getEmailAdress();
						rowApplicants[4] = a.getPhoneNumber();
						rowApplicants[5] = a.getStatus();
						rowApplicants[6] = a.getValid();
						rowApplicants[7] = a.getCurrentStage();

						// Add the new row to the table model
						modelApplicant.addRow(rowApplicants);
					}
				
				
				}
			});
			
			
			
			
			
			
			
			
			
			
			addRemedialBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnSound.clickSound();
					// TODO Auto-generated method stub
					  if(!applicantIdTxtlbl.getText().isEmpty()){
						  String selectedValue =comboBoxRemedial.getSelectedItem().toString();
						    int courseId=Integer.parseInt(selectedValue);
						    fetchAndRefresh();
						    RemedialCourse c =remedialCourses.get(courseId);
						    String ApplicantId=applicantIdTxtlbl.getText().toString();

						    if(!GradesLogic.getInstance().getPassingGrade().containsKey(ApplicantId+c.getCourseId()))
						    {
						    	Double grade=Double.parseDouble(passingGradeTxt.getText().toString());
						    	if(grade>=c.getMinRequiredGrade()&& grade<100)
						    	{
						    		Boolean Success= GradesLogic.getInstance().addPassingGrade(applicantIdTxtlbl.getText().toString(),courseId , grade);
						    		fetchAndRefresh();
						    		//GradesLogic.getInstance().getPassingGrade().containsKey(ApplicantId+c.getCourseId())
						    		//"remedial Id","Passing Grade","semesterA",
									//"semesterB", "semesterC"
						    		PassingGrade pass=GradesLogic.getInstance().getPassingGrade().get(ApplicantId+c.getCourseId());
						    		Object[] remedialTable = new Object[5];
						    		remedialTable[0] = pass.getRemedialCourseId();
						    		remedialTable[1] =pass.getPassingGrade();
						    		remedialTable[2] = c.getSemesterA();
						    		remedialTable[3] = c.getSemesterB();
						    		remedialTable[4] = c.getSemesterSummer();
						    		modelRemedialCourse.addRow(remedialTable);
						    		
						    		if(Success)
						    			JOptionPane.showMessageDialog(null, "Saved conditional Course!", "Success", JOptionPane.INFORMATION_MESSAGE);
						    
						    	}else 
						    		JOptionPane.showMessageDialog(null, "The Grade you inputed is lower than min required grade for this course.", "ERROR",
											JOptionPane.ERROR_MESSAGE);
						} else
							JOptionPane.showMessageDialog(null, "The condition course already saved.", "ERROR",
									JOptionPane.ERROR_MESSAGE);

					} else
						JOptionPane.showMessageDialog(null, "Please Choose Applicant First.",
								"ERROR", JOptionPane.ERROR_MESSAGE);
						    
				}
			});

		

			tableApplicants.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int i = tableApplicants.getSelectedRow();
					if (i >= 0) {
						fetchAndRefresh();

						// sets the id of choosen applicant
						applicantIdTxtlbl.setText(modelApplicant.getValueAt(i, 0).toString());

						ArrayList<BackgroundEducation> applicantBackGrounds = ApplicantLogic.getInstance().getApplicants()
								.get(modelApplicant.getValueAt(i, 0).toString()).getDiplomas();
						if (applicantBackGrounds.isEmpty()) {
							err.setVisible(true);
							err.setText("There is no BackGrounds");
						}

						err.setVisible(false);

						modelBackGrounds.setRowCount(0);
						for (BackgroundEducation b : applicantBackGrounds) {
							Object[] rowBackgrounds = new Object[5];
							rowBackgrounds[0] = b.getApplicantId();
							rowBackgrounds[1] =b.getInstutionName();
							rowBackgrounds[2] = b.getDepartmentname();
							rowBackgrounds[3] = b.getAverageGrade();
							rowBackgrounds[4] = b.getCreditPoints();
							modelBackGrounds.addRow(rowBackgrounds);
						}
						editBtn.removeActionListener(editButtonListener); // Avoid adding multiple listeners
						editBtn.addActionListener(editButtonListener);

					} else {
						JOptionPane.showMessageDialog(null, "Please select a row", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			});

		}

		private void initComponents() {

			// Set the layout to GridBagLayout
			setLayout(new GridBagLayout());
			gbc = new GridBagConstraints();
			btnSound = new SoundPlayer(this.getClass().getResource("/boundary/sound/click.wav"));


			gbc2 = new GridBagConstraints();
			gbc2.insets = new Insets(10, 10,10, 10);

			// panels
			// Configure constraints for the first panel
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.2;  
			gbc.weighty = 1;
			gbc.fill = GridBagConstraints.BOTH;

			this.applicantsPnl = new JPanel(new GridBagLayout());
			this.applicantsPnl.setBackground(new Color(230, 230, 250));
			add(applicantsPnl, gbc);

			
			remedialCoursesPnl= new JPanel(new GridBagLayout());
			gbc.gridx = 1;
			gbc.gridy = 0; 
			gbc.weightx = 0.8; 
			gbc.weighty = 1;

			gbc.fill = GridBagConstraints.BOTH;
			this.remedialCoursesPnl.setBackground(Color.GREEN);
			add(remedialCoursesPnl, gbc);
	    	remedialCoursesPnl.setVisible(false);

			

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 2;  // Span across both columns
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.fill = GridBagConstraints.BOTH;

			this.applicantBackGrounds = new JPanel(new GridBagLayout());
			this.applicantBackGrounds.setBackground(Color.GREEN);
			add(applicantBackGrounds, gbc);

			// Configure constraints for the third panel
			gbc.gridy = 2;
			gbc.gridwidth = 2;  // Span across both columns
			gbc.weighty = 2;
			gbc.fill = GridBagConstraints.BOTH;

			this.applicantsInformationPnl = new JPanel(new GridBagLayout());
			this.applicantsInformationPnl.setBackground(Color.YELLOW);
			add(applicantsInformationPnl, gbc);

			
			
			this.editBtn = new JButton();
			editBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/editBtn.png")));
			editBtn.setPreferredSize(new Dimension(90, 45));
			
			this.resetBtn = new JButton();
			resetBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/ResetBtn.png")));
			resetBtn.setPreferredSize(new Dimension(90, 45));
			
			this.updateRemedialBtn = new JButton();
			updateRemedialBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/editGrade.png")));
			updateRemedialBtn.setPreferredSize(new Dimension(140, 45));
			
			this.addRemedialBtn = new JButton();
			addRemedialBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/AddBtn.png")));
			addRemedialBtn.setPreferredSize(new Dimension(90, 45));
			
			this.deleteRemedialBtn = new JButton();
			deleteRemedialBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/DeleteBtn.png")));
			deleteRemedialBtn.setPreferredSize(new Dimension(100, 45));
			
			addRemedialBtn.setVisible(false);
	    	deleteRemedialBtn.setVisible(false);
	    	updateRemedialBtn.setVisible(false);
			
		
			// labels:
			applicantStatusLbl = new JLabel("Choose Status: ");
			applicantStatusLbl.setFont(new Font("Serif", Font.BOLD, 14));

			err = new JLabel("");
			err.setFont(new Font("Serif", Font.BOLD, 14));
			err.setBackground(Color.red);

			applicantIdlbl = new JLabel("applicant Id: ");
			applicantIdlbl.setFont(new Font("Serif", Font.BOLD, 14));

			applicantIdTxtlbl = new JLabel("");
			applicantIdTxtlbl.setFont(new Font("Serif", Font.BOLD, 14));

			// comboBox
			statusComboBox = createComboBox();

			// title
			title = new JLabel("Update Applicant Status");
			title.setFont(new Font("Serif", Font.BOLD, 18));

			gbc2.gridx = 0;
			gbc2.gridy = 0;
			gbc2.gridwidth = 1;
			gbc2.anchor = GridBagConstraints.CENTER;

			applicantsPnl.add(title, gbc2);

			gbc2.gridy = 1;
			gbc2.anchor = GridBagConstraints.LINE_START;
			gbc2.gridwidth = GridBagConstraints.REMAINDER;

			applicantsPnl.add(applicantIdlbl, gbc2);

			gbc2.gridx = 1;
			gbc2.gridy = 1;
			gbc2.gridwidth = 1;
			gbc2.anchor = GridBagConstraints.LINE_END;
			applicantsPnl.add(applicantIdTxtlbl, gbc2);
			
			gbc2.gridx = 0;
			gbc2.gridy = 2;
			gbc2.anchor = GridBagConstraints.LINE_START; 
			gbc2.gridwidth = 1;
			applicantsPnl.add(applicantStatusLbl, gbc2);

			gbc2.gridx = 1;
			gbc2.gridy = 2;  
			gbc2.anchor = GridBagConstraints.LINE_START; 
			gbc2.gridwidth = 1;
			applicantsPnl.add(statusComboBox, gbc2);


			// editBtn
			
			// Define Insets for spacing between buttons
			Insets buttonInsets = new Insets(5, 10, 5, 10); // Adjusted to provide more space

			// Configure constraints for Edit button
			gbc2.gridx = 0;
			gbc2.gridy = 3;
			gbc2.gridwidth = 2; // Spans two columns
			gbc2.insets = buttonInsets;
			gbc2.anchor = GridBagConstraints.LAST_LINE_START;
			gbc2.fill = GridBagConstraints.NONE;
			applicantsPnl.add(editBtn, gbc2);

			// Configure constraints for Add button
			gbc2.gridx = 1;
			gbc2.gridy = 3;
			gbc2.gridwidth = 1; // Occupy one column
			gbc2.insets = buttonInsets;
			gbc2.anchor = GridBagConstraints.LAST_LINE_END;
			applicantsPnl.add(addRemedialBtn, gbc2);

			// Configure constraints for Delete button
			gbc2.gridx = 0; // Next column
			gbc2.gridy = 4;
			gbc2.gridwidth = 1; 
			gbc2.insets = buttonInsets;
			gbc2.anchor = GridBagConstraints.LAST_LINE_START;
			applicantsPnl.add(deleteRemedialBtn, gbc2);

			// Configure constraints for Update button
			gbc2.gridx = 1; // Next column
			gbc2.gridy = 4;
			gbc2.gridwidth = 1; // Occupy one column
			gbc2.insets = buttonInsets;
			gbc2.anchor = GridBagConstraints.LAST_LINE_END;
			applicantsPnl.add(updateRemedialBtn, gbc2);
			// Configure constraints for Update button
			gbc2.gridx = 0; // Next column
			gbc2.gridy = 5;
			gbc2.gridwidth = 1; // Occupy one column
			gbc2.insets = buttonInsets;
			gbc2.anchor = GridBagConstraints.LAST_LINE_END;
			applicantsPnl.add(resetBtn, gbc2);

			
			// applicants panel
			// Initialize and configure tablePnl
			scrollPaneApplicant = new JScrollPane();
			tableApplicants = new JTable();
			Object[] columns = { "Applicant Id", "First Name", "Last Name", "Email", "phone", "status", "Valid",
					"Current Stage" };
			rowApplicants = new Object[8];

			modelApplicant = new DefaultTableModel();
			modelApplicant.setColumnIdentifiers(columns);
			tableApplicants.setModel(modelApplicant);

			Font font = new Font("", 1, 12);
			tableApplicants.setFont(font);
			tableApplicants.setRowHeight(40);

			// Clear all existing rows
			modelApplicant.setRowCount(0);

			fetchAndRefresh();
			// Iterate over the updated list of Applicants
			for (Applicant a : applicants) {
				// Object[] row = new Object[8];
				rowApplicants[0] = a.getApplicantId();
				rowApplicants[1] = a.getFirstName();
				rowApplicants[2] = a.getLastName();
				rowApplicants[3] = a.getEmailAdress();
				rowApplicants[4] = a.getPhoneNumber();
				rowApplicants[5] = a.getStatus();
				rowApplicants[6] = a.getValid();
				rowApplicants[7] = a.getCurrentStage();

				// Add the new row to the table model
				modelApplicant.addRow(rowApplicants);
			}

			GridBagConstraints gbcTable = new GridBagConstraints();
			gbcTable.gridx = 0; // Grid x position
			gbcTable.gridy = 0; // Grid y position
			gbcTable.fill = GridBagConstraints.BOTH; // Stretch to fill space
			gbcTable.weightx = 1.0; // Allocate extra space horizontally
			gbcTable.weighty = 1.0; // Allocate extra space vertically (optional)
			gbcTable.insets = new Insets(10, 10, 10, 10); // Padding around the table
			scrollPaneApplicant.setViewportView(tableApplicants);

			// Add the scrollPane to the tablePnl with GridBagConstraints
			applicantsInformationPnl.add(scrollPaneApplicant, gbcTable);

			applicantsInformationPnl.setBackground(Color.DARK_GRAY);

			// applicants diploma panel
			scrollPaneBackGrounds= new JScrollPane();
			tableBackGrounds = new JTable();
			Object[] columnsBackgrounds = {"Instution Name","Department Name","Graduation Date",
					"Average Grade", "Credit Points"};
			rowBackgrounds = new Object[5];

			modelBackGrounds = new DefaultTableModel();
			modelBackGrounds.setColumnIdentifiers(columnsBackgrounds);
			tableBackGrounds.setModel(modelBackGrounds);

			tableBackGrounds.setRowHeight(20);

			// Clear all existing rows
			modelBackGrounds.setRowCount(0);
			fetchAndRefresh();
			// Iterate over the updated list of Applicants

			GridBagConstraints gbcTableBackgrounds = new GridBagConstraints();
			gbcTableBackgrounds.gridx = 0; // Grid x position
			gbcTableBackgrounds.gridy = 0; // Grid y position
			gbcTableBackgrounds.fill = GridBagConstraints.BOTH; // Stretch to fill space
			gbcTableBackgrounds.weightx = 1.0; // Allocate extra space horizontally
			gbcTableBackgrounds.weighty = 1.0; // Allocate extra space vertically (optional)
			gbcTableBackgrounds.insets = new Insets(10, 10, 10, 10); // Padding around the table
			scrollPaneBackGrounds.setViewportView(tableBackGrounds);

			// Add the scrollPane to the tablePnl with GridBagConstraints
			applicantBackGrounds.add(scrollPaneBackGrounds, gbcTableBackgrounds);

			applicantBackGrounds.setBackground(Color.LIGHT_GRAY);
			
			
		
			
			///////////////////////////////////////////////////////////////////////////////////// remedial courses panel
			gbc= new GridBagConstraints();
			titleRemedial = new JLabel("Add Remedial Courses: ");
			titleRemedial.setFont(new Font("Serif", Font.BOLD, 18));

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.anchor = GridBagConstraints.CENTER;
			remedialCoursesPnl.add(titleRemedial, gbc);
			

			////combo box
			remedialCourseIdLbl = new JLabel("Choose Course: ");
			remedialCourseIdLbl.setFont(new Font("Serif", Font.BOLD, 14));
			
			gbc.gridy = 1;
			gbc.anchor = GridBagConstraints.LINE_START;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			remedialCoursesPnl.add(remedialCourseIdLbl, gbc);
			
			
			comboBoxRemedial=createRemedialComboBox();
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.LINE_END;
			remedialCoursesPnl.add(comboBoxRemedial, gbc);
			

			///name
			remedialCourseName = new JLabel("Course Name; ");
			remedialCourseName.setFont(new Font("Serif", Font.BOLD, 14));
			
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.anchor = GridBagConstraints.LINE_START;
			gbc.gridwidth = GridBagConstraints.REMAINDER;

			remedialCoursesPnl.add(remedialCourseName, gbc);
			
			remedialCourseNameTxt = new JLabel("");
			remedialCourseNameTxt.setFont(new Font("Serif", Font.BOLD, 14));
			
			gbc.gridx = 1;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.LINE_END;
			remedialCoursesPnl.add(remedialCourseNameTxt, gbc);
			
			
			PassingGradeLbl = new JLabel("Required Grade: ");
			PassingGradeLbl.setFont(new Font("Serif", Font.BOLD, 14));
			
	
			
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.gridwidth = 1; // The label should only occupy one column
			gbc.anchor = GridBagConstraints.LINE_START; // Align to the start of the line
			gbc.fill = GridBagConstraints.NONE; // No fill for the label
			gbc.weightx = 0; // No extra space for the label
			remedialCoursesPnl.add(PassingGradeLbl, gbc);

			// Add the text field in the next column
			passingGradeTxt = new JTextField();
			passingGradeTxt.setPreferredSize(new Dimension(200, 30));

			gbc.gridx = 1; 
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.fill = GridBagConstraints.HORIZONTAL; 
			gbc.anchor = GridBagConstraints.LINE_START; 
			gbc.weightx = 1; 
			remedialCoursesPnl.add(passingGradeTxt, gbc);
	
			
			
		
			
			
			
			////////////////////remedial Tbl
			scrollRemedialCourses= new JScrollPane();
			tableRemedialCourses = new JTable();
			Object[] columnsRemedial = {"remedial Id","Passing Grade","semesterA",
					"semesterB", "semesterC"};
			rowRemedialCourses = new Object[5];

			modelRemedialCourse = new DefaultTableModel();
			modelRemedialCourse.setColumnIdentifiers(columnsBackgrounds);
			tableRemedialCourses.setModel(modelRemedialCourse);

			tableRemedialCourses.setRowHeight(40);

			err = new JLabel("");
			err.setFont(new Font("Serif", Font.BOLD, 14));
			err.setBackground(Color.red);
			
		
			// Clear all existing rows
			modelRemedialCourse.setRowCount(0);
			fetchAndRefresh();
			gbc= new GridBagConstraints();
			gbc.gridx = 0; 
			gbc.gridy = 4; 
			gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across all columns

			gbc.fill = GridBagConstraints.BOTH; 
			gbc.weightx = 1.0; 
			gbc.weighty = 1.0; 
			gbc.insets = new Insets(10, 10, 10, 10); 
			scrollRemedialCourses.setViewportView(tableRemedialCourses);

			remedialCoursesPnl.add(scrollRemedialCourses, gbc);

			//remedialCoursesPnl.setBackground(new Color(230, 230, 250));

		
		}

		private void fetchAndRefresh() {
			// TODO Auto-generated method stub
			applicants = ApplicantLogic.getInstance().filterByAfterInterview();
			remedialCourses=CourseLogic.getInstance().getRemedialCourses();



		}
		
		//comboBoxRemedial
		public JComboBox<String> createRemedialComboBox() {
			fetchAndRefresh();
			String[] remedialCourseStrings = new String[remedialCourses.values().size()];

			int i = 0;
			for (RemedialCourse s : remedialCourses.values()) {
				remedialCourseStrings[i] = String.valueOf(s.getCourseId());
				i++;
			}

			return new JComboBox<>(remedialCourseStrings);
		}

		
		
		

		public JComboBox<String> createComboBox() {
			String[] statusTypeStrings = new String[4];
			statusTypeStrings[0]=ApplicantStatus.accepted.toString();
			statusTypeStrings[1]=ApplicantStatus.conditionally_accepted.toString();
			statusTypeStrings[2]=ApplicantStatus.rejected_after_interview.toString();
			statusTypeStrings[3]=ApplicantStatus.Waiting_List.toString();

			return new JComboBox<>(statusTypeStrings);
		}
		
		public JComboBox<String> createGradeComboBox() {

			String[] examptionStrings = { "No" ,"Yes" };		
			return new JComboBox<>(examptionStrings);
		}



		public String getComboStatus() {
			return (String) statusComboBox.getSelectedItem();
		}

		/*public static void main(String[] args) {
			// Create a frame to hold the panel
			JFrame frame = new JFrame("Update Applicant Status");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1000, 600);

			// Add the AttachDocumentsPnl to the frame
			FrmAfterInterview panel = new FrmAfterInterview();
			frame.add(panel);

			// Make the frame visible
			frame.setVisible(true);
		}*/
	}

