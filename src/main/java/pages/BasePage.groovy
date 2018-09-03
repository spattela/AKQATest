package pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utilities.CalendarUtil

import java.util.regex.Matcher
import java.util.regex.Pattern

class BasePage {

    Logger LOG = LoggerFactory.getLogger(this.class);
    WebDriver driver

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    def extractDigits(String str) {
        str.replaceAll("[^0-9]", "")
    }

    def extractNumber(String str) {
        str.replaceAll("[^0-9\\.]", "")
    }

    def extractNumberWithDecimal(String str) {
        str.replaceAll("[^0-9]", "")
    }

    protected void clickByJavaScript(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element)
    }

    protected void fireChangeEvent(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(" var evt = document.createEvent('HTMLEvents'); evt.initEvent('change',true,true); arguments[0].dispatchEvent(evt);", element)
    }

    protected void fireOnMouseEvent(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(" var evt = document.createEvent('HTMLEvents'); evt.initEvent('mouseover',true,true); arguments[0].dispatchEvent(evt);", element)
    }

    protected void fireOnFocusEvent(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(" var evt = document.createEvent('HTMLEvents'); evt.initEvent('onfocus',true,true); arguments[0].dispatchEvent(evt);", element)
    }

        protected void fireBlurEvent(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(" var evt = document.createEvent('HTMLEvents'); evt.initEvent('blur',true,true); arguments[0].dispatchEvent(evt);", element)
    }

    protected void fireClickEvent(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(" var evt = document.createEvent('HTMLEvents'); evt.initEvent('click',true,true); arguments[0].dispatchEvent(evt);", element)
    }

    protected void setValueByJavaScript(WebElement element, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value ='" + value + "';", element)
    }

    public boolean checkElementExists(By by) {
        return checkElementExists(by, 2);
    }


    def waitForApplicationProcessing() {
        List<By> listOfElements = new ArrayList<By>();
        listOfElements.add(By.id('confirmation'))
        waitUntilAnyOfTheElementIsDisplayed(listOfElements)
    }

    //Below method will wait until any of the element specified is displayed.
    public void waitUntilFinishGettingQuotes() {
        List<By> listOfElements = new ArrayList<By>();
        listOfElements.add(By.id("resultsPage"))
        listOfElements.add(By.xpath("//*[contains(text(),'No results found')]"))
        listOfElements.add(By.xpath("//*[contains(text(),'re sorry')]"))
        listOfElements.add(By.xpath("//*[contains(text(),'unable to get a quote')]"))
        listOfElements.add(By.xpath("//*[contains(text(),'we were unable to get a quote')]"))
        waitUntilAnyOfTheElementIsDisplayed(listOfElements)
    }

    boolean waitUntilAnyOfTheElementIsDisplayed(List<By> byList) {
        for (int i = 1; i <= 60; i++) {
            for (int j = 0; j < byList.size(); j++) {
                if (waitUntilElementIsDisplayed(byList.get(j), 1)) {
                    return true;
                }
            }
            sleep(1000)
        }
        return false
    }

    /*
    This method takes in a By parameter that returns a list of WebElements and assert that they don't exist on the page
     */

    void checkElementsDisappear(By by) {
        //driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)
        assert driver.findElements(by).isEmpty()
        //DriverUtil.settingImplicitWait()
    }

    public void waitUntilElementDisappear(By by) {
        for (int i = 0; i <= 60; i++) {
            if (waitUntilElementIsDisplayed(by,1))
                sleep(100)
            else
                break;
        }
    }

    public boolean checkElementExists(By by, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        try {
            WebElement element = wait.until(
                    ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception ex) {
            return false
        }
        return true
    }

    public boolean waitUntilElementIsDisplayed(By by) {
        waitUntilElementIsDisplayed(by, 60)
    }

    public boolean waitUntilTitleChanges(String title) {
        for (int i = 0; i < 60; i++) {
            if (driver.getTitle().toLowerCase().contains(title.toLowerCase()))
                return true
            else
                sleep(1000)
        }
        return false
    }

    boolean waitUntilValueIsNotEmpty(By by){

        for(int i=0;i<=10;i++) {
            String value = driver.findElement(by).getAttribute("value")
            if (value!=""){
                return true
            }
            sleep(500)
        }
        return false
    }

    public boolean waitUntilElementIsDisplayed(By by, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        try {
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception ex) {
            return false
        }
            return true
    }

    WebElement getElementWithId(String id) {
        return driver.findElement(By.id(id))
    }

    String getInnerHtml(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element)
    }

    String getInnerText(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText;", element)
    }

    def isDocumentReady() {
        String status = ((JavascriptExecutor) driver).executeScript("return document.readyState;")
        LOG.info("Document ready state " + status)
    }

    def scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element)
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-100)")
    }

    def scrollToTop() {
        sleep(1000)
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(100,100)")
        sleep(1000)
    }

    def getDateParts(Calendar date) {
        String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH))
        String month = String.valueOf(date.get(Calendar.MONTH) + 1)
        String year = String.valueOf(date.get(Calendar.YEAR))

        if (day.length() == 1) {
            day = "0" + day
        }
        if (month.length() == 1) {
            month = "0" + month
        }
        def dateParts = [day, month, year]
    }

    def setDate(String id, Calendar date) {

        def dateParts = getDateParts(date)

        //case for date with 3 control
        if (checkElementExists(By.xpath("//input[@id='" + id + "']//parent::div[contains(@class,'hidden')]"))) {
            setValueByJavaScript(driver.findElement(By.id(id + "D")), dateParts[0])
            setValueByJavaScript(driver.findElement(By.id(id + "M")), dateParts[1])
            setValueByJavaScript(driver.findElement(By.id(id + "Y")), dateParts[2])
            ((JavascriptExecutor) driver).executeScript(" var evt = document.createEvent('HTMLEvents'); evt.initEvent('change',true,true); arguments[0].dispatchEvent(evt);", driver.findElement(By.id(id + "Y")))
        } else {
            setValueByJavaScript(driver.findElement(By.id(id)), dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0])
            ((JavascriptExecutor) driver).executeScript(" var evt = document.createEvent('HTMLEvents'); evt.initEvent('change',true,true); arguments[0].dispatchEvent(evt);", driver.findElement(By.id(id)))
        }
    }

    def verifyErrorMessageDisplayed(WebElement webElement, String error) {
        assert webElement.getAttribute('class').contains('has-error')

        //new WebElement created for the error message
        WebElement errorMsg = driver.findElement(By.id(webElement.getAttribute('id') + '-error'))
        assert errorMsg.text.equals(error)
    }

    /*
    Returns true for iPhone and iPad (except iPad Pro)
     */

    boolean isScreenWidthSmall() {
        Long width = (Long) (((JavascriptExecutor) driver).executeScript("return window.screen.width;"));
        LOG.debug("Width of the device is -- " + width)
        //768 is the width for iPad 2 (5.0), iPad Mini, and iPad Air 2 for Appium
        //Width for iPad Pro is 1024
        //iPhone 6s wideth is 375
        if (width < 767)
            return true;
        else
            return false;
    }

    boolean isMobileMenuDisplayed() {
        return waitUntilElementIsDisplayed(By.xpath("//span[contains(@class,'icon-reorder')]"), 1)
    }

    def getFormattedAmount(String amount) {
        String digitalAmount = amount.replaceAll("[\$,]", "");
        return Double.parseDouble(digitalAmount)
    }

    def getBrowserName() {
        String agent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent");
        if (agent.toLowerCase().contains("chrome")) {
            return "Chrome"
        } else if (agent.toLowerCase().contains("firefox")) {
            return "Firefox"
        } else if (agent.toLowerCase().contains(".net")) {
            return "IE"
        } else if (agent.toLowerCase().contains("macintosh") || agent.toLowerCase().contains("safari")) {
            return "Safari"
        }

    }

    //Not returning a page object because we assume it has already previously been declared beforehand, hence the 'back button'
    def clickBackLink() {
        if (isScreenWidthSmall()) {
            driver.findElement(By.xpath("//span[contains(@class,'icon-reorder')]")).click()
        }
        clickByJavaScript(driver.findElement(By.linkText("Back")))
        sleep(1000)
    }


    def isTextDisplayedOnPage(String text) {
        driver.findElement(By.xpath("//span[contains(text(),'" + text + "')]")).displayed
    }

    def waitUntilDocumentStateIsComplete() {
        for (int i = 0; i <= 30; i++) {
            String state = ((JavascriptExecutor) driver).executeScript("return document.readyState;")
            if (state.equalsIgnoreCase("complete"))
                return
            else
                sleep(1000)
        }
    }

    def switchToWindowContainingTitle(String title) {
        int windowCount = driver.getWindowHandles().size()

        LOG.info("Window count " + windowCount)

        if (windowCount == 1) {
            LOG.info("Only one window is open...Switching to it -> " + driver.windowHandles[0])
            driver.switchTo().window(driver.windowHandles[0])
            return true
        }

        String currentWindowHandle = driver.getWindowHandle();
        LOG.info("Current Window Handle " + currentWindowHandle)

        //Switch to new window
        for (String handle in driver.windowHandles) {

            if (!handle.equalsIgnoreCase(currentWindowHandle)) {
                LOG.info("Switching to Window Handle " + handle)
                driver.switchTo().window(handle)
            }
        }
        return waitUntilTitleChanges(title)
    }

    def switchToWindowContainingPageSource(String source) {
        int windowCount = driver.getWindowHandles().size()

        LOG.info("Window count " + windowCount)

        if (windowCount == 1) {
            LOG.info("Only one window is open...Switching to it -> " + driver.windowHandles[0])
            driver.switchTo().window(driver.windowHandles[0])
            return true
        }

        String currentWindowHandle = driver.getWindowHandle();
        LOG.info("Current Window Handle " + currentWindowHandle)

        //Switch to new window
        for (String handle in driver.windowHandles) {

            if (!handle.equalsIgnoreCase(currentWindowHandle)) {
                LOG.info("Switching to Window Handle " + handle)
                driver.switchTo().window(handle)
            }
        }
        return driver.getPageSource().toLowerCase().contains(source)
    }


    def switchToNextWindow() {

        int windowCount = driver.getWindowHandles().size()

        LOG.info("Window count " + windowCount)

        if (windowCount == 1) {
            LOG.info("Only one window is open...Switching to it -> " + driver.windowHandles[0])
            driver.switchTo().window(driver.windowHandles[0])
            return true
        }

        String currentWindowHandle = driver.getWindowHandle();
        LOG.info("Current Window Handle " + currentWindowHandle)

        //Switch to new window
        for (String handle in driver.windowHandles) {

            if (!handle.equalsIgnoreCase(currentWindowHandle)) {
                LOG.info("Switching to Window Handle " + handle)
                driver.switchTo().window(handle)
            }
        }
    }


    def setAggregator() {

        if ((System.getenv("bamboo_aggregator") != null && System.getenv("bamboo_aggregator") != "")) {
            new Select(driver.findElement(By.xpath("//select[contains(@id,'Aggregator')]"))).selectByVisibleText(System.getenv("bamboo_aggregator"))
        }

        if ((System.getProperty("aggregator") != null && System.getProperty("aggregator") != "")) {
            new Select(driver.findElement(By.xpath("//select[contains(@id,'Aggregator')]"))).selectByVisibleText(System.getProperty("aggregator"))
        }

        if ((System.getenv("bamboo_application") != null && System.getenv("bamboo_application") != "")) {
            new Select(driver.findElement(By.xpath("//select[contains(@id,'Application')]"))).selectByVisibleText(System.getenv("bamboo_application"))
        }

        if ((System.getProperty("application") != null && System.getProperty("application") != "")) {
            new Select(driver.findElement(By.xpath("//select[contains(@id,'Application')]"))).selectByVisibleText(System.getProperty("application"))
        }

        //select[contains(@id,'Application')]
        //select[contains(@id,'Validator')]
        //select[contains(@id,'Branches')]
    }

    def checkProdEnvironment() {
        if ((System.getenv("bamboo_env") != null && System.getenv("bamboo_env").toUpperCase() == "PRD") || (System.getProperty("env") != null && System.getProperty("env").toUpperCase() == "PRD") || (System.getenv("bamboo_env") != null && System.getenv("bamboo_env").toUpperCase() == "PRELIVE") || (System.getProperty("env") != null && System.getProperty("env").toUpperCase() == "PRELIVE")) {
            println "Current bamboo environment is " + System.getenv("bamboo_env")
            println "Current environment is " + System.getProperty("env")
            return true
        } else {
            return false
        }
    }

    def waitUntilElementIsClickable(def webElement) {
        try {
            new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(webElement))
        } catch (Exception ex) {
            //ignore the exception coming for safari
        }
    }


    def selectDate(String elementId, String date) {
        def dateParts = getDateParts(CalendarUtil.calculateDate(date))
        new Select(driver.findElement(By.id(elementId + "D"))).selectByValue(dateParts[0])
        new Select(driver.findElement(By.id(elementId + "M"))).selectByValue(dateParts[1])
        new Select(driver.findElement(By.id(elementId + "Y"))).selectByVisibleText(dateParts[2])
    }

    def chooseFirstSuggestedSuburb(String code) {
        waitUntilElementIsDisplayed(By.xpath("//span[contains(text(),'" + code + "')]"), 10)
        clickByJavaScript(driver.findElement(By.xpath("//span[contains(text(),'" + code + "')]")))

    }

    def setEverestDate(String dateId, Calendar date) {
        def dateParts = getDateParts(date)
        driver.findElement(By.xpath("//input[@data-id='" + dateId + "day']")).sendKeys(dateParts[0])
        driver.findElement(By.xpath("//input[@data-id='" + dateId + "month']")).sendKeys(dateParts[1])
        driver.findElement(By.xpath("//input[@data-id='" + dateId + "year']")).sendKeys(dateParts[2])
    }


    def getMonthInMMMFormat(String number) {
        String month = "";
        switch (number) {
            case "01": month = "Jan"; break;
            case "02": month = "Feb"; break;
            case "03": month = "Mar"; break;
            case "04": month = "Apr"; break;
            case "05": month = "May"; break;
            case "06": month = "Jun"; break;
            case "07": month = "Jul"; break;
            case "08": month = "Aug"; break;
            case "09": month = "Sep"; break;
            case "10": month = "Oct"; break;
            case "11": month = "Nov"; break;
            case "12": month = "Dec"; break;
        }
        return month;
    }

    def selectEverestDate(String dateId, Calendar date) {
        def dateParts = getDateParts(date)
        if (String.valueOf(dateParts[0]).startsWith("0")) {
            dateParts[0] = String.valueOf(Integer.parseInt(dateParts[0]))
        }

        new Select(driver.findElement(By.xpath("//select[@data-id='" + dateId + "_day']"))).selectByVisibleText(dateParts[0])
        new Select(driver.findElement(By.xpath("//select[@data-id='" + dateId + "_month']"))).selectByIndex(Integer.parseInt(dateParts[1]))
        new Select(driver.findElement(By.xpath("//select[@data-id='" + dateId + "_year']"))).selectByVisibleText(dateParts[2])
    }
