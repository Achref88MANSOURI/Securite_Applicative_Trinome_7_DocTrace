# DocTrace — Journal technique, environnement installé et passation

**Date de l'état des lieux :** 24 novembre 2026  
**Projet :** DocTrace — application de traçabilité, d'émission et de vérification documentaire  
**Dépôt GitHub :** <https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace>  
**Branche de référence :** `main`  
**Objectif de ce fichier :** permettre à un autre membre de l'équipe de reprendre le développement sans confondre ce qui a été implémenté, ce qui a été installé sur le PC et ce qui reste à faire.

> **Périmètre de vérification.** Les versions des outils locaux et les tests WildFly proviennent des sorties PowerShell partagées pendant la réalisation. Les fichiers `pom.xml`, `frontend/package.json`, `frontend/package-lock.json`, `frontend/vite.config.ts`, les classes REST et `.github/workflows/ci.yml` ont également été vérifiés dans le dépôt GitHub sur `main` le 24/11/2026. Ce document n'est pas un inventaire automatique du PC et ne prétend pas que les services non utilisés ont été installés.

---

## 1. Position du travail dans la mission initiale

| Partie de la mission | État au 24/11/2026 | Réalisations / limites |
|---|---|---|
| **1. Préparation du projet local** | **Socle technique réalisé** | Maven multi-modules, Java 25, Jakarta EE 11, React/TypeScript, PWA, Git/GitHub ; modules métier encore largement vides. |
| **2. Pipeline CI — GitHub Actions** | **CI initiale opérationnelle, sécurité CI incomplète** | Backend `mvn clean verify`, frontend `npm ci`, ESLint, build PWA. Deux exécutions GitHub Actions visibles ont réussi ; Gitleaks, test d'architecture REST et vrais tests unitaires manquent. |
| **3. Première implémentation C-I-D** | **Non développée** | `GET /api/health` fonctionne, mais aucun endpoint d'émission ni de vérification documentaire. Aucun cycle de bout en bout émission → vérification. |
| **4. Sécurisation selon les deux triptyques** | **Non implémentée à ce stade** | Exigences documentées, mais pas de TLS 1.3 en production, chiffrement AES-GCM, signatures Ed25519/JCS, watermarking DCT/QIM, OIDC/PKCE, RBAC/anti-IDOR, traitement documentaire isolé, etc. |
| **Intégration locale WildFly** | **Testée avec succès** | WildFly 42 sert la page React sur `/` et l'API sur `/api`, tous deux en HTTP sur `localhost:8080`. Le déploiement est encore manuel et local. |

**Distinction importante :** « infrastructure de développement préparée » ne signifie ni « produit fini » ni « sécurisé ». Le rendu GitHub est un **socle de démarrage**.

---

## 2. Outils installés et versions confirmées sur le PC Windows

### 2.1 Logiciels système et serveur

| Outil installé / utilisé | Version confirmée | Emplacement / information confirmée | Vérification |
|---|---|---|---|
| **Windows** | Windows 11, x64 | Poste local de développement | `mvn -version` |
| **PowerShell** | Windows PowerShell 5.1 | Terminal de travail | Bannière PowerShell |
| **Eclipse Temurin OpenJDK (JDK)** | **25.0.5.1+2 LTS** | `C:\Program Files\Eclipse Adoptium\jdk-25.0.5.10002-hotspot` | `java -version`, `javac -version` |
| **Apache Maven** | **3.10.2** | `C:\Tools\apache-maven-3.10.2` | `mvn -version` |
| **Node.js** | **26.13.0** | Installation Windows, chemin non relevé | `node -v` |
| **npm** | **11.20.0** | Via Node.js | `npm.cmd -v` |
| **Git pour Windows** | **2.56.0.windows.4** | Chemin non relevé | `git --version` |
| **WildFly** | **42.0.0.Final** | `C:\Tools\wildfly-42.0.0.Final` | Log `WFLYSRV0025` au démarrage |
| **WildFly Core** | **34.0.0.Final** | Inclus dans WildFly | Log au démarrage |

**Installation / configuration réellement effectuée :**

