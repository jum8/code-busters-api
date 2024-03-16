package dev.codebusters.code_busters;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.UserType;
import dev.codebusters.code_busters.repos.AppUserRepository;
import dev.codebusters.code_busters.repos.UserTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableScheduling
public class CodeBustersApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CodeBustersApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AppUserRepository userRepository, UserTypeRepository userTypeRepository, PasswordEncoder passwordEncoder) {
        return args -> {  // inserting data after application is up

            UserType userTypeUser = userTypeRepository.findById(1L).orElseGet(() -> {
                UserType newUserType = new UserType();
                newUserType.setTitle("USER");
                newUserType = userTypeRepository.save(newUserType);
                return newUserType;
            });


            String commonUserEmail = "ned@mail.com";
            if (!userRepository.existsByEmail(commonUserEmail)) {
                AppUser commonUser = new AppUser();
                commonUser.setName("Ned Flanders");
                commonUser.setEmail(commonUserEmail);
                commonUser.setPassword(passwordEncoder.encode("password"));
                commonUser.setUserType(userTypeUser);
                userRepository.save(commonUser);
            }

            UserType userTypeAdmin = userTypeRepository.findById(2L).orElseGet(() -> {
                UserType newUserType = new UserType();
                newUserType.setTitle("ADMIN");
                newUserType = userTypeRepository.save(newUserType);
                return newUserType;
            });

            String adminUserEmail = "ba@mail.com";
            if (!userRepository.existsByEmail(adminUserEmail)) {
                AppUser adminUser = new AppUser();
                adminUser.setName("Bruce Allmighty");
                adminUser.setEmail(adminUserEmail);
                adminUser.setPassword(passwordEncoder.encode("password"));
                adminUser.setUserType(userTypeAdmin);
                userRepository.save(adminUser);
            }

            UserType userTypePremium = userTypeRepository.findById(3L).orElseGet(() -> {
                UserType newUserType = new UserType();
                newUserType.setTitle("PREMIUM");
                newUserType = userTypeRepository.save(newUserType);
                return newUserType;
            });

            String premiumUserEmail = "max@mail.com";
            if (!userRepository.existsByEmail(premiumUserEmail)) {
                AppUser premiumUser = new AppUser();
                premiumUser.setName("Max Power");
                premiumUser.setEmail(premiumUserEmail);
                premiumUser.setPassword(passwordEncoder.encode("password"));
                premiumUser.setUserType(userTypePremium);
                premiumUser.setPremium(true); // todo reemplazar toda la logica premium por userType y borrar campo
                userRepository.save(premiumUser);
            }


        };
    }

}
