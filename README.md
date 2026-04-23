# Playwright Kotlin Automation

A self-contained browser-automation test suite built with
**[Playwright for Java](https://playwright.dev/java/)** and **Kotlin**,
wired together by **Maven** (via the bundled Maven Wrapper).

> **Zero external installs needed** – Java 17+ is the only prerequisite.
> Maven, Kotlin, Playwright, Chromium, and all other dependencies are
> downloaded and managed automatically.

---

## How to Run

**macOS / Linux**
```bash
./mvnw test
./mvnw allure:serve
```

**Windows**
```cmd
mvnw.cmd test
mvnw.cmd allure:serve
```

That single test command will:
1. Download Maven, Kotlin, and all dependencies (first run only)
2. Download the Chromium binary (first run only, cached afterwards):
   - macOS: `~/Library/Caches/ms-playwright/`
   - Windows: `%USERPROFILE%\AppData\Local\ms-playwright\`
   - Linux: `~/.cache/ms-playwright/`
3. Compile and run all tests

To run headless or with other overrides, append them as system properties:

**macOS / Linux**
```bash
./mvnw test -Dheadless=true -DslowMo=500
```

**Windows**
```cmd
mvnw.cmd test -Dheadless=true -DslowMo=500
```

---

## Project Structure

```
playwright-automation/
├── mvnw / mvnw.cmd                    ← Maven Wrapper (no global Maven needed)
├── pom.xml                            ← Dependencies & plugin config
└── src/
    ├── main/kotlin/com/teckro/automation/
    │   ├── config/
    │   │   └── Configuration.kt       ← Runtime config (baseUrl, headless, timeouts…)
    │   ├── pages/
    │   │   ├── BasePage.kt            ← Shared Playwright helpers (click, fill, getElements…)
    │   │   └── HousingPage.kt         ← Page Object – housing search screen
    │   └── utils/
    │       ├── BrowserManager.kt      ← Playwright lifecycle (create / close browser)
    │       └── PageUtils.kt           ← Screenshot helper
    └── test/kotlin/com/teckro/automation/
        ├── hooks/
        │   └── BaseTest.kt            ← TestNG lifecycle + Allure screenshot on failure
        ├── tests/
        │   └── HousingTest.kt         ← Housing test scenarios
        └── resources/
            └── testng.xml             ← Suite definition
```

---

## Key Concepts

### Page Object Model (POM)
Each screen has its own class in `pages/`. Tests call **business-level methods**
(`housingPage.openSortingOptions().assertDefaultSortOptions()`) rather than
raw selectors, keeping tests readable and resilient to UI changes.

### Configuration
All runtime settings live in `Configuration.kt` and can be overridden via
JVM system properties or environment variables (highest priority first):

| Setting | JVM property | Env var | Default |
|---------|-------------|---------|---------|
| Headless mode | `-Dheadless=true` | `HEADLESS` | `false` |
| Slow-motion delay (ms) | `-DslowMo=500` | `SLOW_MO` | `0` |
| Default timeout (ms) | `-DdefaultTimeout=60000` | `DEFAULT_TIMEOUT` | `30000` |
| Base URL | `-DbaseUrl=https://…` | `BASE_URL` | `https://madrid.craigslist.org` |

### Allure Report
Failed tests automatically attach a **full-page screenshot** to the report.
The report is served on a local server — no static file serving issues.

---

## Tech Stack

| Tool | Version | Role |
|------|---------|------|
| Kotlin | 1.9.23 | Language |
| Playwright for Java | 1.44.0 | Browser automation |
| TestNG | 7.10.2 | Test framework |
| Allure | 2.27.0 | Test reporting |
| Maven Wrapper | 3.9.10 | Build & dependency management |
| Java | 17+ | Runtime |
