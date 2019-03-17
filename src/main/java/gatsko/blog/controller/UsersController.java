package gatsko.blog.controller;

import gatsko.blog.model.User;
import gatsko.blog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getUsersByFirstName(@RequestParam(name = "first_name") String firstName) {
        List<User> users = usersRepository.findAllByFirstName(firstName);
        return users;
    }
}
