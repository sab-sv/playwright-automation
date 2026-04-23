package com.teckro.automation.utils

import com.microsoft.playwright.*
import com.teckro.automation.config.Configuration

/**
 * BrowserManager is responsible for the lifecycle of the Playwright browser and context.
 * It can be further extended to support multiple browser types, parallel contexts, video recording, etc.
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

        return playwright.chromium().launch(launchOptions)
    }

    private fun buildContextOptions(): Browser.NewContextOptions =
        Browser.NewContextOptions()
            .setViewportSize(Configuration.viewportWidth, Configuration.viewportHeight)
            .setRecordVideoDir(null)   // set a path here to capture video
}
