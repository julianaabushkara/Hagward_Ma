package entity;

import java.net.URLDecoder;


public class Consts {
	

	private Consts() {
		throw new AssertionError();
	}
    
    protected static final String DB_FILEPATH = getDBPath();

    public static final String CONN_STR = "jdbc:ucanaccess://"  + DB_FILEPATH + ";COLUMNORDER=DISPLAY";

    
    
    
    
    
    ///////////////////////////////////////////////////Examption///////////////////////////////////////////////////////
    //QryInsExamption
    //QryUpdExamption
    //QryDelExemption
    
    
    public static final String SQL_SEL_EXAMPT_COURSES = " SELECT * FROM TblExemption ";
    //exampt courses- insert a new record
    public static final String SQL_INS_EXAMPT_COURSES ="{ call QryInsExamption(?,?,?,?) }";
    
    //exampt Course- update by name of CourseID and remedial Courses
    public static final String SQL_UPD_EXAMPT_COURSES ="{ call QryUpdExamption(?,?,?,?) }";
    
    //Deletes a record 
    public static final String SQL_DEL_EXAMPT_COURSES = "{ call QryDelExemption(?,?) }";// course id -- remedial courses 
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////// Stage ////////////////////////////////////////////////
    
    public static final String SQL_SEL_Stage = "SELECT * FROM TblStage";
    //QryInsArchiveApplicantStage- applicant entered a new stage
    //QryUpdApplicantStage
    public static final String SQL_UPD_APPLICANT_STAGE = "{ call QryUpdApplicantStage(?,?) }";
    public static final String SQL_INS_APPLICANT_STAGE = "{ call QryInsArchiveApplicantStage(?,?,?) }";
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////courses//////////////////////////////////////////
    
    public static final String SQL_SEL_COURSES = "SELECT * FROM TblCourse";
    
    public static final String SQL_SEL_INTERNAL_COURSES = "SELECT * FROM TblInternalCourse";
    
    public static final String SQL_SEL_REMEDIAL_COURSES = "SELECT * FROM TblRemedialCourse";
    
    ////////////////////////////////////////Grades/////////////////////////////////////////////////////
    //QryInsGotGrade,QryUpdGotGrade
    
    public static final String SQL_SEL_GOT_GRADES = "SELECT * FROM gotGrade";
    public static final String SQL_INS_GOT_GRADE = "{ call QryInsGotGrade(?,?,?) }";
    //inserts a new GRADE
    public static final String SQL_UPD_GOT_GRADE ="{ call QryUpdGotGrade(?,?,?) }";
	public static final String DELETE_GOT_GRADE = "{ call QryDelGotGrades(?,?); }";
	
	
	 public static final String SQL_SEL_PASSING_GRADES = "SELECT * FROM TblPassingGrade";
	 public static final String SQL_INS_PASSING_GRADE = "{ call QryInsPassingGrade(?,?,?) }";
	 //inserts a new GRADE
	 public static final String SQL_UPD_PASSING_GRADE ="{ call QryUpdPassingGrade(?,?,?) }";
	 public static final String SQL_DELETE_PASSING_GRADE = "{ call QryDelPassingGrade(?,?); }";
	 
	 //QryDelApplicantGotGrade
	
	 public static final String SQL_DELETE_APPLICANT_GOT_GRADE = "{ call QryDelApplicantGotGrade(?); }";

    
    
    ////////////////////////////////////////////log in info ///////////////////////////////////////////
    public static final String SQL_SEL_LogInFo = "SELECT * FROM TblLogInInfo";
    public static final String SQL_INS_LogInFo = "{ call QryInsLogIn(?,?,?) }";
    //inserts a new applicant
    
    public static final String SQL_UPD_LogIn ="{ call QryUpdLogIn(?,?) }";
    //query to update applicant

    
    
    /////////////////////////////////////////////////////validity report//////////////////////////////////////
    public static final String SQL_SEL_VALIDITY_REPORT = 
    	    "SELECT ApplicantId, FirstName, LastName, EmailAddress, PhoneNumber, Valid " +
    	    "FROM TblApplicant WHERE Status = 'waiting_initial_response'";    
    
    ///////////////////////////////////////////////////////////////Export//////////////////////////////////
    //QryExportAcceptedStudents
    
    //public static final String SQL_SEL_ACCEPTED_APPLICANTS ;
    
    
    
    
    ///////////////////////////////////////////////////Documents//////////////////////////////////////////////
    //query that selects all the documents details 
    public static final String SQL_SEL_DOCUMENT = "SELECT * FROM TblDocument";
    
    //inserts a new DOCUMENT    
    public static final String SQL_INS_DOCUMENT = "{ call QryInsDocument(?,?,?,?,?) }";
    
    //query to update DOCUMENT 
    public static final String SQL_UPD_DOCUMENT ="{ call QryUpdDocument(?,?,?,?,?,?) }";

