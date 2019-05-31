package rest.files.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import rest.files.model.User;
import rest.files.repository.UserRepository;
import rest.files.util.UserUtil;
import rest.files.util.ValidationUtil;
import rest.files.util.exceptions.NotFoundException;
import rest.files.web.security.AuthorizedUser;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository repository;

 /*   @Autowired
    private PasswordEncoder passwordEncoder;
*/
    @Override
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(UserUtil.prepareToSave(user));
    }

    @Override
    public User get(int id) throws NotFoundException {
        return ValidationUtil.checkNotFoundWithId(repository.findById(id).get(), id);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        Assert.notNull(email, "email must not be null");
        return ValidationUtil.checkNotFound(repository.getByEmail(email), "email=" + email);
    }

     @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}