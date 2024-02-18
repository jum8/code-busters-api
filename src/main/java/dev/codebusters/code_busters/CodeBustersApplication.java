package dev.codebusters.code_busters;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.UserType;
import dev.codebusters.code_busters.repos.AppUserRepository;
import dev.codebusters.code_busters.repos.UserTypeRepository;
import dev.codebusters.code_busters.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class CodeBustersApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CodeBustersApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AppUserRepository userRepository, UserTypeRepository userTypeRepository, PasswordEncoder passwordEncoder) {
        return args -> {  // inserting data after application is up

            UserType userTypeUser = new UserType();
            userTypeUser.setTitle("USER");
            userTypeUser = userTypeRepository.save(userTypeUser);

            AppUser commonUser = new AppUser();
            commonUser.setName("Ned Flanders");
            commonUser.setEmail("nedf@mail.com");
            commonUser.setPassword(passwordEncoder.encode("password"));
            commonUser.setEnabled(true);
            commonUser.setUserType(userTypeUser);
            userRepository.save(commonUser);

            UserType userTypeAdmin = new UserType();
            userTypeAdmin.setTitle("ADMIN");
            userTypeAdmin = userTypeRepository.save(userTypeAdmin);

            AppUser adminUser = new AppUser();
            adminUser.setName("Bruce Allmighty");
            adminUser.setEmail("ba@mail.com");
            adminUser.setPassword(passwordEncoder.encode("password"));
            adminUser.setEnabled(true);
            adminUser.setUserType(userTypeAdmin);
            userRepository.save(adminUser);

        };
    }

}
