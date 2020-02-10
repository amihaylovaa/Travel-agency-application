package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import java.util.List;
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
    private final String nonExistingUsername = "miracleJoy12";
    private final String nonExistingEmail = "miracleJoy12@gmail.com";

    @Test
    public void save_UserIsNull_ExceptionThrown() {
        User user = null;

        assertThrows(InvalidArgumentException.class, () -> userService.save(user));
    }

    @Test
    public void save_UserWithGivenUsernameAlreadyExists_ExceptionThrown() {
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
    public void save_UserWithGivenEmailAlreadyExists_ExceptionThrown() {
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
    void findByEmail_UserWithGivenEmailNotPresent_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        Optional<User> userOptional = Optional.of(user);

        lenient().when(userRepository.findByEmail(email)).thenReturn(userOptional);

        assertThrows(NonExistentItemException.class, () -> userService.findByEmail(nonExistingEmail));
    }

    @Test
    void findByEmail_EmailIsEmpty_ExceptionThrown() {
        String emptyEmail = "";

        assertThrows(InvalidArgumentException.class, () -> userService.findByEmail(emptyEmail));
    }

    @Test
    void findByEmail_EmailIsNull_ExceptionThrown() {
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
    void findByUsername_UserWithGivenUsernameIsNotPresent_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        Optional<User> userOptional = Optional.of(user);

        lenient().when(userRepository.findByUsername(username)).thenReturn(userOptional);

        assertThrows(NonExistentItemException.class, () -> userService.findByUsername(nonExistingUsername));
    }

    @Test
    void findByUsername_UsernameIsEmpty_ExceptionThrown() {
        User user = createUser();
        String emptyUsername = "";

        assertThrows(InvalidArgumentException.class, () -> userService.findByUsername(emptyUsername));
    }

    @Test
    void findByUsername_UsernameIsNull_ExceptionThrown() {
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

    @Test
    public void findAll_ListIsNull_ExceptionThrown() {

        when(userRepository.findAll()).thenReturn(null);

        assertThrows(NonExistentItemException.class, () -> userService.findAll());
    }

    @Test
    public void findAll_ListIsEmpty_ExceptionThrown() {
        List<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        assertThrows(NonExistentItemException.class, () -> userService.findAll());
    }

    @Test
    public void findAll_CreatedListOfTwoUsers_ReturnedResultListOfTwoUsers() {
        String firstUsername = "john21";
        String secondUsername = "david53";
        String firstEmail = "john21@gmail.com";
        String secondEmail = "david52@abv.bg";
        String password = "k82d134";
        User firstUser = new User(firstUsername, firstEmail, password);
        User secondUser = new User(secondUsername, secondEmail, password);
        List<User> createdUsers = new ArrayList<>();
        createdUsers.add(firstUser);
        createdUsers.add(secondUser);

        when(userRepository.findAll()).thenReturn(createdUsers);
        List<User> foundUsers = userService.findAll();

        assertEquals(createdUsers.size(), foundUsers.size());
        assertTrue(foundUsers.contains(firstUser));
        assertTrue(foundUsers.contains(secondUser));
    }

    @Test
    public void updatePassword_NewPasswordParamIsEmpty_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        String oldPassword = user.getPassword();
        String invalidNewPassword = "";

        assertThrows(InvalidArgumentException.class,
                () -> userService.updatePassword(username, invalidNewPassword, oldPassword));
    }

    @Test
    public void updatePassword_UserWithGivenUsernameIsNotPresent_ExceptionThrown() {
        User user = createUser();
        Optional<User> userOptional = Optional.of(user);
        String username = user.getUsername();
        String oldPassword = user.getPassword();
        String newPassword = "4821446i32js2";
        String nonExistentUsername = "joyMiracle12";

        lenient().when(userRepository.findByUsername(username)).thenReturn(userOptional);

        assertThrows(NonExistentItemException.class,
                () -> userService.updatePassword(nonExistentUsername, newPassword, oldPassword));
    }

    @Test
    public void updateEmail_NewAndOldEmailsAreTheSame_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        String password = user.getPassword();

        assertThrows(AlreadyExistingItemException.class,
                () -> userService.updateEmail(email, email, password));
    }

    @Test
    public void updateEmail_UserWithGivenEmailIsNotPresent_ExceptionThrown() {
        User user = createUser();
        Optional<User> userOptional = Optional.of(user);
        String email = user.getEmail();
        String password = user.getPassword();
        String newEmail = "sunFlow12@abv.bg";
        String nonExistentOldEmail = "joyMiracle12@gmail.com";

        lenient().when(userRepository.findByEmail(email)).thenReturn(userOptional);

        assertThrows(NonExistentItemException.class,
                () -> userService.updateEmail(newEmail, nonExistentOldEmail, password));
    }

    @Test
    public void deleteByEmail_EmailIsEmpty_ExceptionThrown() {
        User user = createUser();
        String email = "";
        String password = user.getPassword();

        assertThrows(InvalidArgumentException.class, () -> userService.deleteByEmail(email, password));
    }

    @Test
    public void deleteByEmail_PasswordIsEmpty_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        String password = "";

        assertThrows(InvalidArgumentException.class, () -> userService.deleteByEmail(email, password));
    }

    @Test
    public void deleteByEmail_UserWithGivenEmailIsNotPresent_ExceptionThrown() {
        User user = createUser();
        Optional<User> userOptional = Optional.of(user);
        String email = user.getEmail();
        String password = user.getPassword();

        lenient().when(userRepository.findByEmail(email)).thenReturn(userOptional);

        assertThrows(NonExistentItemException.class,
                () -> userService.deleteByUsername(nonExistingEmail, password));
    }

    @Test
    public void deleteByUsername_UsernameIsEmpty_ExceptionThrown() {
        User user = createUser();
        String username = "";
        String password = user.getPassword();

        assertThrows(InvalidArgumentException.class, () -> userService.deleteByUsername(username, password));
    }

    @Test
    public void deleteByUsername_PasswordIsNull_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        String password = null;

        assertThrows(InvalidArgumentException.class, () -> userService.deleteByUsername(username, password));
    }

    @Test
    public void deleteByUsername_UserWithGivenUsernameIsNotPresent_ExceptionThrown() {
        User user = createUser();
        Optional<User> userOptional = Optional.of(user);
        String username = user.getUsername();
        String password = user.getPassword();

        lenient().when(userRepository.findByUsername(username)).thenReturn(userOptional);

        assertThrows(NonExistentItemException.class,
                () -> userService.deleteByUsername(nonExistingUsername, password));
    }

    private User createUser() {
        String username = "violet_sun12";
        String email = "violet_sun12@gmail.com";
        String password = "12345678910";

        return new User(username, email, password);
    }
}