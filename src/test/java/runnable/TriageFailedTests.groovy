package runnable

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "@target/rerun.txt",
        glue = "classpath:steps",
        plugin = ["html:target/test-cucumber-reports/triage"]
)
class TriageFailedTests {
}
