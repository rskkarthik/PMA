package pma.business.service;

import java.util.ArrayList;
import java.util.Arrays;
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

import pma.business.service.impl.TaskServiceImpl;
import pma.dao.ProjectDao;
import pma.dao.TaskDao;
import pma.dao.UserDao;
import pma.dao.impl.ProjectDaoImpl;
import pma.dao.impl.TaskDaoImpl;
import pma.dao.impl.UserDaoImpl;
import pma.dao.util.TaskDataMapperUtil;
import pma.model.dao.ProjectEntity;
import pma.model.dao.TaskEntity;
import pma.model.dao.UserEntity;
import pma.model.ui.Project;
import pma.model.ui.Task;
import pma.model.ui.User;

@RunWith(JUnit4.class)
public class TaskServiceTest {

	private TaskService taskService;
	private TaskDao taskDao;
	private ProjectDao projectDao;
	private UserDao userDao;
	
	@Before
	public void setup() {
		
		taskService = new TaskServiceImpl();
		taskDao = Mockito.mock(TaskDaoImpl.class);
		projectDao = Mockito.mock(ProjectDaoImpl.class);
		userDao = Mockito.mock(UserDaoImpl.class);
		
		Whitebox.setInternalState(taskService, "taskDao", taskDao);
		Whitebox.setInternalState(taskService, "projectDao", projectDao);
		Whitebox.setInternalState(taskService, "userDao", userDao);
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getTasksAssignedToProject() throws Exception {
		
		TaskEntity task1 = new TaskEntity();
		task1.setId(1);
		task1.setName("Task 1");
		task1.setPriority(0);
		task1.setStartDate(new Date());
		task1.setEndDate(new Date());
		task1.setProjectId(1);
		task1.setUserId(1);
		
		TaskEntity task2 = new TaskEntity();
		task2.setId(2);
		task2.setName("Task 2");
		task2.setPriority(0);
		task2.setStartDate(new Date());
		task2.setEndDate(new Date());
		task2.setProjectId(2);
		task2.setUserId(2);
		
		List<TaskEntity> taskEntityList = Arrays.asList(task1, task2);
		
		Mockito.when(taskDao.getTasks()).thenReturn(taskEntityList);
		Mockito.when(projectDao.getProjects()).thenReturn(new ArrayList<>());
		Mockito.when(userDao.getUsers()).thenReturn(new ArrayList<>());
		
		List<Task> taskAssignedToProject = taskService.getTasksAssignedToProject(1);
		Assert.assertEquals(task1.getId(), taskAssignedToProject.get(0).getId());
		
		taskAssignedToProject = taskService.getTasksAssignedToProject(3);
		Assert.assertFalse(taskAssignedToProject.iterator().hasNext());
	}
	
	@Test
	public void isTaskIdExists() {
		
		TaskEntity task1 = new TaskEntity();
		task1.setId(1);
		task1.setName("Task 1");
		task1.setPriority(0);
		task1.setStartDate(new Date());
		task1.setEndDate(new Date());
		task1.setProjectId(1);
		task1.setUserId(1);
		
		TaskEntity task2 = new TaskEntity();
		task2.setId(2);
		task2.setName("Task 2");
		task2.setPriority(0);
		task2.setStartDate(new Date());
		task2.setEndDate(new Date());
		task2.setProjectId(2);
		task2.setUserId(2);
		
		List<TaskEntity> taskList = Arrays.asList(task1, task2);
		
		Mockito.when(taskDao.getTasks()).thenReturn(taskList);
		
		boolean response = taskService.isTaskIdExists(task1.getId());
		Assert.assertTrue(response);
		
		response = taskService.isTaskIdExists(3);
		Assert.assertFalse(response);
		
		response = taskService.isTaskIdExists(task1.getId(), 2);
		Assert.assertTrue(response);
		
		response = taskService.isTaskIdExists(task1.getId(), task1.getId());
		Assert.assertFalse(response);
	}
	
	@Test
	public void isTaskNameExists() {
		
		TaskEntity task1 = new TaskEntity();
		task1.setId(1);
		task1.setName("Task 1");
		task1.setPriority(0);
		task1.setStartDate(new Date());
		task1.setEndDate(new Date());
		task1.setProjectId(1);
		task1.setUserId(1);
		
		TaskEntity task2 = new TaskEntity();
		task2.setId(2);
		task2.setName("Task 2");
		task2.setPriority(0);
		task2.setStartDate(new Date());
		task2.setEndDate(new Date());
		task2.setProjectId(2);
		task2.setUserId(2);
		
		List<TaskEntity> taskList = Arrays.asList(task1, task2);
		
		Mockito.when(taskDao.getTasks()).thenReturn(taskList);
		
		boolean response = taskService.isTaskNameExists(task1.getName());
		Assert.assertTrue(response);
		
		response = taskService.isTaskNameExists("Task 3");
		Assert.assertFalse(response);
		
		response = taskService.isTaskNameExists(task2.getName(), task1.getId());
		Assert.assertTrue(response);
		
		response = taskService.isTaskNameExists(task1.getName(), task1.getId());
		Assert.assertFalse(response);
	}
	
	@Test
	public void searchTasks() {
		
		TaskEntity task1 = new TaskEntity();
		task1.setId(1);
		task1.setName("Task 1");
		task1.setPriority(0);
		task1.setStartDate(new Date());
		task1.setEndDate(new Date());
		task1.setProjectId(1);
		task1.setUserId(1);
		
		TaskEntity task2 = new TaskEntity();
		task2.setId(2);
		task2.setName("Task 2");
		task2.setPriority(0);
		task2.setStartDate(new Date());
		task2.setEndDate(new Date());
		task2.setProjectId(2);
		task2.setUserId(2);
		
		List<TaskEntity> taskList = Arrays.asList(task1, task2);
		
		Mockito.when(taskDao.getTasks()).thenReturn(taskList);
		
		List<Task> searchList = taskService.searchTasks("task");
		Assert.assertEquals(2, searchList.size());
		
		searchList = taskService.searchTasks("0");
		Assert.assertEquals(0, searchList.size());
		
		searchList = taskService.searchTasks("1");
		Assert.assertEquals(1, searchList.size());
	}
	
	@Test
	public void endProject() {
		
		TaskEntity task1 = new TaskEntity();
		task1.setId(1);
		task1.setName("Task 1");
		task1.setPriority(0);
		task1.setStartDate(new Date());
		task1.setEndDate(new Date());
		task1.setProjectId(1);
		task1.setUserId(1);
		
		Mockito.when(taskDao.getTask(task1.getId())).thenReturn(task1);
		Mockito.when(taskDao.saveTask(task1)).thenReturn(true);

		boolean response = taskService.endTask(task1.getId());
		
		Mockito.verify(taskDao).saveTask(task1);
		Assert.assertTrue(response);
	}
	
	@Test
	public void saveTask() {
		
		Task task = new Task();
		task.setId(1);
		task.setName("Task 1");
		task.setPriority(0);
		task.setStartDate(new Date());
		task.setEndDate(new Date());
		task.setProjectId(1);
		task.setUserId(1);
		
		Mockito.when(taskDao.saveTask(Mockito.any())).thenReturn(true);
		Assert.assertTrue(taskService.saveTask(task));
		Mockito.verify(taskDao).saveTask(Mockito.any());
	}
	
	@Test
	public void deleteTask() {
		
		Integer taskId = 0;
		Mockito.doNothing().when(taskDao).deleteTask(taskId);
		taskService.deleteTask(taskId);
		Mockito.verify(taskDao).deleteTask(taskId);
	}
	
	@Test
	public void getTask() {
		
		Integer taskId = 0;
		
		TaskEntity subTaskEntity = new TaskEntity();
		subTaskEntity.setId(1);
		subTaskEntity.setProjectId(1);
		subTaskEntity.setUserId(1);
		subTaskEntity.setParentTaskId(2);

		TaskEntity parentTaskEntity = new TaskEntity();
		parentTaskEntity.setId(subTaskEntity.getParentTaskId());
		parentTaskEntity.setName("Parent Task");
		
		ProjectEntity projectEntity = new ProjectEntity();
		projectEntity.setId(subTaskEntity.getProjectId());
		projectEntity.setName("Project");
		
		UserEntity userEntity = new UserEntity();
		userEntity.setId(subTaskEntity.getUserId());
		userEntity.setFirstName("First");
		userEntity.setLastName("Last");
		
		Mockito.when(taskDao.getTask(taskId)).thenReturn(subTaskEntity);
		Mockito.when(taskDao.getTasks()).thenReturn(Arrays.asList(parentTaskEntity));
		Mockito.when(projectDao.getProjects()).thenReturn(Arrays.asList(projectEntity));
		Mockito.when(userDao.getUsers()).thenReturn(Arrays.asList(userEntity));
		
		Task task = taskService.getTask(taskId);
		
		Assert.assertEquals(projectEntity.getName(), task.getProjectName());
		Assert.assertEquals(parentTaskEntity.getName(), task.getParentTaskName());
		Assert.assertEquals(userEntity.getFirstName() + " " + userEntity.getLastName(), task.getUserName());
	}
	
	@Test
	public void getSubtasksOfTask() {
		
		TaskEntity task1 = new TaskEntity();
		task1.setId(1);
		
		TaskEntity task2 = new TaskEntity();
		task2.setId(2);
		task2.setParentTaskId(task1.getId());
		
		TaskEntity task3 = new TaskEntity();
		task3.setId(3);
		task3.setParentTaskId(task2.getId());
		
		TaskEntity task4 = new TaskEntity();
		task4.setId(4);
		
		List<TaskEntity> taskList = Arrays.asList(task1, task2, task3, task4);
		
		Mockito.when(taskDao.getTasks()).thenReturn(taskList);
		Mockito.when(projectDao.getProjects()).thenReturn(new ArrayList<>());
		Mockito.when(userDao.getUsers()).thenReturn(new ArrayList<>());
		
		List<Task> subtaskList = taskService.getSubtasksOfTask(task1.getId());
		
		Assert.assertEquals(1, subtaskList.size());
		Assert.assertEquals(task2.getId(), subtaskList.get(0).getId());
	}
}
