package pl.dudzinski.clinic.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.dudzinski.clinic.service.ClinicService
import java.net.URI

@RestController
@RequestMapping("/clinics")
class ClinicController(val clinicService: ClinicService,
                       @Value("\${server.servlet.context-path}")
                       val contextPath: String) {

    companion object {
        private val logger = LoggerFactory.getLogger(ClinicController::class.java)
    }

    @GetMapping
    fun getClinics(): List<ClinicDTO> {
        logger.info("Received request for all clinics")
        return clinicService.getAllClinics()
    }

    @GetMapping("/{id}")
    fun getClinic(@PathVariable("id") id: Long): ClinicDTO {
        logger.info("Received request for clinic with id: $id")
        return clinicService.getClinic(id)
    }

    @PostMapping
    fun postClinic(@RequestBody clinicDTO: ClinicDTO): ResponseEntity<Unit> {
        logger.info("Received request for save new clinic: $clinicDTO")
        val savedClinic = clinicService.saveClinic(clinicDTO)
        return ResponseEntity.created(URI("${contextPath}/clinics/${savedClinic.id}")).build()
    }

    @DeleteMapping("/{id}")
    fun deleteClinic(@PathVariable("id") id: Long) {
        logger.info("Received request for delete clinic with id: $id")
        clinicService.deleteClinic(id)
    }

    @PutMapping("/{id}")
    fun putClinic(@PathVariable("id") id: Long, @RequestBody clinicDTO: ClinicDTO): ResponseEntity<Unit> {
        logger.info("Received request for update clinic with id: $id with data: $clinicDTO")
        return if (clinicService.existClinic(id)) {
            clinicService.updateClinic(id, clinicDTO)
            ResponseEntity.noContent().build()
        } else {
            clinicService.saveClinic(clinicDTO)
            ResponseEntity.created(URI("${contextPath}/clinics/${id}")).build()
        }
    }
}
