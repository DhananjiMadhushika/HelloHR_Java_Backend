package com.hellohr.mpxj.dto;

import java.util.List;
import java.util.Map;

public class ProjectResponseDto {
    private String message;
    private String projectName;
    private String projectDescription;
    private String projectCode;
    private List<Map<String, Object>> projectTasks;

    public ProjectResponseDto(String message, String projectName, String projectDescription, String projectCode, List<Map<String, Object>> projectTasks) {
        this.message = message;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectCode = projectCode;
        this.projectTasks = projectTasks;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public List<Map<String, Object>> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(List<Map<String, Object>> projectTasks) {
        this.projectTasks = projectTasks;
    }
}
