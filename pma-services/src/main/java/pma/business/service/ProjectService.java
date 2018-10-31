package pma.business.service;

import java.util.List;

import pma.model.ui.Project;

public interface ProjectService {

	public List<Project> getProjects();
	
	public boolean saveProject(Project project);
	
	public void deleteProject(Integer projectId);
	
	public boolean isProjectNameExists(String name);
	
	public boolean isProjectNameExists(String name, Integer currentProjectIdToSkip);
	
	public boolean isProjectIdExists(Integer projectId);
	
	public boolean isProjectHasAnyTaskAssigned(Integer projectId);
	
	public List<Project> searchProjects(String searchStr);
	
	public boolean endProject(Integer projectId);
}
