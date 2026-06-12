package com.diro.ift2255.service;

import com.diro.ift2255.model.*;
import com.diro.ift2255.util.HttpClientApi;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour CourseService (éligibilité)
 */
class CourseServiceTest {

    private final HttpClientApi httpClient = Mockito.mock(HttpClientApi.class);
    private final CourseService service = new CourseService(httpClient);

    /**
     * Test : échec si un prérequis est manquant
     */
    @Test
    void eligibilityFailsWhenPrerequisiteMissing() {
        Course course = new Course();
        course.setPrerequisite_courses(List.of("IFT1015"));

        EligibilityRequest req = new EligibilityRequest();
        req.setCompletedCourses(List.of("IFT1025"));
        req.setCycle("bac");

        EligibilityResult result = service.checkEligibility(course, req);

        assertFalse(result.isEligible());
        assertEquals(List.of("IFT1015"), result.getMissingPrerequisites());
    }

    /**
     * Test : échec si le cycle n'est pas baccalauréat
     */
    @Test
    void eligibilityFailsWhenCycleIsNotBac() {
        Course course = new Course();
        course.setPrerequisite_courses(List.of());

        EligibilityRequest req = new EligibilityRequest();
        req.setCompletedCourses(List.of());
        req.setCycle("maitrise");

        EligibilityResult result = service.checkEligibility(course, req);

        assertFalse(result.isEligible());
        assertEquals("Cours réservé au baccalauréat", result.getMessage());
    }

    /**
     * Test : succès lorsque toutes les conditions sont respectées
     */
    @Test
    void eligibilitySucceedsWhenAllConditionsMet() {
        Course course = new Course();
        course.setPrerequisite_courses(List.of("IFT1015"));

        EligibilityRequest req = new EligibilityRequest();
        req.setCompletedCourses(List.of("IFT1015"));
        req.setCycle("bac");

        EligibilityResult result = service.checkEligibility(course, req);

        assertTrue(result.isEligible());
        assertEquals("Étudiant éligible à ce cours", result.getMessage());
    }

    /**
     * Test : succès sans prérequis
     */
    @Test
    void eligibilitySucceedsWhenNoPrerequisites() {
        Course course = new Course();
        course.setPrerequisite_courses(List.of());

        EligibilityRequest req = new EligibilityRequest();
        req.setCompletedCourses(List.of());
        req.setCycle("bac");

        EligibilityResult result = service.checkEligibility(course, req);

        assertTrue(result.isEligible());
    }
/**
 * Test : échec si plusieurs prérequis et un seul manquant
 */
@Test
void eligibilityFailsWhenOneOfMultiplePrerequisitesMissing() {
    Course course = new Course();
    course.setPrerequisite_courses(List.of("IFT1015", "IFT1025"));

    EligibilityRequest req = new EligibilityRequest();
    req.setCompletedCourses(List.of("IFT1015"));
    req.setCycle("bac");

    EligibilityResult result = service.checkEligibility(course, req);

    assertFalse(result.isEligible());
    assertEquals(List.of("IFT1025"), result.getMissingPrerequisites());
}
/**
 * Test : refus si le cycle est null
 */
@Test
void eligibilityFailsWhenCycleIsNull() {
    Course course = new Course();
    course.setPrerequisite_courses(List.of());

    EligibilityRequest req = new EligibilityRequest();
    req.setCompletedCourses(List.of());
    req.setCycle(null);

    EligibilityResult result = service.checkEligibility(course, req);

    assertFalse(result.isEligible());
}
  }