package com.diro.ift2255.config;

import io.javalin.Javalin;
import com.diro.ift2255.controller.*;
import com.diro.ift2255.service.*;
import com.diro.ift2255.util.HttpClientApi;

/**
 * Classe responsable de l'enregistrement de toutes les routes HTTP
 * de l'application Javalin.
 *
 * <p>
 * Cette classe centralise la configuration des endpoints REST
 * et relie chaque route à son contrôleur correspondant.
 * </p>
 *
 * <p>
 * Les routes exposées incluent :
 * <ul>
 *   <li>Catalogue de cours (wrappers Planifium)</li>
 *   <li>Programmes</li>
 *   <li>Avis étudiants</li>
 *   <li>Résultats académiques</li>
 *   <li>Utilisateurs</li>
 * </ul>
 * </p>
 */
public class Routes {

    /**
     * Enregistre l'ensemble des routes REST de l'application.
     *
     * @param app instance de l'application Javalin
     */
    public static void register(Javalin app) {

        /* =========================
           SERVICES & CONTRÔLEURS
           ========================= */

        CourseService courseService =
            new CourseService(new HttpClientApi());
        CourseController courseController =
            new CourseController(courseService);

        ProgramService programService =
            new ProgramService(new HttpClientApi());
        ProgramController programController =
            new ProgramController(programService);

        UserService userService = new UserService();
        UserController userController =
            new UserController(userService);

        AvisService avisService = new AvisService();
        AvisController avisController =
            new AvisController(avisService);

        ResultatService resultatService =
            new ResultatService();
        ResultatController resultatController =
            new ResultatController(resultatService);

        /* =========================
           ROUTES PROGRAMMES
           ========================= */

        /**
         * Récupère les informations d'un programme
         * ainsi que la liste de ses cours.
         *
         * GET /programs/{programId}
         */
        app.get("/programs/{programId}",
            programController::getProgram);

        /* =========================
           ROUTES COURS
           ========================= */

        /**
         * Recherche de cours par nom ou description.
         *
         * GET /courses/search
         */
        app.get("/courses/search",
            courseController::searchCourses);

        /**
         * Comparaison de plusieurs cours.
         *
         * GET /courses/compare
         */
        app.get("/courses/compare",
            courseController::compareCourses);

        /**
         * Récupère une liste de cours
         * (wrapper de l'API Planifium).
         *
         * GET /courses
         */
        app.get("/courses",
            courseController::getCourses);

        /**
         * Récupère les informations détaillées
         * d'un cours donné.
         *
         * GET /courses/{id}
         */
        app.get("/courses/{id}",
            courseController::getCourseById);

        /**
         * Vérifie l'éligibilité d'un étudiant
         * à un cours donné.
         *
         * POST /courses/{id}/eligibility
         */
        app.post("/courses/{id}/eligibility",
            courseController::checkEligibility);

        /* =========================
           ROUTES UTILISATEURS
           ========================= */

        /**
         * Récupère la liste de tous les utilisateurs.
         *
         * GET /users
         */
        app.get("/users",
            userController::getAllUsers);

        /**
         * Récupère un utilisateur par identifiant.
         *
         * GET /users/{id}
         */
        app.get("/users/{id}",
            userController::getUserById);

        /* =========================
           ROUTES AVIS ÉTUDIANTS
           ========================= */

        /**
         * Ajoute un avis étudiant.
         *
         * POST /api/avis
         */
        app.post("/api/avis",
            avisController::addAvis);

        /**
         * Récupère tous les avis étudiants.
         *
         * GET /api/avis
         */
        app.get("/api/avis",
            avisController::getAllAvis);

        /**
         * Récupère les avis associés à un cours.
         *
         * GET /api/avis/{course}
         */
        app.get("/api/avis/{course}",
            avisController::getAvisByCourse);

        app.put("/api/avis/{id}",
            avisController::updateAvis);

        app.delete("/api/avis/{id}",
            avisController::deleteAvis);

        /* =========================
           ROUTES RÉSULTATS
           ========================= */

        /**
         * Récupère les résultats académiques
         * agrégés pour un cours.
         *
         * GET /results/{sigle}
         */
        app.get("/results/{sigle}",
            resultatController::getResultatByCourse);
    }
}