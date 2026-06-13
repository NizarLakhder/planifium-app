---
title: Analyse des besoins - Cas d'utilisation
---

# Cas d'utilisation

## Vue d’ensemble

TODO: Introduction aux cas d’utilisation du système.

## Liste des cas d’utilisation - Vue d'ensemble 

| **ID**   | **Nom du CU**                          | **Acteurs**                                | **Scénario résumé** |
|----------|-----------------------------------------|---------------------------------------------|----------------------|
| **CU01** | Authentification de l’utilisateur        | Étudiant                                    | 1. L’étudiant saisit ses identifiants dans la page de connexion.<br>2. Le système vérifie les informations et établit une session utilisateur.<br>3. Si la connexion échoue, un message d’erreur est affiché.<br>4. Une fois connecté, l’étudiant accède à son tableau de bord personnalisé. |
| **CU02** | Consulter le catalogue Planifium         | Étudiant, **API Planifium**                 | 1. L’étudiant accède au catalogue des cours.<br>2. Il saisit un code, un titre ou un mot-clé.<br>3. Le système interroge l’API Planifium pour obtenir la liste officielle des cours correspondants.<br>4. Les résultats s’affichent avec les informations officielles (crédits, horaires, description, prérequis). |
| **CU03** | Afficher les détails d’un cours | Étudiant (principal), **API Planifium** (secondaire) | 1. L’étudiant sélectionne un cours dans le catalogue.<br>2. Le système interroge l’API Planifium pour récupérer les informations officielles du cours (nom, crédits, description, prérequis, horaire).<br>3. Le système récupère les données internes complémentaires (ex. résultats académiques).<br>4. Le système présente toutes les informations du cours de manière claire : description, crédits, prérequis, horaire, résultats académiques. |
| **CU04** | Consulter les avis étudiants             | Étudiant                                    | 1. L’étudiant accède à la section “Avis étudiants”.<br>2. Le système charge les avis stockés localement (fichier JSON).<br>3. Les avis ne sont affichés qu’à partir d’un seuil minimal (n ≥ 5).<br>4. L’étudiant consulte la synthèse (charge, difficulté, commentaires). |
| **CU05** | Comparer plusieurs cours                 | Étudiant                                    | 1. L’étudiant sélectionne plusieurs cours.<br>2. Le système compile les données officielles (Planifium) et les avis étudiants (fichier JSON).<br>3. Un tableau comparatif est généré (charge estimée, difficulté, taux d’échec, compatibilité horaire).<br>4. L’étudiant visualise les différences pour ajuster sa sélection. |
| **CU06** | Modifier son profil utilisateur          | Étudiant                                    | 1. L’étudiant accède à son profil.<br>2. Il modifie ses informations (programme, niveau, préférences théorie/pratique, intérêts).<br>3. Le système enregistre les nouvelles données.<br>4. Les préférences mises à jour sont utilisées par le système pour personnaliser l’affichage et les recommandations. |
| **CU07** | Mettre à jour les avis étudiants (Discord → Plateforme) | Bot Discord, Système | 1. Un étudiant publie un avis via la commande `!avis ...` sur Discord.<br>2. Le bot Discord extrait les champs (cours, difficulté, charge, commentaire).<br>3. Le bot envoie l’avis à l’API interne via une requête POST.<br>4. Le backend valide le format des données.<br>5. Le backend ajoute l’avis dans `data.json`.<br>6. Le système confirme la réception au bot.<br>7. Le bot affiche un message de confirmation à l’utilisateur.<br><br>**Scénario alternatif — Format invalide**<br>4a.1. Le backend détecte une erreur dans les champs reçus.<br>4a.2. Le bot affiche « Format invalide. Utilise : !avis Cours: XXX | Difficulté: X | Charge: X | Commentaire: ... ». <br><br>**Scénario alternatif — Échec de sauvegarde**<br>5a.1. Le fichier `data.json` est inaccessible.<br>5a.2. Le bot affiche « Erreur interne : avis non enregistré ». |



## Diagramme de cas d'utilisation 

Diagramme de cas d'utilisation 

![Diagramme de CU](diagrammes/diagramme-CU.jpg)

## Détail

### CU01 - Connexion

**Acteurs** : Utilisateur (principal)

**Préconditions** :  L’étudiant possède déjà un compte valide et le système d’authentification est opérationnel.

**PostConditions** :  L’étudiant est authentifié, une session sécurisée est créée et il accède à son tableau de bord.

**Déclencheur** :   L’utilisateur saisit ses identifiants et clique sur « Connexion ».

**Dépendances** :   service d’authentification et base de données des comptes utilisateurs.

**But** : Permettre à l’étudiant d’accéder de façon sécurisée à la plateforme pour consulter ses informations.

**Scénario principal :**

