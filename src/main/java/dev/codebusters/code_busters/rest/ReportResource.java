package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.model.ChallengeWithSubmissionCountDTO;
import dev.codebusters.code_busters.model.SubscriptionWithCountDTO;
import dev.codebusters.code_busters.service.ChallengeService;
import dev.codebusters.code_busters.service.SubscriptionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportResource {

    private final ChallengeService challengeService;
    private final SubscriptionService subscriptionService;

    public ReportResource(final ChallengeService challengeService, SubscriptionService subscriptionService) {
        this.challengeService = challengeService;
        this.subscriptionService = subscriptionService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/challenge-popularity")
    public ResponseEntity<List<ChallengeWithSubmissionCountDTO>> getMostPopularExposedChallengesBetweenDates(
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(name = "dateTo", required = false) LocalDate dateTo)
    {
        return ResponseEntity.ok(challengeService.findMostPopularExposedChallengesBetweenDates(limit, dateFrom, dateTo));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/subscription-popularity")
    public ResponseEntity<List<SubscriptionWithCountDTO>> getSubscriptionCountBetweenDates(
            @RequestParam(name = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(name = "dateTo", required = false) LocalDate dateTo)
    {
        return ResponseEntity.ok(subscriptionService.findSubscriptionCountBetweenDates(dateFrom, dateTo));
    }


}
