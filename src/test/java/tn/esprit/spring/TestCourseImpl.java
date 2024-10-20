package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestCourseImpl {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course course;

    @BeforeEach
    void setUp() {
        // Créer une instance de Course pour les tests
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.INDIVIDUAL); // Remplacez par une valeur valide
        course.setSupport(Support.SKI); // Remplacez par une valeur valide
        course.setPrice(100.0f);
        course.setTimeSlot(3);
    }

    @Test
    public void testRetrieveAllCourses() {
        // Given
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));

        // When
        List<Course> result = courseServices.retrieveAllCourses();

        // Then
        verify(courseRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
    }

    @Test
    public void testAddCourse() {
        // Given
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        Course result = courseServices.addCourse(course);

        // Then
        verify(courseRepository, times(1)).save(course);
        assertEquals(course, result);
    }

    @Test
    public void testUpdateCourse() {
        // Given
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        Course result = courseServices.updateCourse(course);

        // Then
        verify(courseRepository, times(1)).save(course);
        assertEquals(course, result);
    }

    @Test
    public void testRetrieveCourse() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // When
        Course result = courseServices.retrieveCourse(1L);

        // Then
        verify(courseRepository, times(1)).findById(1L);
        assertEquals(course, result);
    }

    @Test
    public void testRetrieveCourseNotFound() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Course result = courseServices.retrieveCourse(1L);

        // Then
        verify(courseRepository, times(1)).findById(1L);
        assertNull(result);
    }
}

