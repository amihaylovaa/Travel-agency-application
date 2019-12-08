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

    public List<User> saveAll(List<User> users) {
        validateListOfUsers(users);
        return userRepository.saveAll(users);
    }

    public User findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return getExistingUser(userRepository.findByUsername(username));
    }

    public User findByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return getExistingUser(userRepository.findByEmail(email));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void updatePassword(String newPassword, String currentPassword, String username) {
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(currentPassword)
                || StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }
        findByUsername(username);
        userRepository.updatePassword(newPassword, currentPassword, username);
    }

    public void updateEmail(String newEmail, String currentEmail) {
        if (StringUtils.isEmpty(newEmail) || StringUtils.isEmpty(currentEmail)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }
        findByEmail(currentEmail);
        userRepository.updateEmail(newEmail, currentEmail);
    }

    public void deleteByUsername(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Invalid username or password");
        }

        validatePasswordMatch(password, findByUsername(username));
        userRepository.deleteByUsername(username);
    }

    public void deleteByEmail(String email, String password) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Invalid email or password");
        }

        validatePasswordMatch(password, findByEmail(email));
        userRepository.deleteByEmail(email);
    }

    public void deleteAll() {
        if (ObjectUtils.isEmpty(findAll())) {
            throw new NonExistentItemException("There is nothing to delete");
        }
        userRepository.deleteAll();
    }

    private void validateListOfUsers(List<User> users) {
        if (ObjectUtils.isEmpty(users)) {
            throw new InvalidArgumentException("Invalid list of users");
        }
        for (User user : users) {
            validateUser(user);
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }
        validateUserFields(user);
    }

    private void validateUserFields(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid fields");
        }
    }

    private void validatePasswordMatch(String password, User user) {
        if (!password.equals(user.getPassword())) {
            throw new InvalidArgumentException("Invalid password");
        }
    }

    private User getExistingUser(User user) {
        if (user == null) {
            throw new NonExistentItemException("This user does not exist");
        }
        return user;
    }
}