# DocTrace — Technical Log, Installed Environment, and Handover

**Status Date:** November 24, 2026**Project:** DocTrace — Document traceability, issuance, and verification application**GitHub Repository:** [https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace)**Reference Branch:** `main`**Purpose of this file:** Enable another team member to resume development without confusing what has already been implemented, what is installed locally on the PC, and what remains to be done.

> **Verification Scope.** Local tool versions and WildFly tests come from PowerShell outputs shared during development. The files `pom.xml`, `frontend/package.json`, `frontend/package-lock.json`, `frontend/vite.config.ts`, the REST classes, and `.github/workflows/ci.yml` were also verified in the GitHub repository on `main` on 11/24/2026. This document is not an automated PC inventory and does not claim that unused services have been installed.

---

## 1. Work Status Within the Initial Mission

| Mission Component                              | Status as of 11/24/2026                                  | Achievements / Limitations                                                                                                                                                              |
| ---------------------------------------------- | -------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **1. Local Project Setup**               | **Technical foundation completed**                 | Multi-module Maven, Java 25, Jakarta EE 11, React/TypeScript, PWA, Git/GitHub; business modules are still largely empty.                                                                |
| **2. CI Pipeline — GitHub Actions**     | **Initial CI operational, security CI incomplete** | Backend `mvn clean verify`, frontend `npm ci`, ESLint, PWA build. Two visible GitHub Actions runs succeeded; Gitleaks, REST architecture tests, and real unit tests are missing.    |
| **3. First C-I-A Implementation**        | **Not developed**                                  | `GET /api/health` works, but there are no document issuance or verification endpoints. No end-to-end issuance → verification cycle exists yet.                                       |
| **4. Security According to Both Triads** | **Not implemented at this stage**                  | Requirements are documented, but no production TLS 1.3, AES-GCM encryption, Ed25519/JCS signatures, DCT/QIM watermarking, OIDC/PKCE, RBAC/anti-IDOR, isolated document processing, etc. |
| **Local WildFly Integration**            | **Successfully tested**                            | WildFly 42 serves the React page on `/` and the API on `/api`, both over HTTP on `localhost:8080`. Deployment is still manual and local.                                          |

**Important Distinction:** "Development infrastructure prepared" means neither "finished product" nor "secured." The GitHub submission is a **starter foundation**.

---

## 2. Installed Tools and Confirmed Versions on the Windows PC

### 2.1 System and Server Software

| Installed / Used Tool                   | Confirmed Version          | Location / Confirmed Info                                      | Verification                          |
| --------------------------------------- | -------------------------- | -------------------------------------------------------------- | ------------------------------------- |
| **Windows**                       | Windows 11, x64            | Local development workstation                                  | `mvn -version`                      |
| **PowerShell**                    | Windows PowerShell 5.1     | Working terminal                                               | PowerShell banner                     |
| **Eclipse Temurin OpenJDK (JDK)** | **25.0.5.1+2 LTS**   | `C:\Program Files\Eclipse Adoptium\jdk-25.0.5.10002-hotspot` | `java -version`, `javac -version` |
| **Apache Maven**                  | **3.10.2**           | `C:\Tools\apache-maven-3.10.2`                               | `mvn -version`                      |
| **Node.js**                       | **26.13.0**          | Windows installation, path not recorded                        | `node -v`                           |
| **npm**                           | **11.20.0**          | Via Node.js                                                    | `npm.cmd -v`                        |
| **Git for Windows**               | **2.56.0.windows.4** | Path not recorded                                              | `git --version`                     |
| **WildFly**                       | **42.0.0.Final**     | `C:\Tools\wildfly-42.0.0.Final`                              | `WFLYSRV0025` log at startup        |
| **WildFly Core**                  | **34.0.0.Final**     | Bundled with WildFly                                           | Startup log                           |

**Installation / Configuration Actually Performed:**

1. Installed Temurin JDK 25 and permanently configured `JAVA_HOME`.
2. Extracted Maven to `C:\Tools`, added `C:\Tools\apache-maven-3.10.2\bin` to the user `Path`.
3. Verified in a **new** terminal: `java -version` and `mvn -version` work, and Maven properly uses Java 25.
4. Node.js/npm and Git were already available and have been verified.
5. Downloaded and extracted WildFly 42 to `C:\Tools`; first local startup succeeded.
6. In PowerShell, `npm.cmd` was used instead of `npm` to bypass the execution policy blocking `npm.ps1`. **No global disabling of the PowerShell execution policy was necessary.**

