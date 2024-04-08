package dev.francisbernas.taskmasterserver.service;

import dev.francisbernas.taskmasterserver.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    List<ProjectDto> getAllProjects();

    List<ProjectDto> getAllProjectsByUserId(Long userId);

    ProjectDto getProjectById(Long projectId);

    ProjectDto createProject(ProjectDto projectDto);

    boolean softDeleteProjectById(Long projectId);

    ProjectDto updateProjectById(Long projectId, ProjectDto projectDto);
}
