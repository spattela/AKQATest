package pages


import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import pages.BasePage


class BunSearchLandingPage extends BasePage{

    BunSearchLandingPage(WebDriver driver) {
        super(driver)
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "//input[contains(@class,'search-container_term')]")
    private WebElement searchTermInput

    @FindBy(id="icon-search")
    private WebElement searchIcon


    def enterSearchItem() {
        waitUntilElementIsDisplayed(By.xpath("//span[contains(@class,'view-more-icon')]"),5)
        searchTermInput.sendKeys("paint")


    }

    def clickSearchIcon(){
        waitUntilElementIsDisplayed(By.id("(//li[contains(@class,'ui-menu-item')])[1]"),10)
        clickByJavaScript(driver.findElement(By.xpath("(//li[contains(@class,'ui-menu-item')])[1]")))
        return new BunProductsPage(driver)
    }
}