Verification commands for the next developer:

```powershell
java -version
javac -version
mvn -version
node -v
npm.cmd -v
git --version
```

**Do not report as installed:** Nginx, Keycloak, PostgreSQL, ClamAV, Gitleaks, Ubuntu server, secret manager, signature service, and watermarking engine were neither installed nor validated during this session.

### 2.2 Tool Versions Used to Initialize React

| Component           | Observed Version | Details                                                                 |
| ------------------- | ---------------- | ----------------------------------------------------------------------- |
| `create-vite`     | **9.3.4**  | Generator used during project creation.                                 |
| `vite`            | **8.3.8**  | Actual version displayed by the server and build.                       |
| `vite-plugin-pwa` | **2.0.0**  | Version confirmed via `npm.cmd ls vite-plugin-pwa` and the PWA build. |

Commands executed during initialization:

```powershell
# From the repository root
npm.cmd create vite@latest frontend -- --template react-ts
# Linter selected in the wizard: ESLint

cd frontend
npm.cmd install -D vite-plugin-pwa
npm.cmd ls vite-plugin-pwa
npm.cmd run build
```

The frontend was started locally with Vite at `http://localhost:5173/` and its starter page rendered properly.

---

## 3. Exact List of Frontend npm Packages

The versions below are the **locked versions** directly declared in `frontend/package-lock.json` present on `main` as of 11/24/2026. They are more precise than the version ranges (`^` or `~`) listed in `package.json`.

### 3.1 Runtime Dependencies (`dependencies`)

| npm Package   | Locked Version   | Role                            |
| ------------- | ---------------- | ------------------------------- |
| `react`     | **19.3.1** | User interface library.         |
| `react-dom` | **19.3.1** | React rendering in the browser. |

### 3.2 Development Dependencies (`devDependencies`)

| npm Package                     | Locked Version    | Role                                                      |
| ------------------------------- | ----------------- | --------------------------------------------------------- |
| `@eslint/js`                  | **10.0.1**  | Base JavaScript ESLint rules.                             |
| `@types/node`                 | **24.19.2** | TypeScript types for Node APIs used by the configuration. |
| `@types/react`                | **19.3.0**  | TypeScript types for React.                               |
| `@types/react-dom`            | **19.3.0**  | TypeScript types for React DOM.                           |
| `@vitejs/plugin-react`        | **6.3.0**   | React integration for Vite.                               |
| `eslint`                      | **10.15.0** | Static code analysis / linting.                           |
| `eslint-plugin-react-hooks`   | **7.1.1**   | Rules for React hooks.                                    |
| `eslint-plugin-react-refresh` | **0.5.8**   | Rules associated with React Fast Refresh.                 |
| `globals`                     | **17.14.0** | Global variable definitions for ESLint.                   |
| `typescript`                  | **6.0.3**   | TypeScript compiler and static typing.                    |
| `typescript-eslint`           | **8.73.0**  | TypeScript support in ESLint.                             |
| `vite`                        | **8.3.8**   | Development server and bundler.                           |
| `vite-plugin-pwa`             | **2.0.0**   | PWA manifest and service worker generation (Workbox).     |

The numerous **transitive dependencies** are not all reproduced here: their exact versions and integrity hashes are stored in **`frontend/package-lock.json`** (lockfile v3 format). The preferred command to reproduce them is **`npm ci`**, rather than manual package-by-package reinstallation.

**npm scripts registered in `frontend/package.json`:**

```json
{
  "dev": "vite",
  "build": "tsc -b && vite build",
  "lint": "eslint .",
  "preview": "vite preview"
}
```

Note: **No business UI component library, OIDC/Keycloak client, React router, upload SDK, frontend testing tool, or charting library has been explicitly added yet.**

---

## 4. Declared Maven / Java Dependencies

### 4.1 Application Structure

**Parent Project:**

- `groupId`: **`com.doctrace`** (replaces the old `tn.supcom.doctrace`).
- `artifactId`: **`doctrace-parent`**.
- `version`: **`0.1.0-SNAPSHOT`**.
- `packaging`: `pom`.
- Java target: `<maven.compiler.release>25</maven.compiler.release>`.

