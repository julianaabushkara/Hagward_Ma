package boundary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.toedter.calendar.JDateChooser;

import control.PdfPeriodicalAcceptanceReportLogic;

public class RptGeneratePeriodicalAcceptance extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton generateReportBtn;
	private JDateChooser start;
	private JDateChooser end;
	private JLabel startDate;
	private JLabel lastDate;
	private PdfPeriodicalAcceptanceReportLogic pdfGenerator;

	public RptGeneratePeriodicalAcceptance() {

		fetchAndRefresh();
		initComponents();
		createEvents();

	}

	private void createEvents() {

		generateReportBtn.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        if (areAllFieldsFilled(start, end)) {
		            Date startD = start.getDate();
		            Date endD = end.getDate();

		            //	        

		            if (checkDate(startD, endD)) {
		                pdfGenerator.GenerateReportPdf(startD, endD);
		                // Optionally, open the PDF after generation
		                if(pdfGenerator.openPdfInViewer("PeriodicalAcceptanceReport.pdf"))
		                {
		                	JOptionPane.showMessageDialog(RptGeneratePeriodicalAcceptance.this, "opened Pdf.", "Success", JOptionPane.PLAIN_MESSAGE);
		                }else 
		                	JOptionPane.showMessageDialog(RptGeneratePeriodicalAcceptance.this, "Desktop is not supported to open PDF.", "Error", JOptionPane.ERROR_MESSAGE);
		            } else {
		                JOptionPane.showMessageDialog(RptGeneratePeriodicalAcceptance.this,
		                        "Invalid Dates. Please select valid dates. The start Date must be before the End Date.",
		                        "Error", JOptionPane.WARNING_MESSAGE);
		            }
		        } else {
		            JOptionPane.showMessageDialog(RptGeneratePeriodicalAcceptance.this,
		                    "Please Fill the Required Dates", "", JOptionPane.WARNING_MESSAGE);
		        }
		    }
		});
	}

	private void initComponents() {

		this.generateReportBtn = new JButton("Generate Report");
		this.start = new JDateChooser();
		this.end = new JDateChooser();
		this.startDate = new JLabel("Start Date:");
		this.lastDate = new JLabel("Last Date:");
		// Resize the button
		generateReportBtn.setPreferredSize(new Dimension(200, 50)); // width, height

		start.setPreferredSize(new Dimension(200, 25));
		end.setPreferredSize(new Dimension(200, 25));

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		add(startDate, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(start, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		add(lastDate, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(end, gbc);

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
		pdfGenerator = new PdfPeriodicalAcceptanceReportLogic();

	}

// returns true if the check in day equals or smaller than today else fdalse
	public Boolean checkDate(Date start, Date End) {
		Date currentDate = new Date();
		if (currentDate.after(End) || currentDate.equals(currentDate)) {
			if (start.before(End) || start.equals(End))
				return true;
		}

		return false;

	}

	// checks if all Fields are entered
	public Boolean areAllFieldsFilled(JDateChooser s, JDateChooser e) {
		Boolean fieldsFilled = false;
		Date chosenDate = s.getDate();
		Date chosenDate2 = e.getDate();

		

		if (chosenDate == null || chosenDate2 == null)
			return false;

		fieldsFilled = !e.getDate().equals(null) && !s.getDate().equals(null);

		return fieldsFilled;
	}

	
}
