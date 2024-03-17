package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.model.AppUserDTO;
import dev.codebusters.code_busters.model.auth.AuthenticationRequest;
import dev.codebusters.code_busters.model.auth.AuthenticationResponse;
import dev.codebusters.code_busters.model.auth.JwtUserDetails;
import dev.codebusters.code_busters.model.auth.UserRegistrationRequest;
import dev.codebusters.code_busters.service.AppUserService;
import dev.codebusters.code_busters.service.JwtTokenService;
import dev.codebusters.code_busters.service.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final AppUserService appUserService;

    public AuthenticationResource(JwtUserDetailsService jwtUserDetailsService, AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, AppUserService appUserService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.appUserService = appUserService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAppUser(@RequestBody @Valid final UserRegistrationRequest userRegistrationRequest) {
        final Long createdId = appUserService.create(userRegistrationRequest);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getLogin(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getLogin());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setId(userDetails.getId());
        authenticationResponse.setEmail(userDetails.getEmail());
        authenticationResponse.setName(userDetails.getName());
        authenticationResponse.setPremium(userDetails.getPremium());
        authenticationResponse.setPoints(userDetails.getPoints());
        authenticationResponse.setProfileImage(userDetails.getProfileImage());
        authenticationResponse.setChallengesSolved(userDetails.getChallengesSolved());
        authenticationResponse.setUserType(userDetails.getUserType());
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails));
        return authenticationResponse;
    }
}
