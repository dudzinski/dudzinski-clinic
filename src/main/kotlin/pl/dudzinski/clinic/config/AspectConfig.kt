package pl.dudzinski.clinic.config

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import java.lang.System.currentTimeMillis


@Aspect
@Configuration
class AspectConfig {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Around("execution(* pl.dudzinski.clinic.handler.*.*(..))")
    fun measureTime(joinPoint: ProceedingJoinPoint): Any? {
        val start = currentTimeMillis()
        val proceed = joinPoint.proceed()
        logger.info("${joinPoint.signature.toShortString()} executed in ${currentTimeMillis() - start} ms")
        return proceed
    }

}
