package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class PathReader {

    protected static final Logger LOG = LoggerFactory.getLogger(PathReader.class);

    public static String getWEB_CTMLogPath() {
        final String property = PropertiesHelper.getApplicationProperties().getProperty("web-ctm.log.path");
        LOG.info("WEB-CTM log path is {}", property);
        return property;
    }

    public static String getBERLogPath() {
        final String property = PropertiesHelper.getApplicationProperties().getProperty("ber.log.path");
        LOG.info("BER log path is {}", property);
        return property;
    }

    public static void printFiles(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }
}
