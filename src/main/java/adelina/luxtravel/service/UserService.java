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

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User findByUsername(String username) throws NonExistentItemException, InvalidArgumentException {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return getUserByUsername(username);
    }

    public User findByEmail(String email) throws NonExistentItemException, InvalidArgumentException {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid email");
        }
        return getUserByEmail(email);
    }

    public List<User> findAll() throws NonExistentItemException {
        List<User> users = userRepository.findAll();

        if (ObjectUtils.isEmpty(users)) {
            throw new NonExistentItemException("There are no users found");
        }
        return users;
    }

    public void updatePassword(String username, String newPassword, String oldPassword) throws InvalidArgumentException {
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }

        try {
            User user = findByUsername(username);

            validatePasswordMatch(oldPassword, user.getPassword());
        } catch (NonExistentItemException nonExistentItemException) {
            // logger should be added
        }
        String hashedPassword = passwordEncoder.encode(newPassword);

        userRepository.updatePassword(hashedPassword, oldPassword, username);
    }

    public void updateEmail(String newEmail, String oldEmail, String password) throws InvalidArgumentException {
        if (StringUtils.isEmpty(newEmail) || StringUtils.isEmpty(oldEmail) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }

        try {
            User user = findByEmail(password);

            validatePasswordMatch(password, user.getPassword());
        } catch (NonExistentItemException nonExistentItemException) {
            // logger should be added
        }
        userRepository.updateEmail(newEmail, oldEmail);
    }

    public void deleteByUsername(String username, String password) throws NonExistentItemException, InvalidArgumentException {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Delete can not be executed, invalid parameters");
        }

        User user = findByUsername(username);

        validatePasswordMatch(password, user.getPassword());
        userRepository.deleteByUsername(username);
    }

    public void deleteByEmail(String email, String password) throws NonExistentItemException, InvalidArgumentException {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Delete can not be executed, invalid parameters");
        }

        User user = findByEmail(email);

        validatePasswordMatch(password, user.getPassword());
        userRepository.deleteByEmail(email);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    private void validateUser(User user) throws InvalidArgumentException {
        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }
        validateUserFields(user);
    }

    private void validateUserFields(User user) throws InvalidArgumentException {
        String username = user.getUsername();

        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }

        String password = user.getPassword();

        if (StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Invalid password");
        }
        String email = user.getEmail();

        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid email");
        }
    }

    private void validatePasswordMatch(String expectedPassword, String actualPassword) throws InvalidArgumentException {
        if (!passwordEncoder.matches(expectedPassword, actualPassword)) {
            throw new InvalidArgumentException("Passwords do not match");
        }
    }

    private User getUserByUsername(String username) throws NonExistentItemException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new NonExistentItemException("User with this username not exist");
        }
        return user;
    }

    private User getUserByEmail(String email) throws NonExistentItemException {
        User user = userRepository.findByUsername(email);

        if (user == null) {
            throw new NonExistentItemException("User with this email does not exist");
        }
        return user;
    }
}