| Module                | Packaging | Declared Internal Dependencies           | Intended Use                                         |
| --------------------- | --------- | ---------------------------------------- | ---------------------------------------------------- |
| `doctrace-core`     | `jar`   | None                                     | Models and business logic.                           |
| `doctrace-security` | `jar`   | `doctrace-core`                        | Future cryptographic services and security controls. |
| `doctrace-document` | `jar`   | `doctrace-core`                        | Future document processing.                          |
| `doctrace-api`      | `war`   | `doctrace-core`, `doctrace-security` | Jakarta REST endpoints.                              |

The document module **is not yet declared as a dependency of the API module**; it will be wired in when a feature requires it.

### 4.2 External Dependencies and Plugins

| Maven Element                                 | Observed / Declared Version | Scope                            | Status                                                                               |
| --------------------------------------------- | --------------------------- | -------------------------------- | ------------------------------------------------------------------------------------ |
| `jakarta.platform:jakarta.jakartaee-api`    | **11.0.0**            | `provided` in `doctrace-api` | **Declared**; APIs are provided by WildFly at runtime.                         |
| `org.apache.maven.plugins:maven-war-plugin` | **3.5.1**             | WAR build plugin                 | **Pinned version** in `doctrace-api/pom.xml`.                                |
| `maven-resources-plugin`                    | **3.5.0**             | Build                            | Version **observed in Maven logs**, not explicitly pinned in the created POMs. |
| `maven-compiler-plugin`                     | **3.16.0**            | Build                            | Version observed in logs.                                                            |
| `maven-surefire-plugin`                     | **3.6.1**             | Tests                            | Version observed; no tests have been executed yet (`No tests to run`).             |
| `maven-jar-plugin`                          | **3.5.1**             | JAR build                        | Version observed in logs.                                                            |

**Not yet added to POMs:** Apache PDFBox, steganography/watermarking libraries, dedicated cryptography libraries, Keycloak/OIDC clients, JUnit 5, ArchUnit, or other testing frameworks. The algorithms specified in the design document are not yet installed as dependencies.

**Maven results obtained on the PC:**

```powershell
mvn -B -ntp validate
mvn -B -ntp clean package
```

All five Maven projects (including the parent) displayed `SUCCESS` during the final compilation/packaging. The JARs for modules without source code were empty at this stage, which is normal for this initial setup. Compilation is **not** proof that document processing is implemented.

**Expected Artifacts:**

```text
doctrace-core/target/doctrace-core-0.1.0-SNAPSHOT.jar
doctrace-security/target/doctrace-security-0.1.0-SNAPSHOT.jar
doctrace-document/target/doctrace-document-0.1.0-SNAPSHOT.jar
doctrace-api/target/doctrace-api-0.1.0-SNAPSHOT.war
```

---

## 5. Directory Tree and Actually Created Files

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
│   ├── src/                   # Still essentially the Vite template
│   ├── public/
│   └── index.html
├── docs/                      # Already existing design documents
├── README.md
└── ATTRIBUTIONS.md
```

**Confirmed GitHub Status as of 11/24/2026:** This structure and the `ci.yml` workflow are present on **`main`**. The repository also contains the `feat/bootstrap-doctrace` branch used during development.

**Do not confuse with the unpublished local structure:** A `deploy/` folder was created manually with `wildfly/original`, `wildfly/cli`, `wildfly/config`, and `scripts`. **It does not appear on `main` as of the verification date**, as it was still empty and/or had not been committed to Git. It is not an available deployment tool for the team.

---

## 6. Implemented Backend Code and Routing

### 6.1 Jakarta REST Initialization

File: `doctrace-api/src/main/java/com/doctrace/api/DocTraceApplication.java`

```java
package com.doctrace.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class DocTraceApplication extends Application {
}
```

The Jakarta REST path is **`/`**, not `/api`: the `/api` prefix is already assigned to the **WAR context**.

### 6.2 Availability Endpoint

File: `doctrace-api/src/main/java/com/doctrace/api/HealthResource.java`

- Class annotated with `@Path("/health")`.
- `GET` method annotated with `@GET`, `@PermitAll`, `@Produces(MediaType.APPLICATION_JSON)`.
- Returned response: **`{"status":"UP"}`**.
- No business logic, database access, readiness checks, or identity verification.

**Security Warning:** `@PermitAll` explicitly documents the intended access policy for the endpoint in code, but does not demonstrate a complete operational authentication/authorization policy.

### 6.3 WAR Context

File: `doctrace-api/src/main/webapp/WEB-INF/jboss-web.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jboss-web>
    <context-root>/api</context-root>
