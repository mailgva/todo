package com.gorbatenko.testtask.controller;

import com.gorbatenko.testtask.model.User;
import com.gorbatenko.testtask.to.UserTo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.gorbatenko.testtask.config.SecurityConfig.createJWT;
import static com.gorbatenko.testtask.controller.UserController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends AbstractController{
    static final String REST_URL = "/user/";

    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody UserTo userTo) {
        checkExistCredentionals(userTo);

        if(userRepository.findByUsernameIgnoreCase(userTo.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        User user = fromTo(userTo);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("auth")
    public ResponseEntity<Void> auth(@RequestBody UserTo userTo) {
        checkExistCredentionals(userTo);

        Optional<User> userDB = userRepository.findByUsernameIgnoreCase(userTo.getUsername());

        if(! userDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User user = fromTo(userTo);

        if(passwordEncoder.matches(userDB.get().getPassword(), passwordEncoder.encode(user.getPassword()))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        String jwt = createJWT(secret, user.getUsername());

        user = userDB.get();
        user.setToken(jwt);

        userRepository.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);

        return ResponseEntity.ok().headers(headers).build();
    }

    private void checkExistCredentionals(UserTo userTo) {
        if(userTo.getUsername() == null ||
                userTo.getUsername().isEmpty() ||
                userTo.getPassword() == null ||
                userTo.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private User fromTo(UserTo userTo) {
        return new User(userTo.getUsername(), userTo.getPassword());
    }
}
