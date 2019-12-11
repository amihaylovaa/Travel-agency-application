package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return getExistingUserByUsername(username);
    }

    public User findByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid email");
        }
        return getExistingUserByEmail(email);
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (ObjectUtils.isEmpty(users)) {
            throw new NonExistentItemException("There no users found");
        }
        return users;
    }

    public User updatePassword(String newPassword, String currentPassword, String username) {
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(currentPassword)
                || StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }
        userRepository.updatePassword(newPassword, currentPassword, username);
        return findByUsername(username);
    }

    public User updateEmail(String newEmail, String currentEmail) {
        if (StringUtils.isEmpty(newEmail) || StringUtils.isEmpty(currentEmail)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }
        userRepository.updateEmail(newEmail, currentEmail);
        return findByEmail(newEmail);
    }

    public void deleteByUsername(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Delete can not be executed, invalid parameters");
        }
        validatePasswordMatch(password, findByUsername(username));
        userRepository.deleteByUsername(username);
    }

    public void deleteByEmail(String email, String password) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Delete can not be executed, invalid parameters");
        }
        validatePasswordMatch(password, findByEmail(email));
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

    private void validatePasswordMatch(String password, User user) {
        if (!password.equals(user.getPassword())) {
            throw new InvalidArgumentException("Passwords do not match");
        }
    }

    private User getExistingUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new NonExistentItemException("User with that username not exist");
        }
        return user;
    }

    private User getExistingUserByEmail(String email) {
        User user = userRepository.findByUsername(email);

        if (user == null) {
            throw new NonExistentItemException("User with that email does not exist");
        }
        return user;
    }
}