package pma.dao.util;

import java.util.ArrayList;
import java.util.List;

import pma.model.dao.ProjectEntity;
import pma.model.ui.Project;
import pma.model.ui.Task;
import pma.model.ui.User;

public class ProjectDataMapperUtil {

	public static List<Project> mapprojectEntityListToUIModelList(List<ProjectEntity> projectEntityList, List<User> userList, List<Task> taskList) {
		
		List<Project> projectList = new ArrayList<Project>();
		
		for (ProjectEntity projectEntity : projectEntityList) {
			
			Project project = new Project();
			
			project.setId(projectEntity.getId());
			project.setName(projectEntity.getName());
			project.setPriority(projectEntity.getPriority());
			project.setManagerId(projectEntity.getManagerId());
			project.setActive(projectEntity.getActive());
			
			if (projectEntity.getStartDate() != null && projectEntity.getEndDate() != null) {
				project.setSetDate(true);
				project.setStartDate(projectEntity.getStartDate());
				project.setEndDate(projectEntity.getEndDate());
			}
			
			int totalTaskCount = 0;
			int completedTaskCount = 0;

			for (Task task : taskList) {
				
				if (task.getProjectId().compareTo(project.getId()) == 0) {
					totalTaskCount++;
					if (task.getActive()) {
						completedTaskCount++;
					}
				}
			}
			
			for (User user : userList) {
				if (user.getId().compareTo(project.getManagerId()) == 0) {
					project.setManagerName(user.getFirstName() + " " + user.getLastName());
					break;
				}
			}
			
			project.setTotalTaskCount(totalTaskCount);
			project.setCompletedTaskCount(completedTaskCount);
			
			projectList.add(project);
		}
		
		return projectList;
	}
	
	public static ProjectEntity mapProjectUIToEntityModel(Project project) {
		
		ProjectEntity projectEntity = new ProjectEntity();
		
		projectEntity.setId(project.getId());
		projectEntity.setName(project.getName());
		projectEntity.setPriority(project.getPriority());
		projectEntity.setManagerId(project.getManagerId());
		projectEntity.setActive(project.getActive());
		
		if (project.getSetDate() != null && project.getSetDate()) {
			projectEntity.setStartDate(project.getStartDate());
			projectEntity.setEndDate(project.getEndDate());
		}
		
		return projectEntity;
	}
}
