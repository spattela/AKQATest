package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.Select


class BunProductsPage extends BasePage {


    BunProductsPage(WebDriver driver) {
        super(driver)
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "(//div[contains(@class,'product-list__photo')])[1]")
    private WebElement firstProduct

    def  clickFirstProduct() {
        waitUntilElementIsDisplayed(By.xpath("(//div[contains(@class,'product-list__photo')])[1]"),5)
        clickByJavaScript(firstProduct)
        return new BunProductDescriptionpage(driver)
    }

    def getProductTitle(){
        firstProduct.getText()
    }
}
