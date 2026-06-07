package com.diro.ift2255;

import io.javalin.Javalin;

import com.diro.ift2255.config.Routes;

public class Main {
    public static void main(String[] args) {
        // Crée une instance de Javalin avec une configuration personnalisée
        // Ici, on définit le type de contenu par défaut des réponses HTTP en JSON
        Javalin app = Javalin.create(config -> {

            config.http.defaultContentType = "application/json";

            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";      // servi à la racine
                staticFiles.directory = "/public"; // dossier dans resources
                staticFiles.precompress = false;
            });

        });
            

        // Enregistre toutes les routes de l'application
        Routes.register(app);

        // Démarre le serveur sur le port 7070 (choix arbitraire)
        app.start(7070);
        System.out.println("Serveur démarré sur http://localhost:7070");
    }
}