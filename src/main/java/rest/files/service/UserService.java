package rest.files.service;

import rest.files.util.exceptions.NotFoundException;
import rest.files.model.User;

public interface UserService {

    User create(User user);

    User get(int id) throws NotFoundException;

    User getByEmail(String email) throws NotFoundException;
}