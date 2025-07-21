package boundary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
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
import control.DocumentLogic;
import entity.Applicant;
import entity.ApplicantDocument;
import entity.DocumentType;

import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.FlowLayout;

public class AttachDocumentsPnl extends JPanel {

	private JPanel tablePnl, infoPnl, applicantInfo, btnPanel;
	private JButton addBtn, editBtn, deleteBtn;
	// Jlabels
	private JLabel titleLbl;
	private JLabel applicantIdLbl;
	private JLabel applicantStageLbl;
	private JLabel documentNameLbl;
	private JLabel documentTypeLbl;

	// JtextFields
	private JTextField applicantIdTxt;
	private JTextField stageIdTxt;
	private JTextField documentNameTxt;

	// comboBox
	private JComboBox<String> documentTypeComboBox;
	private GridBagConstraints gbc2, gbc, gbcInfoPnl;

	private Applicant currentUser;
	private int applicantStage;
	private String documnetLink;

	// soundPlayer
	SoundPlayer btnSound;

	private JTable table;
	private Object[] row;
	private JScrollPane scrollPane;
	private DefaultTableModel model;
	private HashMap<String, Applicant> applicants;
	private HashMap<Integer, ApplicantDocument> documents;

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public AttachDocumentsPnl(String CurrentId) {

		
		fetchAndRefresh();

		this.currentUser = applicants.get(CurrentId);

		initComponents();
		createEvents();


	}

