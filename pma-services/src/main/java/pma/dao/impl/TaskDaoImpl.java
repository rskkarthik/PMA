package pma.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pma.dao.TaskDao;
import pma.dao.repo.TaskRepository;
import pma.model.dao.TaskEntity;

@Component
public class TaskDaoImpl implements TaskDao {
	
	@Autowired
	private TaskRepository taskRepository;

	@Override
	public List<TaskEntity> getTasks() {
		
		Iterable<TaskEntity> taskEntityiterable = taskRepository.findAll();
		List<TaskEntity> taskEntityList = new ArrayList<TaskEntity>();
		taskEntityiterable.forEach(taskEntityList::add);
		
		return taskEntityList;
	}
	
	@Override
	public TaskEntity getTask(Integer taskId) {
		
		TaskEntity taskEntity = null;
		Optional<TaskEntity> taskEntityOpt = taskRepository.findById(taskId);
		
		if (taskEntityOpt.isPresent()) {
			taskEntity = taskEntityOpt.get();
		}
		
		return taskEntity;
	}
	
	@Override
	public boolean saveTask(TaskEntity task) {
		return taskRepository.save(task) != null;
	}
	
	@Override
	public void deleteTask(Integer taskId) {
		taskRepository.deleteById(taskId);
	}
	
	@Override
	public boolean isTaskIdExists(Integer taskId) {
		return taskRepository.existsById(taskId);
	}
}
