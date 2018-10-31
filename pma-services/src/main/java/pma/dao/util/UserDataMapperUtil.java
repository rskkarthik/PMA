package pma.dao.util;

import java.util.ArrayList;
import java.util.List;

import pma.model.dao.UserEntity;
import pma.model.ui.User;

public class UserDataMapperUtil {

	public static List<User> mapUserEntityToUIModel(List<UserEntity> userEntityList) {
		
		List<User> userList = new ArrayList<User>();
		
		for (UserEntity userEntity : userEntityList) {
			
			User user = new User();
			
			user.setId(userEntity.getId());
			user.setFirstName(userEntity.getFirstName());
			user.setLastName(userEntity.getLastName());
			user.setEmpId(userEntity.getEmpId());
			
			userList.add(user);
		}
		
		return userList;
	}
	
	public static UserEntity mapUserUIToEntityModel(User user) {
		
		UserEntity userEntity = new UserEntity();
		
		userEntity.setId(user.getId());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setEmpId(user.getEmpId());
		
		return userEntity;
	}
}
