package integration

import org.springframework.http.*
import org.springframework.web.client.RestTemplate
import pl.dudzinski.clinic.handler.AppointmentDTO
import pl.dudzinski.clinic.handler.DoctorDTO
import pl.dudzinski.clinic.handler.PatientDTO

class ClinicInvoker(private val restTemplate: RestTemplate) {

    fun getAllAppointmentsForPatient(id: Long): ResponseEntity<Array<AppointmentDTO>> {
        return get("/patients/${id}/appointments", responseType = Array<AppointmentDTO>::class.java)
    }

    fun getAllAppointments(): ResponseEntity<Array<AppointmentDTO>> {
        return get("/appointments/", responseType = Array<AppointmentDTO>::class.java)
    }

    fun getAppointment(id: Long): ResponseEntity<AppointmentDTO> = get("/appointments/${id}", AppointmentDTO::class.java)

    fun patchAppointment(id: Long, patch: String): ResponseEntity<AppointmentDTO>? =
            patch("/appointments/${id}", patch, AppointmentDTO::class.java)

    fun deleteAppointment(id: Long) = delete("/appointments/${id}")

    fun updateAppointment(id: Long, appointmentDTO: AppointmentDTO) = put("/appointments/${id}", appointmentDTO, Unit::class.java)

    fun saveAppointment(appointmentDTO: AppointmentDTO) = post("/appointments", appointmentDTO, AppointmentDTO::class.java)


    fun getAllDoctors(): ResponseEntity<Array<DoctorDTO>> {
        return get("/doctors/", responseType = Array<DoctorDTO>::class.java)
    }

    fun getDoctor(id: Long): ResponseEntity<DoctorDTO> = get("/doctors/${id}", DoctorDTO::class.java)

    fun deleteDoctor(id: Long) = delete("/doctors/${id}")

    fun updateDoctor(id: Long, doctorDTO: DoctorDTO) = put("/doctors/${id}", doctorDTO, Unit::class.java)

    fun saveDoctor(doctorDTO: DoctorDTO) = post("/doctors", doctorDTO, Unit::class.java)


    fun getAllPatients(): ResponseEntity<Array<PatientDTO>> {
        return get("/patients/", responseType = Array<PatientDTO>::class.java)
    }

    fun getPatient(id: Long): ResponseEntity<PatientDTO> = get("/patients/${id}", PatientDTO::class.java)

    fun savePatient(patientDTO: PatientDTO) = post("/patients", patientDTO, Unit::class.java)

    fun deletePatient(id: Long) = delete("/patients/${id}")

    fun updatePatient(id: Long, patientDTO: PatientDTO) = put("/patients/${id}", patientDTO, Unit::class.java)

    fun <T> get(url: String, responseType: Class<T>): ResponseEntity<T> {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType)
    }

    private fun delete(url: String): ResponseEntity<Unit> = restTemplate.exchange(url, HttpMethod.DELETE, null, Unit::class.java)

    private fun <T, R> patch(url: String, data: T, responseType: Class<R>): ResponseEntity<R>? {

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.valueOf("application/json-patch+json")
        val httpEntity = HttpEntity(data, httpHeaders)
        return restTemplate.exchange(url, HttpMethod.PATCH, httpEntity, responseType)
    }

    private fun <T, R> put(url: String, data: T, responseType: Class<R>): ResponseEntity<R>? {
        return restTemplate.exchange(url, HttpMethod.PUT, HttpEntity(data), responseType)
    }

    private fun <T, R> post(url: String, data: T, responseType: Class<R>): ResponseEntity<R>? {
        return restTemplate.exchange(url, HttpMethod.POST, HttpEntity(data), responseType)
    }
}
