package rest.files.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import rest.files.model.UserFile;
import rest.files.repository.StorageRepository;
import rest.files.to.FileTo;
import rest.files.util.StorageMapper;
import rest.files.util.ValidationUtil;


import java.util.List;

import static rest.files.util.ValidationUtil.checkNotFoundWithId;

@Service
public class StorageServiceImpl implements StorageService{

    private final StorageRepository repository;

    @Autowired
    public StorageServiceImpl(StorageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FileTo> getAll(int userId) {
        return StorageMapper.toListTo(repository.findAllByUserId(userId));
    }

    @Override
    public UserFile get(int userId, String name) {
        return repository.getUserFileByUserIdAndName(userId, name);
    }

    @Override
    @Transactional
    public void DeleteByName(int userId, String name) {
        UserFile userFile = repository.findUserFileByUserIdAndName(userId, name).orElse(null);
        ValidationUtil.checkNotFound(userFile, name + " not found");
        repository.deleteById(userFile.getId());
    }

    @Override
    public void update(UserFile file, int id) {
        Assert.notNull(file, "object must not be null");
        checkNotFoundWithId(repository.save(file), id);
    }

    @Override
    public UserFile create(UserFile file) {
        Assert.notNull(file, "object must not be null");
        return repository.save(file);
    }

    @Override
    public List<FileTo> getFiltered(int userId, String filter) {
        // covert from file mask to sql wildcard
        filter = filter.replace("?", "_");
        filter = filter.replace("*", "%");
        return StorageMapper.toPathOnlyListTo(repository.getFiltered(userId, filter));
    }
}
