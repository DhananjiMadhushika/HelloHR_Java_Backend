package com.hellohr.mpxj.dto;

import java.util.List;
import java.util.Map;

public class ProjectResponseDto {

    private String message;
    private List<Map<String, Object>> projectHierarchy;

    // Constructor
    public ProjectResponseDto(String message, List<Map<String, Object>> projectHierarchy) {
        this.message = message;
        this.projectHierarchy = projectHierarchy;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getProjectHierarchy() {
        return projectHierarchy;
    }

    public void setProjectHierarchy(List<Map<String, Object>> projectHierarchy) {
        this.projectHierarchy = projectHierarchy;
    }
}
