---
title: Conception - Modèle de données
---

# Modèle de données

## Entités principales

 - Étudiant : représente un utilisateur de la plateforme (profil personnel, préférences, historique des recherches).

- Cours : correspond à un cours offert à l’Université, incluant son code, son titre, son nombre de crédits, ses prérequis et ses 
statistiques de réussite.

- Avis : regroupe les commentaires et évaluations provenant des étudiants (source : Discord). Chaque avis est associé à un cours.

- Profil académique : contient les informations propres à chaque étudiant (programme, cycle, crédits obtenus, statut d’admissibilité).
Inscription : représente la demande d’un étudiant pour s’inscrire à un cours, incluant son statut (en attente, validée, refusée).

> Décrire brièvement chaque entité.

## Relations entre entités

- Un étudiant peut avoir plusieurs avis (un par cours suivi).

- Un étudiant peut effectuer plusieurs inscriptions à différents cours.

- Un cours peut avoir plusieurs avis provenant de divers étudiants.

- Un cours peut être lié à plusieurs inscriptions.

- Un profil académique est associé à un seul étudiant.

## Contraintes métier

- Un étudiant ne peut pas s’inscrire deux fois au même cours dans le même trimestre.

- Une inscription n’est possible que si les prérequis sont validés dans le profil académique.

- Les avis étudiants ne sont affichés qu’à partir d’un seuil minimal (n ≥ 5).

- Les données de l’étudiant doivent respecter les exigences de confidentialité (Loi 25).

- L’étudiant doit être authentifié avant de pouvoir effectuer une inscription.

## Évolution potentielle du modèle

- Ajouter des statuts de réservation

- Support multi-utilisateur par ressource

- Ajouter la possibilité d’évaluer les professeurs en plus des cours.

- Permettre à un étudiant de partager ses combinaisons de cours avec d’autres utilisateurs.

- Ajouter un statut d’inscription (en attente, validée, refusée, annulée).