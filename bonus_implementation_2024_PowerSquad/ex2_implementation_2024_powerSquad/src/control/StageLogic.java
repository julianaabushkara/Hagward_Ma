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
import entity.Consts;
import entity.Stage;

public class StageLogic {

	private static StageLogic instance;

	private StageLogic() {
	}

	public static StageLogic getInstance() {
		if (instance == null)
			instance = new StageLogic();
		return instance;
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	// GETS THE stages
	//////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<Stage> getStage() {
		ArrayList<Stage> results = new ArrayList<Stage>();
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_Stage);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int i = 1;
					results.add(new Stage(rs.getInt(i++), rs.getString(i++), rs.getString(i++)));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	
	
	////////////////////////////////////UPDATE STAGE FOR APPLICANT//////////////////////////////////////////////////////
	public boolean updateStageFotApplicant(String applicantId, int stage) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_APPLICANT_STAGE)) {
				int i = 1;

				stmt.setString(i++, applicantId); // can't be null
				stmt.setInt(i++, stage);//cant be null
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
	
	
	/////////////////////////////////////////////////////////////////INSERT NEW STAGE INTO ARCHIVE/////////////////////////

	public boolean archiveApplicantStage(String applicantId, int stageId) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_APPLICANT_STAGE)) {
				int i = 1;

				stmt.setInt(i++, stageId);
				stmt.setString(i++, applicantId); // can't be null

				// DAtesubmitted
				Date submittionDate = new Date();
				// Format the submittionDate to only include the date part
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = dateFormat.format(submittionDate);
				stmt.setDate(i++, java.sql.Date.valueOf(formattedDate));


				
				
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

	
	
	
	
	
	
	
	
	
	
}