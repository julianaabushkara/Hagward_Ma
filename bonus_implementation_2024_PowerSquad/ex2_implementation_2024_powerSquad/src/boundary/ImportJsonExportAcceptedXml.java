package boundary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import control.ApplicantLogic;
import control.ExportStudentsInfoXml;
import control.GradesLogic;
import entity.Applicant;

/**/
public class ImportJsonExportAcceptedXml extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton generateBtn, importBtn;
	private List<Applicant> expelled;

	/**
	 * Create the panel.
	 */
	public ImportJsonExportAcceptedXml() {
		fetchAndRefresh();
		initComponents();
		createEvents();

	}

	private void initComponents() {
		// TODO Auto-generated method stub
		this.generateBtn = new JButton("Generate Xml File");
		// Resize the button
		generateBtn.setPreferredSize(new Dimension(200, 50)); // width, height

		this.importBtn = new JButton("import grades Json File");
		// Resize the button
		importBtn.setPreferredSize(new Dimension(200, 50)); // width, height

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);

		// Third row: Generate Report button
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 15, 15, 15);
		gbc.anchor = GridBagConstraints.CENTER;
		add(generateBtn, gbc);

		gbc.gridx = 2;
		add(importBtn, gbc);

	}

	private void fetchAndRefresh() {
		// TODO Auto-generated method stub

	}

	private void createEvents() {
		// TODO Auto-generated method stub
		generateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ExportStudentsInfoXml.getInstance().exportStudentsToXML();

			}
		});

		importBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				GradesLogic.getInstance().importGradesFromJSON("resources/ApplicantsGrades.json");
				expelled= GradesLogic.getInstance().checkCondition();
				
				if (!expelled.isEmpty() )		
				for(Applicant ex :expelled)
				{
					Boolean succ=ApplicantLogic.getInstance().removeApplicant(ex.getApplicantId());
					//System.out.println(ex);
					
				}
			}
		});
	}

	public static void main(String[] args) {
		// Create a frame to hold the panel
		JFrame frame = new JFrame("Attach Documents Panel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);

		// Add the AttachDocumentsPnl to the frame
		ImportJsonExportAcceptedXml panel = new ImportJsonExportAcceptedXml();
		// CustomerLogic.getInstance().exportCustomersToJSON();
		// CustomerLogic.getInstance().importCustomersFromJSON("json/customers.json");
		frame.add(panel);

		// Make the frame visible
		frame.setVisible(true);
	}

}
