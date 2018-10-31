package pma.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pma.business.service.ProjectService;
import pma.business.service.TaskService;
import pma.business.service.UserService;
import pma.dao.UserDao;
import pma.dao.util.UserDataMapperUtil;
import pma.model.dao.UserEntity;
import pma.model.ui.Project;
import pma.model.ui.Task;
import pma.model.ui.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserDao userDao;

	@Override
	public List<User> getUsers() {
		List<UserEntity> userEntityList = userDao.getUsers();
		return UserDataMapperUtil.mapUserEntityToUIModel(userEntityList);
	}
	
	@Override
	public boolean saveUser(User user) {
		UserEntity userEntity = UserDataMapperUtil.mapUserUIToEntityModel(user);
		return userDao.saveUser(userEntity);
	}
	
	@Override
	public void deleteUser(Integer userId) {
		userDao.deleteUser(userId);
	}
	
	@Override
	public boolean isUserIdExists(Integer userId) {
		return userDao.isUserIdExists(userId);
	}
	
	@Override
	public boolean isEmpIdExists(Integer empId) {
		return isEmpIdExists(empId, null);
	}
	
	@Override
	public boolean isEmpIdExists(Integer empId, Integer currentUserIdToSkip) {
		
		List<User> userList = getUsers();
		
		for (User user : userList) {
			if ((currentUserIdToSkip == null || currentUserIdToSkip.compareTo(user.getId()) != 0) && empId.compareTo(user.getEmpId()) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isUserAssignedToAnyProject(Integer userId) {
		
		List<Project> projectList = projectService.getProjects();
		
		for (Project project : projectList) {
			if (userId.compareTo(project.getManagerId()) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isUserAssignedToAnyTask(Integer userId) {
		
		List<Task> taskList = taskService.getTasks();
		
		for (Task task : taskList) {
			if (userId.compareTo(task.getUserId()) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public List<User> searchUsers(String searchStr) {
		
		List<User> userList = getUsers();
		List<User> searchList = new ArrayList<User>();
		
		for (User user : userList) {
			
			List<String> searchTerms = new ArrayList<String>();
			searchTerms.add(user.getFirstName().toLowerCase());
			searchTerms.add(user.getLastName().toLowerCase());
			searchTerms.addAll(Arrays.asList(user.getFirstName().toLowerCase().split(" ")));
			searchTerms.addAll(Arrays.asList(user.getLastName().toLowerCase().split(" ")));
			
			if (searchTerms.contains(searchStr.toLowerCase())) {
				searchList.add(user);
			}
		}
		
		return searchList;
	}
}
