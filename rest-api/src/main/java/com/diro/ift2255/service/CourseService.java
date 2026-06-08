


package com.diro.ift2255.service;
import com.diro.ift2255.model.EligibilityRequest;
import com.diro.ift2255.model.EligibilityResult;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.util.HttpClientApi;
import com.diro.ift2255.util.HttpClientApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.*;

public class CourseService {

    private static final String BASE_URL =
            "https://planifium-api.onrender.com/api/v1/courses";

    private final HttpClientApi httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public CourseService(HttpClientApi httpClient) {
        this.httpClient = httpClient;
    }

    public List<Course> fetchCourses(
            String ids,
            String name,
            String description,
            boolean includeSchedule,
            String semester,
            String level
    ) {

        try {
            StringBuilder url = new StringBuilder(BASE_URL + "?");

            if (ids != null && !ids.isBlank()) {
                url.append("courses_sigle=").append(ids.toLowerCase()).append("&");
            }
            if (name != null) {
                url.append("name=").append(name).append("&");
            }
            if (description != null) {
                url.append("description=").append(description).append("&");
            }
            if (includeSchedule) {
                url.append("include_schedule=true&");
                if (semester != null) {
                    url.append("schedule_semester=").append(semester).append("&");
                }
            }
            if (level != null) {
                url.append("response_level=").append(level).append("&");
            }

            HttpClientApiResponse response =
                    httpClient.get(URI.create(url.toString()));

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                return mapper.readValue(
                        response.getBody(),
                        new TypeReference<List<Course>>() {}
                );
            }
        } catch (Exception e) {
            System.err.println("Erreur API Planifium: " + e.getMessage());
        }

        return Collections.emptyList();
    }

    
    
    public EligibilityResult checkEligibility(
        Course course,
        EligibilityRequest request
) {
    if (course == null) {
        return new EligibilityResult(
                false,
                List.of(),
                "Cours introuvable"
        );
    }

    // 1️ Vérification des prérequis
    List<String> prerequisites =
            course.getPrerequisite_courses() == null
                    ? List.of()
                    : course.getPrerequisite_courses();

    List<String> completed =
            request.getCompletedCourses() == null
                    ? List.of()
                    : request.getCompletedCourses();

    List<String> missing = prerequisites.stream()
            .filter(p -> completed.stream()
                    .noneMatch(c -> c.equalsIgnoreCase(p)))
            .toList();

    if (!missing.isEmpty()) {
        return new EligibilityResult(
                false,
                missing,
                "Prérequis non satisfaits"
        );
    }

    // 2️ Vérification du cycle (simplifiée mais valide)
    String cycle = request.getCycle();

    if (cycle == null || cycle.isBlank()) {
        return new EligibilityResult(
                false,
                List.of(),
                "Cycle non spécifié"
        );
    }

    if (!cycle.equalsIgnoreCase("bac")) {
        return new EligibilityResult(
                false,
                List.of(),
                "Cours réservé au baccalauréat"
        );
    }

    //  Tout est bon
    return new EligibilityResult(
            true,
            List.of(),
            "Étudiant éligible à ce cours"
    );
}
    
    public Course fetchCourseById(
            String id,
            boolean includeSchedule,
            String semester,
            String level
    ) {

        try {
            StringBuilder url = new StringBuilder(BASE_URL + "/" + id.toLowerCase() + "?");

            if (includeSchedule) {
                url.append("include_schedule=true&");
                if (semester != null) {
                    url.append("schedule_semester=").append(semester).append("&");
                }
            }
            if (level != null) {
                url.append("response_level=").append(level);
            }

            HttpClientApiResponse response =
                    httpClient.get(URI.create(url.toString()));

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                return mapper.readValue(response.getBody(), Course.class);
            }
        } catch (Exception e) {
            System.err.println("Erreur API Planifium: " + e.getMessage());
        }

        return null;
    }

    public List<Course> getCoursParCodes(String[] sigles) {
        List<Course> list = new ArrayList<>();
        for (String sigle : sigles) {
            Course c = fetchCourseById(sigle.trim(), false, null, "reg");
            if (c != null) list.add(c);
        }
        return list;
    }
}