package control;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import entity.Applicant;
import entity.ApplicantStatus;
import entity.BackgroundEducation;
import entity.Consts;
import entity.ApplicantDocument;
import entity.applicantStages;

public class ApplicantLogic {

	private static ApplicantLogic instance;

	private ApplicantLogic() {
	}

	public static ApplicantLogic getInstance() {
		if (instance == null)
			instance = new ApplicantLogic();
		return instance;
	}

	/*
	 * fetches all employees from db file.
	 * 
	 * @return arraylist of employees.
	 */
	/**
	 * adds an Applicant to db file. ApplicantID-string firstName-string
	 * lastName-string EmailAdress-string phone- string Status-string
	 * 
	 * @return
	 */
	public HashMap<String, Applicant> getApplicants() {
		HashMap<String, Applicant> Applicants = new HashMap<String, Applicant>();
		ArrayList<Applicant> results = new ArrayList<Applicant>();

		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_APPLICANT);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					results.add(new Applicant(rs.getString(i++), rs.getString(i++), rs.getString(i++),
							rs.getString(i++), rs.getString(i++), rs.getDate(i++), rs.getString(i++),
							rs.getBoolean(i++), rs.getInt(i++)));

				}

				// fills in all the diplomas of every applicant
				for (Applicant A : results) {
					// calls the query that select all the departments for every institution
					ArrayList<BackgroundEducation> diploma = getDiplomaByApplicanId(A.getApplicantId());

					// adds every diploma to the arraylist in applicants
					for (BackgroundEducation d : diploma) {
						A.addDiploma(d);
					}

					ArrayList<ApplicantDocument> docs = getDocumentsByApplicanId(A.getApplicantId());
					// adds every doc to the arraylist in applicants
					for (ApplicantDocument d : docs) {
						A.addDocuments(d);
					}

					// inserts every applicant in a hashmap with key -> ID
					Applicants.put(A.getApplicantId(), A);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return Applicants;

	}

	/*
	 * applicant id-string first-string last-string email-string phone-string
	 * status-string
	 * 
	 * 
	 * 
	 * 
	 */

	public boolean addApplicant(String applicantId, String firstName, String lastName, String email, String Phone) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_Applicant)) {
				int i = 1;

				stmt.setString(i++, applicantId); // can't be null

				// firstName
				if (firstName != null)
					stmt.setString(i++, firstName);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				// last name
				if (lastName != null)
					stmt.setString(i++, lastName);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				// email
				if (email != null)
					stmt.setString(i++, email);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				// phone
				if (Phone != null)
					stmt.setString(i++, Phone);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				// DAtesubmitted

				Date submittionDate = new Date();
				// Format the submittionDate to only include the date part
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = dateFormat.format(submittionDate);

				stmt.setDate(i++, java.sql.Date.valueOf(formattedDate));

				stmt.setString(i++, ApplicantStatus.waiting_initial_response.toString());// can't be null
				stmt.setBoolean(i++, false);// can't be null -
				stmt.setInt(i++, 1);// can't be null

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

// edits the applicant
	public boolean editApplicant(String applicantId, String firstName, String lastName, String email, String Phone,
			Date submittionDate, String Status, Boolean valid, int stage) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_Applicant)) {
				int i = 1;

				stmt.setString(i++, applicantId); // can't be null

				if (firstName != null)
					stmt.setString(i++, firstName);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				if (lastName != null)
					stmt.setString(i++, lastName);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				if (email != null)
					stmt.setString(i++, email);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				if (Phone != null)
					stmt.setString(i++, Phone);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				// Format the submittionDate to only include the date part
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = dateFormat.format(submittionDate);
				stmt.setDate(i++, java.sql.Date.valueOf(formattedDate));

				stmt.setString(i++, Status); // can't be null
				stmt.setBoolean(i++, valid);// can't be null

				stmt.setInt(i++, stage);// cant be null
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

	///////////////////////////////// update Status
////////////////////////////////////UPDATE STAGE FOR APPLICANT//////////////////////////////////////////////////////
	public boolean updateStatusForApplicant(String applicantId, String status) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_STATUS)) {
				int i = 1;

				stmt.setString(i++, status);// cant be null
				stmt.setString(i++, applicantId); // can't be null

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

///////////////////////////////////UPDATE validity FOR APPLICANT//////////////////////////////////////////////////////
	public boolean updateValidForApplicant(String applicantId, Boolean valid) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_VALIDITY)) {
				int i = 1;

				stmt.setBoolean(i++, valid);// cant be null
				stmt.setString(i++, applicantId); // can't be null

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

	//////////////////////////////////////////////// filter initial decision
	public List<Applicant> filterByWaitingInitialResponse() {
		HashMap<String, Applicant> apps = getApplicants();
		//updates the valid 
		for (Applicant a : apps.values()) {
			a.setValid();
			updateValidForApplicant(a.getApplicantId(),a.getValid());
		}
		
		List<Applicant> filteredApplicants = apps.values().stream()
			    .filter(applicant -> ApplicantStatus.waiting_initial_response.toString().equals(applicant.getStatus()))
			    .filter(applicant -> applicant.getValid()) 
			    .collect(Collectors.toList());
		
		return filteredApplicants;

	}
	
	
	//////////////////////////////////////////////// filter AFTER INTERVIEW
	public List<Applicant> filterByAfterInterview() {
		HashMap<String, Applicant> apps = getApplicants();
		List<Applicant> filteredApplicants = apps.values().stream()
			    .filter(applicant -> ApplicantStatus.after_Interview.toString().equals(applicant.getStatus())) 
			    .collect(Collectors.toList());
		
		return filteredApplicants;

	}
	
	
	
	//////////////////////////////////////////////// filter Final decision
	public List<Applicant> filterByFinalDesicion() {
		HashMap<String, Applicant> apps = getApplicants();
		List<Applicant> filteredApplicants = apps.values().stream()
			    .filter(applicant -> ApplicantStatus.Waiting_List.toString().equals(applicant.getStatus())) 
			    .collect(Collectors.toList());
		
		return filteredApplicants;

	}

	// removes the applicant========== applicant id ->1
	public boolean removeApplicant(String applicantId) {
		/////////////////////// deletes applicant's documents///////////////////
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt1 = conn.prepareCall(Consts.SQL_DEL_Documents_By_Applicant_Id);) {
				stmt1.setString(1, applicantId);
				stmt1.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		
			/////////////////////// deletes gotGrades ///////////////////
			try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

				try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
						CallableStatement stmt1 = conn.prepareCall(Consts.SQL_DELETE_APPLICANT_GOT_GRADE);) {
					stmt1.setString(1, applicantId);
					stmt1.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
		
		
		
		
		/////////////////////// deletes applicant's stage///////////////////
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt1 = conn.prepareCall(Consts.SQL_DEL_Applicant_from_stage);) {
				stmt1.setString(1, applicantId);
				stmt1.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		/////////////////////// deletes applicant's passing grade///////////////////
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt1 = conn.prepareCall(Consts.SQL_DEL_PassingGrade_Of_Applicant);) {
				stmt1.setString(1, applicantId);
				stmt1.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// deletes every diploma /background education
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt1 = conn.prepareCall(Consts.SQL_DEL_BACKGROUND_EDUCATION_By_Applicant);) {
				stmt1.setString(1, applicantId);
				stmt1.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		/////////////////////// deletes applicant///////////////////
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_DEL_Applicant)) {
				stmt.setString(1, applicantId);
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////	
///////////////////////////////////// checks if the applicant already exists
////////////////////////////////////////////////////////////////////////////////////////////////////
	public Boolean applicantAlreadyExists(String applicantId) {
		Boolean fieldsValid = false;
		HashMap<String, Applicant> ap = getApplicants();
		fieldsValid = ap.containsKey(applicantId);
		return fieldsValid;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////
	// READ Background Education FROM DB - Department name/institution Name/
	//////////////////////////////////////////////////////////////////////////////////////////// phonenumber
	////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	// HASHMAP KEY ID+INSTITUTION+DEPARTMMENT
	public HashMap<String, BackgroundEducation> getBackgroundEducations() {
		HashMap<String, BackgroundEducation> education = new HashMap<String, BackgroundEducation>();
		ArrayList<BackgroundEducation> results = new ArrayList<BackgroundEducation>();

		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_BACKGROUND_EDUCATION);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					results.add(new BackgroundEducation(rs.getString(i++), rs.getString(i++), rs.getString(i++),
							rs.getDate(i++), rs.getInt(i++), rs.getInt(i++)));
				}

				for (BackgroundEducation B : results) {

					education.put(B.getInstutionName().toUpperCase() + B.getDepartmentname().toUpperCase()
							+ B.getApplicantId().toUpperCase(), B);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return education;
	}

	// adds a new background education
	public boolean addBackgroundEducations(String institutionName, String departmentName, String applicantID,
			Date graduationDate, int creditPoints, double averageGrade) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_BACKGROUND_EDUCATION)) {
				int i = 1;

				stmt.setString(i++, institutionName); // can't be null
				stmt.setString(i++, departmentName); // can't be null
				stmt.setString(i++, applicantID); // can't be null

				if (graduationDate != null) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String formattedDate = dateFormat.format(graduationDate);
					stmt.setDate(i++, java.sql.Date.valueOf(formattedDate));
				} else
					stmt.setNull(i++, java.sql.Types.DATE);

				if (creditPoints > 0)
					stmt.setInt(i++, creditPoints);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				if (averageGrade > 0)
					stmt.setDouble(i++, averageGrade);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

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

	//////////// edits the background education
	public boolean editBackgroundEducations(String institutionName, String departmentName, String applicantID,
			Date graduationDate, int creditPoints, double averageGrade) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_BACKGROUND_EDUCATION)) {
				int i = 1;

				stmt.setString(i++, institutionName); // can't be null
				stmt.setString(i++, departmentName); // can't be null
				stmt.setString(i++, applicantID); // can't be null

				if (graduationDate != null) {
					// Format the graduationDate to only include the date part
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String formattedDate = dateFormat.format(graduationDate);
					stmt.setDate(i++, java.sql.Date.valueOf(formattedDate));
				}
				// stmt.setDate(i++, new java.sql.Date(graduationDate.getTime()));
				else
					stmt.setNull(i++, java.sql.Types.DATE);

				if (creditPoints > 0)
					stmt.setInt(i++, creditPoints);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

				if (averageGrade > 0)
					stmt.setDouble(i++, averageGrade);
				else
					stmt.setNull(i++, java.sql.Types.VARCHAR);

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

///////////////////////////////////// returns the current stage of applicant
////////////////////////////////////////////////////////////////////////////////////////////////////
	public int getApplicantCurrentStage(Applicant applicant) {
		int currentStage = 1;
		ArrayList<applicantStages> temp = applicant.getStages();

		/*System.out.println(
				"-----------------------------------------------------------------------------------------------------------");*/

		for (applicantStages s : temp) {

			//System.out.println("stage for " + applicant + " s=" + s);

			if (currentStage < s.getStageId())
				currentStage = s.getStageId();

		}

		return currentStage;

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////		
///////////////////////////////////// checks if the Background application already exists
////////////////////////////////////////////////////////////////////////////////////////////////////
	// B.getInstutionName() + B.getDepartmentname() + B.getApplicantId()
	public Boolean backgroundEducationAlreadyExists(String inst, String department, String applicantId) {
		Boolean fieldsValid = false;
		String key = inst.toUpperCase() + department.toUpperCase() + applicantId.toUpperCase();
		HashMap<String, BackgroundEducation> background = getBackgroundEducations();

		fieldsValid = background.containsKey(key);

		return fieldsValid;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * institution name->1 department name->2 applicant Id ->3
	 * 
	 */

	public boolean removeBackgroundEducations(String institutionName, String departmentName, String applicantID) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt1 = conn.prepareCall(Consts.SQL_DEL_BACKGROUND_EDUCATION);) {

				stmt1.setString(1, institutionName);
				stmt1.setString(2, departmentName);
				stmt1.setString(3, applicantID);

				stmt1.executeUpdate();
				return true;

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

//////////////////////////////////////////////////////////////////////////////////////////
// GETS THE diplomas FOR EVERY applicant//SQL_SEL_Background_Education_For_APPLICANT
//////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<BackgroundEducation> getDiplomaByApplicanId(String ApplicantId) {
		ArrayList<BackgroundEducation> diplomas = new ArrayList<>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_Background_Education_For_APPLICANT)) {
				stmt.setString(1, ApplicantId); // Set the first parameter to the institution name
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						int i = 1;
						diplomas.add(new BackgroundEducation(rs.getString(i++), rs.getString(i++), rs.getString(i++),
								rs.getDate(i++), rs.getInt(i++), rs.getInt(i++)));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return diplomas;
	}

//////////////////////////////////////////////////////////////////////////////////////////
//GETS THE documents FOR EVERY applicant//SQL_DEL_ALL_DOCS_BY_APPLICANT_ID
//////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<ApplicantDocument> getDocumentsByApplicanId(String ApplicantId) {
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
