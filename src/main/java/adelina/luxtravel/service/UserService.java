package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        validateUser(user);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return userRepository.findByEmail(email);
    }

    public void updatePassword(String newPassword, String currentPassword, String username) {
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(currentPassword) || StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }
        findByUsername(username);
        userRepository.updatePassword(newPassword, currentPassword, username);
    }

    public void updateEmail(String newEmail, String currentEmail, String username) {
        if (StringUtils.isEmpty(newEmail) || StringUtils.isEmpty(currentEmail) || StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }
        findByEmail(currentEmail);
        userRepository.updateEmail(newEmail, currentEmail, username);
    }

    public void deleteByUsername(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Invalid username or password");
        }

        User user = findByUsername(username);

        if (user == null) {
            throw new InvalidArgumentException("User with that username does not exist");
        }
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

        if (user == null) {
            throw new InvalidArgumentException("User with that username does not exist");
        }
        if (!password.equals(user.getPassword())) {
            throw new InvalidArgumentException("Wrong password");
        }
        userRepository.deleteByEmail(email);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }

        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid fields");
        }
    }
}