package pma.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pma.dao.UserDao;
import pma.dao.repo.UserRepository;
import pma.model.dao.UserEntity;

@Component
public class UserDaoImpl implements UserDao {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<UserEntity> getUsers() {
		
		Iterable<UserEntity> userEntityiterable = userRepository.findAll();
		List<UserEntity> userEntityList = new ArrayList<UserEntity>();
		userEntityiterable.forEach(userEntityList::add);
		
		return userEntityList;
	}
	
	@Override
	public boolean saveUser(UserEntity user) {
		return userRepository.save(user) != null;
	}
	
	@Override
	public void deleteUser(Integer userId) {
		userRepository.deleteById(userId);
	}
	
	@Override
	public boolean isUserIdExists(Integer userId) {
		return userRepository.existsById(userId);
	}
}
