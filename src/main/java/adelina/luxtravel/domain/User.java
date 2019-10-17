package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;

public class User {
    private City username;
    private City email;
    private City password;

    public User(City username, City email, City password) {
        initializeFields(username, email, password);
    }

    private void initializeFields(City username, City email, City password) {
        if (username == null || username.isEmpty()) {
            throw new FailedInitializationException("Invalid username");
        } else if (email == null || email.isEmpty()) {
            throw new FailedInitializationException("Invalid email");
        } else if (password == null || password.isEmpty() || password.length() < 8) {
            throw new FailedInitializationException("Invalid password");
        }
        this.username = new City(username);
        this.email = new City(email);
        this.password = new City( password);
    }
}