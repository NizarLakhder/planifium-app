package com.diro.ift2255.controller;

import com.diro.ift2255.service.ProgramService;
import io.javalin.http.Context;

import java.util.Map;

/**
 * Contrôleur REST responsable de la gestion des programmes d’études.
 *
 * <p>
 * Ce contrôleur agit comme un <strong>wrapper</strong> autour de l’API externe
 * Planifium afin de :
 * <ul>
 *   <li>simplifier l’accès aux programmes côté frontend</li>
 *   <li>offrir un contrôle fin sur le niveau de détail retourné</li>
 *   <li>uniformiser les réponses HTTP</li>
 * </ul>
 * </p>
 *
 * <p>
 * La logique métier et les appels HTTP externes sont entièrement délégués
 * à {@link ProgramService}.
 * </p>
 */
public class ProgramController {

    /** Service métier responsable des programmes */
    private final ProgramService programService;

    /**
     * Construit un contrôleur de programmes.
     *
     * @param programService service métier des programmes
     */
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    /**
     * Récupère les informations d’un programme d’études.
     *
     * <p>
     * Endpoint : {@code GET /programs/{programId}}
     * </p>
     *
     * <p>
     * Exemples d’utilisation :
     * <ul>
     *   <li>{@code /programs/117510}</li>
     *   <li>{@code /programs/117510?include_courses_detail=true}</li>
     *   <li>{@code /programs/117510?include_courses_detail=true&response_level=full}</li>
     * </ul>
     * </p>
     *
     * <p>
     * Paramètres supportés :
     * <ul>
     *   <li>{@code include_courses_detail} : inclure les détails complets des cours</li>
     *   <li>{@code response_level} : niveau de détail de la réponse (reg / full)</li>
     * </ul>
     * </p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void getProgram(Context ctx) {

        String programId = ctx.pathParam("programId");

        boolean includeDetails =
                "true".equalsIgnoreCase(
                        ctx.queryParam("include_courses_detail")
                );

        String responseLevel = ctx.queryParam("response_level");
        if (responseLevel == null) {
            responseLevel = "reg";
        }

        Object result = programService.fetchProgram(
                programId,
                includeDetails,
                responseLevel
        );

        if (result == null) {
            ctx.status(404).json(
                    Map.of(
                        "message",
                        "Programme " + programId + " non trouvé"
                    )
            );
        } else {
            ctx.json(result);
        }
    }
}