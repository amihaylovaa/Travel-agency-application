package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
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
    private final String USERNAME = "miracleJoy12";
    private final String EMAIL = "miracleJoy12@gmail.com";
    private final String PASSWORD = "x821356k";

    @Test
    public void save_UserIsNull_ExceptionThrown() {
        User user = null;

        assertThrows(InvalidArgumentException.class, () -> userService.save(user));
    }

    @Test
    public void save_UserWithGivenUsernameAlreadyExists_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        String password = user.getPassword();
        User anotherUser = new User(username, EMAIL, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(AlreadyExistingItemException.class, () -> userService.save(anotherUser));
    }

    @Test
    public void save_UserWithGivenEmailAlreadyExists_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        String password = user.getPassword();
        User anotherUser = new User(USERNAME, email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(AlreadyExistingItemException.class, () -> userService.save(anotherUser));
    }

    @Test
    public void save_ValidData_SuccessfullyCreatedUser() {
        User user = createUser();
        String username = user.getUsername();
        String email = user.getEmail();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        User createdUser = userService.findByUsername(username);

        assertEquals(username, createdUser.getUsername());
        assertEquals(email, createdUser.getEmail());
    }

    @Test
    public void findByEmail_UserWithGivenEmailDoesNotExist_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        String nonExistentEmail = EMAIL;

        lenient().when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(NonExistentItemException.class, () -> userService.findByEmail(nonExistentEmail));
    }

    @Test
    public void findByEmail_EmailIsEmpty_ExceptionThrown() {
        String emptyEmail = StringUtils.EMPTY;

        assertThrows(InvalidArgumentException.class, () -> userService.findByEmail(emptyEmail));
    }

    @Test
    public void findByEmail_EmailIsNull_ExceptionThrown() {
        String email = null;

        assertThrows(InvalidArgumentException.class, () -> userService.findByEmail(email));
    }

    @Test
    public void findByEmail_ValidData_SuccessfullyFoundUser() {
        User user = createUser();
        String email = user.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail(email);

        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(email, foundUser.getEmail());
    }

    @Test
    public void findByUsername_UserWithGivenUsernameDoesNotExist_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();

        lenient().when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(NonExistentItemException.class, () -> userService.findByUsername(USERNAME));
    }

    @Test
    public void findByUsername_UsernameIsEmpty_ExceptionThrown() {
        String emptyUsername = StringUtils.EMPTY;

        assertThrows(InvalidArgumentException.class, () -> userService.findByUsername(emptyUsername));
    }

    @Test
    public void findByUsername_UsernameIsNull_ExceptionThrown() {
        String username = null;

        assertThrows(InvalidArgumentException.class, () -> userService.findByUsername(username));
    }

    @Test
    public void findByUsername_ValidData_SuccessfullyFoundUser() {
        User user = createUser();
        String username = user.getUsername();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername(username);

        assertEquals(username, foundUser.getUsername());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    public void findAll_ListIsNull_ExceptionThrown() {
        List<User> users = null;

        when(userRepository.findAll()).thenReturn(users);

        assertThrows(NonExistentItemException.class, () -> userService.findAll());
    }

    @Test
    public void findAll_ListIsEmpty_ExceptionThrown() {
        List<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        assertThrows(NonExistentItemException.class, () -> userService.findAll());
    }

    @Test
    public void findAll_CreatedListOfTwoUsers_ReturnedListOfTwoUsers() {
        User firstUser = createUser();
        User secondUser = new User(USERNAME, EMAIL, PASSWORD);
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
    public void updatePassword_NewPasswordIsEmpty_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        String oldPassword = user.getPassword();
        String invalidNewPassword = StringUtils.EMPTY;

        assertThrows(InvalidArgumentException.class,
                () -> userService.updatePassword(username, invalidNewPassword, oldPassword));
    }

    @Test
    public void updatePassword_UserWithGivenUsernameDoesNotExist_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        String oldPassword = user.getPassword();
        String newPassword = PASSWORD;
        String nonExistentUsername = USERNAME;

        lenient().when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

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
    public void updateEmail_UserWithGivenEmailDoesNotExist_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        String password = user.getPassword();
        String nonExistentOldEmail = "butterfly2@gmail.com";

        lenient().when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(NonExistentItemException.class,
                () -> userService.updateEmail(EMAIL, nonExistentOldEmail, password));
    }

    @Test
    public void deleteByEmail_EmailIsEmpty_ExceptionThrown() {
        User user = createUser();
        String email = StringUtils.EMPTY;
        String password = user.getPassword();

        assertThrows(InvalidArgumentException.class, () -> userService.deleteByEmail(email, password));
    }

    @Test
    public void deleteByEmail_PasswordIsEmpty_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        String password = StringUtils.EMPTY;

        assertThrows(InvalidArgumentException.class, () -> userService.deleteByEmail(email, password));
    }

    @Test
    public void deleteByEmail_UserWithGivenEmailDoesNotExist_ExceptionThrown() {
        User user = createUser();
        String email = user.getEmail();
        String nonExistentEmail = EMAIL;
        String password = user.getPassword();

        lenient().when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(NonExistentItemException.class,
                () -> userService.deleteByUsername(nonExistentEmail, password));
    }

    @Test
    public void deleteByUsername_UsernameIsEmpty_ExceptionThrown() {
        User user = createUser();
        String username = StringUtils.EMPTY;
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
    public void deleteByUsername_UserWithGivenUsernameDoesNotExist_ExceptionThrown() {
        User user = createUser();
        String username = user.getUsername();
        String password = user.getPassword();

        lenient().when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(NonExistentItemException.class,
                () -> userService.deleteByUsername(USERNAME, password));
    }

    private User createUser() {
        String username = "violet_sun12";
        String email = "violet_sun12@gmail.com";
        String password = "12345678910";

        return new User(username, email, password);
    }
}