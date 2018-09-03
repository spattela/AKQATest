package utilities;

/**
 * Created by ssalunke on 14/02/2017.
 */
public class ProcessUtil {

    public static void kill(String process) {
        String[] command = {"/bin/sh", "-c", "ps -ef | grep -w " + process + " | grep -v grep | awk '/[0-9]/{print $2}' | xargs kill -9 "};
        LinuxUtil.executeCommandAndPrintOutput(command);
    }
}
