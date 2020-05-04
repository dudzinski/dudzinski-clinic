package pl.dudzinski.clinic.handler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.dudzinski.clinic.service.DefaultDoctorService
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
    @ResponseStatus(HttpStatus.OK)
    fun getDoctor(@PathVariable("id") id: Long): DoctorDTO {
        logger.info("Received request for doctor with id: $id")
        val optionalDoctor = doctorService.getDoctor(id)
        return optionalDoctor.orElseThrow {
            ResourceNotFoundException("Could not find doctor with id: $id")
        }
    }

    @PostMapping
    fun saveDoctor(@RequestBody doctorDTO: DoctorDTO): ResponseEntity<Unit> {
        logger.info("Received request for save new doctor: $doctorDTO")
        val savedDoctor = doctorService.saveDoctor(doctorDTO)
        return createdResource(savedDoctor)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteDoctor(@PathVariable("id") id: Long) {
        logger.info("Received request for delete doctor with id: $id")
        if (!doctorService.existDoctor(id)) {
            throw ResourceNotFoundException("Could not find doctor with id: $id")
        }
        doctorService.deleteDoctor(id)
    }

    @PutMapping("/{id}")
    fun updateDoctor(@PathVariable("id") id: Long, @RequestBody doctorDTO: DoctorDTO): ResponseEntity<Unit> {
        logger.info("Received request for update doctor with id: $id with data: $doctorDTO")
        return if (doctorService.existDoctor(id)) {
            doctorService.updateDoctor(id, doctorDTO)
            ResponseEntity.noContent().build()
        } else {
            val savedDoctor = doctorService.saveDoctor(doctorDTO)
            createdResource(savedDoctor)
        }
    }

    private fun createdResource(doctorDTO: DoctorDTO): ResponseEntity<Unit> =
            ResponseEntity.created(URI("${contextPath}/doctors/${doctorDTO.id}")).build()
}
