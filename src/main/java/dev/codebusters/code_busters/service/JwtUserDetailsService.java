package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.model.auth.JwtUserDetails;
import dev.codebusters.code_busters.repos.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final AppUserRepository userRepository;

    public JwtUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public JwtUserDetails loadUserByUsername(final String username) {
        final AppUser user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found"));
        final String userType = user.getUserType().getTitle();
        final List<SimpleGrantedAuthority> roles = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + userType));
        final List<Long> challengesSolved = user.getUserSubmissions().stream()
                .map(submission -> submission.getChallenge().getId()).toList();
        return new JwtUserDetails(user.getId(), user.getEmail(), user.getName(), user.getPremium(),
                user.getPoints(), user.getProfileImage(), challengesSolved, userType,
                username, user.getPassword(), roles);
    }
}
