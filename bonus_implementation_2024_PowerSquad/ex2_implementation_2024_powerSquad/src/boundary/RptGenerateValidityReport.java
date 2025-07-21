package boundary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import control.PdfValidityReportLogic;

public class RptGenerateValidityReport extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton generateReportBtn;
	private PdfValidityReportLogic pdfGenerator;

	/**
	 * Create the panel.
	 */
	public RptGenerateValidityReport() {

		fetchAndRefresh();
		initComponents();
		createEvents();

	}

	private void createEvents() {
		// TODO Auto-generated method stub
		generateReportBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfGenerator.GeneratevalidityReportPdf();
				// Optionally, open the PDF after generation
				if (pdfGenerator.openPdfInViewer("validityReport.pdf")) {
					JOptionPane.showMessageDialog(RptGenerateValidityReport.this, "opened Pdf.", "Success",
							JOptionPane.PLAIN_MESSAGE);
				} else
					JOptionPane.showMessageDialog(RptGenerateValidityReport.this,
							"Desktop is not supported to open PDF.", "Error", JOptionPane.ERROR_MESSAGE);

			}
		});
	}

	private void initComponents() {
		// TODO Auto-generated method stub
		this.generateReportBtn = new JButton("Generate Report");
		// Resize the button
		generateReportBtn.setPreferredSize(new Dimension(200, 50)); // width, height

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);

		// Third row: Generate Report button
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 15, 15, 15);
		gbc.anchor = GridBagConstraints.CENTER;
		add(generateReportBtn, gbc);

	}

	private void fetchAndRefresh() {
		// TODO Auto-generated method stub
		pdfGenerator = new PdfValidityReportLogic();

	}

}
