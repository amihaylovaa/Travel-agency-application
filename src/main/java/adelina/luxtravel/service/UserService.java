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

    List<User> findAll() {
        return userRepository.findAll();
    }

    void updatePassword(String newPassword, String username) {
        if (!isFieldValid(newPassword) || !isFieldValid(username)) {
            throw new InvalidArgumentException("Update can not be executed, bad parameters");
        }
        userRepository.updatePassword(newPassword, username);
    }

    void updateEmail(String newEmail, String username) {
        if (!isFieldValid(newEmail) || !isFieldValid(username)) {
            throw new InvalidArgumentException("Update can not be executed, bad parameters");
        }
        userRepository.updateEmail(newEmail, username);
    }

    void delete(String username) {
        if(!isFieldValid(username)){
            throw new InvalidArgumentException("Invalid username");
        }
        userRepository.delete(username);
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
        return field == null || field.isEmpty();
    }
}