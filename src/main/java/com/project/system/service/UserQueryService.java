package com.project.system.service;

import com.project.system.model.UserQuery;
import com.project.system.request.SubmitUserQueryRequest;

public interface UserQueryService {

    UserQuery submitUserQuery(SubmitUserQueryRequest request);


}
