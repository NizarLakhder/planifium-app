package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.model.Course;
import com.diro.ift2255.model.EligibilityRequest;
import com.diro.ift2255.model.EligibilityResult;
import com.diro.ift2255.service.CourseService;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST responsable de la gestion des cours.
 *
 * <p>
 * Ce contrôleur agit comme un <strong>wrapper</strong> autour de l’API externe
 * Planifium afin de :
 * <ul>
 *   <li>simplifier l’utilisation côté frontend</li>
 *   <li>contrôler le volume de données retournées</li>
 *   <li>exposer des cas d’utilisation métier spécifiques</li>
 * </ul>
 * </p>
 */
public class CourseController {

    /** Service de gestion des cours */
    private final CourseService courseService;

    /**
     * Construit un contrôleur de cours avec le service associé.
     *
     * @param courseService service métier des cours
     */
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Recherche de cours (wrapper).
     *
     * <p>
     * Ce endpoint redirige vers {@link #getCourses(Context)} afin de centraliser
     * la logique de récupération et de filtrage des cours.
     * </p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void searchCourses(Context ctx) {
        getCourses(ctx);
    }

    /**
     * Vérifie l’éligibilité d’un étudiant à un cours donné.
     *
     * <p>
     * Endpoint : {@code POST /courses/{id}/eligibility}
     * </p>
     *
     * <p>
     * Le corps de la requête doit contenir un {@link EligibilityRequest}
     * indiquant les cours complétés et le cycle d’études.
     * </p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void checkEligibility(Context ctx) {

        String courseId = ctx.pathParam("id");

        EligibilityRequest request =
                ctx.bodyAsClass(EligibilityRequest.class);

        Course course = courseService.fetchCourseById(
                courseId,
                false,
                null,
                "full"
        );

        EligibilityResult result =
                courseService.checkEligibility(course, request);

        ctx.json(result);
    }

    /**
     * Récupère une liste de cours.
     *
     * <p>
     * Endpoint : {@code GET /courses}
     * </p>
     *
     * <p>
     * Paramètres supportés (wrappers Planifium) :
     * <ul>
     *   <li>{@code ids} : liste de sigles (ex: IFT1015,IFT1025)</li>
     *   <li>{@code name} : recherche par nom</li>
     *   <li>{@code description} : recherche par description</li>
     *   <li>{@code include_schedule} : inclure les horaires</li>
     *   <li>{@code schedule_semester} : limiter à un trimestre</li>
     *   <li>{@code response_level} : niveau de détail (reg / full)</li>
     * </ul>
     * </p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void getCourses(Context ctx) {

        // ==========================
        // PARAMÈTRES FRONTEND
        // ==========================

        String ids = ctx.queryParam("ids");

        // Paramètres Planifium natifs
        String coursesSigle = ctx.queryParam("courses_sigle");
        String name = ctx.queryParam("name");
        String description = ctx.queryParam("description");

        // Priorité au paramètre frontend "ids"
        if (ids != null && !ids.isBlank()) {
            coursesSigle = ids;
        }

        boolean includeSchedule =
                "true".equalsIgnoreCase(ctx.queryParam("include_schedule"));

        String semester = ctx.queryParam("schedule_semester");

        String responseLevel = ctx.queryParam("response_level");
        if (responseLevel == null || responseLevel.isBlank()) {
            responseLevel = "reg";
        }

        // ==========================
        // APPEL SERVICE
        // ==========================
        List<Course> courses = courseService.fetchCourses(
                coursesSigle,
                name,
                description,
                includeSchedule,
                semester,
                responseLevel
        );

        // ==========================
        // RÉPONSE HTTP
        // ==========================
        if (courses.isEmpty()) {
            ctx.status(404).json(
                    Map.of("message", "Aucun cours trouvé")
            );
        } else {
            ctx.json(courses);
        }
    }

    /**
     * Récupère les informations détaillées d’un cours.
     *
     * <p>
     * Endpoint : {@code GET /courses/{id}}
     * </p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void getCourseById(Context ctx) {

        String id = ctx.pathParam("id");

        boolean includeSchedule =
                "true".equalsIgnoreCase(ctx.queryParam("include_schedule"));

        String semester = ctx.queryParam("schedule_semester");

        String responseLevel = ctx.queryParam("response_level");
        if (responseLevel == null) {
            responseLevel = "reg";
        }

        Course course = courseService.fetchCourseById(
                id,
                includeSchedule,
                semester,
                responseLevel
        );

        if (course == null) {
            ctx.status(404).json(
                    Map.of("message", "Cours " + id + " non trouvé")
            );
        } else {
            ctx.json(course);
        }
    }

    /**
     * Compare plusieurs cours à partir de leurs sigles.
     *
     * <p>
     * Endpoint : {@code GET /courses/compare?ids=IFT1015,IFT1025}
     * </p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void compareCourses(Context ctx) {

        String idsParam = ctx.queryParam("ids");

        if (idsParam == null || idsParam.isBlank()) {
            ctx.status(400).json(
                    Map.of(
                        "error",
                        "Paramètre ids manquant (ex: ?ids=IFT1015,IFT1025)"
                    )
            );
            return;
        }

        String[] sigles = idsParam.split(",");

        List<Course> courses = courseService.getCoursParCodes(sigles);

        if (courses.isEmpty()) {
            ctx.status(404).json(
                    Map.of("message", "Aucun cours trouvé pour la comparaison")
            );
        } else {
            ctx.json(courses);
        }
    }
}