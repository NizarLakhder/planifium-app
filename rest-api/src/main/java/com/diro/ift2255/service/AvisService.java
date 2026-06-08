package com.diro.ift2255.service;

import com.diro.ift2255.model.Avis;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AvisService implements IService<Avis> {

    private final String filePath;
    private final ObjectMapper mapper = new ObjectMapper();

    public AvisService() {
        this.filePath = "data/avis.json";
    }

    public AvisService(String filePath) {
        this.filePath = filePath;
    }

    // ==========================
    // MÉTHODES MÉTIER
    // ==========================

    public List<Avis> lireAvis() {
        try {
            File file = new File(filePath);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Avis>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Avis> getByCourse(String courseId) {
        return lireAvis().stream()
                .filter(a -> a.getCours().equalsIgnoreCase(courseId))
                .toList();
    }

    public void ajouterAvis(Avis avis) {
        if (avis.getId() == null || avis.getId().isBlank()) {
            avis.setId(UUID.randomUUID().toString());
        }
        List<Avis> avisList = lireAvis();
        avisList.add(avis);
        sauvegarder(avisList);
    }

    private void sauvegarder(List<Avis> avisList) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(new File(filePath), avisList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ==========================
    // MÉTHODES INTERFACE IService
    // ==========================

    @Override
    public List<Avis> getAll() {
        return lireAvis();
    }

    @Override
    public Avis getById(String id) {
        return lireAvis().stream()
                .filter(a -> id.equals(a.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Avis avis) {
        ajouterAvis(avis);
    }

    @Override
    public void update(String id, Avis updated) {
        List<Avis> avisList = lireAvis();
        boolean found = false;
        for (int i = 0; i < avisList.size(); i++) {
            if (id.equals(avisList.get(i).getId())) {
                updated.setId(id);
                avisList.set(i, updated);
                found = true;
                break;
            }
        }
        if (!found) throw new IllegalArgumentException("Avis introuvable : " + id);
        sauvegarder(avisList);
    }

    @Override
    public void delete(String id) {
        List<Avis> avisList = lireAvis();
        boolean removed = avisList.removeIf(a -> id.equals(a.getId()));
        if (!removed) throw new IllegalArgumentException("Avis introuvable : " + id);
        sauvegarder(avisList);
    }
}
