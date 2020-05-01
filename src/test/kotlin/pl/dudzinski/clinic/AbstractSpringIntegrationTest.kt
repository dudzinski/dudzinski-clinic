package pl.dudzinski.clinic

import integration.ClinicInvoker
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.web.client.RestTemplate

@SpringBootTest(
        classes = [ClinicApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application-integration-test.properties"])
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
abstract class AbstractSpringIntegrationTest {

    @LocalServerPort
    private val port: String? = null

    @Value("\${server.servlet.context-path:}")
    private val serverContextPath: String? = null

    val serverURI: String get() = "http://localhost:$port$serverContextPath"

    val hostWithPort: String get() ="http://localhost:$port"

    protected val rest: RestTemplate get() = RestTemplateBuilder().rootUri(serverURI).build()

    protected val invoker: ClinicInvoker get() = ClinicInvoker(rest)


}
