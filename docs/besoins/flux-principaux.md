---
title: Analyse des besoins - Flux principaux
---

# Flux principaux

## Objectif

Décrire les flux d’interaction entre les acteurs et le système.

## Diagramme de contexte

Diagramme de contexte du projet :

![Diagramme de contexte](diagrammes/diagramme-contexte.jpg)

## Diagrammes d’activités

### Flux 1 — Consultation du catalogue et  recherche de l informations relative à un cours
Ce flux permet à l’étudiant de rechercher des cours par sigle, titre ou mot-clé.
Après validation de la saisie, le système interroge l’API Planifium pour récupérer la liste officielle des cours.
Pour améliorer les performances, on pourrait envisager un cache local : si les données d’une recherche ont déjà été consultées récemment, le système peut les renvoyer directement sans interroger l’API.
Une fois les données obtenues (depuis l’API ou le cache), le système applique les filtres éventuels (horaire, niveau, mots-clés) et affiche les résultats sous forme de liste ou de fiche détaillée.

Acteurs impliqués : Étudiant, Système principal, API Planifium, (optionnellement) Cache local.

![Diagramme d'activite](diagrammes/diagrammeactivité1.jpg)

### Flux 2 — Recherche des détails spécifiques d'un cours 
Ce flux décrit la consultation détaillée d’un cours sélectionné par l’étudiant. Une fois le cours choisi, le système valide la sélection et envoie une requête à l’API Planifium, qui renvoie les informations officielles du cours (sigle, description, crédits, horaires, prérequis). Si l’API ne répond pas, le système affiche un message d’erreur et interrompt le processus. Lorsque les données sont reçues, le système effectue ensuite une requête à la base des résultats académiques afin d’obtenir les statistiques associées (moyenne, taux d’échec, nombre d’inscrits). Parallèlement, il tente de récupérer les avis étudiants stockés dans un fichier JSON provenant du bot Discord : s’il y a au moins cinq avis, une synthèse est générée, sinon un message “Avis insuffisants” est affiché. Enfin, toutes les informations récupérées sont fusionnées et renvoyées sous forme d’une fiche complète consultable par l’étudiant.


![Diagramme d'activite](diagrammes/diagrammeactivité2.jpg)

### Flux 3 — Consultation et comparaison (avis + recommandations)

Ce flux permet à l’étudiant de sélectionner plusieurs cours et d’obtenir une comparaison claire et structurée entre eux. L’étudiant saisit deux ou plusieurs sigles de cours ; le système récupère ensuite les informations officielles de chaque cours via l’API Planifium (titre, crédits, horaires, prérequis). Il complète ces données avec les résultats académiques disponibles (moyennes, taux d’échec, nombre d’inscrits) ainsi que les avis étudiants lorsque le seuil minimal d’avis (≥ 5) est atteint.

Une fois toutes les données réunies, le système génère un tableau comparatif permettant de visualiser rapidement la charge de travail, la difficulté perçue, la réussite académique et la compatibilité horaire de chaque cours. L’étudiant peut ainsi évaluer les différences entre les cours et prendre une décision éclairée.

Acteurs impliqués : Étudiant, Système principal, API Planifium 


![Diagramme d'activite](diagrammes/diagrammeactivité3.jpg)



### Flux 4 — Soumettre un avis au bot discord 


Ce flux représente l’intégration automatique des avis étudiants envoyés depuis un bot Discord. Lorsqu’un étudiant publie un avis avec la commande prévue (ex. !avis Cours: IFT2255 | Difficulté: 7 | Charge: 6 | Commentaire: ...), le bot Discord extrait les informations du message, les transforme en un format structuré (JSON) puis envoie ces données au point d’entrée (endpoint) de l’API de la plateforme.

Le système principal reçoit cet avis, vérifie sa validité (structure du message, cours existant, valeurs correctement formatées). Si l’avis est conforme, il est ajouté à la base interne d’avis et lié au cours correspondant. Si l’avis est invalide, il est ignoré ou rejeté. Lorsqu’un cours atteint le seuil de cinq avis valides, la plateforme met automatiquement à jour la synthèse affichée aux utilisateurs. Ce flux garantit que les avis étudiants sont intégrés continuellement et que les données consultées par les étudiants restent à jour.

- Acteurs impliqués : Étudiant, Bot discord , Système principal

- Acteurs impliqués : Étudiant, Bot discord , Système principal


![Diagramme d'activite](diagrammes/diagrammeactivité4.jpg)
### Description des flux complexes

