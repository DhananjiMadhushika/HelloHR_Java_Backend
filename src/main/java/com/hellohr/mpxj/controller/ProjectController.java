package com.hellohr.mpxj.controller;

import com.hellohr.mpxj.dto.ErrorResponseDto;
import com.hellohr.mpxj.dto.ProjectResponseDto;
import com.hellohr.mpxj.service.MppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Object> uploadProject(@RequestParam("file") MultipartFile file) {
        try {
            // Create temporary file from the uploaded MPP file
            File tempFile = File.createTempFile("mpp", ".mpp");
            file.transferTo(tempFile);

            // Parse and save the MPP data
            List<Map<String, Object>> projectHierarchy = mppService.parseAndSaveMpp(tempFile.getAbsolutePath());

            // Prepare a response DTO with the project hierarchy
            ProjectResponseDto response = new ProjectResponseDto("File processed successfully.", projectHierarchy);

            // Return the hierarchical structure
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Return an error response with error message
            ErrorResponseDto errorResponse = new ErrorResponseDto("Error processing file", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
