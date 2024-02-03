package com.qburst.genericsearch.controllers;

import com.qburst.genericsearch.entitys.UsersEntity;
import com.qburst.genericsearch.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @PostMapping("/search")
    public List<UsersEntity> getAllUsers(@RequestBody String requestBody) {
        return usersService.getAllUsers(requestBody);
    }
    @PostMapping("/create")
    public UsersEntity createUsers(@RequestBody UsersEntity requestBody) {
        return usersService.createUsers(requestBody);
    }
}
