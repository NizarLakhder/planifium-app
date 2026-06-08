package com.diro.ift2255.controller;

import java.util.List;
import java.util.Optional;

import com.diro.ift2255.model.User;
import com.diro.ift2255.service.UserService;
import com.diro.ift2255.util.ResponseUtil;
import com.diro.ift2255.util.ValidationUtil;

import io.javalin.http.Context;


public class UserController {
    // Service qui contient la logique métier pour la manipulation des utilisateurs (users) et la communication avec les services externes
    private final UserService service;
    
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Récupère la liste de tous les utilisateurs.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void getAllUsers(Context ctx) {
        List<User> users = service.getAllUsers();
        ctx.json(users);
    }

    /**
     * Récupère un utilisateur spécifique par son ID.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void getUserById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        Optional<User> user = service.getUserById(id);
        if (user.isPresent()) {
            ctx.json(user.get());
        } else {
            ctx.status(404).json(ResponseUtil.formatError("Aucun utilisateur ne correspond à l'ID: " + id));
        }
    }

    /**
     * Crée un nouvel utilisateur avec les données passées dans le body.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void createUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        if (!ValidationUtil.isEmail(user.getEmail())) {
            ctx.status(400).json("Invalid email format");
            return;
        }
        service.createUser(user);
        ctx.status(201).json(user);
    }

    /**
     * Met à jour un utilisateur existant avec les données passées dans le body.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void updateUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        User updated = ctx.bodyAsClass(User.class);
        service.updateUser(id, updated);
        ctx.json(updated);
    }

    /**
     * Supprime un utilisateur existant.
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void deleteUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        service.deleteUser(id);
        ctx.status(204);
    }
}
