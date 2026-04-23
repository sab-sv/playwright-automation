package com.teckro.automation.tests

import com.teckro.automation.config.Configuration
import com.teckro.automation.hooks.BaseTest
import com.teckro.automation.pages.HousingPage
import com.teckro.automation.pages.SortOption
import org.testng.annotations.Test

class HousingTest : BaseTest() {

    private val baseUrl = Configuration.baseUrl

    @Test(
        groups = ["housing"],
        description = "Selecting ascending sort option and validate updated price list"
    )
    fun selectSortByPriceLowToHigh() {
        HousingPage(page)
            .open(baseUrl)
            .waitUntilLoaded()
            .changeToThumbnailView()
            .openSortingOptions()
            .selectSortOption(SortOption.PRICE_ASC)
            .assertPriceInfoListOrder(true)
    }

    @Test(
        groups = ["housing"],
        description = "Selecting descending sort option and validate updated price list"
    )
    fun selectSortByPriceHighToLow() {
        HousingPage(page)
            .open(baseUrl)
            .waitUntilLoaded()
            .changeToThumbnailView()
            .openSortingOptions()
            .selectSortOption(SortOption.PRICE_DESC)
            .assertPriceInfoListOrder(false)
    }

    @Test(
        groups = ["housing"],
        description = "Sort dropdown shows all default options before any search"
    )
    fun defaultSortOptionsAreVisible() {
        HousingPage(page)
            .open(baseUrl)
            .waitUntilLoaded()
            .openSortingOptions()
            .assertDefaultSortOptions()
    }

    @Test(
        groups = ["housing"],
        description = "Sort dropdown shows correct options after a keyword search"
    )
    fun sortOptionsAfterSearchAreCorrect() {
        HousingPage(page)
            .open(baseUrl)
            .waitUntilLoaded()
            .searchForProperty("House")
            .openSortingOptions()
            .assertAfterSearchSortOptions()
    }

    // This test is intentionally designed to fail by expecting the "Relevance" option to be visible after a search,
    // even though it is currently not implemented in the application.  This simulates an induced failure scenario for reportdemonstration purposes.
    @Test(
        groups = ["housing"],
        description = "Induced Failure:Sort dropdown shows unexpected option after a keyword search"
    )
    fun sortOptionsAfterSearchAreCorrect_inducedFailure() {
        HousingPage(page)
            .open(baseUrl)
            .waitUntilLoaded()
            .searchForProperty("House")
            .openSortingOptions()
            .assertAfterSearchSortOptions_inducedFailure()
    }
    
}
