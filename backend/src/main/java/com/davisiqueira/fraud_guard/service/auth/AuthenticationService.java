package com.davisiqueira.fraud_guard.service.auth;

import com.davisiqueira.fraud_guard.config.SecurityConfiguration;
import com.davisiqueira.fraud_guard.dto.auth.CreateUserDTO;
import com.davisiqueira.fraud_guard.dto.auth.LoginRequestDTO;
import com.davisiqueira.fraud_guard.dto.auth.LoginResponseDTO;
import com.davisiqueira.fraud_guard.model.RoleModel;
import com.davisiqueira.fraud_guard.model.UserModel;
import com.davisiqueira.fraud_guard.repository.RoleRepository;
import com.davisiqueira.fraud_guard.repository.UserRepository;
import com.davisiqueira.fraud_guard.security.RoleName;
import com.davisiqueira.fraud_guard.service.user.JwtService;
import com.davisiqueira.fraud_guard.service.user.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SecurityConfiguration securityConfiguration;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService, SecurityConfiguration securityConfiguration, UserRepository userRepository, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.securityConfiguration = securityConfiguration;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public LoginResponseDTO authenticateUser(LoginRequestDTO request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new LoginResponseDTO(jwtService.generateToken(userDetails));
    }

    public void createUser(CreateUserDTO user) {
        final RoleName roleName = user.role();

        RoleModel role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(RoleModel.builder().name(roleName).build()));

        UserModel newUser = UserModel.builder()
                .cpf(user.cpf())
                .email(user.email())
                .password(securityConfiguration.passwordEncoder().encode(user.password()))
                .roles(Set.of(role))
                .build();

        userRepository.save(newUser);
    }
}
