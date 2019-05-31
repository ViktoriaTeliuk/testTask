package rest.files.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import rest.files.model.User;
import rest.files.service.UserService;
import rest.files.to.ResponseAnswer;
import rest.files.to.ResponseTo;
import rest.files.util.*;
import rest.files.web.security.AuthorizedUser;
import rest.files.web.security.JwtTokenProvider;

import java.time.LocalDateTime;
import java.time.ZoneId;


@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    public static final String REST_URL = "/storage";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService service;

 /*   @Autowired
    private PasswordEncoder passwordEncoder;

 */ @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseTo register(@Validated(UserValidation.class) @RequestBody User user) {
        try {
            log.info("creating new user");
            service.create(user);
            return new ResponseTo(ResponseAnswer.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseTo(ResponseAnswer.ERROR, e.getCause().getMessage());
        }
    }


    @PostMapping(value = "/login")
    public ResponseTo login(@AuthenticationPrincipal AuthorizedUser user) {
        ResponseTo result = new ResponseTo(ResponseAnswer.SUCCESS);
 //       Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        result.setToken(jwtTokenProvider.generateToken(user.getUser()));
        result.setExpiresAt(LocalDateTime.ofInstant(jwtTokenProvider.getExpiredDate().toInstant(), ZoneId.systemDefault()));
        return result;
    }

    /*Extends the valiblity of the token with 30 min

Request:
POST /storage/login/refresh
"AuthorizationToken":"12sdsds34566assda778989asas"

{"status": "success", "token":"12sdsds34566assda778989asas", "expiresAt":"2018-01-01 00:30:00"}
{"status": "error", "message":"Token expired"} */

/*
    @PostMapping(value = "/login/refresh", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseTo refresh(String token) {

    }
*/

/*Invalidates the token passed as an argument

Request:
POST /storage/logout
"AuthorizationToken":"12sdsds34566assda778989asas"
{"status": "success"}
{"status": "error", "message":"Token expired"} */

/*
    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseTo login(@Validated(UserValidation.class) @RequestBody UserTo userTo) {

    }
*/

}