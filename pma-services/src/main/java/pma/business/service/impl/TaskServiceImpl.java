package pma.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pma.business.service.TaskService;
import pma.dao.ProjectDao;
import pma.dao.TaskDao;
import pma.dao.UserDao;
import pma.dao.util.TaskDataMapperUtil;
import pma.model.dao.ProjectEntity;
import pma.model.dao.TaskEntity;
import pma.model.dao.UserEntity;
import pma.model.ui.Task;

@Service
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private UserDao userDao;

	@Override
	public List<Task> getTasks() {
		List<TaskEntity> taskEntityList = taskDao.getTasks();
		List<ProjectEntity> projectEntityList = projectDao.getProjects();
		List<UserEntity> userEntityList = userDao.getUsers();
		return TaskDataMapperUtil.maptaskEntityListToUIModel(taskEntityList, projectEntityList, userEntityList);
	}
	
	@Override
	public Task getTask(Integer taskId) {
		TaskEntity taskEntity = taskDao.getTask(taskId);
		List<TaskEntity> taskEntityList = taskDao.getTasks();
		List<ProjectEntity> projectEntityList = projectDao.getProjects();
		List<UserEntity> userEntityList = userDao.getUsers();
		return TaskDataMapperUtil.maptaskEntityToUIModel(taskEntity, taskEntityList, projectEntityList, userEntityList);
	}
	
	@Override
	public List<Task> getTasksAssignedToProject(Integer projectId) {
		
		List<Task> taskList = getTasks();
		List<Task> taskAssignedToProject = new ArrayList<Task>();
		
		for (Task task : taskList) {
			if (projectId.compareTo(task.getProjectId()) == 0) {
				taskAssignedToProject.add(task);
			}
		}
		
		return taskAssignedToProject;
	}
	
	@Override
	public List<Task> getSubtasksOfTask(Integer taskId) {
		
		List<Task> taskList = getTasks();
		List<Task> subtaskList = new ArrayList<Task>();
		
		System.out.println(taskList.size());
		
		for (Task task : taskList) {
			if (taskId.compareTo(task.getId()) != 0 && task.getSetParentTask() && taskId.compareTo(task.getParentTaskId()) == 0) {
				subtaskList.add(task);
			}
		}
		
		return subtaskList;
	}
	
	@Override
	public boolean saveTask(Task task) {
		TaskEntity taskEntity = TaskDataMapperUtil.mapTaskUIToEntityModel(task);
		return taskDao.saveTask(taskEntity);
	}

	@Override
	public void deleteTask(Integer taskId) {
		taskDao.deleteTask(taskId);
	}

	@Override
	public boolean isTaskIdExists(Integer taskId) {
		return isTaskIdExists(taskId, null);
	}
	
	@Override
	public boolean isTaskIdExists(Integer taskId, Integer currentTaskIdToSkip) {
		
		List<Task> taskList = getTasks();
		
		for (Task task : taskList) {
			if ((currentTaskIdToSkip == null || currentTaskIdToSkip.compareTo(task.getId()) != 0) && taskId.compareTo(task.getId()) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isTaskNameExists(String taskName) {
		return isTaskNameExists(taskName, null);
	}
	
	@Override
	public boolean isTaskNameExists(String taskName, Integer currentTaskIdToSkip) {
		
		List<Task> taskList = getTasks();
		
		for (Task task : taskList) {
			if ((currentTaskIdToSkip == null || currentTaskIdToSkip.compareTo(task.getId()) != 0) && taskName.equalsIgnoreCase(task.getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public List<Task> searchTasks(String searchStr) {
		
		List<Task> taskList = getTasks();
		List<Task> searchList = new ArrayList<Task>();
		
		for (Task task : taskList) {
			
			List<String> searchTerms = new ArrayList<String>();
			searchTerms.add(task.getName().toLowerCase());
			searchTerms.addAll(Arrays.asList(task.getName().toLowerCase().split(" ")));
			
			if (searchTerms.contains(searchStr.toLowerCase())) {
				searchList.add(task);
			}
		}
		
		return searchList;
	}
	
	@Override
	public boolean endTask(Integer taskId) {
	
		TaskEntity taskEntity = taskDao.getTask(taskId);
		
		if (taskEntity != null) {
			taskEntity.setActive(false);
			
			return taskDao.saveTask(taskEntity);
		}
		
		return false;
	}
}
