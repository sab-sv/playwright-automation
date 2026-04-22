# Playwright Kotlin Automation

A self-contained browser-automation test suite built with
**[Playwright for Java](https://playwright.dev/java/)** and **Kotlin**,
wired together by **Maven** (via the bundled Maven Wrapper).

> **Zero external installs needed** – Java 17+ is the only prerequisite.
> Maven, Kotlin, Playwright, and all browser binaries are downloaded
> automatically on the first run.

---

## Project Structure

```
playwright-automation/
├── mvnw / mvnw.cmd            ← Maven Wrapper (no global Maven needed)
├── .mvn/wrapper/              ← Wrapper bootstrap config
├── pom.xml                    ← All dependencies & plugin config
└── src/
    ├── main/kotlin/com/teckro/automation/
    │   ├── config/
    │   │   └── Configuration.kt      ← Runtime config (browser, baseUrl, headless…)
    │   ├── pages/
    │   │   ├── BasePage.kt           ← Shared Playwright helpers (click, fill…)
    │   │   ├── LoginPage.kt          ← Page Object – login screen
    │   │   └── DashboardPage.kt      ← Page Object – dashboard screen
    │   └── utils/
    │       ├── BrowserManager.kt     ← Playwright lifecycle (create / close)
    │       └── PageUtils.kt          ← Screenshot, navigation, text helpers
    └── test/kotlin/com/teckro/automation/
        ├── hooks/
        │   └── BaseTest.kt           ← TestNG lifecycle (@BeforeClass / @AfterClass)
        ├── tests/
        │   ├── LoginTest.kt          ← Login test scenarios
        │   └── DashboardTest.kt      ← Dashboard test scenarios
        └── resources/
            └── testng.xml            ← Suite definition (groups, classes)
```

---

## Key Concepts

### Page Object Model (POM)
Each screen has its own class in `pages/`.  Tests call **business-level
methods** (`loginPage.loginAs(user, pass)`) instead of low-level selectors.
This keeps tests readable and easy to maintain when the UI changes.

### Configuration
All run-time settings live in `Configuration.kt`.  You can override any
setting three ways (highest priority first):

| Method | Example |
|--------|---------|
| JVM system property | `./mvnw test -Dbrowser=firefox` |
| Environment variable | `HEADLESS=true ./mvnw test` |
| Default in `pom.xml` | `<browser>chromium</browser>` |

### TestNG Groups
Tests are tagged with groups (`smoke`, `regression`, `login`, `dashboard`).
Run only what you need:

```bash
./mvnw test -Dgroups=smoke
./mvnw test -Dgroups="login,dashboard"
```

---

## How to Run

> **The only prerequisite is Java 17+.**
> Maven, Kotlin, Playwright, and Chromium are all handled automatically.

### Run the full test suite
```bash
./mvnw test
```
On the very first run this will:
1. Download Maven 3.9.10 (via the wrapper)
2. Download Kotlin, Playwright JAR, TestNG and all other dependencies
3. **Download the Chromium browser binary** (≈150 MB, cached in `~/Library/Caches/ms-playwright/`) — skipped automatically on subsequent runs if already cached
4. Compile and execute all tests

### 3 · Common overrides

| Goal | Command |
|------|---------|
| Run headless | `./mvnw test -Dheadless=true` |
| Use Firefox | `./mvnw test -Dbrowser=firefox` |
| Use WebKit (Safari engine) | `./mvnw test -Dbrowser=webkit` |
| Slow-motion (500 ms/action) | `./mvnw test -DslowMo=500` |
| Smoke tests only | `./mvnw test -Dgroups=smoke` |
| Combine options | `./mvnw test -Dheadless=true -Dbrowser=firefox` |

---

## Adding New Tests

1. **Add a page object** in `src/main/kotlin/…/pages/`
   – extend `BasePage`, add selectors as private vals, expose action/query methods.

2. **Add a test class** in `src/test/kotlin/…/tests/`
   – extend `BaseTest`, annotate methods with `@Test(groups = […])`.

3. **Register the class** in `src/test/resources/testng.xml`.

---

## Tech Stack

| Tool | Version | Role |
|------|---------|------|
| Kotlin | 1.9.23 | Language |
| Playwright for Java | 1.44.0 | Browser automation |
| TestNG | 7.10.2 | Test framework |
| Maven Wrapper | 3.9.10 | Build & dependency management |
| Java | 17+ | Runtime |
