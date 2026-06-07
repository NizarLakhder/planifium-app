package com.diro.ift2255.model;

import java.util.List;

/**
 * Représente une requête de vérification d’éligibilité à un cours.
 *
 * <p>
 * Cette classe est utilisée comme corps (body) d’une requête HTTP
 * envoyée au endpoint :
 * </p>
 *
 * <pre>
 * POST /courses/{id}/eligibility
 * </pre>
 *
 * <p>
 * Elle contient les informations nécessaires pour déterminer
 * si un étudiant est autorisé à s’inscrire à un cours donné,
 * notamment les cours complétés et le cycle d’études.
 * </p>
 */
public class EligibilityRequest {

    /**
     * Liste des sigles des cours déjà complétés par l’étudiant.
     * <p>Exemple : ["IFT1015", "IFT1025"]</p>
     */
    private List<String> completedCourses;

    /**
     * Cycle d’études de l’étudiant.
     * <p>Valeurs attendues : "bac", "maitrise"</p>
     */
    private String cycle;

    /**
     * Retourne la liste des cours complétés par l’étudiant.
     *
     * @return liste des cours complétés
     */
    public List<String> getCompletedCourses() {
        return completedCourses;
    }

    /**
     * Définit la liste des cours complétés par l’étudiant.
     *
     * @param completedCourses liste des sigles des cours complétés
     */
    public void setCompletedCourses(List<String> completedCourses) {
        this.completedCourses = completedCourses;
    }

    /**
     * Retourne le cycle d’études de l’étudiant.
     *
     * @return cycle d’études
     */
    public String getCycle() {
        return cycle;
    }

    /**
     * Définit le cycle d’études de l’étudiant.
     *
     * @param cycle cycle d’études (ex: "bac", "maitrise")
     */
    public void setCycle(String cycle) {
        this.cycle = cycle;
    }
}