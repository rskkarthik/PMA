package pma.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pma.business.service.ProjectService;
import pma.business.service.TaskService;
import pma.business.service.UserService;
import pma.dao.ProjectDao;
import pma.dao.util.ProjectDataMapperUtil;
import pma.model.dao.ProjectEntity;
import pma.model.ui.Project;
import pma.model.ui.Task;
import pma.model.ui.User;

@Service
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ProjectDao projectDao;

	@Override
	public List<Project> getProjects() {
		List<ProjectEntity> projectEntityList = projectDao.getProjects();
		List<User> userList = userService.getUsers();
		List<Task> taskList = taskService.getTasks();
		return ProjectDataMapperUtil.mapprojectEntityListToUIModelList(projectEntityList, userList, taskList);
	}
	
	@Override
	public boolean saveProject(Project project) {
		ProjectEntity projectEntity = ProjectDataMapperUtil.mapProjectUIToEntityModel(project);
		return projectDao.saveProject(projectEntity);
	}
	
	@Override
	public void deleteProject(Integer projectId) {
		projectDao.deleteProject(projectId);
	}
	
	@Override
	public boolean isProjectIdExists(Integer projectId) {
		
		List<Project> projectList = getProjects();
		
		for (Project project : projectList) {
			if (projectId.compareTo(project.getId()) == 0) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean isProjectNameExists(String projectName) {
		return isProjectNameExists(projectName, null);
	}
	
	@Override
	public boolean isProjectNameExists(String projectName, Integer currentProjectIdToSkip) {
		
		Iterable<Project> projectList = getProjects();
		
		for (Project project : projectList) {
			if ((currentProjectIdToSkip == null || currentProjectIdToSkip.compareTo(project.getId()) != 0) && projectName.equalsIgnoreCase(project.getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isProjectHasAnyTaskAssigned(Integer projectId) {

		List<Task> taskList = taskService.getTasks();
		
		for (Task task : taskList) {
			if (projectId.compareTo(task.getProjectId()) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public List<Project> searchProjects(String searchStr) {
		
		List<Project> projectList = getProjects();
		List<Project> searchList = new ArrayList<Project>();
		
		for (Project project : projectList) {
			
			List<String> searchTerms = new ArrayList<String>();
			searchTerms.add(project.getName().toLowerCase());
			searchTerms.addAll(Arrays.asList(project.getName().toLowerCase().split(" ")));
			
			if (searchTerms.contains(searchStr.toLowerCase())) {
				searchList.add(project);
			}
		}
		
		return searchList;
	}
	
	@Override
	public boolean endProject(Integer projectId) {
	
		ProjectEntity projectEntity = projectDao.getProject(projectId);
		
		if (projectEntity != null) {
			projectEntity.setActive(false);
			
			return projectDao.saveProject(projectEntity);
		}
		
		return false;
	}
}	