1. L’étudiant saisit son identifiant et son mot de passe.<br>
2. Le système vérifie la présence du compte dans la base de données.<br>
3. Le système valide le mot de passe.<br>
4. Le système crée une session sécurisée.<br>
5. Le tableau de bord s’affiche.<br>

**Scénario alternatif :**


**3a – Mot de passe incorrect**<br>
3a.1. Le système affiche « Mot de passe incorrect ». <br>
3a.2. L’étudiant peut réessayer.<br>
<br>
**2a – Compte inexistant**<br>
2a.1. Le système affiche « Compte introuvable ». <br>
2a.2. Le scénario se termine.<br>


### CU02 - Consulter le catalogue Planifium

**Acteurs** : Étudiant (principal), API Planifium (secondaire)

**Préconditions**: L’étudiant est connecté et l’API Planifium est disponible.

***Déclencheur**  : L’étudiant effectue une recherche par code, titre ou mot-clé.

**Dépendances** : API Planifium pour les données officielles.

**But** : Permettre à l’étudiant de consulter la liste officielle des cours offerts par l’université.

**Scénario principal :**

1. L’étudiant ouvre la section « Catalogue ». <br>
2. Il saisit un code, un titre ou un mot-clé. <br>
3. Le système interroge l’API Planifium. <br>
4. L’API retourne les cours correspondants. <br>
5. Le système affiche les résultats. <br>

**Scénario alternatif :**

**3a – API Planifium indisponible**<br>
3a.1. Le système affiche « Service temporairement indisponible ». <br>
3a.2. Le scénario se termine.<br>
<br>
**4a – Aucun résultat trouvé**<br>
4a.1. Le système affiche « Aucun cours trouvé ». <br>
4a.2. Le scénario se termine.<br>

### CU03 – Afficher le détail d’un cours

**Acteurs** : Étudiant (principal), API Planifium (secondaire)

**Préconditions** :  
L’étudiant a sélectionné un cours dans le catalogue.

**PostConditions** :  
Le système affiche toutes les informations détaillées du cours (description, crédits, horaire, prérequis, résultats académiques…).

**Déclencheur** :  
L’étudiant clique sur un cours pour en afficher les détails.

**Dépendances** :  
API Planifium, Base de données interne (résultats ou données locales).

**But** :  
Permettre à l’étudiant de consulter toutes les informations utiles sur un cours.

---

 **Scénario principal :**

1. L’étudiant sélectionne un cours dans la liste de résultats. <br>
2. Le système interroge l’API Planifium pour récupérer les informations officielles du cours. <br>
3. Le système récupère les données internes complémentaires (ex. résultats académiques, historiques). <br>
4. Le système affiche clairement : <br>
&nbsp;&nbsp;&nbsp;• le sigle du cours <br>
&nbsp;&nbsp;&nbsp;• le nom du cours <br>
&nbsp;&nbsp;&nbsp;• les crédits <br>
&nbsp;&nbsp;&nbsp;• la description <br>
&nbsp;&nbsp;&nbsp;• les prérequis et co-requis <br>
&nbsp;&nbsp;&nbsp;• le cycle / niveau <br>
&nbsp;&nbsp;&nbsp;• l’horaire pour la session sélectionnée <br>
&nbsp;&nbsp;&nbsp;• les résultats académiques (moyenne, inscrits, échecs) <br>

 **Scénarios alternatifs :**

**2a – API Planifium indisponible** <br>
2a.1. Le système affiche : « Informations du cours momentanément indisponibles. » <br>
2a.2. Le scénario se termine. <br>

**3a – Données académiques manquantes** <br>
3a.1. Le système affiche les détails disponibles sans résultats académiques. <br>
3a.2. Un message d’avertissement est affiché : « Données académiques non disponibles. » <br>

### CU04 – Consulter les avis étudiants

**Acteurs** : Étudiant (principal)

**Préconditions** : Le fichier `avis.json` contient des avis valides.

**PostConditions** : Une synthèse des avis est affichée.

**Déclencheur** : L’étudiant clique sur « Voir les avis ».

**Dépendances** : avis.json, AvisService, AvisController.

**But** : Permettre d’évaluer la charge et la difficulté d’un cours.

 **Scénario principal :**

1. L’étudiant sélectionne un cours et clique sur « Voir les avis ». <br>
2. Le système charge les avis du fichier `dataDiscord/avis.json`. <br>
3. Le système filtre uniquement les avis du cours sélectionné. <br>
4. Le système vérifie qu’il y a au moins 5 avis valides. <br>
5. Le système génère une synthèse (difficulté moyenne, charge moyenne, commentaires). <br>
6. La synthèse est affichée. <br>

 **Scénarios alternatifs :**

**4a – Moins de 5 avis**<br>
4a.1. Le système affiche : « Pas assez d’avis pour une synthèse fiable. »<br>
4a.2. Le scénario se termine.<br>

**2a – Fichier JSON introuvable**<br>
2a.1. Le système affiche : « Avis indisponibles pour le moment. »<br>
2a.2. Le scénario se termine.<br>


