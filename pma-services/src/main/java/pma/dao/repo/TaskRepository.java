package pma.dao.repo;

import org.springframework.data.repository.CrudRepository;

import pma.model.dao.TaskEntity;

public interface TaskRepository extends CrudRepository<TaskEntity, Integer>{

}
