package pma.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pma.business.service.ProjectService;
import pma.business.service.UserService;
import pma.model.ui.Project;
import pma.model.ui.Response;
import pma.model.ui.Search;

@RestController
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping(path="/getProjects")
	public Response getProjects() {
		
		try {
			List<Project> projectList = projectService.getProjects();
			return new Response<>(Response.SUCCESS, projectList, null);
		} catch (Exception e) {
			return new Response<>(Response.EXCEPTION, null, e.getMessage());
		}
	}
	
	@PostMapping("/addProject")
	public Response addProject(@RequestBody Project project) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (project == null 
					|| project.getId() != null 
					|| StringUtils.isBlank(project.getName()) 
					|| project.getPriority() == null
					|| project.getManagerId() == null
					) {
				errorMsg = "Invalid input.";
			} else if (projectService.isProjectNameExists(project.getName())) {
				errorMsg = "Another project with name '" + project.getName() + "' already exists.";
			} else if (!userService.isUserIdExists(project.getManagerId())) {
				errorMsg = "Manager does not exist.";
			}
			
			if (errorMsg == null) {
				boolean isAdded = projectService.saveProject(project);
				status = isAdded ? Response.SUCCESS : Response.EXCEPTION;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
	
	@PostMapping("/updateProject")
	public Response updateProject(@RequestBody Project project) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (project == null 
					|| project.getId() == null 
					|| StringUtils.isBlank(project.getName()) 
					|| project.getPriority() == null
					|| project.getManagerId() == null
					) {
				errorMsg = "Invalid input.";
			} else if (!projectService.isProjectIdExists(project.getId())) {
				errorMsg = "Project does not exist.";
			} else if (projectService.isProjectNameExists(project.getName(), project.getId())) {
				errorMsg = "Another project with name '" + project.getName() + "' already exists.";
			} else if (!userService.isUserIdExists(project.getManagerId())) {
				errorMsg = "Manager does not exist.";
			}
			
			if (errorMsg == null) {
				boolean isUpdated = projectService.saveProject(project);
				status = isUpdated ? Response.SUCCESS : Response.EXCEPTION;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
	
	@PostMapping("/deleteProject")
	public Response deleteProject(@RequestBody Project project) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (project == null 
					|| project.getId() == null 
					) {
				errorMsg = "Invalid input.";
			} else if (!projectService.isProjectIdExists(project.getId())) {
				errorMsg = "Project does not exist.";
			} else if (projectService.isProjectHasAnyTaskAssigned(project.getId())) {
				errorMsg = "Project has one or more task associated with it.";
			}
			
			if (errorMsg == null) {
				projectService.deleteProject(project.getId());
				status = Response.SUCCESS;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
	
	@PostMapping("/searchProjects")
	public Response searchProjects(@RequestBody Search search) {
		
		int status = Response.BAD_REQUEST;
		List<Project> projectList = null;
		String errorMsg = null;
		
		try {
			if (search == null 
					|| StringUtils.isBlank(search.getName())) {
				errorMsg = "Invalid input.";
			}
			
			if (errorMsg == null) {
				projectList = projectService.searchProjects(search.getName());
				status = Response.SUCCESS;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, projectList, errorMsg);
	}
	
	@PostMapping("/endProject")
	public Response endProject(@RequestBody Project project) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (project == null 
					|| project.getId() == null 
					) {
				errorMsg = "Invalid input.";
			} else if (!projectService.isProjectIdExists(project.getId())) {
				errorMsg = "Project does not exist.";
			}
			
			if (errorMsg == null) {
				boolean isEnded = projectService.endProject(project.getId());
				status = isEnded ? Response.SUCCESS : Response.EXCEPTION;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
}
