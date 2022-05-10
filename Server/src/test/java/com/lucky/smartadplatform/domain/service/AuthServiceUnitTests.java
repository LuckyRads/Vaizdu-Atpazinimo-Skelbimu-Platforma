package com.lucky.smartadplatform.domain.service;

import static com.lucky.smartadplatform.domain.type.RoleType.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.lucky.smartadplatform.domain.User;
import com.lucky.smartadplatform.domain.repository.RoleRepository;
import com.lucky.smartadplatform.domain.repository.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTests {

    private AuthService authService;

    @Mock
    private UserRepository<JpaUser> userRepository;

    @Mock
    private RoleRepository<JpaRole> roleRepository;

    @Captor
    private ArgumentCaptor<JpaUser> userArgumentCaptor;

    private JpaUser user1;

    @BeforeEach
    private void setUp() {
        authService = new AuthService(new BCryptPasswordEncoder(), new ModelMapper(), userRepository, roleRepository);
    }

    private void prepareUsers() {
        Set<JpaRole> userRoles = new HashSet();
        userRoles.add(TestUtils.getUserRole());

        user1 = TestUtils.getTestUser1(userRoles);
    }

    @Test
    void testAreCredentialsValidWithValidCredentialsSuccess() {
        prepareUsers();

        JpaUser user = user1;

        String username = "MatasM";
        String password = "P@ssword123";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean areCredentialsValid = authService.areCredentialsValid(username, password);

        assertEquals(true, areCredentialsValid);
    }

    @Test
    void testAreCredentialsValidWithInvalidCredentialsSuccess() {
        prepareUsers();

        JpaUser user = user1;

        String username = "MatasM";
        String password = "P@ssword1234";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean areCredentialsValid = authService.areCredentialsValid(username, password);

        assertEquals(false, areCredentialsValid);
    }

    @Test
    void testAreCredentialsValidUserNotFound() {
        prepareUsers();

        JpaUser user = user1;

        String username = "Matas";
        String password = "P@ssword1234";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean areCredentialsValid = authService.areCredentialsValid(username, password);

        assertEquals(false, areCredentialsValid);
    }

    @Test
    void testCreateUserNonExistentUserSuccess() {
        prepareUsers();

        String username = "Matas";
        String email = "matas@vilniustech.lt";
        String password = "P@ssword123";
        String validationPassword = "P@ssword123";

        Mockito.when(roleRepository.findByName(ROLE_USER)).thenReturn(Optional.of(TestUtils.getUserRole()));

        User createdUser = authService.createUser(username, email, password, validationPassword);

        assertEquals(username, createdUser.getUsername());
        assertEquals(true, createdUser.getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_USER)));
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
    }

    @Test
    void testCreateUserNonExistentRoleSuccess() {
        prepareUsers();

        String username = "Matas";
        String email = "matas@vilniustech.lt";
        String password = "P@ssword123";
        String validationPassword = "P@ssword123";

        Mockito.when(roleRepository.findByName(ROLE_USER)).thenReturn(Optional.empty());

        User createdUser = authService.createUser(username, email, password, validationPassword);

        assertEquals(username, createdUser.getUsername());
        assertEquals(true, createdUser.getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_USER)));
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
    }

}
