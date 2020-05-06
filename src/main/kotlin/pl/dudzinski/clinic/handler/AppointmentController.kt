package pl.dudzinski.clinic.handler

import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.dudzinski.clinic.service.AppointmentService
import pl.dudzinski.clinic.service.PatientService
import java.net.URI

@RestController
@RequestMapping("/appointments")
class AppointmentController(val patientService: PatientService,
                            val appointmentService: AppointmentService,
                            @Value("\${server.servlet.context-path}")
                            val contextPath: String) {

    companion object {
        private val logger = LoggerFactory.getLogger(AppointmentController::class.java)
    }

    @GetMapping
    fun getAppointments(): List<AppointmentDTO> {
        logger.info("Received request for all appointments")
        return appointmentService.getAllAppointments()
    }

    @GetMapping("/{id}")
    fun getAppointment(@PathVariable("id") id: Long): AppointmentDTO {
        logger.info("Received request for appointment with id: $id")
        return appointmentService.getAppointment(id)
    }

    @GetMapping("/patient/{id}")
    fun getAppointmentForPatient(@PathVariable("id") id: Long): List<AppointmentDTO> {
        logger.info("Received request for appointments of patient with id: $id")
        return appointmentService.getAppointmentsForPatient(id)
    }

    @PostMapping
    fun postAppointment(@RequestBody appointmentDTO: AppointmentDTO): ResponseEntity<Unit> {
        logger.info("Received request for save new appointment: $appointmentDTO")
        val appointment = appointmentService.saveAppointment(appointmentDTO)
        return ResponseEntity.created(URI("${contextPath}/appointments/${appointment.id}")).build()
    }

    @DeleteMapping("/{id}")
    fun deleteAppointment(@PathVariable("id") id: Long) {
        logger.info("Received request for delete appointment with id: $id")
        appointmentService.deleteAppointment(id)
    }

    @PutMapping("/{id}")
    fun putAppointment(@PathVariable("id") id: Long, @RequestBody appointmentDTO: AppointmentDTO): ResponseEntity<Unit> {
        logger.info("Received request for update appointment with id: $id with data: $appointmentDTO")
        return if (appointmentService.existAppointment(id)) {
            appointmentService.updateAppointment(id, appointmentDTO)
            ResponseEntity.noContent().build()
        } else {
            appointmentService.saveAppointment(appointmentDTO)
            ResponseEntity.created(URI("${contextPath}/appointments/${id}")).build()
        }

    }

    @PatchMapping("/{id}", consumes = ["application/json-patch+json"])
    fun patchAppointment(@PathVariable("id") id: Long, @RequestBody patch: JsonPatch): ResponseEntity<Unit> {
        logger.info("Received request for patch appointment with id: $id with patch: $patch")
        appointmentService.patchAppointment(id, patch)
        return ResponseEntity.ok().build()
    }

}
