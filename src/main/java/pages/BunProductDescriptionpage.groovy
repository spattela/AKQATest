package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory



class BunProductDescriptionpage extends BasePage {


    BunProductDescriptionpage(WebDriver driver) {
        super(driver)
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "//button[contains(@class,'btn-add-wishlist')]")
    private WebElement addtoWishListBtn

    def addProdToWishList() {
        waitUntilElementIsDisplayed(By.xpath("//button[contains(@class,'btn-add-wishlist')]"), 10)
        clickByJavaScript(addtoWishListBtn)
        assert waitUntilElementIsDisplayed(By.xpath("//button[contains(@class,'btn-add-wishlist added')]"), 5)

    }

    def clickWishListIcon(){
        sleep(500)
//        scrollToElement(driver.findElement(By.linkText("Wish List")))
        driver.findElement(By.xpath("//a[contains(@title,'Wish List')]")).click()
        return new BunWishlistPage(driver)
    }


}
