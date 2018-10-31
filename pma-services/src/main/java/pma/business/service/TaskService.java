package pma.business.service;

import java.util.List;

import pma.model.ui.Task;

public interface TaskService {

	public List<Task> getTasks();
	
	public Task getTask(Integer taskId);
	
	public List<Task> getTasksAssignedToProject(Integer projectId);
	
	public List<Task> getSubtasksOfTask(Integer taskId);
	
	public boolean saveTask(Task task);
	
	public void deleteTask(Integer taskId);
	
	public boolean isTaskIdExists(Integer taskId);
	
	public boolean isTaskIdExists(Integer taskId, Integer currentTaskIdToSkip);
	
	public boolean isTaskNameExists(String taskName);
	
	public boolean isTaskNameExists(String taskName, Integer currentTaskIdToSkip);
	
	public List<Task> searchTasks(String searchStr);
	
	public boolean endTask(Integer taskId);
}
