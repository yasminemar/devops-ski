package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

@ExtendWith(MockitoExtension.class)
class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        // Set up an Instructor instance for the tests
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setName("Hedi Thameur");

        // Set up a Course instance for the tests
        course = new Course();
        course.setNumCourse(1L);
        course.setName("Ski Basics");
    }

    @Test
    void testAddInstructor() {
        // Given
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        Instructor result = instructorServices.addInstructor(instructor);

        // Then
        verify(instructorRepository, times(1)).save(instructor);
        assertEquals(instructor, result);
    }

    @Test
    void testRetrieveAllInstructors() {
        // Given
        when(instructorRepository.findAll()).thenReturn(Collections.singletonList(instructor));

        // When
        List<Instructor> result = instructorServices.retrieveAllInstructors();

        // Then
        verify(instructorRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(instructor, result.get(0));
    }

    @Test
    void testUpdateInstructor() {
        // Given
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        Instructor result = instructorServices.updateInstructor(instructor);

        // Then
        verify(instructorRepository, times(1)).save(instructor);
        assertEquals(instructor, result);
    }

    @Test
    void testRetrieveInstructor() {
        // Given
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        // When
        Instructor result = instructorServices.retrieveInstructor(1L);

        // Then
        verify(instructorRepository, times(1)).findById(1L);
        assertEquals(instructor, result);
    }

    @Test
    void testRetrieveInstructorNotFound() {
        // Given
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Instructor result = instructorServices.retrieveInstructor(1L);

        // Then
        verify(instructorRepository, times(1)).findById(1L);
        assertNull(result);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        // Then
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
        Set<Course> expectedCourses = new HashSet<>();
        expectedCourses.add(course);
        assertEquals(expectedCourses, result.getCourses());
    }
}
