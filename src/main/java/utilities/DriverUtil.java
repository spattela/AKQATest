package utilities;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.ProxyingRemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DriverUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(DriverUtil.class);
    public static WebDriver driver;
    public static final String FIREFOX_PROPERTIES = "firefox.properties";
    public static final String MODIFY_HEADERS_EXTENSION = "modify_headers-0.7.1.1-fx.xpi";
    public static String targetHost = System.getProperty("targetHost");
    protected static DesiredCapabilities sCaps;
    public static String targetEnv = System.getProperty("env");
    public static String device = System.getProperty("device");
    public static String browser = System.getProperty("browser");

    public static WebDriver createWebDriver() {
        //backup the report directory for vertical



        if (System.getProperty("targetHost") == null) {
            System.setProperty("targetHost", "localHost");
            targetHost = "localHost";
        }

        if (System.getProperty("targetHost") != null && System.getProperty("targetHost").equalsIgnoreCase("BrowserStack")) {
            if (System.getProperty("driverLaunched") != null && System.getProperty("driverLaunched").equalsIgnoreCase("true")) {
                return driver;
            }
        }

        if (targetHost.equalsIgnoreCase("BrowserStack")) {
            if (System.getProperty("driverLaunched")!=null && System.getProperty("driverLaunched").equalsIgnoreCase("true")){
                return driver;
            }
        }

        try {
            if (System.getenv("bamboo_planName") != null) {
                killOrphanProcesses();
            }

            setDefaultValues();
            LOG.info("Starting browser -> " + browser);
            if (targetHost.equalsIgnoreCase("BrowserStack")) {
                invokeBrowserStackDriver(browser);
            } else if (targetHost.equalsIgnoreCase("SauceLab")) {
                invokeSauceLabsDriver(browser);

            } else {
                invokeLocalDriver(browser);
            }
            settingImplicitWait();
            printBrowserInfo();

            if (System.getProperty("ApplicationDateTesting") != null) {
                try {
                    DriverUtil.driver.get(URLReader.getURL("ctm.data"));
//                    CTMDataPage ctmDataPage = new CTMDataPage(DriverUtil.driver);
//                    ctmDataPage.enterApplicationDate(7);
//                    ctmDataPage.verifyDateSuccess();
                } catch (IOException ex) {
                    ex.printStackTrace(System.out);
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception occurred while starting the browser - > " + ex.getMessage());
            if (ex.getMessage().toLowerCase().contains("local testing through BrowserStack is not connected")) {
                System.out.println("Starting the local binary");
                BrowserStackUtil.startUpBrowserStackLocalTestingBinary();
            }

            if (ex.getMessage().toLowerCase().contains("chrome failed to start") || ex.getMessage().toLowerCase().contains("chrome not reachable")) {
                System.out.println("Rebooting the machine");
                String[] cmd = {"/bin/bash", "-c", "sudo reboot"};
                LinuxUtil.executeCommandAndPrintOutput(cmd);
            }

            System.out.println("Printing stack trace");
            ex.printStackTrace(System.out);
        }

        System.setProperty("driverLaunched", "true");
        return driver;
    }


    private static void copyFiles(String source, String desti) {
        //copy car directory to backup directory
        if (!new File(source).exists()){
            return;
        }
        File srcDir = new File(source);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss");
        String destination = desti + sdf.format(timestamp);
        File destDir = new File(destination);
        copyDirs(srcDir, destDir);
    }

    private static void copyDirs(File srcDir, File destDir) {
        try {
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

    private static void printBrowserInfo() {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        String os = cap.getPlatform().toString();
        String v = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent");
        LOG.info("Running tests on " + browserName + " - " + v + " and OS is " + os);
    }

    private static void setDefaultValues() {

        if (System.getProperty("mobilePlatform") == null)
            System.setProperty("mobilePlatform", "");

        if (System.getProperty("mobileBrowser") == null)
            System.setProperty("mobileBrowser", "");

        if (System.getProperty("browserVersion") == null)
            System.setProperty("browserVersion", "");

        if (targetHost == null) {
            System.setProperty("targetHost", "");
            targetHost = "local";
        }

        if (targetEnv == null) {
            targetEnv = "";
        }

        if (device == null) {
            device = "";
        }

        if (browser == null) {
            browser = "chrome";
            System.setProperty("browser", browser);
        }
    }

    private static void invokeLocalDriver(String browser) {

        if (browser.equalsIgnoreCase("firefox")) {
            if (SystemUtils.IS_OS_LINUX) {
                String fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/browsers/";
                fileLocation += "firefox_linux/geckodriver";
                LOG.info("Set geckodriver for Linux");
                System.setProperty("webdriver.gecko.driver", fileLocation);
                try {
                    Runtime.getRuntime().exec("chmod +x " + fileLocation);
                    LOG.info("Made geckodriver executable for Linux");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (SystemUtils.IS_OS_WINDOWS) {
                String fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/browsers/";
                fileLocation += "firefox_windows/geckodriver.exe";
                System.setProperty("webdriver.gecko.driver", fileLocation);
                FirefoxBinary firefoxBinary = new FirefoxBinary();
                firefoxBinary.addCommandLineOptions("--headless");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setBinary(firefoxBinary);
                driver = new FirefoxDriver(firefoxOptions);
//                driver = new FirefoxDriver();


            } else {
                String fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/browsers/";
                fileLocation += "firefox_mac/geckodriver";
                LOG.info("Set geckodriver for Mac");
                System.setProperty("webdriver.gecko.driver", fileLocation);
                FirefoxBinary firefoxBinary = new FirefoxBinary();
                firefoxBinary.addCommandLineOptions("--headless");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setBinary(firefoxBinary);
                driver = new FirefoxDriver(firefoxOptions);

                try {
                    Runtime.getRuntime().exec("chmod +x " + fileLocation);
                    LOG.info("Made geckodriver executable for Mac");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } else if (browser.equalsIgnoreCase("IE")) {
            //System.getProperty("java.io.tmpdir","C:\\Users\\ctm_qa\\");
            String fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/browsers/IE_32bit/IEDriverServer.exe";
            System.out.println("Launching the IE driver " + fileLocation);
            System.setProperty("webdriver.ie.driver", fileLocation);
            System.out.println("Starting IEDriver");
            driver = new InternetExplorerDriver();
            System.out.println("IEDriver started");
            // ((JavascriptExecutor) driver).executeScript("window.focus();");
        } else if (browser.equalsIgnoreCase("edge")) {
            //System.getProperty("java.io.tmpdir","C:\\Users\\ctm_qa\\");
            String fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/browsers/edge/MicrosoftWebDriver.exe";
            System.out.println("Launching the Edge driver " + fileLocation);
            System.setProperty("webdriver.edge.driver", fileLocation);
            System.out.println("Starting Edge Driver");
            driver = new EdgeDriver();
            System.out.println("Edge Driver started");
        } else if (browser.equalsIgnoreCase("Safari")) {
            System.setProperty("mobilePlatform", "true");
            driver = new SafariDriver();
        } else if (browser.equalsIgnoreCase("phantomjs")) {
            sCaps = new DesiredCapabilities();
            String[] cliArgsCap = {"--web-security=false", "--ssl-protocol=any", "--ignore-ssl-errors=true", "--webdriver-loglevel=DEBUG"};
            sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, getModuleBaseDir() + "/src/main/resources/phantomjs/phantomjs.com");
            sCaps.setJavascriptEnabled(true);
            driver = new PhantomJSDriver(sCaps);
        } else if (browser.equalsIgnoreCase("htmlunit")) {
            //driver = new HtmlUnitDriver();
            //((HtmlUnitDriver) driver).setJavascriptEnabled(true);
        } else if (browser.equalsIgnoreCase("mobilechrome")) {
            Map<String, String> mobileEmulation = new HashMap<String, String>();
            mobileEmulation.put("deviceName", "iPhone 6");
            Map<String, Object> chromeOptions = new HashMap<>();
            chromeOptions.put("mobileEmulation", mobileEmulation);
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            if ((SystemUtils.IS_OS_MAC)) {
                String fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/browsers/chromedriver_mac/chromedriver";
                try {
                    Runtime.getRuntime().exec("chmod +x " + fileLocation);
                    LOG.info("Made chromedriver executable for Mac");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.setProperty("webdriver.chrome.driver", fileLocation);
            } else {
                String fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/browsers/chromedriver_win32/chromedriver.exe";
                System.setProperty("webdriver.chrome.driver", fileLocation);
            }


            driver = new ChromeDriver(capabilities);

        } else {
            String fileLocation = DriverUtil.getModuleBaseDir() + "/binaries/browsers/";

            if (SystemUtils.IS_OS_LINUX) {

                fileLocation += "chromedriver_linux64/chromedriver";
                LOG.info("Set chromedriver for Linux");

                try {
                    Runtime.getRuntime().exec("chmod +x " + fileLocation);
                    LOG.info("Made chromedriver executable for Linux");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (SystemUtils.IS_OS_WINDOWS) {
                fileLocation += "chromedriver_win32/chromedriver.exe";

            } else if (SystemUtils.IS_OS_MAC) {
                fileLocation += "chromedriver_mac/chromedriver";
                try {
                    Runtime.getRuntime().exec("chmod +x " + fileLocation);
                    LOG.info("Made chromedriver executable for Mac");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.setProperty("webdriver.chrome.driver", fileLocation);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-extensions");
            options.addArguments("--start-maximized");
            //options.setBinary("/opt/google/chrome");
            if (System.getenv("bamboo_planName") != null) {
                options.addArguments("headless");
                LOG.info("Running chrome in headless manner");
            }
            if (System.getProperty("headless")!=null && System.getProperty("headless").equalsIgnoreCase("true")){
                options.addArguments("headless");
                LOG.info("Running chrome in headless manner");
            }

           /* if (SystemUtils.IS_OS_LINUX) {
                String[] command = {"/bin/sh", "-c", "docker-compose up"};
                LinuxUtil.executeCommandAndPrintOutput(command);
            }else if (SystemUtils.IS_OS_WINDOWS) {
                String command = "powershell docker-compose up -d";
                WindowsUtil.executeCommandAndPrintOutput(command);
            }*/

            if (System.getenv("bamboo_planName") != null) {
                try {
                    if (waitUntilHubIsUp())
                        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
                    else
                        throw new Exception("Hub is not running");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else{
                driver = new ChromeDriver(options);
            }
            LOG.info("Created a new instance of ChromeDriver");



            if (SystemUtils.IS_OS_WINDOWS) {
                driver.manage().window().maximize();
            }else{

                if (browser.toLowerCase().contains("chrome")) {
                driver.manage().window().setPosition(new Point(-10, 0));
                driver.manage().window().setSize(new Dimension(1600, 900));
                }
            }
        }
    }


    public static void destroyWebDriver() {
       /* if (System.getProperty("backup")!=null)
            backUpDirectories();*/
        destroyWebDriver(driver);

       /* if (SystemUtils.IS_OS_LINUX) {
            String[] command = {"/bin/sh", "-c", "docker-compose down"};
            LinuxUtil.executeCommandAndPrintOutput(command);
        }else if (SystemUtils.IS_OS_WINDOWS) {
            String command = "powershell docker-compose down";
            WindowsUtil.executeCommandAndPrintOutput(command);
        }*/
    }

    public static int getResponseCode(String urlString) {
        try{
        URL u = new URL(urlString);
        HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
        }catch (Exception ex){
            return -1;
        }
    }

    private static boolean waitUntilHubIsUp() throws Exception{
       int i;
        System.out.println("Checking if hub is running");
       for (i=1;i<=20;i++){
           if (getResponseCode("http://localhost:4444/") != 200){
               Thread.sleep(5000);
           }
       }
       if (i==20){
           return false;
       }else{
           return true;
       }
    }
    private static void invokeSauceLabsDriver(String browser) {
        sCaps = SauceLabUtil.sauceLabSetUp(browser);

        try {
            driver = new ProxyingRemoteWebDriver(new URL(SauceLabUtil.URL), sCaps);
        } catch (MalformedURLException e) {
            driver = null;
            e.printStackTrace();
        }
    }

    private static void invokeBrowserStackDriver(String browser) {
        sCaps = BrowserStackUtil.browserStackSetUp(browser);
        try {
            driver = new RemoteWebDriver(new URL(BrowserStackUtil.URL), sCaps);
        } catch (MalformedURLException e) {
            driver = null;
            e.printStackTrace();
        }
    }

    public static void settingImplicitWait() {

        if (!(System.getProperty("browser").equalsIgnoreCase("safari") || System.getProperty("browser").equalsIgnoreCase("IE")))
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

        //driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }


    public static WebDriver getDriver() {
        return driver;
    }

    public static WebDriver destroyWebDriver(WebDriver driver) {
        if (driver != null) {

            try {
                String status = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
                LOG.info("Document status is " + status);
            } catch (Exception ex) {
            }

            try {
                ((JavascriptExecutor) driver).executeScript("window.onbeforeunload = null");
            } catch (Exception ex) {
                LOG.error("Failed to run quit() prevention javascript");
            }

            try {
                if (System.getProperty("browser") != null && System.getProperty("browser").equalsIgnoreCase("firefox")) {
                    Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe /T");
                }
                driver.quit();

            } catch (Exception ex) {
                LOG.error("Failed to run quit() on the driver", ex.getMessage());
            }
        }
        return driver;
    }

    public static String getModuleBaseDir() {
        String baseDir = System.getProperty("user.dir");

        Class thisClass = DriverUtil.class;
        String classResourceName = thisClass.getName().replace(DriverUtil.class.getSimpleName(), "").replace(".", "/");
        classResourceName = classResourceName + thisClass.getClass().getSimpleName() + ".class";
        URL classResourcePathURL = thisClass.getClassLoader().getResource(classResourceName);
        if (classResourcePathURL != null) {
            int splitIndex = classResourcePathURL.getPath().indexOf("/target/classes");
            baseDir = classResourcePathURL.getPath().substring(0, splitIndex);
        }
        LOG.debug("Os={} BaseDir={}", System.getProperty("os.name"), baseDir);
        return baseDir;
    }

    private static FirefoxProfile getFirefoxProfile(Properties ffProps) {
        FirefoxProfile profile = new FirefoxProfile();
        File modifyHeaders = new File(getModuleBaseDir() + "/src/main/resources/firefox/" + MODIFY_HEADERS_EXTENSION);
        try {
            profile.addExtension(modifyHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Map.Entry<Object, Object> entry : ffProps.entrySet()) {
            String key = String.valueOf(entry.getKey());
            if (key.endsWith(".int")) {
                String newKey = key.substring(0, key.length() - ".int".length());
                int value = Integer.valueOf(String.valueOf(entry.getValue()));
                LOG.debug("Setting firefox profile key/value: {}", newKey);
                profile.setPreference(newKey, value);
            } else if (key.endsWith(".boolean")) {
                String newKey = key.substring(0, key.length() - ".boolean".length());
                boolean value = Boolean.valueOf(String.valueOf(entry.getValue()));
                LOG.debug("Setting firefox profile key/value: {}", newKey);
                profile.setPreference(newKey, value);
            } else {
                String value = String.valueOf(entry.getValue());
                LOG.debug("Setting firefox profile key/value: {}", key);
                profile.setPreference(key, value);
            }
        }
        return profile;
    }

    private static Properties loadFirefoxProperties() {
        Properties ffProps = new Properties();
        try {
            InputStream inputStream = DriverUtil.class.getClassLoader().getResourceAsStream(FIREFOX_PROPERTIES);
            ffProps.load(inputStream);
            LOG.debug("Successfully loaded [{}]", FIREFOX_PROPERTIES);
        } catch (IOException ioex) {
            String err = String.format("Failed to load [%s]", FIREFOX_PROPERTIES);
            LOG.error(err);
            throw new IllegalStateException(err);
        }
        return ffProps;
    }


    public static void killOrphanProcesses() {
        try {
            LOG.info("Killing orphan processes");
            String[] processNames = {"firefox", "iexplore", "chromium-browser", "IEDriverServer", "chromedriver"};
            for (String process : processNames) {
                if (SystemUtils.IS_OS_LINUX) {
                    String[] command = {"/bin/sh", "-c", "ps -ef | grep -w " + process + " | grep -v grep | awk '/[0-9]/{print $2}' | xargs kill -9 "};
                    LinuxUtil.executeCommandAndPrintOutput(command);
                    //LOG.info("Killing orphan processes on Linux");
                } else if (SystemUtils.IS_OS_WINDOWS) {
                    //LOG.info("Killing orphan processes on Windows");
                    //Runtime.getRuntime().exec("taskkill /F /IM "+ process +".exe");
                }
            }
        } catch (Exception e) {
            LOG.info("Error occurred while killing orphan processes");
            e.printStackTrace();
        }
    }

}