	private void createEvents() {
		// TODO Auto-generated method stub
		
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int i = table.getSelectedRow();
				if(i>=0)
				{	
					documentNameTxt.setText(model.getValueAt(i, 1).toString());
					documentTypeComboBox.setSelectedItem(model.getValueAt(i, 2).toString());
					documnetLink=model.getValueAt(i, 3).toString();
					
				}
				else 
					JOptionPane.showMessageDialog(null, "Please select a row", "ERROR", JOptionPane.ERROR_MESSAGE);
			}

		});

		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				btnSound.clickSound();
				String filePath = DocumentLogic.selectDocument();
				if (filePath != null) {
					documnetLink = filePath;
					// System.out.println("Selected file: " + filePath);
				}

				if (areAllFieldsFilled()) {
					

					int currentStage = ApplicantLogic.getInstance().getApplicantCurrentStage(currentUser);
					Boolean doc = DocumentLogic.getInstance().addDocument(documentNameTxt.getText().toString(),
							getCombotypeDocument(), documnetLink, currentUser.getApplicantId(),
							currentStage);
					fetchAndRefresh();

					row[0] = DocumentLogic.getInstance().getApplicantCurrentDocumentId();
					row[1] = documentNameTxt.getText().toString();
					row[2] = getCombotypeDocument();
					row[3] = documnetLink;
					addRowTOJtable(row);

					JOptionPane.showMessageDialog(null, "Saved Successfully");

				} else
					JOptionPane.showMessageDialog(null, "Please fill all fields", "ERROR", JOptionPane.ERROR_MESSAGE);

			}
		});

		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnSound.clickSound();
			     int i = table.getSelectedRow();
			        if (i >= 0) {
			            int docId = (int) model.getValueAt(i, 0); // Ensure docId is correctly retrieved as int
			            fetchAndRefresh();

			            ApplicantDocument tempDoc = documents.get(docId);
			            String filePath = tempDoc.getFolderlink();
			            documnetLink = filePath;
			            int currentStageEdit = ApplicantLogic.getInstance().getApplicantCurrentStage(currentUser);

			            Boolean editDoc = DocumentLogic.getInstance().editDocument(
			                docId,
			                documentNameTxt.getText(),
			                (String) documentTypeComboBox.getSelectedItem(),
			                documnetLink,
			                currentUser.getApplicantId(),
			                currentStageEdit
			            );

		                fetchAndRefresh();

			            if (editDoc) { // Check if editDocument was successful
			            	
			                model.setValueAt(docId, i, 0);
			                model.setValueAt(documentNameTxt.getText(), i, 1);
			                model.setValueAt((String) documentTypeComboBox.getSelectedItem(), i, 2);
			                model.setValueAt(documnetLink, i, 3);

			                JOptionPane.showMessageDialog(null, "Edited Successfully");
			            } else {
			                JOptionPane.showMessageDialog(null, "Failed to Edit Document", "ERROR", JOptionPane.ERROR_MESSAGE);
			            }
			        } else {
			            JOptionPane.showMessageDialog(null, "Please Select A Row First", "ERROR", JOptionPane.ERROR_MESSAGE);
			        }
			}



		});

		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSound.clickSound();
				int i = table.getSelectedRow();
				if (i >= 0) {
					int docId = (int) model.getValueAt(i, 0);
					boolean removed = DocumentLogic.getInstance().removeDocument(docId);
					fetchAndRefresh();
					model.removeRow(i);



					JOptionPane.showMessageDialog(null, "Deleted Successfully");
				} else

					JOptionPane.showMessageDialog(null, "Please Select A Row First", "ERROR",
							JOptionPane.ERROR_MESSAGE);

			}
		});

	}

	private void initComponents() {
		// TODO Auto-generated method stub

		// Set the layout of AttachDocumentsPnl to GridBagLayout
		setLayout(new GridLayout());

		
		btnSound = new SoundPlayer(this.getClass().getResource("/boundary/sound/click.wav"));
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;

		// Initialize and configure infoPnl
		infoPnl = new JPanel(new GridBagLayout());

		gbcInfoPnl = new GridBagConstraints();
		gbcInfoPnl.insets = new Insets(5, 5, 5, 5);

		infoPnl.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Document Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		infoPnl.setBackground(Color.LIGHT_GRAY);

		applicantInfo = new JPanel(new GridBagLayout());
		applicantInfo.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"applicant Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		applicantInfo.setBackground(new Color(173, 216, 230));

		gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(10, 10, 10, 10);

		// Title

		titleLbl = new JLabel("Document Details :");
		titleLbl.setFont(new Font("Serif", Font.BOLD, 18));

		gbcInfoPnl.gridx = 0;
		gbcInfoPnl.gridy = 0;
		gbcInfoPnl.gridwidth = GridBagConstraints.REMAINDER; // Span to the end
		gbcInfoPnl.fill = GridBagConstraints.BOTH;
		gbcInfoPnl.weightx = 1;
		gbcInfoPnl.weighty = 0;

		infoPnl.add(applicantInfo, gbcInfoPnl);

		gbcInfoPnl.gridx = 0;
		gbcInfoPnl.gridy = 1;
		gbcInfoPnl.anchor = GridBagConstraints.LINE_END; // Aligns to the right
		gbcInfoPnl.gridwidth = GridBagConstraints.REMAINDER; // Span to the end
		infoPnl.add(titleLbl, gbcInfoPnl);

		// text
		applicantStage=ApplicantLogic.getInstance().getApplicantCurrentStage(currentUser);
		

		applicantIdTxt = new JTextField();
		applicantIdTxt.setEditable(false);
		applicantIdTxt.setPreferredSize(new Dimension(50, 25));
		applicantIdTxt.setText(currentUser.getApplicantId());

		
		
		stageIdTxt = new JTextField();
		stageIdTxt.setEditable(false);
		stageIdTxt.setText(""+applicantStage);
		stageIdTxt.setPreferredSize(new Dimension(50, 25));

		documentNameTxt = new JTextField();
		documentNameTxt.setPreferredSize(new Dimension(200, 25));

		// comboBox
		documentTypeComboBox = createComboBox();

		// labels:
		documentNameLbl = new JLabel("Enter Document name:");
		documentNameLbl.setFont(new Font("Serif", Font.BOLD, 14));


		gbcInfoPnl.gridx = 0;
		gbcInfoPnl.gridy = 2;
		gbcInfoPnl.anchor = GridBagConstraints.LINE_END;
		gbcInfoPnl.gridwidth = 1;
		infoPnl.add(documentNameLbl, gbcInfoPnl);

		gbcInfoPnl.gridx = 1;
		gbcInfoPnl.anchor = GridBagConstraints.LINE_START;
		gbcInfoPnl.gridwidth = GridBagConstraints.REMAINDER;
		infoPnl.add(documentNameTxt, gbcInfoPnl);

		documentTypeLbl = new JLabel("Enter Document type:");
		documentTypeLbl.setFont(new Font("Serif", Font.BOLD, 14));

		gbcInfoPnl.gridx = 0;
		gbcInfoPnl.gridy = 3;
		gbcInfoPnl.anchor = GridBagConstraints.LINE_END;
		gbcInfoPnl.gridwidth = 1;
		infoPnl.add(documentTypeLbl, gbcInfoPnl);

		gbcInfoPnl.gridx = 1;
		gbcInfoPnl.anchor = GridBagConstraints.LINE_START;
		gbcInfoPnl.gridwidth = GridBagConstraints.REMAINDER;
		infoPnl.add(documentTypeComboBox, gbcInfoPnl);

		applicantStageLbl = new JLabel("Applicant Stage:");
		applicantStageLbl.setFont(new Font("Serif", Font.BOLD, 18));

		applicantIdLbl = new JLabel("Applicant Id:");
		applicantIdLbl.setFont(new Font("Serif", Font.BOLD, 18));

		gbc2.gridx = 0;
		gbc2.gridy = 0;
		gbc2.gridwidth = 1;
		gbc2.anchor = GridBagConstraints.LINE_END;

		applicantInfo.add(applicantIdLbl, gbc2);

		gbc2.gridx = 1;
		gbc2.anchor = GridBagConstraints.LINE_START;
		gbc2.gridwidth = GridBagConstraints.REMAINDER;

		applicantInfo.add(applicantIdTxt, gbc2);

		// stage
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 1;
		gbc2.anchor = GridBagConstraints.LINE_END;
		applicantInfo.add(applicantStageLbl, gbc2);

		gbc2.gridx = 1;
		gbc2.anchor = GridBagConstraints.LINE_START;
		gbc2.gridwidth = GridBagConstraints.REMAINDER;

		applicantInfo.add(stageIdTxt, gbc2);

		/* addBtn, editBtn, deleteBtn */
		this.addBtn = new JButton();
		addBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/AddBtn.png")));
		addBtn.setPreferredSize(new Dimension(90, 45));

		this.editBtn = new JButton();
		editBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/editBtn.png")));
		editBtn.setPreferredSize(new Dimension(90, 45));

		this.deleteBtn = new JButton();
		deleteBtn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/DeleteBtn.png")));
		deleteBtn.setPreferredSize(new Dimension(100, 45));

		btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1)); // Add a black border with 1-pixel width
		btnPanel.setBackground(Color.gray);
		// btnPanel.setPreferredSize(new Dimension(10, 60)); // Set a preferred size for
		// the panel
		btnPanel.add(addBtn);
		btnPanel.add(editBtn);
		btnPanel.add(deleteBtn);

		// add btn panel to document panel
		gbcInfoPnl.gridx = 0;
		gbcInfoPnl.gridy = 4;
		gbcInfoPnl.gridwidth = GridBagConstraints.REMAINDER;
		gbcInfoPnl.anchor = GridBagConstraints.LINE_END;
		infoPnl.add(btnPanel, gbcInfoPnl);

		add(infoPnl);

		// Initialize and configure tablePnl
		tablePnl = new JPanel(new GridBagLayout());
		scrollPane = new JScrollPane();
		table = new JTable();
		Object[] columns = { "Document Id", "Document Name", "Document Type", "Document Link" };
		row = new Object[4];

		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);

		Font font = new Font("", 1, 22);
		table.setFont(font);
		table.setRowHeight(40);

		// resets the jtable
		resetJtable();
		fetchAndRefresh();

		resetJtable();
		
		GridBagConstraints gbcTable = new GridBagConstraints();
		gbcTable.gridx = 0; // Grid x position
		gbcTable.gridy = 0; // Grid y position
		gbcTable.fill = GridBagConstraints.BOTH; // Stretch to fill space
		gbcTable.weightx = 1.0; // Allocate extra space horizontally
		gbcTable.weighty = 1.0; // Allocate extra space vertically (optional)
		gbcTable.insets = new Insets(10, 10, 10, 10); // Padding around the table

		

		scrollPane.setViewportView(table);
		// Add the scrollPane to the tablePnl with GridBagConstraints
				tablePnl.add(scrollPane, gbcTable);

		tablePnl.setBackground(Color.DARK_GRAY);
		add(tablePnl);

	}

	private void fetchAndRefresh() {
		// TODO Auto-generated method stub
		documents = DocumentLogic.getInstance().getDocuments();

		applicants = ApplicantLogic.getInstance().getApplicants();

	}

	public JComboBox<String> createComboBox() {
		DocumentType[] documentTypes = DocumentType.values();
		String[] documentTypeStrings = new String[documentTypes.length];

		for (int i = 0; i < documentTypes.length; i++) {
		    documentTypeStrings[i] = documentTypes[i].toString();
		
		}
		
		return new JComboBox<>(documentTypeStrings);
	}

	// checks if all Fields are entered
	public Boolean areAllFieldsFilled() {
		Boolean fieldsFilled = false;

		fieldsFilled = !documentNameTxt.getText().isEmpty() && getCombotypeDocument() != null
				&& !documnetLink.isEmpty();

		return fieldsFilled;
	}

	public String getCombotypeDocument() {
		return (String) documentTypeComboBox.getSelectedItem();
	}

	// adds row to the table
	public void addRowTOJtable(Object[] row) {
		model.addRow(row);
	}

	// reset the table
	public void resetJtable() {
		 // Clear all existing rows
	    model.setRowCount(0);

	    // Iterate over the updated list of documents
	    for (ApplicantDocument d : currentUser.getDocuments()) {
	        // Create a new row array with the correct number of columns
	        Object[] row = new Object[4];
	        row[0] = d.getDocumentId();
	        row[1] = d.getDocumentName();
	        row[2] = d.getType().toString();
	        row[3] = d.getFolderlink();

	        // Add the new row to the table model
	        model.addRow(row);
	    }
	}
	
	/*public static void main(String[] args) {
		// Create a frame to hold the panel
		JFrame frame = new JFrame("Update Applicant Status");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);

		// Add the AttachDocumentsPnl to the frame
		AttachDocumentsPnl panel = new AttachDocumentsPnl("1");
		frame.add(panel);

		// Make the frame visible
		frame.setVisible(true);
	}*/


//new Color(230, 230, 250)
}
