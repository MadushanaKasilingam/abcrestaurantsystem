package com.project.system.repository;

import com.project.system.model.UserQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQueryRepository extends JpaRepository<UserQuery, Long>
{
}