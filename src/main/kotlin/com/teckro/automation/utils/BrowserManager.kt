package com.teckro.automation.utils

import com.microsoft.playwright.*
import com.teckro.automation.config.Configuration

/**
 * BrowserManager is responsible for the lifecycle of the Playwright
 * instance, the Browser, and the BrowserContext.
 *
 * It is NOT a singleton – each test class (or thread) should own
 * its own instance so tests can run in parallel safely.
 *
 * Typical usage:
 * ```kotlin
 * val manager = BrowserManager()
 * val page    = manager.newPage()
 * // ... test code ...
 * manager.close()
 * ```
 */
class BrowserManager {

    private val playwright: Playwright = Playwright.create()
    private val browser: Browser       = launchBrowser()
    private val context: BrowserContext = browser.newContext(buildContextOptions())

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Opens a fresh [Page] inside the shared [BrowserContext].
     * Multiple pages share cookies / local-storage within one context.
     */
    fun newPage(): Page = context.newPage().also { page ->
        page.setDefaultTimeout(Configuration.defaultTimeout)
        page.setDefaultNavigationTimeout(Configuration.defaultTimeout)
    }

    /**
     * Closes the context, browser, and Playwright instance.
     * Always call this in an @AfterClass / @AfterSuite hook.
     */
    fun close() {
        runCatching { context.close() }
        runCatching { browser.close() }
        runCatching { playwright.close() }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private fun launchBrowser(): Browser {
        val launchOptions = BrowserType.LaunchOptions()
            .setHeadless(Configuration.headless)
            .setSlowMo(Configuration.slowMo)

        return when (Configuration.browser.lowercase()) {
            "firefox" -> playwright.firefox().launch(launchOptions)
            "webkit"  -> playwright.webkit().launch(launchOptions)
            else      -> playwright.chromium().launch(launchOptions)   // default
        }
    }

    private fun buildContextOptions(): Browser.NewContextOptions =
        Browser.NewContextOptions()
            .setViewportSize(Configuration.viewportWidth, Configuration.viewportHeight)
            .setRecordVideoDir(null)   // set a path here to capture video
}
