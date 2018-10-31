package pma.dao;

import java.util.List;

import pma.model.dao.UserEntity;

public interface UserDao {
	
	public List<UserEntity> getUsers();
	
	public boolean saveUser(UserEntity user);
	
	public void deleteUser(Integer userId);
	
	public boolean isUserIdExists(Integer userId);
}
