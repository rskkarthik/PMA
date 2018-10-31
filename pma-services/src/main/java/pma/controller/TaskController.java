package pma.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pma.business.service.ProjectService;
import pma.business.service.TaskService;
import pma.business.service.UserService;
import pma.model.ui.Project;
import pma.model.ui.Response;
import pma.model.ui.Search;
import pma.model.ui.Task;

@RestController
public class TaskController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;

	@GetMapping(path="/getTasks")
	public Response getTasks() {
		
		try {
			List<Task> taskList = taskService.getTasks();
			return new Response<>(Response.SUCCESS, taskList, null);
		} catch (Exception e) {
			return new Response<>(Response.EXCEPTION, null, e.getMessage());
		}
	}
	
	@PostMapping(path="/getTasksOfProject")
	public Response getTasksAssignedToProject(@RequestBody Project project) {
		
		int status = Response.BAD_REQUEST;
		List<Task> taskList = null;
		String errorMsg = null;
		
		try {
			if (project == null 
					|| project.getId() == null) {
				errorMsg = "Invalid input.";
			}
			
			if (errorMsg == null) {
				taskList = taskService.getTasksAssignedToProject(project.getId());
				status = Response.SUCCESS;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, taskList, errorMsg);
	}
	
	@PostMapping("/addTask")
	public Response addTask(@RequestBody Task task) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (task == null 
					|| task.getId() != null 
					|| StringUtils.isBlank(task.getName()) 
					|| task.getProjectId() == null
					|| task.getPriority() == null
					|| task.getStartDate() == null
					|| task.getEndDate() == null
					|| task.getUserId() == null
					) {
				errorMsg = "Invalid input.";
			} else if (taskService.isTaskNameExists(task.getName())) {
				errorMsg = "Another task with name '" + task.getName() + "' already exists.";
			} else if (!projectService.isProjectIdExists(task.getProjectId())) {
				errorMsg = "Project does not exist.";
			} else if (task.getSetParentTask() != null && task.getSetParentTask()) {
				if ((task.getParentTaskId() == null || !taskService.isTaskIdExists(task.getParentTaskId()))) {
					errorMsg = "Parent task does not exist.";
				}
			} else if (!userService.isUserIdExists(task.getUserId())) {
				errorMsg = "User does not exist.";
			}
			
			if (errorMsg == null) {
				boolean isAdded = taskService.saveTask(task);
				status = isAdded ? Response.SUCCESS : Response.EXCEPTION;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
	
	@PostMapping("/updateTask")
	public Response updateTask(@RequestBody Task task) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			
			if (task == null 
					|| task.getId() == null 
					|| StringUtils.isBlank(task.getName()) 
					|| task.getProjectId() == null
					|| task.getPriority() == null
					|| task.getStartDate() == null
					|| task.getEndDate() == null
					|| task.getUserId() == null
					) {
				errorMsg = "Invalid input.";
			} else if (!taskService.isTaskIdExists(task.getId())) {
				errorMsg = "Task does not exist.";
			} else if (taskService.isTaskNameExists(task.getName(), task.getId())) {
				errorMsg = "Another task with name '" + task.getName() + "' already exists.";
			} else if (!projectService.isProjectIdExists(task.getProjectId())) {
				errorMsg = "Project does not exist.";
			} else if (task.getSetParentTask() != null && task.getSetParentTask()) {
				if (task.getParentTaskId() == null) {
					errorMsg = "Parent task not available.";
				} else {
					Task parentTaskToBe = taskService.getTask(task.getParentTaskId());
					if (parentTaskToBe == null) {
						errorMsg = "Parent task does not exist.";
					} else if (parentTaskToBe.getParentTaskId() != null 
							&& parentTaskToBe.getParentTaskId().compareTo(task.getId()) == 0) {
						errorMsg = "Parent task selected is already a subtask of this task.";
					}
				}
			} else if (!userService.isUserIdExists(task.getUserId())) {
				errorMsg = "User does not exist.";
			}
			
			if (errorMsg == null) {
				boolean isAdded = taskService.saveTask(task);
				status = isAdded ? Response.SUCCESS : Response.EXCEPTION;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
	
	@PostMapping("/deleteTask")
	public Response deleteTask(@RequestBody Task task) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (task == null 
					|| task.getId() == null 
					) {
				errorMsg = "Invalid input.";
			} else if (!taskService.isTaskIdExists(task.getId())) {
				errorMsg = "Task does not exist.";
			} else {
				List<Task> subTasks = taskService.getSubtasksOfTask(task.getId());
				if (!subTasks.isEmpty()) {
					errorMsg = "Task has one or more sub task assigned.";
				}
			}
			
			if (errorMsg == null) {
				taskService.deleteTask(task.getId());
				status = Response.SUCCESS;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
	
	@PostMapping("/searchTasks")
	public Response searchTasks(@RequestBody Search search) {
		
		int status = Response.BAD_REQUEST;
		List<Task> taskList = null;
		String errorMsg = null;
		
		try {
			if (search == null 
					|| StringUtils.isBlank(search.getName())) {
				errorMsg = "Invalid input.";
			}
			
			if (errorMsg == null) {
				taskList = taskService.searchTasks(search.getName());
				status = Response.SUCCESS;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, taskList, errorMsg);
	}
	
	@PostMapping("/endTask")
	public Response endTask(@RequestBody Task task) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (task == null 
					|| task.getId() == null 
					) {
				errorMsg = "Invalid input.";
			} else if (!taskService.isTaskIdExists(task.getId())) {
				errorMsg = "Task does not exist.";
			}
			
			if (errorMsg == null) {
				boolean isEnded = taskService.endTask(task.getId());
				status = isEnded ? Response.SUCCESS : Response.EXCEPTION;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
}
