# 📑 Fiche d'Attribution, d'Intégrité Académique et d'Usage de l'IA

**Projet : DocTrace — Plateforme sécurisée de distribution et de traçabilité des documents confidentiels**  
**Année universitaire : 2026–2027**

---

## 👥 1. Identification de l'Équipe & Rôles

- **Établissement :** École Supérieure des Communications de Tunis (SUP'COM)
- **Année universitaire :** 2026–2027
- **Groupe / Trinôme N° :** Trinôme 7
- **Nom de l'Application Métier :** DocTrace

### Membres de l'équipe

1. **AYED Mohamed Amin** — Email : `mohamedamin.ayed@supcom.tn` — **Rôle principal :** conception de l'architecture applicative (*Architecture Designer*).
2. **BEN AMEUR Ahmed** — Email : `ahmad.benameur@supcom.tn` — **Rôle principal :** ingénierie de la sécurité (*Security Engineer*).
3. **MANSOURI Achref** — Email : `achraf.mansour@supcom.tn` — **Rôle principal :** modélisation des menaces et analyse des cas d'abus (*Threat Modelling*).

---

## 📜 2. Déclaration d'Intégrité et d'Anti-Plagiat

Par la présente, nous certifions sur l'honneur que :

- L'architecture, la conception logicielle et l'implémentation métier de DocTrace sont réalisées dans le cadre du travail de notre équipe.
- Aucun code, diagramme ou document d'un autre groupe de l'atelier n'a été reproduit par copier-coller.
- Les sources externes employées, notamment les bibliothèques open source, les référentiels de sécurité et les documentations techniques Jakarta EE/WildFly, sont citées dans les documents du projet.
- Les propositions issues d'outils d'intelligence artificielle sont examinées à partir des besoins métier, des scénarios d'abus et des contraintes techniques de DocTrace avant leur intégration.

---

##  3. Déclaration de l'Usage des Intelligences Artificielles (IA)

Nous utilisons l'intelligence artificielle comme outil d'appui à l'analyse, à la compréhension de notions techniques, à l'examen d'alternatives et à la rédaction structurée. Les décisions retenues sont fondées sur le fonctionnement de DocTrace, les exigences du cours et l'analyse des compromis techniques.

### 📊 Tableau de transparence IA

| Composant / Phase | IA utilisée (Nom/Version) | Pourcentage estimé de contenu généré | Rôle précis de l'IA et consignes de travail |
| :--- | :--- | :---: | :--- |
| **Conception du fonctionnement métier et de l'architecture** | Claude 3.5 Sonnet | **20 %** | Assistance à la structuration des descriptions fonctionnelles et des diagrammes d'architecture à partir du périmètre métier défini pour DocTrace. |
| **Phase 1 — Exigences de sécurité (SRE)** | **ChatGPT Sol** | **45 %** | Appui à la compréhension des cas d'abus, recherche de contre-mesures adaptées, comparaison de mécanismes de sécurité, examen des conditions d'échec et des critères de vérification. Les échanges sont centrés sur les rôles, les ACL, la distribution de copies marquées, l'intégrité des preuves et la traçabilité. |
| **Phase 2 — Modélisation des menaces et analyse des risques** | Aucun déclaré pour la cotation des risques | **0 %** | Identification des scénarios et cotation des risques réalisées manuellement selon la matrice retenue dans le dossier de conception. |
| **Développement Backend Jakarta EE** | Non applicable à ce jalon | — | Développement non documenté dans la phase de conception actuelle. |
| **Moteur de dissimulation (watermarking / stéganographie)** | Non applicable à ce jalon | — | Implémentation non documentée dans la phase de conception actuelle. |

### 🛠️ Prompt clé — Analyse des cas d'abus et recherche des contre-mesures

> Pour DocTrace, nous avons défini les fonctions métier suivantes : publication de documents confidentiels, gestion des autorisations par rôles et ACL, génération de copies PDF marquées individuellement et investigation d'une copie suspecte. Nous analysons notamment MU-01 (accès à un document par manipulation d'identifiant), MU-04 (contournement du marquage) et MU-07 (falsification de marques et fausse attribution). Pour chaque cas d'abus, examine les conditions nécessaires à sa réalisation, identifie plusieurs contrôles de sécurité envisageables et compare leurs limites. Propose des critères de vérification concrets, en distinguant ce qui relève de l'autorisation côté serveur, de l'intégrité cryptographique et des indices de traçabilité. Signale les hypothèses techniques et les contradictions éventuelles avec nos règles métier afin de nous aider à choisir les exigences pertinentes.

### Autres consignes d'analyse SRE

**Contrôle d'accès documentaire (MU-01)**

> Dans notre application, ADMIN gère les comptes et les politiques sans obtenir automatiquement le droit de lire les documents ; PUBLISHER gère ses publications ; READER consulte uniquement les documents autorisés ; AUDITOR analyse les investigations qui lui sont attribuées. Étudie les risques de contournement d'autorisation lorsque l'identifiant d'un document est modifié dans une requête API. Compare les contrôles RBAC, ACL par document et niveaux d'habilitation, puis suggère des tests négatifs permettant de vérifier la décision d'accès côté middleware.



---

## ✍️ 4. Engagement et signatures des membres

En inscrivant leur nom dans le commit de validation, les membres de l'équipe s'engagent sur l'exactitude des informations figurant dans cette fiche et sur sa mise à jour à chaque jalon du projet.

- **AYED Mohamed Amin** : Engagé — 11/11/2026.
- **BEN AMEUR Ahmed** : Engagé — 11/11/2026.
- **MANSOURI Achref** : Engagé — 11/11/2026.