1. Installation de Temurin JDK 25 et configuration permanente de `JAVA_HOME`.
2. Extraction de Maven dans `C:\Tools`, ajout de `C:\Tools\apache-maven-3.10.2\bin` au `Path` utilisateur.
3. Vérification dans un **nouveau** terminal : `java -version` et `mvn -version` fonctionnent, Maven utilise bien Java 25.
4. Node.js/npm et Git étaient déjà disponibles et ont été vérifiés.
5. Téléchargement et extraction de WildFly 42 dans `C:\Tools` ; premier démarrage local réussi.
6. Sous PowerShell, `npm.cmd` a été utilisé à la place de `npm` pour contourner le blocage de `npm.ps1` par la stratégie d'exécution. **Aucune désactivation globale de la stratégie PowerShell n'a été nécessaire.**

Commandes de contrôle pour le prochain développeur :

```powershell
java -version
javac -version
mvn -version
node -v
npm.cmd -v
git --version
```

**À ne pas présenter comme installé :** Nginx, Keycloak, PostgreSQL, ClamAV, Gitleaks, serveur Ubuntu, gestionnaire de secrets, service de signatures et moteur de watermarking n'ont pas fait l'objet d'une installation ni d'une validation dans cette session.

### 2.2 Versions des outils utilisés pour initialiser React

| Composant | Version observée | Précision |
|---|---|---|
| `create-vite` | **9.3.4** | Générateur utilisé lors de la création du projet. |
| `vite` | **8.3.8** | Version réellement affichée par le serveur et le build. |
| `vite-plugin-pwa` | **2.0.0** | Version confirmée avec `npm.cmd ls vite-plugin-pwa` et le build PWA. |

Commandes exécutées pendant l'initialisation :

```powershell
# Depuis la racine du dépôt
npm.cmd create vite@latest frontend -- --template react-ts
# Choix du linter dans l'assistant : ESLint

cd frontend
npm.cmd install -D vite-plugin-pwa
npm.cmd ls vite-plugin-pwa
npm.cmd run build
```

Le frontend a été démarré localement avec Vite sur `http://localhost:5173/` et sa page de démarrage s'est affichée.

---

## 3. Liste précise des paquets npm du frontend

Les versions ci-dessous sont les **versions verrouillées** et directement déclarées dans `frontend/package-lock.json` présent sur `main` au 24/11/2026. Elles sont plus précises que les plages (`^` ou `~`) figurant dans `package.json`.

### 3.1 Dépendances d'exécution (`dependencies`)

| Package npm | Version verrouillée | Rôle |
|---|---|---|
| `react` | **19.3.1** | Bibliothèque d'interface utilisateur. |
| `react-dom` | **19.3.1** | Rendu de React dans le navigateur. |

### 3.2 Dépendances de développement (`devDependencies`)

| Package npm | Version verrouillée | Rôle |
|---|---|---|
| `@eslint/js` | **10.0.1** | Règles ESLint JavaScript de base. |
| `@types/node` | **24.19.2** | Types TypeScript pour les API Node utilisées par la configuration. |
| `@types/react` | **19.3.0** | Types TypeScript pour React. |
| `@types/react-dom` | **19.3.0** | Types TypeScript pour React DOM. |
| `@vitejs/plugin-react` | **6.3.0** | Intégration React à Vite. |
| `eslint` | **10.15.0** | Analyse statique/lint du code. |
| `eslint-plugin-react-hooks` | **7.1.1** | Règles pour les hooks React. |
| `eslint-plugin-react-refresh` | **0.5.8** | Règles associées au React Fast Refresh. |
| `globals` | **17.14.0** | Définitions de variables globales pour ESLint. |
| `typescript` | **6.0.3** | Compilateur et typage statique TypeScript. |
| `typescript-eslint` | **8.73.0** | Support TypeScript dans ESLint. |
| `vite` | **8.3.8** | Serveur de développement et bundler. |
| `vite-plugin-pwa` | **2.0.0** | Génération du manifeste PWA et du service worker (Workbox). |

Les nombreuses **dépendances transitives** ne sont pas toutes reproduites ici : leurs versions exactes et leurs empreintes d'intégrité sont conservées dans **`frontend/package-lock.json`** (format lockfile v3). La commande à privilégier pour les reproduire est **`npm ci`**, pas une réinstallation manuelle paquet par paquet.

**Scripts npm enregistrés dans `frontend/package.json` :**

```json
{
  "dev": "vite",
  "build": "tsc -b && vite build",
  "lint": "eslint .",
  "preview": "vite preview"
}
```

