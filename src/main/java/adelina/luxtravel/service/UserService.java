package adelina.luxtravel.service;

import adelina.luxtravel.domain.Role;
import adelina.luxtravel.domain.User;
import adelina.luxtravel.enumeration.RoleType;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.RoleRepository;
import adelina.luxtravel.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.*;

/**
 * Represents service for a user
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves new user
     *
     * @param user the new user that is going to be saved
     * @return the saved user
     */
    public User save(User user) {
        validateUserNotNull(user);

        String username = user.getUsername();
        String email = user.getEmail();

        validateUsernameNotEmpty(username);
        validateEmailNotEmpty(email);
        validateUsernameNotExists(username);
        validateEmailNotExists(email);
        setRole(user);
        validatePasswordNotEmpty(user.getPassword());
        encodePassword(user);

        return userRepository.save(user);
    }

    /**
     * Simulates log out
     *
     * @return true for successful logout
     **/
    public boolean logout() {
        return true;
    }

    /**
     * Gets user by id
     *
     * @param id user's id
     * @return if present - the searched user, otherwise throws exception for non-existent user
     */
    public User findById(long id) {
        if (id <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new NonExistentItemException("User with given id does not exist");
        }
        return user.get();
    }

    /**
     * Gets user by username
     *
     * @param username user's username
     * @return if present - the searched user, otherwise throws exception for non-existent user
     */
    public User findByUsername(String username) {
        validateUsernameNotEmpty(username);

        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new NonExistentItemException("User with this username does not exist");
        }
        return user.get();
    }

    /**
     * Gets user by email
     *
     * @param email user's email
     * @return if present - the searched user, otherwise throws exception for non-existent user
     */
    public User findByEmail(String email) {
        validateEmailNotEmpty(email);

        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new NonExistentItemException("User with this email does not exist");
        }
        return user.get();
    }

    /**
     * Gets all existing users
     *
     * @return list of all users, throws exception if users are not found
     */
    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (ObjectUtils.isEmpty(users)) {
            throw new NonExistentItemException("Users are not found");
        }
        return users;
    }

    /**
     * Updates user's password
     *
     * @param user the user whose password is going to be updated (contains user's username and the new password)
     * @return 1 for successful update
     */
    public int updatePassword(User user) {
        validateUserNotNull(user);

        String username = user.getUsername();
        String newPassword = user.getPassword();

        validateUsernameNotEmpty(username);
        validatePasswordNotEmpty(newPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);

        return userRepository.updatePassword(encodedPassword, username);
    }

    /**
     * Updates user's email
     *
     * @param user the user whose email is going to be updated (contains user's username, password and the new email)
     * @return the user with updated email
     */
    public User updateEmail(User user) {
        validateUserNotNull(user);

        String newEmail = user.getEmail();
        String username = user.getUsername();
        User storedUser = findByUsername(username);
        String storedPassword = storedUser.getPassword();
        String sentPassword = user.getPassword();

        validateEmailNotEmpty(newEmail);
        validatePasswordMatch(sentPassword, storedPassword);
        validateEmailNotExists(newEmail);
        userRepository.updateEmail(newEmail, username);

        return findByEmail(newEmail);
    }

    /**
     * Updates user's role
     *
     * @param user the user whose role is going to be updated (contains user's username and the new role)
     * @return the user with updated role
     */
    public User updateRole(User user) {
        validateUserNotNull(user);

        String username = user.getUsername();
        Role role = user.getRole();

        if (ObjectUtils.isEmpty(role) || ObjectUtils.isEmpty(role.getRoleType())) {
            throw new InvalidArgumentException("Invalid role");
        }

        validateUsernameNotEmpty(username);

        String roleType = role.getRoleType().name();
        long roleId = roleRepository.getIdFromName(roleType);
        userRepository.updateRole(roleId, username);

        return findByUsername(username);
    }

    /**
     * Deletes user by id
     *
     * @param id user's id that is going to be deleted
     * @return true for successfully deleted user, false for unsuccessful attempt
     */
    public boolean deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        if (!userRepository.findById(id).isPresent()) {
            throw new NonExistentItemException("User with given id does not exist");
        } else {
            userRepository.deleteById(id);

            try {
                findById(id);
            } catch (NonExistentItemException e) {
                return true;
            }
            return false;
        }
    }

    private void validateUserNotNull(User user) {
        if (ObjectUtils.isEmpty(user)) {
            throw new InvalidArgumentException(INVALID_USER);
        }
    }

    private void validateUsernameNotExists(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            throw new AlreadyExistingItemException("User with this username already exists");
        }
    }

    private void validateEmailNotExists(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new AlreadyExistingItemException("User with this email already exists");
        }
    }

    private void validatePasswordMatch(String expectedPassword, String encryptedActualPassword) {
        if (!(passwordEncoder.matches(expectedPassword, encryptedActualPassword))) {
            throw new MismatchException("Passwords do not match");
        }
    }

    private void validateUsernameNotEmpty(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException(INVALID_USERNAME);
        }
    }

    private void validateEmailNotEmpty(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException(INVALID_EMAIL);
        }
    }

    private void validatePasswordNotEmpty(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException(INVALID_PASSWORD);
        }
    }

    private void encodePassword(User user) {
        String enteredPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(enteredPassword);

        user.setPassword(encodedPassword);
    }

    private void setRole(User user) {
        long roleUserId = 2;
        Role role = new Role(roleUserId, RoleType.USER);

        user.setRole(role);
    }
}