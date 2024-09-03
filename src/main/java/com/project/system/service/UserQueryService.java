package com.project.system.service;

import com.project.system.model.UserQuery;
import com.project.system.request.SubmitUserQueryRequest;

import java.util.List;

public interface UserQueryService {

    UserQuery submitUserQuery(SubmitUserQueryRequest request);

    List<UserQuery> getAllUserQueries();
}
