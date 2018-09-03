package utilities

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period

class CalendarUtil {

    static Calendar futureCalendarDate
    static def yyyy, mm, dd

    /*date input is in the format of "today +10"
     /return date in the format of '2015-11-05'
    */

    static String getFormattedFutureDate(String date) {
        futureCalendarDate = futureDate(date)
        dd = (String) futureCalendarDate.get(Calendar.DATE)
        if (dd.length() < 2) {
            dd = '0' + dd
        }
        //note mm is an int and always starts with 0, ie month of May is "4"
        mm = (String) futureCalendarDate.get(Calendar.MONTH) + 1
        if (mm.length() < 2) {
            mm = '0' + mm
        }
        yyyy = futureCalendarDate.get(Calendar.YEAR)
        return yyyy + '-' + mm + '-' + dd
    }

    static Calendar dateSelector(List<WebElement> calendarDates, String date, WebDriver driver) {
        //monthYear WebElement returns the current calendar month year on the page, eg 'April 2015'
        String currentCalMonth = driver.findElement(By.cssSelector('.datepicker-switch')).text.split(" ")[0]
        String currentCalYear = driver.findElement(By.cssSelector('.datepicker-switch')).text.split(" ")[1]

        if (date.contains('today')) {
            //if date passed in from feature file is in the format of 'today +5'
            futureCalendarDate = futureDate(date)
            dd = futureCalendarDate.get(Calendar.DATE)
            //note mm is an int and always starts with 0, ie month of May is "4"
            mm = futureCalendarDate.get(Calendar.MONTH)
            yyyy = futureCalendarDate.get(Calendar.YEAR)
        } else {
            //if date passed in is '2015-06-30'
            yyyy = Integer.parseInt(date.split("-")[0])
            mm = Integer.parseInt(date.split("-")[1]) - 1
            dd = Integer.parseInt(date.split("-")[2])
            futureCalendarDate = new GregorianCalendar(yyyy, mm, dd)
        }

        //Convert the future month into string text
        String monthInString = new DateFormatSymbols().getMonths()[mm]
        String yearInString = Integer.toString(yyyy)

        if (isDualMonthCalendar(driver)) {
            //navigating the dual month calendar
            while (!monthInString.equals(getCurrentCalendarMonth(driver)) || !yearInString.equals(getCurrentCalendarYear(driver))) {
                driver.findElements(By.cssSelector('.next'))[1].click()
            }

            for (WebElement element : driver.findElement(By.cssSelector('.month0')).findElements(By.cssSelector('.day'))) {
                if (!element.getAttribute("class").contains("disabled") && !element.getAttribute("class").contains("old")) {
                    if (Integer.parseInt(element.getText()) == dd) {
                        element.click()
                        break
                    }
                }
            }
        } else {
            //navigating single month calendar
            while (!monthInString.equals(currentCalMonth) || !yearInString.equals(currentCalYear)) {
                driver.findElement(By.cssSelector('.next')).click()
                String updateCurrentCalMonth = driver.findElement(By.cssSelector('.datepicker-switch')).getText().split(" ")[0]
                currentCalMonth = updateCurrentCalMonth

                String updateCurrentCalYear = driver.findElement(By.cssSelector('.datepicker-switch')).getText().split(" ")[1]
                currentCalYear = updateCurrentCalYear
            }

            for (WebElement element : calendarDates) {
                //Ignore calendar dates that are 'greyed out', these have the class attribute 'disabled'
                if (!element.getAttribute("class").contains("disabled") && !element.getAttribute("class").contains("old")) {
                    if (Integer.parseInt(element.getText()) == dd) {
                        element.click()
                        break
                    }
                }
            }
        }
        futureCalendarDate
    }

    static boolean isDualMonthCalendar(WebDriver driver) {
        //Check if calendar has dual calendar month showing. Currently only travel has dual calendar
        List<WebElement> calendarMonthsDisplayed = driver.findElements(By.cssSelector('.datepicker-switch'))
        int numberOfMonthsDisplayed = 0
        boolean dualMonthCalendar = false
        calendarMonthsDisplayed.each { month ->
            if (month.displayed) {
                numberOfMonthsDisplayed++
            }
        }
        if (numberOfMonthsDisplayed == 2) {
            dualMonthCalendar = true
        }
        return dualMonthCalendar
    }

    static String getNextCalendarMonth(WebDriver driver) {
        return driver.findElements(By.cssSelector('.datepicker-switch'))[1].text.split(" ")[0]
    }

    static String getCurrentCalendarMonth(WebDriver driver) {
        return driver.findElement(By.cssSelector('.datepicker-switch')).getText().split(" ")[0]
    }

    static String getCurrentCalendarYear(WebDriver driver) {
        return driver.findElement(By.cssSelector('.datepicker-switch')).getText().split(" ")[1]
    }

    static Calendar futureDate(String date) {

        int daysAhead = Integer.parseInt(date.split(" ")[1].substring(1))
        Calendar futureCal = Calendar.getInstance()
        futureCal.add(Calendar.DATE, daysAhead)
        futureCal
    }

    //returns the age of a person given a dob
    static int ageCalculator(String dobYYYY, dobMM, dobDD) {

        LocalDate today = LocalDate.now()
        LocalDate birthday = LocalDate.of(Integer.parseInt(dobYYYY), Integer.parseInt(dobMM), Integer.parseInt(dobDD))
        Period p = Period.between(birthday, today)
        return p.getYears()
    }

    static Calendar calculateDate(String date) {

        if (date.contains("/")) {
            List<String> dob = new ArrayList<String>()

            for (String ddmmyy : date.split("/")) {
                dob.add(ddmmyy)
            }

            int day = Integer.parseInt(dob[0]);
            int month = Integer.parseInt(dob[1]) - 1;
            int year = Integer.parseInt(dob[2]);

            Calendar newDate = Calendar.getInstance();
            newDate.set(year, month, day, 0, 0);
            return newDate;
        }

        int durationToBeAddedOrSubtracted;
        Calendar newDate = Calendar.getInstance()


        if (date.contains("today")) {
            //today -10
            if (date.trim().equalsIgnoreCase("today"))
                durationToBeAddedOrSubtracted = 0
            else
                durationToBeAddedOrSubtracted = Integer.parseInt(date.split(" ")[1])
        } else {
            //-30 years
            durationToBeAddedOrSubtracted = Integer.parseInt(date.split(" ")[0])
        }

        if (date.toLowerCase().contains("months")) {
            newDate.add(Calendar.MONTH, durationToBeAddedOrSubtracted)
        } else if (date.toLowerCase().contains("years")) {
            newDate.add(Calendar.YEAR, durationToBeAddedOrSubtracted)
        } else {
            newDate.add(Calendar.DATE, durationToBeAddedOrSubtracted)
        }

        return newDate
    }

    //This method returns the future date that is x number of days from today in the format of "yyyy/MM/dd HH:mm:ss"
    static String getFutureDate(int days) {
        Calendar now = Calendar.getInstance()
        GregorianCalendar gregorianCalendar = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE))
        gregorianCalendar.add(GregorianCalendar.DATE, days)
        Date date = gregorianCalendar.getTime()
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        String futureDate = dateFormat.format(date)
        return futureDate
    }

    public static String convertDateToDBFormat(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }
}
