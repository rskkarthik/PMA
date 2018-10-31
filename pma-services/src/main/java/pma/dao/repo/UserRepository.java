package pma.dao.repo;

import org.springframework.data.repository.CrudRepository;

import pma.model.dao.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer>{

}
