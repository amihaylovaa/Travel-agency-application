package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;

public class User {
    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        initializeFields(username, email, password);
    }

    private void initializeFields(String username, String email, String password) {
        if (username == null || username.isEmpty()) {
            throw new FailedInitializationException("Invalid username");
        } else if (email == null || email.isEmpty()) {
            throw new FailedInitializationException("Invalid email");
        } else if (password == null || password.isEmpty() || password.length() < 8) {
            throw new FailedInitializationException("Invalid password");
        }
        this.username = new String(username);
        this.email = new String(email);
        this.password = new String( password);
    }
}