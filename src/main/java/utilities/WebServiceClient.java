package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceClient.class);

    private final URL url;

    private final Map<String, String> requestHeadersMap = new HashMap<>();

    private final int connectionTimeout;

    private final int readTimeout;

    private Proxy proxy;

    private WebServiceClient(final String urlStr) {
        this(urlStr, 30000, 60000);
    }

    private WebServiceClient(final String urlStr, final int connectionTimeout, final int readTimeout) {
        try {
            url = new URL(urlStr);
            this.connectionTimeout = connectionTimeout;
            this.readTimeout = readTimeout;
        } catch (Exception e) {
            LOGGER.error("Unable to create WebServiceClient", e);
            throw new RuntimeException(e);
        }
    }

    public static WebServiceClient build(final String urlStr, final int connectionTimeout, final int readTimeout) {
        return new WebServiceClient(urlStr, connectionTimeout, readTimeout);
    }

    public static WebServiceClient build(final String urlStr) {
        return new WebServiceClient(urlStr);
    }

    public WebServiceClient addRequestHeader(String name, String value) {
        requestHeadersMap.put(name, value);
        return this;
    }

    public String doPost(String request) throws IOException {
        final HttpURLConnection urlConnection = getHttpURLConnection("POST", true);
        final OutputStream os = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(request);
        writer.flush();
        writer.close();
        os.close();

        urlConnection.connect();

        final int status = urlConnection.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                return sb.toString();
            default:
                final String responseMessage = urlConnection.getResponseMessage();
                LOGGER.error("{}: Status code error {} {} ", url.getPath(), status, responseMessage);
                throw new IOException("Status code error " + status + " " + responseMessage);
        }
    }

    private HttpURLConnection getHttpURLConnection(String method, boolean doOutput) throws IOException {
        final HttpURLConnection urlConnection;
        if (proxy != null) {
            urlConnection = (HttpURLConnection) url.openConnection(proxy);
        } else {
            urlConnection = (HttpURLConnection) url.openConnection();
        }
        urlConnection.setRequestMethod(method);
        urlConnection.setUseCaches(false);
        urlConnection.setAllowUserInteraction(false);
        urlConnection.setConnectTimeout(connectionTimeout);
        urlConnection.setReadTimeout(readTimeout);
        urlConnection.setDoOutput(doOutput);
        requestHeadersMap.entrySet()
                .stream()
                .forEach(e -> urlConnection.setRequestProperty(e.getKey(), e.getValue()));
        return urlConnection;
    }

    public String doGet() throws IOException {
        final HttpURLConnection urlConnection = getHttpURLConnection("GET", true);
        urlConnection.connect();

        final int status = urlConnection.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                return sb.toString();
            default:
                final String responseMessage = urlConnection.getResponseMessage();
                LOGGER.error("{}: Status code error {} {} ", url.getPath(), status, responseMessage);
                throw new IOException("Status code error " + status + " " + responseMessage);
        }
    }

    public WebServiceClient setProxy(final Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

}
