package boundary;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import control.ApplicantLogic;
import control.InstitutionLogic;
import control.LogInLogic;
import entity.Applicant;
import entity.BackgroundEducation;
import entity.Department;
import entity.Institution;
import entity.LogInInfo;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

public class FrmAddNewApplication extends JPanel {

	private static final long serialVersionUID = 1L;

	private HashMap<String, Applicant> applicants;

	// panels
	JPanel applicantInfomationPnl;
	FrmBackgroundEducation FrmBackground;

	// Jlabels
	private JLabel lblApplicantId;
	private JLabel lblFName;
	private JLabel lblLName;
	private JLabel lblEmail;
	private JLabel lblPhoneNum;
	private JLabel lblRegistration;
	private JLabel lblPassword;

	// JtextFields
	private JTextField textEmailAdress;
	private JTextField textPhoneNumber;
	private JTextField textFName;
	private JTextField textId;
	private JTextField textLastName;
	private JPasswordField textPassword;

	// set add mode
	private Boolean inAddMode;
	int i;
	private Boolean SignUp;
	private String UserId;

	// Jbuttons
	private JButton btnSaveApplicantInfo;
	private JButton btnUpdateInformation;
	private JButton btnSDeleteApplicantInfo;
	private JButton movetToHomePage;

	// constructor for the panel
	public FrmAddNewApplication(int i, String userId) {
		if (i == 1) {
			SignUp = true;
			UserId = "";
		} else {
			SignUp = false;
			UserId = userId;

		}

		initComponents();
		fetchAndRefresh();
		createEvents();

	}

	// fetches the applicant hashmap from the data base/control
	private void fetchAndRefresh() {
		applicants = ApplicantLogic.getInstance().getApplicants();

	}

