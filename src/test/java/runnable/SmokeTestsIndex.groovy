package runnable

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/production/WordpressIndex.feature",
        glue = "classpath:steps",
        tags = "~@wip",
        plugin = ["html:target/test-cucumber-reports/smoke_tests_index", "rerun:target/rerun.txt"]
)
class SmokeTestsIndex {
}
