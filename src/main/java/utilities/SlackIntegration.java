package utilities;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by ssalunke on 26/10/2016.
 */
public class SlackIntegration {

    //This method will push simple message to slack
    public static void pushToSlack(String message) throws Exception {

        String address = "192.168.130.20";
        int port = 8080;
        String proxyUsername = "ittest";
        String proxyPassword = "1tt3st";
        String url = "https://hooks.slack.com/services/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";

        //create default client
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(
                new AuthScope(address, port),
                new NTCredentials(proxyUsername, proxyPassword, "192.168.130.20", "domain"));
        HttpHost proxy = new HttpHost(address, port);
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        client.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
        HttpPost httppost = new HttpPost("https://hooks.slack.com/services/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");
        httppost.addHeader("Content-Type", "application/json");
        StringEntity params = new StringEntity("{\"text\" : \"" + message + "\"}", "UTF-8");
        httppost.setEntity(params);
        CloseableHttpResponse response = client.execute(httppost);
        client.close();
    }

    public static void pushToSlack(String scenarioName, String url, String title, String exception, String transactionID) {

        try {
            String address = "192.168.130.20";
            int port = 8080;
            String proxyUsername = "ittest";
            String proxyPassword = "1tt3st";
            String webhook = "https://hooks.slack.com/services/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";

            //create default client
            DefaultHttpClient client = new DefaultHttpClient();
          /*  client.getCredentialsProvider().setCredentials(
                    new AuthScope(address, port),
                    new NTCredentials(proxyUsername, proxyPassword, "192.168.130.20", "domain"));*/
            //HttpHost proxy = new HttpHost(address, port);
            //client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            client.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
            HttpPost httppost = new HttpPost(webhook);
            httppost.addHeader("Content-Type", "application/json");

            url = removeSpecialChars(url);
            title = removeSpecialChars(title);
            exception = removeSpecialChars(exception);

            if (!transactionID.equalsIgnoreCase(""))
                exception = "Transaction ID : " + transactionID + " \n " + exception;

            String color;
            if (exception.toLowerCase().contains("timeoutexception") || exception.toLowerCase().contains("assertion"))
                color = "danger";
            else
                color = "warning";

            //Default Image
            String verticalIconUrl = ":x:";
            String verticalName = "CTM";
            if (url.toLowerCase().contains("card")) {
                //verticalIconUrl ="https://cdn.pixabay.com/photo/2014/02/01/18/01/money-256319_960_720.jpg";
                verticalIconUrl = ":credit_card:";
                verticalName = "Money";
            } else if (url.toLowerCase().contains("car")) {
                //verticalIconUrl ="https://pixabay.com/static/uploads/photo/2013/07/13/11/44/car-158548_960_720.png";
                verticalIconUrl = ":red_car:";
                verticalName = "CAR";
            } else if (url.toLowerCase().contains("pet")) {
                //verticalIconUrl ="https://pixabay.com/static/uploads/photo/2013/07/13/11/44/car-158548_960_720.png";
                verticalIconUrl = ":cat2:";
                verticalName = "PET";
            } else if (url.toLowerCase().contains("health")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2013/07/12/13/58/stethoscope-147700_960_720.png";
                verticalIconUrl = ":hospital:";
                verticalName = "HEALTH";
            } else if (url.toLowerCase().contains("travel")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2016/07/15/08/40/plane-1518481_960_720.jpg";
                verticalIconUrl = ":airplane:";
                verticalName = "TRAVEL";

            } else if (url.toLowerCase().contains("home_contents") || url.toLowerCase().contains("home-contents")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2013/07/12/14/14/house-148033_960_720.png";
                verticalIconUrl = ":house:";
                verticalName = "HOME and CONTENTS";

            } else if (url.toLowerCase().contains("fuel")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2014/03/24/17/15/gas-station-295202_960_720.png";
                verticalIconUrl = ":fuelpump:";
                verticalName = "FUEL";
            } else if (url.toLowerCase().contains("roadside")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2014/04/03/10/09/tow-truck-309953_960_720.png";
                verticalIconUrl = ":hammer_and_wrench:";
                verticalName = "Roadside Assistance";
            } else if (url.toLowerCase().contains("utilities") || url.toLowerCase().contains("energy")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2016/08/12/04/03/light-bulb-1587569_960_720.png";
                verticalIconUrl = ":bulb:";
                verticalName = "UTILITIES";
            } else if (url.toLowerCase().contains("life")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2013/05/11/08/28/person-110305_960_720.jpg";
                verticalIconUrl = ":mens:";
                verticalName = "LIFE INSURANCE";
            } else if (url.toLowerCase().contains("loan")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2013/07/12/15/28/valuation-149889_960_720.png";
                verticalIconUrl = ":house:";
                verticalName = "HOME LOAN";
            } else if (url.toLowerCase().contains("hotel")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2013/07/12/15/28/valuation-149889_960_720.png";
                verticalIconUrl = ":hotel:";
                verticalName = "HOTEL";
            } else if (url.toLowerCase().contains("ip_") || url.toLowerCase().contains("income-")) {
                //verticalIconUrl = "https://pixabay.com/static/uploads/photo/2016/06/17/08/46/cash-1462856_960_720.png";
                verticalIconUrl = ":moneybag:";
                verticalName = "INCOME PROTECTION";
            }


            String planKey = "";
            if (System.getenv("bamboo_planKey") != null) {
                planKey = System.getenv("bamboo_planKey");
            }

            String BambooReport = "https://bamboo.comparethemarket.com.au/browse/" + planKey + "-" + System.getenv("bamboo_buildNumber") + "/artifact/JOB1/Cucumber-html-reports/index.html";

            String message = "{\n" +
                    "            \"icon_emoji\": \"" + verticalIconUrl + "\",\n" +
                    "            \"text\": \"" + "*" + verticalName + " - " + scenarioName + "*" + "\",\n" +
                    "    \"attachments\": [\n" +
                    "        {\n" +
                    "            \n" +
                    "            \"color\": \"" + color + "\",\n" +
                    "            \"author_name\":\" " + title + "\",\n" +
                    "            \"author_link\":\"" + url + "\",\n" +
                    "            \"title\":\" View Bamboo Report \",\n" +
                    "            \"title_link\":\"" + BambooReport + "\",\n" +
                    //"            \"author_icon\": \""+ verticalIconUrl +"\",\n" +
                    "            \"text\": \"" + exception + "\" \n" +
                    "            \n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

            StringEntity params = new StringEntity(message, "UTF-8");
            httppost.setEntity(params);
            CloseableHttpResponse response = client.execute(httppost);
            System.out.println(response.toString());
            client.close();
        } catch (Exception ex) {
            System.out.println("Exception occurred whiling slacking " + ex.toString());
            ex.printStackTrace(System.out);
        }
    }

