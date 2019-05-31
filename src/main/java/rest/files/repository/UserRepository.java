package rest.files.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import rest.files.model.User;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {
    @Override
    @Transactional
    User save(User user);

    @Override
    Optional<User> findById(Integer id);

    @Override
    List<User> findAll(Sort sort);

    User getByEmail(String email);
}
