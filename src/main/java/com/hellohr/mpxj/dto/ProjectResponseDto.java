package com.hellohr.mpxj.dto;

import java.util.List;

public class ProjectResponseDto {

    private String message;
    private List<String> projectAndTaskNames;

    // Constructor
    public ProjectResponseDto(String message, List<String> projectAndTaskNames) {
        this.message = message;
        this.projectAndTaskNames = projectAndTaskNames;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getProjectAndTaskNames() {
        return projectAndTaskNames;
    }

    public void setProjectAndTaskNames(List<String> projectAndTaskNames) {
        this.projectAndTaskNames = projectAndTaskNames;
    }
}
