package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by alitan on 15/05/2015.
 */
public class URLReader {

    protected static final Logger LOG = LoggerFactory.getLogger(DriverUtil.class);

    public static String getURL(String product) throws IOException {
        String fileName, url;
        //By default, tests run on NXI
        fileName = "DEV.properties";
        url = "";

        // Precedence - first look at System.getProperty("env")
        // Precedence - then look at System.getEnv("env")
        String targetEnv = System.getProperty("env");
        if (targetEnv != null && targetEnv.length() > 0) {
            if (targetEnv.equalsIgnoreCase("LOCALHOST")) {
                fileName = "LOCALHOST.properties";
            } else if (targetEnv.equalsIgnoreCase("NXS")) {
                fileName = "NXS.properties";
            } else if (targetEnv.equalsIgnoreCase("NXI")) {
                fileName = "NXI.properties";
            } else if (targetEnv.equalsIgnoreCase("NXQ")) {
                fileName = "NXQ.properties";
            } else if (targetEnv.equalsIgnoreCase("PRD")) {
                fileName = "PRD.properties";
            } else if (targetEnv.equalsIgnoreCase("PRELIVE")) {
                fileName = "PRELIVE.properties";
            } else {
                throw new IllegalArgumentException("Unknown environment: " + targetEnv);
            }
        } else {
            // This is set by bamboo
            targetEnv = System.getenv("bamboo_env");
            if (targetEnv != null && targetEnv.length() > 0) {
                if (targetEnv.equals("LOCALHOST")) {
                    fileName = "LOCALHOST.properties";
                } else if (targetEnv.equals("NXI")) {
                    fileName = "NXI.properties";
                } else if (targetEnv.equals("NXS")) {
                    fileName = "NXS.properties";
                } else if (targetEnv.equals("NXQ")) {
                    fileName = "NXQ.properties";
                } else if (targetEnv.equals("PRD")) {
                    fileName = "PRD.properties";
                } else if (targetEnv.equals("PRELIVE")) {
                    fileName = "PRELIVE.properties";
                } else {
                    throw new IllegalArgumentException("Unknown environment: " + targetEnv);
                }
            }
        }

        Properties prop = new Properties();
        try {
            InputStream inputStream = URLReader.class.getClassLoader().getResourceAsStream(fileName);
            prop.load(inputStream);
        } catch (IOException ioex) {
            String err = "Failed to load firefox.properties";
            LOG.error(err);
            throw new IllegalStateException(err);
        }

        //normal url is: http://nxi.secure.comparethemarket.com.au/ctm/car_quote.jsp?
        //url with branch will be updated to: http://nxi.secure.comparethemarket.com.au/ctm_CTMIT-554-plp-upgrade-plugin-library-versions/car_quote.jsp

        String targetBranch = System.getProperty("branch");
        String targetJParam = System.getProperty("j");
        if (targetBranch != null && targetBranch.length() > 0 && targetJParam != null && targetJParam.length() > 0) {
            String originalURL = prop.getProperty(product);
            url = originalURL.replace("ctm", targetBranch) + "&j=" + targetJParam;
        } else if (targetBranch != null && targetBranch.length() > 0) {
            String originalURL = prop.getProperty(product);
            url = originalURL.replace("ctm", "ctm_" + targetBranch);
        } else if (targetJParam != null && targetJParam.length() > 0) {
            url = prop.getProperty(product) + "&j=" + targetJParam;
        } else {
            url = prop.getProperty(product);
        }

        /*
        //param automated-test=true will turn off meerkat logging module
        String loggingOff = System.getProperty("loggingOff");
        if (loggingOff != null && loggingOff.equals("true") && !product.equals("ctm.money")) {
            url = url + "&automated-test=true";
        }

        if (System.getenv("bamboo_DevBranch") != null && System.getenv("bamboo_DevBranch") != "") {
            url = url.replaceAll("ctm", "ctm_" + System.getenv("bamboo_DevBranch"));
            LOG.info("Replacing url with branch" + System.getenv("bamboo_DevBranch"));
        }

        url = url.replaceAll("ctm_/", "ctm/");

        */
        if (System.getProperty("testbranch") != null && !System.getProperty("testbranch").equalsIgnoreCase("default")) {
            LOG.info("Navigating to testbranch");
            url = System.getProperty("testbranch");
        }


        LOG.info("Navigating to URL " + url);

        return url;
    }

}
