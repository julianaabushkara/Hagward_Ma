package control;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;

import entity.Consts;
import entity.ApplicantDocument;

public class DocumentLogic {

	private static DocumentLogic instance;

	private DocumentLogic() {
	}

	public static DocumentLogic getInstance() {
		if (instance == null)
			instance = new DocumentLogic();
		return instance;
	}

	//////////////////////////////////////////////////////////////////
	// READ document FROM DB - Document(int documentId, String documentName, String
	////////////////////////////////////////////////////////////////// type, String
	////////////////////////////////////////////////////////////////// folderlink,
	////////////////////////////////////////////////////////////////// String
	////////////////////////////////////////////////////////////////// applicantId,String
	////////////////////////////////////////////////////////////////// stageID)
	//////////////////////////////////////////////////////////////////
	public HashMap<Integer, ApplicantDocument> getDocuments() {
		HashMap<Integer, ApplicantDocument> documents = new HashMap<Integer, ApplicantDocument>();
		ArrayList<ApplicantDocument> results = new ArrayList<ApplicantDocument>();

		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_DOCUMENT);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					results.add(new ApplicantDocument(rs.getInt(i++), rs.getString(i++), rs.getString(i++),
							rs.getString(i++), rs.getString(i++), rs.getInt(i++)));
				}
				for (ApplicantDocument d : results) {
					documents.put(d.getDocumentId(), d);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return documents;
	}



	public boolean addDocument(String documentName, String type, String link, String applicantId, int stageId) {
		 final int MAX_LENGTH = 49; // Example length limit, adjust as needed
		 link = truncateIfNeeded(link, MAX_LENGTH);
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_DOCUMENT)) {

				int i = 1;
				
				stmt.setString(i++, documentName); // can't be null
				stmt.setString(i++, type); // can't be null
				stmt.setString(i++, link); // can't be null
				stmt.setString(i++, applicantId); // can't be null
				stmt.setInt(i++, stageId); // can't be null

				stmt.executeUpdate();
				return true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Delete the selected employee in form. return true if the deletion was
	 * successful, else - return false
	 * 
	 * @param employeeID - the employee to delete from DB
	 * @return
	 */
	public boolean removeDocument(int documentId) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_DEL_DOCUMENT)) {

				stmt.setInt(1, documentId);
				stmt.executeUpdate();
				return true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Editing a exist employee with the parameters received from the form. return
	 * true if the update was successful, else - return false
	 * 
	 * @return
	 */

///////////////////////////////////////////////////////////////////////////////////////////////////////////	

///////////////////////////////////// returns the current stage of applicant
////////////////////////////////////////////////////////////////////////////////////////////////////
	public int getApplicantCurrentDocumentId() {
		int currentDocumentId = 0;
		HashMap<Integer, ApplicantDocument> docs = getDocuments();


		for (ApplicantDocument d : docs.values()) {


			if (currentDocumentId < d.getDocumentId())
				currentDocumentId = d.getDocumentId();

		}

		return currentDocumentId;
	}

	public boolean editDocument(int documentId, String documentName, String type, String link, String applicantId,
			int stageId) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_DOCUMENT)) {
				int i = 1;

				stmt.setString(i++, documentName); // can't be null
				stmt.setString(i++, type); // can't be null
				stmt.setString(i++, link); // can't be null
				stmt.setString(i++, applicantId); // can't be null

				stmt.setInt(i++, stageId); // can't be null

				stmt.setInt(i++, documentId);

				stmt.executeUpdate();
				return true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String selectDocument() {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile.getAbsolutePath();

		}
		return null;
	}
	
	
	private String truncateIfNeeded(String value, int maxLength) {
	    return value != null && value.length() > maxLength ? value.substring(0, maxLength) : value;
	}
	
}
