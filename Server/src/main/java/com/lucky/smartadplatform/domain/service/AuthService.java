package com.lucky.smartadplatform.domain.service;

import com.lucky.smartadplatform.domain.Role;
import com.lucky.smartadplatform.domain.User;
import com.lucky.smartadplatform.domain.repository.RoleRepository;
import com.lucky.smartadplatform.domain.repository.UserRepository;
import com.lucky.smartadplatform.domain.type.RoleType;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaRole;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository<JpaUser> userRepository;

	@Autowired
	private RoleRepository<JpaRole> roleRepository;

	public User createUser(String username, String email, String password, String validationPassword) {
		User user = new User(username, email, password, validationPassword, passwordEncoder);

		JpaRole jpaUserRole;
		Role userRole;
		try {
			jpaUserRole = roleRepository.findByName(RoleType.ROLE_USER).orElseThrow(NullPointerException::new);
			userRole = modelMapper.map(jpaUserRole, Role.class);
		} catch (NullPointerException e) {
			userRole = new Role(RoleType.ROLE_USER);
			roleRepository.save(modelMapper.map(userRole, JpaRole.class));
		}
		user.addRole(userRole);

		userRepository.save(modelMapper.map(user, JpaUser.class));
		return user;
	}

	public boolean areCredentialsValid(String username, String password) {
		JpaUser user;
		try {
			user = userRepository.findByUsername(username)
					.orElseThrow(NullPointerException::new);
		} catch (NullPointerException e) {
			return false;
		}
		return passwordEncoder.matches(password, user.getPassword());
	}

}
