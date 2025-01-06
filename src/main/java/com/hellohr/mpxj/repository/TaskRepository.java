package com.hellohr.mpxj.repository;

import com.hellohr.mpxj.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByCode(String code);  // Find task by its code
}
