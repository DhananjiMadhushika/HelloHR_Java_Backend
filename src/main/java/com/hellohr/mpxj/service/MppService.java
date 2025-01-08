package com.hellohr.mpxj.service;

import com.hellohr.mpxj.entity.Project;
import com.hellohr.mpxj.entity.Task;
import com.hellohr.mpxj.repository.ProjectRepository;
import com.hellohr.mpxj.repository.TaskRepository;
import net.sf.mpxj.*;
import net.sf.mpxj.mpp.MPPReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

@Service
public class MppService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Map<String, Object>> parseAndSaveMpp(String filePath) throws Exception {
        MPPReader reader = new MPPReader();
        ProjectFile projectFile = reader.read(filePath);

        // Save project details
        Project project = new Project();
        String projectName = projectFile.getProjectProperties().getProjectTitle();
        if (projectName != null) {
            project.setName(projectName);
        }
        project.setStartDate(projectFile.getProjectProperties().getStartDate());
        project.setEndDate(projectFile.getProjectProperties().getFinishDate());
        projectRepository.save(project);

        // Map to hold tasks by their IDs for parent-child relationships
        Map<Integer, Task> taskMap = new HashMap<>();
        List<Map<String, Object>> projectHierarchy = new ArrayList<>();

        // Recursive method to build the task hierarchy
        for (net.sf.mpxj.Task mpxjTask : projectFile.getChildTasks()) {
            Map<String, Object> taskHierarchy = processTask(mpxjTask, project, taskMap, null); // Pass null as parentTask
            projectHierarchy.add(taskHierarchy);
        }


        return projectHierarchy;
    }

    private Map<String, Object> processTask(net.sf.mpxj.Task mpxjTask, Project project, Map<Integer, Task> taskMap, Task parentTask) {
        // Create a task entity
        Task taskEntity = new Task();
        taskEntity.setName(mpxjTask.getName());
        taskEntity.setStartTime(mpxjTask.getStart());
        taskEntity.setEndTime(mpxjTask.getFinish());

        if (mpxjTask.getDuration() != null) {
            taskEntity.setDuration((float) mpxjTask.getDuration().getDuration());
        }

        if (mpxjTask.getPercentageComplete() != null) {
            taskEntity.setComplete(mpxjTask.getPercentageComplete().intValue());
        }

        taskEntity.setTaskMode(mpxjTask.getTaskMode().toString());
        taskEntity.setProject(project);
        taskEntity.setParentTask(parentTask); // Set the parent task

        // Set predecessor relationships
        if (mpxjTask.getPredecessors() != null) {
            for (Relation relation : mpxjTask.getPredecessors()) {
                Task predecessor = taskMap.get(relation.getTargetTask().getID());
                if (predecessor != null) {
                    taskEntity.setPredecessorTask(predecessor);
                    break; // Assuming only one predecessor for simplicity
                }
            }
        }

        // Save task to the database
        taskRepository.save(taskEntity);

        // Store the task in the map
        taskMap.put(mpxjTask.getID(), taskEntity);

        // Build the hierarchical structure
        Map<String, Object> taskHierarchy = new HashMap<>();
        taskHierarchy.put("id", mpxjTask.getID());
        taskHierarchy.put("taskName", mpxjTask.getName());
        taskHierarchy.put("duration", mpxjTask.getDuration() != null ? mpxjTask.getDuration().getDuration() : 0);
        taskHierarchy.put("completePercentage", mpxjTask.getPercentageComplete() != null ? mpxjTask.getPercentageComplete() : 0);
        taskHierarchy.put("parentTaskId", parentTask != null ? parentTask.getId() : null); // Include parent task ID
        taskHierarchy.put("start", mpxjTask.getStart());
        taskHierarchy.put("finish", mpxjTask.getFinish());
        taskHierarchy.put("predecessor", mpxjTask.getPredecessors() != null ? mpxjTask.getPredecessors().toString() : "[]");
        taskHierarchy.put("subtasks", new ArrayList<Map<String, Object>>());

        // Process subtasks recursively
        for (net.sf.mpxj.Task subTask : mpxjTask.getChildTasks()) {
            Map<String, Object> subTaskHierarchy = processTask(subTask, project, taskMap, taskEntity);
            ((List<Map<String, Object>>) taskHierarchy.get("subtasks")).add(subTaskHierarchy);
        }

        return taskHierarchy;
    }




}


