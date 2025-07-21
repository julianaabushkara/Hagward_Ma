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
import entity.Consts;
import entity.ApplicantDocument;
import entity.ValidityReportDetails;
public class PdfValidityReportLogic {
	
	public HashMap<String, ValidityReportDetails> getValidityApplicants() {
	    HashMap<String, ValidityReportDetails> applicants = new HashMap<>();
	    ArrayList<ValidityReportDetails> results = new ArrayList<>();

	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
	             PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_VALIDITY_REPORT);
	             ResultSet rs = stmt.executeQuery()) {

	            while (rs.next()) {
	                int i = 1;
	                results.add(new ValidityReportDetails(
	                    rs.getString(i++),  // ApplicantId
	                    rs.getString(i++),  // FirstName
	                    rs.getString(i++),  // LastName
	                    rs.getString(i++),  // EmailAddress
	                    rs.getString(i++),  // PhoneNumber
	                    rs.getBoolean(i++)  // Valid
	                ));
	            }

	            // Fills in all the diplomas of every applicant
	            for (ValidityReportDetails applicant : results) {
	                ArrayList<ApplicantDocument> docs = getDocumentsByNewlyApplicanId(applicant.getApplicantId());

	                // Adds every document to the arraylist in the applicant
	                for (ApplicantDocument doc : docs) {
	                    applicant.addDocuments(doc);

	                    switch (doc.getType()) {
	                        case "certificate":
	                            applicant.setCertificate(true);
	                            break;
	                        case "resume":
	                            applicant.setResume(true);
	                            break;
	                        case "recommendation":
	                            applicant.setRecommendation(true);
	                            break;
	                        case "transcript":
	                            applicant.setTranscript(true);
	                            break;
	                    }
	                }

	                // Inserts every applicant in a hashmap with key -> ID
	                applicants.put(applicant.getApplicantId(), applicant);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    return applicants;
	}


	// generates the PDF
	public void GeneratevalidityReportPdf() {
		// Path to the output PDF file
		String dest = "validityReport.pdf";

		// Create a new Document object
		Document document = new Document();

		try {
			// Initialize PdfWriter for the document
			PdfWriter.getInstance(document, new FileOutputStream(dest));

			// Open the document
			document.open();

			// Add a title to the document
			Paragraph title = new Paragraph("validity Report",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, BaseColor.RED));
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			// logo
			Image logo = Image.getInstance(this.getClass().getResource("/boundary/images/logo.png"));
			logo.scaleToFit(120, 100);

			// Calculate coordinates for top right corner
			float x = PageSize.A4.getWidth() - logo.getScaledWidth();
			float y = PageSize.A4.getHeight() - logo.getScaledHeight();

			logo.setAbsolutePosition(x, y);
			document.add(logo);

			// formats the date util
			Date generateDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = dateFormat.format(generateDate);

			// adds when was the report generated
			document.add(new Paragraph("Report Generated In : " + formattedDate));

			// adds separator
			Paragraph Seperater = new Paragraph(
					"--------------------------------------------------------------------------------------------------------------------------");
			document.add(Seperater);

			// Add a blank line
			document.add(new Paragraph(" "));

			// Create a table with fixed column widths

			float[] columnW = { 600f, 600f, 650f, 600f, 600f, 600f, 600f, 600f, 600f, 600f };
			PdfPTable table = new PdfPTable(columnW);
			table.setWidthPercentage(100);

			// Add headers to the table
			PdfPCell id = new PdfPCell(new Paragraph("Applicant Id"));
			id.setHorizontalAlignment(Element.ALIGN_CENTER);
			id.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(id);

			// Add headers to the table
			PdfPCell fName = new PdfPCell(new Paragraph("First Name"));
			fName.setHorizontalAlignment(Element.ALIGN_CENTER);
			fName.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(fName);

			// Add headers to the table
			PdfPCell lName = new PdfPCell(new Paragraph("Last Name"));
			lName.setHorizontalAlignment(Element.ALIGN_CENTER);
			lName.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(lName);

			// Add headers to the table
			PdfPCell email = new PdfPCell(new Paragraph("Email Adress"));
			email.setHorizontalAlignment(Element.ALIGN_CENTER);
			email.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(email);

			// Add headers to the table
			PdfPCell phone = new PdfPCell(new Paragraph("Phone Number"));
			phone.setHorizontalAlignment(Element.ALIGN_CENTER);
			phone.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(phone);

			// Add headers to the table
			PdfPCell resume = new PdfPCell(new Paragraph("resume"));
			resume.setHorizontalAlignment(Element.ALIGN_CENTER);
			resume.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(resume);

			// Add headers to the table
			PdfPCell transcript = new PdfPCell(new Paragraph("transcript"));
			transcript.setHorizontalAlignment(Element.ALIGN_CENTER);
			transcript.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(transcript);

			// Add headers to the table
			PdfPCell certificate = new PdfPCell(new Paragraph("certificate"));
			certificate.setHorizontalAlignment(Element.ALIGN_CENTER);
			certificate.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(certificate);

			// Add headers to the table
			PdfPCell recommendation = new PdfPCell(new Paragraph("recommendation"));
			recommendation.setHorizontalAlignment(Element.ALIGN_CENTER);
			recommendation.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(recommendation);

			// Add headers to the table
			PdfPCell valid = new PdfPCell(new Paragraph("Valid"));
			valid.setHorizontalAlignment(Element.ALIGN_CENTER);
			valid.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(valid);

			HashMap<String, ValidityReportDetails> reportRow = getValidityApplicants();

			for (ValidityReportDetails v : reportRow.values()) {
				String idApp, fNameApp = "--", lNameApp = "--", emailApp = "--", phoneApp = "--", resumeApp = "X",
						certificateApp = "X", recommendationApp = "X", transcriptApp = "X", validApp = "X";
				idApp = v.getApplicantId();

				if (!v.getFirstName().isEmpty())
					fNameApp = v.getFirstName();

				if (!v.getLastName().isEmpty())
					lNameApp = v.getLastName();

				if (!v.getEmailAdress().isEmpty())
					emailApp = v.getEmailAdress();

				if (!v.getPhoneNumber().isEmpty())
					phoneApp = v.getPhoneNumber();

				if (v.getResume() == true)
					resumeApp = "V";

				if (v.getCertificate() == true)
					certificateApp = "V";

				if (v.getTranscript() == true)
					transcriptApp = "V";

				if (v.getRecommendation() == true)
					recommendationApp = "V";

				if (v.getValid() == true)
					validApp = "V";

				// add row to table pdf
				table.addCell(idApp);
				table.addCell("" + fNameApp);
				table.addCell("" + lNameApp);
				table.addCell("" + emailApp);
				table.addCell("" + phoneApp);

				table.addCell("" + resumeApp);

				table.addCell("" + transcriptApp);

				table.addCell("" + certificateApp);

				table.addCell("" + recommendationApp);
				table.addCell("" + validApp);

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

//////////////////////////////////////////////////////////////////////////////////////////
//GETS THE documents FOR EVERY applicant//SQL_DEL_ALL_DOCS_BY_APPLICANT_ID
//////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<ApplicantDocument> getDocumentsByNewlyApplicanId(String ApplicantId) {
		ArrayList<ApplicantDocument> docs = new ArrayList<>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_ALL_DOCS_BY_APPLICANT_ID)) {
				stmt.setString(1, ApplicantId); // Set the first parameter to the institution name
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						int i = 1;
						/*
						 * int documentId, String documentName, String type, String folderlink, String
						 * applicantId, String stageID
						 */
						docs.add(new ApplicantDocument(rs.getInt(i++), rs.getString(i++), rs.getString(i++),
								rs.getString(i++), rs.getString(i++), rs.getInt(i++)));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return docs;
	}

}
