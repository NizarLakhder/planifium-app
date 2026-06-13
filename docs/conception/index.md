---
title: Conception - Présentation générale
---

# Conception

L’architecture adoptée suit un modèle client–serveur classique, structuré selon une approche inspirée du MVC (Modèle–Vue–Contrôleur).
Le client correspond à l’interface web accessible aux étudiants, tandis que le serveur gère la logique d’application, les échanges de données et la communication avec les sources externes (API Planifium, Discord, système académique).
Cette structure favorise une séparation claire des responsabilités :
Vue : interface utilisateur intuitive et accessible (tableaux de bord, formulaires de recherche, avis).
Contrôleur : gestion des requêtes, validation des données et orchestration des appels aux services externes.
Modèle : stockage et manipulation des données locales (profils utilisateurs, préférences, cours consultés).

## Approche utilisée

Découpage en modules ou couches
Le système est organisé en quatre couches principales :
Interface utilisateur (Front-end) : application web permettant la recherche, la comparaison et la personnalisation des cours.
API applicative (Back-end) : point central qui traite les requêtes, applique les règles métier et interagit avec les autres services.
Sources externes : intégrations avec les API Planifium (catalogue de cours), Discord (avis étudiants) et le système académique (validation des inscriptions).
Base de données interne : gestion des profils étudiants, préférences, historiques de recherches et agrégation de données.

## Contraintes prises en compte

- Contraintes techniques :

Le système doit être compatible avec une architecture web RESTful pour faciliter les échanges entre modules.

Les technologies envisagées incluent : Python/Node.js pour le serveur, React ou Vue.js pour le client, et une base de données 

relationnelle (PostgreSQL ou MySQL).


- Contraintes liées aux exigences:

Confidentialité et conformité légale : respect des politiques de protection des données (Loi 25).

Performance : réponses rapides pour la recherche et la comparaison de cours.

Fiabilité : le système doit rester fonctionnel même si une API externe (Planifium ou Discord) est temporairement indisponible.

Évolutivité : permettre l’ajout futur d’autres universités ou départements sans refonte complète de l’architecture.
