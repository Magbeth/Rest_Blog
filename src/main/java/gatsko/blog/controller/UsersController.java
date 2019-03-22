package gatsko.blog.controller;

import gatsko.blog.model.User;
import gatsko.blog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public User getUsersByFirstName(@RequestParam(name = "username") String username) {
        Optional<User> user = usersRepository.findByUsername(username);
        return user.get();
    }
}
