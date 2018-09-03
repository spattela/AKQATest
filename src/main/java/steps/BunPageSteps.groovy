package steps

import cucumber.api.DataTable
import cucumber.api.PendingException
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.xpath.operations.String
import org.openqa.selenium.*
import pages.BunProductDescriptionpage
import pages.BunProductsPage
import pages.BunSearchLandingPage
import pages.BunWishlistPage
import utilities.DriverUtil
import utilities.URLReader

import static cucumber.api.groovy.EN.*
import static cucumber.api.groovy.Hooks.Before

WebDriver driver
BunSearchLandingPage bunSearchLandingPage
BunProductsPage bunProductsPage
BunWishlistPage bunWishlistPage
BunProductDescriptionpage bunProductDescriptionpage
def productTitle

Before("@Wishlist") { scenario ->
    driver = DriverUtil.createWebDriver()
    bunSearchLandingPage = new BunSearchLandingPage(driver)
    beforeScenario = scenario
}

Given(~/^I am on bunnings search landing page$/) { ->
    driver.get(URLReader.getURL("bun.url"))
}

When(~/^I navigate through search page with paint product$/) { ->
    bunSearchLandingPage.enterSearchItem()
    bunProductsPage = bunSearchLandingPage.clickSearchIcon()

}

And(~/^I click on the first paint result in the products page$/) { ->
    productTitle = bunProductsPage.getProductTitle()
    bunProductDescriptionpage = bunProductsPage.clickFirstProduct()

}

Then(~/^I should land on product details page and add product to wishlist$/) { ->
      bunProductDescriptionpage.addProdToWishList()
      bunWishlistPage= bunProductDescriptionpage.clickWishListIcon()
}

Then(~/^I should see the wishlist with a product added$/) { ->
    bunWishlistPage.verifyAProductAdded(productTitle)
}
