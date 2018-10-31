package pma.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import pma.business.service.impl.ProjectServiceImpl;
import pma.business.service.impl.TaskServiceImpl;
import pma.business.service.impl.UserServiceImpl;
import pma.model.ui.Project;
import pma.model.ui.Response;
import pma.model.ui.Search;
import pma.model.ui.Task;

@RunWith(JUnit4.class)
public class TaskControllerTest {

	private TaskController taskController;
	private UserServiceImpl userService;
	private ProjectServiceImpl projectService;
	private TaskServiceImpl taskService;
	
	@Before
	public void setup() {
		
		taskController = new TaskController();
		userService = Mockito.mock(UserServiceImpl.class);
		projectService = Mockito.mock(ProjectServiceImpl.class);
		taskService = Mockito.mock(TaskServiceImpl.class);
		
		Whitebox.setInternalState(taskController, "userService", userService);
		Whitebox.setInternalState(taskController, "projectService", projectService);
		Whitebox.setInternalState(taskController, "taskService", taskService);
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getTasks() throws Exception {
		
		Task task = new Task();
		task.setId(1);
		task.setName("Task 1");
		task.setPriority(0);
		task.setStartDate(new Date());
		task.setEndDate(new Date());
		task.setProjectId(1);
		task.setUserId(1);
		
		List<Task> taskList = Collections.singletonList(task);
		
		Mockito.when(taskService.getTasks()).thenReturn(taskList);
		Response response = taskController.getTasks();
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertEquals(taskList, response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(taskService.getTasks()).thenThrow(new RuntimeException("error"));
		
		response = taskController.getTasks();
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
	}
	
	@Test
	public void getTasksAssignedToProject() throws Exception {
		
		Task task1 = new Task();
		task1.setId(1);
		task1.setName("Task 1");
		task1.setPriority(0);
		task1.setStartDate(new Date());
		task1.setEndDate(new Date());
		task1.setProjectId(1);
		task1.setUserId(1);
		
		Project project = new Project();
		project.setId(1);
		
		Mockito.when(taskService.getTasksAssignedToProject(task1.getProjectId())).thenReturn(Arrays.asList(task1));
		
		Response response = taskController.getTasksAssignedToProject(project);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertEquals(Arrays.asList(task1), response.getData());
		Assert.assertNull(response.getMessage());
		
		response = taskController.getTasksAssignedToProject(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(taskService.getTasksAssignedToProject(task1.getProjectId())).thenThrow(new RuntimeException("error"));
		
		response = taskController.getTasksAssignedToProject(project);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
	}
	
	@Test
	public void addTask() {

		Task task = new Task();
		task.setName("Task 1");
		task.setPriority(0);
		task.setStartDate(new Date());
		task.setEndDate(new Date());
		task.setProjectId(1);
		task.setUserId(1);
		
		Mockito.when(taskService.isTaskNameExists(task.getName())).thenReturn(false);
		Mockito.when(projectService.isProjectIdExists(task.getProjectId())).thenReturn(true);
		Mockito.when(taskService.isTaskIdExists(task.getParentTaskId())).thenReturn(true);
		Mockito.when(userService.isUserIdExists(task.getUserId())).thenReturn(true);
		Mockito.when(taskService.saveTask(task)).thenReturn(true);
		
		Response response = taskController.addTask(task);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(taskService.saveTask(task)).thenReturn(false);
		
		response = taskController.addTask(task);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(taskService.saveTask(task)).thenThrow(new RuntimeException("error"));
		
		response = taskController.addTask(task);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = taskController.addTask(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(taskService.isTaskNameExists(task.getName())).thenReturn(true);
		
		response = taskController.addTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Another task with name 'Task 1' already exists.", response.getMessage());
		
		Mockito.when(taskService.isTaskNameExists(task.getName())).thenReturn(false);
		Mockito.when(projectService.isProjectIdExists(task.getProjectId())).thenReturn(false);
		
		response = taskController.addTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Project does not exist.", response.getMessage());
		
		task.setSetParentTask(true);
		task.setParentTaskId(2);
		
		Mockito.when(taskService.isTaskNameExists(task.getName())).thenReturn(false);
		Mockito.when(projectService.isProjectIdExists(task.getProjectId())).thenReturn(true);
		Mockito.when(taskService.isTaskIdExists(task.getParentTaskId())).thenReturn(false);
		
		response = taskController.addTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Parent task does not exist.", response.getMessage());
		
		task.setSetParentTask(false);
		
		Mockito.when(taskService.isTaskNameExists(task.getName())).thenReturn(false);
		Mockito.when(projectService.isProjectIdExists(task.getProjectId())).thenReturn(true);
		Mockito.when(userService.isUserIdExists(task.getUserId())).thenReturn(false);
		
		response = taskController.addTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("User does not exist.", response.getMessage());
	}
	
	@Test
	public void updateTask() {

		Task task = new Task();
		task.setId(1);
		task.setName("Task 1");
		task.setPriority(0);
		task.setStartDate(new Date());
		task.setEndDate(new Date());
		task.setProjectId(1);
		task.setUserId(1);
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(true);
		Mockito.when(taskService.isTaskNameExists(task.getName(), task.getId())).thenReturn(false);
		Mockito.when(projectService.isProjectIdExists(task.getProjectId())).thenReturn(true);
		Mockito.when(userService.isUserIdExists(task.getUserId())).thenReturn(true);
		Mockito.when(taskService.saveTask(task)).thenReturn(true);
		
		Response response = taskController.updateTask(task);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(taskService.saveTask(task)).thenReturn(false);
		
		response = taskController.updateTask(task);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(taskService.saveTask(task)).thenThrow(new RuntimeException("error"));
		
		response = taskController.updateTask(task);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = taskController.updateTask(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(false);
		
		response = taskController.updateTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Task does not exist.", response.getMessage());
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(true);
		Mockito.when(taskService.isTaskNameExists(task.getName(), task.getId())).thenReturn(true);
		
		response = taskController.updateTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Another task with name 'Task 1' already exists.", response.getMessage());
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(true);
		Mockito.when(taskService.isTaskNameExists(task.getName(), task.getId())).thenReturn(false);
		Mockito.when(taskService.isTaskNameExists(task.getName())).thenReturn(false);
		Mockito.when(projectService.isProjectIdExists(task.getProjectId())).thenReturn(false);
		
		response = taskController.updateTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Project does not exist.", response.getMessage());
		
		task.setSetParentTask(true);
		task.setParentTaskId(2);
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(true);
		Mockito.when(taskService.isTaskNameExists(task.getName(), task.getId())).thenReturn(false);
		Mockito.when(taskService.isTaskNameExists(task.getName())).thenReturn(false);
		Mockito.when(projectService.isProjectIdExists(task.getProjectId())).thenReturn(true);
		Mockito.when(taskService.isTaskIdExists(task.getParentTaskId())).thenReturn(false);
		
		response = taskController.updateTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Parent task does not exist.", response.getMessage());
		
		task.setSetParentTask(false);
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(true);
		Mockito.when(taskService.isTaskNameExists(task.getName(), task.getId())).thenReturn(false);
		Mockito.when(taskService.isTaskNameExists(task.getName())).thenReturn(false);
		Mockito.when(projectService.isProjectIdExists(task.getProjectId())).thenReturn(true);
		Mockito.when(userService.isUserIdExists(task.getUserId())).thenReturn(false);
		
		response = taskController.updateTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("User does not exist.", response.getMessage());
	}
	
	@Test
	public void deleteTask() {
		
		Task task = new Task();
		task.setId(1);
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(true);
		Mockito.doNothing().when(taskService).deleteTask(task.getId());
		
		Response response = taskController.deleteTask(task);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.doThrow(new RuntimeException("error")).when(taskService).deleteTask(task.getId());
		
		response = taskController.deleteTask(task);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = taskController.deleteTask(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(false);
		
		response = taskController.deleteTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Task does not exist.", response.getMessage());
	}
	
	@Test
	public void searchTasks() {
		
		Task task1 = new Task();
		task1.setId(1);
		task1.setName("Task 1");
		task1.setPriority(0);
		task1.setStartDate(new Date());
		task1.setEndDate(new Date());
		task1.setProjectId(1);
		task1.setUserId(1);
		
		Task task2 = new Task();
		task2.setId(2);
		task2.setName("Task 2");
		task2.setPriority(0);
		task2.setStartDate(new Date());
		task2.setEndDate(new Date());
		task2.setProjectId(1);
		task2.setUserId(1);

		List<Task> taskList = Arrays.asList(task1, task2);
		
		Search search = new Search();
		search.setName("Task");
		
		Mockito.when(taskService.searchTasks(search.getName())).thenReturn(taskList);
		
		Response response = taskController.searchTasks(search);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertEquals(taskList, response.getData());
		Assert.assertNull(response.getMessage());
		
		response = taskController.searchTasks(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(taskService.searchTasks(search.getName())).thenThrow(new RuntimeException("error"));
		
		response = taskController.searchTasks(search);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
	}
	
	@Test
	public void endTask() {
		
		Task task = new Task();
		task.setId(1);
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(true);
		Mockito.when(taskService.endTask(task.getId())).thenReturn(true);
		
		Response response = taskController.endTask(task);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(taskService.endTask(task.getId())).thenReturn(false);
		
		response = taskController.endTask(task);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.doThrow(new RuntimeException("error")).when(taskService).endTask(task.getId());
		
		response = taskController.endTask(task);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = taskController.endTask(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(taskService.isTaskIdExists(task.getId())).thenReturn(false);
		
		response = taskController.endTask(task);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Task does not exist.", response.getMessage());
	}
}
