package com.diro.ift2255.service;

import com.diro.ift2255.util.HttpClientApi;
import com.diro.ift2255.util.HttpClientApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;

/**
 * Service responsable de l’accès aux données des programmes
 * via l’API externe Planifium.
 *
 * <p>
 * Ce service agit comme une façade entre l’API Planifium et
 * l’API REST interne de l’application.
 * Il permet de récupérer la structure complète d’un programme
 * ainsi que la liste des cours associés.
 * </p>
 *
 * <p>
 * L’objectif est de simplifier l’utilisation de Planifium côté frontend
 * et de contrôler le niveau de détail des données retournées.
 * </p>
 */
public class ProgramService {

    /**
     * URL de base de l’API Planifium pour les programmes.
     */
    private static final String BASE_URL =
            "https://planifium-api.onrender.com/api/v1/programs";

    /**
     * Client HTTP abstrait utilisé pour effectuer les requêtes externes.
     */
    private final HttpClientApi httpClient;

    /**
     * Mapper Jackson pour la désérialisation JSON.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Construit un service de programmes.
     *
     * @param httpClient client HTTP utilisé pour appeler l’API Planifium
     */
    public ProgramService(HttpClientApi httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Récupère les informations d’un programme académique à partir de son identifiant.
     *
     * <p>
     * Cette méthode encapsule l’appel à l’API Planifium :
     * </p>
     * <ul>
     *   <li>{@code programs_list} : identifiant du programme</li>
     *   <li>{@code include_courses_detail} : inclure les détails des cours</li>
     *   <li>{@code response_level} : niveau de détail (min, reg, full)</li>
     * </ul>
     *
     * <p>
     * Le format retourné dépend directement de la réponse Planifium
     * et est volontairement conservé générique ({@code Object})
     * afin de permettre une flexibilité côté frontend.
     * </p>
     *
     * @param programId identifiant du programme (ex: 117510)
     * @param includeCoursesDetail true pour inclure les détails des cours
     * @param responseLevel niveau de détail de la réponse (reg par défaut)
     * @return objet représentant le programme ou {@code null} si non trouvé ou erreur
     */
    public Object fetchProgram(
            String programId,
            boolean includeCoursesDetail,
            String responseLevel
    ) {
        try {
            StringBuilder url = new StringBuilder(BASE_URL + "?");
            url.append("programs_list=").append(programId);

            if (includeCoursesDetail) {
                url.append("&include_courses_detail=true");
            }

            if (responseLevel != null && !responseLevel.isBlank()) {
                url.append("&response_level=").append(responseLevel);
            }

            HttpClientApiResponse response =
                    httpClient.get(URI.create(url.toString()));

            if (response.getStatusCode() >= 200
                    && response.getStatusCode() < 300) {
                return mapper.readValue(
                        response.getBody(),
                        Object.class
                );
            }

        } catch (Exception e) {
            System.err.println(
                "Erreur API Planifium (programs): " + e.getMessage()
            );
        }

        return null;
    }
}