package com.hellohr.mpxj.service;

import com.hellohr.mpxj.dto.ProjectResponseDto;
import com.hellohr.mpxj.entity.Project;
import com.hellohr.mpxj.entity.Resource;
import com.hellohr.mpxj.entity.Task;
import com.hellohr.mpxj.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project getProjectByCode(String code) {
        return projectRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Project with code " + code + " not found."));
    }

    public ProjectResponseDto mapToResponseDto(Project project) {
        // Collect root tasks only (tasks with no parent)
        List<Map<String, Object>> projectTasks = project.getTasks().stream()
                .filter(task -> task.getParentTask() == null) // Include only root tasks
                .map(this::mapTaskToResponse)
                .toList();

        return new ProjectResponseDto(
                "File processed successfully.",
                project.getName(),
                project.getDescription(),
                project.getCode(),
                projectTasks
        );
    }

    private Map<String, Object> mapTaskToResponse(Task task) {
        // Avoid creating duplicate entries
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("duration", task.getDuration());
        response.put("code", task.getCode());
        response.put("completePercentage", task.getComplete());
        response.put("parentTaskId", task.getParentTask() != null ? task.getParentTask().getId() : null);
        response.put("start", task.getStartTime());
        response.put("resources", task.getResources().stream().distinct().map(Resource::getName).toList());
        response.put("taskName", task.getName());
        response.put("finish", task.getEndTime());
        response.put("id", task.getId());
        response.put("predecessor", task.getPredecessorTask() != null ? task.getPredecessorTask().getName() : null);

        // Use a Set to ensure unique subtasks
        response.put("subtasks", task.getSubtasks().stream()
                .distinct()
                .map(this::mapTaskToResponse)
                .toList());
        return response;
    }



}
