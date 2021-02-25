package com.gorbatenko.testtask.controller;

import com.gorbatenko.testtask.repository.ToDoRepository;
import com.gorbatenko.testtask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class AbstractController {
    @Value("${jwt.secret.key}")
    protected String secret;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ToDoRepository toDoRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;
}
