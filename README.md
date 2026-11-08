# DocTrace

**Plateforme de distribution sécurisée et de traçabilité des documents confidentiels**

Projet académique de **sécurité applicative** réalisé à **SUP’COM (École Supérieure des Communications de Tunis)** dans le cadre du cycle d’ingénieur ICT, option **CySeD**.

## Présentation du projet

DocTrace vise à sécuriser la publication et la distribution de documents confidentiels au sein d'une organisation. La plateforme associe le **contrôle d'accès** à un **marquage individualisé des copies** et à la **traçabilité des distributions**, afin de faciliter l'analyse d'un document retrouvé hors de son contexte autorisé.

Le système s'appuie sur trois techniques complémentaires :

- **Watermarking** : ajout de marques visibles et discrètes permettant de différencier les copies distribuées.
- **Stéganographie** : insertion d'un identifiant de distribution chiffré et authentifié dans les images compatibles.
- **Fingerprinting** : calcul d'empreintes **SHA-256** pour contrôler l'intégrité des fichiers et rechercher des correspondances exactes.

L'identification d'une copie distribuée ne constitue pas, à elle seule, une preuve de l'identité de la personne à l'origine d'une fuite.

## Fonctionnalités principales

- Authentification et gestion des habilitations selon quatre rôles : `ADMIN`, `PUBLISHER`, `READER` et `AUDITOR`.
- Publication de documents, gestion des versions et contrôle des accès.
- Génération de copies marquées pour chaque distribution autorisée.
- Enregistrement des identifiants, empreintes et événements liés aux distributions.
- Analyse de fichiers suspects et production de rapports d'investigation.
- Protection des documents et des échanges, journalisation et contrôles de sécurité applicative.

## Architecture technique envisagée

| Composant | Technologies |
| --- | --- |
| Interface web | React, TypeScript, SPA/PWA |
| Middleware | Java 25, Jakarta EE 11, WildFly 42 |
| Authentification | Keycloak, OpenID Connect (OIDC) |
| Base de données | PostgreSQL |
| Traitement documentaire | Apache PDFBox, Java ImageIO |
| Protection des fichiers | ClamAV, AES-GCM, SHA-256 |

## Équipe — Trinôme 7

Nous sommes trois élèves ingénieurs de **SUP’COM**, option **CySeD** :

- **Achref Mansouri**
- **Ayed Mohamed Amin**
- **Ben Ameur Ahmed**

## Documentation

Les documents de conception et d'analyse de sécurité sont disponibles dans le dossier [`doc/`](./doc/) :

- **Cahier des charges (CDC)** : contexte, besoins fonctionnels et architecture envisagée.
- **Recueil des exigences de sécurité (SRE)** : objectifs de protection, scénarios d'abus, exigences et critères de vérification.

## État du projet

**Conception et spécification.** Les fonctionnalités et contrôles présentés constituent le périmètre prévu et seront développés et vérifiés progressivement.

---

*DocTrace — Sécuriser l'accès, marquer chaque copie, préserver la traçabilité.*
