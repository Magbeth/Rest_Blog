package gatsko.blog.repository;

import gatsko.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);

    User findByEmailIgnoreCase(String email);

    List<User> findAllByFirstName(String firstName);
}
