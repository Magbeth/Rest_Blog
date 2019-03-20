package gatsko.blog.service;

import gatsko.blog.model.Role;
import gatsko.blog.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public Collection<Role> findAll() {
        return roleRepository.findAll();
    }
}
