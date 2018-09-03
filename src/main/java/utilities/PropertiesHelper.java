package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {
    protected static final Logger LOG = LoggerFactory.getLogger(PropertiesHelper.class);
    private static final Properties INSTANCE = initializeProperties();
    private static Properties initializeProperties() {
        String fileName, url;
        //By default, tests run on NXI
        fileName = "NXI.properties";

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
                if (targetEnv.equalsIgnoreCase("LOCALHOST")) {
                    fileName = "LOCALHOST.properties";
                } else if (targetEnv.equalsIgnoreCase("NXI")) {
                    fileName = "NXI.properties";
                } else if (targetEnv.equalsIgnoreCase("NXS")) {
                    fileName = "NXS.properties";
                } else if (targetEnv.equalsIgnoreCase("NXQ")) {
                    fileName = "NXQ.properties";
                } else if (targetEnv.equalsIgnoreCase("PRD")) {
                    fileName = "PRD.properties";
                } else if (targetEnv.equalsIgnoreCase("PRELIVE")) {
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

        return prop;
    }

    public static Properties getApplicationProperties() {
        return INSTANCE;
    }


    public static boolean checkProdEnvironment() {

        System.out.println("env Property is " + System.getProperty("env"));

        if ((System.getProperty("env") != null && System.getProperty("env").equalsIgnoreCase("PRD")) || (System.getProperty("env") != null && System.getProperty("env").equalsIgnoreCase("PRELIVE"))) {
            //System.out.println("Current bamboo environment is " + System.getenv("bamboo_env"));
            System.out.println("Current environment is " + System.getProperty("env"));
            return true;
        } else {
            return false;
        }
    }
}
