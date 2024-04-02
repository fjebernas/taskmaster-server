package dev.francisbernas.taskmasterserver.service.impl;

import dev.francisbernas.taskmasterserver.dto.ProjectDto;
import dev.francisbernas.taskmasterserver.entity.Project;
import dev.francisbernas.taskmasterserver.entity.User;
import dev.francisbernas.taskmasterserver.mapper.ProjectMapper;
import dev.francisbernas.taskmasterserver.repository.ProjectRepository;
import dev.francisbernas.taskmasterserver.repository.UserRepository;
import dev.francisbernas.taskmasterserver.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    @Override
    public List<ProjectDto> getAllProjects() {
        return ProjectMapper.mapEntitiesToDtos(projectRepository.findAllNotDeleted());
    }

    @Override
    public List<ProjectDto> getAllProjectsByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findByIdNotDeleted(userId);
        if (optionalUser.isPresent()) {
            return ProjectMapper.mapEntitiesToDtos(projectRepository.findAllByUserNotDeleted(optionalUser.get()));
        } else {
            throw new RuntimeException(String.format("User with id %s doesn't exist, failed to get projects", userId));
        }
    }

    @Override
    public ProjectDto getProjectById(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findByIdNotDeleted(projectId);
        return optionalProject.map(ProjectMapper::mapEntityToDto).orElse(null);
    }

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Optional<User> optionalUser = userRepository.findByIdNotDeleted(projectDto.getUserDto().getId());
        if (optionalUser.isPresent()) {
            Project projectEntity = ProjectMapper.mapDtoToEntity(projectDto);
            // TODO: dynamic createdBy field - need Spring Security for this
            projectEntity.setCreatedBy("admin");
            projectEntity.setCreatedDate(LocalDateTime.now());
            Project createdProject = projectRepository.save(projectEntity);
            return ProjectMapper.mapEntityToDto(createdProject);
        } else {
            throw new RuntimeException(String.format("User with id %s doesn't exist, failed to create project", projectDto.getUserDto().getId()));
        }
    }

    @Transactional
    @Override
    public boolean deleteProjectById(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findByIdNotDeleted(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            projectRepository.softDeleteById("admin", LocalDateTime.now(), project.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ProjectDto updateProjectById(Long projectId, ProjectDto projectDto) {
        Optional<Project> optionalProject = projectRepository.findByIdNotDeleted(projectId);
        if (optionalProject.isPresent()) {
            if (!projectId.equals(projectDto.getId())) {
                throw new RuntimeException(String.format("Project id %s doesn't match given path variable id %s", projectDto.getId(), projectId));
            }
            Project updatedProject = ProjectMapper.mapDtoToEntity(projectDto);
            // TODO: dynamic deletedBy field - need Spring Security for this
            updatedProject.setLastModifiedBy("admin");
            updatedProject.setLastModifiedDate(LocalDateTime.now());
            Project savedUpdatedProject = projectRepository.save(updatedProject);
            return ProjectMapper.mapEntityToDto(savedUpdatedProject);
        } else {
            throw new RuntimeException(String.format("Project with id %s doesn't exist", projectId));
        }
    }
}
