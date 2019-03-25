package gatsko.blog.service;

import gatsko.blog.model.Role;
import gatsko.blog.repository.RoleRepository;
import gatsko.blog.service.apiInterface.RoleService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Collection<Role> findAll() {
        return roleRepository.findAll();
    }
}
