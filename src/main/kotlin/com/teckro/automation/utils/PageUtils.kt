package com.teckro.automation.utils

import com.microsoft.playwright.Page
import com.microsoft.playwright.options.LoadState
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A collection of commonly needed helper actions that wrap Playwright
 * calls with better error messages and sensible defaults.
 *
 * Pass a [Page] instance – no state is stored here.
 */
object PageUtils {

    private val timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")

    // ── Navigation ────────────────────────────────────────────────────────────

    /**
     * Navigate to a URL and wait until the page has fully loaded.
     */
    fun navigateTo(page: Page, url: String) {
        page.navigate(url)
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }

    // ── Screenshots ───────────────────────────────────────────────────────────

    /**
     * Capture a full-page screenshot.
     * Saved to: target/screenshots/<name>_<timestamp>.png
     *
     * @param page  The Playwright page to screenshot.
     * @param name  A short descriptive name for the file (no extension needed).
     */
    fun takeScreenshot(page: Page, name: String): String {
        val timestamp = LocalDateTime.now().format(timestampFormatter)
        val filePath  = "target/screenshots/${name}_$timestamp.png"
        Paths.get("target/screenshots").toFile().mkdirs()
        page.screenshot(
            Page.ScreenshotOptions()
                .setPath(Paths.get(filePath))
                .setFullPage(true)
        )
        return filePath
    }

    // ── Waits ─────────────────────────────────────────────────────────────────

    /**
     * Wait for a CSS selector to become visible.
     */
    fun waitForVisible(page: Page, selector: String) {
        page.locator(selector).waitFor(
            com.microsoft.playwright.Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
        )
    }

    // ── Text helpers ──────────────────────────────────────────────────────────

    /**
     * Returns the trimmed inner text of the first element matching [selector].
     */
    fun getText(page: Page, selector: String): String =
        page.locator(selector).first().innerText().trim()
}
