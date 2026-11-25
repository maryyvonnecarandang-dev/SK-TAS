package com.skrrt.sktas.controller;

import com.skrrt.sktas.model.Project;
import com.skrrt.sktas.repository.ProjectRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }


// READ - Retrieve a specific project
@GetMapping("/{id}")
public Project getProjectById(@PathVariable Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Project not found"));
}

// UPDATE - Update an existing project
@PutMapping("/{id}")
public Project updateProject(@PathVariable Long id,
                             @RequestBody Project updatedProject) {

    Project project = getProjectById(id);

    project.setProjectName(updatedProject.getProjectName());
    project.setDescription(updatedProject.getDescription());
    project.setAllocatedBudget(updatedProject.getAllocatedBudget());
    project.setStatus(updatedProject.getStatus());

    return projectRepository.save(project);
}

// DELETE - Remove a project
@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteProject(@PathVariable Long id) {
    Project project = getProjectById(id);
    projectRepository.delete(project);
}


}
