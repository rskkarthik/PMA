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

import pma.business.service.UserService;
import pma.business.service.impl.UserServiceImpl;
import pma.model.ui.Response;
import pma.model.ui.Search;
import pma.model.ui.User;

@RunWith(JUnit4.class)
public class UserControllerTest {

	private UserController userController;
	private UserService userService;
	
	@Before
	public void setup() {
		
		userController = new UserController();
		userService = Mockito.mock(UserServiceImpl.class);
		
		Whitebox.setInternalState(userController, "userService", userService);
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getUsers() throws Exception {
		
		User user = new User();
		user.setId(1);
		user.setFirstName("Karthik");
		user.setLastName("Kanagaraj");
		user.setEmpId(344818);

		List<User> userList = Collections.singletonList(user);
		
		Mockito.when(userService.getUsers()).thenReturn(userList);
		
		Response response = userController.getUsers();
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertEquals(userList, response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(userService.getUsers()).thenThrow(new RuntimeException("error"));
		
		response = userController.getUsers();
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
	}
	
	@Test
	public void addUser() {

		User user = new User();
		user.setFirstName("Karthik");
		user.setLastName("Kanagaraj");
		user.setEmpId(344818);
		
		Mockito.when(userService.isEmpIdExists(user.getEmpId())).thenReturn(false);
		Mockito.when(userService.saveUser(user)).thenReturn(true);
		
		Response response = userController.addUser(user);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(userService.saveUser(user)).thenReturn(false);
		
		response = userController.addUser(user);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(userService.saveUser(user)).thenThrow(new RuntimeException("error"));
		
		response = userController.addUser(user);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = userController.addUser(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(userService.isEmpIdExists(user.getEmpId())).thenReturn(true);
		
		response = userController.addUser(user);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Another user with emp id '344818' already exists.", response.getMessage());
	}
	
	@Test
	public void updateUser() {
		
		User user = new User();
		user.setId(1);
		user.setFirstName("Karthik");
		user.setLastName("Kanagaraj");
		user.setEmpId(344818);
		
		Mockito.when(userService.isUserIdExists(user.getId())).thenReturn(true);
		Mockito.when(userService.isEmpIdExists(user.getEmpId(), user.getId())).thenReturn(false);
		Mockito.when(userService.saveUser(user)).thenReturn(true);
		
		Response response = userController.updateUser(user);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.when(userService.saveUser(user)).thenReturn(false);
		
		response = userController.updateUser(user);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());		
		
		Mockito.when(userService.saveUser(user)).thenThrow(new RuntimeException("error"));
		
		response = userController.updateUser(user);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = userController.updateUser(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(userService.isUserIdExists(user.getId())).thenReturn(false);
		
		response = userController.updateUser(user);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("User does not exist.", response.getMessage());
		
		Mockito.when(userService.isUserIdExists(user.getId())).thenReturn(true);
		Mockito.when(userService.isEmpIdExists(user.getEmpId(), user.getId())).thenReturn(true);
		
		response = userController.updateUser(user);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Another user with emp id '344818' already exists.", response.getMessage());
	}
	
	@Test
	public void deleteUser() {
		
		User user = new User();
		user.setId(1);
		
		Mockito.when(userService.isUserIdExists(user.getId())).thenReturn(true);
		Mockito.when(userService.isUserAssignedToAnyProject(user.getId())).thenReturn(false);
		Mockito.when(userService.isUserAssignedToAnyTask(user.getId())).thenReturn(false);
		
		Response response = userController.deleteUser(user);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertNull(response.getMessage());
		
		Mockito.doThrow(new RuntimeException("error")).when(userService).deleteUser(user.getId());
		
		response = userController.deleteUser(user);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
		
		response = userController.deleteUser(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(userService.isUserIdExists(user.getId())).thenReturn(false);
		
		response = userController.deleteUser(user);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("User does not exist.", response.getMessage());
		
		Mockito.when(userService.isUserIdExists(user.getId())).thenReturn(true);
		Mockito.when(userService.isUserAssignedToAnyProject(user.getId())).thenReturn(true);
		
		response = userController.deleteUser(user);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("User assigned to one or more project.", response.getMessage());
		
		Mockito.when(userService.isUserIdExists(user.getId())).thenReturn(true);
		Mockito.when(userService.isUserAssignedToAnyProject(user.getId())).thenReturn(false);
		Mockito.when(userService.isUserAssignedToAnyTask(user.getId())).thenReturn(true);
		
		response = userController.deleteUser(user);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("User assigned to one or more task.", response.getMessage());
	}
	
	@Test
	public void searchUsers() {
		
		User user1 = new User();
		user1.setId(1);
		user1.setFirstName("Karthik");
		user1.setLastName("Kanagaraj");
		user1.setEmpId(344818);
		
		User user2 = new User();
		user2.setId(1);
		user2.setFirstName("Dhisha");
		user2.setLastName("Karthik");
		user2.setEmpId(200444);

		List<User> userList = Arrays.asList(user1, user2);
		
		Search search = new Search();
		search.setName(user2.getFirstName());
		
		Mockito.when(userService.searchUsers(search.getName())).thenReturn(userList);
		
		Response response = userController.searchUsers(search);
		
		Assert.assertEquals(Response.SUCCESS, response.getStatus());
		Assert.assertEquals(userList, response.getData());
		Assert.assertNull(response.getMessage());
		
		response = userController.searchUsers(null);
		
		Assert.assertEquals(Response.BAD_REQUEST, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("Invalid input.", response.getMessage());
		
		Mockito.when(userService.searchUsers(search.getName())).thenThrow(new RuntimeException("error"));
		
		response = userController.searchUsers(search);
		
		Assert.assertEquals(Response.EXCEPTION, response.getStatus());
		Assert.assertNull(response.getData());
		Assert.assertEquals("error", response.getMessage());
	}
}
