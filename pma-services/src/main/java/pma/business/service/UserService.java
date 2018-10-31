package pma.business.service;

import java.util.List;

import pma.model.ui.User;

public interface UserService {

	public List<User> getUsers();
	
	public boolean saveUser(User user);
	
	public void deleteUser(Integer userId);
	
	public boolean isUserIdExists(Integer userId);
	
	public boolean isEmpIdExists(Integer empId);
	
	public boolean isEmpIdExists(Integer empId, Integer currentUserIdToSkip);
	
	public boolean isUserAssignedToAnyProject(Integer userId);
	
	public boolean isUserAssignedToAnyTask(Integer userId);
	
	public List<User> searchUsers(String searchStr);
}
