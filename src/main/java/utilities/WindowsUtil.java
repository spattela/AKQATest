package utilities;

/**
 * Created by ssalunke on 9/03/2016.
 */
public class WindowsUtil {

    //Below method will execute the Windows command and return the output of the command
    public static void executeCommandAndPrintOutput(String cmd) {

       // StringBuffer output = new StringBuffer();
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // return output.toString();
    }

}