</jboss-web>
```

The file was **verified inside the WAR**:

```powershell
jar tf .\doctrace-api\target\doctrace-api-0.1.0-SNAPSHOT.war |
    Select-String "jboss-web.xml"
# Output obtained: WEB-INF/jboss-web.xml
```

**Adopted Routing Contract:** Frontend `/`, API `/api/...` on the same origin. Do not configure both the `/api` WAR context and `@ApplicationPath("/api")`, otherwise the URL path will be duplicated.

---

## 7. React Frontend and PWA Configuration

File: `frontend/vite.config.ts`.

**Currently Configured:**

- Plugins: `@vitejs/plugin-react` and `vite-plugin-pwa`.
- Manifest name: `DocTrace`; display mode `standalone`; `start_url` and `scope` set to `.`.
- `registerType: 'prompt'` and `injectRegister: 'auto'`.
- Workbox: precaches `**/*.{js,css,html}`; no additional runtime caching (`runtimeCaching: []`); excludes `/api` paths from navigation fallback; cleans up outdated caches.

**Actually Validated Build:**

```powershell
cd frontend
npm.cmd run build
```

Output obtained: `vite v8.3.8`, build succeeded, `PWA v2.0.0` and generation of:

```text
frontend/dist/index.html
frontend/dist/manifest.webmanifest
frontend/dist/registerSW.js
frontend/dist/sw.js
frontend/dist/workbox-2fbc6a65.js    # observed hashed name, may vary
frontend/dist/assets/...             # variable hashed names
```

The build reported **5 precached entries**.

**Unvalidated Limitations:** Real PWA installation, complete PWA icons, update notifications, offline behavior, private data caching tests, and API error handling. `runtimeCaching: []` is a helpful setting, **not** sufficient proof of confidentiality.

---

## 8. Local WildFly 42 Deployment Performed

### 8.1 Local Windows Folders

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

The folder `C:\Tools\doctrace-deploy\frontend` received a **copy of `frontend/dist/*`**. This folder is **not** a Git-tracked directory.

### 8.2 Startup and API Deployment

```powershell
# Terminal 1: start the server (keep this terminal open)
& "C:\Tools\wildfly-42.0.0.Final\bin\standalone.bat" -b 127.0.0.1

# Terminal 2: from the repository root, with the WAR previously built
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect `
  --command="deploy doctrace-api\target\doctrace-api-0.1.0-SNAPSHOT.war"
```

WildFly reported a successful startup:

```text
WFLYSRV0025: WildFly 42.0.0.Final (WildFly Core 34.0.0.Final)
started ... Server configuration file in use: standalone.xml
```

Local application port: **8080 (HTTP)**. Observed management interface: **127.0.0.1:9990 (HTTP)**. This is **not** an internet-facing production configuration.

### 8.3 Undertow Static Handler

The default server initially used `welcome-content`, pointing to `${jboss.home.dir}/welcome-content`. This handler **was not modified directly**; a new handler was created:

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect `
  --command="/subsystem=undertow/configuration=handler/file=doctrace-frontend:add(path=C:/Tools/doctrace-deploy/frontend,directory-listing=false,follow-symlink=false)"
```

Reading the configuration confirmed:

```text
"path" => "C:/Tools/doctrace-deploy/frontend"
"directory-listing" => false
"follow-symlink" => false
```

The `/` path of the default Undertow host was then mapped to the new handler:

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect `
  --command="/subsystem=undertow/server=default-server/host=default-host/location=\/:write-attribute(name=handler,value=doctrace-frontend)"
```

WildFly requested a reload (`reload-required`). This was executed:

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect --command=":reload"
```

Both URLs were verified again after this reload.

### 8.4 Actually Executed HTTP Tests

| Local Test                                       | Observed Result                                       |
| ------------------------------------------------ | ----------------------------------------------------- |
| `http://localhost:8080/`                       | React/Vite starter page served from WildFly.          |
| `http://localhost:8080/api/health`             | Body: `{"status":"UP"}`.                            |
| `http://localhost:8080/api/non-existent-route` | `404`.                                              |
| `http://localhost:8080/dashboard`              | `404` (expected: no React fallback configured yet). |
| After `:reload`                                | Both frontend and API remain accessible.              |

Commands used to verify error codes:

```powershell
curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/api/non-existent-route
curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/dashboard
```

**Work not yet done:** SPA deep routing, safe fallback strategy, HTTPS/TLS 1.3, Nginx proxy, secrets management, application rollback, automated DEV/PROD deployment.

### 8.5 Server Configuration Snapshot

Executed command:

```powershell
& "C:\Tools\wildfly-42.0.0.Final\bin\jboss-cli.bat" --connect --command=":take-snapshot"
```

Location returned by WildFly:

```text
C:\Tools\wildfly-42.0.0.Final\standalone\configuration\standalone_xml_history\snapshot\20261123-192608964standalone.xml
```

This snapshot is a **configuration backup**, not a backup of the React files or the WAR.

### 8.6 Professor's Scripts

Provided archive: **`Atelier_Sécurité_Applicative_-_Semaine_4(1).zip`**.

It contains exactly:

```text
add-api.cli
default-host.cli
add-host.cli
```

These scripts were received and reviewed as **reference material**. They were **not** applied as-is to the currently running instance. Adapting them requires preserving `/api` during SPA fallback, avoiding masking API 404 errors, controlling HTTP headers, and making the commands idempotent.

Copying them into `deploy/wildfly/original` had been requested but **was not confirmed** in the conversation; the `deploy/` folder is not present on `main` at the time of verification.

---

## 9. Git, Branches, and GitHub Actions Pipeline

### 9.1 Completed Git Work

- Created a `feat/bootstrap-doctrace` branch to prepare the foundation without immediately touching `main`.
- Added a `.gitignore` protecting `**/target/`, `**/node_modules/`, `**/dist/`, `.env` files, and IDE files.
- Initial commit and push of `feat/bootstrap-doctrace` succeeded.
- As of **11/24/2026**, the starter files and CI are **also present on `main`**: integration on the main branch is therefore verifiable on GitHub.
- Commit recorded on the latest GitHub Actions workflow run: **`7bdff7fcef3c87e7d440d13e00310c1f095c0ab2`**, message `feat: configure WildFly deployment and DocTrace CI foundation`.

### 9.2 Existing Workflow `.github/workflows/ci.yml`

**Trigger:** `push` to branches and `pull_request` targeting `main`.

**`backend` job:**

```text
ubuntu-latest
→ actions/checkout@v7
→ actions/setup-java@v6 (Temurin Java 25, Maven cache)
→ mvn -B -ntp clean verify
```

**`frontend` job:**

```text
ubuntu-latest
→ actions/checkout@v7
→ actions/setup-node@v7 (Node.js 26, npm cache)
→ npm ci
→ npm run lint
→ npm run build
```

**Actually Verified Status on GitHub:** Two runs of the `DocTrace CI` workflow were found—one on `feat/bootstrap-doctrace` and one on `main`—both with **`conclusion: success`**. The latest run:

[https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/actions/runs/45464412019](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/actions/runs/45464412019)

**Limitations:**

- A successful `mvn verify` does not guarantee that unit tests exist. During local packaging, Maven reported `No tests to run`.
- **Gitleaks is missing** from the current workflow.
- **REST architecture test is missing**: automatically verifying that every endpoint has `@RolesAllowed`, `@DenyAll`, or `@PermitAll`.
- No DEV/PROD deployment pipeline or WildFly integration test in GitHub Actions.
- No automated verification of the document issuance → verification cycle.

---

## 10. Resuming the Project on Another Team Member's PC

### 10.1 Prerequisites

To closely reproduce the observed environment, prepare:

- Windows or Linux with **JDK 25**;
- **Apache Maven 3.10.2** or a compatible version, validated against the repository build;
- **Node.js 26** and a compatible npm version;
- Git;
- **WildFly 42.0.0.Final** to test server integration.

DEV/PROD parity should be pursued, but the **Windows path `C:\Tools` is not portable** to an Ubuntu VM. It will need to be replaced with configurable paths.

### 10.2 Cloning and Building the Repository

```powershell
git clone https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace.git
cd .\Securite_Applicative_Trinome_7_DocTrace

