package pl.dudzinski.clinic.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.util.UriComponentsBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration
import springfox.documentation.spring.web.paths.DefaultPathProvider
import springfox.documentation.spring.web.paths.Paths.removeAdjacentForwardSlashes
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc


@Configuration
@EnableSwagger2WebMvc
@Import(SpringDataRestConfiguration::class)
class SwaggerConfig {

    @Value("\${server.servlet.context-path}")
    private val contextPath: String? = null

    @Bean
    fun docket(): Docket? = Docket(DocumentationType.SWAGGER_2)
            .pathProvider(CustomPathProvider(contextPath!!))
            .select()
            .apis(RequestHandlerSelectors.basePackage("pl.dudzinski.clinic.handler"))
            .paths(PathSelectors.any())
            .build();

    private inner class CustomPathProvider(val contextPath : String) : DefaultPathProvider() {
        override fun getOperationPath(operationPath: String): String {
            var path = operationPath
            if (path.startsWith(contextPath)) {
                path = path.substring(contextPath.length)
            }
            return removeAdjacentForwardSlashes(UriComponentsBuilder.newInstance().replacePath(path)
                    .build().toString())
        }
    }
}
