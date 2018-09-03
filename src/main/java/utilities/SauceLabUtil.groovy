package utilities

import data.ProxySettings
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.SystemUtils
import org.openqa.selenium.remote.DesiredCapabilities
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by alitan on 2/02/2016.
 */
class SauceLabUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(DriverUtil.class);
    public static final String USERNAME = "ctmtesting";
    public static final String AUTOMATE_KEY = "6cbb220a-c1c6-46f0-aad2-466f568098c6";
    public static final String URL = "http://" + USERNAME + ":" + AUTOMATE_KEY + "@ondemand.saucelabs.com:80/wd/hub";
    public static String targetEnv = System.getProperty("env");

    public static DesiredCapabilities caps
    public static String bambooBuildNumber = System.getProperty("bambooBuildNumber");

    public static DesiredCapabilities sauceLabSetUp(String targetBrowser) {
        System.setProperty("http.proxyHost", ProxySettings.host);
        System.setProperty("http.proxyPort", ProxySettings.port);
        System.setProperty("http.proxyUser", ProxySettings.username);
        System.setProperty("http.proxyPassword", ProxySettings.password);

        String mobileDevice = System.getProperty("mobileDevice");
        String mobilePlatform = System.getProperty("mobilePlatform");
        String browserVersion = System.getProperty("browserVersion")

        //if we have mobileBrowser param, then this is going to be a tablet/mobile testing, else this is desktop testing
        if (mobilePlatform != "") {
            //mobile browser is either Android or iOS
            if (mobilePlatform.equalsIgnoreCase('iOS')) {
                if (mobileDevice.equals('iPhone 5')) {
                    caps = DesiredCapabilities.iphone();
                    caps.setCapability("deviceName", "iPhone 5");
                    caps.setCapability("deviceOrientation", "portrait");
                    caps.setCapability("platformVersion", "9.2");
                    caps.setCapability("browserName", "Safari");
                } else if (mobileDevice.equals('iPad 2')) {
                    //iPad 2 - available iOS from iOS 8.0 - 9.2
                    caps = DesiredCapabilities.ipad();
                    caps.setCapability("deviceName", "iPad 2");
                    caps.setCapability("platformVersion", "9.2");
                    caps.setCapability("browserName", "Safari");
                } else if (mobileDevice.equals('iPhone 6')) {
                    caps = DesiredCapabilities.iphone();
                    caps.setCapability("deviceName", "iPhone 6");
                    caps.setCapability("platformVersion", "9.2");
                    caps.setCapability("browserName", "Safari");
                } else if (mobileDevice.equals('iPhone 6 Plus')) {
                    caps = DesiredCapabilities.iphone();
                    caps.setCapability("deviceName", "iPhone 6 Plus");
                    caps.setCapability("platformVersion", "9.2");
                    caps.setCapability("browserName", "Safari");
                }
                caps.setCapability("platformName", "iOS");
                caps.setCapability("appiumVersion", "1.4.16");
                caps.setCapability("deviceOrientation", "portrait");
            } else if (mobilePlatform.equalsIgnoreCase('Android')) {
                caps = DesiredCapabilities.android();
                caps.setCapability("browserName", "Browser");
                caps.setCapability("platformName", "Android");

                if (mobileDevice.equals('Samsung Galaxy S4')) {
                    //Samsung Galaxy S4 emulator for Android 4.4 OS
                    caps.setCapability("deviceName", "Samsung Galaxy S4 Emulator");
                    caps.setCapability("platformVersion", "4.2");
                    caps.setCapability("automationName", "Selendroid");
                } else if (mobileDevice.equals('Android Emulator 4.4')) {
                    //Generic Android Emulator Phone on Android 4.4 OS
                    caps.setCapability("deviceName", "Android Emulator");
                    caps.setCapability("platformVersion", "4.4");
                    caps.setCapability("deviceType", "phone");
                    caps.setCapability("browserName", "Browser");
                } else if (mobileDevice.equals('Samsung Galaxy Note Emulator')) {
                    caps.setCapability("deviceName", "Samsung Galaxy Note Emulator");
                    caps.setCapability("platformVersion", "4.1");
                    caps.setCapability("automationName", "Selendroid");
                }
                caps.setCapability("appiumVersion", "1.5.0");
                caps.setCapability("deviceOrientation", "portrait");
            }

            if (bambooBuildNumber == null) {
                bambooBuildNumber = "0.1";
            }
            caps.setCapability("build", "Bamboo Build: " + bambooBuildNumber);
        } else {
            if (targetBrowser.equals('Chrome')) {
                caps = DesiredCapabilities.chrome();
                caps.setCapability("platform", "Windows 7");
                caps.setCapability("version", "48.0");
            } else if (targetBrowser.equals('IE')) {
                caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("platform", "Windows 7");

                if (browserVersion.equals("10.0")) {
                    caps.setCapability("version", "10.0");
                } else {
                    caps.setCapability("version", "11.0");
                }
            } else if (targetBrowser.equals('Firefox')) {
                caps = DesiredCapabilities.firefox();
                caps.setCapability("platform", "Windows 7");
                caps.setCapability("version", "40.0");
            } else if (targetBrowser.equals('Safari')) {
                //Safari 8.0 on Mac Yosemite
                caps = DesiredCapabilities.safari();
                caps.setCapability("platform", "OS X 10.10");
                caps.setCapability("version", "8.0");
            }
            caps.setCapability("screenResolution", "1680x1050");
//            caps.setCapability("seleniumVersion", "2.47.1");
        }

        if (targetEnv == null) {
            targetEnv = "";
        }

        //setting Sauce Connect Local Testing only for non-prod test environments in order to securely connect to our internal networks
        if (!targetEnv.equals("PRD")) {
            if (SystemUtils.IS_OS_LINUX) {
                //Connection from Bamboo through a Linux machine
                caps.setCapability("tunnelIdentifier", System.getProperty("localIdentifierLinuxServer"));
            } else if (SystemUtils.IS_OS_WINDOWS) {
                //Connection locally from Windows 7
                caps.setCapability("tunnelIdentifier", System.getProperty("localIdentifierWindows"));
            }
        }

        if (SystemUtils.IS_OS_LINUX) {
            caps.setCapability("name", "subsetAllVerticals" + "_" + System.getProperty("user.name"));
            if (bambooBuildNumber == null) {
                bambooBuildNumber = "0.1";
            }
            caps.setCapability("build", "Bamboo Build: " + bambooBuildNumber);

        } else if (SystemUtils.IS_OS_WINDOWS) {
            caps.setCapability("name", "subsetAllVerticals" + "_" + System.getProperty("user.name"));
            String localBuildNumber = System.getProperty("localBuildNumber");
            if (localBuildNumber == null) {
                localBuildNumber = "0.2";
            }
            caps.setCapability("build", "Windows7_local Build: " + localBuildNumber);
        }
        return caps;
    }

    static void startUpSauceConnect() {

        LOG.info("---Getting ready to start up Sauce Connect---");
        if (checkNoCurrentSauceConnectIsRunning()) {
            LOG.info("Confirms that there is currently no Sauce Connect running");
        } else {
            LOG.info("ERROR - There is some Sauce Connect process running before execute command to start it up");
        }

        String fileLocation;
        String sauceConnectCommandLineParameters = "-u ctmtesting -k 6cbb220a-c1c6-46f0-aad2-466f568098c6 --proxy 192.168.130.20:8080 --proxy-userpwd ittest:1tt3st --pac http://autoproxy/FV_proxy.pac -i";
        Process proc;

        if (SystemUtils.IS_OS_LINUX) {
            fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/sauce_connect/linux-64/sc-4.3.13-linux/bin/sc";
            try {
                LOG.info("Current Sauce Connect file permission is: ")

                String[] lsCmd = ["/bin/bash", "-c", "ls -al " + fileLocation + "*"]
                executeCommandAndPrintOutput(lsCmd)

                String[] chmodCmd = ["/bin/bash", "-c", "chmod +x " + fileLocation]
                Runtime.getRuntime().exec(chmodCmd)
                LOG.info("Made Sauce Connect executable for Linux");
                LOG.info("Sleeping for 1000ms..");
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                LOG.info("Sauce Connect file permission after chmod +x is: ")

                String[] lsCmd = ["/bin/bash", "-c", "ls -al " + fileLocation + "*"]
                executeCommandAndPrintOutput(lsCmd);
                proc = Runtime.getRuntime().exec(fileLocation + " " + sauceConnectCommandLineParameters + System.getProperty("localIdentifierLinuxServer"));
                LOG.info("Launching Sauce Connect for Linux");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (SystemUtils.IS_OS_WINDOWS) {
            fileLocation = DriverUtil.getModuleBaseDir() + "\\binaries\\sauce_connect\\windows\\sc-4.3.13-win32\\bin\\sc.exe";
            try {
                proc = Runtime.getRuntime().exec(fileLocation + " " + sauceConnectCommandLineParameters + System.getProperty("localIdentifierWindows"));
                LOG.info("Launching Sauce Connect for Windows");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //Reading Sauce Connect inputs
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            System.out.println("Here is the output of starting up Sauce Connect:\n");
            String output = null;

            long start = System.currentTimeMillis();
            long end = start + 60 * 1000; // 60 seconds * 1000 ms/sec

            //will wait for up to 60 seconds
            while ((output = stdInput.readLine()) != null && !output.contains('Sauce Connect is up, you may start your tests')) {
                System.out.println(output);
                if (System.currentTimeMillis() > end) {
                    System.out.println("It has taken more than 60 seconds and Sauce Lab is still not connected. Terminating..")
                    break
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (verifySauceConnectIsRunning()) {
            LOG.info("Success - Confirms Sauce Connect process has been started correctly");
        } else {
            LOG.info("ERROR - Sauce Connect process has not been started correctly!");
        }
    }

    static void shutDownSauceConnect() {

        LOG.info("---Getting ready to shut down Sauce Connect---");

        if (verifySauceConnectIsRunning()) {
            LOG.info("Confirms Sauce Connect process is still running");
        } else {
            LOG.info("ERROR - Sauce Connect process has already ended before executing command to shut it down!");
        }

        if (SystemUtils.IS_OS_LINUX) {
            try {
                Runtime.getRuntime().exec("/usr/bin/killall sc");
                LOG.info("Shut down Sauce Connect for Linux");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (SystemUtils.IS_OS_WINDOWS) {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM sc.exe");
                LOG.info("Shut down Sauce Connect for Windows");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Wait for 10 seconds before verifying Sauce Connect has been shutdown correctly
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            LOG.error(e.toString());
        }

        if (checkNoCurrentSauceConnectIsRunning()) {
            LOG.info("SUCCESS - confirms Sauce Connect has been shut down correctly");
        } else {
            LOG.info("ERROR - Sauce Connect binary is still running after executing command to shut it down");
        }
    }

    public static String executeCommandAndPrintOutput(String[] cmd) {
        StringBuffer output = new StringBuffer();
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            List<String> result = IOUtils.readLines(p.getInputStream());
            for (String line : result) {
                System.out.println(line);
                output.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public static boolean checkNoCurrentSauceConnectIsRunning() {
        String processOutput = checkingSauceConnectProcess();
        if (processOutput.contains("sc")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean verifySauceConnectIsRunning() {
        String processOutput = checkingSauceConnectProcess();
        if (processOutput.contains("sc")) {
            return true;
        } else {
            return false;
        }
    }

    public static String checkingSauceConnectProcess() {

        String[] command = ""

        if (SystemUtils.IS_OS_LINUX) {
            command = ["/bin/bash", "-c", " ps -fu`whoami`|grep sc |grep -v grep"]
        } else if (SystemUtils.IS_OS_WINDOWS) {
            command = ["bash", "-c", "tasklist | grep sc"]
        }

        LOG.info("Checking current running processes with command: " + command)
        return executeCommandAndPrintOutput(command)
    }
}