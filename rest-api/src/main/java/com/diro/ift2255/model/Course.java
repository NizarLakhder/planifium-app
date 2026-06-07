package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

/**
 * Représente un cours universitaire tel que fourni par l’API Planifium.
 *
 * <p>
 * Cette classe est utilisée comme modèle central pour la manipulation
 * des informations de cours dans l’application :
 * catalogue, recherche, comparaison, vérification d’éligibilité
 * et affichage des horaires.
 * </p>
 *
 * <p>
 * Les champs non utilisés ou inconnus retournés par Planifium
 * sont ignorés afin de garantir la robustesse de la désérialisation JSON.
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {

    /** Liste des sigles des cours prérequis */
    private List<String> prerequisite_courses;

    /** Sigle du cours (ex: IFT2255) */
    private String id;

    /** Nom complet du cours */
    private String name;

    /** Nombre de crédits du cours */
    private double credits;

    /** Description détaillée du cours */
    private String description;

    /**
     * Disponibilité du cours par trimestre.
     * <p>Exemple : { "winter": true, "autumn": false }</p>
     */
    private Map<String, Boolean> available_terms;

    /**
     * Disponibilité du cours par période académique
     * (champ optionnel selon Planifium).
     */
    private Map<String, Boolean> available_periods;

    /**
     * Horaires bruts du cours tels que retournés par Planifium.
     *
     * <p>
     * Cette structure est volontairement générique afin de permettre
     * une manipulation flexible des données complexes
     * (sections, volets, activités).
     * </p>
     */
    private List<Object> schedules;

    // ==========================
    // GETTERS
    // ==========================

    /**
     * Retourne le sigle du cours.
     *
     * @return sigle du cours
     */
    public String getId() {
        return id;
    }

    /**
     * Retourne le nom du cours.
     *
     * @return nom du cours
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne le nombre de crédits du cours.
     *
     * @return crédits
     */
    public double getCredits() {
        return credits;
    }

    /**
     * Retourne la description du cours.
     *
     * @return description du cours
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retourne la disponibilité du cours par trimestre.
     *
     * @return map des trimestres disponibles
     */
    public Map<String, Boolean> getAvailable_terms() {
        return available_terms;
    }

    /**
     * Retourne la disponibilité du cours par période.
     *
     * @return map des périodes disponibles
     */
    public Map<String, Boolean> getAvailable_periods() {
        return available_periods;
    }

    /**
     * Retourne les horaires bruts du cours.
     *
     * @return liste des horaires
     */
    public List<Object> getSchedules() {
        return schedules;
    }

    /**
     * Retourne la liste des cours prérequis.
     *
     * @return liste des sigles des prérequis
     */
    public List<String> getPrerequisite_courses() {
        return prerequisite_courses;
    }

    // ==========================
    // SETTERS
    // ==========================

    /**
     * Définit le sigle du cours.
     *
     * @param id sigle du cours
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Définit le nom du cours.
     *
     * @param name nom du cours
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Définit le nombre de crédits du cours.
     *
     * @param credits nombre de crédits
     */
    public void setCredits(double credits) {
        this.credits = credits;
    }

    /**
     * Définit la description du cours.
     *
     * @param description description du cours
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Définit la disponibilité du cours par trimestre.
     *
     * @param available_terms map des trimestres
     */
    public void setAvailable_terms(Map<String, Boolean> available_terms) {
        this.available_terms = available_terms;
    }

    /**
     * Définit la disponibilité du cours par période.
     *
     * @param available_periods map des périodes
     */
    public void setAvailable_periods(Map<String, Boolean> available_periods) {
        this.available_periods = available_periods;
    }

    /**
     * Définit les horaires bruts du cours.
     *
     * @param schedules horaires du cours
     */
    public void setSchedules(List<Object> schedules) {
        this.schedules = schedules;
    }

    /**
     * Définit la liste des cours prérequis.
     *
     * @param prerequisite_courses liste des prérequis
     */
    public void setPrerequisite_courses(List<String> prerequisite_courses) {
        this.prerequisite_courses = prerequisite_courses;
    }
}