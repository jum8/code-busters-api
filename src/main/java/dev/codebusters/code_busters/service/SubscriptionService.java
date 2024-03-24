package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.Subscription;
import dev.codebusters.code_busters.domain.UserSubscription;
import dev.codebusters.code_busters.model.SubscriptionDTO;
import dev.codebusters.code_busters.model.SubscriptionWithCountDTO;
import dev.codebusters.code_busters.repos.SubscriptionRepository;
import dev.codebusters.code_busters.repos.UserSubscriptionRepository;
import dev.codebusters.code_busters.util.NotFoundException;
import dev.codebusters.code_busters.util.ReferencedWarning;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository,
                               final UserSubscriptionRepository userSubscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    public List<SubscriptionDTO> findAll() {
        final List<Subscription> subscriptions = subscriptionRepository.findAll(Sort.by("id"));
        return subscriptions.stream()
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .toList();
    }

    public List<SubscriptionWithCountDTO> findSubscriptionCountBetweenDates(LocalDate from, LocalDate to) {
        OffsetDateTime dateFrom = from == null ? null : OffsetDateTime.of(from, LocalTime.MAX, ZoneOffset.UTC);
        OffsetDateTime dateTo = to == null ? null : OffsetDateTime.of(to, LocalTime.MAX, ZoneOffset.UTC);
        return subscriptionRepository.findSubscriptionCountBetweenDates(dateFrom, dateTo).stream()
                .map(object -> mapToDTO((Subscription) object[0], new SubscriptionWithCountDTO(), (Long) object[1]))
                .toList();
    }

    public SubscriptionDTO get(final Long id) {
        return subscriptionRepository.findById(id)
                .map(subscription -> mapToDTO(subscription, new SubscriptionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = new Subscription();
        mapToEntity(subscriptionDTO, subscription);
        return subscriptionRepository.save(subscription).getId();
    }

    public void update(final Long id, final SubscriptionDTO subscriptionDTO) {
        final Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(subscriptionDTO, subscription);
        subscriptionRepository.save(subscription);
    }

    public void delete(final Long id) {
        subscriptionRepository.deleteById(id);
    }

    private SubscriptionDTO mapToDTO(final Subscription subscription,
                                     final SubscriptionDTO subscriptionDTO) {
        subscriptionDTO.setId(subscription.getId());
        subscriptionDTO.setName(subscription.getName());
        subscriptionDTO.setDurationInMonths(subscription.getDurationInMonths());
        subscriptionDTO.setPrice(subscription.getPrice());
        return subscriptionDTO;
    }
    private SubscriptionWithCountDTO mapToDTO(final Subscription subscription,
                                              final SubscriptionWithCountDTO subscriptionWithCountDTO,
                                              Long userSubscriptionCount) {
        subscriptionWithCountDTO.setId(subscription.getId());
        subscriptionWithCountDTO.setName(subscription.getName());
        subscriptionWithCountDTO.setDurationInMonths(subscription.getDurationInMonths());
        subscriptionWithCountDTO.setPrice(subscription.getPrice());
        subscriptionWithCountDTO.setUserSubscriptionCount(userSubscriptionCount);
        return subscriptionWithCountDTO;
    }

    private Subscription mapToEntity(final SubscriptionDTO subscriptionDTO,
                                     final Subscription subscription) {
        subscription.setName(subscriptionDTO.getName());
        subscription.setDurationInMonths(subscriptionDTO.getDurationInMonths());
        subscription.setPrice(subscriptionDTO.getPrice());
        return subscription;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final UserSubscription subscriptionUserSubscription = userSubscriptionRepository.findFirstBySubscription(subscription);
        if (subscriptionUserSubscription != null) {
            referencedWarning.setKey("subscription.userSubscription.subscription.referenced");
            referencedWarning.addParam(subscriptionUserSubscription.getId());
            return referencedWarning;
        }
        return null;
    }

}
