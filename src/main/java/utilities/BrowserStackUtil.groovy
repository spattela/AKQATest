package utilities

import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.SystemUtils
import org.openqa.selenium.remote.DesiredCapabilities
import org.slf4j.Logger
import org.slf4j.LoggerFactory
/**`
 * Created by alitan on 2/02/2016.
 */
class BrowserStackUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(DriverUtil.class);
    public static final String USERNAME = "sagar297";
    public static final String AUTOMATE_KEY = "5sssdwsRu86stxJpgLy9";
    public static final String URL = "http://" + USERNAME + ":" + AUTOMATE_KEY + "@hub.browserstack.com/wd/hub";

    public static String targetEnv = System.getProperty("env");
    public static String bambooBuildNumber = System.getProperty("bambooBuildNumber");


    public static DesiredCapabilities browserStackSetUp(String targetBrowser) {
        DesiredCapabilities caps = new DesiredCapabilities();

        if (targetBrowser.toLowerCase().equalsIgnoreCase("chrome") && System.getProperty("mobileDevice") == null) {
            caps.setCapability("browserName", targetBrowser.toLowerCase());
            caps.setCapability("os", "WINDOWS");
            caps.setCapability("version", "55");
            caps.setCapability("os_version", "7");
        }

        if (targetBrowser.toLowerCase().equalsIgnoreCase("ie")) {
            caps.setCapability("browserName", targetBrowser.toLowerCase());
            caps.setCapability("os", "WINDOWS");
            caps.setCapability("os_version", "10");
        }

        if (targetBrowser.toLowerCase().equalsIgnoreCase("edge")) {
            caps.setCapability("browserName", targetBrowser.toLowerCase());
            caps.setCapability("version", "14");
            caps.setCapability("os", "WINDOWS");
            caps.setCapability("os_version", "10");
        }

        if (targetBrowser.toLowerCase().equalsIgnoreCase("firefox47")) {
            caps.setCapability("browserName", "firefox");
            caps.setCapability("version", "47");
            caps.setCapability("os", "WINDOWS");
            caps.setCapability("os_version", "10");
        }

        if (targetBrowser.toLowerCase().equalsIgnoreCase("safari")) {
            caps.setCapability("browserName", targetBrowser.toLowerCase());
            caps.setCapability("platform", "MAC");
            caps.setCapability("version", "9.1");

        }

        LOG.info("mobile device name is -> " + System.getProperty("mobileDevice"))
        if (System.getProperty("mobileDevice") != null && System.getProperty("mobileDevice").equalsIgnoreCase("galaxys5")) {
            LOG.info("Setting capabilities for Android Samsung Galaxy S5")
            caps.setCapability("browserName", "chrome");
            caps.setCapability("platform", "ANDROID");
            caps.setCapability("device", "SAMSUNG GALAXY S5");
        }

        if (System.getProperty("mobileDevice") != null && System.getProperty("mobileDevice").equalsIgnoreCase("iphone6s")) {
            LOG.info("Setting capabilities for iPhone 6s")
            caps.setCapability("platform", "MAC");
            caps.setCapability("browserName", "iPhone");
            caps.setCapability("device", "iPhone 6s");
        }


        if (System.getProperty("mobileDevice") != null && System.getProperty("mobileDevice").equalsIgnoreCase("iphone7")) {
            LOG.info("Setting capabilities for iPhone 7")


            caps.setCapability("browserName", "iPhone");
            caps.setCapability("device", "iPhone 7");
            caps.setCapability("realMobile", "true");
            caps.setCapability("os_version", "10.3");
        }

        if (System.getProperty("mobileDevice") != null && System.getProperty("mobileDevice").equalsIgnoreCase("ipad5")) {
            LOG.info("Setting capabilities for iPad 5th Gen")
            caps.setCapability("os_version", "11.0");
            caps.setCapability("device", "iPad 5th");
            caps.setCapability("real_mobile", "true")
            caps.setCapability("device", "iPad 5th");
            caps.setCapability("browserstack.debug","true")

        }

        if (!PropertiesHelper.checkProdEnvironment())
            caps.setCapability("browserstack.local", "true");

        caps.setCapability("project", "CTM - " + System.getenv("bamboo_shortPlanName"));

        if (System.getenv("bamboo_buildNumber") == null) {
            bambooBuildNumber = "0.1";
        } else {
            bambooBuildNumber = System.getenv("bamboo_buildNumber");
        }
        caps.setCapability("build", "Bamboo Build: " + bambooBuildNumber);

        Map map = caps.asMap();
        Set keys = caps.asMap().keySet();

        println("Printing all capabilities");
        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = (String) map.get(key);
            println(key + " = " + value);
        }

        return caps;
    }

    public static void startUpBrowserStackLocalTestingBinary() {

        if (checkNoCurrentBrowserStackBinaryIsRunning()) {
            LOG.info("Confirms that there is currently no BrowserStack binary running");
        } else {
            LOG.info("There is some BrowserStack binary process running before execute command to start it up");
            ProcessUtil.kill("BrowserStackLocal")
        }

        String fileLocation;
        String browserStackCommandLineParameters = "--key " + AUTOMATE_KEY + "  --force-local";

        if (SystemUtils.IS_OS_LINUX) {
            fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/browserstack/linux-x64/BrowserStackLocal";
            try {
                LOG.info("Current BrowserStack binary file permission is: ")

                String[] lsCmd = ["/bin/bash", "-c", "ls -al " + fileLocation + "*"]
                LinuxUtil.executeCommandAndPrintOutput(lsCmd)

                String[] chmodCmd = ["/bin/bash", "-c", "chmod +x " + fileLocation]
                Runtime.getRuntime().exec(chmodCmd)
                LOG.info("Made BrowserStack binary executable for Linux");
                LOG.info("Sleeping for 1000ms..");
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                LOG.info("BrowserStack binary file permission after chmod +x is: ")
                String[] lsCmd = ["/bin/bash", "-c", "ls -al " + fileLocation + "*"]
                LinuxUtil.executeCommandAndPrintOutput(lsCmd);
                Runtime.getRuntime().exec(fileLocation + " " + browserStackCommandLineParameters);
                LOG.info("Launching BrowserStack Local Testing binary for Linux");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (SystemUtils.IS_OS_WINDOWS) {
            fileLocation = DriverUtil.getModuleBaseDir() + "\\binaries\\browserstack\\windows\\BrowserStackLocal.exe";
            try {

                LOG.info("Launching BrowserStack Local Testing binary for Windows");
                try {
                    Process p = Runtime.getRuntime().exec(fileLocation + " " + browserStackCommandLineParameters);
                    List<String> result = IOUtils.readLines(p.getInputStream());
                    for (String line : result) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Putting in a sleep of ten seconds to allow enough time for BrowserStack Local Testing binary to start up
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            LOG.error(e.toString());
        }

        if (verifyBrowserStackBinaryIsRunning()) {
            LOG.info("Success - Confirms Browserstack binary process has been started correctly");
        } else {
            LOG.info("ERROR - BrowserStack binary process has not been started correctly!");
            //wait for another 60 seconds for BrowserStack Binary to startup
            for (int i = 0; i <= 60; i++) {
                if (!verifyBrowserStackBinaryIsRunning()) {
                    LOG.info("Waiting for BrowserStack binary to start up. Attempt " + i)
                    sleep(1000)
                } else {
                    LOG.info("After 60 attempts, BrowserStack still hasn't started up.")
                    break;
                }
            }
        }
    }

    public static String checkingBrowserStackProcess() {

        String[] command = ""

        if (SystemUtils.IS_OS_LINUX) {
            command = ["/bin/bash", "-c", " ps -fu`whoami`|grep BrowserStackLocal|grep -v grep"]
        } else if (SystemUtils.IS_OS_WINDOWS) {
            command = ["bash", "-c", "tasklist | grep BrowserStackLocal"]
        }

        LOG.info("Checking current running processes with command: " + command)
        return LinuxUtil.executeCommandAndPrintOutput(command)
    }


    public static boolean checkNoCurrentBrowserStackBinaryIsRunning() {
        String processOutput = checkingBrowserStackProcess();
        LOG.info(processOutput)
        if (processOutput.contains("BrowserStack")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean verifyBrowserStackBinaryIsRunning() {
        String processOutput = checkingBrowserStackProcess();

        if (processOutput.contains("BrowserStack")) {
            return true;
        } else {
            return false;
        }
    }

    public static void shutDownBrowserStackLocalTestingBinary() {

        if (verifyBrowserStackBinaryIsRunning()) {
            LOG.info("Confirms Browserstack binary process is still running");
        } else {
            LOG.info("ERROR - BrowserStack binary process has already ended before executing command to shut it down!");
        }

        if (SystemUtils.IS_OS_LINUX) {
            try {
                Runtime.getRuntime().exec("/usr/bin/killall BrowserStackLocal");
                LOG.info("Shut down BrowserStack Local Testing binary for Linux");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (SystemUtils.IS_OS_WINDOWS) {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM BrowserstackLocal.exe");
                LOG.info("Shut down BrowserStack Local Testing binary for Windows");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Wait for 10 seconds before verifying BrowserStack binary has been shutdown correctly
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            LOG.error(e.toString());
        }

        if (checkNoCurrentBrowserStackBinaryIsRunning()) {
            LOG.info("SUCCESS - confirms BrowserStack binary has been shut down correctly");
        } else {
            LOG.info("ERROR - BrowserStack binary is still running after executing command to shut it down");
        }
    }
}