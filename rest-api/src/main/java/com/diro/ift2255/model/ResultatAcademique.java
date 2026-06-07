package com.diro.ift2255.model;

/**
 * Représente les résultats académiques agrégés d’un cours.
 *
 * <p>
 * Ces données proviennent d’un fichier local (CSV) et sont exposées
 * via l’API REST par le endpoint :
 * </p>
 *
 * <pre>
 * GET /results/{sigle}
 * </pre>
 *
 * <p>
 * Elles permettent aux étudiants de consulter des indicateurs
 * globaux de performance pour un cours donné.
 * </p>
 */
public class ResultatAcademique {

    /**
     * Sigle du cours (ex: IFT2255).
     */
    private String sigle;

    /**
     * Nom complet du cours.
     */
    private String nom;

    /**
     * Moyenne finale obtenue par les étudiants (format texte).
     * <p>Ex: "B+", "A-", etc.</p>
     */
    private String moyenne;

    /**
     * Score académique global du cours.
     * <p>Valeur numérique normalisée (ex: sur 5).</p>
     */
    private double score;

    /**
     * Nombre total d’étudiants ayant participé au cours.
     */
    private int participants;

    /**
     * Nombre de trimestres analysés pour calculer les statistiques.
     */
    private int trimestres;

    /**
     * Constructeur par défaut requis pour la désérialisation.
     */
    public ResultatAcademique() {}

    /**
     * Retourne le sigle du cours.
     *
     * @return sigle du cours
     */
    public String getSigle() { 
        return sigle; 
    }

    /**
     * Définit le sigle du cours.
     *
     * @param sigle sigle du cours
     */
    public void setSigle(String sigle) { 
        this.sigle = sigle; 
    }

    /**
     * Retourne le nom du cours.
     *
     * @return nom du cours
     */
    public String getNom() { 
        return nom; 
    }

    /**
     * Définit le nom du cours.
     *
     * @param nom nom du cours
     */
    public void setNom(String nom) { 
        this.nom = nom; 
    }

    /**
     * Retourne la moyenne finale du cours.
     *
     * @return moyenne finale (ex: A-, B+)
     */
    public String getMoyenne() { 
        return moyenne; 
    }

    /**
     * Définit la moyenne finale du cours.
     *
     * @param moyenne moyenne finale
     */
    public void setMoyenne(String moyenne) { 
        this.moyenne = moyenne; 
    }

    /**
     * Retourne le score académique du cours.
     *
     * @return score académique
     */
    public double getScore() { 
        return score; 
    }

    /**
     * Définit le score académique du cours.
     *
     * @param score score académique
     */
    public void setScore(double score) { 
        this.score = score; 
    }

    /**
     * Retourne le nombre de participants.
     *
     * @return nombre d’étudiants
     */
    public int getParticipants() { 
        return participants; 
    }

    /**
     * Définit le nombre de participants.
     *
     * @param participants nombre d’étudiants
     */
    public void setParticipants(int participants) { 
        this.participants = participants; 
    }

    /**
     * Retourne le nombre de trimestres analysés.
     *
     * @return nombre de trimestres
     */
    public int getTrimestres() { 
        return trimestres; 
    }

    /**
     * Définit le nombre de trimestres analysés.
     *
     * @param trimestres nombre de trimestres
     */
    public void setTrimestres(int trimestres) { 
        this.trimestres = trimestres; 
    }
}