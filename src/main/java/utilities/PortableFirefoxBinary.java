package utilities;

/**
 * Created by dkocovski on 13/05/2015.
 */

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class PortableFirefoxBinary extends FirefoxBinary {
    protected static final Logger LOG = LoggerFactory.getLogger(PortableFirefoxBinary.class);

    // Use renamed exe's so that we dont hard kill any existing running firefox.exe's
    public static final String WINDOWS_FF_EXE_NAME = "firefox_p.exe";
    public static final String WINDOWS_FF_PORTABLE_NAME = "FirefoxPortable_p";
    public static final String WINDOWS_FF_PORTABLE_EXTENSION = "exe";

    public static final String WINDOWS_FF_PORTABLE_EXE_NAME = WINDOWS_FF_PORTABLE_NAME + "." + WINDOWS_FF_PORTABLE_EXTENSION;


    private WebDriver webDriver;

    public PortableFirefoxBinary(File binaryPath) {
        super(binaryPath);
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     */
    @Override
    public void quit() {
        LOG.info("quit() start");
        super.quit();

        // Workaround
        // Need to hard kill firefox portable child process for windows, (in < 2.44.0 versions of selenium)
        // Extract the unique executable name
       /* int lastSep = getExecutable().getFile().getAbsolutePath().lastIndexOf("\\");
        String execName = getExecutable().getFile().getAbsolutePath().substring(lastSep + 1);
        try {
            Runtime.getRuntime().exec(String.format("Taskkill /IM %s /F /T", execName));
            Thread.sleep(1000);
            getExecutable().getFile().delete();
        } catch (Exception ex) {
            LOG.error("Fatal error while trying to terminate [{}]", WINDOWS_FF_EXE_NAME);
        }

        LOG.info("quit() finish");*/
    }

}
