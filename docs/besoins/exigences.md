---
title: Analyse des besoins - Exigences
---

# Exigences

## Exigences fonctionnelles


| Exigence | Description détaillée | Priorité |
|----------|------------------------|----------|
| EF1 – Authentification sécurisée | L’étudiant peut se connecter à la plateforme à l’aide d’identifiants sécurisés. Les informations sont vérifiées avant de créer une session active. | Critique |
| EF2 – Consultation des cours officiels | Le système affiche la liste officielle des cours récupérés depuis l’API Planifium, incluant : prérequis, co-requis, taux d’échec, nombre d’inscrits et crédits. | Critique |
| EF3 – Recherche et filtres | L’étudiant peut rechercher un cours par sigle, titre, mots-clés ou horaire. Les résultats sont filtrés et présentés de façon claire. | Critique |
| EF4 – Validation d’inscription | L’étudiant peut soumettre une demande d’inscription. La requête est transmise au TGDE pour validation académique (prérequis, limites, statut). | Importante |
| EF5 – Recommandations personnalisées | Le système génère automatiquement des recommandations basées sur le profil étudiant (programme, préférences, historique académique). | Importante |
| EF6 – Gestion des sources externes | L’administrateur peut configurer et maintenir les connexions aux sources externes : API Planifium, résultats académiques, bot Discord. | Importante |
| EF7 – Soumission d’avis | L’étudiant peut rédiger un avis sur un cours suivi. L’avis est stocké et intégré à l’analyse lorsqu’au moins 5 avis valides sont collectés. | Secondaire |
| EF8 – Personnalisation des choix | L’étudiant peut indiquer ses préférences (théorie/pratique, charge faible vs élevée, intérêts). Ces préférences influencent les résultats de recherche. | Secondaire |
| EF9 – Comparaison de cours | L’étudiant peut comparer plusieurs cours selon : charge de travail estimée, difficulté perçue, taux de réussite, horaires. | Secondaire |
| EF10 – Affichage des avis agrégés | Le système affiche les avis étudiants agrégés provenant du bot Discord, filtrés par cours et accessibles seulement si n ≥ 5 avis. | Secondaire |

## Exigences non fonctionnelles


Exemple :

| Besoin non fonctionnel | Description améliorée | Justification |
|------------------------|------------------------|---------------|
| Compatibilité navigateurs | L’application doit fonctionner sans erreur sur les versions récentes (≤2 ans) de Chrome, Firefox et Safari. Les pages doivent s’afficher correctement avec un écart visuel < 5%. | Les étudiants utilisent des appareils variés : garantir un comportement identique évite des blocages d’accès. |
| Protection des données (Loi 25) | Les données personnelles utilisées par la plateforme doivent être anonymisées, chiffrées au repos et accessibles uniquement avec consentement explicite. Aucun avis étudiant ne doit contenir d’information permettant l’identification. | Obligations légales de la Loi 25 concernant les données sensibles (profil étudiant, avis, historique académique). |
| Accessibilité & simplicité d’usage | Un nouvel utilisateur doit comprendre comment rechercher et consulter un cours en moins de 3 minutes, sans tutoriel. Le nombre de clics nécessaires pour effectuer ces actions ne doit pas dépasser 3. | Assure une interface intuitive pour des étudiants aux niveaux technologiques variés, y compris internationaux. |
| Performance de navigation | Le système doit conserver les 5 recherches récentes localement afin de proposer une autocomplétion en moins de 200 ms. | Réduit la friction utilisateur et accélère la comparaison de cours fréquemment consultés. |
| Gestion des erreurs | Les messages d’erreur doivent être explicites, par exemple « Prérequis manquant : IFT1000 » ou « Aucun cours trouvé ». Chaque erreur doit inclure une suggestion corrective. | Améliore la compréhension et la prise de décision de l’utilisateur, et réduit les erreurs répétées. |

## Priorisation






## Types d'utilisateurs

> Identifier les différents profils qui interagiront avec le système.

| Type d’utilisateur | Description | Exemples de fonctionnalités accessibles |
|--------------------|-------------|------------------------------------------|
| Utilisateur invité | Accès limité, pas d’authentification | Consultation des ressources |
| Utilisateur authentifié | Compte personnel, fonctions principales | Réservation, historique |
| Administrateur | Droits étendus, gestion des ressources | Création/suppression de ressources, gestion des utilisateurs |



## Infrastructures et besoins matériels

## Besoins matériels

La solution proposée est légère et ne nécessite pas une infrastructure matérielle complexe.  
Elle doit cependant garantir une capacité suffisante pour manipuler les données provenant de l’API Planifium, des résultats académiques et des avis étudiants.

Les besoins matériels minimaux sont :

- Un serveur léger ou une machine locale capable d’exécuter une application Java (JDK 17+).
- 2 Go de RAM suffisent pour exécuter le backend, les tests et les opérations de lecture/écriture de données.
- 100 Mo d’espace disque pour stocker les fichiers persistants (CSV, JSON) et les logs.
- Une connexion réseau stable pour interroger l’API Planifium et recevoir les données envoyées par le bot Discord.

Ces ressources sont suffisantes car le système repose principalement sur des appels externes (API) et sur une quantité raisonnable de données persistantes (quelques milliers de cours, quelques centaines d’avis). Aucune charge computationnelle lourde n’est attendue.

### Solution de stockage

Pour cette phase du projet, nous avons choisi une solution de stockage basée sur des fichiers persistants (CSV pour les résultats académiques et JSON pour les avis étudiants).  

Ce choix se justifie par plusieurs facteurs :

- Le volume de données manipulé reste modeste (liste des cours, résultats agrégés, avis étudiants).
- L’objectif du projet est pédagogique : privilégier la simplicité plutôt qu’une base de données lourde.
- Les formats CSV et JSON sont directement compatibles avec l’API Planifium, ce qui minimise la transformation des données.
- Ces formats facilitent le développement rapide, les tests unitaires et la portabilité sur n’importe quel poste étudiant.

Une migration future vers une base de données relationnelle (PostgreSQL, SQLite) reste possible si l’outil devait supporter un grand volume d’avis ou des fonctionnalités avancées.

### Solution d’intégration


L’intégration du système repose sur une architecture simple et modulaire :

1. **Intégration avec Planifium**  
   Le backend utilise un client HTTP dédié pour interroger l’API Planifium et transformer les réponses JSON en objets métier (cours, horaires, prérequis).

2. **Intégration avec les données académiques**  
   Les résultats agrégés sont chargés depuis un fichier CSV et convertis en objets de domaine accessibles aux fonctionnalités de comparaison.

3. **Intégration des avis étudiants**  
   Le bot Discord envoie automatiquement les avis reçus vers un endpoint de l’API.  
   Le backend les valide, les stocke dans un fichier JSON, puis les agrège avant l’affichage.

4. **Intégration interne backend → console**  
   La couche service expose des méthodes simples (rechercherCours, comparerCours, getDetails) utilisées par l’interface console.  
   Cette séparation respecte l’architecture MVC et facilite l’évolution vers une interface web.

Cette approche d’intégration assure une faible dépendance entre les composants, une bonne maintenabilité et une compatibilité future avec d’autres interfaces ou systèmes.
