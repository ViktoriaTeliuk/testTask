package rest.files.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import rest.files.model.User;
import rest.files.repository.UserRepository;
import rest.files.util.exceptions.DuplicateEmailException;

@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {

    @Autowired
    private UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        User dbUser = repository.getByEmail(user.getEmail().toLowerCase());
        if (dbUser != null && !dbUser.getId().equals(user.getId())) {
            throw new DuplicateEmailException(dbUser.getEmail());
        }
    }
}
