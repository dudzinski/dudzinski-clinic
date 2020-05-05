package pl.dudzinski.clinic.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.dudzinski.clinic.service.AppointmentService
import pl.dudzinski.clinic.service.PatientService
import java.net.URI

@RestController
@RequestMapping("/patients")
class PatientController(val patientService: PatientService,
                        val appointmentService: AppointmentService,
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
    fun getPatient(@PathVariable("id") id: Long): PatientDTO {
        logger.info("Received request for patient with id: $id")
        return patientService.getPatient(id)
    }

    @GetMapping("/{id}/appointments")
    fun getPatientAppointments(@PathVariable("id") id: Long): List<AppointmentDTO> {
        logger.info("Received request for appointments of patient with id: $id")
        return appointmentService.getAppointmentsForPatient(id)
    }

    @PostMapping
    fun postPatient(@RequestBody patientDTO: PatientDTO): ResponseEntity<Unit> {
        logger.info("Received request for save new patient: $patientDTO")
        val savedPatient = patientService.savePatient(patientDTO)
        return ResponseEntity.created(URI("${contextPath}/patients/${savedPatient.id}")).build()
    }

    @DeleteMapping("/{id}")
    fun deletePatient(@PathVariable("id") id: Long) {
        logger.info("Received request for delete patient with id: $id")
        patientService.deletePatient(id)
    }

    @PutMapping("/{id}")
    fun putPatient(@PathVariable("id") id: Long, @RequestBody patientDTO: PatientDTO): ResponseEntity<Unit> {
        logger.info("Received request for update patient with id: $id with data: $patientDTO")
        return if (patientService.existPatient(id)) {
            patientService.updatePatient(id, patientDTO)
            ResponseEntity.noContent().build()
        } else {
            patientService.savePatient(patientDTO)
            ResponseEntity.created(URI("${contextPath}/patients/${id}")).build()
        }
    }
}
