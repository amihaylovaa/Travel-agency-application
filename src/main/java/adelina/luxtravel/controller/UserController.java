package adelina.luxtravel.controller;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User save(@RequestBody User user) throws InvalidArgumentException, NonExistentItemException {
        return userService.save(user);
    }

    @GetMapping(value = "/{username}")
    public User findByUsername(@PathParam("username") String username)
            throws InvalidArgumentException, NonExistentItemException {
        return userService.findByUsername(username);
    }

    @GetMapping(value = "/{email}")
    public User findByEmail(@PathParam("email") String email)
            throws InvalidArgumentException, NonExistentItemException {
        return userService.findByEmail(email);
    }

    @GetMapping(value = "/all")
    public List<User> findAll() throws NonExistentItemException {
        return userService.findAll();
    }

    @PutMapping(value = "/{username}/{newPassword}/{oldPassword}")
    public void updatePassword(@PathParam("username") String username,
                               @PathParam("newPassword") String newPassword,
                               @PathParam("oldPassword") String oldPassword)
            throws InvalidArgumentException, NonExistentItemException {
        userService.updatePassword(username, newPassword, oldPassword);
    }

    @PutMapping(value = "/{newEmail}/{oldEmail}/{password}")
    public void updateEmail(@PathParam("newEmail") String newEmail,
                            @PathParam("oldEmail") String oldEmail,
                            @PathParam("password") String password)
            throws InvalidArgumentException, NonExistentItemException {
        userService.updateEmail(newEmail, oldEmail, password);
    }

    @DeleteMapping(value = "/{username}/{password}")
    public void deleteByUsername(@PathParam("username") String username,
                                 @PathParam("password") String password)
            throws InvalidArgumentException, NonExistentItemException {

        userService.deleteByUsername(username, password);
    }

    @DeleteMapping(value = "/{email}/{password}")
    public void deleteByEmail(@PathParam("email") String email,
                              @PathParam("password") String password)
            throws InvalidArgumentException, NonExistentItemException {

        userService.deleteByEmail(email, password);
    }
}