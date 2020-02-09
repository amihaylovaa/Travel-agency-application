package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.AlreadyExistingItemException;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    public void save_UserIsNull_ExceptionThrown() {
        User user = null;

        assertThrows(InvalidArgumentException.class, () -> userService.save(user));
    }

    @Test
    public void save_UserWithThisUsernameAlreadyExists_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        String email = "violet_sun12@gmail.com";
        String password = user.getPassword();
        User anotherUser = new User(username, email, password);
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findByUsername(username)).thenReturn(userOptional);

        assertThrows(AlreadyExistingItemException.class, () -> userService.save(anotherUser));
    }

    @Test
    public void save_UserWithThisEmailAlreadyExists_ExceptionThrown() {
        User user = createUser();
        String username = "violetSky2";
        String email = user.getEmail();
        String password = user.getPassword();
        User anotherUser = new User(username, email, password);
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findByEmail(email)).thenReturn(userOptional);

        assertThrows(AlreadyExistingItemException.class, () -> userService.save(anotherUser));
    }

    @Test
    void save_ValidData_SuccessfullyCreatedUser() {
        User user = createUser();
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findByUsername(username)).thenReturn(userOptional);

        User createdUser = userService.findByUsername(username);

        assertEquals(username, createdUser.getUsername());
        assertEquals(email, createdUser.getEmail());
    }

    @Test
    void findByEmail_UserWithThisEmailNotPresent_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        String invalidEmail = "miracle123@abv.bg";
        Optional<User> userOptional = Optional.of(user);

        lenient().when(userRepository.findByEmail(email)).thenReturn(userOptional);

        assertThrows(NonExistentItemException.class, () -> userService.findByEmail(invalidEmail));
    }

    @Test
    void findByEmail_EmptyEmail_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        String emptyEmail = "";

        assertThrows(InvalidArgumentException.class, () -> userService.findByEmail(emptyEmail));
    }

    @Test
    void findByEmail_EmailIsNull_ExceptionThrown() {
        User user = createUser();
        String email = null;

        assertThrows(InvalidArgumentException.class, () -> userService.findByEmail(email));
    }

    @Test
    void findByEmail_ValidData_SuccessfullyFoundUser() {
        User user = createUser();
        String email = user.getEmail();
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findByEmail(email)).thenReturn(userOptional);

        User foundUser = userService.findByEmail(email);

        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(email, foundUser.getEmail());
    }

    @Test
    void findByUsername_UserWithThisUsernameIsNotPresent_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        String invalidUsername = "miracle123";
        Optional<User> userOptional = Optional.of(user);

        lenient().when(userRepository.findByUsername(username)).thenReturn(userOptional);

        assertThrows(NonExistentItemException.class, () -> userService.findByUsername(invalidUsername));
    }

    @Test
    void findByUsername_UsernameIsEmpty_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        String emptyUsername = "";

        assertThrows(InvalidArgumentException.class, () -> userService.findByUsername(emptyUsername));
    }

    @Test
    void findByUsername_UsernameIsNull_ExceptionThrown() {
        User user = createUser();
        String username = null;

        assertThrows(InvalidArgumentException.class, () -> userService.findByUsername(username));
    }

    @Test
    void findByUsername_SuccessfullyFoundUser() {
        User user = createUser();
        String username = user.getUsername();
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findByUsername(username)).thenReturn(userOptional);

        User foundUser = userService.findByUsername(username);

        assertEquals(username, foundUser.getUsername());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    private User createUser() {
        String username = "violet_sun12";
        String email = "violet_sun12@gmail.com";
        String password = "12345678910";

        return new User(username, email, password);
    }
}