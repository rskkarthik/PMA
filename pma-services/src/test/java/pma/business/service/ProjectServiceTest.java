package pma.business.service;

import java.util.ArrayList;
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
import pma.dao.ProjectDao;
import pma.dao.impl.ProjectDaoImpl;
import pma.model.dao.ProjectEntity;
import pma.model.ui.Project;
import pma.model.ui.Task;
import pma.model.ui.User;

@RunWith(JUnit4.class)
public class ProjectServiceTest {

	private ProjectService projectService;
	private UserService userService;
	private TaskService taskService;
	private ProjectDao projectDao;
	
	@Before
	public void setup() {
		projectService = new ProjectServiceImpl();
		userService = Mockito.mock(UserServiceImpl.class);
		taskService = Mockito.mock(TaskServiceImpl.class);
		projectDao = Mockito.mock(ProjectDaoImpl.class);
		
		Whitebox.setInternalState(projectService, "taskService", taskService);
		Whitebox.setInternalState(projectService, "userService", userService);
		Whitebox.setInternalState(projectService, "projectDao", projectDao);
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void isProjectIdExists() throws Exception {
		
		ProjectEntity project = new ProjectEntity();
		project.setId(1);
		project.setName("Project 1");
		project.setPriority(0);
		project.setManagerId(1);
		
		List<ProjectEntity> projectList = Collections.singletonList(project);
		
		Mockito.when(userService.getUsers()).thenReturn(new ArrayList<User>());
		Mockito.when(projectDao.getProjects()).thenReturn(projectList);
		
		boolean response = projectService.isProjectIdExists(project.getId());
		Assert.assertTrue(response);
		
		response = projectService.isProjectIdExists(2);
		Assert.assertFalse(response);
	}
	
	@Test
	public void isProjectNameExists() throws Exception {
		
		ProjectEntity project1 = new ProjectEntity();
		project1.setId(1);
		project1.setName("Project 1");
		project1.setPriority(0);
		project1.setManagerId(1);
		
		ProjectEntity project2 = new ProjectEntity();
		project2.setId(2);
		project2.setName("Project 2");
		project2.setPriority(0);
		project2.setManagerId(2);
		
		List<ProjectEntity> projectList = Arrays.asList(project1, project2);
		
		Mockito.when(userService.getUsers()).thenReturn(new ArrayList<User>());
		Mockito.when(projectDao.getProjects()).thenReturn(projectList);
		
		boolean response = projectService.isProjectNameExists(project1.getName());
		Assert.assertTrue(response);
		
		response = projectService.isProjectNameExists("Project 3");
		Assert.assertFalse(response);
		
		response = projectService.isProjectNameExists("Project 2", project1.getId());
		Assert.assertTrue(response);
		
		response = projectService.isProjectNameExists("Project 2", project2.getId());
		Assert.assertFalse(response);
	}
	
	@Test
	public void isProjectHasAnyTaskAssigned() {
		
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
		
		boolean response = projectService.isProjectHasAnyTaskAssigned(task.getProjectId());
		Assert.assertTrue(response);
		
		response = projectService.isProjectHasAnyTaskAssigned(2);
		Assert.assertFalse(response);
	}
	
	@Test
	public void searchProjects() {
		
		ProjectEntity project1 = new ProjectEntity();
		project1.setId(1);
		project1.setName("Project 1");
		project1.setPriority(0);
		project1.setManagerId(1);
		
		ProjectEntity project2 = new ProjectEntity();
		project2.setId(2);
		project2.setName("Project 2");
		project2.setPriority(0);
		project2.setManagerId(2);
		
		List<ProjectEntity> projectList = Arrays.asList(project1, project2);
		
		Mockito.when(userService.getUsers()).thenReturn(new ArrayList<User>());
		Mockito.when(projectDao.getProjects()).thenReturn(projectList);
		
		List<Project> searchList = projectService.searchProjects("project");
		Assert.assertEquals(2, searchList.size());
		
		searchList = projectService.searchProjects("0");
		Assert.assertEquals(0, searchList.size());
		
		searchList = projectService.searchProjects("1");
		Assert.assertEquals(1, searchList.size());
	}
	
	@Test
	public void endProject() {
		
		ProjectEntity project1 = new ProjectEntity();
		project1.setId(1);
		project1.setName("Project 1");
		project1.setPriority(0);
		project1.setManagerId(1);
		project1.setActive(true);
		
		Mockito.when(projectDao.getProject(project1.getId())).thenReturn(project1);
		Mockito.when(projectDao.saveProject(project1)).thenReturn(true);

		boolean response = projectService.endProject(project1.getId());
		
		Mockito.verify(projectDao).saveProject(project1);
		Assert.assertTrue(response);
	}
	
	@Test
	public void saveProject() {
		
		Project project = new Project();
		project.setId(1);
		project.setName("Project 1");
		
		Mockito.when(projectDao.saveProject(Mockito.any())).thenReturn(true);
		Assert.assertTrue(projectService.saveProject(project));
		Mockito.verify(projectDao).saveProject(Mockito.any());
	}
	
	@Test
	public void deleteUser() {
		
		Integer projectId = 0;
		Mockito.doNothing().when(projectDao).deleteProject(projectId);
		projectService.deleteProject(projectId);
		Mockito.verify(projectDao).deleteProject(projectId);
	}
}
