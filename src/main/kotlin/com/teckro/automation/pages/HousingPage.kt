package com.teckro.automation.pages

import com.microsoft.playwright.Page

/**
 * All valid sort options for the housing search results.
 */
enum class SortOption(val label: String, val selector: String) {
    NEWEST ("Newest", ".bd-for-bd-combo-box .cl-search-sort-mode-newest"),
    OLDEST ("Oldest", ".bd-for-bd-combo-box .cl-search-sort-mode-oldest"),
    PRICE_ASC ("Price (low to high)", ".bd-for-bd-combo-box .cl-search-sort-mode-price-asc"),
    PRICE_DESC ("Price (high to low)", ".bd-for-bd-combo-box .cl-search-sort-mode-price-desc"),
    UPCOMING ("Upcoming", ".bd-for-bd-combo-box .cl-search-sort-mode-upcoming"),
    RELEVANCE ("Relevance", ".bd-for-bd-combo-box .cl-search-sort-mode-relevance")
}

/**
 * Page Object for the Housing screen.
 */
class HousingPage(page: Page) : BasePage(page) {

    // ── Selectors ─────────────────────────────────────────────────────────────
    private val pageHeading = "h1"
    private val sortComboBox = ".cl-search-sort-mode"
    private val sortContextOptions = ".bd-for-bd-combo-box"
    private val searchInput = ".cl-query-with-search-suggest input"
    private val executeSearch = ".cl-query-bar .cl-exec-search"
    private val priceInfoList = ".priceinfo"
    private val thumbnailList = "button.cl-search-view-mode-thumb"
    // Sort option selectors are defined in the SortOption enum above
    // ── Actions ───────────────────────────────────────────────────────────────

    /** Navigate to the housing page. */
    fun open(baseUrl: String): HousingPage {
        goto("$baseUrl/search/hhh?lang=en&cc=gb")
        return this
    }

    /** Wait for the housing page to finish loading. */
    fun waitUntilLoaded(): HousingPage {
        waitForVisible(pageHeading)
        return this
    }

    /** Change to thumbnail view */
    fun changeToThumbnailView(): HousingPage {
        click(thumbnailList)
        return this
    }

    /** Search for a specific property */
    fun searchForProperty(query: String): HousingPage {
        fill(searchInput, query)
        click(executeSearch)
        return this
    }

    /** Open sorting combo options */
    fun openSortingOptions(): HousingPage {
        click(sortComboBox)
        if (!isVisible(sortContextOptions))
            throw AssertionError("Sort options not visible after clicking sort combo box")
        return this
    }

    /** Select a value from the sort combo box using a type-safe enum entry. */
    fun selectSortOption(option: SortOption): HousingPage {
        click(option.selector)
        return this
    }

    /**
     * Assert that every option in [expectedVisible] is visible in the sort dropdown.
     * Simply omit an option from the list if you don't expect it to appear —
     * no flags or negative assertions needed.
     */
    fun assertSortingOptions(expectedVisible: List<SortOption>): HousingPage {
        expectedVisible.forEach { option ->
            if (!isVisible(option.selector))
                throw AssertionError("Expected sort option '${option.label}' to be visible but it was not")
        }
        val unexpected = SortOption.entries - expectedVisible.toSet()
        unexpected.forEach { option ->
            if (isVisible(option.selector))
                throw AssertionError("Unexpected sort option '${option.label}' was visible but should not be")
        }
        return this
    }

    /** Assert the default set of sort options shown before any search is performed. */
    fun assertDefaultSortOptions(): HousingPage {
        return assertSortingOptions(
            listOf(
                SortOption.NEWEST,
                SortOption.OLDEST,
                SortOption.PRICE_ASC,
                SortOption.PRICE_DESC,
                SortOption.UPCOMING
            )
        )
    }

    /** Assert the set of sort options shown after a keyword search. */
    fun assertAfterSearchSortOptions(): HousingPage {
        return assertSortingOptions(
            listOf(
                SortOption.NEWEST,
                SortOption.OLDEST,
                SortOption.PRICE_ASC,
                SortOption.PRICE_DESC,
                SortOption.RELEVANCE,
                SortOption.UPCOMING
            )
        )
    }

    /** Induced failure: Assert that the "Relevance" option is NOT visible after a search, even though it actually is. */
    fun assertAfterSearchSortOptions_inducedFailure(): HousingPage {
        return assertSortingOptions(
            listOf(
                SortOption.NEWEST,
                SortOption.OLDEST,
                SortOption.PRICE_ASC,
                SortOption.PRICE_DESC,
                SortOption.UPCOMING
            )
        )
    }

    /** Assert price info list order (Asc/ Desc) */
    fun assertPriceInfoListOrder(ascending: Boolean = true): HousingPage {
        val prices = getElements(priceInfoList).map { element ->
            element.textContent()
                ?.filter { char -> char.isDigit() || char == '.' }
                ?.replace(".", "")
                ?.toIntOrNull()
        }.filterNotNull()

        val sortedPrices = if (ascending) prices.sorted() else prices.sortedDescending()

        if (prices != sortedPrices)
            throw AssertionError(
                "Expected price info list to be in ${if (ascending) "ascending" else "descending"} order, " +
                "but it was not. Actual order: $prices"
            )
        return this
    }

    // ── Assertions / queries ──────────────────────────────────────────────────

    /** Returns true when the housing page heading is visible. */
    fun isLoaded(): Boolean = isVisible(pageHeading)
}