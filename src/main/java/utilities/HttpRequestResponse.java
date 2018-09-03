package utilities;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


class HttpRequestResponse {
    static final Logger LOG = LoggerFactory.getLogger(HttpRequestResponse.class);

//    WebDriver driver;
//
//    HttpRequestResponse(WebDriver driver) {
//        this.driver = driver;
//        PageFactory.initElements(driver, this);
//    }

//    public int getResponseCode(String url) {
//        try {
//            return Request.Get(url).execute().returnResponse().getStatusLine()
//                    .getStatusCode();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static void testContentJSON(String restURL, String element, String expectedValue)
            throws ClientProtocolException, IOException, SAXException, ParserConfigurationException, JSONException {

        HttpUriRequest request = new HttpGet(restURL);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Convert the response to a String format
        String result = EntityUtils.toString(httpResponse.getEntity());

        LOG.info("result is {}", httpResponse.getEntity());

        // Convert the result as a String to a JSON object
        JSONObject jo = new JSONObject(result);

//        Assert.assertEquals(expectedValue, jo.getString(element));

        assert expectedValue.equals(jo.getString(element));

    }

    public static void printJSONObject(String restURL)
            throws ClientProtocolException, IOException, SAXException, ParserConfigurationException, JSONException {


        HttpUriRequest request = new HttpGet(restURL);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Convert the response to a String format
        String result = EntityUtils.toString(httpResponse.getEntity());

        LOG.info("result is {}", httpResponse.getEntity());

        // Convert the result as a String to a JSON object
        JSONObject jo = new JSONObject(result);

        LOG.info(jo.toString());

    }
}
