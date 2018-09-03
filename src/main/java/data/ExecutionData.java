package data;

import java.text.DecimalFormat;

/**
 * Created by ssalunke on 15/11/2016.
 */
public class ExecutionData {
    public String avgResponseTime = null;
    public String minResponseTime = null;
    public String maxResponseTime = null;
    public String totalRuns = null;
    public String failedRuns = null;
    public String responseTimes = null;
    public String vertical = null;
    public String comments = "";

    public int getPassed() {
        return Integer.parseInt(totalRuns) - Integer.parseInt(failedRuns);
    }

    public String getSuccessPercentage() {
        DecimalFormat df = new DecimalFormat("#.00");
        Double result = (getPassed() * 100) / Double.parseDouble(totalRuns);
        df = new DecimalFormat("#.0");
        if (result == 100) {
            return "100";
        }
        return df.format(result);
    }
}
