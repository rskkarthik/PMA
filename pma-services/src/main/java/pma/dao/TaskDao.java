package pma.dao;

import java.util.List;

import pma.model.dao.TaskEntity;

public interface TaskDao {
	
	public List<TaskEntity> getTasks();
	
	public TaskEntity getTask(Integer taskId);
	
	public boolean saveTask(TaskEntity task);
	
	public void deleteTask(Integer taskId);
	
	public boolean isTaskIdExists(Integer taskId);
}
