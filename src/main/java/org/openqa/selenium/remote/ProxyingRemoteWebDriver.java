package org.openqa.selenium.remote;

import org.openqa.selenium.Capabilities;

import java.net.URL;

/**
 * Created by developer on 13/07/15.
 */
public class ProxyingRemoteWebDriver extends RemoteWebDriver {


    protected ProxyingRemoteWebDriver() {
    }

    public ProxyingRemoteWebDriver(CommandExecutor executor, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(executor, desiredCapabilities, requiredCapabilities);
    }

    public ProxyingRemoteWebDriver(CommandExecutor executor, Capabilities desiredCapabilities) {
        this(executor, desiredCapabilities, null);
    }

    public ProxyingRemoteWebDriver(Capabilities desiredCapabilities) {
        this((URL) null, desiredCapabilities);
    }

    public ProxyingRemoteWebDriver(URL remoteAddress, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        this(new ProxyingHttpCommandExecutor(remoteAddress), desiredCapabilities, requiredCapabilities);
    }

    public ProxyingRemoteWebDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        this(new ProxyingHttpCommandExecutor(remoteAddress), desiredCapabilities, null);
    }

}
