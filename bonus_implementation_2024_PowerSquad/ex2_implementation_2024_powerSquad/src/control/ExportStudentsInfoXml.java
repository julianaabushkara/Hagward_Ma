package control;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import entity.Consts;
public class ExportStudentsInfoXml {
	
	
	private static ExportStudentsInfoXml instance;
	
	private ExportStudentsInfoXml() {}
	
	public static ExportStudentsInfoXml getInstance() {
		if (instance == null)
			instance = new ExportStudentsInfoXml();
		return instance;
	}
	
    public void exportStudentsToXML() {
        // Define the SQL query for selecting accepted and conditionally accepted applicants
        String query = "SELECT ApplicantId, FirstName, LastName, EmailAddress, PhoneNumber, Status " +
                       "FROM TblApplicant " +
                       "WHERE Status IN ('Accepted', 'conditionally_accepted')";
        
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            try (Connection conn = DriverManager.getConnection(Consts.CONN_STR);
                 PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                
                // Create document object.
                Document doc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder().newDocument();
                
                // Push root element into document object.
                Element rootElement = doc.createElement("AcceptedApplicants");
                rootElement.setAttribute("exportDate", LocalDateTime.now().toString());
                doc.appendChild(rootElement);
                
                while (rs.next()) {
                    // Create applicant element.
                    Element applicant = doc.createElement("Applicant");
                    
                    // Assign key to applicant.
                    Attr attr = doc.createAttribute("ID");
                    attr.setValue(rs.getString("ApplicantId"));
                    applicant.setAttributeNode(attr);
                    
                    // Push elements to applicant.
                    applicant.appendChild(createElement(doc, "FirstName", rs.getString("FirstName")));
                    applicant.appendChild(createElement(doc, "LastName", rs.getString("LastName")));
                    applicant.appendChild(createElement(doc, "EmailAddress", rs.getString("EmailAddress")));
                    applicant.appendChild(createElement(doc, "PhoneNumber", rs.getString("PhoneNumber")));
                    applicant.appendChild(createElement(doc, "Status", rs.getString("Status")));
                    
                    // Check if conditionally accepted to add remedial courses
                    if ("conditionally_accepted".equals(rs.getString("Status"))) {
                        PreparedStatement remedialStmt = conn.prepareStatement(
                            "SELECT r.RemedialCourseId, c.CourseName, c.CreditPoint, r.MinimumRequiredGrades, " +
                            "r.SemesterA, r.SemesterB, r.SemesterSummer " +
                            "FROM TblRemedialCourse r " +
                            "JOIN TblInternalCourse i ON r.RemedialCourseId = i.InternalCourseId " +
                            "JOIN TblCourse c ON i.InternalCourseId = c.CourseId " +
                            "JOIN TblPassingGrade p ON r.RemedialCourseId = p.RemedialCourseId " +
                            "WHERE p.ApplicantId = ?");
                        remedialStmt.setString(1, rs.getString("ApplicantId"));
                        ResultSet remedialRs = remedialStmt.executeQuery();

                        Element remedialCourses = doc.createElement("RemedialCourses");
                        while (remedialRs.next()) {
                            Element course = doc.createElement("Course");
                            course.setAttribute("RemedialCourseId", remedialRs.getString("RemedialCourseId"));
                            course.appendChild(createElement(doc, "CourseName", remedialRs.getString("CourseName")));
                            course.appendChild(createElement(doc, "CreditPoint", remedialRs.getString("CreditPoint")));
                            course.appendChild(createElement(doc, "MinimumRequiredGrades", remedialRs.getString("MinimumRequiredGrades")));
                            course.appendChild(createElement(doc, "SemesterA", remedialRs.getBoolean("SemesterA") ? "true" : "false"));
                            course.appendChild(createElement(doc, "SemesterB", remedialRs.getBoolean("SemesterB") ? "true" : "false"));
                            course.appendChild(createElement(doc, "SemesterSummer", remedialRs.getBoolean("SemesterSummer") ? "true" : "false"));
                            remedialCourses.appendChild(course);
                        }
                        applicant.appendChild(remedialCourses);
                    }
                    
                    // Push applicant to document's root element.
                    rootElement.appendChild(applicant);
                }
                
                // Write the content into XML file.
                DOMSource source = new DOMSource(doc);
                File file = new File("xml/accepted_applicants.xml");
                file.getParentFile().mkdirs(); // Create xml folder if it doesn't exist.
                StreamResult result = new StreamResult(file);
                TransformerFactory factory = TransformerFactory.newInstance();
                
                // Optional: Set indentation for pretty printing.
                factory.setAttribute("indent-number", 2);
                Transformer transformer = factory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                
                transformer.transform(source, result);
                
                System.out.println("Applicant data exported successfully!");
            } catch (SQLException | NullPointerException | ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static Element createElement(Document doc, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value != null ? value : ""));
        return element;
    }
	

}
