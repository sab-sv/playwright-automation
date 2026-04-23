package com.teckro.automation.pages

import com.microsoft.playwright.ElementHandle
import com.microsoft.playwright.Page

/**
 * BasePage is the parent for every Page Object in this project.
 *
 * It holds a reference to the Playwright [Page] and exposes small
 * helpers so that child pages don't have to repeat boilerplate.
 *
 * ## Page Object Model (POM) pattern
 * Each screen / section of the application gets its own class that
 * extends BasePage.  Those classes expose high-level methods that
 * describe *what* to do (e.g. `loginPage.loginAs(user, pass)`) and
 * hide the low-level selector details from the test code.
 */
abstract class BasePage(protected val page: Page) {

    // ── Shared navigation ─────────────────────────────────────────────────────

    /**
     * Navigate to [url] and wait for the page to settle.
     */
    protected fun goto(url: String) {
        page.navigate(url)
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED)
    }

    // ── Shared interactions ───────────────────────────────────────────────────

    /** Click the first element matching [selector]. */
    protected fun click(selector: String) = page.locator(selector).first().click()

    /** Fill the first element matching [selector] with [value]. */
    protected fun fill(selector: String, value: String) =
        page.locator(selector).first().fill(value)

    /** Return the trimmed text content of the first element matching [selector]. */
    protected fun textOf(selector: String): String =
        page.locator(selector).first().innerText().trim()

    /** Return true when the first element matching [selector] is visible. */
    protected fun isVisible(selector: String): Boolean =
        page.locator(selector).first().isVisible

    /**
     * Wait until the first element matching [selector] becomes visible.
     * Uses the default timeout configured on the Page instance.
     */
    protected fun waitForVisible(selector: String) {
        page.locator(selector).first().waitFor(
            com.microsoft.playwright.Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
        )
    }

    /** Return all [ElementHandle]s matching [selector]. */
    protected fun getElements(selector: String): List<ElementHandle> =
        page.querySelectorAll(selector)
}
