package com.teckro.automation.hooks

import com.microsoft.playwright.Page
import com.teckro.automation.config.Configuration
import com.teckro.automation.utils.BrowserManager
import com.teckro.automation.utils.PageUtils
import io.qameta.allure.Allure
import io.qameta.allure.model.Status
import org.testng.ITestResult
import org.testng.annotations.AfterClass
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream

/**
 * BaseTest is the parent class for every test class.
 *
 * It manages the Playwright [BrowserManager] lifecycle and exposes a
 * ready-to-use [page] property to all subclasses.
 *
 * ## Lifecycle
 * ```
 * @BeforeClass  → opens browser + new page
 * @AfterMethod  → on FAILURE: takes a screenshot
 * @AfterClass   → closes browser
 * ```
 *
 * ## Learning note — TestNG annotations
 * | Annotation       | Runs …                                       |
 * |------------------|----------------------------------------------|
 * | @BeforeSuite     | once before all test classes in the suite    |
 * | @BeforeClass     | once before all tests in this class          |
 * | @BeforeMethod    | before every single @Test method             |
 * | @AfterMethod     | after every single @Test method              |
 * | @AfterClass      | once after all tests in this class finish    |
 * | @AfterSuite      | once after the entire suite finishes         |
 */
abstract class BaseTest {

    private val log = LoggerFactory.getLogger(javaClass)

    // BrowserManager is initialised in @BeforeClass and closed in @AfterClass
    private lateinit var browserManager: BrowserManager

    /**
     * The Playwright [Page] instance.
     * Available to all test methods in subclasses.
     */
    protected lateinit var page: Page

    // ── Lifecycle hooks ───────────────────────────────────────────────────────

    @BeforeClass(alwaysRun = true)
    fun setUpBrowser() {
        log.info("Launching browser: Chromium | headless=${Configuration.headless}")
        browserManager = BrowserManager()
        page = browserManager.newPage()
    }

    @AfterMethod(alwaysRun = true)
    fun handleTestResult(result: ITestResult) {
        if (result.status == ITestResult.FAILURE) {
            val screenshotBytes = page.screenshot(Page.ScreenshotOptions().setFullPage(true))
            // Attach screenshot to Allure report
            Allure.addAttachment("Screenshot on failure", "image/png", ByteArrayInputStream(screenshotBytes), "png")
            // Also save to disk as before
            val screenshotPath = PageUtils.takeScreenshot(page, result.name)
            log.error("Test FAILED: ${result.name}. Screenshot saved → $screenshotPath")
        }
    }

    @AfterClass(alwaysRun = true)
    fun tearDownBrowser() {
        log.info("Closing browser after: ${javaClass.simpleName}")
        browserManager.close()
    }
}
