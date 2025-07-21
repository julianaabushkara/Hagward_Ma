package boundary;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import control.ApplicantLogic;
import control.InstitutionLogic;
import entity.Applicant;
import entity.BackgroundEducation;
import entity.Department;
import entity.Institution;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FrmBackgroundEducation extends JPanel {

	private static final long serialVersionUID = 1L;
	// panels
	private JPanel educationPanel;
	private FrmAddNewInstitution addNewInstitute;

	// labels
	private JLabel lblInstitutionName;
	private JLabel lblDepartmentName;
	private JLabel lblAverageGrade;
	private JLabel lblGraduationDate;

	// buttons
	private JButton btnAddRow;
	private JButton btnUpdate;
	private JButton btnDelete;

	// table
	private JTable table;
	private JScrollPane scrollPane;
	private DefaultTableModel model;
	private JDateChooser dateChooser;
	private JTextField textAverageGrade;
	private JTextField textCreditPoints;
	private JButton btnNewInstitute;
	private JLabel lblCreditPoints;

	// combobox
	private JComboBox<String> institutionComboBox;
	private JComboBox<String> departmentComboBox;

	private HashMap<String, Department> departments = new HashMap<String, Department>();
	private HashMap<String, Institution> institutions = new HashMap<String, Institution>();
	private String applicantId;
	private Boolean inAddMode;

	/**
	 * Create the panel.
	 */
	public FrmBackgroundEducation() {
		fetchAndRefresh();
		initComponents();
		createEvents();

	}

	private void initComponents() {

		educationPanel = new JPanel();
		setPreferredSize(new Dimension(1219, 739));

		lblInstitutionName = new JLabel("Institution Name:");
		lblInstitutionName.setBounds(14, 10, 140, 30);
		lblInstitutionName.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblDepartmentName = new JLabel("Department Name:");
		lblDepartmentName.setBounds(14, 60, 160, 30);
		lblDepartmentName.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblGraduationDate = new JLabel("Graduation Date:");
		lblGraduationDate.setBounds(14, 210, 130, 30);
		lblGraduationDate.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblAverageGrade = new JLabel("Average Grade :");
		lblAverageGrade.setBounds(14, 110, 161, 30);
		lblAverageGrade.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblCreditPoints = new JLabel("Credit Points :");
		lblCreditPoints.setBounds(14, 160, 221, 30);
		lblCreditPoints.setFont(new Font("Tahoma", Font.BOLD, 15));

		// combo boxes
		institutionComboBox = new JComboBox<String>();

		institutionComboBox.setBounds(164, 10, 341, 30);
		educationPanel.add(institutionComboBox);
		updateInstitutionComboBox();

		// Create the second combo box (departments)
		departmentComboBox = new JComboBox<String>();

		departmentComboBox.setBounds(164, 62, 341, 30);

		educationPanel.add(departmentComboBox);

		textAverageGrade = new JTextField();

		textAverageGrade.setBounds(164, 113, 341, 30);
		textAverageGrade.setColumns(10);

		textCreditPoints = new JTextField();

		textCreditPoints.setBounds(167, 168, 341, 30);
		textCreditPoints.setColumns(10);

		// buttons
		btnAddRow = new JButton("Add ");
		btnAddRow.setBounds(40, 270, 140, 60);

		btnUpdate = new JButton("Update");
		btnUpdate.setBounds(200, 270, 140, 60);

		btnDelete = new JButton("Delete");
		btnDelete.setBounds(360, 270, 140, 60);

		btnNewInstitute = new JButton("+ Request To Add New Institute");
		btnNewInstitute.setBounds(167, 350, 213, 36);

		// table initialisation
		scrollPane = new JScrollPane();
		table = new JTable();
		Object[] columns = { "Institution Name", "Department Name", "Department PhoneNumber", "Country", "City",
				"Street address", "Graduation Date", "Credit Points", "Average Grade" };

		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);

		table.setBackground(Color.cyan);
		table.setForeground(Color.white);
		Font font = new Font("", 1, 22);
		table.setFont(font);
		table.setRowHeight(30);

		scrollPane.setViewportView(table);

		// new panel institute
		addNewInstitute = new FrmAddNewInstitution();
		addNewInstitute.setVisible(false); // Initially hidden

		//
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(educationPanel, GroupLayout.PREFERRED_SIZE, 645, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(addNewInstitute, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
										.addGap(24))
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1186, Short.MAX_VALUE)
										.addGap(23)))));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(14)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(educationPanel, GroupLayout.PREFERRED_SIZE, 410, Short.MAX_VALUE)
								.addComponent(addNewInstitute, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE).addGap(83)));

		// adds labels and text fields
		educationPanel.setLayout(null);
		educationPanel.add(institutionComboBox);
		educationPanel.add(lblCreditPoints);
		educationPanel.add(lblInstitutionName);
		educationPanel.add(lblDepartmentName);
		educationPanel.add(lblAverageGrade);
		educationPanel.add(textAverageGrade);
		educationPanel.add(textCreditPoints);
		educationPanel.add(lblGraduationDate);
		educationPanel.add(btnAddRow);
		educationPanel.add(btnUpdate);
		educationPanel.add(btnDelete);
		educationPanel.add(btnNewInstitute);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(167, 221, 341, 19);
		educationPanel.add(dateChooser);

		setLayout(groupLayout);

	}

	private void fetchAndRefresh() {
		departments = InstitutionLogic.getInstance().getDepartments();
		institutions = InstitutionLogic.getInstance().getInstitution();
	}

	private void createEvents() {

		
		btnNewInstitute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewInstitute.setVisible(!addNewInstitute.isVisible());

				// Change button text
				if (addNewInstitute.isVisible()) {
					btnNewInstitute.setText("- Request To Add New Institute");
				} else {
					btnNewInstitute.setText("+ Request To Add New Institute");
				}
			}
		});

		// Add action listener to the first combo box
		institutionComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateDepartmentComboBox();
			}
		});

		institutionComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				updateInstitutionComboBox();
			}
		});

		departmentComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				updateDepartmentComboBox();
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int i = table.getSelectedRow();
				selectItemInstitution(model.getValueAt(i, 0).toString());
				selectItemDepartment(model.getValueAt(i, 1).toString());

				try {
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String) model.getValueAt(i, 6).toString());
					dateChooser.setDate(date);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				textCreditPoints.setText(model.getValueAt(i, 7).toString());
				textAverageGrade.setText(model.getValueAt(i, 8).toString());

			}

		});

		// credit points and average grade -> only numbers validation /space/backspace
		textCreditPoints.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				char key = e.getKeyChar();
				if (!(Character.isDigit(key)) && !(Character.isWhitespace(key)) && !(Character.isISOControl(key))) {
					// textAverageGrade.setEditable(true);
					e.consume();

					JOptionPane.showMessageDialog(null, "Enter only numbers. ", "ERROR", JOptionPane.ERROR_MESSAGE);

				}
			}
		});

		textAverageGrade.addKeyListener(new KeyAdapter() {
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

		// add btn
		btnAddRow.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {


				Object[] row = new Object[9];

				// TODO Auto-generated method stub
				fetchAndRefresh();
				if (inAddMode != null) {
					if (inAddMode != false) {
						if (areAllFieldsFilled()) {
							if (!ApplicantLogic.getInstance().backgroundEducationAlreadyExists(
									getCombotypeInstitution(), getCombotypeDepartment(), applicantId)) {
								if (isValidInt(textCreditPoints.getText())) {
									if (checkGrade(textAverageGrade)) {
										if (checkGraduationDate())

										{
											fetchAndRefresh();

											String choosenInstId = getCombotypeInstitution();
											String choosenDepId = getCombotypeDepartment();

											Department currentDep = departments.get(choosenDepId + choosenInstId);// gets
																													// the
																													// dep
											// from // DB
											Institution currentInst = institutions.get(choosenInstId);// gets the inst
																										// from
																										// DB
											row[0] = choosenInstId;
											row[1] = choosenDepId;
											row[2] = currentDep.getDepartmentPhoneNumber();
											row[3] = currentInst.getInstitutionCountry();
											row[4] = currentInst.getCity();
											row[5] = currentInst.getStreetAddress();

											try {
												SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
												String date = dateFormat.format(dateChooser.getDate());
												row[6] = date;

											} catch (Exception ex) {
												JOptionPane.showMessageDialog(null, "No Date Selected");
											}
											// row[6]=dateChooser.getDateFormatString();
											row[7] = textCreditPoints.getText();
											row[8] = textAverageGrade.getText();
											/*
											 * (String institutionName,String departmentName,String applicantID,Date
											 * graduationDate ,int creditPoints,int averageGrade)
											 */

											Boolean success = ApplicantLogic.getInstance().addBackgroundEducations(
													choosenInstId, choosenDepId, applicantId, dateChooser.getDate(),
													Integer.parseInt(textCreditPoints.getText()),
													Double.parseDouble(textAverageGrade.getText()));

											fetchAndRefresh();

											model.addRow(row);

											if (success)
												JOptionPane.showMessageDialog(null,
														"Education Diploma Record Added Successfully", "Success",
														JOptionPane.INFORMATION_MESSAGE);

											fetchAndRefresh();

										} else
											JOptionPane.showMessageDialog(null,
													"Graduation Date should be before todays date ", "ERROR",
													JOptionPane.ERROR_MESSAGE);

									} else
										JOptionPane.showMessageDialog(null, "Average Grade must be between 55 and 100",
												"ERROR", JOptionPane.ERROR_MESSAGE);
								} else
									JOptionPane.showMessageDialog(null,
											"number must be a valid integer between 0 and 999", "ERROR",
											JOptionPane.ERROR_MESSAGE);

							} else
								JOptionPane.showMessageDialog(null,
										"Please fill a valid Background Education , no duplicates ", "ERROR",
										JOptionPane.ERROR_MESSAGE);
						} else
							JOptionPane.showMessageDialog(null,
									"Please fill all required fields (Institution, Department, Graduation Date, Credit Points, Average Grade) ",
									"ERROR", JOptionPane.ERROR_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null, "Please fill a valid applicant id ", "ERROR",
								JOptionPane.ERROR_MESSAGE);
				} else
					JOptionPane.showMessageDialog(null, "Please fill a valid applicant id ", "ERROR",
							JOptionPane.ERROR_MESSAGE);
			}

		});

		/// update btn
		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int i = table.getSelectedRow();
				if (i >= 0) {
					if (ApplicantLogic.getInstance().applicantAlreadyExists(applicantId)) {
						if (areAllFieldsFilled()) {

							if (isValidInt(textCreditPoints.getText())) {
								if (checkGrade(textAverageGrade)) {
									if (checkGraduationDate()) {
										String oldInstitutionName = (String) model.getValueAt(i, 0); // Assuming
																										// institutionName
																										// is the first
																										// column
										String oldDepartmentName = (String) model.getValueAt(i, 1); // Assuming
																									// departmentName is
																									// the second column

										String newInstitutionName = getCombotypeInstitution();
										String newDepartmentName = getCombotypeDepartment();

										if (oldInstitutionName.compareTo(newDepartmentName) == 0
												|| oldDepartmentName.compareTo(newDepartmentName) == 0) {
											fetchAndRefresh();

											Department currentDep = departments
													.get(oldDepartmentName + oldInstitutionName);// gets the department
											Institution currentInst = institutions.get(oldInstitutionName);// gets the
																											// inst
											model.setValueAt(oldInstitutionName, i, 0);
											model.setValueAt(oldDepartmentName, i, 1);

											model.setValueAt(currentDep.getDepartmentPhoneNumber(), i, 2);
											model.setValueAt(currentInst.getInstitutionCountry(), i, 3);
											model.setValueAt(currentInst.getCity(), i, 4);
											model.setValueAt(currentInst.getStreetAddress(), i, 5);

											try {
												SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
												String date = dateFormat.format(dateChooser.getDate());
												model.setValueAt(date, i, 6);

											} catch (Exception ex) {
												JOptionPane.showMessageDialog(null, "Not a Valid Date");
											}
											// String institutionName, String departmentName, String applicantID,
											// Date graduationDate, int creditPoints, int averageGrade
											model.setValueAt(textCreditPoints.getText(), i, 7);
											model.setValueAt(textAverageGrade.getText(), i, 8);

											Boolean success = ApplicantLogic.getInstance().editBackgroundEducations(
													oldInstitutionName, oldDepartmentName, applicantId,
													dateChooser.getDate(), Integer.parseInt(textCreditPoints.getText()),
													Double.parseDouble(textAverageGrade.getText()));

											if (success)
												JOptionPane.showMessageDialog(null, "Education Diploma Record updated ",
														"information", JOptionPane.INFORMATION_MESSAGE);

											fetchAndRefresh();

										} else if (!ApplicantLogic.getInstance().backgroundEducationAlreadyExists(
												newInstitutionName, newDepartmentName, applicantId)) {

											Department currentDep = departments
													.get(newDepartmentName + newInstitutionName);
											Institution currentInst = institutions.get(newInstitutionName);

											model.setValueAt(newInstitutionName, i, 0);
											model.setValueAt(newDepartmentName, i, 1);
											model.setValueAt(currentDep.getDepartmentPhoneNumber(), i, 2);
											model.setValueAt(currentInst.getInstitutionCountry(), i, 3);
											model.setValueAt(currentInst.getCity(), i, 4);
											model.setValueAt(currentInst.getStreetAddress(), i, 5);
											try {
												SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
												String date = dateFormat.format(dateChooser.getDate());
												model.setValueAt(date, i, 6);

											} catch (Exception ex) {
												JOptionPane.showMessageDialog(null, "Not a Valid Date");
											}
											// String institutionName, String departmentName, String applicantID,
											// Date graduationDate, int creditPoints, int averageGrade
											model.setValueAt(textCreditPoints.getText(), i, 7);
											model.setValueAt(textAverageGrade.getText(), i, 8);

											Boolean deleteSuccess = ApplicantLogic.getInstance()
													.removeBackgroundEducations(oldInstitutionName, oldDepartmentName,
															applicantId);

											if (deleteSuccess) {
												// Insert the new record
												Boolean insertSuccess = ApplicantLogic.getInstance()
														.addBackgroundEducations(newInstitutionName, newDepartmentName,
																applicantId, dateChooser.getDate(),
																Integer.parseInt(textCreditPoints.getText()),
																Double.parseDouble(textAverageGrade.getText()));

												if (insertSuccess) {
													JOptionPane.showMessageDialog(null,
															"Education Diploma Record updated", "Information",
															JOptionPane.INFORMATION_MESSAGE);
													fetchAndRefresh();
												} else {
													JOptionPane.showMessageDialog(null, "Insert failed", "ERROR",
															JOptionPane.ERROR_MESSAGE);
												}
											} else {
												JOptionPane.showMessageDialog(null, "Delete failed", "ERROR",
														JOptionPane.ERROR_MESSAGE);
											}
										} else {
											JOptionPane.showMessageDialog(null, "Update Error. Please Insert valid id ", "ERROR",
													JOptionPane.ERROR_MESSAGE);
										}

									} else
										JOptionPane.showMessageDialog(null, "Graduation Date should be before today",
												"ERROR", JOptionPane.ERROR_MESSAGE);
								} else
									JOptionPane.showMessageDialog(null, "Average Grade must be between 55 and 100",
											"ERROR", JOptionPane.ERROR_MESSAGE);
							} else
								JOptionPane.showMessageDialog(null, "credit Points must be bigger than 0", "ERROR",
										JOptionPane.ERROR_MESSAGE);

						} else
							JOptionPane.showMessageDialog(null,
									"Please fill all required fields (Institution, Department, Graduation Date, Credit Points, Average Grade)",
									"ERROR", JOptionPane.ERROR_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null,
								"Applicant does not exist. Please provide a valid applicant ID", "ERROR",
								JOptionPane.ERROR_MESSAGE);

				} else
					JOptionPane.showMessageDialog(null, "Update Error. Please Select A valid Row", "ERROR",
							JOptionPane.ERROR_MESSAGE);

			}

		});

		/// delete btn - action
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int i = table.getSelectedRow();
				if (i >= 0) {
					fetchAndRefresh();
					int deleteItem = JOptionPane.showConfirmDialog(null, "Confirm if you want to delete item?",
							"Warning", JOptionPane.YES_NO_OPTION);
					if (deleteItem == JOptionPane.YES_OPTION) {
						Boolean suceess = ApplicantLogic.getInstance().removeBackgroundEducations(
								model.getValueAt(i, 0).toString(), model.getValueAt(i, 1).toString(), applicantId);
						fetchAndRefresh();
						model.removeRow(i);

						if (suceess) {
							resetJtextFields();
						
							JOptionPane.showMessageDialog(null, "Education Diploma Record Deleted.", "information",
									JOptionPane.INFORMATION_MESSAGE);
						}

					}

				} else
					JOptionPane.showMessageDialog(null, "Delete Error. Please Choose Row!", "ERROR",
							JOptionPane.ERROR_MESSAGE);
			}

		});

	}

	// checks if all Fields are entered
	public Boolean areAllFieldsFilled() {
		Boolean fieldsFilled = false;
		Date chosenDate = dateChooser.getDate();

		if (chosenDate == null)
			return false;

		fieldsFilled = getCombotypeInstitution() != null && getCombotypeDepartment() != null
				&& !textCreditPoints.getText().isEmpty() && !textAverageGrade.getText().isEmpty()
				&& !dateChooser.getDate().equals(null);

		return fieldsFilled;
	}

	// return the selected institution
	public String getCombotypeInstitution() {
		return (String) institutionComboBox.getSelectedItem();
	}

	// return the selected department
	public String getCombotypeDepartment() {
		return (String) departmentComboBox.getSelectedItem();
	}

	// Method to update the second combo box based on the selected institution
	public void updateDepartmentComboBox() {
		fetchAndRefresh();
		String selectedInstitution = getCombotypeInstitution();

		if (selectedInstitution != null && institutions.containsKey(selectedInstitution)) {
			ArrayList<Department> currentDeps = institutions.get(selectedInstitution).getDeps();

			departmentComboBox.removeAllItems(); // Clear existing items

			if (currentDeps != null && !currentDeps.isEmpty()) {
				for (Department department : currentDeps) {
					departmentComboBox.addItem(department.getDepartmentName());
				}
			}
				
		}
	}

	// Method to validate that input is an integer and within range
	public boolean isValidInt(String text) {
		try {
			int num = Integer.parseInt(text);
			return num > 0 && num <= 999; // Ensures the integer is within 0-999 range
		} catch (NumberFormatException e) {

			return false;
		}
	}

	// sets the updated institution in the combo box
	public void selectItemInstitution(String itemToSelect) {
		institutionComboBox.setSelectedItem(itemToSelect);
	}

	// sets the updated department in the combo box
	public void selectItemDepartment(String itemToSelect) {
		updateDepartmentComboBox();
		departmentComboBox.setSelectedItem(itemToSelect);
	}

	// makes sure that the entered grade is above 55 and under or equals 100
	public boolean checkGrade(JTextField textfield) {
		if (isValidInt(textfield.getText())) {
			double num = Double.parseDouble(textfield.getText());
			boolean valid = num > 55 && num <= 100;

			return valid;
		}
		return false;
	}

	// sets the applicantID
	public void setApplicantId(String id) {
		this.applicantId = id;
	}

	// sets the mode
	public void setAddMode(Boolean mode) {
		this.inAddMode = mode;
	}

	// returns true if the check in day equals or smaller than today else false
	public Boolean checkGraduationDate() {
		Date checkDate = dateChooser.getDate();
		Date currentDate = new Date();
		if (checkDate.before(currentDate) || checkDate.equals(currentDate))
			return true;
		return false;

	}

	// initialize the institute combo box
	public void updateInstitutionComboBox() {
		fetchAndRefresh();
		institutionComboBox.removeAllItems(); // Clear existing items
		if (institutions != null && !institutions.isEmpty()) {
			for (Entry<String, Institution> entry : institutions.entrySet()) {
				String key = entry.getKey();
				institutionComboBox.addItem(key);
			}
		} else {
			// Handle case where institutions map is empty or null
		}

	}

	// adds row to the table in the event of updated applicant
	public void addRowTOJtable(Object[] row) {
		model.addRow(row);
	}
	
	
	
	// adds row to the table in the event of updated applicant
		public void setBackgroundsDiplomas(Applicant AP) {
		ArrayList<BackgroundEducation>b=AP.getDiplomas();
		/*"Institution Name", "Department Name", "Department PhoneNumber", "Country", "City",
		"Street address", "Graduation Date", "Credit Points", "Average Grade"*/
	    // Clear the table model
	    model.setRowCount(0);
	    
	    // Ensure the table has enough rows for the data
	    if (b.size() > 0) {
	        model.setRowCount(b.size());
	    }
		int i=0;
		for(BackgroundEducation b1 :b)
		{
			String departmentName=b1.getDepartmentname();
			String institutionName=b1.getInstutionName();
			Department dep = departments.get(departmentName + institutionName);
			Institution inst = institutions.get(institutionName);
		     // Safeguard against null values
	        String depPhoneNumber = dep.getDepartmentPhoneNumber() ;
	        String instCountry =  inst.getInstitutionCountry();
	        String instCity =inst.getCity() ;
	        String instStreetAddress = inst.getStreetAddress();
	        Date graduationDate =  b1.getGraduationDate() ;
	        Double averageGrade =  b1.getAverageGrade();
	        int creditPoints =  b1.getCreditPoints() ;

	        // Set values in the model
	        model.setValueAt(institutionName, i, 0);
	        model.setValueAt(departmentName, i, 1);
	        model.setValueAt(depPhoneNumber, i, 2);
	        model.setValueAt(instCountry, i, 3);
	        model.setValueAt(instCity, i, 4);
	        model.setValueAt(instStreetAddress, i, 5);
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(graduationDate);
	        
	        
	        model.setValueAt(date, i, 6);
	        model.setValueAt(averageGrade, i, 7);
	        model.setValueAt(creditPoints, i, 8);
			
			
			
			/*model.setValueAt(institutionName, i, 0);
			model.setValueAt(departmentName, i, 1);
			model.setValueAt(Dep.getDepartmentPhoneNumber(), i, 2);
			model.setValueAt(inst.getInstitutionCountry(), i, 3);
			model.setValueAt(inst.getCity(), i, 4);
			model.setValueAt(inst.getStreetAddress(), i, 5);
			model.setValueAt(b1.getGraduationDate(), i, 6);
			model.setValueAt(b1.getAverageGrade(), i, 7);
			model.setValueAt(b1.getCreditPoints(), i, 8);*/
			i++;	
		}
		
		
	}
	
	// adds row to the table in the event of updated applicant
	public void resetJtextFields() {
		textAverageGrade.setText("");
		textCreditPoints.setText("");
		dateChooser.setDate(null);
	}

	// reset the table
	public void resetJtable() {
		model.setRowCount(0); // Clears all rows
	}
}
