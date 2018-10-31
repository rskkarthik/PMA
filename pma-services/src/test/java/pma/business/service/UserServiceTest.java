package pma.business.service;

import static org.junit.Assert.assertTrue;

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
import pma.dao.UserDao;
import pma.dao.impl.UserDaoImpl;
import pma.model.dao.UserEntity;
import pma.model.ui.Project;
import pma.model.ui.Task;
import pma.model.ui.User;

@RunWith(JUnit4.class)
public class UserServiceTest {

	private UserService userService;
	private ProjectService projectService;
	private TaskService taskService;
	private UserDao userDao;
	
	@Before
	public void setup() {
		userService = new UserServiceImpl();
		projectService = Mockito.mock(ProjectServiceImpl.class);
		taskService = Mockito.mock(TaskServiceImpl.class);
		userDao = Mockito.mock(UserDaoImpl.class);
		
		Whitebox.setInternalState(userService, "projectService", projectService);
		Whitebox.setInternalState(userService, "taskService", taskService);
		Whitebox.setInternalState(userService, "userDao", userDao);
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void isEmpIdExists() throws Exception {
		
		UserEntity user1 = new UserEntity();
		user1.setId(1);
		user1.setFirstName("Karthik");
		user1.setLastName("Kanagaraj");
		user1.setEmpId(344818);
		
		UserEntity user2 = new UserEntity();
		user2.setId(2);
		user2.setFirstName("Dhisha");
		user2.setLastName("Karthik");
		user2.setEmpId(200444);

		List<UserEntity> userList = Arrays.asList(user1, user2);
		
		Mockito.when(userDao.getUsers()).thenReturn(userList);
		
		boolean response = userService.isEmpIdExists(user1.getEmpId());
		Assert.assertTrue(response);
		
		response = userService.isEmpIdExists(2);
		Assert.assertFalse(response);
		
		response = userService.isEmpIdExists(user2.getEmpId(), user1.getId());
		Assert.assertTrue(response);
		
		response = userService.isEmpIdExists(user2.getEmpId(), user2.getId());
		Assert.assertFalse(response);
	}
	
	@Test
	public void isUserAssignedToAnyProject() throws Exception {
		
		Project project = new Project();
		project.setId(1);
		project.setName("Project 1");
		project.setPriority(0);
		project.setManagerId(1);
		project.setSetDate(false);
		
		List<Project> projectList = Collections.singletonList(project);
		
		Mockito.when(projectService.getProjects()).thenReturn(projectList);
		
		boolean response = userService.isUserAssignedToAnyProject(project.getManagerId());
		Assert.assertTrue(response);
		
		response = userService.isUserAssignedToAnyProject(2);
		Assert.assertFalse(response);
	}
	
	@Test
	public void isUserAssignedToAnyTask() throws Exception {
		
		Task task = new Task();
		task.setId(1);
		task.setName("Task 1");
		task.setPriority(0);
		task.setStartDate(new Date());
		task.setEndDate(new Date());
		task.setProjectId(1);
		task.setUserId(1);
		task.setSetParentTask(false);
		
		List<Task> taskList = Collections.singletonList(task);
		
		Mockito.when(taskService.getTasks()).thenReturn(taskList);
		
		boolean response = userService.isUserAssignedToAnyTask(task.getUserId());
		Assert.assertTrue(response);
		
		response = userService.isUserAssignedToAnyTask(2);
		Assert.assertFalse(response);
	}
	
	@Test
	public void searchUsers() {
		
		UserEntity user1 = new UserEntity();
		user1.setId(1);
		user1.setFirstName("Karthik");
		user1.setLastName("Kanagaraj");
		user1.setEmpId(344818);
		
		UserEntity user2 = new UserEntity();
		user2.setId(2);
		user2.setFirstName("Dhisha");
		user2.setLastName("Karthik");
		user2.setEmpId(200444);

		List<UserEntity> userList = Arrays.asList(user1, user2);
		
		Mockito.when(userDao.getUsers()).thenReturn(userList);
		
		List<User> searchList = userService.searchUsers("karthik");
		
		Assert.assertEquals(2, searchList.size());
	}
	
	@Test
	public void saveUser() {
		
		User user = new User();
		user.setId(1);
		user.setFirstName("Karthik");
		user.setLastName("Kanagaraj");
		user.setEmpId(344818);
		
		Mockito.when(userDao.saveUser(Mockito.any())).thenReturn(true);
		Assert.assertTrue(userService.saveUser(user));
		Mockito.verify(userDao).saveUser(Mockito.any());
	}
	
	@Test
	public void deleteUser() {
		
		Integer userId = 0;
		Mockito.doNothing().when(userDao).deleteUser(userId);
		userService.deleteUser(userId);
		Mockito.verify(userDao).deleteUser(userId);
	}
	
	@Test
	public void isUserIdExists() {
		Integer userId = 0;
		Mockito.when(userDao.isUserIdExists(userId)).thenReturn(true);
		assertTrue(userService.isUserIdExists(userId));
		Mockito.verify(userDao).isUserIdExists(userId);
	}
}
