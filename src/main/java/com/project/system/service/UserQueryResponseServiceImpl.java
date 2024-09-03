package com.project.system.service;

import com.project.system.dto.UserQueryResponseDTO;
import com.project.system.model.User;
import com.project.system.model.UserQuery;
import com.project.system.repository.UserQueryRepository;
import com.project.system.repository.UserQueryResponseRepository;
import com.project.system.repository.UserRepository;
import com.project.system.request.SubmitUserQueryResponseRequest;
import com.project.system.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryResponseServiceImpl implements UserQueryResponseService {

    private final UserQueryResponseRepository userQueryResponseRepository;
    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserQueryResponseServiceImpl(UserQueryResponseRepository userQueryResponseRepository,
                                        UserQueryRepository userQueryRepository,
                                        UserRepository userRepository) {
        this.userQueryResponseRepository = userQueryResponseRepository;
        this.userQueryRepository = userQueryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserQueryResponseDTO submitUserQueryResponse(SubmitUserQueryResponseRequest request) {
        Optional<UserQuery> userQuery = userQueryRepository.findById(request.getQueryId());
        Optional<User> responder = userRepository.findById(request.getResponderId());

        if (userQuery.isPresent() && responder.isPresent()) {
            ApiResponse.UserQueryResponse response = new ApiResponse.UserQueryResponse(
                    userQuery.get(),
                    responder.get(),
                    request.getResponseMessage()
            );
            ApiResponse.UserQueryResponse savedResponse = userQueryResponseRepository.save(response);
            return new UserQueryResponseDTO(
                    savedResponse.getUserQuery().getId(),
                    savedResponse.getRespondedBy().getUsername(),
                    savedResponse.getResponseMessage(),
                    savedResponse.getResponseTime()
            );
        } else {
            throw new IllegalArgumentException("Invalid Query ID or Responder ID");
        }
    }

    @Override
    public List<UserQuery> getAllUserQueries() {
        return userQueryRepository.findAll();
    }
}
