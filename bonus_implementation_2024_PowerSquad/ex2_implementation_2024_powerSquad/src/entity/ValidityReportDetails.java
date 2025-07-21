package entity;

import java.sql.Date;
import java.util.ArrayList;

public class ValidityReportDetails {

	private String applicantId;
	private String firstName;
	private String lastName;
	private String emailAdress;
	private String phoneNumber;
	private Boolean valid;
	private ArrayList<ApplicantDocument> documents;
	private Boolean resume;
	private Boolean transcript;
	private Boolean certificate;
	private Boolean recommendation;
	
	
	
	
	
	
	
	
	
	
	
public ValidityReportDetails(String applicantId, String firstName, String lastName, String emailAdress,
			String phoneNumber, Boolean valid) {
		super();
		this.applicantId = applicantId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAdress = emailAdress;
		this.phoneNumber = phoneNumber;
		this.valid = valid;
		this.documents = new ArrayList<>();
		this.resume = false;
		this.transcript = false;
		this.certificate = false;
		this.recommendation = false;
	}
//	resume,transcript,certificate,recommendation
	public String getApplicantId() {
		return applicantId;
	}
	
	
	public void addDocuments(ApplicantDocument document) {
		   if (this.documents == null) {
		        this.documents = new ArrayList<>(); // Initialize if null (this should ideally be handled in constructors)
		    }
		this.documents.add(document);
	}
	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAdress() {
		return emailAdress;
	}
	public void setEmailAdress(String emailAdress) {
		this.emailAdress = emailAdress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public ArrayList<ApplicantDocument> getDocuments() {
		return documents;
	}
	public void setDocuments(ArrayList<ApplicantDocument> documents) {
		this.documents = documents;
	}
	public Boolean getResume() {
		return resume;
	}
	public void setResume(Boolean resume) {
		this.resume = resume;
	}
	public Boolean getTranscript() {
		return transcript;
	}
	public void setTranscript(Boolean transcript) {
		this.transcript = transcript;
	}
	public Boolean getCertificate() {
		return certificate;
	}
	public void setCertificate(Boolean certificate) {
		this.certificate = certificate;
	}
	public Boolean getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(Boolean recommendation) {
		this.recommendation = recommendation;
	}

	
	
	
}
