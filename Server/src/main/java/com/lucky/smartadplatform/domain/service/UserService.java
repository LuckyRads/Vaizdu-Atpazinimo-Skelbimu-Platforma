package com.lucky.smartadplatform.domain.service;

import java.util.Optional;
import java.util.stream.Collectors;

import com.lucky.smartadplatform.domain.Role;
import com.lucky.smartadplatform.domain.User;
import com.lucky.smartadplatform.domain.repository.RoleRepository;
import com.lucky.smartadplatform.domain.repository.UserRepository;
import com.lucky.smartadplatform.domain.type.RoleType;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaRole;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository<JpaUser> userRepository;

    @Autowired
    private RoleRepository<JpaRole> roleRepository;

    @Transactional
    public User addRoleToUser(String username, String toBeAssignedRoleName)
            throws NullPointerException, IllegalArgumentException {
        RoleType toBeAssignedRoleType = null;
        try {
            toBeAssignedRoleType = RoleType.valueOf(toBeAssignedRoleName);
        } catch (Exception e) {
            throw new IllegalArgumentException("Role type is not correct!");
        }
        if (toBeAssignedRoleType.equals(RoleType.ROLE_USER))
            throw new IllegalArgumentException("User role cannot be user!");

        JpaUser jpaUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("User not found!"));
        User user = modelMapper.map(jpaUser, User.class);

        if (user.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(toBeAssignedRoleType))
            throw new IllegalArgumentException("User already has this role.");

        Optional<JpaRole> jpaRole = roleRepository.findByName(toBeAssignedRoleType);
        Role role = null;
        if (!jpaRole.isPresent()) {
            role = new Role(toBeAssignedRoleType);
            jpaRole = Optional.of(modelMapper.map(role, JpaRole.class));
            roleRepository.save(jpaRole.get());
        }

        jpaUser.getRoles().add(jpaRole.get());

        return modelMapper.map(jpaUser, User.class);
    }

}