/*
    def selectEnergyEverestDate(String dateId, Calendar date) {
        def dateParts = getDateParts(date)
        if (String.valueOf(dateParts[0]).startsWith("0")) {
            dateParts[0] = String.valueOf(Integer.parseInt(dateParts[0]))
        }
        dateParts[1] = getMonthInMMMFormat(dateParts[1]).toLowerCase()
        new Select(driver.findElement(By.xpath("//select[@data-id='" + dateId + "_day']"))).selectByVisibleText(dateParts[0])
        new Select(driver.findElement(By.xpath("//select[@data-id='" + dateId + "_month']"))).selectByVisibleText(dateParts[1])
        new Select(driver.findElement(By.xpath("//select[@data-id='" + dateId + "_year']"))).selectByVisibleText(dateParts[2])
    }*/

    def setCookie() {
        String js = "var website_host = window.location.hostname.replace('www.', '');"
        js = js + "document.cookie = 'automated-test=true; path=/;domain=.' + website_host"
        ((JavascriptExecutor) driver).executeScript(js)
    }

    def verifyErrorMessageDisplayed(String error) {
        assert waitUntilElementIsDisplayed(By.xpath("//label[contains(text(),'" + error + "')]"), 5)
    }

    def getFirstMatch(String regex, String text) {
        Pattern pattern =
                Pattern.compile(regex);
        Matcher matcher =
                pattern.matcher(text);

        boolean found = false;
        String firstMatch = null;

        while (matcher.find()) {
            System.out.format("I found the text" +
                    " \"%s\" starting at " +
                    "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
            found = true;
            firstMatch = matcher.group(1)
            return firstMatch
        }

        if (!found) {
            System.out.println("Sorry, no match!");
            return null;
        }

    }

    def verifyRedErrorMessage(String errMessage) {
        assert waitUntilElementIsDisplayed(By.xpath("//label[contains(text(),\"" + errMessage + "\")]"), 5)
        assert driver.findElement(By.xpath("//label[contains(text(),\"" + errMessage + "\")]")).getAttribute("class").toLowerCase().contains("has-error")
    }

    def verifyRedErrorBorder(WebElement inputFiled) {
        inputFiled.getAttribute("class").toLowerCase().contains("has-error")
    }

    def verifyTextDisplayed(String text, int time) {
        waitUntilElementIsDisplayed(By.xpath("//*[contains(text(),'" + text + "')]"), time)
    }

    protected boolean isSelected(WebElement element) {
        (boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].checked;", element)
    }

    void fillAddressInWebCtm(String address) {
        sleep(1000)
        WebElement e = driver.findElement(By.xpath("//input[contains(@id,'streetSearch')]"))
        driver.findElement(By.xpath("//input[contains(@id,'streetSearch')]")).sendKeys(address)
        waitUntilElementIsDisplayed(By.xpath("//div[contains(@class,'autocomplete-suggestion')]"), 2)
        WebElement e1 = driver.findElement(By.xpath("//div[contains(@class,'autocomplete-suggestion')]"))
        //new Actions(driver).moveToElement(e).click().build().perform()
        e1.click()
        fireChangeEvent(e)
        fireBlurEvent(e)
    }

    def clickFirstSuggestion() {
        waitUntilElementIsDisplayed(By.xpath("//*[contains(@class,'suggestion')]"))
        driver.findElement(By.xpath("//*[contains(@class,'suggestion')]")).click()
    }
}
