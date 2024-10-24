package com.hotels.roomwiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hotels.roomwiz.exception.RoleAlreadyExistsException;
import com.hotels.roomwiz.exception.UserAlreadyExistsException;
import com.hotels.roomwiz.model.Role;
import com.hotels.roomwiz.model.User;
import com.hotels.roomwiz.repository.RoleRepository;
import com.hotels.roomwiz.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if(roleRepository.existsByName(role)) {
            throw new RoleAlreadyExistsException(theRole.getName() + " role already exists.");
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
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);

        if(role.isPresent() && role.get().getUsers().contains(user.get())) {
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }

        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);

        if(user.isPresent() && user.get().getRoles().contains(role.get())) {
            throw new UserAlreadyExistsException(user.get().getFirstName() + " is already assign to the " + role.get().getName() + " role");
        }

        if(role.isPresent()) {
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
