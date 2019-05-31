package rest.files.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import rest.files.model.UserFile;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface StorageRepository extends JpaRepository<UserFile, Integer> {

    List<UserFile> findAllByUserId(int userId);

    @Transactional
    @Modifying
    @Override
    void deleteById(Integer integer);

    @Override
    @Transactional
    UserFile save(UserFile user);

    UserFile getUserFileByUserIdAndName(int userId, String name);

    Optional<UserFile> findUserFileByUserIdAndName(int userId, String name);

    @Query("select uf from UserFile uf where uf.userId = :userId and uf.name like :filter")
    List<UserFile> getFiltered(@Param("userId") int userId, @Param("filter") String filter);
}
