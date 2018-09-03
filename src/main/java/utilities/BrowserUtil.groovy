package utilities

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

/**
 * Created by alitan on 27/05/2015.
 */
class BrowserUtil {

    static void scrollToYCoordinate(WebDriver driver, String y_coordinate) {
        sleep(1000)
        JavascriptExecutor jsx = (JavascriptExecutor) driver
        jsx.executeScript("scroll(0," + y_coordinate + ")")
    }

    static void addingPause() {
        String pauseByMinutesParam = System.getProperty("pauseByMinutes")
        def pauseBySecondsParam = System.getProperty("pauseBySeconds")

        if (pauseByMinutesParam != null) {
            Long pauseByMilliseconds = Long.parseLong(pauseByMinutesParam) * 60 * 1000
            if (pauseByMilliseconds > 0) {
                sleep(pauseByMilliseconds)
            }
        }

        if (pauseBySecondsParam != null) {
            Long pauseByMilliseconds = Long.parseLong(pauseBySecondsParam) * 1000
            if (pauseByMilliseconds > 0) {
                sleep(pauseByMilliseconds)
            }
        }
    }

//    static void captureScreenshot(WebDriver driver) {
//        //Taking a screenshot here
//        try {
//            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//            FileUtils.copyFile(scrFile, new File(DriverUtil.getModuleBaseDir() + "/target/test-cucumber-screenshots/screenshot1.png"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    static void embedScreenshot(def cucumberStep, WebDriver driver) {
        def screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)
        cucumberStep.embed(screenshot, 'image/png')
    }

    static void switchBrowserWindow(WebDriver driver) {
        Set<String> allWindows = driver.getWindowHandles()
        if (!allWindows.isEmpty()) {
            for (String windowId : allWindows) {
                driver.switchTo().window(windowId)
            }
        }
    }

    static void isHandoverPageLoaded(WebDriver driver, String insurer) {

        switchBrowserWindow(driver)

        (new WebDriverWait(driver, 60)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
//                return !driver.getTitle().contains('Transferring')
                return driver.getTitle().contains(insurer)

            }
        })
        assert driver.getTitle().contains(insurer)
    }

    static String getPageSourceHTML(WebDriver driver) {
        return driver.getPageSource()
    }
}
