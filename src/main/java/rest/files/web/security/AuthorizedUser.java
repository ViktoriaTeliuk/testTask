package rest.files.web.security;

import rest.files.model.User;

import java.util.ArrayDeque;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private User user;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true, new ArrayDeque<>());
        this.user = user;
    }

    public int getId() {
        return user.getId();
    }

    public void update(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return user.toString();
    }
}