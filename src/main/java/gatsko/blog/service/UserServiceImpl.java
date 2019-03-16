package gatsko.blog.service;

import gatsko.blog.model.User;
import gatsko.blog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public User findByEmail(String email) {
        return usersRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public User findByUsername(String username) {
        return usersRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public User currentUser() {
        return findByUsername("admin");
    }


}
