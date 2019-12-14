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

    public User save(User user) {
        validateUser(user);

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return getUserByUsername(username);
    }

    public User findByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid email");
        }
        return getUserByEmail(email);
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (ObjectUtils.isEmpty(users)) {
            throw new NonExistentItemException("There are no users found");
        }
        return users;
    }

    // TODO : think about return result - need optimization
    public void updatePassword(String username, String newPassword, String oldPassword) {
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }

        User user = findByUsername(username);

        validatePasswordMatch(oldPassword, user.getPassword());

        String hashedPassword = passwordEncoder.encode(newPassword);

        userRepository.updatePassword(hashedPassword, oldPassword, username);
    }

    // TODO : same
    public void updateEmail(String newEmail, String oldEmail, String password) {
        if (StringUtils.isEmpty(newEmail) || StringUtils.isEmpty(oldEmail) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }

        User user = findByEmail(password);

        validatePasswordMatch(password, user.getPassword());

        userRepository.updateEmail(newEmail, oldEmail);
       // return findByEmail(newEmail);
    }

    public void deleteByUsername(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Delete can not be executed, invalid parameters");
        }

        User user = findByUsername(username);

        validatePasswordMatch(password, user.getPassword());
        userRepository.deleteByUsername(username);
    }

    public void deleteByEmail(String email, String password) {
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

    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }
        validateUserFields(user);
    }

    private void validateUserFields(User user) {
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

    private void validatePasswordMatch(String expectedPassword, String actualPassword) {
        if (!passwordEncoder.matches(expectedPassword, actualPassword)) {
            throw new InvalidArgumentException("Passwords do not match");
        }
    }

    private User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new NonExistentItemException("User with this username not exist");
        }
        return user;
    }

    private User getUserByEmail(String email) {
        User user = userRepository.findByUsername(email);

        if (user == null) {
            throw new NonExistentItemException("User with this email does not exist");
        }
        return user;
    }
}