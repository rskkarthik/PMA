package pma.dao.repo;

import org.springframework.data.repository.CrudRepository;

import pma.model.dao.ProjectEntity;

public interface ProjectRepository extends CrudRepository<ProjectEntity, Integer>{

}
