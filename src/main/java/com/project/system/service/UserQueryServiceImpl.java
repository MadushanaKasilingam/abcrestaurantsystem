package com.project.system.service;

import com.project.system.model.UserQuery;
import com.project.system.repository.UserQueryRepository;
import com.project.system.request.SubmitUserQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserQueryRepository userQueryRepository;

    @Autowired
    public UserQueryServiceImpl(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    @Override
    public UserQuery submitUserQuery(SubmitUserQueryRequest request) {
        UserQuery userQuery = new UserQuery(request.getUserId(), request.getSubject(), request.getMessage());
        return userQueryRepository.save(userQuery);
    }

    @Override
    public List<UserQuery> getAllUserQueries() {
        return userQueryRepository.findAll();
    }
}
