package adelina.luxtravel.service;

import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    void save(User user) {
        validateUser(user);
        userRepository.save(user);
    }

    User findByUsername(String username) {
        if (!isFieldValid(username)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return userRepository.findByUsername(username);
    }

    void updatePassword(String newPassword, String currentPassword, String username) {
        if (!isFieldValid(newPassword) || !isFieldValid(username) || !isFieldValid(currentPassword)) {
            throw new InvalidArgumentException("Update can not be executed, bad parameters");
        }
        userRepository.updatePassword(newPassword, currentPassword, username);
    }

    void updateEmail(String newEmail, String currentEmail, String username) {
        if (!isFieldValid(newEmail) || !isFieldValid(username) || !isFieldValid(currentEmail)) {
            throw new InvalidArgumentException("Update can not be executed, bad parameters");
        }
        userRepository.updateEmail(newEmail, currentEmail, username);
    }

    void delete(String username, String password) {
        if (!isFieldValid(username) || !isFieldValid(password)) {
            throw new InvalidArgumentException("Invalid username or password");
        }
        userRepository.delete(username, password);
    }

    void validateUser(User user) {
        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }

        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();

        if (!isFieldValid(username) || !isFieldValid(email) || isFieldValid(password)) {
            throw new InvalidArgumentException("Invalid fields");
        }
    }

    boolean isFieldValid(String field) {
        return field != null || !field.isEmpty();
    }
}