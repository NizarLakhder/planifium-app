package com.diro.ift2255.controller;

import com.diro.ift2255.service.ResultatService;
import io.javalin.http.Context;

import java.util.Map;

/**
 * Contrôleur REST responsable de l’exposition des résultats académiques agrégés.
 *
 * <p>
 * Ce contrôleur fournit un point d’accès aux statistiques académiques
 * (moyenne, score, nombre de participants, trimestres analysés)
 * associées à un cours donné.
 * </p>
 *
 * <p>
 * Les résultats sont agrégés et stockés localement (mock / simulation),
 * et ne proviennent pas directement de l’API Planifium.
 * </p>
 *
 * <p>
 * Toute la logique métier est déléguée à {@link ResultatService}.
 * Le contrôleur se limite à la gestion HTTP et à la sérialisation JSON.
 * </p>
 */
public class ResultatController {

    /** Service métier des résultats académiques */
    private final ResultatService service;

    /**
     * Construit un contrôleur de résultats académiques.
     *
     * @param service service responsable de l’accès aux résultats
     */
    public ResultatController(ResultatService service) {
        this.service = service;
    }

    /**
     * Récupère les résultats académiques agrégés pour un cours donné.
     *
     * <p>
     * Endpoint : {@code GET /results/{sigle}}
     * </p>
     *
     * <p>
     * Exemple :
     * <ul>
     *   <li>{@code /results/IFT2255}</li>
     * </ul>
     * </p>
     *
     * <p>
     * Si aucun résultat n’est disponible pour le cours demandé,
     * une réponse HTTP 404 est retournée avec un message explicite.
     * </p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void getResultatByCourse(Context ctx) {

        String sigle = ctx.pathParam("sigle");

        service.getBySigle(sigle)
            .ifPresentOrElse(
                ctx::json,
                () -> ctx.status(404).json(
                        Map.of(
                            "error",
                            "Aucun résultat académique pour ce cours"
                        )
                )
            );
    }
}