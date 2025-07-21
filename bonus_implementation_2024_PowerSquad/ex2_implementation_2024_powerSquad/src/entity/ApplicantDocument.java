package entity;

import java.util.Objects;

public class ApplicantDocument {
	
	private int documentId; 
	private String documentName;
	private String type;
	private String folderlink;
	private String applicantId;
	private int stageID;
	
	
	///to get the documents from the DB
	public ApplicantDocument(int documentId, String documentName, String type, String folderlink, String applicantId,
			int stageID) {
		super();
		this.documentId = documentId;
		this.documentName = documentName;
		

		this.type = type;
		this.folderlink = folderlink;
		this.applicantId = applicantId;
		this.stageID = stageID;
	}

	///add new document
	public ApplicantDocument(String documentName, String type, String folderlink, String applicantId, int stageID) {
		super();
		this.documentName = documentName;
		this.type = type;
		this.folderlink = folderlink;
		this.applicantId = applicantId;
		this.stageID = stageID;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(applicantId, documentId, documentName, folderlink, stageID, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicantDocument other = (ApplicantDocument) obj;
		return Objects.equals(applicantId, other.applicantId) && documentId == other.documentId
				&& Objects.equals(documentName, other.documentName) && Objects.equals(folderlink, other.folderlink)
				&& Objects.equals(stageID, other.stageID) && type == other.type;
	}
	
	
	

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFolderlink() {
		return folderlink;
	}

	public void setFolderlink(String folderlink) {
		this.folderlink = folderlink;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public int getStageID() {
		return stageID;
	}

	public void setStageID(int stageID) {
		this.stageID = stageID;
	}

	//	resume,transcript,certificate,recommendation
	public DocumentType StringConvertDocumentType(String type)
	{
		if(type.equals(DocumentType.resume.toString()))
			return DocumentType.resume; 
		if(type.equals(DocumentType.recommendation.toString()))
			return DocumentType.recommendation;
		if(type.equals(DocumentType.certificate.toString()))
			return DocumentType.certificate;
		if(type.equals(DocumentType.transcript.toString()))
			return DocumentType.transcript;
		
		return null;
	}

	@Override
	public String toString() {
		return "Document [documentId=" + documentId + ", documentName=" + documentName + ", type=" + type
				+ ", folderlink=" + folderlink + ", applicantId=" + applicantId + ", stageID=" + stageID + "]";
	}
	
}
