package pma.dao;

import java.util.List;

import pma.model.dao.ProjectEntity;

public interface ProjectDao {
	
	public List<ProjectEntity> getProjects();
	
	public ProjectEntity getProject(Integer projectId);
	
	public boolean saveProject(ProjectEntity project);
	
	public void deleteProject(Integer projectId);
	
	public boolean isProjectIdExists(Integer projectId);
}
