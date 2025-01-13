package com.hellohr.mpxj.repository;

import com.hellohr.mpxj.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByName(String name); // Removed static keyword
}
