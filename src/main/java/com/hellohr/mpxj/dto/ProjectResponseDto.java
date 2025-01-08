package com.hellohr.mpxj.dto;

import java.util.List;
import java.util.Map;

public class ProjectResponseDto {

    private String message;
    private List<Map<String, Object>> projectTask;

    // Constructor
    public ProjectResponseDto(String message, List<Map<String, Object>> projectTask) {
        this.message = message;
        this.projectTask = projectTask;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getProjectTask() {
        return projectTask;
    }

    public void setProjectHierarchy(List<Map<String, Object>> projectHierarchy) {
        this.projectTask = projectTask;
    }
}
