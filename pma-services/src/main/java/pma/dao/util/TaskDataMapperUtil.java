package pma.dao.util;

import java.util.ArrayList;
import java.util.List;

import pma.model.dao.ProjectEntity;
import pma.model.dao.TaskEntity;
import pma.model.dao.UserEntity;
import pma.model.ui.Task;

public class TaskDataMapperUtil {

	public static List<Task> maptaskEntityListToUIModel(List<TaskEntity> taskEntityList, List<ProjectEntity> projectEntityList, List<UserEntity> userEntityList) {
		
		List<Task> taskList = new ArrayList<Task>();
		
		for (TaskEntity taskEntity : taskEntityList) {
			
			Task task = maptaskEntityToUIModel(taskEntity, taskEntityList, projectEntityList, userEntityList);
			
			if (task != null) {
				taskList.add(task);
			}
		}
		
		return taskList;
	}
	
	public static Task maptaskEntityToUIModel(TaskEntity taskEntity, List<TaskEntity> taskEntityList, List<ProjectEntity> projectEntityList, List<UserEntity> userEntityList) {
		
		Task task = null;
		
		if (taskEntity != null) {
			
			task = new Task();
		
			task.setId(taskEntity.getId());
			task.setName(taskEntity.getName());
			task.setPriority(taskEntity.getPriority());
			task.setStartDate(taskEntity.getStartDate());
			task.setEndDate(taskEntity.getEndDate());
			task.setProjectId(taskEntity.getProjectId());
			task.setUserId(taskEntity.getUserId());
			task.setActive(taskEntity.getActive());
			
			if (taskEntity.getParentTaskId() != null) {
				
				task.setSetParentTask(true);
				task.setParentTaskId(taskEntity.getParentTaskId());
				
				for (ProjectEntity projectEntityFromList : projectEntityList) {
					
					if (taskEntity.getProjectId().compareTo(projectEntityFromList.getId()) == 0) {
						task.setProjectName(projectEntityFromList.getName());
						break;
					}
				}
				
				for (TaskEntity taskEntityFromList : taskEntityList) {
					
					if (taskEntity.getParentTaskId().compareTo(taskEntityFromList.getId()) == 0) {
						task.setParentTaskName(taskEntityFromList.getName());
						break;
					}
				}

				for (UserEntity userEntityFromList : userEntityList) {
					
					if (taskEntity.getUserId().compareTo(userEntityFromList.getId()) == 0) {
						task.setUserName(userEntityFromList.getFirstName() + " " + userEntityFromList.getLastName());
						break;
					}
				}
			}
		}
		
		return task;
	}
	
	public static TaskEntity mapTaskUIToEntityModel(Task task) {
		
		TaskEntity taskEntity = null;
		
		if (task != null) {
			
			taskEntity = new TaskEntity();
		
			taskEntity.setId(task.getId());
			taskEntity.setName(task.getName());
			taskEntity.setPriority(task.getPriority());
			taskEntity.setStartDate(task.getStartDate());
			taskEntity.setEndDate(task.getEndDate());
			taskEntity.setProjectId(task.getProjectId());
			taskEntity.setUserId(task.getUserId());
			taskEntity.setActive(task.getActive());
			
			if (task.getSetParentTask() != null && task.getSetParentTask()) {
				taskEntity.setParentTaskId(task.getParentTaskId());
			}
		}
		
		return taskEntity;
	}
	
	
}
