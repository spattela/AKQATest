package steps

import org.openqa.selenium.JavascriptExecutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utilities.*

import java.text.SimpleDateFormat

import static cucumber.api.groovy.Hooks.After
import static cucumber.api.groovy.Hooks.Before

Before { scenario ->
    Logger LOG = LoggerFactory.getLogger(this.class);
    LOG.info("Current user is " + System.getProperty("user.name"))
    LOG.info("Running Scenario -- " + scenario.name)
    System.setProperty("ScenarioName", scenario.name)

    LOG.info("Current time zone is " + TimeZone.getDefault())
    //Set default time zone
    LOG.info("ScenarioStarted in UTC zone - " + CalendarUtil.convertDateToDBFormat(new Date()));

    TimeZone.setDefault(TimeZone.getTimeZone("Australia/Brisbane"));

    LOG.info("New time zone is " + TimeZone.getDefault())

    LOG.info("ScenarioStarted in brisbane timezone - " + CalendarUtil.convertDateToDBFormat(new Date()));

    System.setProperty("ScenarioStarted", CalendarUtil.convertDateToDBFormat(new Date()));

    if (System.getenv("bamboo_planKey") != null) {
        planKey = System.getenv("bamboo_planKey");
        LOG.info("Plan Key is " + planKey)
    }
}

After { scenario ->
    Logger LOG = LoggerFactory.getLogger(this.class);
    String transactionID = "";

    try {
        System.out.printf("%n")
        scenario.write("Current time stamp -> " + new Date().toString())

        String title = DriverUtil.driver.getTitle()
        scenario.write("Title of the page -> " + title)


    } catch (Exception ex) {
        //ignore errors for LI and IP
    }

    String s1 = System.getProperty("ScenarioStarted")
    String s2 = "";
    if (System.getProperty("ScenarioEnded"))
        s2 = System.getProperty("ScenarioEnded")
    else {
        Calendar cal = Calendar.getInstance();
        s2 = CalendarUtil.convertDateToDBFormat(cal.getTime());
    }

    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date1 = null;
    Date date2 = null;

    System.out.println("Scenario Started " + s1);
    System.out.println("Scenario Ended " + s2);
    try {
        date1 = formatter1.parse(s1);
        date2 = formatter1.parse(s2);
    } catch (Exception ex) {
    }

    //in milliseconds
    long diff = date2.getTime() - date1.getTime();
    long diffSeconds = ((long) (diff / 1000)) % 60;
    long diffMinutes = ((long) (diff / (60 * 1000))) % 60;
    scenario.write("Scenario duration - " + diffMinutes + " min " + diffSeconds + " seconds")
    LOG.info("Scenario duration - " + diffMinutes + " min " + diffSeconds + " seconds")

    if (scenario.status == "passed")
        LOG.info("*****" + scenario.name + " Scenario Passed*****")
    else
        LOG.info("*****" + scenario.name + " Scenario Failed*****")
    DriverUtil.destroyWebDriver()
}
