package utilities;

import org.apache.commons.io.IOUtils;

import java.util.List;

public class LinuxUtil {

    //Below method will execute the linux command and return the output of the command
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

}
