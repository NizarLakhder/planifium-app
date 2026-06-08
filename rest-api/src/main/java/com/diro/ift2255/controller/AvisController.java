package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.model.Avis;
import com.diro.ift2255.service.AvisService;
import com.diro.ift2255.util.ResponseUtil;

/**
 * Contrôleur REST pour la gestion des avis étudiants.
 * Expose les opérations CRUD sur les avis via HTTP.
 */
public class AvisController {

    private final AvisService avisService;

    public AvisController(AvisService avisService) {
        this.avisService = avisService;
    }

    /**
     * POST /api/avis
     * Crée un nouvel avis à partir du corps JSON de la requête.
     * Retourne 201 avec l'avis créé (incluant son id généré).
     */
    public void addAvis(Context ctx) {
        try {
            Avis avis = ctx.bodyAsClass(Avis.class);
            avisService.ajouterAvis(avis);
            ctx.status(201).json(avis);
        } catch (Exception e) {
            ctx.status(500).json(ResponseUtil.formatError("Erreur lors de l'ajout de l'avis : " + e.getMessage()));
        }
    }

    /**
     * GET /api/avis/{course}
     * Retourne tous les avis associés au sigle de cours donné (insensible à la casse).
     */
    public void getAvisByCourse(Context ctx) {
        String courseId = ctx.pathParam("course");
        ctx.json(avisService.getByCourse(courseId));
    }

    /**
     * GET /api/avis
     * Retourne la liste complète de tous les avis étudiants stockés.
     */
    public void getAllAvis(Context ctx) {
        ctx.json(avisService.lireAvis());
    }

    /**
     * PUT /api/avis/{id}
     * Met à jour un avis existant identifié par son id.
     * Retourne 404 si l'id est introuvable, 200 avec l'avis modifié si succès.
     */
    public void updateAvis(Context ctx) {
        String id = ctx.pathParam("id");
        try {
            Avis updated = ctx.bodyAsClass(Avis.class);
            avisService.update(id, updated);
            ctx.status(200).json(updated);
        } catch (IllegalArgumentException e) {
            ctx.status(404).json(ResponseUtil.formatError(e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(ResponseUtil.formatError("Erreur lors de la modification : " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/avis/{id}
     * Supprime définitivement un avis identifié par son id.
     * Retourne 204 si succès, 404 si l'id est introuvable.
     */
    public void deleteAvis(Context ctx) {
        String id = ctx.pathParam("id");
        try {
            avisService.delete(id);
            ctx.status(204);
        } catch (IllegalArgumentException e) {
            ctx.status(404).json(ResponseUtil.formatError(e.getMessage()));
        } catch (Exception e) {
            ctx.status(500).json(ResponseUtil.formatError("Erreur lors de la suppression : " + e.getMessage()));
        }
    }
}
