package pma.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pma.dao.ProjectDao;
import pma.dao.repo.ProjectRepository;
import pma.model.dao.ProjectEntity;

@Component
public class ProjectDaoImpl implements ProjectDao {
	
	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public List<ProjectEntity> getProjects() {
		
		Iterable<ProjectEntity> projectEntityiterable = projectRepository.findAll();
		List<ProjectEntity> projectEntityList = new ArrayList<ProjectEntity>();
		projectEntityiterable.forEach(projectEntityList::add);
		
		return projectEntityList;
	}
	
	@Override
	public ProjectEntity getProject(Integer projectId) {
		
		ProjectEntity projectEntity = null;
		Optional<ProjectEntity> projectEntityOpt = projectRepository.findById(projectId);
		
		if (projectEntityOpt.isPresent()) {
			projectEntity = projectEntityOpt.get();
		}
		
		return projectEntity;
	}
	
	@Override
	public boolean saveProject(ProjectEntity project) {
		return projectRepository.save(project) != null;
	}
	
	@Override
	public void deleteProject(Integer projectId) {
		projectRepository.deleteById(projectId);
	}
	
	@Override
	public boolean isProjectIdExists(Integer projectId) {
		return projectRepository.existsById(projectId);
	}
}
