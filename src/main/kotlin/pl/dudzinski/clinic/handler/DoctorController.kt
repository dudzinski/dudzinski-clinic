package pl.dudzinski.clinic.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.dudzinski.clinic.service.DoctorService
import java.net.URI

@RestController
@RequestMapping("/doctors")
class DoctorController(val doctorService: DoctorService,
                       @Value("\${server.servlet.context-path}")
                       val contextPath: String) {

    companion object {
        private val logger = LoggerFactory.getLogger(DoctorController::class.java)
    }

    @GetMapping
    fun getDoctors(): List<DoctorDTO> {
        logger.info("Received request for all doctors")
        return doctorService.getAllDoctors()
    }

    @GetMapping("/{id}")
    fun getDoctor(@PathVariable("id") id: Long): DoctorDTO {
        logger.info("Received request for doctor with id: $id")
        return doctorService.getDoctor(id)
    }

    @PostMapping
    fun postDoctor(@RequestBody doctorDTO: DoctorDTO): ResponseEntity<Unit> {
        logger.info("Received request for save new doctor: $doctorDTO")
        val savedDoctor = doctorService.saveDoctor(doctorDTO)
        return ResponseEntity.created(URI("${contextPath}/doctors/${savedDoctor.id}")).build()
    }

    @DeleteMapping("/{id}")
    fun deleteDoctor(@PathVariable("id") id: Long) {
        logger.info("Received request for delete doctor with id: $id")
        doctorService.deleteDoctor(id)
    }

    @PutMapping("/{id}")
    fun putDoctor(@PathVariable("id") id: Long, @RequestBody doctorDTO: DoctorDTO): ResponseEntity<Unit> {
        logger.info("Received request for update doctor with id: $id with data: $doctorDTO")
        return if (doctorService.existDoctor(id)) {
            doctorService.updateDoctor(id, doctorDTO)
            ResponseEntity.noContent().build()
        } else {
            doctorService.saveDoctor(doctorDTO)
            ResponseEntity.created(URI("${contextPath}/doctors/${id}")).build()
        }
    }
}
