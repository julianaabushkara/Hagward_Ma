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
import entity.Consts;
import entity.LogInInfo;

public class LogInLogic {

	private static LogInLogic instance;

	private LogInLogic() {
	}

	public static LogInLogic getInstance() {
		if (instance == null)
			instance = new LogInLogic();
		return instance;
	}

//////////////////////////////////////////////////////////////////
//READ LogInInfo FROM DB -  id/Password
//////////////////////////////////////////////////////////////////
	public HashMap<String, LogInInfo> getLogIn() {
		ArrayList<LogInInfo> results = new ArrayList<LogInInfo>();
		HashMap<String, LogInInfo> logInInfo = new HashMap<String, LogInInfo>();

		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_LogInFo);
					ResultSet logIn = stmt.executeQuery()) {
				while (logIn.next()) {
					int i = 1;
					results.add(new LogInInfo(logIn.getString(i++), logIn.getString(i++), logIn.getBoolean(i++)));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for (LogInInfo l : results) {

			logInInfo.put(l.getId(), l);
		}

		// enters the program chair and secretary id
		LogInInfo programChair = new LogInInfo("0", "0", false);
		LogInInfo secretery = new LogInInfo("01", "01", false);
		logInInfo.put(secretery.getId(), secretery);
		logInInfo.put(programChair.getId(), programChair);

	

		return logInInfo;
	}

/////////////////////////////////////////////////////////////////////////////////////////////
//adds LogIN
///////////////////////////////////////////////////////////////////////////////////////////

	public boolean addLogIN(String id, String password, Boolean deleted) {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

			try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
					CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_LogInFo)) {
				int i = 1;

				stmt.setString(i++, id); // can't be null
				stmt.setString(i++, password);

				if (deleted != null)
					stmt.setBoolean(i++, deleted);
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
	
	// edits the applicant
		public boolean editLogIn(String applicantId, Boolean Deleted) {
			try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

				try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
						CallableStatement stmt = conn.prepareCall(Consts.SQL_UPD_LogIn)) {
					int i = 1;


					
					stmt.setBoolean(i++, Deleted);// can't be null
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

	
	

///////////////////////////////////////////////////////////////////////////////////////////////////////////	
///////////////////////////////////// checks if the applicant already exists
////////////////////////////////////////////////////////////////////////////////////////////////////
	public Boolean LogInExists(String applicantId) {
		Boolean fieldsValid = false;
		HashMap<String, LogInInfo> ap = getLogIn();
		fieldsValid = ap.containsKey(applicantId);
		return fieldsValid;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
