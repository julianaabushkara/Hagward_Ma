package control;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.ApplicantStatus;
import entity.Consts;
import entity.ReportDetails;

public class PdfPeriodicalAcceptanceReportLogic {

	
	//returns hashmap<institution,HashMap<status,reportdetails>>
	public HashMap<String, HashMap<String, ReportDetails>> getPeriodicalAcceptanceReportDetails(Date startDate,
			Date endDate) {
		ArrayList<ReportDetails> results = new ArrayList<>();

		// hashmap<institution,HashMap<status,reportdetails>>
		HashMap<String, HashMap<String, ReportDetails>> inst = new HashMap<>();

		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			// Format dates for SQL query
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String startDateStr = dateFormat.format(startDate);
			String endDateStr = dateFormat.format(endDate);

			// Directly adding the date range into the SQL query string

			String query = "SELECT " + "    i.InstitutionName, " + "    a.Status, "
					+ "    COUNT(a.ApplicantId) * 100.0 / total.total_applicants AS percentage " + "FROM "
					+ "    TblInstitution i " + "JOIN "
					+ "    TblDepartment d ON i.InstitutionName = d.InstitutionName " + "JOIN "
					+ "    TblBackgroundEducation be ON d.InstitutionName = be.InstitutionName AND d.DepartmentName = be.DepartmentName "
					+ "JOIN " + "    TblApplicant a ON be.ApplicantId = a.ApplicantId " + "JOIN " + "    (SELECT "
					+ "        i.InstitutionName, " + "        COUNT(a.ApplicantId) AS total_applicants " + "     FROM "
					+ "        TblInstitution i " + "     JOIN "
					+ "        TblDepartment d ON i.InstitutionName = d.InstitutionName " + "     JOIN "
					+ "        TblBackgroundEducation be ON d.InstitutionName = be.InstitutionName AND d.DepartmentName = be.DepartmentName "
					+ "     JOIN " + "        TblApplicant a ON be.ApplicantId = a.ApplicantId " + "     WHERE "
					+ "        a.DateSubmitted BETWEEN '" + startDateStr + "' AND '" + endDateStr + "' "
					+ "     GROUP BY " + "        i.InstitutionName "
					+ "    ) total ON i.InstitutionName = total.InstitutionName " + "WHERE "
					+ "    a.DateSubmitted BETWEEN '" + startDateStr + "' AND '" + endDateStr + "' " + "GROUP BY "
					+ "    i.InstitutionName, " + "    a.Status, " + "    total.total_applicants " + "ORDER BY "
					+ "    i.InstitutionName, " + "    a.Status;";

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(query)) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						String institutionName = rs.getString("InstitutionName");
						String status = rs.getString("Status");
						double percentage = rs.getDouble("percentage");
						results.add(new ReportDetails(institutionName, status, percentage));
					}
				}

				// Print results for debugging
				for (ReportDetails r : results) {
					// if the inner map is empty
					if (!inst.containsKey(r.getInstitutionName())) {
						HashMap<String, ReportDetails> innerMap = new HashMap<>();
						innerMap.put(r.getStatus(), r);
						inst.put(r.getInstitutionName(), innerMap);
					}
					// if not empty
					else {
						// If it exists, get the inner map and update it
						HashMap<String, ReportDetails> innerMap = inst.get(r.getInstitutionName());
						innerMap.put(r.getStatus(), r);
						inst.put(r.getInstitutionName(), innerMap);

					}

				}

				

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return inst;
	}

	
	
	//generates the PDF
	public void GenerateReportPdf(Date startDate, Date endDate) {
		// Path to the output PDF file
		String dest = "PeriodicalAcceptanceReport.pdf";

		// Create a new Document object
		Document document = new Document();

		try {
			// Initialize PdfWriter for the document
			PdfWriter.getInstance(document, new FileOutputStream(dest));

			// Open the document
			document.open();

			// Add a title to the document
			Paragraph title = new Paragraph("Periodical Acceptance Report",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.RED));
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			
			//logo
			Image logo = Image.getInstance(this.getClass().getResource("/boundary/images/logo.png"));
            logo.scaleToFit(120, 100);  
            
            // Calculate coordinates for top right corner
            float x = PageSize.A4.getWidth() - logo.getScaledWidth();
            float y = PageSize.A4.getHeight() - logo.getScaledHeight();

            logo.setAbsolutePosition(x, y);
			document.add(logo);


	
			//formats the date util
			Date generateDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = dateFormat.format(generateDate);
			
			//adds when was the report generated
			document.add(new Paragraph("Report Generated In : " + formattedDate));
			
			// adds separator
			Paragraph Seperater = new Paragraph(
					"--------------------------------------------------------------------------------------------------------------------------");
			document.add(Seperater);

			// Add a blank line
			document.add(new Paragraph(" "));

			// Create a table with fixed column widths

			float[] columnW = { 700f, 400f, 550f, 400f, 400f, 400f, 400f };
			PdfPTable table = new PdfPTable(columnW);
			table.setWidthPercentage(100);

			// Add headers to the table
			PdfPCell institutionName = new PdfPCell(new Paragraph("Institution Name"));
			institutionName.setHorizontalAlignment(Element.ALIGN_CENTER);
			institutionName.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(institutionName);

			// Add headers to the table
			PdfPCell accepted = new PdfPCell(new Paragraph("Accepted"));
			accepted.setHorizontalAlignment(Element.ALIGN_CENTER);
			accepted.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(accepted);

			// Add headers to the table
			PdfPCell conditionallyAccepted = new PdfPCell(new Paragraph("Conditionally Accepted"));
			conditionallyAccepted.setHorizontalAlignment(Element.ALIGN_CENTER);
			conditionallyAccepted.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(conditionallyAccepted);

			// Add headers to the table
			PdfPCell succeededConditions = new PdfPCell(new Paragraph("Succeeded Conditions"));
			succeededConditions.setHorizontalAlignment(Element.ALIGN_CENTER);
			succeededConditions.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(succeededConditions);

			// Add headers to the table
			PdfPCell failedConditions = new PdfPCell(new Paragraph("Failed Conditions"));
			failedConditions.setHorizontalAlignment(Element.ALIGN_CENTER);
			failedConditions.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(failedConditions);

			// Add headers to the table
			PdfPCell rejectedAfterInterview = new PdfPCell(new Paragraph("Rejected After Interview"));
			rejectedAfterInterview.setHorizontalAlignment(Element.ALIGN_CENTER);
			rejectedAfterInterview.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(rejectedAfterInterview);

			// Add headers to the table
			PdfPCell rejectedWithoutInterview = new PdfPCell(new Paragraph("Rejected Without Interview"));
			rejectedWithoutInterview.setHorizontalAlignment(Element.ALIGN_CENTER);
			rejectedWithoutInterview.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(rejectedWithoutInterview);

			HashMap<String, HashMap<String, ReportDetails>> reportRow = getPeriodicalAcceptanceReportDetails(startDate,
					endDate);

			// accepted,succeeded_conditions,failed_conditions,rejected_after_interview,rejected_without_interview,waiting_initial_response,
			// Invite_interview,Waiting_List,conditionally_accepted

			for (String institution : reportRow.keySet()) {
				HashMap<String, ReportDetails> innerMap = reportRow.get(institution);
				double perAccepted = 0.0, perConditionallyAccepted = 0.0, perFailedConditions = 0.0,
						perRejectedAfterInterview = 0.0, perRejectedWithoutInterview = 0.0,
						perSucceededConditions = 0.0;
				for (String report : innerMap.keySet()) {
					if (report.equals(ApplicantStatus.accepted.toString())) {
						perAccepted = innerMap.get(report).getPercentage();
					}

					if (report.equals(ApplicantStatus.succeeded_conditions.toString())) {
						perSucceededConditions = innerMap.get(report).getPercentage();
					}

					if (report.equals(ApplicantStatus.failed_conditions.toString())) {
						perFailedConditions = innerMap.get(report).getPercentage();
					}

					if (report.equals(ApplicantStatus.rejected_after_interview.toString())) {
						perRejectedAfterInterview = innerMap.get(report).getPercentage();
					}

					if (report.equals(ApplicantStatus.rejected_without_interview.toString())) {
						perRejectedWithoutInterview = innerMap.get(report).getPercentage();
					}

					if (report.equals(ApplicantStatus.conditionally_accepted.toString())) {
						perConditionallyAccepted = innerMap.get(report).getPercentage();
					}

				}
				// add row to table pdf
				table.addCell(institution);
				table.addCell(String.valueOf(perAccepted)+"%");
				table.addCell(String.valueOf(perConditionallyAccepted)+"%");
				table.addCell(String.valueOf(perSucceededConditions)+"%");
				table.addCell(String.valueOf(perFailedConditions)+"%");
				table.addCell(String.valueOf(perRejectedAfterInterview)+"%");
				table.addCell(String.valueOf(perRejectedWithoutInterview)+"%");
			}

			// Add the table to the document
			document.add(table);

			// Close the document
			document.close();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}

	public Boolean openPdfInViewer(String pdfFilePath) {
	    if (Desktop.isDesktopSupported()) {
	        try {
	            File pdfFile = new File(pdfFilePath);
	            Desktop.getDesktop().open(pdfFile);
	            return true;
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            return false;
	        }
	    } else {
	    	return false;
	    }
	}

	
}
