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
import entity.Stage;

public class FrmUpdateStage extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel applicantsPnl;
	private JPanel applicantDocuments;
	private JPanel applicantsInformationPnl;
	// comboBox
	private JComboBox<String> stageTypeComboBox;
	private GridBagConstraints gbc2, gbc;
	private JLabel applicantStageLbl, applicantIdlbl, applicantIdTxtlbl, err;
	private JLabel title;


	private JButton editBtn;



	// soundPlayer
	SoundPlayer btnSound;

	private JTable tableDocs, tableApplicants;
	private Object[] rowDocs;
	private Object[] rowApplicants;

	private JScrollPane scrollPaneDocs, scrollPaneApplicant;
	private DefaultTableModel modelDocs, modelApplicant;
	private HashMap<String, Applicant> applicants;
	ActionListener editButtonListener;

	/**
	 * Launch the application.
	 */

	public FrmUpdateStage() {

		fetchAndRefresh();
		initComponents();
		createEvents();

	}

	private void createEvents() {
		// TODO Auto-generated method stub

		editButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndex = tableApplicants.getSelectedRow(); // Fetch the latest selected row index
				if (selectedRowIndex >= 0) {
					String selectedApplicantId = modelApplicant.getValueAt(selectedRowIndex, 0).toString();

					int selectedValue = 0;
					try {
						selectedValue = Integer.parseInt(getCombotypeDocument());
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Selected item is not a valid number", "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}

					fetchAndRefresh();
					int currentStage=ApplicantLogic.getInstance().getApplicants().get(selectedApplicantId).getCurrentStage();
					if(selectedValue <= currentStage)
					{
						JOptionPane.showMessageDialog(null, "Applicant Already Passed the choosen Stage", "ERROR", JOptionPane.ERROR_MESSAGE);
					}

					else {
					// Updating the stage for the selected applicant
					Boolean editApplicant = StageLogic.getInstance().updateStageFotApplicant(selectedApplicantId,
							selectedValue);
					Boolean archiveApplicantStage = StageLogic.getInstance()
							.archiveApplicantStage(selectedApplicantId, selectedValue);

					fetchAndRefresh();

					if (editApplicant && archiveApplicantStage) {
						modelApplicant.setValueAt(
								ApplicantLogic.getInstance().getApplicants().get(selectedApplicantId).getCurrentStage(),
								selectedRowIndex, 7);
						JOptionPane.showMessageDialog(null, "Stage Updated", "Success",
								JOptionPane.INFORMATION_MESSAGE);
					}
				
				
					}
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

					ArrayList<ApplicantDocument> applicantDocs = ApplicantLogic.getInstance().getApplicants()
							.get(modelApplicant.getValueAt(i, 0).toString()).getDocuments();
					if (applicantDocs.isEmpty()) {
						err.setVisible(true);
						err.setText("**There is no Documents**");
					}

					err.setVisible(false);

					modelDocs.setRowCount(0);
					for (ApplicantDocument d : applicantDocs) {
						rowDocs[0] = d.getDocumentId();
						rowDocs[1] = d.getDocumentName();
						rowDocs[2] = d.getType();
						rowDocs[3] = d.getFolderlink();
						rowDocs[4] = d.getStageID();

						modelDocs.addRow(rowDocs);
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
		this.applicantsPnl.setBackground(Color.white);
		add(applicantsPnl, gbc);

		// Configure constraints for the second panel
		gbc.gridy = 1;
		this.applicantDocuments = new JPanel(new GridBagLayout());
		this.applicantDocuments.setBackground(Color.GREEN);
		add(applicantDocuments, gbc);

		// Configure constraints for the third panel
		gbc.gridy = 2;
		gbc.weighty = 2;
		this.applicantsInformationPnl = new JPanel(new GridBagLayout());
		this.applicantsInformationPnl.setBackground(Color.YELLOW);
		add(applicantsInformationPnl, gbc);

		this.editBtn = new JButton();
		editBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/editBtn.png")));
		editBtn.setPreferredSize(new Dimension(90, 45));

		// labels:
		applicantStageLbl = new JLabel("choose Stage: ");
		applicantStageLbl.setFont(new Font("Serif", Font.BOLD, 14));

		err = new JLabel("");
		err.setFont(new Font("Serif", Font.BOLD, 14));
		err.setBackground(Color.red);

		applicantIdlbl = new JLabel("applicant Id: ");
		applicantIdlbl.setFont(new Font("Serif", Font.BOLD, 14));

		applicantIdTxtlbl = new JLabel("");
		applicantIdTxtlbl.setFont(new Font("Serif", Font.BOLD, 14));

		// comboBox
		stageTypeComboBox = createComboBox();

		// title
		title = new JLabel("Update Applicant Stage");
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

		applicantsPnl.add(applicantStageLbl, gbc2);

		// stage
		gbc2.gridx = 1;
		gbc2.gridwidth = 1;
		gbc2.anchor = GridBagConstraints.LINE_END;
		applicantsPnl.add(stageTypeComboBox, gbc2);

		// editBtn
		gbc2.gridx = 1;
		gbc2.gridy = 3;
		gbc2.gridwidth = 2;
		gbc2.anchor = GridBagConstraints.CENTER;
		applicantsPnl.add(editBtn, gbc2);

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
		for (Applicant a : applicants.values()) {
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

		applicantsInformationPnl.setBackground(new Color(230, 230, 250));

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
		scrollPaneDocs = new JScrollPane();
		tableDocs = new JTable();
		Object[] columnsDocs = { "Document Id", "Document Name", "Document Type", "Folder Link", "Stage Id" };
		rowDocs = new Object[5];

		modelDocs = new DefaultTableModel();
		modelDocs.setColumnIdentifiers(columnsDocs);
		tableDocs.setModel(modelDocs);

		tableDocs.setRowHeight(40);

		// Clear all existing rows
		modelDocs.setRowCount(0);
		fetchAndRefresh();
		// Iterate over the updated list of Applicants

		GridBagConstraints gbcTableDocs = new GridBagConstraints();
		gbcTableDocs.gridx = 0; // Grid x position
		gbcTableDocs.gridy = 0; // Grid y position
		gbcTableDocs.fill = GridBagConstraints.BOTH; // Stretch to fill space
		gbcTableDocs.weightx = 1.0; // Allocate extra space horizontally
		gbcTableDocs.weighty = 1.0; // Allocate extra space vertically (optional)
		gbcTableDocs.insets = new Insets(10, 10, 10, 10); // Padding around the table
		scrollPaneDocs.setViewportView(tableDocs);

		// Add the scrollPane to the tablePnl with GridBagConstraints
		applicantDocuments.add(scrollPaneDocs, gbcTableDocs);

		applicantDocuments.setBackground(new Color(230, 230, 250));

	}

	private void fetchAndRefresh() {
		// TODO Auto-generated method stub
		applicants = ApplicantLogic.getInstance().getApplicants();

	}

	public JComboBox<String> createComboBox() {

		ArrayList<Stage> stageTypes = StageLogic.getInstance().getStage();
		String[] stageTypeStrings = new String[stageTypes.size()];

		int i = 0;
		for (Stage s : stageTypes) {
			stageTypeStrings[i] = String.valueOf(s.getStageId());
			i++;
		}

		return new JComboBox<>(stageTypeStrings);
	}

	// checks if all Fields are entered
	/*
	 * public Boolean areAllFieldsFilled() { Boolean fieldsFilled = false;
	 * 
	 * fieldsFilled = !documentNameTxt.getText().isEmpty() && getCombotypeDocument()
	 * != null && !documnetLink.isEmpty();
	 * 
	 * return fieldsFilled; }
	 */

	public String getCombotypeDocument() {
		return (String) stageTypeComboBox.getSelectedItem();
	}

	/*public static void main(String[] args) {
		// Create a frame to hold the panel
		JFrame frame = new JFrame("Attach Documents Panel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);

		// Add the AttachDocumentsPnl to the frame
		FrmUpdateStage panel = new FrmUpdateStage();
		frame.add(panel);

		// Make the frame visible
		frame.setVisible(true);
	}*/
}
