package com.moroccantube.app.controller;

import com.moroccantube.app.dao.UpdateFullnameRequest;
import com.moroccantube.app.dao.UpdatePasswordDao;
import com.moroccantube.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/update-username")
    public ResponseEntity<?> updateFullname(@RequestBody UpdateFullnameRequest updateFullnameRequest) {
        return userService.handleUpdateFullname(updateFullnameRequest.getNewFullname(), updateFullnameRequest.getUsername());
    }


    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDao updatePasswordDao) {
        return userService.handleUpdatePassword(updatePasswordDao);
    }


}
