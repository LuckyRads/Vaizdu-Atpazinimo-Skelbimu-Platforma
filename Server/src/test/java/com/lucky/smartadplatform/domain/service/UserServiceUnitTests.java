package com.lucky.smartadplatform.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Optional;

import com.lucky.smartadplatform.domain.User;
import com.lucky.smartadplatform.domain.repository.RoleRepository;
import com.lucky.smartadplatform.domain.repository.UserRepository;
import com.lucky.smartadplatform.domain.type.RoleType;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaRole;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;
import com.lucky.smartadplatform.util.TestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    private UserService userService;

    @Mock
    private UserRepository<JpaUser> userRepository;

    @Mock
    private RoleRepository<JpaRole> roleRepository;

    @Captor
    private ArgumentCaptor<JpaRole> roleArgumentCaptor;

    private JpaUser user1;

    @BeforeEach
    private void setUp() {
        userService = new UserService(new ModelMapper(), userRepository, roleRepository);
    }

    private void prepareUsers() {
        user1 = TestUtils.getTestUser1(new HashSet<>());
    }

    @Test
    void testAddRoleToUserUserRoleException() {
        prepareUsers();

        JpaUser userToBeAssignedToRole = user1;

        String username = userToBeAssignedToRole.getUsername();
        String roleToBeAssignedName = "ROLE_USER";

        assertThrows(IllegalArgumentException.class,
                () -> userService.addRoleToUser(username, roleToBeAssignedName));
    }

    @Test
    void testAddRoleToUserAdminRoleExistingSuccess() {
        prepareUsers();

        JpaUser userToBeAssignedToRole = user1;

        String username = userToBeAssignedToRole.getUsername();
        String roleToBeAssignedName = "ROLE_ADMIN";

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(userToBeAssignedToRole));
        Mockito.when(roleRepository.findByName(RoleType.valueOf(roleToBeAssignedName)))
                .thenReturn(Optional.of(TestUtils.getAdminRole()));

        User user = userService.addRoleToUser(username, roleToBeAssignedName);

        assertEquals(true, user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleType.ROLE_ADMIN)));
    }

}
