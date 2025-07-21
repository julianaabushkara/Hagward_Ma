package entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

public class Applicant {

	private String applicantId;
	private String firstName;
	private String lastName;
	private String emailAdress;
	private String phoneNumber;
	private Date submittionDate;
	private String Status;
	private Boolean valid;
	private int currentStage;

	private ArrayList<ApplicantDocument> documents;
	private ArrayList<BackgroundEducation> diplomas;// 1: many relationship
	private ArrayList<applicantStages> stages; 

	////// constructor to download the applicants from the data base -> get
	////// Applicants ///////	
	public Applicant(String applicantId, String firstName, String lastName, String emailAdress, String phoneNumber,
			Date submittionDate, String status, Boolean valid,int currentStage) {
		super();
		this.applicantId = applicantId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAdress = emailAdress;
		this.phoneNumber = phoneNumber;
		this.submittionDate = submittionDate;
		this.Status = status;
		this.valid = valid;
		this.documents = new ArrayList<>();
		this.diplomas = new ArrayList<BackgroundEducation>();
		this.stages=new ArrayList<>();
		this.currentStage=currentStage;
	}




	////// constructor to create a new applicant -> create Applicants- add applicant
	////// ///////
	public Applicant(String applicantId, String firstName, String lastName, String emailAdress, String phoneNumber,Date submittionDate) {
		super();
		this.applicantId = applicantId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAdress = emailAdress;
		this.phoneNumber = phoneNumber;
		this.submittionDate=submittionDate;
		this.valid = false;
		this.Status = ApplicantStatus.waiting_initial_response.toString();
		this.documents = new ArrayList<>();
		this.diplomas = new ArrayList<BackgroundEducation>();
		this.stages=new ArrayList<>();
		this.currentStage=1;

	}

	
	
	public void addDiploma(BackgroundEducation diploma) {
		   if (this.diplomas == null) {
		        this.diplomas = new ArrayList<>(); // Initialize if null (this should ideally be handled in constructors)
		    }
		this.diplomas.add(diploma);
	}
	
	public void addDocuments(ApplicantDocument document) {
		   if (this.documents == null) {
		        this.documents = new ArrayList<>(); // Initialize if null (this should ideally be handled in constructors)
		    }
		this.documents.add(document);
	}
	
	public void addstages(applicantStages stage) {
		   if (this.stages == null) {
		        this.stages = new ArrayList<>(); // Initialize if null (this should ideally be handled in constructors)
		    }
		this.stages.add(stage);
	}
	
	
	public ArrayList<applicantStages> getStages() {
		return stages;
	}




	public void setStages(ArrayList<applicantStages> stages) {
		this.stages = stages;
	}




	public String getApplicantId() {
		return applicantId;
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




	public Date getSubmittionDate() {
		return submittionDate;
	}




	public void setSubmittionDate(Date submittionDate) {
		this.submittionDate = submittionDate;
	}




	public String getStatus() {
		return Status;
	}




	public void setStatus(String status) {
		Status = status;
	}




	public Boolean getValid() {
		return valid;
	}




	public void setValid() {
		this.valid = validApplicant();
	}




	public ArrayList<ApplicantDocument> getDocuments() {
		return documents;
	}




	public void setDocuments(ArrayList<ApplicantDocument> documents) {
		this.documents = documents;
	}




	public ArrayList<BackgroundEducation> getDiplomas() {
		return diplomas;
	}




	public void setDiplomas(ArrayList<BackgroundEducation> diplomas) {
		this.diplomas = diplomas;
	}


	
	
	@Override
	public String toString() {
		return "Applicant [applicantId=" + applicantId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAdress=" + emailAdress + ", phoneNumber=" + phoneNumber + ", submittionDate=" + submittionDate
				+ ", Status=" + Status + ", valid=" + valid + ", currentStage=" + currentStage + ", documents="
				+ documents + ", diplomas=" + diplomas + ", stages=" + stages + "]";
	}




	@Override
	public int hashCode() {
		return Objects.hash(Status, applicantId, currentStage, diplomas, documents, emailAdress, firstName, lastName,
				phoneNumber, stages, submittionDate, valid);
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Applicant other = (Applicant) obj;
		return Objects.equals(Status, other.Status) && Objects.equals(applicantId, other.applicantId)
				&& currentStage == other.currentStage && Objects.equals(diplomas, other.diplomas)
				&& Objects.equals(documents, other.documents) && Objects.equals(emailAdress, other.emailAdress)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(phoneNumber, other.phoneNumber) && Objects.equals(stages, other.stages)
				&& Objects.equals(submittionDate, other.submittionDate) && Objects.equals(valid, other.valid);
	}




	public Boolean validApplicant()
	{
	
		Boolean resume=false ,recommendation=false ,certificate=false,transcript=false;
		if(documents.isEmpty())
			return false;
		for(ApplicantDocument d:documents)
		{
			
			if(d.getType().equals(DocumentType.transcript.toString()))
				transcript=true;
			if(d.getType().equals(DocumentType.certificate.toString()))
				certificate=true;
			if(d.getType().equals(DocumentType.recommendation.toString()))
				recommendation=true;
			if(d.getType().equals(DocumentType.resume.toString()))
				resume=true;
			
		}
		if( transcript && resume &&(recommendation||certificate ))
			return true;
		
		return false;
	}

	



	public int getCurrentStage() {
		return currentStage;
	}




	public void setCurrentStage(int currentStage) {
		this.currentStage = currentStage;
	}



	




	
	


}
