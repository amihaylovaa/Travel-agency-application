package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void saveShouldThrowExceptionWhenUserIsNull() {
        User user = null;

        assertThrows(InvalidArgumentException.class, () -> userService.save(user));
    }

    @Test
    public void saveShouldThrowExceptionWhenEmailAlreadyExists() {
        String firstUsername = "violet_sun12";
        String firstEmail = "violet_sun12@gmail.com";
        String firstPassword = "12345678910";
        String secondUsername = "purple_sun12";
        String secondEmail = "purple_sun12@gmail.com";
        String secondPassword = "12345678910";
        User firstUser = new User(firstUsername, firstEmail, firstPassword);
        User secondUser = new User(secondUsername, secondEmail, secondPassword);

        userService.save(firstUser);

        assertThrows(NonExistentItemException.class, () -> userService.save(secondUser));
    }
}