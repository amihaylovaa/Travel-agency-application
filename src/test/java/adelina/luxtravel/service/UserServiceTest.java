package adelina.luxtravel.service;

import adelina.luxtravel.domain.Role;
import adelina.luxtravel.domain.User;
import adelina.luxtravel.enumeration.RoleType;
import adelina.luxtravel.exception.AlreadyExistingItemException;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.RoleRepository;
import adelina.luxtravel.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    UserService userService;
    static User expectedUser;

    @BeforeAll
    public static void init() {
        long id = 5;
        String username = "username";
        String email = "email@gmail.com";
        String password = "password";
        Role role = new Role(RoleType.USER);

        expectedUser = new User(id, username, email, password, role);
    }

    @Test
    void save_UserIsNull_ExceptionThrown() {

        assertThrows(InvalidArgumentException.class, () -> userService.save(null));
    }

    @Test
    void save_UserWithGivenUsernameAlreadyExists_ExceptionThrown() {
        String existingUsername = expectedUser.getUsername();

        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(expectedUser));

        assertThrows(AlreadyExistingItemException.class, () -> userService.save(expectedUser));
    }

    @Test
    void save_UserEmailIsNull_ExceptionThrown() {
        String username = expectedUser.getUsername();
        String email = null;
        String password = expectedUser.getPassword();
        Role role = expectedUser.getRole();
        User user = new User(username, email, password, role);

        assertThrows(InvalidArgumentException.class, () -> userService.save(user));
    }

    @Test
    void save_UserPasswordIsEmpty_ExceptionThrown() {
        String username = expectedUser.getUsername();
        String email = expectedUser.getEmail();
        String password = StringUtils.EMPTY;
        Role role = expectedUser.getRole();
        User user = new User(username, email, password, role);

        assertThrows(InvalidArgumentException.class, () -> userService.save(user));
    }

    @Test
    void save_ValidData_SavedUser() {
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        User actualUser = userService.save(expectedUser);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findById_IdIsNegative_ExceptionThrown() {
        long negativeId = NumberUtils.LONG_MINUS_ONE;

        assertThrows(InvalidArgumentException.class, () -> userService.findById(negativeId));
    }

    @Test
    void findById_UserWithGivenIdDoesNotExist_ExceptionThrown() {
        long id = NumberUtils.LONG_ONE;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class, () -> userService.findById(id));
    }

    @Test
    void findById_UserWithGivenIdExists_FoundUser() {
        long id = expectedUser.getId();

        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findById(id);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByUsername_UsernameIsEmpty_ExceptionThrown() {
        String username = StringUtils.EMPTY;

        assertThrows(InvalidArgumentException.class, () -> userService.findByUsername(username));
    }

    @Test
    void findByUsername_UserWithGivenUsernameDoesNotExist_ExceptionThrown() {
        String username = expectedUser.getUsername();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class, () -> userService.findByUsername(username));
    }

    @Test
    void findByUsername_UserWithGivenUsernameExists_FoundUser() {
        String username = expectedUser.getUsername();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findByUsername(username);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByEmail_EmailIsNull_ExceptionThrown() {
        String email = null;

        assertThrows(InvalidArgumentException.class, () -> userService.findByEmail(email));
    }

    @Test
    void findByEmail_UserWithGivenEmailDoesNotExist_ExceptionThrown() {
        String email = expectedUser.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class, () -> userService.findByEmail(email));
    }

    @Test
    void findByEmail_UserWithGivenEmailExists_FoundUser() {
        String email = expectedUser.getEmail();

        when(userRepository.findByUsername(email)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findByUsername(email);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findAll_UsersAreNotFound_ExceptionThrown() {
        List<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        assertThrows(NonExistentItemException.class, () -> userService.findAll());
    }

    @Test
    void findAll_UsersExist_FoundUsers() {
        long id = NumberUtils.LONG_ONE;
        String username = "username";
        String email = "email";
        String password = "password";
        Role role = expectedUser.getRole();
        User firstUser = expectedUser;
        User secondUser = new User(id, username, email, password, role);
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(firstUser);
        expectedUsers.add(secondUser);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.findAll();

        assertTrue(actualUsers.contains(firstUser));
        assertTrue(actualUsers.contains(secondUser));
        assertEquals(expectedUsers.size(), actualUsers.size());
    }

    @Test
    void updatePassword_UserIsNull_ExceptionThrown() {
        User user = null;

        assertThrows(InvalidArgumentException.class, () -> userService.updatePassword(user));
    }

    @Test
    void updatePassword_UsernameIsEmpty_ExceptionThrown() {
        String emptyUsername = StringUtils.EMPTY;
        String password = expectedUser.getPassword();
        User user = new User(emptyUsername, password);

        assertThrows(InvalidArgumentException.class, () -> userService.updatePassword(user));
    }

    @Test
    void updatePassword_PasswordIsNull_ExceptionThrown() {
        String username = expectedUser.getUsername();
        String password = null;
        User user = new User(username, password);

        assertThrows(InvalidArgumentException.class, () -> userService.updatePassword(user));
    }

    @Test
    void updatePassword_validDate_SuccessfullyUpdatedPassword() {
        String username = expectedUser.getUsername();
        String newPassword = "new-password-456-328";
        User userWithUpdatedPassword = new User(username, newPassword);
        int expectedResult = NumberUtils.INTEGER_ONE;

        when(bCryptPasswordEncoder.encode(newPassword)).thenReturn(newPassword);
        when(userRepository.updatePassword(newPassword, username)).thenReturn(expectedResult);

        int actualResult = userService.updatePassword(userWithUpdatedPassword);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void updateEmail_UserIsNull_ExceptionThrown() {
        User user = null;

        assertThrows(InvalidArgumentException.class, () -> userService.updateEmail(user));
    }

    @Test
    void updateEmail_UserWithNewGivenEmailAlreadyExists_ExceptionThrown() {
        String newEmail = expectedUser.getEmail();
        String username = expectedUser.getUsername();
        String password = expectedUser.getPassword();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));
        when(bCryptPasswordEncoder.matches(password, password)).thenReturn(true);
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.of(expectedUser));

        assertThrows(AlreadyExistingItemException.class, () -> userService.updateEmail(expectedUser));
    }

    @Test
    void updateEmail_NewEmailIsEmpty_ExceptionThrown() {
        String newEmail = StringUtils.EMPTY;
        String username = expectedUser.getUsername();
        String password = expectedUser.getPassword();
        User user = new User(username, newEmail, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(InvalidArgumentException.class, () -> userService.updateEmail(user));
    }

    @Test
    void updateRole_UserIsNull_ExceptionThrown() {
        User user = null;

        assertThrows(InvalidArgumentException.class, () -> userService.updateRole(user));
    }

    @Test
    void updateRole_UsernameIsEmpty_ExceptionThrown() {
        Role role = expectedUser.getRole();
        String emptyUsername = StringUtils.EMPTY;
        User user = new User(emptyUsername, role);

        assertThrows(InvalidArgumentException.class, () -> userService.updateRole(user));
    }

    @Test
    void updateRole_RoleIsNotSet_ExceptionThrown() {
        Role role = expectedUser.getRole();
        String emptyUsername = StringUtils.EMPTY;
        User user = new User(emptyUsername, role);

        assertThrows(InvalidArgumentException.class, () -> userService.updateRole(user));
    }

    @Test
    void updateRole_ValidData_UserWithUpdatedRole() {
        Role role = new Role(RoleType.ADMIN);
        String username = expectedUser.getUsername();
        User expectedUser = new User(username, role);
        long id = NumberUtils.LONG_ONE;

        when(roleRepository.getIdFromName(RoleType.ADMIN.name())).thenReturn(id);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.updateRole(expectedUser);

        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getRole(), actualUser.getRole());
    }

    @Test
    void deleteById_IdIsNegative_ExceptionThrown() {
        long negativeId = NumberUtils.LONG_MINUS_ONE;

        assertThrows(InvalidArgumentException.class, () -> userService.deleteById(negativeId));
    }

    @Test
    void deleteById_UserWithGivenIdDoesNotExist_ExceptionThrown() {
        long id = NumberUtils.LONG_ONE;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class, () -> userService.deleteById(id));
    }

    @AfterAll
    static void clear() {
        expectedUser = null;
    }
}