package com.project.system.repository;

import com.project.system.model.UserQueryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQueryResponseRepository extends JpaRepository<UserQueryResponse, Long> {
}