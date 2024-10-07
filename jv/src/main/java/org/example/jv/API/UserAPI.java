package org.example.jv.API;

import org.example.jv.Entity.UserEntity;
import org.example.jv.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserAPI {

    @Autowired
    private UserService userService;

    @GetMapping (value = "/user/{id}")
    public UserEntity getUserById (@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping (value = "/user")
    public UserEntity create (@RequestBody UserEntity user) {
        return userService.save(user);
    }

    @PutMapping (value = "/user/{id}")
    public UserEntity update (@RequestBody UserEntity user, @PathVariable Long id) {
        user.setId(id);
        return userService.save(user);
    }

    @DeleteMapping (value = "/user/{id}")
    public void delete (@PathVariable Long id) {
        userService.deleteById(id);
    }
}
