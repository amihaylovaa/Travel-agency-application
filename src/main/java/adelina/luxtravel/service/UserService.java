package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static adelina.luxtravel.utility.Constants.INVALID_EMAIL;
import static adelina.luxtravel.utility.Constants.INVALID_USERNAME;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) throws InvalidArgumentException {
        validateUser(user);
        validateUserFields(user);
        validateUsernameDoesNotExist(user.getUsername());
        validateEmailDoesNotExist(user.getEmail());

        String userPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(userPassword);

        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User findByUsername(String username) throws InvalidArgumentException {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException(INVALID_USERNAME);
        }
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) throws InvalidArgumentException {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException(INVALID_EMAIL);
        }
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() throws NonExistentItemException {
        List<User> users = userRepository.findAll();

        if (ObjectUtils.isEmpty(users)) {
            throw new NonExistentItemException("There are no users found");
        }
        return users;
    }

    public void updatePassword(String username, String newPassword, String oldPassword) throws InvalidArgumentException {
        validateUpdatePassword(username, newPassword, oldPassword);

        User user = findByUsername(username);

        validatePasswordMatch(oldPassword, user.getPassword());

        String newHashedPassword = passwordEncoder.encode(newPassword);

        userRepository.updatePassword(newHashedPassword, username);
    }

    public void updateEmail(String newEmail, String oldEmail, String password) throws InvalidArgumentException {
        validateUpdateEmail(newEmail, oldEmail, password);

        User user = findByEmail(oldEmail);

        validatePasswordMatch(password, user.getPassword());

        validateEmailDoesNotExist(newEmail);

        userRepository.updateEmail(newEmail, oldEmail);
    }

    public void deleteByUsername(String username, String password) throws InvalidArgumentException {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }

        validatePassword(password);

        User user = findByUsername(username);

        validatePasswordMatch(password, user.getPassword());
        userRepository.deleteByUsername(username);
    }

    public void deleteByEmail(String email, String password) throws InvalidArgumentException {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException(INVALID_EMAIL);
        }

        validatePassword(password);

        User user = findByEmail(email);

        validatePasswordMatch(password, user.getPassword());
        userRepository.deleteByEmail(email);
    }

    private void validateUser(User user) throws InvalidArgumentException {
        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }
    }

    private void validateUserFields(User user) throws InvalidArgumentException {
        String username = user.getUsername();

        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException(INVALID_USERNAME);
        }

        String email = user.getEmail();

        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException(INVALID_EMAIL);
        }

        validatePassword(user.getPassword());
    }

    private void validatePassword(String password) throws InvalidArgumentException {
        if (StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Invalid password");
        }
    }

    private void validateUsernameDoesNotExist(String username) throws InvalidArgumentException {
        User user = findByUsername(username);

        if (username.equals(user.getUsername())) {
            throw new AlreadyExistingItemException("User with this user name already exists");
        }
    }

    private void validateEmailDoesNotExist(String email) throws InvalidArgumentException {
        User user = findByEmail(email);

        if (email.equals(user.getEmail())) {
            throw new AlreadyExistingItemException("User with this email name already exists");
        }
    }

    private void validatePasswordMatch(String expectedPassword, String actualPassword) throws
            InvalidArgumentException {
        if (!passwordEncoder.matches(expectedPassword, actualPassword)) {
            throw new InvalidArgumentException("Passwords do not match");
        }
    }

    private void validateUpdateEmail(String newEmail, String oldEmail, String password) throws InvalidArgumentException {
        if (StringUtils.isEmpty(newEmail)) {
            throw new InvalidArgumentException("Invalid new email");
        }
        if (StringUtils.isEmpty(oldEmail)) {
            throw new InvalidArgumentException("Invalid old email");
        }
        if (newEmail.equals(oldEmail)) {
            throw new InvalidArgumentException("New email can not be the same as the current");
        }

        validatePassword(password);
    }

    private void validateUpdatePassword(String username, String newPassword, String oldPassword) throws InvalidArgumentException {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException(INVALID_USERNAME);
        }

        validatePassword(newPassword);
        validatePassword(oldPassword);

        if (newPassword.equals(oldPassword)) {
            throw new InvalidArgumentException("New password can not be the same as the new one");
        }
    }
}