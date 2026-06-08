package com.diro.ift2255.service;

import com.diro.ift2255.model.ResultatAcademique;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;

/**
 * Service responsable de l’accès aux résultats académiques agrégés.
 *
 * <p>
 * Les résultats académiques sont stockés localement dans un fichier CSV
 * fourni dans le cadre du projet. Chaque ligne représente les statistiques
 * agrégées d’un cours suivi par les étudiants du baccalauréat en informatique.
 * </p>
 *
 * <p>
 * Ce service permet de récupérer les statistiques d’un cours précis
 * à partir de son sigle (ex: IFT2255).
 * </p>
 *
 * <p>
 * Ce choix de stockage local (CSV) est volontaire et conforme aux exigences
 * du projet, qui autorisent l’utilisation de fichiers pour la persistance
 * des données.
 * </p>
 */
public class ResultatService {

    /**
     * Chemin du fichier CSV contenant les résultats académiques.
     */
    private static final String FILE_PATH = "data/resultats.csv";

    /**
     * Récupère les résultats académiques agrégés pour un cours donné.
     *
     * <p>
     * La méthode parcourt le fichier CSV ligne par ligne et retourne
     * la première entrée correspondant au sigle fourni.
     * </p>
     *
     * <p>
     * Colonnes attendues dans le fichier CSV :
     * </p>
     * <ul>
     *   <li>Sigle du cours</li>
     *   <li>Nom du cours</li>
     *   <li>Moyenne obtenue (cote littérale)</li>
     *   <li>Score académique (1 à 5)</li>
     *   <li>Nombre total de participants</li>
     *   <li>Nombre de trimestres analysés</li>
     * </ul>
     *
     * @param sigle sigle du cours recherché (ex: IFT2255)
     * @return un {@link Optional} contenant les résultats académiques
     *         si le cours est trouvé, sinon {@link Optional#empty()}
     */
    public Optional<ResultatAcademique> getBySigle(String sigle) {
        try (BufferedReader br = new BufferedReader(
                new FileReader(FILE_PATH))) {

            // Lecture de la ligne d’en-tête
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts[0].equalsIgnoreCase(sigle)) {
                    ResultatAcademique r =
                            new ResultatAcademique();

                    r.setSigle(parts[0]);
                    r.setNom(parts[1]);
                    r.setMoyenne(parts[2]);
                    r.setScore(
                            Double.parseDouble(parts[3])
                    );
                    r.setParticipants(
                            Integer.parseInt(parts[4])
                    );
                    r.setTrimestres(
                            Integer.parseInt(parts[5])
                    );

                    return Optional.of(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}