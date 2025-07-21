package entity;

import java.util.Objects;

public class LogInInfo {
	
	private String id;
	private String password;
	private Boolean deleted ;
	
	public LogInInfo(String id,String password,Boolean deleted) {
		super();
		this.password = password;
		this.id = id;
		this.deleted=deleted;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, password);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogInInfo other = (LogInInfo) obj;
		return Objects.equals(id, other.id) && Objects.equals(password, other.password);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	
	

	

	
	
	
	
}
