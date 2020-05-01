package pl.dudzinski.clinic.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.dudzinski.clinic.service.PatientService
import java.net.URI

@RestController
@RequestMapping("/patients")
class PatientController(val protocolMapper: ProtocolMapper,
                        val patientService: PatientService,
                        @Value("\${server.servlet.context-path}")
                        val contextPath: String) {

    companion object {
        private val logger = LoggerFactory.getLogger(PatientController::class.java)
    }

    @GetMapping
    fun getPatients(): List<PatientDTO> {
        logger.info("Received request for all patients")
        return patientService.getAllPatients()
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPatient(@PathVariable("id") id: Long): PatientDTO {
        logger.info("Received request for patient with id: $id")
        val optionalPatient = patientService.getPatient(id)
        return optionalPatient.orElseThrow {
            ResourceNotFoundException("Could not find patient with id: $id")
        }
    }

    @PostMapping
    fun savePatient(@RequestBody patientDTO: PatientDTO): ResponseEntity<Unit> {
        logger.info("Received request for save new patient: $patientDTO")
        val savedPatient = patientService.savePatient(patientDTO)
        return ResponseEntity.created(URI("${contextPath}/patients/${savedPatient.id}")).build()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deletePatient(@PathVariable("id") id: Long) {
        logger.info("Received request for delete patient with id: $id")
        if (!patientService.existPatient(id)) {
            throw ResourceNotFoundException("Could not find patient with id: $id")
        }
        patientService.deletePatient(id)
    }

    @PutMapping("/{id}")
    fun updatePatient(@PathVariable("id") id: Long, @RequestBody patientDTO: PatientDTO): ResponseEntity<Unit> {
        logger.info("Received request for update patient with id: $id with data: $patientDTO")
        return if (patientService.existPatient(id)) {
            patientService.updatePatient(id, patientDTO)
            ResponseEntity.noContent().build()
        } else {
            val savedPatient = patientService.savePatient(patientDTO)
            ResponseEntity.created(URI("${contextPath}/patients/${savedPatient.id}")).build()
        }
    }
}
