package com.diro.ift2255.model;

/**
 * Représente un avis étudiant associé à un cours.
 *
 * <p>
 * Un avis contient une évaluation subjective d’un étudiant concernant
 * la difficulté perçue et la charge de travail d’un cours,
 * ainsi qu’un commentaire optionnel.
 * </p>
 *
 * <p>
 * Les avis sont collectés via un bot Discord et stockés localement
 * afin d’être agrégés et consultés par l’application.
 * </p>
 */
public class Avis {

    /** Identifiant unique de l’avis */
    private String id;

    /** Sigle du cours concerné par l’avis (ex: IFT2255) */
    private String cours;

    /** Difficulté perçue du cours (échelle de 1 à 5) */
    private int difficulte;

    /** Charge de travail estimée (échelle de 1 à 5) */
    private int charge;

    /** Commentaire libre optionnel de l’étudiant */
    private String commentaire;

    /** Auteur de l’avis (nom ou pseudonyme Discord) */
    private String auteur;

    public Avis() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    /**
     * Construit un avis étudiant complet.
     *
     * @param cours sigle du cours
     * @param difficulte difficulté perçue (1–5)
     * @param charge charge de travail estimée (1–5)
     * @param commentaire commentaire optionnel
     * @param auteur auteur de l’avis
     */
    public Avis(String cours,
                int difficulte,
                int charge,
                String commentaire,
                String auteur) {

        this.cours = cours;
        this.difficulte = difficulte;
        this.charge = charge;
        this.commentaire = commentaire;
        this.auteur = auteur;
    }

    /**
     * Retourne le sigle du cours concerné.
     *
     * @return sigle du cours
     */
    public String getCours() {
        return cours;
    }

    /**
     * Définit le sigle du cours concerné.
     *
     * @param cours sigle du cours
     */
    public void setCours(String cours) {
        this.cours = cours;
    }

    /**
     * Retourne la difficulté perçue du cours.
     *
     * @return difficulté (1–5)
     */
    public int getDifficulte() {
        return difficulte;
    }

    /**
     * Définit la difficulté perçue du cours.
     *
     * @param difficulte difficulté (1–5)
     */
    public void setDifficulte(int difficulte) {
        this.difficulte = difficulte;
    }

    /**
     * Retourne la charge de travail estimée.
     *
     * @return charge de travail (1–5)
     */
    public int getCharge() {
        return charge;
    }

    /**
     * Définit la charge de travail estimée.
     *
     * @param charge charge de travail (1–5)
     */
    public void setCharge(int charge) {
        this.charge = charge;
    }

    /**
     * Retourne le commentaire associé à l’avis.
     *
     * @return commentaire ou chaîne vide
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Définit le commentaire associé à l’avis.
     *
     * @param commentaire commentaire libre
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * Retourne l’auteur de l’avis.
     *
     * @return auteur de l’avis
     */
    public String getAuteur() {
        return auteur;
    }

    /**
     * Définit l’auteur de l’avis.
     *
     * @param auteur auteur de l’avis
     */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
}