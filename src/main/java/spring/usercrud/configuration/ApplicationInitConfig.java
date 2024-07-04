package spring.usercrud.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.usercrud.entity.User;
import spring.usercrud.enums.Role;
import spring.usercrud.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashSet;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .firstName("Admin")
                        .lastName("User")
                        .dateOfBirth(LocalDate.of(2003, 10, 18))
                        .build();

                userRepository.save(user);
                log.info("User admin has been created");
            }
        };
    }
}
