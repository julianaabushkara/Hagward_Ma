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
import java.sql.Date;
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
import javax.swing.table.DefaultTableModel;
import control.ApplicantLogic;
import control.StageLogic;
import entity.Applicant;
import entity.ApplicantDocument;
import entity.ApplicantStatus;
import entity.BackgroundEducation;
import entity.Stage;

public class FrmFinalPeriod extends JPanel {

	private JPanel applicantsPnl;
	private JPanel applicantBackGrounds;
	private JPanel applicantsInformationPnl;

	// comboBox
	private JComboBox<String> statusComboBox;
	private GridBagConstraints gbc2, gbc;
	private JLabel applicantStatusLbl, applicantIdlbl, applicantIdTxtlbl, err;
	private JLabel title;

	private JButton editBtn,resetBtn;

	// soundPlayer
	SoundPlayer btnSound;

	private JTable tableBackGrounds, tableApplicants;
	private Object[] rowBackgrounds;
	private Object[] rowApplicants;

	private JScrollPane scrollPaneBackGrounds, scrollPaneApplicant;
	private DefaultTableModel modelBackGrounds, modelApplicant;
	private List<Applicant> applicants;
	ActionListener editButtonListener;

	public FrmFinalPeriod() {

		fetchAndRefresh();
		initComponents();
		createEvents();

	}

	private void createEvents() {
		// TODO Auto-generated method stub

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
		
		
		
		editButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndex = tableApplicants.getSelectedRow(); // Fetch the latest selected row index
				if (selectedRowIndex >= 0) {
					String selectedApplicantId = String.valueOf(modelApplicant.getValueAt(selectedRowIndex, 0));

					String selectedValue = getComboStatus();

					if (!selectedValue.isEmpty()) {
						fetchAndRefresh();
						String status = ApplicantLogic.getInstance().getApplicants().get(selectedApplicantId)
								.getStatus();
						if (selectedValue.equals(status)) {
							JOptionPane.showMessageDialog(null, "Applicant Already With This Status", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}

						else {
							// Updating the stage for the selected applicant
							Boolean editApplicant = ApplicantLogic.getInstance()
									.updateStatusForApplicant(selectedApplicantId, selectedValue);

							fetchAndRefresh();

							if (editApplicant) {
								modelApplicant.setValueAt(ApplicantLogic.getInstance().getApplicants()
										.get(selectedApplicantId).getStatus(), selectedRowIndex, 5);
								JOptionPane.showMessageDialog(null, "Status Updated", "Success",
										JOptionPane.INFORMATION_MESSAGE);
							}

						}
					} else
						JOptionPane.showMessageDialog(null, "Please select a status", "ERROR",
								JOptionPane.ERROR_MESSAGE);

				} else {
					JOptionPane.showMessageDialog(null, "Please select a row", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		};

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
						rowBackgrounds[1] = b.getInstutionName();
						rowBackgrounds[2] = b.getDepartmentname();
						rowBackgrounds[3] = b.getAverageGrade();
						rowBackgrounds[4] = b.getCreditPoints();

						modelBackGrounds.addRow(rowBackgrounds);
					}

					// Attach the action listener to the edit button
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


		gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(10, 10, 10, 10);

		// panels
		// Configure constraints for the first panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.applicantsPnl = new JPanel(new GridBagLayout());
		this.applicantsPnl.setBackground(new Color(230, 230, 250));
		add(applicantsPnl, gbc);

		// Configure constraints for the second panel
		gbc.gridy = 1;
		this.applicantBackGrounds = new JPanel(new GridBagLayout());
		this.applicantBackGrounds.setBackground(Color.LIGHT_GRAY);
		add(applicantBackGrounds, gbc);

		// Configure constraints for the third panel
		gbc.gridy = 2;
		gbc.weighty = 2;
		this.applicantsInformationPnl = new JPanel(new GridBagLayout());
		this.applicantsInformationPnl.setBackground(Color.GRAY);
		add(applicantsInformationPnl, gbc);

		this.editBtn = new JButton();
		editBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/editBtn.png")));
		editBtn.setPreferredSize(new Dimension(90, 45));
		
		//
		this.resetBtn = new JButton();
		resetBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/ResetBtn.png")));
		resetBtn.setPreferredSize(new Dimension(90, 45));

		// labels:
		applicantStatusLbl = new JLabel("Choose Status: ");
		applicantStatusLbl.setFont(new Font("Serif", Font.BOLD, 14));

		err = new JLabel("");
		err.setFont(new Font("Serif", Font.BOLD, 14));
		err.setBackground(Color.red);

		applicantIdlbl = new JLabel("applicant Id: ");
		applicantIdlbl.setFont(new Font("Serif", Font.BOLD, 14));

		applicantIdTxtlbl = new JLabel("Text");
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

		// stage
		gbc2.gridx = 1;
		gbc2.gridy = 1;
		gbc2.gridwidth = 1;
		gbc2.anchor = GridBagConstraints.LINE_END;
		applicantsPnl.add(applicantIdTxtlbl, gbc2);

		gbc2.gridx = 0;
		gbc2.gridy = 2;
		gbc2.anchor = GridBagConstraints.LINE_START;
		gbc2.gridwidth = GridBagConstraints.REMAINDER;

		applicantsPnl.add(applicantStatusLbl, gbc2);

		gbc2.gridx = 1;
		gbc2.gridwidth = 1;
		gbc2.anchor = GridBagConstraints.LINE_END;
		applicantsPnl.add(statusComboBox, gbc2);

		// editBtn
		gbc2.gridx = 1;
		gbc2.gridy = 3;
		gbc2.gridwidth = 2;
		gbc2.anchor = GridBagConstraints.CENTER;
		applicantsPnl.add(editBtn, gbc2);
		
		// editBtn
		gbc2.gridx = 0;
		gbc2.gridy = 3;
		gbc2.gridwidth = 2;
		gbc2.anchor = GridBagConstraints.CENTER;
		applicantsPnl.add(resetBtn, gbc2);

		/*
		 * tableApplicants; private Object[] rowDocs; private Object[] rowApplicants;
		 * 
		 * private JScrollPane scrollPaneDocs, scrollPaneApplicant; private
		 * DefaultTableModel modelDocs, modelApplicant;
		 * 
		 */

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

		//applicantsInformationPnl.setBackground(new Color(230, 230, 250));

		// documents panel

		/*
		 * tableApplicants; private Object[] rowDocs; private Object[] rowApplicants;
		 * 
		 * private JScrollPane scrollPaneDocs, scrollPaneApplicant; private
		 * DefaultTableModel modelDocs, modelApplicant;
		 * 
		 */

		// applicants panel
		// Initialize and configure tablePnl
		scrollPaneBackGrounds = new JScrollPane();
		tableBackGrounds = new JTable();
		Object[] columnsBackgrounds = { "Instution Name", "Department Name", "Graduation Date", "Average Grade",
				"Credit Points" };
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

		//applicantBackGrounds.setBackground(new Color(230, 230, 250));

	}

	private void fetchAndRefresh() {
		// filterByFinalDesicion()
		applicants = ApplicantLogic.getInstance().filterByFinalDesicion();

	}

	public JComboBox<String> createComboBox() {
		String[] statusTypeStrings = new String[2];
		statusTypeStrings[0] = ApplicantStatus.Invite_interview.toString();
		statusTypeStrings[1] = ApplicantStatus.rejected_without_interview.toString();

		return new JComboBox<>(statusTypeStrings);
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
		FrmFinalPeriod panel = new FrmFinalPeriod();
		frame.add(panel);

		// Make the frame visible
		frame.setVisible(true);
	}*/
}