package rest.files.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rest.files.model.UserFile;
import rest.files.service.StorageService;
import rest.files.to.FileTo;
import rest.files.to.ResponseAnswer;
import rest.files.to.ResponseTo;
import rest.files.util.FileUtil;
import rest.files.util.exceptions.IllegalRequestDataException;
import rest.files.web.security.AuthorizedUser;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping(value = ProfileController.REST_URL)
public class StorageController {
    public static final String ROOT_DIRECTORY = "D:" + File.separator + "temp";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final StorageService service;

    @Autowired
    public StorageController(StorageService service) {
        this.service = service;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseTo getAll(@AuthenticationPrincipal AuthorizedUser user) {
        log.info("get All for user " + user.getUsername());
        ResponseTo result = new ResponseTo(ResponseAnswer.SUCCESS);
        result.setObjects(service.getAll(user.getId()));
        return result;
    }

    @DeleteMapping(value = "/{name:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseTo remove(@PathVariable String name, @AuthenticationPrincipal AuthorizedUser user) {
        log.info(String.format("delete %s for user %s", name, user.getUsername()));
        service.DeleteByName(user.getId(), name);
        return new ResponseTo(ResponseAnswer.SUCCESS);
    }

    @GetMapping(value = "/{name:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserFile get(@PathVariable String name, @AuthenticationPrincipal AuthorizedUser user) {
        log.info("get info for " + name);
        return service.get(user.getId(), name);
    }

    @GetMapping(value = "/{name:.+}", params = "alt")
    public void get(HttpServletResponse response, @PathVariable String name,
                    @RequestParam String alt, @AuthenticationPrincipal AuthorizedUser user) throws IOException {
        log.info("load file to response body " + name);
        UserFile userFile = service.get(user.getId(), name);
        Path path = Paths.get(ROOT_DIRECTORY + userFile.getPath());

        response.setStatus(HttpStatus.OK.value());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + userFile.getPath());
        response.setContentType(userFile.getContentType());
        response.setContentLength((int) userFile.getSize());
        response.setCharacterEncoding("UTF-8");

        FileUtil.writeFileToStream(path.toString(), response.getOutputStream());
    }

    @PostMapping(value = "/upload"/*, consumes = "multipart/related"*/)
    public ResponseTo upload(@RequestPart("file") MultipartFile multipartFile, @AuthenticationPrincipal AuthorizedUser user) throws IOException {

        String name = multipartFile.getOriginalFilename();

        log.info("Upload file " + name);

        if (name == null || name.length() == 0) throw new IllegalRequestDataException("upload wrong file " + name);

        File serverFile = new File(ROOT_DIRECTORY + File.separator + name);
        multipartFile.transferTo(serverFile);

        UserFile userFile = service.get(user.getId(), serverFile.getName());
        if (userFile != null) {
            userFile.setSize(multipartFile.getSize());
            service.update(userFile, userFile.getId());
        } else {
            BasicFileAttributes attr = Files.readAttributes(serverFile.toPath(), BasicFileAttributes.class);
            service.create(new UserFile(null, serverFile.getName(), name, LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault()),
                    multipartFile.getSize(), multipartFile.getContentType(), user.getId()));
        }
        ResponseTo response = new ResponseTo(ResponseAnswer.SUCCESS);
        response.setObject(new FileTo(serverFile.getName()));
        return response;
    }

    @PostMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseTo find(@RequestParam String filter, @AuthenticationPrincipal AuthorizedUser user) {
        ResponseTo response = new ResponseTo(ResponseAnswer.SUCCESS);
        response.setQuery(filter);
        response.setObjects(service.getFiltered(user.getId(), filter));
        return response;
    }
}