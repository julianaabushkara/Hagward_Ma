package control;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import entity.Consts;
import entity.Department;
import entity.Institution;

public class InstitutionLogic {
	private static InstitutionLogic instance;

    private InstitutionLogic() { }

    public static InstitutionLogic getInstance() {
        if (instance == null)
            instance = new InstitutionLogic();
        return instance;
    }

  //////////////////////////////////////////////////////////////////
  //READ department FROM DB - Department name/institution Name/ phone number 
  //////////////////////////////////////////////////////////////////
    public HashMap<String,Department> getDepartments() {
      	 ArrayList<Department> results = new ArrayList<Department>();
 		HashMap<String,Department>departments=new HashMap<String,Department>();


   	        try {
   	            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
   	            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
   	                    PreparedStatement stmt =conn.prepareStatement(Consts.SQL_SEL_Department);
   	                    ResultSet department = stmt.executeQuery()) {
   	                while (department.next()) {
   	                    int i = 1;
   	                    results.add(new Department(department.getString(i++), department.getString(i++),department.getString(i++) ));
   	                    
   	   
   	                }
   	                
   	            } catch (SQLException e) {
   	                e.printStackTrace();
   	            }
   	        } catch (ClassNotFoundException e) {
   	            e.printStackTrace();
   	        }

			for(Department D :results)
			{				 

				departments.put(D.getDepartmentName()+D.getInstitutionName() , D);
			}
   	        return departments;
      }
    
    

    ////////////////////////////////////////////////////////////////////////////////////////
    //READ Institution FROM DB - institution name// countey // city // street adress 
    //////////////////////////////////////////////////////////////////////////////////////////
    
    public HashMap<String,Institution> getInstitution() {
     	 ArrayList<Institution> results = new ArrayList<Institution>();
		HashMap<String,Institution>institutions=new HashMap<String,Institution>();


  	        try {
  	            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
  	            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
  	                    PreparedStatement stmt =conn.prepareStatement(Consts.SQL_SEL_Institution);
  	                    ResultSet institution = stmt.executeQuery()) {
  	                while (institution.next()) {
  	                    int i = 1;
  	                    results.add(new Institution(institution.getString(i++), institution.getString(i++),institution.getString(i++),institution.getString(i++) ));
  	                }
  	                
  	            } catch (SQLException e) {
  	                e.printStackTrace();
  	            }
  	        } catch (ClassNotFoundException e) {
  	            e.printStackTrace();
  	        }
  	        
  	        //fills in all the departments of every institution
  	      for (Institution institution : results) {
  	    	  //calls the query that select all the departments for every institution
  	        ArrayList<Department> departments = getDepartmentsByInstitutionName(institution.getInstitutionName());
  	        
  	        //adds every department to the arraylist departments in institution 
  	        for (Department dept : departments) {
  	            institution.addDepartment(dept);
  	        }
  	        // inserts every institution in a hashmap with key -> institutions name
  	        institutions.put(institution.getInstitutionName(), institution);
  	    }			 
			
  	        return institutions;
     }
    
    //////////////////////////////////////////////////////////////////////////////////////////
    // GETS THE DEPARTMENTS FOR EVERY INSTITUTION//SQL_SEL_InstitutionDep
    //////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Department> getDepartmentsByInstitutionName(String institutionName) {
        ArrayList<Department> departments = new ArrayList<>();
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(Consts.SQL_SEL_InstitutionDep)) { 
                stmt.setString(1, institutionName); // Set the first parameter to the institution name
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                    	int i = 1;
                    	departments.add(new Department(rs.getString(i++),rs.getString(i++),rs.getString(i++)));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return departments;
    }

    
    
    
 
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    //adds institution
    ///////////////////////////////////////////////////////////////////////////////////////////
   
    public boolean addInstitution(String institutionName, String country,String city,String streetAdrr) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                    CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_Institution)) {
                int i = 1;

                stmt.setString(i++, institutionName);  // can't be null

                if (country != null)
                    stmt.setString(i++, country);
                else
                    stmt.setNull(i++, java.sql.Types.VARCHAR);
                
                if (city != null)
                    stmt.setString(i++, city);
                else
                    stmt.setNull(i++, java.sql.Types.VARCHAR);
                
                if (streetAdrr != null)
                    stmt.setString(i++, streetAdrr);
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
    
    
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    //adds department
    ///////////////////////////////////////////////////////////////////////////////////////////
    
    public boolean addDepartment( String departmentName,String institutionName,String departmentPhone) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                    CallableStatement stmt = conn.prepareCall(Consts.SQL_INS_Department)) {
                int i = 1;

                stmt.setString(i++, departmentName); // can't be null
                stmt.setString(i++, institutionName);  // can't be null

           
                if (departmentPhone != null)
                    stmt.setString(i++, departmentPhone);
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
    
    
    //////////////////////////////////////////////////////////////////////////////
    //checks if the institution and the department already exists
    //////////////////////////////////////////////////////////////////////////////
    
	// Check if the institution and department already exist
	public boolean institutionIsValid(String instituteName, String departmentName) {
	    String key = departmentName + instituteName;
	    return getDepartments().containsKey(key);
	}
	
	
    
  
	
	
	
	
}
