package pma.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pma.business.service.UserService;
import pma.model.ui.Response;
import pma.model.ui.Search;
import pma.model.ui.User;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(path="/getUsers")
	public Response getUsers() {
		
		try {
			List<User> userList = userService.getUsers();
			return new Response<>(Response.SUCCESS, userList, null);
		} catch (Exception e) {
			return new Response<>(Response.EXCEPTION, null, e.getMessage());
		}
	}
	
	@PostMapping("/addUser")
	public Response addUser(@RequestBody User user) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (user == null 
					|| user.getId() != null 
					|| StringUtils.isBlank(user.getFirstName()) 
					|| StringUtils.isBlank(user.getLastName()) 
					|| user.getEmpId() == null
					|| !NumberUtils.isDigits(user.getEmpId().toString())
					) {
				errorMsg = "Invalid input.";
			} else if (userService.isEmpIdExists(user.getEmpId())) {
				errorMsg = "Another user with emp id '" + user.getEmpId() + "' already exists.";
			}
			
			if (errorMsg == null) {
				boolean isAdded = userService.saveUser(user);
				status = isAdded ? Response.SUCCESS : Response.EXCEPTION;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
	
	@PostMapping("/updateUser")
	public Response updateUser(@RequestBody User user) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (user == null 
					|| user.getId() == null 
					|| StringUtils.isBlank(user.getFirstName()) 
					|| StringUtils.isBlank(user.getLastName()) 
					|| user.getEmpId() == null 
					|| !NumberUtils.isDigits(user.getEmpId().toString())
					) {
				errorMsg = "Invalid input.";
			} else if (!userService.isUserIdExists(user.getId())) {
				errorMsg = "User does not exist.";	
			} else if (userService.isEmpIdExists(user.getEmpId(), user.getId())) {
				errorMsg = "Another user with emp id '" + user.getEmpId() + "' already exists.";
			}
			
			if (errorMsg == null) {
				boolean isUpdated = userService.saveUser(user);
				status = isUpdated ? Response.SUCCESS : Response.EXCEPTION;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
	
	@PostMapping("/deleteUser")
	public Response deleteUser(@RequestBody User user) {

		int status = Response.BAD_REQUEST;
		String errorMsg = null;
		
		try {
			if (user == null 
					|| user.getId() == null 
					|| !NumberUtils.isDigits(user.getId().toString())
					) {
				errorMsg = "Invalid input.";
			} else if (!userService.isUserIdExists(user.getId())) {
				errorMsg = "User does not exist.";	
			} else if (userService.isUserAssignedToAnyProject(user.getId())) {
				errorMsg = "User assigned to one or more project.";
			} else if (userService.isUserAssignedToAnyTask(user.getId())) {
				errorMsg = "User assigned to one or more task.";
			}
			
			if (errorMsg == null) {
				userService.deleteUser(user.getId());
				status = Response.SUCCESS;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, null, errorMsg);
	}
	
	@PostMapping("/searchUsers")
	public Response searchUsers(@RequestBody Search search) {
		
		int status = Response.BAD_REQUEST;
		List<User> userList = null;
		String errorMsg = null;
		
		try {
			if (search == null 
					|| StringUtils.isBlank(search.getName())) {
				errorMsg = "Invalid input.";
			}
			
			if (errorMsg == null) {
				userList = userService.searchUsers(search.getName());
				status = Response.SUCCESS;
			}
		} catch (Exception e) {
			status = Response.EXCEPTION;
			errorMsg = e.getMessage();
		}
		
		return new Response<>(status, userList, errorMsg);
	}
}