    //deletes DOCUMENT
    public static final String SQL_DEL_DOCUMENT="{call QryDeleteDocument(?)}";
    
    ////////////////////////////////////////////////////Applicants///////////////////////////////////////////
    public static final String SQL_SEL_APPLICANT = "SELECT * FROM TblApplicant";
    //query that selects all the applicants details 
    public static final String SQL_INS_Applicant = "{ call QryInsApplicant(?,?,?,?,?,?,?,?,?) }";
    //inserts a new applicant
    public static final String SQL_UPD_Applicant ="{ call QryUpdApplicant(?,?,?,?,?,?,?,?,?) }";
    //query to update applicant 
    public static final String SQL_DEL_Applicant="{call QryDeleteApplicant(?)}";
    //deletes applicant
    
    public static final String SQL_SEL_ALL_DOCS_BY_APPLICANT_ID="{call QryAllDocsByApplicantId(?) }";
    //returns all documents of a certain applicant by his id 
    
    
    
    ///update status -> QryUpdStatus
    public static final String SQL_UPD_STATUS ="{ call QryUpdStatus(?,?) }";
    //update applicant Validity -> QryUpdValid
    public static final String SQL_UPD_VALIDITY ="{ call QryUpdValid(?,?) }";
    
    
    
    
    
    //
    
    //////////////////////////////////////////////////////documents///////////////////////////////////////////
    // deletes the documents of the applicant from table

    public static final String SQL_DEL_Documents_By_Applicant_Id="{call QryDeleteApllicantsDocuments(?)}";
    //query to delete every document by applicant id 
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    //////////////////////////////////////////////////////passing grade////////////////////////////////////////
    // deletes the applicant from table passing grades
    public static final String SQL_DEL_PassingGrade_Of_Applicant="{call QryDeleteApplicantInPassingGrade(?)}";
    //query to delete every record of passing grade by applicant id
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /////////////////////////////////////////////////////Stage/////////////////////////////////////////////////
    // deletes the applicant from table stage 
    public static final String SQL_DEL_Applicant_from_stage="{call QryDeleteApplicantInStage(?)}";
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    
    
    
    //////////////////////////////////////////////////Institutions/////////////////////////////////////////////
    public static final String SQL_SEL_Institution = "SELECT * FROM TblInstitution";
    //selects the institutions from tblInstitutions
    public static final String SQL_INS_Institution = "{ call QryInsInstitution(?,?,?,?)}";
    //inserts a new applicant

    public static final String SQL_SEL_InstitutionDep ="{ call QryInstitutionDep(?)}";
    //selects every department by institution name

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    
    
    //////////////////////////////////////////////////Departments/////////////////////////////////////////////////
    public static final String SQL_SEL_Department = "SELECT * FROM TblDepartment";
    //selects the institutions from tblInstitutions

    public static final String SQL_INS_Department ="{ call QruInsDepartment(?,?,?)}";
    //inserts a new department 
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    
    ///////////////////////////////////////////////////Diplomas- background education////////////////////////////////////////////////////
   
    //selects all background education from the table
    public static final String SQL_SEL_BACKGROUND_EDUCATION = " SELECT * FROM TblBackgroundEducation ";
    //background education- insert a new record

    public static final String SQL_INS_BACKGROUND_EDUCATION ="{ call QryInsBackgroundEducation(?,?,?,?,?,?) }";
    
    //background education- update by the name of the institution && department && applicant

    public static final String SQL_UPD_BACKGROUND_EDUCATION ="{ call QryUpdBackgroundEducation(?,?,?,?,?,?) }";
    
    //Deletes a record 
    public static final String SQL_DEL_BACKGROUND_EDUCATION = "{ call QryDeleteBackgroundInformation(?,?,?) }";//-- id,institution, departmrnt
    //selects background education from the table by applicant id

    public static final String SQL_SEL_Background_Education_For_APPLICANT ="{ call QueryEveryDiplomaForApplicant(?)}";
    //deletes all background education by applicant id

    public static final String SQL_DEL_BACKGROUND_EDUCATION_By_Applicant="{ call QueryDeleteBackgroundEducationByApplicanId(?)}";// query deletes diplomas by applicant id
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    

    
    
  
	
    
    private static String getDBPath() {
		try {
			String path = Consts.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decoded = URLDecoder.decode(path, "UTF-8");
			// System.out.println(decoded) - Can help to check the returned path
			if (decoded.contains(".jar")) {
				decoded = decoded.substring(0, decoded.lastIndexOf('/'));
				return decoded + "/database/ex2_2024_PowerSquad.accdb";
			} else {
				decoded = decoded.substring(0, decoded.lastIndexOf("bin/"));
				return decoded + "src/entity/ex2_2024_PowerSquad.accdb";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
    }

}
}
