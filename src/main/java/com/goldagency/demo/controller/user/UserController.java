package com.goldagency.demo.controller.user;

import com.goldagency.demo.controller.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/users")
    public ResponseEntity createUser(@RequestBody @Valid User user) {
        List<User> byEmail = userRepository.findByEmail(user.getEmail());
        if(!byEmail.isEmpty()) {
            return new ResponseEntity("User already exist", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }
}
