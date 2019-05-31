package rest.files.util;

import rest.files.model.User;

public class UserUtil {

    public static User prepareToSave(User user/*, PasswordEncoder passwordEncoder*/) {
/*
        String password = user.getPassword();
        user.setPassword(StringUtils.isEmpty(password) ? password : passwordEncoder.encode(password));
*/
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }

}