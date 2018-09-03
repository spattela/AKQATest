package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class URLReader {

    protected static final Logger LOG = LoggerFactory.getLogger(DriverUtil.class);

    public static String getURL(String product) throws IOException {
        String fileName, url;
        //By default, tests run on DEV
        fileName = "DEV.properties";
        url = "";
        String targetEnv = System.getProperty("env");
        if (targetEnv != null && targetEnv.length() > 0) {
            if (targetEnv.equalsIgnoreCase("LOCALHOST")) {
                fileName = "LOCALHOST.properties";
            } else if (targetEnv.equalsIgnoreCase("DEV")) {
                fileName = "DEV.properties";
            } else if (targetEnv.equalsIgnoreCase("UAT")) {
                fileName = "UAT.properties";
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
        url = prop.getProperty(product);

        LOG.info("Navigating to URL " + url);

        return url;
    }

}
