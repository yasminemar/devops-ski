package tn.esprit.spring.test;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class SubscriptionServiceTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSubscriptionAnnual() {
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        Subscription result = subscriptionService.addSubscription(subscription);

        assertEquals(subscription.getEndDate(), LocalDate.now().plusYears(1), "L'abonnement annuel n'est pas configuré correctement");
        verify(subscriptionRepository).save(subscription);
    }
    @Test
    public void testUpdateSubscription() {
        Subscription subscription = new Subscription();
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription updatedSubscription = subscriptionService.updateSubscription(subscription);

        assertEquals(subscription, updatedSubscription);
        verify(subscriptionRepository).save(subscription);
    }
    @Test
    public void testRetrieveSubscriptionById() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionService.retrieveSubscriptionById(1L);

        assertEquals(subscription, result);
        verify(subscriptionRepository).findById(1L);
    }



    @Test
    public void testRetrieveSubscriptions() {
        Subscription subscription = new Subscription();
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        when(subscriptionRepository.findDistinctOrderByEndDateAsc()).thenReturn(List.of(subscription));

        subscriptionService.retrieveSubscriptions();

        verify(subscriptionRepository).findDistinctOrderByEndDateAsc();
        verify(skierRepository).findBySubscription(subscription);
    }

    @Test
    public void testShowMonthlyRecurringRevenue() {
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(1000f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(6000f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(12000f);

        subscriptionService.showMonthlyRecurringRevenue();

        verify(subscriptionRepository).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
    }



    @Test
    public void testRetrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        List<Subscription> subscriptions = Arrays.asList(new Subscription());
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(subscriptions);

        List<Subscription> result = subscriptionService.retrieveSubscriptionsByDates(startDate, endDate);

        assertEquals(subscriptions, result);
        verify(subscriptionRepository).getSubscriptionsByStartDateBetween(startDate, endDate);
    }







}
