package testTask.storage.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import rest.files.model.User;
import rest.files.repository.UserRepository;
import testTask.storage.TestData;

public class UserRepositoryTest extends AbstractRepositoryTest{

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSave() {
        User user = new User(TestData.USER_1);
        user.setEmail("edited@gmail.com");
        TestData.assertMatch(userRepository.save(user), user);
    }

    @Test
    public void testDelete() {
        userRepository.deleteById(TestData.USER_ID_1);
        TestData.assertMatch(userRepository.findAll(Sort.by("id")), TestData.USERS);
    }

    @Test
    public void testGet() {
        User user = userRepository.findById(TestData.USER_ID_1).orElse(null);
        TestData.assertMatch(user, TestData.USER_1, "registered");
    }

    @Test
    public void testGetByEmail() {
        User user = userRepository.getByEmail(TestData.USER_1.getEmail());
        TestData.assertMatch(user, TestData.USER_1, "registered");
    }

    @Test
    public void testGetAll() {
        TestData.assertMatch(userRepository.findAll(Sort.by("id")), TestData.USERS);
    }
}