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

    public User save(User user) throws InvalidArgumentException, NonExistentItemException {
        validateUser(user);

        String userPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(userPassword);

        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User findByUsername(String username) throws InvalidArgumentException {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) throws InvalidArgumentException {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidArgumentException("Invalid email");
        }
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (ObjectUtils.isEmpty(users)) {
            // TODO : maybe logger
            //throw new NonExistentItemException("There are no users found");
        }
        return users;
    }

    public void updatePassword(String username, String newPassword, String oldPassword) throws InvalidArgumentException {
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(oldPassword)
                || StringUtils.isEmpty(username) || newPassword.equals(oldPassword)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }

        User user = findByUsername(username);

        validatePasswordMatch(oldPassword, user.getPassword());

        String newHashedPassword = passwordEncoder.encode(newPassword);

        userRepository.updatePassword(newHashedPassword, username);
    }

    public void updateEmail(String newEmail, String oldEmail, String password) throws InvalidArgumentException, NonExistentItemException {
        if (StringUtils.isEmpty(newEmail) || StringUtils.isEmpty(oldEmail)
                || StringUtils.isEmpty(password) || newEmail.equals(oldEmail)) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }

        User user = findByEmail(oldEmail);

        validatePasswordMatch(password, user.getPassword());

        validateEmailDoesNotExist(newEmail);

        userRepository.updateEmail(newEmail, oldEmail);
    }

    public void deleteByUsername(String username, String password) throws InvalidArgumentException {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Delete can not be executed, invalid parameters");
        }

        User user = findByUsername(username);

        validatePasswordMatch(password, user.getPassword());
        userRepository.deleteByUsername(username);
    }

    public void deleteByEmail(String email, String password) throws InvalidArgumentException {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new InvalidArgumentException("Delete can not be executed, invalid parameters");
        }

        User user = findByEmail(email);

        validatePasswordMatch(password, user.getPassword());
        userRepository.deleteByEmail(email);
    }

    private void validateUser(User user) throws InvalidArgumentException, NonExistentItemException {
        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }
        validateFields(user);
        validateUsernameDoesNotExist(user.getUsername());
        validateEmailDoesNotExist(user.getEmail());
    }

    private void validateFields(User user) throws InvalidArgumentException {
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

    private void validateUsernameDoesNotExist(String username) throws InvalidArgumentException, NonExistentItemException {
        User user = findByUsername(username);

        if (username.equals(user.getUsername())) {
            throw new AlreadyExistingItemException("User with this user name already exists");
        }
    }

    private void validateEmailDoesNotExist(String email) throws InvalidArgumentException, NonExistentItemException {
        User user = findByEmail(email);

        if (email.equals(user.getEmail())) {
            throw new AlreadyExistingItemException("User with this email name already exists");
        }
    }

    private void validatePasswordMatch(String expectedPassword, String actualPassword) throws InvalidArgumentException {
        if (!passwordEncoder.matches(expectedPassword, actualPassword)) {
            throw new InvalidArgumentException("Passwords do not match");
        }
    }
}