    public static String removeSpecialChars(String message) {
        message = message.replaceAll("\"", "'");
        message = message.replaceAll("\\{", "[");
        message = message.replaceAll("}", "]");

        return message;
    }

    public static void pushToVictorOps(String scenarioName, String url, String title, String exception, String transactionID) {


        try {

            String webHook = "https://alert.victorops.com/integrations/generic/20131114/alert/13089f1b-84f0-4cda-87db-476d5151cd11/Default%20Routing%20Policy";

            //create default client
            HttpClient client = HttpClientBuilder.create().build();

            //client.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());

            HttpPost httppost = new HttpPost(webHook);

            httppost.addHeader("Content-Type", "application/json");

            url = removeSpecialChars(url);
            title = removeSpecialChars(title);
            exception = exception.replaceAll(":","");
            exception = exception.replaceAll("\n","").replaceAll("\r","");
            exception = removeSpecialChars(exception);

            String planKey = "";
            if (System.getenv("bamboo_planKey") != null) {
                planKey = System.getenv("bamboo_planKey");
            }

            String BambooReport = "https://bamboo.comparethemarket.com.au/browse/" + planKey + "-" + System.getenv("bamboo_buildNumber") + "/artifact/JOB1/Cucumber-html-reports/index.html";

            if (!transactionID.equalsIgnoreCase(""))
                exception = "Transaction ID : " + transactionID + " : " + exception;

            System.out.println("Firing victorops alert");

            System.out.println("Firing victorops alert");

            String message_type = "CRITICAL";

            String message =
                            "{\n" +
                            "\"message_type\":\""+message_type+"\",\n" +
                            "\"entity_id\":\""+BambooReport + "\",\n" +
                            "\"entity_display_name\":\"" + scenarioName + "\",\n" +
                            "\"state_message\":\""+exception + "\"\n" +
                            "}";

            System.out.println(message);
            //String message ="{'message_type','Sample Message'}";
            StringEntity params = new StringEntity(message);
            httppost.setEntity(params);

            HttpResponse response = client.execute(httppost);
            System.out.println(response.toString());

        } catch (Exception ex) {
            System.out.println("Exception occurred " + ex.toString());
        }
    }
}



