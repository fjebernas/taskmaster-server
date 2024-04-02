package dev.francisbernas.taskmasterserver.mapper;

import dev.francisbernas.taskmasterserver.dto.ProjectDto;
import dev.francisbernas.taskmasterserver.entity.Project;

import java.util.List;

public class ProjectMapper {
    public static ProjectDto mapEntityToDto(Project projectEntity) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(projectEntity.getId());
        projectDto.setName(projectEntity.getName());
        projectDto.setDescription(projectEntity.getDescription());
        projectDto.setUserDto(UserMapper.mapEntityToDto(projectEntity.getUser()));
        return projectDto;
    }

    public static List<ProjectDto> mapEntitiesToDtos(List<Project> projectEntities) {
        return projectEntities.stream().map(ProjectMapper::mapEntityToDto).toList();
    }

    public static Project mapDtoToEntity(ProjectDto projectDto) {
        Project project = new Project();
        project.setId(projectDto.getId());
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setUser(UserMapper.mapDtoToEntity(projectDto.getUserDto()));
        return project;
    }
}
