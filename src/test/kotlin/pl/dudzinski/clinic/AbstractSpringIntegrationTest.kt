package pl.dudzinski.clinic

import integration.ClinicInvoker
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@SpringBootTest(
        classes = [ClinicApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
abstract class AbstractSpringIntegrationTest {

    @LocalServerPort
    private val port: String? = null

    @Value("\${server.servlet.context-path:}")
    private val serverContextPath: String? = null

    val serverURI: String get() = "http://localhost:$port$serverContextPath"

    val hostWithPort: String get() = "http://localhost:$port"

    protected val rest: RestTemplate get() = RestTemplateBuilder()
            .rootUri(serverURI)
            .requestFactory(HttpComponentsClientHttpRequestFactory::class.java).build()

    protected val invoker: ClinicInvoker get() = ClinicInvoker(rest)

    fun assertResourceNotFound(runnable: () -> Unit) = assertStatus(runnable, HttpStatus.NOT_FOUND)

    protected fun assertStatus(runnable: () -> Unit, status: HttpStatus) {
        var actual = HttpStatus.OK
        try {
            runnable.invoke()
        } catch (e: HttpClientErrorException) {
            if (e.rawStatusCode == status.value()) {
                return
            }
            actual = HttpStatus.valueOf(e.rawStatusCode)
        }
        Assertions.fail<Unit>("Expected $status but received $actual")
    }

}
