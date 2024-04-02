package dev.francisbernas.taskmasterserver.controller;

import dev.francisbernas.taskmasterserver.dto.ProjectDto;
import dev.francisbernas.taskmasterserver.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("${serverBaseUrl}")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        log.info("Retrieving all projects");
        List<ProjectDto> projectDtos = projectService.getAllProjects();
        log.info("Retrieved {} projects", projectDtos.size());
        return new ResponseEntity<>(projectDtos, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<List<ProjectDto>> getAllProjectsByUserId(@PathVariable Long userId) {
        log.info("Retrieving all projects");
        List<ProjectDto> projectDtos = projectService.getAllProjectsByUserId(userId);
        log.info("Retrieved {} projects", projectDtos.size());
        return new ResponseEntity<>(projectDtos, HttpStatus.OK);
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long projectId) {
        log.info("Attempting to retrieve project with id {}", projectId);
        ProjectDto projectDto = projectService.getProjectById(projectId);
        if (projectDto != null) {
            log.info("Successfully retrieved project with id {}", projectId);
            return new ResponseEntity<>(projectDto, HttpStatus.OK);
        } else {
            log.info("Failed to retrieve project with id {}", projectId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectDto> createUser(@RequestBody ProjectDto projectDto) {
        log.info("Creating project: {}", projectDto);
        ProjectDto createdProject = projectService.createProject(projectDto);
        log.info("Project created: {}", createdProject);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<?> deleteProjectById(@PathVariable Long projectId) {
        log.info("Attempting to delete project with id {}", projectId);
        boolean isDeleted = projectService.deleteProjectById(projectId);
        if (isDeleted) {
            log.info("Project with id {} deleted successfully", projectId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.info("Project with id {} not found, deletion failed", projectId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectDto> updateProjectById(@PathVariable Long projectId, @RequestBody ProjectDto projectDto) {
        log.info("Updating project with id {}: {}", projectId, projectDto);
        ProjectDto updatedProjectDto = projectService.updateProjectById(projectId, projectDto);
        log.info("User with id {} updated successfully: {}", projectId, updatedProjectDto);
        return new ResponseEntity<>(updatedProjectDto, HttpStatus.OK);
    }
}
