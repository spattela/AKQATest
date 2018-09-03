package runnable

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "classpath:steps",
        tags = "@triage",
        plugin = ["html:target/test-cucumber-reports/triage", "rerun:target/rerun.txt"]
)
class TriageTests {
}
