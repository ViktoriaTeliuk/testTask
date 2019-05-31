package rest.files.service;

import rest.files.model.UserFile;
import rest.files.to.FileTo;

import java.util.List;

public interface StorageService {
    List<FileTo> getAll(int userId);

    List<FileTo> getFiltered(int userId, String filter);

    void DeleteByName(int userId, String name);

    UserFile get(int userId, String name);

    void update(UserFile file, int id);

    UserFile create(UserFile file);
}
