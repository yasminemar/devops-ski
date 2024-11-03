package tn.esprit.spring;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.*;

public class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }




    // Test addSkier
    @Test
    void addSkierWithAnnualSubscription() {
        // Given
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        skier.setSubscription(subscription);

        when(skierRepository.save(skier)).thenReturn(skier);


        // When
        Skier result = skierServices.addSkier(skier);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now().plusYears(1), result.getSubscription().getEndDate());
        verify(skierRepository, times(1)).save(skier);
    }




    // Test removeSkier
    @Test
    void removeSkier() {
        // Given
        Long skierId = 1L;

        // When
        skierServices.removeSkier(skierId);

        // Then
        verify(skierRepository, times(1)).deleteById(skierId);
    }

    // Test retrieveSkier
    @Test
    void retrieveSkier() {
        // Given
        Skier skier = new Skier();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        // When
        Skier result = skierServices.retrieveSkier(1L);

        // Then
        assertNotNull(result);
        assertEquals(skier, result);
        verify(skierRepository, times(1)).findById(1L);
    }



    // Test retrieveSkiersBySubscriptionType
    @Test
    void retrieveSkiersBySubscriptionType() {
        // Given
        List<Skier> skiers = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(skiers);

        // When
        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
    }
    @Test
    void retrieveAllSkiers() {
        // Given
        Skier skier1 = new Skier();
        Skier skier2 = new Skier();
        List<Skier> skiers = Arrays.asList(skier1, skier2);
        when(skierRepository.findAll()).thenReturn(skiers);

        // When
        List<Skier> result = skierServices.retrieveAllSkiers();

        // Then
        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findAll();
    }
}
