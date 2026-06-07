package com.diro.ift2255.model;

import java.util.List;

/**
 * Représente le résultat de la vérification d’éligibilité
 * d’un étudiant à un cours donné.
 *
 * <p>
 * Cette classe est retournée par le endpoint :
 * </p>
 *
 * <pre>
 * POST /courses/{id}/eligibility
 * </pre>
 *
 * <p>
 * Elle indique si l’étudiant est autorisé à s’inscrire au cours,
 * ainsi que les éventuels prérequis manquants et un message explicatif.
 * </p>
 */
public class EligibilityResult {

    /**
     * Indique si l’étudiant est éligible au cours.
     * <p>true si éligible, false sinon</p>
     */
    private boolean eligible;

    /**
     * Liste des prérequis manquants empêchant l’inscription.
     * <p>Liste vide si aucun prérequis ne manque.</p>
     */
    private List<String> missingPrerequisites;

    /**
     * Message explicatif décrivant le résultat de la vérification.
     * <p>Exemples : "Étudiant éligible", "Prérequis non satisfaits"</p>
     */
    private String message;

    /**
     * Construit un résultat d’éligibilité.
     *
     * @param eligible indique si l’étudiant est éligible
     * @param missingPrerequisites liste des prérequis manquants
     * @param message message descriptif du résultat
     */
    public EligibilityResult(
            boolean eligible,
            List<String> missingPrerequisites,
            String message
    ) {
        this.eligible = eligible;
        this.missingPrerequisites = missingPrerequisites;
        this.message = message;
    }

    /**
     * Indique si l’étudiant est éligible au cours.
     *
     * @return true si éligible, false sinon
     */
    public boolean isEligible() {
        return eligible;
    }

    /**
     * Retourne la liste des prérequis manquants.
     *
     * @return liste des prérequis manquants
     */
    public List<String> getMissingPrerequisites() {
        return missingPrerequisites;
    }

    /**
     * Retourne le message explicatif du résultat.
     *
     * @return message de résultat
     */
    public String getMessage() {
        return message;
    }
}