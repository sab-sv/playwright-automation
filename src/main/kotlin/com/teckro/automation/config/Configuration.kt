package com.teckro.automation.config

/**
 * Central configuration object that reads runtime properties.
 */
object Configuration {

    /** Run browser without a visible window */
    val headless: Boolean
        get() = (prop("headless") ?: env("HEADLESS") ?: "false").toBoolean()

    /** Slow down each Playwright action by this many milliseconds (useful while learning) */
    val slowMo: Double
        get() = (prop("slowMo") ?: env("SLOW_MO") ?: "0").toDouble()

    /** Maximum time (ms) to wait for navigation / elements */
    val defaultTimeout: Double
        get() = (prop("defaultTimeout") ?: env("DEFAULT_TIMEOUT") ?: "30000").toDouble()

    /** Base URL for the application under test */
    val baseUrl: String
        get() = prop("baseUrl") ?: env("BASE_URL") ?: "https://madrid.craigslist.org"

    /** Viewport width */
    val viewportWidth: Int
        get() = (prop("viewportWidth") ?: env("VIEWPORT_WIDTH") ?: "1280").toInt()

    /** Viewport height */
    val viewportHeight: Int
        get() = (prop("viewportHeight") ?: env("VIEWPORT_HEIGHT") ?: "720").toInt()

    // ── Helpers ──────────────────────────────────────────────────────────────

    private fun prop(key: String): String? = System.getProperty(key)?.takeIf { it.isNotBlank() }
    private fun env(key: String): String? = System.getenv(key)?.takeIf { it.isNotBlank() }
}
