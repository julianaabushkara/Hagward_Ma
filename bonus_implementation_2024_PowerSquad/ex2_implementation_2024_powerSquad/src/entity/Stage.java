package entity;

import java.util.Objects;

public class Stage {
	
	private int stageId;
	private String stageName;
	private String comment;//optional 
	
	
	//with comment
	public Stage(int stageId, String stageName, String comment) {
		super();
		this.stageId = stageId;
		this.stageName = stageName;
		this.comment = comment;
	}


	//without comment 
	public Stage(int stageId, String stageName) {
		super();
		this.stageId = stageId;
		this.stageName = stageName;
	}


	@Override
	public int hashCode() {
		return Objects.hash(comment, stageId, stageName);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stage other = (Stage) obj;
		return Objects.equals(comment, other.comment) && stageId == other.stageId
				&& Objects.equals(stageName, other.stageName);
	}


	public int getStageId() {
		return stageId;
	}


	public void setStageId(int stageId) {
		this.stageId = stageId;
	}


	public String getStageName() {
		return stageName;
	}


	public void setStageName(String stageName) {
		this.stageName = stageName;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	@Override
	public String toString() {
		return "Stage [stageId=" + stageId + ", stageName=" + stageName + ", comment=" + comment + "]";
	}
	
	
	
	
	

	
	
}
