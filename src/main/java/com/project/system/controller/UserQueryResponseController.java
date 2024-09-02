package com.project.system.controller;

import com.project.system.dto.UserQueryResponseDTO;
import com.project.system.model.UserQuery;
import com.project.system.request.SubmitUserQueryResponseRequest;
import com.project.system.response.UserQueryResponseMessage;
import com.project.system.service.UserQueryResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/query-responses")
public class UserQueryResponseController {

    private final UserQueryResponseService userQueryResponseService;

    @Autowired
    public UserQueryResponseController(UserQueryResponseService userQueryResponseService) {
        this.userQueryResponseService = userQueryResponseService;
    }

    @PostMapping
    public ResponseEntity<UserQueryResponseMessage> submitUserQueryResponse(@RequestBody SubmitUserQueryResponseRequest request) {
        UserQueryResponseDTO responseDTO = userQueryResponseService.submitUserQueryResponse(request);
        return ResponseEntity.ok(new UserQueryResponseMessage("Response submitted successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserQuery>> getAllUserQueries() {
        List<UserQuery> userQueries = userQueryResponseService.getAllUserQueries();
        return ResponseEntity.ok(userQueries);
    }
}
