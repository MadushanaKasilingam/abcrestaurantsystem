package com.project.system.controller;

import com.project.system.model.UserQuery;
import com.project.system.request.SubmitUserQueryRequest;
import com.project.system.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-queries")
public class UserQueryController {

    private final UserQueryService userQueryService;

    @Autowired
    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @PostMapping
    public ResponseEntity<UserQuery> submitUserQuery(@RequestBody SubmitUserQueryRequest request) {
        UserQuery userQuery = userQueryService.submitUserQuery(request);
        return ResponseEntity.ok(userQuery);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserQuery>> getAllUserQueries() {
        List<UserQuery> userQueries = userQueryService.getAllUserQueries();
        return ResponseEntity.ok(userQueries);
    }
}