	//
	private void createEvents() {
		// adds the new application

		movetToHomePage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				LogInPage login = new LogInPage();
				login.setVisible(true);

				// Dispose the parent frame
				JFrame parentFrame = getParentFrame();
				if (parentFrame != null) {
					parentFrame.dispose();
				}

			}
		});

		btnSaveApplicantInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// reInitialise the in add mode so every time we press save
				inAddMode = false;

				if (applicantIdFilled()) {
					if (!LogInLogic.getInstance().LogInExists(textId.getText())
							&& !ApplicantLogic.getInstance().applicantAlreadyExists(textId.getText())) {

						
						char[] password = textPassword.getPassword();

						String pass = new String(password);
						;
						// System.out.print("pass ="+pass);

						UserId = textId.getText();

						Boolean success2 = LogInLogic.getInstance().addLogIN(textId.getText(), pass, false);

						Boolean success = ApplicantLogic.getInstance().addApplicant(textId.getText(),
								textFName.getText(), textLastName.getText(), textEmailAdress.getText(),
								textPhoneNumber.getText());
						inAddMode = true;
						FrmBackground.resetJtable();

						FrmBackground.setApplicantId(textId.getText());
						FrmBackground.setAddMode(inAddMode);
						

						if (success) {
							JOptionPane.showMessageDialog(null, "Application record successfully added", "Success",
									JOptionPane.INFORMATION_MESSAGE);

							fetchAndRefresh();// updates the array

						}

					} else
						JOptionPane.showMessageDialog(null, "Application already exists. Please use a different ID.",
								"ERROR", JOptionPane.ERROR_MESSAGE);
				} else
					JOptionPane.showMessageDialog(null, "Please fill in the ID and PassWord field.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
			}
		});

		if (SignUp == true) {
			btnUpdateInformation.setVisible(false);
			btnSDeleteApplicantInfo.setVisible(false);
			textId.setEditable(true);
			movetToHomePage.setVisible(true);
		} else {
			textId.setEditable(false);
			textId.setText(UserId);
			btnUpdateInformation.setVisible(true);
			btnSDeleteApplicantInfo.setVisible(true);
			btnSaveApplicantInfo.setVisible(false);
			movetToHomePage.setVisible(false);
			textPassword.setEditable(false);
			fetchAndRefresh();// updates the array
			Applicant currentApplicant= applicants.get(UserId);

			FrmBackground.setBackgroundsDiplomas(currentApplicant);
			textEmailAdress.setText(currentApplicant.getEmailAdress());
			textPhoneNumber.setText(currentApplicant.getPhoneNumber());
			textFName.setText(currentApplicant.getFirstName());
			textLastName.setText(currentApplicant.getLastName());

		}

		
		
		////////////////////// updates the applicant info
		btnUpdateInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// we check if the id is filled and if its true we check if there is an
				// applicant with this ID
				if (applicantIdFilledDel_Upd()) {

					if (ApplicantLogic.getInstance().applicantAlreadyExists(UserId)) {
						fetchAndRefresh();// updates the array
						boolean success = ApplicantLogic.getInstance().editApplicant(textId.getText(),
								textFName.getText(), textLastName.getText(), textEmailAdress.getText(),
								textPhoneNumber.getText(), applicants.get(UserId).getSubmittionDate(),
								applicants.get(textId.getText()).getStatus(), applicants.get(UserId).getValid(),applicants.get(UserId).getCurrentStage());
						FrmBackground.resetJtable();
						// checks if the array list diplomas is not empty
						if (!applicants.get(UserId).getDiplomas().isEmpty()) {
							ArrayList<BackgroundEducation> tempList = new ArrayList<>();
							tempList = applicants.get(UserId).getDiplomas();

							for (BackgroundEducation b : tempList) {
								// dep+inst
								Institution tempInst = InstitutionLogic.getInstance().getInstitution()
										.get(b.getInstutionName());
								Department tempDep = InstitutionLogic.getInstance().getDepartments()
										.get(b.getDepartmentname() + b.getInstutionName());

								Object[] row = new Object[9];
								row[0] = tempInst.getInstitutionName();
								row[1] = tempDep.getDepartmentName();
								row[2] = tempDep.getDepartmentPhoneNumber();

								row[3] = tempInst.getInstitutionCountry();
								row[4] = tempInst.getCity();
								row[5] = tempInst.getStreetAddress();

								try {
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
									String date = dateFormat.format(b.getGraduationDate());
									row[6] = date;

								} catch (Exception ex) {
									JOptionPane.showMessageDialog(null, "Erorr");
								}

								row[7] = b.getCreditPoints();
								row[8] = b.getAverageGrade();
								FrmBackground.addRowTOJtable(row);
							}
						}

						inAddMode = true;
						FrmBackground.setApplicantId(UserId);
						FrmBackground.setAddMode(inAddMode);

						fetchAndRefresh();// updates the array
						if (success)
							JOptionPane.showMessageDialog(null, "Applicant information updated successfully.",
									"Success", JOptionPane.INFORMATION_MESSAGE);

					} else
						JOptionPane.showMessageDialog(null, "Application doesn't exist. Please Log In.", "ERROR",
								JOptionPane.ERROR_MESSAGE);
				} else
					JOptionPane.showMessageDialog(null, "Please fill in the Log In.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
			}
		});

		////// delete btn
		btnSDeleteApplicantInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// we check if the id is filled and if its true we check if there is an
				// applicant with this ID
				if (applicantIdFilledDel_Upd()) {

					if (ApplicantLogic.getInstance().applicantAlreadyExists(textId.getText())) {
						fetchAndRefresh();// updates the array

						int deleteItem = JOptionPane.showConfirmDialog(null,
								"Are you sure you want to delete this application?", "Warning",
								JOptionPane.YES_NO_OPTION);
						if (deleteItem == JOptionPane.YES_OPTION) {
							boolean success = ApplicantLogic.getInstance().removeApplicant(textId.getText());
							LogInLogic.getInstance().getLogIn().get(textId.getText()).setDeleted(true);
							boolean success2 = LogInLogic.getInstance().editLogIn(textId.getText(), true);
							

							FrmBackground.resetJtable();
							if (success) {
								resetApplicantJtextfields();
								FrmBackground.resetJtextFields();
								JOptionPane.showMessageDialog(null, "Application record deleted successfully.",
										"Success", JOptionPane.INFORMATION_MESSAGE);
							}

							fetchAndRefresh();
						}
						;
					} else
						JOptionPane.showMessageDialog(null, "Application doesn't exist. Please enter a valid ID.",
								"ERROR", JOptionPane.ERROR_MESSAGE);
				} else
					JOptionPane.showMessageDialog(null, "Please fill in the ID field.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
			}

		});
		
		
		
		

	}

	
	//////////////////////////////////////////////////////////////////// create
	//////////////////////////////////////////////////////////////////// panels
	private void initComponents() {
		// Set the preferred size for this panel
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int width = (int) (screenSize.getWidth() * 0.8);
		final int height = (int) (screenSize.getHeight() * 0.8);
		setPreferredSize(new Dimension(width-40, height-48));

		// Initialise the in add mode as false
		inAddMode = false;

		// You can add additional components and customizations here
		setBackground(new Color(128, 128, 192));
		applicantInfomationPnl = new JPanel();
		applicantInfomationPnl.setBorder(
				new TitledBorder(null, "Applicant Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		// background Education panel
		FrmBackground = new FrmBackgroundEducation();

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// Initialize components
		lblApplicantId = new JLabel("ID :  ");
		lblApplicantId.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblFName = new JLabel("First Name:");
		lblFName.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblLName = new JLabel("Last Name : ");
		lblLName.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblEmail = new JLabel("Email Adress :");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblPhoneNum = new JLabel("Phone Number : ");
		lblPhoneNum.setFont(new Font("Tahoma", Font.BOLD, 15));

		// text fields
		textEmailAdress = new JTextField();
		textEmailAdress.setColumns(10);

		textPhoneNumber = new JTextField();
		textPhoneNumber.setColumns(10);

		textFName = new JTextField();
		textFName.setColumns(10);

		textId = new JTextField();
		textId.setColumns(10);

		textLastName = new JTextField();
		textLastName.setColumns(10);

		// label registration
		lblRegistration = new JLabel("Registration Form");
		lblRegistration.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblRegistration.setHorizontalAlignment(SwingConstants.CENTER);

		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 15));

		textPassword = new JPasswordField();
		textPassword.setColumns(10);

		btnUpdateInformation = new JButton("Update Information ");
		btnUpdateInformation.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/editBtn.png")));
		btnUpdateInformation.setPreferredSize(new Dimension(100, 45));

		// btnUpdateInformation.setFont(new Font("Tahoma", Font.BOLD, 15));

		btnSDeleteApplicantInfo = new JButton("Delete Application");
		btnSDeleteApplicantInfo.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/DeleteBtn.png")));
		btnSDeleteApplicantInfo.setPreferredSize(new Dimension(100, 45));

		movetToHomePage = new JButton("Update Information ");
		movetToHomePage.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/finish.png")));
		movetToHomePage.setPreferredSize(new Dimension(100, 45));

		// btnSDeleteApplicantInfo.setFont(new Font("Tahoma", Font.BOLD, 15));

		btnSaveApplicantInfo = new JButton("Save");

		// btnSaveApplicantInfo.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSaveApplicantInfo.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/AddBtn.png")));
		btnSaveApplicantInfo.setPreferredSize(new Dimension(100, 45));
		
		
	

		// Panel setup
		applicantInfomationPnl.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		// gbc.anchor = GridBagConstraints.LINE_END;
		gbc.anchor = GridBagConstraints.CENTER;

		applicantInfomationPnl.add(lblRegistration, gbc);

		// second
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(lblApplicantId, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;

		// gbc.anchor = GridBagConstraints.LINE_END;
		applicantInfomationPnl.add(textId, gbc);

		// email
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(lblEmail, gbc);

		gbc.gridx = 3;
		gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(textEmailAdress, gbc);

		// third
		gbc.gridx = 0;
		gbc.gridy = 2;
		// gbc.anchor = GridBagConstraints.LINE_END;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;

		applicantInfomationPnl.add(lblFName, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(textFName, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		// gbc.anchor = GridBagConstraints.LINE_END;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;

		applicantInfomationPnl.add(lblLName, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(textLastName, gbc);

		// phone
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;

		// gbc.anchor = GridBagConstraints.LINE_END;
		applicantInfomationPnl.add(lblPhoneNum, gbc);

		gbc.gridx = 3;
		gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(textPhoneNumber, gbc);

		// password
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;

		// gbc.anchor = GridBagConstraints.LINE_END;
		applicantInfomationPnl.add(lblPassword, gbc);

		gbc.gridx = 3;
		// gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(textPassword, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.LINE_END;
		applicantInfomationPnl.add(btnSaveApplicantInfo, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(btnUpdateInformation, gbc);

		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(btnSDeleteApplicantInfo, gbc);

		gbc.gridx = 3;
		gbc.anchor = GridBagConstraints.LINE_START;
		applicantInfomationPnl.add(movetToHomePage, gbc);

		// applicantInfomationPnl.setLayout(applicantInfo);

		// Initialize scroll pane and add split pane
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, applicantInfomationPnl, FrmBackground);
		split.setResizeWeight(0.7); // Adjust the initial split ratio
		split.setOneTouchExpandable(true);
		split.setContinuousLayout(true);

		// scrollPane
		JScrollPane scrollPane = new JScrollPane(split);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);

	}

	// checks if all Fields are entered
	public Boolean applicantIdFilled() {
		Boolean fieldsFilled = false;
		char[] password = textPassword.getPassword();

		fieldsFilled = !textId.getText().isEmpty() && !(password.length == 0);
		return fieldsFilled;
	}
	
	
	// checks if all Fields are entered
	public Boolean applicantIdFilledDel_Upd() {
		Boolean fieldsFilled = false;

		fieldsFilled = !textId.getText().isEmpty();
		return fieldsFilled;
	}

	public void resetApplicantJtextfields() {
		textEmailAdress.setText("");
		textPhoneNumber.setText("");
		textFName.setText("");
		textId.setText("");
		textLastName.setText("");

	}

	// getter for inAddMode -> so we know if its an update or a new applicant
	public Boolean getInAddMode() {
		return inAddMode;
	}

	// getter for inAddMode -> so we know if its an update or a new applicant
	public Boolean getSucessInSignUp() {
		return inAddMode;
	}

	// getter for inAddMode -> so we know if its an update or a new applicant
	public LogInInfo getLogInUser() {
		LogInInfo usersInfo;
		usersInfo = LogInLogic.getInstance().getLogIn().get(UserId);
		return usersInfo;
	}

	private JFrame getParentFrame() {
		// Find the parent JFrame of this panel
		java.awt.Component parent = this;
		while (parent != null && !(parent instanceof JFrame)) {
			parent = parent.getParent();
		}
		return (JFrame) parent;
	}


}
