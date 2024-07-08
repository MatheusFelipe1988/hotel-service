package com.hotel.lakeside.service;

import com.hotel.lakeside.exception.RoleAlreadyExistException;
import com.hotel.lakeside.exception.UserAlreadyExistsException;
import com.hotel.lakeside.model.Role;
import com.hotel.lakeside.model.User;
import com.hotel.lakeside.repository.RoleRepository;
import com.hotel.lakeside.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements IRoleService{

    private final UserRepository repository;
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_"+theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if (roleRepository.existsByName(role)){
            throw new RoleAlreadyExistException(theRole.getName()+ "role already exist");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }


    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user = repository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> user = repository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if (user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new UserAlreadyExistsException(user.get().getFirstName() +
                    "is already assigned to this" + role.get().getName() + "role");
        }if (role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.get().removeAllUsersFromRole();
        return roleRepository.save(role.get());
    }
}
