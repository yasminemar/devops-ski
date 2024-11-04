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
class SubscriptionServiceTest {

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
     void testUpdateSubscription() {
        Subscription subscription = new Subscription();
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription updatedSubscription = subscriptionService.updateSubscription(subscription);

        assertEquals(subscription, updatedSubscription);
        verify(subscriptionRepository).save(subscription);
    }
    @Test
     void testRetrieveSubscriptionById() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionService.retrieveSubscriptionById(1L);

        assertEquals(subscription, result);
        verify(subscriptionRepository).findById(1L);
    }

    @Test
    void testShowMonthlyRecurringRevenue() {
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(1000f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(6000f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(12000f);

        subscriptionService.showMonthlyRecurringRevenue();

        verify(subscriptionRepository).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
    }



    @Test
     void testRetrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        List<Subscription> subscriptions = Arrays.asList(new Subscription());
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(subscriptions);

        List<Subscription> result = subscriptionService.retrieveSubscriptionsByDates(startDate, endDate);

        assertEquals(subscriptions, result);
        verify(subscriptionRepository).getSubscriptionsByStartDateBetween(startDate, endDate);
    }

    @Test
    void testAddSubscriptionAnnual() {
        // Créez un abonnement annuel
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());

        // Simulez le comportement du repository
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Appelez la méthode addSubscription
        Subscription savedSubscription = subscriptionService.addSubscription(subscription);

        // Vérifiez que l'EndDate est correct
        assertEquals(LocalDate.now().plusYears(1), savedSubscription.getEndDate(), "L'abonnement annuel n'est pas configuré correctement");
        verify(subscriptionRepository).save(subscription);
    }


    @Test
    void testAddSubscriptionSemestriel() {
        // Créez un abonnement semestriel
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.SEMESTRIEL);
        subscription.setStartDate(LocalDate.now());

        // Simulez le comportement du repository
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Appelez la méthode addSubscription
        Subscription savedSubscription = subscriptionService.addSubscription(subscription);

        // Vérifiez que l'EndDate est correct
        assertEquals(LocalDate.now().plusMonths(6), savedSubscription.getEndDate(), "L'abonnement semestriel n'est pas configuré correctement");
        verify(subscriptionRepository).save(subscription);
    }

    @Test
    void testAddSubscriptionMonthly() {
        // Créez un abonnement mensuel
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        subscription.setStartDate(LocalDate.now());

        // Simulez le comportement du repository
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Appelez la méthode addSubscription
        Subscription savedSubscription = subscriptionService.addSubscription(subscription);

        // Vérifiez que l'EndDate est correct
        assertEquals(LocalDate.now().plusMonths(1), savedSubscription.getEndDate(), "L'abonnement mensuel n'est pas configuré correctement");
        verify(subscriptionRepository).save(subscription);
    }
}
