package com.my.api.controller;

import com.my.api.model.User;
import com.my.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/users")
public class UserController {
    private static final String UPDATED_USER_MESSAGE = "User successfully updated";
    private static final String CREATED_USER_MESSAGE = "The user has been created";
    private static final String DELETED_USER_MESSAGE = "User successfully deleted";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<CreationResponse> add(@RequestBody User user) {
        long id = userService.addUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreationResponse(id, CREATED_USER_MESSAGE));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateResponse> updateAllUserFields(@PathVariable Long id, @RequestBody User user) {
        User updateUser = userService.updateUser(id, user);
        return ResponseEntity.ok(new UpdateResponse(updateUser, UPDATED_USER_MESSAGE));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResponse> updateSomeOfUserFields(@PathVariable Long id, @RequestBody User updates) {
        log.info("Received a patch request: {}", updates);
        User user = userService.updateUserFields(id, updates);
        return ResponseEntity.ok(new UpdateResponse(user, UPDATED_USER_MESSAGE));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<DeleteResponse> delete(@PathVariable long id) {
        userService.deleteById(id);
        return ResponseEntity.ok(new DeleteResponse(DELETED_USER_MESSAGE));
    }

    @GetMapping(value = "/filtered")
    public ResponseEntity<GetByBirthDateResponse> getByBirthDate(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        List<User> users = userService.getUsersByBirthDate(start, end);
        return ResponseEntity.ok(new GetByBirthDateResponse(users));
    }

    public record UpdateResponse(User user, String message) {}
    public record GetByBirthDateResponse(List<User> users) {}
    public record DeleteResponse(String message) {}
    public record CreationResponse(Long id, String message){}
}
