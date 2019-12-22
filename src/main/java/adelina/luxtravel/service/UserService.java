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
import java.util.Optional;

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

    public User save(User user) throws InvalidArgumentException, NonExistentItemException {
        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }

        validateUsernameDoesNotExist(user.getUsername());
        validateEmailDoesNotExist(user.getEmail());

        String userPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(userPassword);

        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User findByUsername(String username) throws InvalidArgumentException, NonExistentItemException {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException(INVALID_USERNAME);
        }
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new NonExistentItemException("User with this username does not exist");
        }
        return user.get();
    }

    public User findByEmail(String email) throws InvalidArgumentException, NonExistentItemException {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException(INVALID_EMAIL);
        }

        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new NonExistentItemException("User with this email does not exist");
        }
        return user.get();
    }

    public List<User> findAll() throws NonExistentItemException {
        List<User> users = userRepository.findAll();

        if (ObjectUtils.isEmpty(users)) {
            throw new NonExistentItemException("There are no users found");
        }
        return users;
    }

    public void updatePassword(String username, String newPassword, String oldPassword)
            throws InvalidArgumentException, NonExistentItemException {
        validatePasswordUpdate(username, newPassword, oldPassword);

        User user = findByUsername(username);

        validatePasswordMatch(oldPassword, user.getPassword());

        String newHashedPassword = passwordEncoder.encode(newPassword);

        userRepository.updatePassword(newHashedPassword, username);
    }

    public void updateEmail(String newEmail, String oldEmail, String password)
            throws InvalidArgumentException, NonExistentItemException {
        validateEmailUpdate(newEmail, oldEmail, password);

        User user = findByEmail(oldEmail);

        validatePasswordMatch(password, user.getPassword());

        validateEmailDoesNotExist(newEmail);

        userRepository.updateEmail(newEmail, oldEmail);
    }

    public void deleteByUsername(String username, String password)
            throws InvalidArgumentException, NonExistentItemException {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }

        validatePassword(password);

        User user = findByUsername(username);

        validatePasswordMatch(password, user.getPassword());
        userRepository.deleteByUsername(username);
    }

    public void deleteByEmail(String email, String password)
            throws InvalidArgumentException, NonExistentItemException {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException(INVALID_EMAIL);
        }

        validatePassword(password);

        User user = findByEmail(email);

        validatePasswordMatch(password, user.getPassword());
        userRepository.deleteByEmail(email);
    }

    private void validatePassword(String password) throws InvalidArgumentException {
        if (StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Invalid password");
        }
    }

    private void validateUsernameDoesNotExist(String username)
            throws InvalidArgumentException, NonExistentItemException {
        User user = findByUsername(username);

        if (username.equals(user.getUsername())) {
            throw new AlreadyExistingItemException("User with this username already exists");
        }
    }

    private void validateEmailDoesNotExist(String email)
            throws InvalidArgumentException, NonExistentItemException {
        User user = findByEmail(email);

        if (email.equals(user.getEmail())) {
            throw new AlreadyExistingItemException("User with this email already exists");
        }
    }

    private void validatePasswordMatch(String expectedPassword, String actualPassword) throws
            InvalidArgumentException {
        if (!passwordEncoder.matches(expectedPassword, actualPassword)) {
            throw new InvalidArgumentException("Passwords do not match");
        }
    }

    private void validateEmailUpdate(String newEmail, String oldEmail, String password) throws InvalidArgumentException {
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

    private void validatePasswordUpdate(String username, String newPassword, String oldPassword) throws InvalidArgumentException {
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