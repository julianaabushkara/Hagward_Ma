package entity;

import java.util.Date;
import java.util.Objects;

public class applicantStages {
	private int stageId;
	private String applicantId;
	private Date dateEntered;
	
	
	
	public applicantStages(int stageId, String applicantId, Date dateEntered) {
		super();
		this.stageId = stageId;
		this.applicantId = applicantId;
		this.dateEntered = dateEntered;
	}



	@Override
	public int hashCode() {
		return Objects.hash(applicantId, dateEntered, stageId);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		applicantStages other = (applicantStages) obj;
		return Objects.equals(applicantId, other.applicantId) && Objects.equals(dateEntered, other.dateEntered)
				&& stageId == other.stageId;
	}



	public int getStageId() {
		return stageId;
	}



	public void setStageId(int stageId) {
		this.stageId = stageId;
	}



	public String getApplicantId() {
		return applicantId;
	}



	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}



	public Date getDateEntered() {
		return dateEntered;
	}



	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}



	@Override
	public String toString() {
		return "applicantStages [stageId=" + stageId + ", applicantId=" + applicantId + ", dateEntered=" + dateEntered
				+ "]";
	}
	
	
	
	
	
}
