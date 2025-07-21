package boundary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
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
import control.DocumentLogic;
import entity.Course;
import entity.Examption;
import entity.RemedialCourse;

public class FrmExamptCourses extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel courseInfoPnl;
	private JPanel remedialCourseInfo;
	private JPanel remedialTableCourseInfo;

	private JPanel backgroundPnl;

	// courses Id
	private JComboBox<String> remedialComboBox;
	private JComboBox<String> courseComboBox;
	private JComboBox<String> examptComboBox;

	private GridBagConstraints  gbc;
	private JScrollPane scrollPaneCourses, scrollPaneRemedialCourses;
	// soundPlayer
	SoundPlayer btnSound;
	private JTable tableRemedialCourses, tableCourses;
	private Object[] rowRemedialCourses;
	private Object[] rowCourses;
	private DefaultTableModel modelCourses, modelRemedialCourses;

	// labels
	private JLabel courseIdLbl, courseName, courseNameTxt, gradeLbl, exemptLbl,
			remidialCourseIdLbl, examptionGradeLbl, err;
	private JLabel title, courseIdTxt;
	private JTextField gradeTxt;
	private HashMap<Integer, Course> courses;
	private HashMap<Integer, RemedialCourse> remedialCourses;
	private HashMap<Integer, Examption> examptions;
	// buttons

	private JButton editBtn, getCoursesbtn, addBtn, deleteBtn;

	/**
	 * Create the panel.
	 */
	public FrmExamptCourses() {
		fetchAndRefresh();
		initComponents();
		createEvents();
	}

	private void createEvents() {
		// TODO Auto-generated method stub

		gradeTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				char key = e.getKeyChar();
				if (!(Character.isDigit(key)) && !(Character.isWhitespace(key)) && !(Character.isISOControl(key))
						&& key != '.') {
					// textAverageGrade.setEditable(true);
					e.consume();
					JOptionPane.showMessageDialog(null, "Enter only numbers.", "ERROR", JOptionPane.ERROR_MESSAGE);

				}

			}
		});
		
		
		examptComboBox.addActionListener(e -> {
		    String selectedValue = (String) examptComboBox.getSelectedItem();
		    if ("Yes".equals(selectedValue)) {
		        gradeLbl.setVisible(true);
		        gradeTxt.setVisible(true);
		    } else {
		        gradeLbl.setVisible(false);
		        gradeTxt.setVisible(false);
		    }
		});
		
		
		
		tableCourses.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				

				int i = tableCourses.getSelectedRow();
				if(i>=0)
				{
					courseIdTxt.setText(modelCourses.getValueAt(i, 0).toString());
					courseNameTxt.setText(modelCourses.getValueAt(i, 1).toString());
				}
			}

		});
		
		//editBtn
		
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				btnSound.clickSound();
				int i = tableRemedialCourses.getSelectedRow();
				if (i >= 0) {
					int courseId = (int) modelRemedialCourses.getValueAt(i, 0);
					int remedialCourseId=Integer.parseInt(getCombotypeRemedialCourseId());
					
				
					
					if(getCombotypeExampt()!=null)
					{
					
					 String selectedValue = (String) examptComboBox.getSelectedItem();
					    if ("Yes".equals(selectedValue)) {
					        gradeLbl.setVisible(true);
					        gradeTxt.setVisible(true);
					        
					        
					        
					        if(!gradeTxt.getText().isEmpty())
							{
								int grade=Integer.parseInt(gradeTxt.getText().toString());
								Boolean edit=CourseLogic.getInstance().editExemption(courseId, remedialCourseId, grade, true);
						        JOptionPane.showMessageDialog(null, "Saved Exemption successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
							
								fetchAndRefresh();
								Course c=CourseLogic.getInstance().getCourse().get(courseId);
								Examption exam=CourseLogic.getInstance().getExampt().get(courseId+remedialCourseId);
								//"Course Id", "Name", "Credit Points", "Grade Level", "exempt"
								modelRemedialCourses.setValueAt(exam.getCourseId(), i, 0);
								modelRemedialCourses.setValueAt(c.getCourseName(), i, 1);
								modelRemedialCourses.setValueAt( c.getCreditPoint(), i, 2);
								modelRemedialCourses.setValueAt(exam.getGradeLevel(), i, 3);
								modelRemedialCourses.setValueAt(exam.getExampt(), i, 4);
								
								

							}else
								JOptionPane.showMessageDialog(null, "Please enter a grade", "ERROR", JOptionPane.ERROR_MESSAGE);
							
							
							
					        
					        
					    } else {
							Boolean edit=CourseLogic.getInstance().editExemption(courseId, remedialCourseId, 0,false);
							fetchAndRefresh();
							Course c=CourseLogic.getInstance().getCourse().get(courseId);
							Examption exam=CourseLogic.getInstance().getExampt().get(courseId+remedialCourseId);
							//"Course Id", "Name", "Credit Points", "Grade Level", "exempt"
							modelRemedialCourses.setValueAt(exam.getCourseId(), i, 0);
							modelRemedialCourses.setValueAt(c.getCourseName(), i, 1);
							modelRemedialCourses.setValueAt( c.getCreditPoint(), i, 2);
							modelRemedialCourses.setValueAt(exam.getGradeLevel(), i, 3);
							modelRemedialCourses.setValueAt(exam.getExampt(), i, 4
						);
					        
					   }
					    
					}else 
						JOptionPane.showMessageDialog(null, "Please select a type of exempt", "ERROR", JOptionPane.ERROR_MESSAGE);
					


				} else

					JOptionPane.showMessageDialog(null, "Please Select A Row First", "ERROR",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				btnSound.clickSound();
				int i = tableRemedialCourses.getSelectedRow();
				if (i >= 0) {
					int courseId = (int) modelRemedialCourses.getValueAt(i, 0);
					int remedialCourseId=Integer.parseInt(getCombotypeRemedialCourseId());
					boolean removed = CourseLogic.getInstance().removeExemption(courseId, remedialCourseId);
					fetchAndRefresh();
					modelRemedialCourses.removeRow(i);

					if(removed)
					JOptionPane.showMessageDialog(null, "Deleted Successfully");
				} else

					JOptionPane.showMessageDialog(null, "Please Select A Row First", "ERROR",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		
		
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				btnSound.clickSound();
				int i = tableCourses.getSelectedRow();
				if(i>=0)
				{	
					
					int courseId,remedialId;
					courseIdTxt.setText(modelCourses.getValueAt(i, 0).toString());
					courseNameTxt.setText(modelCourses.getValueAt(i, 1).toString());
					
					
					if(getCombotypeExampt()!=null)
					{
						courseId=(int)modelCourses.getValueAt(i, 0);
						remedialId=Integer.parseInt(getCombotypeRemedialCourseId());
						
						if(getCombotypeExampt().equals("Yes"))
						{
							gradeLbl.setVisible(true);
							gradeTxt.setVisible(true);
							if(!CourseLogic.getInstance().ExemptionAlreadyExists(courseId, remedialId))
							{
								if(!gradeTxt.getText().isEmpty())
								{
									int grade=Integer.parseInt(gradeTxt.getText().toString());
									Boolean insert=CourseLogic.getInstance().addExamption(courseId, remedialId, grade, true);
							        JOptionPane.showMessageDialog(null, "Saved Exemption successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
									
									fetchAndRefresh();
									Course c=CourseLogic.getInstance().getCourse().get(courseId);
									Examption exam=CourseLogic.getInstance().getExampt().get(courseId+remedialId);
									//"Course Id", "Name", "Credit Points", "Grade Level", "exempt"
									Object[] rowRemedialCourses = new Object[5];

									rowRemedialCourses[0] = exam.getCourseId();
									rowRemedialCourses[1] = c.getCourseName();
									rowRemedialCourses[2] = c.getCreditPoint();
									rowRemedialCourses[3] = exam.getGradeLevel();
									rowRemedialCourses[4] = exam.getExampt();
									modelRemedialCourses.addRow(rowRemedialCourses);

								}else
									JOptionPane.showMessageDialog(null, "Please enter a grade", "ERROR", JOptionPane.ERROR_MESSAGE);
							}else 
								JOptionPane.showMessageDialog(null, "This remedial course already has this exemption . you can edit or delete and renter", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					 if(getCombotypeExampt().equals("No"))
						{
							if(!CourseLogic.getInstance().ExemptionAlreadyExists(courseId, remedialId))
							{
									Boolean insert=CourseLogic.getInstance().addExamption(courseId, remedialId, 0, true);
							        JOptionPane.showMessageDialog(null, "Saved Exemption successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
									fetchAndRefresh();
									Course c=CourseLogic.getInstance().getCourse().get(courseId);
									Examption exam=CourseLogic.getInstance().getExampt().get(courseId+remedialId);
									//"Course Id", "Name", "Credit Points", "Grade Level", "exempt"
									Object[] rowRemedialCourses = new Object[5];

									rowRemedialCourses[0] = exam.getCourseId();
									rowRemedialCourses[1] = c.getCourseName();
									rowRemedialCourses[2] = c.getCreditPoint();
									rowRemedialCourses[3] = exam.getGradeLevel();
									rowRemedialCourses[4] = exam.getExampt();
									modelRemedialCourses.addRow(rowRemedialCourses);

									
							}else 
								JOptionPane.showMessageDialog(null, "This remedial course already has this exemption . you can edit or delete and renter", "ERROR", JOptionPane.ERROR_MESSAGE);
						
						}	
						
					}else 
						JOptionPane.showMessageDialog(null, "Please select a type of exempt", "ERROR", JOptionPane.ERROR_MESSAGE);
					
					
					
				}
				else 
					JOptionPane.showMessageDialog(null, "Please select a row from table of courses", "ERROR", JOptionPane.ERROR_MESSAGE);
				
				gradeLbl.setVisible(false);
				gradeTxt.setVisible(false);

			}
		});
		
		
		getCoursesbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				btnSound.clickSound();
				modelRemedialCourses.setRowCount(0);

				if (getCombotypeRemedialCourseId() != null) {
					fetchAndRefresh();

					// Iterate over the updated list of Applicants

					for (Examption e1 : examptions.values()) {
						// Create a new row array with the correct number of columns
						// Object[] row = new Object[4];
						Course c = courses.get(e1.getCourseId());
						if (Integer.parseInt(getCombotypeRemedialCourseId()) == e1.getRemedialCourseId()) {
							Object[] rowRemedialCourses = new Object[5];

							rowRemedialCourses[0] = e1.getCourseId();
							rowRemedialCourses[1] = c.getCourseName();
							rowRemedialCourses[2] = c.getCreditPoint();
							rowRemedialCourses[3] = e1.getGradeLevel();
							rowRemedialCourses[4] = e1.getExampt();

							// Add the new row to the table model
							modelRemedialCourses.addRow(rowRemedialCourses);
						}
					}

				}

			}
		});

	}

	private void initComponents() {

		// TODO Auto-generated method stub
		// Set the layout to GridBagLayout
		setLayout(new GridBagLayout());

		setBackground(Color.GRAY);
		btnSound = new SoundPlayer(this.getClass().getResource("/boundary/sound/click.wav"));

		this.courseInfoPnl = new JPanel(new GridBagLayout());
		this.remedialCourseInfo = new JPanel(new GridBagLayout());
		this.backgroundPnl = new JPanel(new GridBagLayout());
		this.remedialTableCourseInfo = new JPanel(new GridBagLayout());

		gbc = new GridBagConstraints();
		gbc.insets=new Insets(10,10,10,10);

		//gbc1 = new GridBagConstraints();

		//gbc2 = new GridBagConstraints();
		//gbc4 = new GridBagConstraints();

		
		// panels
		// Configure constraints for the first panel (remedialCourseInfo)
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.remedialCourseInfo.setBackground(Color.LIGHT_GRAY);
		add(remedialCourseInfo, gbc);

		// Configure constraints for the second panel (courseInfoPnl)
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.courseInfoPnl.setBackground(new Color(230, 230, 250));
		add(courseInfoPnl, gbc);

		// Configure constraints for the third panel (backgroundPnl)
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2; // Span across both columns
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(backgroundPnl, gbc);

		// Configure constraints for the fourth panel (remedialTableCourseInfo)
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2; // Span across both columns
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(remedialTableCourseInfo, gbc);

		// comboBox
		remedialComboBox = createRemedialComboBox();
		examptComboBox = createExamptionsComboBox();
		courseComboBox = createCoursesComboBox();

		// addBtn,deleteBtn
		this.deleteBtn = new JButton();
		deleteBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/DeleteBtn.png")));
		deleteBtn.setPreferredSize(new Dimension(100, 45));

		this.addBtn = new JButton();
		addBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/AddBtn.png")));
		addBtn.setPreferredSize(new Dimension(100, 45));

		this.editBtn = new JButton();
		editBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/editBtn.png")));
		editBtn.setPreferredSize(new Dimension(100, 45));

		this.getCoursesbtn = new JButton();
		getCoursesbtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/getCourses.png")));
		getCoursesbtn.setPreferredSize(new Dimension(140, 45));

		// labels:
		courseIdLbl = new JLabel("CourseId: ");
		courseIdLbl.setFont(new Font("Serif", Font.BOLD, 14));

		// labels:
		courseName = new JLabel("Course Name: ");
		courseName.setFont(new Font("Serif", Font.BOLD, 14));

		// labels:
		gradeLbl = new JLabel("Grade level:");
		gradeLbl.setFont(new Font("Serif", Font.BOLD, 14));

		// labels:
		courseNameTxt = new JLabel("");
		courseNameTxt.setFont(new Font("Serif", Font.BOLD, 14));

	

		// labels:
		courseIdTxt = new JLabel("");
		courseIdTxt.setFont(new Font("Serif", Font.BOLD, 14));

		err = new JLabel("");
		err.setFont(new Font("Serif", Font.BOLD, 14));
		err.setBackground(Color.red);

		remidialCourseIdLbl = new JLabel("remedial Course Id: ");
		remidialCourseIdLbl.setFont(new Font("Serif", Font.BOLD, 14));


		// exemptLbl
		examptionGradeLbl = new JLabel("exampt required Grade: ");
		examptionGradeLbl.setFont(new Font("Serif", Font.BOLD, 14));

		exemptLbl = new JLabel("exampt: ");
		exemptLbl.setFont(new Font("Serif", Font.BOLD, 14));

		gradeTxt = new JTextField();
		gradeTxt.setPreferredSize(new Dimension(200, 25));

		title = new JLabel("Set Exemptions for Remedial Courses");
		title.setFont(new Font("Serif", Font.BOLD, 18));

		///////////////////////////////////////////////////////////// exemption panel
/*
 * 
 * 
 */
		gbc = new GridBagConstraints();
		gbc.insets=new Insets(10,10,10,10);
		gbc.fill = GridBagConstraints.HORIZONTAL; // Allow components to expand horizontally

		// Configure constraints for the title
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across all columns
		gbc.anchor = GridBagConstraints.CENTER; // Center the title
		remedialCourseInfo.add(title, gbc);

		// Configure constraints for remedialCourseIdLbl
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = GridBagConstraints.RELATIVE; // Align with the next component
		gbc.anchor = GridBagConstraints.LINE_START; // Align to the start (left)
		remedialCourseInfo.add(remidialCourseIdLbl, gbc);

		// Configure constraints for remedialComboBox
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER; // Fill remaining space
		gbc.anchor = GridBagConstraints.LINE_END; // Align to the end (right)
		remedialCourseInfo.add(remedialComboBox, gbc);

		///////////////
		// Initialize and configure tablePnl
		scrollPaneRemedialCourses = new JScrollPane();
		tableRemedialCourses = new JTable();

		Object[] columns = { "Course Id", "Name", "Credit Points", "Grade Level", "exempt" };
		rowRemedialCourses = new Object[4];
		modelRemedialCourses = new DefaultTableModel();
		modelRemedialCourses.setColumnIdentifiers(columns);
		tableRemedialCourses.setModel(modelRemedialCourses);

		Font font = new Font("", Font.BOLD, 12);
		tableRemedialCourses.setFont(font);
		tableRemedialCourses.setRowHeight(30);

		// Clear all existing rows
		modelRemedialCourses.setRowCount(0);

		// Add the scroll pane to the table panel
		scrollPaneRemedialCourses.setViewportView(tableRemedialCourses);

		
		GridBagConstraints gbcTable = new GridBagConstraints();
		gbcTable.gridx = 0; // Grid x position
		gbcTable.gridy = 0; // Grid y position
		gbcTable.fill = GridBagConstraints.BOTH; // Stretch to fill space
		gbcTable.weightx = 1.0; // Allocate extra space horizontally
		gbcTable.weighty = 1.0; // Allocate extra space vertically (optional)
		gbcTable.insets = new Insets(10, 10, 10, 10); // Padding around the table

		// Add the scrollPane to the tablePnl with GridBagConstraints
		scrollPaneRemedialCourses.setViewportView(tableRemedialCourses);
		remedialTableCourseInfo.add(scrollPaneRemedialCourses, gbcTable);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across all columns
		gbc.weightx = 1.0; // Allocate extra space horizontally
		gbc.weighty = 1.0; // Allocate extra space vertically
		gbc.fill = GridBagConstraints.BOTH;
		remedialCourseInfo.add(remedialTableCourseInfo, gbc);

		
		/*
		// Configure constraints for the table panel
		/*gbc4.gridx = 0; // Position in the first column
		gbc4.gridy = 2; // Position in the third row (below the label and combo box)
		gbc4.gridwidth = GridBagConstraints.REMAINDER; // Span across all columns
		gbc4.weightx = 1.0; // Allocate extra space horizontally
		gbc4.weighty = 1.0; // Allocate extra space vertically
		gbc4.fill = GridBagConstraints.BOTH; // Allow the table to expand in both directions
		remedialTableCourseInfo.add(scrollPaneRemedialCourses, gbc4);*/

		// Add the table panel below the info panel
		/*gbc2.gridx = 0;
		gbc2.gridy = 2;
		gbc2.gridwidth = GridBagConstraints.REMAINDER; // Span across all columns
		gbc2.fill = GridBagConstraints.BOTH; // Allow the table to expand in both directions
		remedialCourseInfo.add(remedialTableCourseInfo, gbc2);

		// Set the position of the table
		/*gbc4.gridx = 0; // Position in the first column
		gbc4.gridy = 0; // Position in the third row (adjust as needed)
		gbc4.weightx = 1.0;
		gbc4.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;

		// Add the scroll pane to the panel
		scrollPaneRemedialCourses.setViewportView(tableRemedialCourses);
		//remedialTableCourseInfo.add(scrollPaneRemedialCourses, gbc4);*/

		//////////////////////////////////////////////////////////////////////////////// coursePanel

		gbc = new GridBagConstraints();
		gbc.insets=new Insets(10,10,10,10);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridwidth = 1;
		courseInfoPnl.add(courseIdLbl, gbc);

		// Add courseIdTxt to the panel
		gbc.gridx = 1; // Move to the next column
		gbc.gridwidth = GridBagConstraints.REMAINDER; // Span the rest of the row if needed
		gbc.anchor = GridBagConstraints.LINE_END;
		courseInfoPnl.add(courseIdTxt, gbc);

		// Add courseName label to the panel
		gbc.gridx = 0; // Move to the next row, same column as the label
		gbc.gridy = 1;
		gbc.gridwidth = 1; // Only take one column width
		gbc.anchor = GridBagConstraints.LINE_START;
		courseInfoPnl.add(courseName, gbc);

		// Add courseNameTxt to the panel
		gbc.gridx = 1; // Move to the next column
		gbc.gridwidth = GridBagConstraints.REMAINDER; // Span the rest of the row if needed
		gbc.anchor = GridBagConstraints.LINE_END;
		courseInfoPnl.add(courseNameTxt, gbc);

		gbc.gridx = 0; // Move to the next row, same column as the label
		gbc.gridy = 2;
		gbc.gridwidth = 1; // Only take one column width
		gbc.anchor = GridBagConstraints.LINE_START;
		courseInfoPnl.add(exemptLbl, gbc);

		gbc.gridx = 1; // Move to the next column
		gbc.gridwidth = GridBagConstraints.REMAINDER; // Span the rest of the row if needed
		gbc.anchor = GridBagConstraints.LINE_END;
		courseInfoPnl.add(examptComboBox, gbc);

		// Add gradeLbl label to the panel
		gbc.gridx = 0; // Move to the next row, same column as the label
		gbc.gridy = 3;
		gbc.gridwidth = 1; // Only take one column width
		gbc.anchor = GridBagConstraints.LINE_START;
		courseInfoPnl.add(gradeLbl, gbc);

		// Add gradeLblTxt to the panel
		gbc.gridx = 1; // Move to the next column
		gbc.gridwidth = GridBagConstraints.REMAINDER; // Span the rest of the row if needed
		gbc.anchor = GridBagConstraints.LINE_END;
		courseInfoPnl.add(gradeTxt, gbc);

		// Add editBtn to the panel
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		courseInfoPnl.add(editBtn, gbc);

		// Add get to the panel
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		courseInfoPnl.add(getCoursesbtn, gbc);
		// //addBtn,deleteBtn

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		courseInfoPnl.add(addBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		courseInfoPnl.add(deleteBtn, gbc);

	/*	////////////////////////////////////////////////////////////////////// courses
		////////////////////////////////////////////////////////////////////// panel/////////////////////////////
		/*
		 * /* scrollPaneRemedialCourses = new JScrollPane(); tableRemedialCourses = new
		 * JTable(); Object[] columns = { "course Id", "Name", "credit points"
		 * ,"grade level"}; rowRemedialCourses = new Object[4];
		 * 
		 * modelRemedialCourses = new DefaultTableModel();
		 * modelRemedialCourses.setColumnIdentifiers(columns);
		 * tableRemedialCourses.setModel(modelRemedialCourses);
		 * 
		 * private JScrollPane scrollPaneCourses, scrollPaneRemedialCourses; //
		 * soundPlayer SoundPlayer btnSound; private JTable tableRemedialCourses,
		 * tableCourses; private Object[] rowRemedialCourses; private Object[]
		 * rowCourses; private DefaultTableModel modelCourses, modelRemedialCourses;
		 * private Insets insets;
		 * 
		 * // labels private JLabel courseIdLbl, gradeLblTxt, courseName,
		 * courseNameTxt, gradeLbl, exemptLbl, remidialCourseIdLbl,
		 * examptionGradeLbl, err; private JLabel title, courseIdTxt; private JTextField
		 * gradeTxt;
		 * 
/*		 */

		// Initialize and configure tablePnl
		gbc = new GridBagConstraints();
		gbc.insets=new Insets(10,10,10,10);
		scrollPaneCourses = new JScrollPane();
		tableCourses = new JTable();

		Object[] columns2 = { "Course Id", "Name", "Credit Points" };
		modelCourses = new DefaultTableModel();
		modelCourses.setColumnIdentifiers(columns2);
		tableCourses.setModel(modelCourses);

		// Clear all existing rows
		modelCourses.setRowCount(0);

		
		tableCourses.setFont(font);
		tableCourses.setRowHeight(30);
		
		// Add the scroll pane to the table panel
		scrollPaneCourses.setViewportView(tableCourses);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		backgroundPnl.add(scrollPaneCourses, gbc);
		
		
		
		fetchAndRefresh();
		
		for (Course c : courses.values()) {
			// Create a new row array with the correct number of columns
			// Object[] row = new Object[4];
			Object[] rowCourses = new Object[3];

			rowCourses[0] = c.getCourseId();
			rowCourses[1] = c.getCourseName();
			rowCourses[2] = c.getCreditPoint();
			// Add the new row to the table model
			modelCourses.addRow(rowCourses);
			
		}
		
		

	}

	private void fetchAndRefresh() {
		// TODO Auto-generated method stub
		remedialCourses = CourseLogic.getInstance().getRemedialCourses();
		courses = CourseLogic.getInstance().getCourse();
		examptions = CourseLogic.getInstance().getExampt();

	}

	public JComboBox<String> createCoursesComboBox() {

		fetchAndRefresh();
		String[] CourseStrings = new String[courses.values().size()];

		int i = 0;
		for (Course s : courses.values()) {
			CourseStrings[i] = String.valueOf(s.getCourseId());
			i++;
		}

		return new JComboBox<>(CourseStrings);
	}

	/*
	 * public JComboBox<String> createExamptionsComboBox() {
	 * 
	 * fetchAndRefresh(); String[] examptionStrings = new
	 * String[examptions.values().size()];
	 * 
	 * int i = 0; for (Examption s : examptions.values()) { examptionStrings[i] =
	 * String.valueOf(s.getCourseId()); i++; }
	 * 
	 * return new JComboBox<>(examptionStrings); }
	 */

	public JComboBox<String> createExamptionsComboBox() {

		fetchAndRefresh();
		String[] examptionStrings = { "No" ,"Yes" };		
		return new JComboBox<>(examptionStrings);
	}

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

	public String getCombotypeExampt() {
		return (String) examptComboBox.getSelectedItem();
	}

	public String getCombotypeRemedialCourseId() {
		return (String) remedialComboBox.getSelectedItem();
	}

	public String getCombotypeCourseId() {
		return (String) courseComboBox.getSelectedItem();
	}

	public static void main(String[] args) {
		// Create a frame to hold the panel
		JFrame frame = new JFrame("Attach Documents Panel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);

		// Add the AttachDocumentsPnl to the frame
		FrmExamptCourses panel = new FrmExamptCourses();
		frame.add(panel);

		// Make the frame visible
		frame.setVisible(true);
	}

}
