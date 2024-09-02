package com.project.system.service;

import com.project.system.dto.UserQueryResponseDTO;
import com.project.system.model.UserQuery;
import com.project.system.request.SubmitUserQueryResponseRequest;

import java.util.List;

public interface UserQueryResponseService {

    UserQueryResponseDTO submitUserQueryResponse(SubmitUserQueryResponseRequest request);

    List<UserQuery> getAllUserQueries();
}
