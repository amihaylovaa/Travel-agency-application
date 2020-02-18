package adelina.luxtravel.controller;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping(value = "username/{username}")
    public User findByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username);
    }

    @GetMapping(value = "email/{email}")
    public User findByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    @GetMapping(value = "/all")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PutMapping(value = "update-password/{username}/{newPassword}/{oldPassword}")
    public void updatePassword(@PathVariable("username") String username,
                               @PathVariable("newPassword") String newPassword,
                               @PathVariable("oldPassword") String oldPassword) {
        userService.updatePassword(username, newPassword, oldPassword);
    }

    @PutMapping(value = "update-email/{newEmail}/{oldEmail}/{password}")
    public void updateEmail(@PathVariable("newEmail") String newEmail,
                            @PathVariable("oldEmail") String oldEmail,
                            @PathVariable("password") String password) {
        userService.updateEmail(newEmail, oldEmail, password);
    }

    @DeleteMapping(value = "username/{username}/{password}")
    public void deleteByUsername(@PathVariable("username") String username,
                                 @PathVariable("password") String password) {
        userService.deleteByUsername(username, password);
    }

    @DeleteMapping(value = "email/{email}/{password}")
    public void deleteByEmail(@PathParam("email") String email,
                              @PathParam("password") String password) {
        userService.deleteByEmail(email, password);
    }
}