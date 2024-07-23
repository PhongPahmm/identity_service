/* (C)2024 */
package spring.usercrud.configuration;

import java.time.LocalDate;
import java.util.HashSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.usercrud.constant.PredefinedRole;
import spring.usercrud.entity.Role;
import spring.usercrud.entity.User;
import spring.usercrud.repository.RoleRepository;
import spring.usercrud.repository.UserRepository;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_USER)
                        .description("User role")
                        .build());
                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_ADMIN)
                        .description("Admin role")
                        .build());

                var role = new HashSet<Role>();
                role.add(adminRole);

                User user =
                        User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin"))
                                .firstName("Admin")
                                .lastName("User")
                                .dateOfBirth(LocalDate.of(2003, 10, 18))
                                .roles(role)
                                .build();

                userRepository.save(user);
                log.info("User admin has been created");
            }
        };
    }
}
