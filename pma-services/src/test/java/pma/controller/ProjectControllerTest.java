package pma.controller;

import java.util.Arrays;
import java.util.Collections;
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
import pma.business.service.impl.UserServiceImpl;
import pma.model.ui.Project;
import pma.model.ui.Response;
import pma.model.ui.Search;

@RunWith(JUnit4.class)
public class ProjectControllerTest {

	private ProjectController projectController;
	private UserServiceImpl userService;
	private ProjectServiceImpl projectService;
	
	@Before
	public void setup() {
		
		projectController = new ProjectController();
		userService = Mockito.mock(UserServiceImpl.class);
		projectService = Mockito.mock(ProjectServiceImpl.class);
		
		Whitebox.setInternalState(projectController, "userService", userService);
		Whitebox.setInternalState(projectController, "projectService", projectService);
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getProjects() throws Exception {
		
		Project project = new Project();
		project.setId(1);
		project.setName("Project 1");
		project.setPriority(0);
		project.setManagerId(1);
		
		List<Project> projectList = Collections.singletonList(project);
		
		Mockito.when(projectService.getProjects()).thenReturn(projectList);
		Response response = projectController.getProjects();
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertEquals(projectList, response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(projectService.getProjects()).thenThrow(new RuntimeException("error"));
		
		response = projectController.getProjects();
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
	}
	
	@Test
	public void addProject() {

		Project project = new Project();
		project.setName("Project 1");
		project.setPriority(0);
		project.setManagerId(1);
		
		Mockito.when(projectService.isProjectNameExists(project.getName())).thenReturn(false);
		Mockito.when(userService.isUserIdExists(project.getManagerId())).thenReturn(true);
		Mockito.when(projectService.saveProject(project)).thenReturn(true);
		
		Response response = projectController.addProject(project);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(projectService.saveProject(project)).thenReturn(false);
		
		response = projectController.addProject(project);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(projectService.saveProject(project)).thenThrow(new RuntimeException("error"));
		
		response = projectController.addProject(project);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = projectController.addProject(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(projectService.isProjectNameExists(project.getName())).thenReturn(true);
		
		response = projectController.addProject(project);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Another project with name 'Project 1' already exists.", response.getMessage());
		
		Mockito.when(projectService.isProjectNameExists(project.getName())).thenReturn(false);
		Mockito.when(userService.isUserIdExists(project.getManagerId())).thenReturn(false);
		
		response = projectController.addProject(project);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Manager does not exist.", response.getMessage());
	}
	
	@Test
	public void updateProject() {
		
		Project project = new Project();
		project.setId(1);
		project.setName("Project 1");
		project.setPriority(0);
		project.setManagerId(1);
		
		Mockito.when(projectService.isProjectIdExists(project.getId())).thenReturn(true);
		Mockito.when(projectService.isProjectNameExists(project.getName(), project.getId())).thenReturn(false);
		Mockito.when(userService.isUserIdExists(project.getManagerId())).thenReturn(true);
		Mockito.when(projectService.saveProject(project)).thenReturn(true);
		
		Response response = projectController.updateProject(project);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(projectService.saveProject(project)).thenReturn(false);
		
		response = projectController.updateProject(project);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(projectService.saveProject(project)).thenThrow(new RuntimeException("error"));
		
		response = projectController.updateProject(project);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = projectController.updateProject(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(projectService.isProjectIdExists(project.getId())).thenReturn(false);
		
		response = projectController.updateProject(project);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Project does not exist.", response.getMessage());
		
		Mockito.when(projectService.isProjectIdExists(project.getId())).thenReturn(true);
		Mockito.when(projectService.isProjectNameExists(project.getName(), project.getId())).thenReturn(true);
		
		response = projectController.updateProject(project);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Another project with name 'Project 1' already exists.", response.getMessage());
		
		Mockito.when(projectService.isProjectIdExists(project.getId())).thenReturn(true);
		Mockito.when(projectService.isProjectNameExists(project.getName(), project.getId())).thenReturn(false);
		Mockito.when(userService.isUserIdExists(project.getManagerId())).thenReturn(false);
		
		response = projectController.updateProject(project);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Manager does not exist.", response.getMessage());
	}
	
	@Test
	public void deleteProject() {
		
		Project project = new Project();
		project.setId(1);
		
		Mockito.when(projectService.isProjectIdExists(project.getId())).thenReturn(true);
		Mockito.when(projectService.isProjectHasAnyTaskAssigned(project.getId())).thenReturn(false);
		Mockito.doNothing().when(projectService).deleteProject(project.getId());
		
		Response response = projectController.deleteProject(project);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.doThrow(new RuntimeException("error")).when(projectService).deleteProject(project.getId());
		
		response = projectController.deleteProject(project);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = projectController.deleteProject(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(projectService.isProjectIdExists(project.getId())).thenReturn(false);
		
		response = projectController.deleteProject(project);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Project does not exist.", response.getMessage());
		
		Mockito.when(projectService.isProjectIdExists(project.getId())).thenReturn(true);
		Mockito.when(projectService.isProjectHasAnyTaskAssigned(project.getId())).thenReturn(true);
		
		response = projectController.deleteProject(project);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Project has one or more task associated with it.", response.getMessage());
	}
	
	@Test
	public void searchProjects() {
		
		Project project1 = new Project();
		project1.setId(1);
		project1.setName("Project 1");
		project1.setPriority(0);
		project1.setManagerId(1);
		
		Project project2 = new Project();
		project2.setId(2);
		project2.setName("Project 2");
		project2.setPriority(0);
		project2.setManagerId(1);

		List<Project> projectList = Arrays.asList(project1, project2);
		
		Search search = new Search();
		search.setName("Project");
		
		Mockito.when(projectService.searchProjects(search.getName())).thenReturn(projectList);
		
		Response response = projectController.searchProjects(search);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertEquals(projectList, response.getData());
		Assert.assertNull(response.getMessage());
		
		response = projectController.searchProjects(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(projectService.searchProjects(search.getName())).thenThrow(new RuntimeException("error"));
		
		response = projectController.searchProjects(search);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
	}
	
	@Test
	public void endProject() {
		
		Project project = new Project();
		project.setId(1);
		
		Mockito.when(projectService.isProjectIdExists(project.getId())).thenReturn(true);
		Mockito.when(projectService.endProject(project.getId())).thenReturn(true);
		
		Response response = projectController.endProject(project);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(projectService.endProject(project.getId())).thenReturn(false);
		
		response = projectController.endProject(project);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.doThrow(new RuntimeException("error")).when(projectService).endProject(project.getId());
		
		response = projectController.endProject(project);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = projectController.endProject(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(projectService.isProjectIdExists(project.getId())).thenReturn(false);
		
		response = projectController.endProject(project);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Project does not exist.", response.getMessage());
	}
}
