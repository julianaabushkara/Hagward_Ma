package boundary;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import control.InstitutionLogic;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FrmAddNewInstitution extends JPanel {

	private static final long serialVersionUID = 1L;

	//Jlabels
	private JLabel lblStreetAddress;
	private JLabel lblCity;
	private JLabel lblInstitutionCountry;
	private JLabel lblDepartmentName;
	private JLabel lblDepartmentPhoneNumb;
	private JLabel lblInstitutionName;
	private JLabel lblAddNewInstitute;
	
	
	//SaveButto
	private JButton btnSaveInstitute;
	
	//JtextField
	private JTextField textDepartmentPhoneNumber;
	private JTextField textDepartName;
	private JTextField textInstituteName;
	private JTextField textCountry;
	private JTextField textAdress;
	private JTextField textCity;



	/**
	 * Create the panel.
	 */
	public FrmAddNewInstitution() {

		initComponents();
		fetchAndRefresh();
		createEvents();

	}

	private void createEvents() {
		
		btnSaveInstitute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (fieldsAreFilled()) {
					if (!InstitutionLogic.getInstance().institutionIsValid(textInstituteName.getText(), textDepartName.getText())) {
						Boolean SuccessInst=InstitutionLogic.getInstance().addInstitution(textInstituteName.getText(),
								textCountry.getText(), textCity.getText(), textAdress.getText());
						Boolean SuccessDepartment= InstitutionLogic.getInstance().addDepartment(textDepartName.getText(), textInstituteName.getText(), textDepartmentPhoneNumber.getText());
						
						// Refresh your UI or perform other necessary actions
		                fetchAndRefresh();
					
						if(SuccessInst&&SuccessDepartment)
						JOptionPane.showMessageDialog(null, "Request to add institution and department completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

					} else
						JOptionPane.showMessageDialog(null, "The institution and department already exist.", "ERROR",
								JOptionPane.ERROR_MESSAGE);

				} else
					JOptionPane.showMessageDialog(null, "Please fill in the institution name and department name fields.",
							"ERROR", JOptionPane.ERROR_MESSAGE);

			}
		});

	}

	

	private void fetchAndRefresh() {
		
	}

	private void initComponents() {

		setLayout(null);

		
		
		lblStreetAddress = new JLabel("StreetAddress:");
		lblStreetAddress.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblStreetAddress.setBounds(48, 272, 136, 19);
		add(lblStreetAddress);

		lblCity = new JLabel("City:");
		lblCity.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCity.setBounds(48, 229, 125, 19);
		add(lblCity);

		lblInstitutionCountry = new JLabel("Country:");
		lblInstitutionCountry.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblInstitutionCountry.setBounds(48, 187, 144, 21);
		add(lblInstitutionCountry);

		lblDepartmentPhoneNumb = new JLabel("Department PhoneNumber :");
		lblDepartmentPhoneNumb.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblDepartmentPhoneNumb.setBounds(48, 148, 233, 19);
		add(lblDepartmentPhoneNumb);

		lblDepartmentName = new JLabel("Department Name:");
		lblDepartmentName.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblDepartmentName.setBounds(48, 99, 219, 19);
		add(lblDepartmentName);

		lblInstitutionName = new JLabel("Institution Name:");
		lblInstitutionName.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblInstitutionName.setBounds(48, 54, 219, 19);
		add(lblInstitutionName);

		lblAddNewInstitute = new JLabel("add New Institute");
		lblAddNewInstitute.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAddNewInstitute.setBounds(175, 10, 219, 19);
		add(lblAddNewInstitute);

		textDepartmentPhoneNumber = new JTextField();
		textDepartmentPhoneNumber.setBounds(264, 145, 256, 30);
		add(textDepartmentPhoneNumber);
		textDepartmentPhoneNumber.setColumns(10);

		textDepartName = new JTextField();
		textDepartName.setColumns(10);
		textDepartName.setBounds(264, 96, 256, 30);
		add(textDepartName);

		textInstituteName = new JTextField();
		textInstituteName.setColumns(10);
		textInstituteName.setBounds(264, 51, 256, 30);
		add(textInstituteName);

		textCountry = new JTextField();
		textCountry.setColumns(10);
		textCountry.setBounds(264, 185, 256, 30);
		add(textCountry);

		textAdress = new JTextField();
		textAdress.setColumns(10);
		textAdress.setBounds(264, 267, 256, 34);
		add(textAdress);

		textCity = new JTextField();
		textCity.setColumns(10);
		textCity.setBounds(264, 226, 256, 30);
		add(textCity);

		btnSaveInstitute = new JButton("Save Institute");
		btnSaveInstitute.setBounds(175, 311, 136, 50);
		add(btnSaveInstitute);

	}

	
	
	
	
	// checks if all Fields are entered - that cant be null
	public Boolean fieldsAreFilled() {
		Boolean fieldsFilled = false;
		fieldsFilled = !textInstituteName.getText().isEmpty() && !textDepartName.getText().isEmpty();
		return fieldsFilled;
	}



}
