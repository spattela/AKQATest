package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.Select

class BunWishlistPage extends BasePage{



    BunWishlistPage(WebDriver driver) {
        super(driver)
        PageFactory.initElements(driver, this)
    }

    @FindBy(id = 'developmentApplicationEnvironment')
    private WebElement appEnv



    def verifyAProductAdded(String prod){
        waitUntilElementIsDisplayed(By.xpath("(//tr[contains(@class,'hproduct')])[1]"),30)
        assert driver.findElement(By.xpath("(//tr[contains(@class,'hproduct')])[1]")).getText().contains(prod)
    }
}