git switch main
git pull --ff-only origin main

# Maven Backend
mvn -B -ntp clean verify

# Frontend: versions locked by package-lock.json
cd .\frontend
npm.cmd ci
npm.cmd run lint
npm.cmd run build
```

On Linux, use `npm` instead of `npm.cmd`.

### 10.3 Understanding the Limitations of Cloning

**The repository alone is not yet sufficient to automatically reproduce the local WildFly environment.** Versioned deployment files are currently missing. A teammate must:

1. install WildFly 42 and start an isolated development server;
2. build the WAR;
3. deploy the WAR to WildFly;
4. build the frontend and copy its static files to a chosen directory;
5. create the Undertow handler and corresponding routing;
6. validate `/` and `/api/health` as well as 404 errors.

**Do not blindly copy `standalone.xml` from another PC**: it may contain machine-dependent paths, ports, and settings. Prefer parameterized, reviewed, and tested CLI scripts.

---

## 11. Remaining Work, Categorized by the Four Mission Blocks

### Block 1 — Project Setup: Foundation Already in Place

- Replace the Vite demo interface with a DocTrace UI structure.
- Lock down the frontend/API contract in development and production.
- Add minimal startup tests and reproduction documentation.

### Block 2 — Mandatory CI: Immediate Priority

1. Add **Gitleaks** to the pipeline (secret detection and failing CI upon findings).
2. Add **unit tests** and their test dependency (e.g., JUnit, to be explicitly selected and configured).
3. Implement the **REST endpoint architecture test**: no exposed method without a `@RolesAllowed`, `@DenyAll`, or `@PermitAll` policy annotation.
4. Verify that every mandatory check fails the workflow if it does not pass.
5. Add a minimal WildFly deployment test to reduce integration regressions.

### Block 3 — First C-I-A Functional Chain: Business Priority

1. Define the JSON/HTTP contract for **issuance**.
2. Create a minimal issuance endpoint.
3. Create a **verification** endpoint.
4. Connect issuance → verification on a simple, reproducible positive test case.
5. Add negative test cases, notably modified document, unauthorized access, and malicious file.

### Block 4 — Security: To Be Implemented Progressively

- **Confidentiality:** TLS 1.3, AES-256-GCM, secure key and secret management.
- **Integrity:** SHA-256, Ed25519, JCS canonicalization, DCT/QIM watermarking followed by LSB experiments.
- **Availability:** File validation, malicious file detection, size and dimension limits, memory/CPU/time budgets, isolated document processing.
- **Authentication:** OIDC Authorization Code with PKCE, MFA/TOTP, secure sessions.
- **Authorization:** Explicit access policies, RBAC, object-level access control (anti-IDOR), optionally ABAC.
- **Private Data:** Minimization, pseudonymization as needed, control over logs and PWA caches.

### Cross-Cutting Track — Robust Deployment and Professor's Scripts

- Version-control the **original `.cli` scripts** and their DocTrace adaptations.
- Make operations repeatable (parameterized DEV/PROD configuration, pre-modification checks, handling existing resources).
- Guarantee **same-origin** hosting for React/PWA (`/`) and the API (`/api`).
- Handle React fallback without turning API 404 errors into HTML or masking static resource errors.
- Plan for HTTPS/TLS, monitoring, secrets management, smoke tests, and rollback capability.
- Test on a clean environment, not only on the manually modified WildFly instance.

---

## 12. Summary for the Team

**What can be picked up immediately:** The source code on `main`, the multi-module Java structure, the React/PWA frontend, the two REST classes, the WAR configuration, and the first passing GitHub Actions pipeline.

**What works only on the development PC:** The manually configured WildFly 42 server, the `doctrace-frontend` Undertow handler, the local copy of `frontend/dist` in `C:\Tools\doctrace-deploy\frontend`, and the configuration snapshot.

**What does not exist yet in the project:** Operational document application, issuance/verification, authentication, full application security, Gitleaks, REST architecture tests, and reproducible automated deployment.

**Recommended next steps:** Finalize the required CI checks, version-control the deployment scripts, and then build a first end-to-end testable C-I-A document feature, introducing the appropriate security mechanisms at each step.

---

## 13. Verified Repository References for This Handover

- [`main` Repository](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/tree/main)
- [Parent `pom.xml`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/pom.xml)
- [`doctrace-api/pom.xml`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/doctrace-api/pom.xml)
- [`frontend/package.json`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/frontend/package.json)
- [`frontend/package-lock.json`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/frontend/package-lock.json)
- [`frontend/vite.config.ts`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/frontend/vite.config.ts)
- [Workflow `.github/workflows/ci.yml`](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/blob/main/.github/workflows/ci.yml)
- [Latest observed CI run on `main` — SUCCESS](https://github.com/Achref88MANSOURI/Securite_Applicative_Trinome_7_DocTrace/actions/runs/45464412019)

*This log describes the actual state observed on November 24, 2026. As the code evolves, update the versions, test results, and deployment scripts in the repository.*
