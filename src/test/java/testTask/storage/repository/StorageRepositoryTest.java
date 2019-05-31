package testTask.storage.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rest.files.model.UserFile;
import rest.files.repository.StorageRepository;
import testTask.storage.TestData;

import java.util.List;

import static testTask.storage.TestData.*;

public class StorageRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private StorageRepository repository;

    @Test
    public void testGetAll() {
        List<UserFile> files = repository.findAllByUserId(USER_ID_1);
        TestData.assertMatch(files, TestData.USER_1_FILES);
    }

    @Test
    public void testDelete() {
        repository.deleteById(TestData.USER_1_FILE1.getId());
        TestData.assertMatch(repository.findAllByUserId(USER_ID_1), List.of(USER_1_FILE2));
    }

}
