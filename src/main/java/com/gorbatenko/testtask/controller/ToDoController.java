package com.gorbatenko.testtask.controller;

import com.gorbatenko.testtask.model.ToDo;
import com.gorbatenko.testtask.model.User;
import com.gorbatenko.testtask.to.ToDoTo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import static com.gorbatenko.testtask.controller.ToDoController.REST_URL;

@AllArgsConstructor
@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ToDoController extends AbstractController {
    static final String REST_URL = "/todo/";

    @GetMapping
    public List<ToDo> getAll(Principal principal){
        return toDoRepository.findAllByUserId(getUserIdFromPrincipal(principal));
    }

    @GetMapping("/{id}")
    public ToDo getById(@PathVariable Long id, Principal principal){
        return toDoRepository.findByIdAndUserId(id, getUserIdFromPrincipal(principal))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id, Principal principal){
        toDoRepository.findByIdAndUserId(id, getUserIdFromPrincipal(principal))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        toDoRepository.deleteById(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ToDo> create(@RequestBody ToDoTo toDoTo, Principal principal){
        ToDo toDo = fromTo(toDoTo, principal);
        toDo = toDoRepository.save(toDo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(toDo.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(toDo);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<Void> update(@RequestBody ToDoTo toDoTo, Principal principal){
        toDoRepository.findByIdAndUserId(toDoTo.getId(), getUserIdFromPrincipal(principal))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ToDo toDo = fromTo(toDoTo, principal);
        toDo.setId(toDoTo.getId());

        toDoRepository.save(toDo);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT );
    }

    private ToDo fromTo(ToDoTo toDoTo, Principal principal) {
        return new ToDo(
                getUserFromPrincipal(principal),
                toDoTo.getDateTime(),
                toDoTo.getTask(),
                toDoTo.getExecuted());
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        return getUserFromPrincipal(principal).getId();
    }

    private User getUserFromPrincipal(Principal principal) {
        return userRepository.findByUsernameIgnoreCase(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
