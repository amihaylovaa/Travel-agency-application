package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.INVALID_EMAIL;
import static adelina.luxtravel.utility.Constants.INVALID_USERNAME;

@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        validateUser(user);

        String userPassword = user.getPassword();

        user.setPassword(passwordEncoder.encode(userPassword));

        return userRepository.save(user);
    }

    public void login(String username, String password) {
        authenticationByUsername(username, password);
    }

    public User findByUsername(String username) {
        validateUsername(username);

        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new NonExistentItemException("User with this username does not exist");
        }
        return user.get();
    }

    public User findByEmail(String email) {
        validateEmail(email);

        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new NonExistentItemException("User with this email does not exist");
        }
        return user.get();
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (ObjectUtils.isEmpty(users)) {
            throw new NonExistentItemException("There are no users found");
        }
        return users;
    }

    public void updatePassword(String username, String newPassword, String oldPassword) {
        validatePassword(newPassword);
        authenticationByUsername(username, oldPassword);

        if (newPassword.equals(oldPassword)) {
            throw new InvalidArgumentException("New password can not be the same as the new one");
        }

        String newHashedPassword = passwordEncoder.encode(newPassword);

        userRepository.updatePassword(newHashedPassword, username);
    }

    public void updateEmail(String newEmail, String oldEmail, String password) {
        validateEmail(newEmail);
        authenticationByEmail(oldEmail, password);
        validateEmailDoesNotExist(newEmail);

        userRepository.updateEmail(newEmail, oldEmail);
    }

    public void deleteByUsername(String username, String password) {
        authenticationByUsername(username, password);

        userRepository.deleteByUsername(username);
    }

    private void validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Invalid password");
        }
    }

    private void validateUsernameDoesNotExist(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            throw new AlreadyExistingItemException("User with this username already exists");
        }
    }

    private void validateEmailDoesNotExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new AlreadyExistingItemException("User with this email already exists");
        }
    }

    private void validatePasswordMatch(String expectedPassword, String encryptedActualPassword) {
        if (!(passwordEncoder.matches(expectedPassword, encryptedActualPassword))) {
            throw new InvalidArgumentException("Passwords do not match");
        }
    }

    private void validateUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException(INVALID_USERNAME);
        }
    }

    private void validateEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException(INVALID_EMAIL);
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }

        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();

        validateUsername(username);
        validateEmail(email);
        validatePassword(password);
        validateUsernameDoesNotExist(username);
        validateEmailDoesNotExist(email);
    }

    private void authenticationByUsername(String username, String password) {
        validateUsername(username);
        validatePassword(password);

        User user = findByUsername(username);

        validatePasswordMatch(password, user.getPassword());
    }

    private void authenticationByEmail(String email, String password) {
        validateEmail(email);
        validatePassword(password);

        User user = findByEmail(email);

        validatePasswordMatch(password, user.getPassword());
    }

    public void deleteByEmail(String email, String password) {
        authenticationByEmail(email, password);

        userRepository.deleteByEmail(email);
    }
}