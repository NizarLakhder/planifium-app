---
title: Conception - Diagrammes UML
---

# Diagrammes UML

## Diagrammes de classes

- Modèle de données orienté objet
![Diagramme de classe](../../besoins/diagrammes/diagrammedeclasse.jpg)


⸻

CourseController – CourseService

Cardinalité : 1 ↔ 1
Chaque CourseController utilise un seul CourseService pour exécuter les opérations sur les cours. Le service est injecté comme dépendance dans le contrôleur et contient toute la logique métier.

⸻

CourseService – Cours

Cardinalité : 1 ↔ 0..*
Un CourseService gère plusieurs objets Cours, par exemple lorsqu’il recherche ou compare des cours. Chaque cours appartient logiquement au service qui le manipule.

⸻

AvisController – AvisService

Cardinalité : 1 ↔ 1
Le AvisController interagit avec un unique AvisService. Ce service contient les méthodes permettant d’ajouter ou de récupérer des avis depuis un fichier JSON.

⸻

AvisService – Avis

Cardinalité : 1 ↔ 0..*
Un AvisService gère une collection d’avis (lecture et écriture). Chaque Avis est créé ou lu par ce service.

⸻

UserController – UserService

Cardinalité : 1 ↔ 1
Chaque UserController possède une seule instance de UserService. Ce dernier regroupe toutes les opérations liées aux utilisateurs (création, mise à jour, suppression).

⸻

UserService – User

Cardinalité : 1 ↔ 0..*
Le UserService maintient une liste d’utilisateurs dans une structure de type Map. Chaque User est manipulé via ce service unique.

⸻

IService – (Services)

Relation : implémentation (héritage)
Les classes CourseService, AvisService et UserService implémentent l’interface générique IService<T> afin d’assurer une structure commune aux opérations CRUD.



## Diagrammes de séquence

![Diagramme de sequence](../../besoins/diagrammes/diagrammedesequence1.jpg)
L’étudiant demande la recherche d’un cours par sigle.
Le contrôleur transmet la requête au service, qui interroge l’API Planifium, transforme les données reçues, puis renvoie la liste des cours trouvés.
Le contrôleur affiche soit les résultats, soit un message d’erreur si aucun cours n’est disponible.

![Diagramme de sequence](../../besoins/diagrammes/diagrammedesequence2.jpg)
L’étudiant envoie une liste de sigles à comparer.
Le contrôleur appelle le service, qui récupère les informations de chaque cours via l’API Planifium.
Une fois les données réunies, le contrôleur affiche le tableau comparatif, ou un message d’erreur si la liste contient moins de deux cours.

![Diagramme de sequence](../../besoins/diagrammes/diagrammedesequence3.jpg)
L’étudiant soumet un avis.
Le contrôleur envoie l’avis au service, qui le valide.
Si l’avis est valide, il est enregistré et envoyé au bot Discord, puis un message de confirmation est affiché.
Si l’avis est invalide, un message d’erreur est affiché à l’étudiant.