À noter : **aucune bibliothèque de composants UI métier, client OIDC/Keycloak, routeur React, SDK d'upload, outil de test frontend ou bibliothèque de graphiques n'a encore été ajoutée explicitement**.

---

## 4. Dépendances Maven / Java déclarées

### 4.1 Structure de l'application

**Projet parent :**

- `groupId` : **`com.doctrace`** (remplace l'ancien `tn.supcom.doctrace`).
- `artifactId` : **`doctrace-parent`**.
- `version` : **`0.1.0-SNAPSHOT`**.
- `packaging` : `pom`.
- Cible Java : `<maven.compiler.release>25</maven.compiler.release>`.

| Module | Packaging | Dépendances internes déclarées | Usage prévu |
|---|---|---|---|
| `doctrace-core` | `jar` | Aucune | Modèles et logique métier. |
| `doctrace-security` | `jar` | `doctrace-core` | Services cryptographiques et contrôles de sécurité futurs. |
| `doctrace-document` | `jar` | `doctrace-core` | Traitement de documents futurs. |
| `doctrace-api` | `war` | `doctrace-core`, `doctrace-security` | Points d'entrée Jakarta REST. |

Le module documentaire **n'est pas encore déclaré comme dépendance du module API** ; son raccordement se fera lorsqu'une fonctionnalité en aura besoin.

### 4.2 Dépendances et plugins externes

| Élément Maven | Version observée / déclarée | Portée | État |
|---|---|---|---|
| `jakarta.platform:jakarta.jakartaee-api` | **11.0.0** | `provided` dans `doctrace-api` | **Déclaré** ; les API sont fournies par WildFly à l'exécution. |
| `org.apache.maven.plugins:maven-war-plugin` | **3.5.1** | Plugin de construction du WAR | **Version fixée** dans `doctrace-api/pom.xml`. |
| `maven-resources-plugin` | **3.5.0** | Build | Version **observée dans les logs Maven**, pas explicitement fixée dans les POM créés. |
| `maven-compiler-plugin` | **3.16.0** | Build | Version observée dans les logs. |
| `maven-surefire-plugin` | **3.6.1** | Tests | Version observée ; aucun test n'avait encore été exécuté (`No tests to run`). |
| `maven-jar-plugin` | **3.5.1** | Build JAR | Version observée dans les logs. |

**Pas encore ajoutés dans les POM :** Apache PDFBox, bibliothèques de stéganographie/watermarking, bibliothèques de cryptographie dédiées, clients Keycloak/OIDC, JUnit 5, ArchUnit ou autres frameworks de tests. Les algorithmes annoncés dans le dossier de conception ne sont pas des dépendances déjà installées.

**Résultats Maven obtenus sur le PC :**

```powershell
mvn -B -ntp validate
mvn -B -ntp clean package
```

Les cinq projets Maven (parent inclus) ont affiché `SUCCESS` lors de la compilation/du packaging final. Les JAR des modules sans sources étaient vides à ce stade, ce qui est normal pour cette initialisation. La compilation n'est **pas** une preuve que les traitements documentaires sont implémentés.

**Artefacts attendus :**

```text
doctrace-core/target/doctrace-core-0.1.0-SNAPSHOT.jar
doctrace-security/target/doctrace-security-0.1.0-SNAPSHOT.jar
doctrace-document/target/doctrace-document-0.1.0-SNAPSHOT.jar
doctrace-api/target/doctrace-api-0.1.0-SNAPSHOT.war
```

---

## 5. Arborescence et fichiers effectivement créés

```text
Securite_Applicative_Trinome_7_DocTrace/
├── .github/
│   └── workflows/
│       └── ci.yml
├── .gitignore
├── pom.xml
├── doctrace-core/
│   └── pom.xml
├── doctrace-security/
│   └── pom.xml
├── doctrace-document/
│   └── pom.xml
├── doctrace-api/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/doctrace/api/
│       │   ├── DocTraceApplication.java
│       │   └── HealthResource.java
│       └── webapp/WEB-INF/jboss-web.xml
├── frontend/
│   ├── package.json
│   ├── package-lock.json
│   ├── vite.config.ts
│   ├── eslint.config.js
│   ├── src/                   # Encore essentiellement le template Vite
│   ├── public/
│   └── index.html
├── docs/                      # Documents de conception déjà présents
├── README.md
└── ATTRIBUTIONS.md
```

**État GitHub confirmé au 24/11/2026 :** cette structure et le workflow `ci.yml` sont présents sur **`main`**. Le dépôt possède également la branche `feat/bootstrap-doctrace` utilisée durant le développement.

**Ne pas confondre avec la structure locale non publiée :** un dossier `deploy/` a été créé manuellement, avec `wildfly/original`, `wildfly/cli`, `wildfly/config` et `scripts`. **Il n'apparaît pas sur `main` à la date de vérification**, car il était encore vide et/ou n'avait pas été ajouté à Git. Il ne constitue pas un outil de déploiement disponible pour le binôme.

---

## 6. Code backend implémenté et routage

### 6.1 Initialisation Jakarta REST

Fichier : `doctrace-api/src/main/java/com/doctrace/api/DocTraceApplication.java`

```java
package com.doctrace.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class DocTraceApplication extends Application {
}
```

Le chemin Jakarta REST est **`/`**, et non `/api` : le préfixe `/api` est déjà attribué au **contexte WAR**.

### 6.2 Endpoint de disponibilité

Fichier : `doctrace-api/src/main/java/com/doctrace/api/HealthResource.java`

- Classe annotée `@Path("/health")`.
- Méthode `GET` annotée `@GET`, `@PermitAll`, `@Produces(MediaType.APPLICATION_JSON)`.
- Réponse retournée : **`{"status":"UP"}`**.
- Pas de logique métier, d'accès à une base de données, de contrôle de préparation ni de contrôle d'identité.

**Attention sécurité :** `@PermitAll` explicite l'intention d'accès au endpoint dans le code, mais ne démontre pas une politique complète d'authentification/autorisation opérationnelle.

### 6.3 Contexte du WAR

Fichier : `doctrace-api/src/main/webapp/WEB-INF/jboss-web.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jboss-web>
    <context-root>/api</context-root>
</jboss-web>
```

Le fichier a été **vérifié à l'intérieur du WAR** :

```powershell
jar tf .\doctrace-api\target\doctrace-api-0.1.0-SNAPSHOT.war |
    Select-String "jboss-web.xml"
# Sortie obtenue : WEB-INF/jboss-web.xml
```

**Contrat de routage retenu :** frontend `/`, API `/api/...` sur la même origine. Ne pas configurer à la fois le contexte WAR `/api` et `@ApplicationPath("/api")`, faute de quoi l'URL risque d'être doublée.

---

## 7. Frontend React et configuration PWA

Fichier : `frontend/vite.config.ts`.

**Ce qui est actuellement configuré :**

- Plugins : `@vitejs/plugin-react` et `vite-plugin-pwa`.
- Nom du manifeste : `DocTrace` ; mode d'affichage `standalone` ; `start_url` et `scope` définis à `.`.
- `registerType: 'prompt'` et `injectRegister: 'auto'`.
- Workbox : précache `**/*.{js,css,html}` ; aucun cache runtime supplémentaire (`runtimeCaching: []`) ; exclusion des chemins `/api` du fallback de navigation ; suppression des anciens caches.

**Build réellement validé :**

```powershell
cd frontend
npm.cmd run build
```

Sortie obtenue : `vite v8.3.8`, build réussi, `PWA v2.0.0` et génération de :

```text
frontend/dist/index.html
frontend/dist/manifest.webmanifest
frontend/dist/registerSW.js
frontend/dist/sw.js
frontend/dist/workbox-2fbc6a65.js    # nom hashé observé, peut varier
frontend/dist/assets/...             # noms hashés variables
```

Le build annonçait **5 entrées précachées**.

**Limites non encore validées :** installation réelle de la PWA, icônes PWA complètes, notification d'update, comportement hors-ligne, tests de cache des données privées et des erreurs API. `runtimeCaching: []` est un paramètre utile, **pas** une preuve suffisante de confidentialité.

---

## 8. Déploiement WildFly 42 effectué localement

### 8.1 Dossiers locaux Windows

```text
C:\Tools\wildfly-42.0.0.Final\
    bin\standalone.bat
    bin\jboss-cli.bat
    standalone\configuration\...

C:\Tools\doctrace-deploy\frontend\
    index.html
    manifest.webmanifest
    sw.js
    assets\...
```

Le dossier `C:\Tools\doctrace-deploy\frontend` a reçu une **copie de `frontend/dist/*`**. Ce dossier **n'est pas** un répertoire versionné Git.

### 8.2 Démarrage et déploiement API

```powershell
# Terminal 1 : démarrage du serveur (laisser ce terminal ouvert)
& "C:\Tools\wildfly-42.0.0.Final\bin\standalone.bat" -b 127.0.0.1

# Terminal 2 : depuis la racine du dépôt, WAR préalablement construit
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect `
  --command="deploy doctrace-api\target\doctrace-api-0.1.0-SNAPSHOT.war"
```

WildFly a annoncé le succès de son démarrage :

```text
WFLYSRV0025: WildFly 42.0.0.Final (WildFly Core 34.0.0.Final)
started ... Server configuration file in use: standalone.xml
```

Port applicatif local : **8080 (HTTP)**. Interface de gestion observée : **127.0.0.1:9990 (HTTP)**. Ce n'est **pas** une configuration de production exposable à Internet.

### 8.3 Gestionnaire statique Undertow

Le serveur par défaut utilisait initialement `welcome-content`, pointant vers `${jboss.home.dir}/welcome-content`. Ce gestionnaire **n'a pas été modifié directement** ; un nouveau gestionnaire a été créé :

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect `
  --command="/subsystem=undertow/configuration=handler/file=doctrace-frontend:add(path=C:/Tools/doctrace-deploy/frontend,directory-listing=false,follow-symlink=false)"
```

La lecture de la configuration a confirmé :

```text
"path" => "C:/Tools/doctrace-deploy/frontend"
"directory-listing" => false
"follow-symlink" => false
```

Le chemin `/` de l'hôte Undertow par défaut a ensuite été associé au nouveau gestionnaire :

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect `
  --command="/subsystem=undertow/server=default-server/host=default-host/location=\/:write-attribute(name=handler,value=doctrace-frontend)"
```

WildFly a demandé un rechargement (`reload-required`). Celui-ci a été exécuté :

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect --command=":reload"
```

Les deux URLs ont été vérifiées à nouveau après ce rechargement.

### 8.4 Tests HTTP réellement exécutés

| Test local | Résultat constaté |
|---|---|
| `http://localhost:8080/` | Page de démarrage React/Vite affichée depuis WildFly. |
| `http://localhost:8080/api/health` | Corps : `{"status":"UP"}`. |
| `http://localhost:8080/api/route-inexistante` | `404`. |
| `http://localhost:8080/dashboard` | `404` (normal : aucun fallback React configuré). |
| Après `:reload` | Frontend et API encore accessibles. |

Commandes utilisées pour contrôler les erreurs :

```powershell
curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/api/route-inexistante
curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/dashboard
```

**Travail non encore fait :** routage profond de la SPA, stratégie de fallback sûre, HTTPS/TLS 1.3, proxy Nginx, gestion des secrets, rollback applicatif, déploiement automatisé DEV/PROD.

### 8.5 Snapshot de la configuration serveur

Commande exécutée :

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect --command=":take-snapshot"
```

Emplacement retourné par WildFly :

```text
C:\Tools\wildfly-42.0.0.Final\standalone\configuration\standalone_xml_history\snapshot\20261123-192608964standalone.xml
```

Ce snapshot est une **sauvegarde de configuration**, pas une sauvegarde des fichiers React ni du WAR.

### 8.6 Scripts du professeur

Archive fournie : **`Atelier_Sécurité_Applicative_-_Semaine_4(1).zip`**.

Elle contient exactement :

```text
add-api.cli
default-host.cli
add-host.cli
```

Ces scripts ont été reçus et étudiés comme **référence**. Ils n'ont **pas** été appliqués tels quels à l'instance qui fonctionne actuellement. Les adapter nécessite notamment de préserver `/api` lors du fallback SPA, d'éviter de masquer les 404 d'API, de maîtriser les en-têtes HTTP et de rendre les commandes idempotentes.

Leur copie dans `deploy/wildfly/original` avait été demandée mais **n'a pas été confirmée** dans la conversation ; le dossier `deploy/` n'est pas présent sur `main` au moment de la vérification.

---

## 9. Git, branches et pipeline GitHub Actions

### 9.1 Travail Git effectué

- Création d'une branche `feat/bootstrap-doctrace` pour préparer le socle sans toucher d'emblée à `main`.
- Ajout d'un `.gitignore` protégeant les dossiers `**/target/`, `**/node_modules/`, `**/dist/`, les `.env` et des fichiers IDE.
- Premier commit et publication de `feat/bootstrap-doctrace` réussis.
- Au **24/11/2026**, les fichiers de démarrage et la CI sont **également présents sur `main`** : l'intégration sur la branche principale est donc vérifiable dans GitHub.
- Commit relevé sur le dernier workflow GitHub Actions : **`7bdff7fcef3c87e7d440d13e00310c1f095c0ab2`**, message `feat: configure WildFly deployment and DocTrace CI foundation`.

### 9.2 Workflow existant `.github/workflows/ci.yml`

**Déclenchement :** `push` sur les branches et `pull_request` ciblant `main`.

**Job `backend` :**

```text
ubuntu-latest
→ actions/checkout@v7
→ actions/setup-java@v6 (Temurin Java 25, cache Maven)
→ mvn -B -ntp clean verify
```

**Job `frontend` :**

```text
ubuntu-latest
→ actions/checkout@v7
→ actions/setup-node@v7 (Node.js 26, cache npm)
→ npm ci
→ npm run lint
→ npm run build
```

**État réellement vérifié sur GitHub :** deux exécutions du workflow `DocTrace CI` ont été trouvées, l'une sur `feat/bootstrap-doctrace`, l'autre sur `main`, toutes deux avec **`conclusion: success`**. La dernière :

<https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/actions/runs/45464412019>

**Limites :**

- Un `mvn verify` réussi ne garantit pas que des tests unitaires existent. Au moment du packaging local, Maven indiquait `No tests to run`.
- **Gitleaks absent** du workflow actuel.
- **Test d'architecture REST absent** : vérifier automatiquement que chaque endpoint possède `@RolesAllowed`, `@DenyAll` ou `@PermitAll`.
- Pas de pipeline de déploiement DEV/PROD ni de test d'intégration sur WildFly dans GitHub Actions.
- Pas de vérification automatique du cycle documentaire émission → vérification.

---

## 10. Reprendre le projet sur le PC d'un autre membre

### 10.1 Prérequis

Pour reproduire au plus près l'environnement observé, prévoir :

- Windows ou Linux avec **JDK 25** ;
- **Apache Maven 3.10.2** ou version compatible, à valider avec le build du dépôt ;
- **Node.js 26** et npm compatible ;
- Git ;
- **WildFly 42.0.0.Final** pour tester l'intégration serveur.

La parité DEV/PROD doit être recherchée, mais le **chemin Windows de `C:\Tools` n'est pas portable** vers une VM Ubuntu. Il faudra le remplacer par des chemins configurables.

### 10.2 Récupérer et compiler le dépôt

```powershell
git clone https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace.git
cd .\Securite_Applicative_Trinome_7_DocTrace

git switch main
git pull --ff-only origin main

# Backend Maven
mvn -B -ntp clean verify

# Frontend : versions verrouillées par package-lock.json
cd .\frontend
npm.cmd ci
npm.cmd run lint
npm.cmd run build
```

Sur Linux, utiliser `npm` à la place de `npm.cmd`.

### 10.3 Comprendre les limites du clonage

**Le dépôt seul ne suffit pas encore à reproduire automatiquement l'environnement WildFly local.** Il manque actuellement les fichiers de déploiement versionnés. Un collègue doit :

1. installer WildFly 42 et démarrer un serveur de développement isolé ;
2. construire le WAR ;
3. déployer le WAR sur WildFly ;
4. construire le frontend et en copier les fichiers statiques vers un répertoire choisi ;
5. créer le gestionnaire Undertow et le routage correspondant ;
6. valider `/` et `/api/health` ainsi que les erreurs 404.

**Ne pas copier aveuglément `standalone.xml` depuis un autre PC** : il peut contenir des chemins, ports et paramètres dépendants de la machine. Préférer des scripts CLI paramétrables, revus et testés.

---

## 11. Travail restant, classé selon les quatre blocs de la mission

### Bloc 1 — Préparation du projet : socle déjà en place

- Remplacer l'interface de démonstration Vite par une structure d'interface DocTrace.
- Fixer le contrat front/API en développement et production.
- Ajouter tests minimum de démarrage et documentation de reproduction.

### Bloc 2 — CI obligatoire : priorité immédiate

1. Ajouter **Gitleaks** au pipeline (détection de secrets et échec de la CI en cas d'anomalie).
2. Ajouter les **tests unitaires** et leur dépendance de test (par exemple JUnit, à choisir et configurer explicitement).
3. Implémenter le **test d'architecture des endpoints REST** ; aucune méthode exposée sans annotation de politique `@RolesAllowed`, `@DenyAll` ou `@PermitAll`.
4. Vérifier que chaque contrôle obligatoire provoque un échec du workflow s'il échoue.
5. Ajouter un test minimum de déploiement WildFly pour réduire les régressions d'intégration.

### Bloc 3 — Première chaîne fonctionnelle C-I-D : priorité métier

1. Définir le contrat JSON/HTTP de l'**émission**.
2. Créer un endpoint d'émission minimal.
3. Créer un endpoint de **vérification**.
4. Relier émission → vérification sur un cas positif simple et reproductible.
5. Ajouter des cas négatifs, notamment document modifié, accès non autorisé et fichier hostile.

### Bloc 4 — Sécurité : à implémenter progressivement

- **Confidentialité :** TLS 1.3, AES-256-GCM, gestion sûre des clés et secrets.
- **Intégrité :** SHA-256, Ed25519, canonicalisation JCS, dissimulation DCT/QIM puis expérimentations LSB.
- **Disponibilité :** validation des fichiers, détection de fichiers malveillants, limites de tailles et dimensions, budgets de mémoire/CPU/durée, isolation du traitement documentaire.
- **Authentification :** OIDC Authorization Code avec PKCE, MFA/TOTP, sessions sûres.
- **Autorisations :** politiques d'accès explicites, RBAC, contrôle objet par objet (anti-IDOR), éventuellement ABAC.
- **Données privées :** minimisation, pseudonymisation selon le besoin, contrôle des logs et des caches PWA.

### Chantier transverse — Déploiement robuste et scripts du professeur

- Versionner les **scripts `.cli` originaux** et leurs adaptations DocTrace.
- Rendre les opérations répétables (configuration paramétrée DEV/PROD, vérification avant modification, gestion des ressources existantes).
- Garantir **une même origine** pour React/PWA (`/`) et l'API (`/api`).
- Traiter le fallback React sans transformer les erreurs API 404 en HTML et sans masquer des erreurs de ressources statiques.
- Prévoir HTTPS/TLS, supervision, gestion des secrets, tests de fumée (*smoke tests*) et possibilité de rollback.
- Tester sur un environnement propre, pas uniquement sur le WildFly modifié manuellement.

---

## 12. Synthèse destinée au binôme

**Ce qui peut être repris immédiatement :** le code source sur `main`, la structure multi-modules Java, le frontend React/PWA, les deux classes REST, la configuration du WAR et le premier pipeline GitHub Actions réussi.

**Ce qui fonctionne uniquement sur le PC de développement :** le serveur WildFly 42 configuré manuellement, le gestionnaire Undertow `doctrace-frontend`, la copie locale de `frontend/dist` dans `C:\Tools\doctrace-deploy\frontend`, et le snapshot de configuration.

**Ce qui n'existe pas encore dans le projet :** application documentaire opérationnelle, émission/vérification, authentification, sécurité applicative complète, Gitleaks, tests d'architecture REST et déploiement automatisé reproductible.

**La bonne suite :** finaliser la CI exigée, versionner le déploiement, puis créer une première fonctionnalité documentaire C-I-D testable de bout en bout, en introduisant les mécanismes de sécurité adaptés à chaque étape.

---

## 13. Références du dépôt vérifiées pour cette passation

- [Dépôt `main`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/tree/main)
- [Fichier parent `pom.xml`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/pom.xml)
- [`doctrace-api/pom.xml`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/doctrace-api/pom.xml)
- [`frontend/package.json`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/frontend/package.json)
- [`frontend/package-lock.json`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/frontend/package-lock.json)
- [`frontend/vite.config.ts`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/frontend/vite.config.ts)
- [Workflow `.github/workflows/ci.yml`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/.github/workflows/ci.yml)
- [Dernière exécution CI observée sur `main` — SUCCESS](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/actions/runs/45464412019)

*Ce journal décrit l'état réellement observé au 24 novembre 2026. Si le code évolue, mettre à jour les versions, résultats des tests et scripts de déploiement dans le dépôt.*
