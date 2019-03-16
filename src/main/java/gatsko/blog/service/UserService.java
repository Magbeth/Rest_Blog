package gatsko.blog.service;


import gatsko.blog.model.User;

public interface UserService {

    User findByEmail(String email);

    User findByUsername(String username);

    boolean emailExists(String email);

    boolean usernameExists(String username);

    User currentUser();

}
