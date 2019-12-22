package adelina.luxtravel.controller;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public User save(@RequestBody User user) throws InvalidArgumentException, NonExistentItemException {
        return userService.save(user);
    }

    @GetMapping(value = "/by-username")
    public User findByUsername(@PathVariable("username") String username)
            throws InvalidArgumentException, NonExistentItemException {
        return userService.findByUsername(username);
    }

    @GetMapping(value = "/by-email")
    public User findByEmail(@PathVariable("email") String email)
            throws InvalidArgumentException, NonExistentItemException {
        return userService.findByEmail(email);
    }

    @GetMapping(value = "/all")
    public List<User> findAll() throws NonExistentItemException {
        return userService.findAll();
    }

    @PutMapping(value = "/new-password")
    public void updatePassword(@PathVariable("username") String username,
                               @PathVariable("newPassword") String newPassword,
                               @PathVariable("oldPassword") String oldPassword)
            throws InvalidArgumentException, NonExistentItemException {
        userService.updatePassword(username, newPassword, oldPassword);
    }

    @PutMapping(value = "/new-email")
    public void updateEmail(@PathVariable("newEmail") String newEmail,
                            @PathVariable("oldEmail") String oldEmail,
                            @PathVariable("password") String password)
            throws InvalidArgumentException, NonExistentItemException {
        userService.updateEmail(newEmail, oldEmail, password);
    }

    @DeleteMapping(value = "/by-username")
    public void deleteByUsername(@PathVariable("username") String username,
                                 @PathVariable("password") String password)
            throws InvalidArgumentException, NonExistentItemException {

        userService.deleteByUsername(username, password);
    }

    @DeleteMapping(value = "/by_email")
    public void deleteByEmail(@PathVariable("email") String email,
                              @PathVariable("password") String password)
            throws InvalidArgumentException, NonExistentItemException {

        userService.deleteByEmail(email, password);
    }
}