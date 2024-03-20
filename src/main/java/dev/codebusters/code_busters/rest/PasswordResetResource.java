package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.service.AppUserService;
import dev.codebusters.code_busters.service.PasswordResetService;
import dev.codebusters.code_busters.util.Password;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/password")
public class PasswordResetResource {

    private final PasswordResetService passwordResetService;

    public PasswordResetResource(AppUserService appUserService, PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }


    @PostMapping("/forgot/{email}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<String> forgotPassword(HttpServletRequest request, @PathVariable final String email) {
        String contextPath = getAppUrl(request);

        String baseUrl = contextPath + "/users/password/reset?token=";

        passwordResetService.forgotPassword(baseUrl, email);

        return new ResponseEntity<>("An email was sent", HttpStatus.OK);
    }

    @PostMapping("/reset")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Long> changePassword(@RequestBody @Password final String password, @RequestParam String token) {
        Long id = passwordResetService.updatePassword(password, token);
        return ResponseEntity.ok(id);
    }

    private String getAppUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }


}
