package com.davisiqueira.fraud_guard.service.user;

import com.davisiqueira.fraud_guard.model.RoleModel;
import com.davisiqueira.fraud_guard.model.UserModel;
import com.davisiqueira.fraud_guard.repository.UserRepository;
import com.davisiqueira.fraud_guard.security.RoleName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTests {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Nested
    class LoadUserByUsername {
        @Nested
        class Sanity {
            @Test
            void shouldReturnUserDetails_whenUserExists() {
                UserModel user = UserModel.builder()
                        .email("test@email.com")
                        .password("password")
                        .roles(new HashSet<>() {{
                            add(RoleModel.builder().name(RoleName.valueOf("ROLE_CUSTOMER")).build());
                        }})
                        .build();

                UserDetails userDetails = new UserDetailsImpl(user);

                when(repository.findWithRolesByEmail(anyString())).thenReturn(Optional.of(user));

                UserDetails result = service.loadUserByUsername(user.getEmail());

                assertEquals(userDetails.getUsername(), result.getUsername());
                assertEquals(userDetails.getPassword(), result.getPassword());
                assertEquals(userDetails.getAuthorities(), result.getAuthorities());
                verifyNoMoreInteractions(repository);
            }
        }

        @Nested
        class Boundary {
            @Test
            void shouldThrowUsernameNotFoundException_whenUsernameIsNull() {
                when(repository.findWithRolesByEmail(any())).thenReturn(Optional.empty());

                assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(null));
                verifyNoMoreInteractions(repository);
            }
        }
    }
}
