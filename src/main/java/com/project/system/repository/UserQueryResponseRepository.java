package com.project.system.repository;

import com.project.system.response.ApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQueryResponseRepository extends JpaRepository<ApiResponse.UserQueryResponse, Long> {
}