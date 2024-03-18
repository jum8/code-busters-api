package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.model.UserSubscriptionDTO;
import dev.codebusters.code_busters.service.UserSubscriptionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/user-subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserSubscriptionResource {

    private final UserSubscriptionService userSubscriptionService;

    public UserSubscriptionResource(final UserSubscriptionService userSubscriptionService) {
        this.userSubscriptionService = userSubscriptionService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserSubscriptionDTO>> getAllUserSubscriptions() {
        return ResponseEntity.ok(userSubscriptionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSubscriptionDTO> getUserSubscription(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(userSubscriptionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUserSubscription(
            @RequestBody @Valid final UserSubscriptionDTO userSubscriptionDTO) {
        final Long createdId = userSubscriptionService.create(userSubscriptionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateUserSubscription(@PathVariable(name = "id") final Long id,
                                                       @RequestBody @Valid final UserSubscriptionDTO userSubscriptionDTO) {
        userSubscriptionService.update(id, userSubscriptionDTO);
        return ResponseEntity.ok(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable(name = "id") final Long id) {
        userSubscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
