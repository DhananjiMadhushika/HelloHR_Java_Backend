package com.hellohr.mpxj.controller;

import com.hellohr.mpxj.dto.ErrorResponseDto;
import com.hellohr.mpxj.dto.ProjectResponseDto;
import com.hellohr.mpxj.entity.Project;
import com.hellohr.mpxj.service.MppService;
import com.hellohr.mpxj.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private MppService mppService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadProject(
            @RequestParam("name") String name,
            @RequestParam("code") String code,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file)
    {
        try {
            // Create temporary file from the uploaded MPP file
            File tempFile = File.createTempFile("mpp", ".mpp");
            file.transferTo(tempFile);

            // Parse and save the MPP data
            List<Map<String, Object>> projectTask = mppService.parseAndSaveMpp(tempFile.getAbsolutePath(), name, code, description);

            // Prepare a response DTO with the project hierarchy
            ProjectResponseDto response = new ProjectResponseDto(
                    "File processed successfully.",
                    name,
                    code,
                    description,
                    projectTask
            );
            // Return the hierarchical structure
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Return an error response with error message
            ErrorResponseDto errorResponse = new ErrorResponseDto("Error processing file", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private ProjectService projectService;

    @GetMapping("/details")
    public ResponseEntity<Object> getProjectByCode(@RequestParam("code") String code) {
        try {
            // Fetch project by code
            Project project = projectService.getProjectByCode(code);

            // Map project to response DTO
            ProjectResponseDto response = projectService.mapToResponseDto(project);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponseDto("Project not found", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponseDto("Error fetching project details", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
