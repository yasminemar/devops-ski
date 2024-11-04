package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImpl {

    @Mock
    private IRegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Registration registration;
    private Course course;
    private Skier skier;

    @BeforeEach
    void setUp() {
        course = new Course();
        skier = new Skier();

        registration = new Registration();
        registration.setCourse(course);
        registration.setSkier(skier);
        registration.setNumRegistration(1L);
        registration.setNumWeek(1);
    }

    @Test
    public void testAddRegistrationAndAssignToSkier(){
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, 1L);
        verify(registrationRepository, times(1)).save(registration);
        assertEquals(registration, result);
    }

    @Test
    public void testAssignRegistrationToCourse() {
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        verify(registrationRepository, times(1)).save(registration);
        assertEquals(registration, result);
    }

    @Test
    public void testAddRegistrationAndAssignToSkierAndCourse() {
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        verify(registrationRepository, times(1)).save(registration);
        assertEquals(registration, result);
    }

    @Test
    public void testNumWeeksCourseOfInstructorBySupport() {
        List<Integer> expectedWeeks = Arrays.asList(1, 2, 3);
        when(registrationServices.numWeeksCourseOfInstructorBySupport(1L, any())).thenReturn(expectedWeeks);

        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(1L, any());

        verify(registrationRepository, never()).save(any());
        assertEquals(expectedWeeks, result);
    }

    // Additional tests for other scenarios and methods can be added here
}
