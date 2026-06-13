---
title: Vue d'ensemble
---

<style>
    @media screen and (min-width: 76em) {
        .md-sidebar--primary {
            display: none !important;
        }
    }
</style>

# plateforme de gesion de cours - planifium 

## Lien vers le repo github
 
[https://github.com/NizarLakhder/planifium-app.git](https://github.com/NizarLakhder/planifium-app.git)


## Description du projet

#### Objectif du processus
Le projet consiste à concevoir une plateforme web intelligente, accessible via une API REST, destinée aux étudiants pour les aider à prendre des décisions éclairées dans leur choix de cours.

Cette plateforme vise à :
Centraliser les données officielles (via l’API Planifium et les résultats académiques agrégés) et informelles (avis étudiants recueillis sur Discord) ;
Fournir des tableaux de bord interactifs permettant de visualiser la charge de travail, le taux de réussite et les moyennes des cours ;
Offrir une recherche intelligente et personnalisée selon le profil de l’étudiant ;
Permettre la comparaison de plusieurs cours pour estimer la charge combinée et la compatibilité des choix ;
Assurer la confidentialité des données et le respect des normes légales (Loi 25).

---

#### Présentation générale et avancement du projet (dernier jalon)

À ce stade du projet, nous travaillons sur le dernier jalon du devoir, qui consiste à finaliser l’implémentation fonctionnelle de la plateforme conformément à la conception définie lors des phases précédentes.

Le travail actuel porte principalement sur :
L’intégration complète des différentes sources de données, incluant le catalogue officiel des cours via l’API Planifium, les résultats académiques agrégés, ainsi que les avis étudiants collectés et stockés localement ;
La mise en place des fonctionnalités clés attendues par l’énoncé, notamment la recherche avancée de cours, l’affichage détaillé des informations d’un cours, et la comparaison de plusieurs cours dans un tableau synthétique ;
L’agrégation et la présentation des avis étudiants afin d’estimer la charge de travail et la difficulté perçue, ainsi que l’affichage des résultats académiques agrégés comme indicateurs objectifs de difficulté ;
La vérification de la cohérence entre le frontend et le backend à travers des services REST dédiés, garantissant une séparation claire des responsabilités et une architecture maintenable ;
La validation du comportement du système à l’aide de scénarios réalistes d’utilisation, afin de s’assurer que la plateforme répond aux besoins des étudiants de manière claire, fiable et compréhensible.

Ce dernier jalon vise donc à livrer une solution fonctionnelle, cohérente et conforme aux exigences du cours, tout en démontrant la capacité de l’équipe à concevoir, intégrer et justifier des choix techniques et fonctionnels dans un contexte réel de génie logiciel.

---

#### Acteurs impliqués
- **Étudiant** : Cherche, compare et choisit ses cours  
- **Administration** : Fournit de l’information sur les cours et gère les inscriptions  
- **Planifium API** : Donne accès au catalogue officiel des programmes, des cours et des horaires  
- **Système d’information (résultats académiques agrégés)** : Regroupe les résultats globaux d’un cours donné à une session précise (moyenne finale, nombre d’étudiants inscrits, nombre d’échecs)  
- **Sources informelles (Discord, forum, bouche-à-oreille)** : Regroupe les avis d’autres étudiants  

---
#### Étapes du processus

| Étape | Acteur                    | Action                                             | Entrée                                                                           | Sortie                                                     |   |
| ----- | ------------------------- | -------------------------------------------------- | -------------------------------------------------------------------------------- | ---------------------------------------------------------- | - |
| 1     | Étudiant                  | Se connecter au système                            | Identifiants (nom d’utilisateur, mot de passe)                                   | Accès sécurisé à la plateforme                             |   |
| 2     | Étudiant                  | Consulter le catalogue Planifium                   | Code, titre ou mots-clés de cours                                                | Requête envoyée à l’API Planifium                          |   |
| 3     | **Système Planifium**     | Fournir les informations officielles sur les cours | Requête de l’étudiant                                                            | Liste des cours (titre, crédits, horaire, prérequis, etc.) |   |
| 4     | Étudiant                  | Vérifier les prérequis et contraintes              | Informations du cours et du programme                                            | Liste de cours éligibles                                   |   |
| 5     | **Système Discord / Bot** | Extraire et agréger les avis étudiants             | Messages et réactions des étudiants sur Discord                                  | Base d’avis anonymisée (difficulté, charge, satisfaction)  |   |
| 6     | Étudiant                  | Consulter les avis étudiants                       | Données agrégées (Discord + base locale)                                         | Synthèse des avis et estimations de difficulté             |   |
| 7     | Étudiant                  | Comparer plusieurs cours                           | Informations collectées (officielles et avis)                                    | Tableau comparatif : charge, difficulté, taux d’échec      |   |
| 8     | Étudiant                  | Personnaliser les recommandations                  | Profil personnel (intérêts, niveau, disponibilité, préférences théorie/pratique) | Classement personnalisé des cours recommandés              |   |
| 9     | Étudiant                  | Sauvegarder ou exporter ses choix                  | Sélection finale de cours                                                        | Liste personnelle enregistrée ou exportée                  |   |
| 10    | **Administrateur**        | Gérer les données du système                       | Données Planifium, résultats académiques, avis étudiants                         | Mise à jour de la base et supervision du système           |   |

#### Contraintes du processus
1. **Informations dispersées et manque de centralisation** : Les informations nécessaires sont éparpillées sur différentes sources/sites.  
2. **Avis non standardisés** : Les avis ne sont pas forcément qualitatifs ou peuvent être biaisés, voire obsolètes.  
3. **Pas de personnalisation selon le profil de l’étudiant** : Le processus actuel ne prend pas en compte les préférences et réalités spécifiques des étudiants.  
4. **Temps et efforts requis** : Le processus manuel est long et exige une forte implication de l’étudiant.  
5. **Accessibilité et interface limitée** : Les plateformes ne sont pas conçues pour faciliter la recherche rapide ou la comparaison des cours, ni pour les étudiants ayant un handicap.  
6. **Confidentialité et légalité** : Certaines sources peuvent ne pas respecter la confidentialité ou la législation en vigueur (ex. Loi 25 au Québec).  

---

---

---

#### Endpoints de l’API REST

La plateforme expose une API REST permettant l’accès aux données du catalogue de cours, aux avis étudiants et aux résultats académiques agrégés.  
Les endpoints liés au catalogue agissent comme des **wrappers** autour de l’API externe **Planifium**, afin de simplifier son utilisation côté frontend et de contrôler le volume de données retournées.

##### Catalogue des cours (wrappers Planifium)

- **GET `/courses`**  
  Wrapper de l’endpoint Planifium permettant de récupérer une liste de cours filtrée.  
  Par défaut, l’application évite de récupérer l’ensemble des cours pour des raisons de performance.

  **Exemple d’entrée :**  
  `http://localhost:7070/courses`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/courses`

  **Sortie :** Liste des cours (sigle, nom, crédits, description, disponibilité par trimestre).

---

- **GET `/courses?ids=IFT1015,IFT1025,IFT2255`**  
  Wrapper permettant de récupérer uniquement certains cours à partir de leurs sigles.

  **Exemple d’entrée :**  
  `http://localhost:7070/courses?ids=IFT1015,IFT1025,IFT2255`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/courses?courses_sigle=ift1015,ift1025,ift2255`

  **Sortie :** Liste restreinte de cours correspondant aux sigles fournis.

---

- **GET `/courses?ids=IFT1015,IFT1025&include_schedule=true`**  
  Wrapper permettant d’inclure les horaires des cours.

  **Exemple d’entrée :**  
  `http://localhost:7070/courses?ids=IFT1015,IFT1025&include_schedule=true`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/courses?courses_sigle=ift1015,ift1025&include_schedule=true`

  **Sortie :** Cours avec informations d’horaires et de sections.

---

- **GET `/courses?ids=IFT2255&include_schedule=true&schedule_semester=A25`**  
  Wrapper permettant de limiter les horaires à un trimestre précis.

  **Exemple d’entrée :**  
  `http://localhost:7070/courses?ids=IFT2255&include_schedule=true&schedule_semester=A25`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/courses?courses_sigle=ift2255&include_schedule=true&schedule_semester=a25`

  **Sortie :** Horaires du cours pour le trimestre spécifié uniquement.

---

- **GET `/courses/search?name=logiciel`**  
  Wrapper pour la recherche de cours par nom.

  **Exemple d’entrée :**  
  `http://localhost:7070/courses/search?name=logiciel`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/courses?name=logiciel`

  **Sortie :** Liste de cours dont le nom correspond au terme recherché.

---

- **GET `/courses/search?description=java`**  
  Wrapper pour la recherche de cours par description.

  **Exemple d’entrée :**  
  `http://localhost:7070/courses/search?description=java`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/courses?description=java`

  **Sortie :** Liste de cours dont la description correspond au terme recherché.

---

- **GET `/courses/{id}`**  
  Wrapper pour récupérer les informations détaillées d’un cours donné.

  **Entrée :** `id` (sigle du cours, ex. IFT2255)  
  **Exemple d’entrée :**  
  `http://localhost:7070/courses/IFT2255`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/courses/ift2255`

  **Sortie :** Détails complets du cours (description, crédits, prérequis, équivalents, etc.).

---

- **GET `/courses/{id}?include_schedule=true&schedule_semester=A25`**  
  Wrapper pour récupérer un cours avec ses horaires limités à un trimestre.

  **Exemple d’entrée :**  
  `http://localhost:7070/courses/IFT2255?include_schedule=true&schedule_semester=A25`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/courses/ift2255?include_schedule=true&schedule_semester=a25`

---

- **GET `/programs/{programId}`**  
  Wrapper permettant de récupérer la liste des cours associés à un programme.

  **Exemple d’entrée :**  
  `http://localhost:7070/programs/117510`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/programs?programs_list=117510`

  **Sortie :** Liste des cours offerts dans le programme.

---

- **GET `/programs/{programId}?include_courses_detail=true&response_level=full`**  
  Wrapper permettant d’obtenir les détails complets des cours d’un programme.

  **Exemple d’entrée :**  
  `http://localhost:7070/programs/117510?include_courses_detail=true&response_level=full`

  **Correspondance Planifium :**  
  `https://planifium-api.onrender.com/api/v1/programs?programs_list=117510&include_courses_detail=true&response_level=full`

  **Sortie :** Informations complètes des cours (pré-requis, équivalents, concomitants).

---

##### Avis étudiants

- **GET `/api/avis`**  
  Récupère l’ensemble des avis étudiants stockés localement.

  **Exemple d’entrée :**  
  `http://localhost:7070/api/avis`

---

- **GET `/api/avis/{course}`**  
  Récupère les avis associés à un cours précis.

  **Exemple d’entrée :**  
  `http://localhost:7070/api/avis/IFT2255`

---

- **POST `/api/avis`**  
  Ajoute un nouvel avis étudiant.

  **Exemple d’entrée :**  
  `http://localhost:7070/api/avis`

---

##### Résultats académiques agrégés

- **GET `/results/{sigle}`**  
  Récupère les résultats académiques agrégés pour un cours donné.

  **Exemple d’entrée :**  
  `http://localhost:7070/results/IFT2255`

---