---

### CU05 - Comparer plusieurs cours
**Acteurs** : Étudiant (principal)


**Préconditions** : L’étudiant a sélectionné plusieurs cours à comparer.

**PostConditions** : Un tableau comparatif est affiché selon la charge, la difficulté et le taux de réussite.

**Déclencheur** : L’étudiant clique sur « Comparer les cours sélectionnés ».

**Dépendances** : Données provenant de Planifium et des avis étudiants.

**But** : Permettre à l’étudiant de comparer plusieurs cours pour choisir la combinaison la plus adaptée.

**Scénario principal :**

1. L’étudiant sélectionne plusieurs cours. <br>
2. Le système récupère les informations nécessaires (charge, difficulté, taux de réussite). <br>
3. Le système génère un tableau comparatif. <br>
4. Le tableau comparatif est affiché. <br>

**Scénario alternatif :**

**1a – Un seul cours sélectionné**<br>
1a.1. Le système affiche « Sélectionnez au moins deux cours ». <br>
1a.2. Le scénario se termine.<br>
<br>
**2a – Données manquantes pour un cours**<br>
2a.1. Le système signale les cours aux données incomplètes. <br>
2a.2. Le tableau partiel est affiché. <br>

### CU06 - Modifier son profil utilisateur

**Acteurs** : Étudiant (principal)

**Préconditions** : L’étudiant est connecté à la plateforme.
à
**PostConditions** : Les informations du profil sont mises à jour et sauvegardées.

**Déclencheur** : L’étudiant accède à la section « Mon profil » et sélectionne « Modifier mes informations ».

**Dépendances** : Base de données du profil utilisateur.

**But** : Permettre à l’étudiant de personnaliser son profil pour obtenir des recommandations adaptées.

**Scénario principal :**

1. L’étudiant ouvre la section « Mon profil ». <br>
2. Il clique sur « Modifier mes informations ». <br>
3. Il modifie ses données personnelles. <br>
4. Le système valide les nouvelles informations. <br>
5. Le système enregistre les modifications. <br>
6. Un message de confirmation est affiché. <br>

**Scénario alternatif :**

**4a – Informations invalides**<br>
4a.1. Le système affiche les erreurs liées aux champs invalides. <br>
4a.2. L’étudiant corrige les informations. <br>
<br>
**5a – Base de données indisponible**<br>
5a.1. Le système affiche « Enregistrement impossible pour le moment ». <br>
5a.2. Le scénario se termine.<br>

### CU07 – Mettre à jour les avis étudiants

**Acteurs** : Bot Discord (principal), Système (secondaire)

**Préconditions** :  
Le bot Discord est opérationnel et reçoit un message au format valide (ex. `!avis Cours: IFT2255 | Difficulté: 7 | Charge: 6 | Commentaire: ...`).  
Le serveur Javalin est en cours d’exécution et expose l’endpoint de réception des avis.

**PostConditions** :  
L’avis est validé, transformé, puis ajouté au fichier `data.json`.  
Il sera ensuite pris en compte dans l’agrégation des avis pour chaque cours.

**Déclencheur** :  
Un étudiant envoie un message contenant un avis sur Discord, et le bot l’interprète comme un nouvel avis.

**Dépendances** :  
Bot Discord, API interne Javalin, fichier JSON contenant les avis.

**But** :  
Mettre à jour la base d’avis étudiants afin que la plateforme puisse calculer des statistiques fiables (difficulté, charge, commentaires).



**Scénario principal :**

1. Un étudiant publie un message d’avis sur Discord via la commande `!avis ...`. <br>
2. Le bot Discord extrait automatiquement les champs (cours, difficulté, charge, commentaire). <br>
3. Le bot envoie l’avis formaté à l’API Javalin via une requête HTTP POST. <br>
4. Le backend valide le format des données reçues. <br>
5. Le backend ajoute l'avis au fichier `data.json`. <br>
6. Le système confirme la réception correcte au bot. <br>
7. Le bot envoie un message de confirmation sur Discord. <br>



**Scénario alternatif :**

**4a – Format invalide** <br>
4a.1. Le backend détecte que certains champs sont manquants ou incorrects. <br>
4a.2. Le backend renvoie une erreur au bot Discord. <br>
4a.3. Le bot affiche : « Format invalide. Utilise : !avis Cours: XXX | Difficulté: X | Charge: X | Commentaire: ... ». <br>
4a.4. Le scénario se termine. <br>

<br>

**5a – Échec de sauvegarde (fichier JSON inaccessible)** <br>
5a.1. Le backend n’arrive pas à écrire dans `dataDiscord/data.json`. <br>
5a.2. Le backend renvoie un message d’erreur au bot. <br>
5a.3. Le bot affiche : « Erreur interne : avis non enregistré ». <br>
5a.4. Le scénario se termine. <br>