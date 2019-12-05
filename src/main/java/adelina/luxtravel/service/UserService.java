package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

        User user = findByUsername(username);

        if (!password.equals(user.getPassword())) {
            throw new InvalidArgumentException("Wrong password");
        }
        userRepository.deleteByUsername(username);
    }

    public void deleteByEmail(String email, String password) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Invalid email or password");
        }

        User user = findByEmail(email);

        if (!password.equals(user.getPassword())) {
            throw new InvalidArgumentException("Wrong password");
        }
        userRepository.deleteByEmail(email);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    private void validateListOfUsers(List<User> users) {
        if (users == null || users.isEmpty()) {
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

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid fields");
        }
    }

    private User getExistingUser(User user) {
        if (user == null) {
            throw new NonExistentItemException("This user does not exist");
        }
        return user;
    }
}