package testTask.storage;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import rest.files.model.User;
import rest.files.model.UserFile;
import rest.files.to.FileTo;
import rest.files.to.ResponseAnswer;
import rest.files.to.ResponseTo;
import testTask.storage.util.TestUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestData {

    public static Integer USER_ID_1 = 1;
    public static Integer USER_ID_2 = 2;
    public static Integer USER_ID_3 = 3;

    public static User USER_1 = new User(USER_ID_1, "User1", "user1@yandex.ru", "password1");
    public static User USER_2 = new User(USER_ID_2, "User2", "user2@yandex.ru", "password2");
    public static User USER_3 = new User(USER_ID_3, "User3", "user3@yandex.ru", "password3");

    public static List<User> USERS = List.of(USER_1, USER_2, USER_3);

    public static ResponseTo RESPONSE_SUCCESS = new ResponseTo(ResponseAnswer.SUCCESS);
    public static ResponseTo RESPONSE_USER_1_FILES = new ResponseTo(ResponseAnswer.SUCCESS);
    public static ResponseTo RESPONSE_USER_1_FILES_PATH_ONLY = new ResponseTo(ResponseAnswer.SUCCESS);
    public static ResponseTo RESPONSE_USER_NEW_FILE = new ResponseTo(ResponseAnswer.SUCCESS);

    private static String USER_1_FILE_1_PATH = "/parentFolder/1.txt";
    private static String USER_1_FILE_2_PATH = "/parentFolder/2.txt";
    public static String USER_1_FILE_NEW_NAME = "newFile.txt";
    public static String USER_1_FILE_NEW_PATH = "/parentFolder/" + USER_1_FILE_NEW_NAME;

    public static String FILE_SEARCH_MASK = "?.txt";

    static {
        List<FileTo> USER1_FILES = new ArrayList<>();
        USER1_FILES.add(new FileTo("1.txt", USER_1_FILE_1_PATH));
        USER1_FILES.add(new FileTo("2.txt", USER_1_FILE_2_PATH));

        RESPONSE_USER_1_FILES.setObjects(USER1_FILES);
        RESPONSE_USER_NEW_FILE.setObject(new FileTo("newFile.txt"));
        RESPONSE_USER_1_FILES_PATH_ONLY.setQuery(FILE_SEARCH_MASK);
        RESPONSE_USER_1_FILES_PATH_ONLY.setObjects(List.of(new FileTo(null, USER_1_FILE_1_PATH), new FileTo(null, USER_1_FILE_2_PATH)));
    }

    public static UserFile USER_1_FILE1 = new UserFile(10, "1.txt", USER_1_FILE_1_PATH,
            LocalDateTime.of(2019, 5, 30, 10, 0), 134, MediaType.TEXT_PLAIN_VALUE, 1);
    public static UserFile USER_1_FILE2 = new UserFile(11, "2.txt", USER_1_FILE_2_PATH,
            LocalDateTime.of(2019, 5, 30, 10, 1), 234, MediaType.TEXT_PLAIN_VALUE, 1);

    public static List<UserFile> USER_1_FILES = List.of(USER_1_FILE1, USER_1_FILE2);

    public static <T> void assertMatch(T actual, T expected) {
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static <T> void assertMatch(T actual, T expected, String... ignore) {
        assertThat(actual).usingComparatorForFields((x, y) -> 0, ignore).isEqualToComparingFieldByFieldRecursively(expected);
    }

    public static <T> ResultMatcher contentJson(Class clazz, T... expected) {
        return result -> assertMatch(TestUtil.readListFromJsonMvcResult(result, clazz), List.of(expected));
    }

}