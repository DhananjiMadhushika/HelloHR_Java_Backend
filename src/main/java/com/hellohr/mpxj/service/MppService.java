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

import java.util.ArrayList;
import java.util.List;

@Service
public class MppService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<String> parseAndSaveMpp(String filePath) throws Exception {
        MPPReader reader = new MPPReader();
        ProjectFile projectFile = reader.read(filePath);

        // Create a list to hold the project and task names
        List<String> projectAndTaskNames = new ArrayList<>();

        // Fetch project properties and save project
        Project project = new Project();
        String projectName = projectFile.getProjectProperties().getProjectTitle();
        if (projectName != null) {
            project.setName(projectName);
            projectAndTaskNames.add("Project: " + projectName);
        }

        project.setStartDate(projectFile.getProjectProperties().getStartDate());
        project.setEndDate(projectFile.getProjectProperties().getFinishDate());
        projectRepository.save(project);

        // A map to track tasks by their IDs for parent-child relationships
        Map<Integer, Task> taskMap = new HashMap<>();

        // Iterate through tasks and save them
        for (net.sf.mpxj.Task mpxjTask : projectFile.getTasks()) {
            if (mpxjTask.getName() != null) {
                Task taskEntity = new Task();
                taskEntity.setName(mpxjTask.getName());
                taskEntity.setStartTime(mpxjTask.getStart());
                taskEntity.setEndTime(mpxjTask.getFinish());

                // Convert duration to Float
                if (mpxjTask.getDuration() != null) {
                    taskEntity.setDuration((float) mpxjTask.getDuration().getDuration());
                }

                taskEntity.setProject(project);

                // Check if the task has a parent task and set the parent-child relationship
                if (mpxjTask.getParentTask() != null) {
                    Task parentTask = taskMap.get(mpxjTask.getParentTask().getID());
                    if (parentTask != null) {
                        taskEntity.setParentTask(parentTask); // Set the parent task
                        parentTask.getSubtasks().add(taskEntity); // Add current task as a subtask of the parent
                    }
                }

                // Save the task entity
                taskRepository.save(taskEntity);

                // Add task name to the list
                projectAndTaskNames.add("Task: " + taskEntity.getName());

                // Store the task in the map for later reference (for parent-child relation)
                taskMap.put(mpxjTask.getID(), taskEntity);
            }
        }

        // Return the list of project and task names
        return projectAndTaskNames;
    }


}
