package pma.model.ui;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Project {

	private Integer id;
	private String name;
	private Integer priority;
	private Boolean setDate;
	private Date startDate;
	private Date endDate;
	private Integer managerId;
	private String managerName;
	private Boolean active;
	private Integer totalTaskCount;
	private Integer completedTaskCount;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = StringUtils.trim(name);
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Boolean getSetDate() {
		return setDate != null ? setDate : false;
	}
	public void setSetDate(Boolean setDate) {
		this.setDate = setDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getManagerId() {
		return managerId;
	}
	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active == null ? true : active;
	}
	public Integer getTotalTaskCount() {
		return totalTaskCount;
	}
	public void setTotalTaskCount(Integer totalTaskCount) {
		this.totalTaskCount = totalTaskCount;
	}
	public Integer getCompletedTaskCount() {
		return completedTaskCount;
	}
	public void setCompletedTaskCount(Integer completedTaskCount) {
		this.completedTaskCount = completedTaskCount;
	}
}
