---
title: Analyse des besoins - Risques
---

# Analyse des risques

## Identification des risques

La réussite du projet dépend de plusieurs facteurs techniques, humains et organisationnels.  
Cette section présente les principaux risques identifiés ainsi que les mesures prévues pour les atténuer.



### Risque 1 – Dépendance aux sources externes (Planifium, Discord, résultats académiques)

Le système dépend fortement de Planifium, des fichiers de résultats académiques et du bot Discord. Une panne, un changement d’API ou un format de fichier modifié peut bloquer des fonctionnalités essentielles.

**Probabilité :** Élevée  
**Impact :** Élevé  

**Mesures de mitigation :**
- Mettre en place un cache local mis à jour régulièrement.  
- Centraliser les appels aux APIs dans des services dédiés pour pouvoir adapter le code facilement.  
- Prévoir un mode dégradé utilisant les données du cache.  
- Afficher des messages d’erreur clairs et informatifs à l’utilisateur.  


---

### Risque 2 – Problèmes de performance et charge du serveur

Lors du choix de cours, de nombreux étudiants utilisent la plateforme simultanément. Le serveur peut ralentir ou devenir indisponible.

**Probabilité :** Moyenne  
**Impact :** Élevé  

**Mesures de mitigation :**
- Mettre en place de la mise en cache pour réduire les appels répétitifs à Planifium.  
- Réaliser des tests de charge (ex: JMeter).  
- Surveiller les métriques du système (CPU, RAM, latence).  
- Prévoir un hébergement scalable en cas d’augmentation de charge.  


---

### Risque 3 – Fiabilité, âge et pertinence des avis étudiants

Les avis Discord peuvent être obsolètes (cours modifié, nouveau professeur), incomplets ou biaisés, ce qui affecte la pertinence des synthèses affichées.

**Probabilité :** Moyenne  
**Impact :** Moyen  

**Mesures de mitigation :**
- Afficher un indicateur d’ancienneté des avis.  
- Ne garder que les avis des dernières années (ex: 2 ou 3 ans).  
- Afficher les avis seulement à partir d’un seuil minimal (n ≥ 5).  
- Permettre le filtrage par session/année.  
- Agréger les avis pour obtenir une tendance représentative.  


---

### Risque 4 – Incohérence entre données officielles et avis étudiants

Planifium, les résultats académiques et les avis Discord peuvent ne pas être synchronisés (sigles différents, cours modifiés, données anciennes).

**Probabilité :** Moyenne  
**Impact :** Moyen  

**Mesures de mitigation :**
- Normaliser automatiquement les sigles des cours lors du traitement.  
- Afficher la date de mise à jour de chaque source.  
- Détecter les incohérences et afficher un avertissement.  
- Synchroniser régulièrement les sources (quotidien ou hebdomadaire).  


---

### Risque 5 – Sécurité et confidentialité des données (Loi 25)

Le système manipule des données sensibles comme les préférences étudiantes ou les avis soumis via Discord. Une mauvaise gestion peut entraîner des violations légales.

**Probabilité :** Faible à moyenne  
**Impact :** Élevé  

**Mesures de mitigation :**
- Forcer l’utilisation de HTTPS.  
- Hacher les mots de passe et chiffrer les données sensibles.  
- Anonymiser immédiatement les avis Discord.  
- Respecter la Loi 25 et les directives institutionnelles.  


---

### Risque 6 – Absence prolongée d’un membre clé de l'équipe

L’indisponibilité d’un membre essentiel risque de ralentir le projet ou nuire à la cohérence.

**Probabilité :** Moyenne  
**Impact :** Élevé  

**Mesures de mitigation :**
- Documenter toutes les fonctionnalités développées.  
- Diviser clairement les responsabilités.  
- Utiliser GitHub Issues + branches pour suivre et partager les tâches.  
- Encourager le pair programming pour réduire la dépendance individuelle.  


---

### Risque 7 – Adoption limitée de la plateforme par les étudiants

Même si la plateforme est fonctionnelle, les étudiants pourraient ne pas l’utiliser si elle ne présente pas clairement ses avantages ou si elle semble complexe.

**Probabilité :** Moyenne  
**Impact :** Moyen  

**Mesures de mitigation :**
- Concevoir une interface simple, intuitive et adaptée aux téléphones.  
- Promouvoir l’outil auprès des associations étudiantes et dans les serveurs Discord du DIRO.  
- Ajouter un système de rétroaction utilisateur intégré.  
- Mettre en avant les bénéfices : comparaison, avis, charge estimée.  

## Modification du processus opérationnel

La mise en place du système pourrait modifier certaines pratiques actuelles :  
- **Consultation des cours** : auparavant dispersée entre plusieurs plateformes, elle sera centralisée.  
- **Collecte d’avis étudiants** : d’un processus informel (ex. discussions Discord) à un mécanisme structuré et intégré.  
- **Choix de cours** : passage d’un processus manuel et fastidieux à un processus guidé et assisté.
