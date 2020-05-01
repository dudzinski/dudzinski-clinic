package integration

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import pl.dudzinski.clinic.handler.PatientDTO

class ClinicInvoker(private val restTemplate: RestTemplate) {

    fun getAllPatients(): ResponseEntity<Array<PatientDTO>> {
        return get("/patients/", responseType = Array<PatientDTO>::class.java)
    }

    fun getPatient(id: Long): ResponseEntity<PatientDTO> = get("/patients/${id}", PatientDTO::class.java)

    fun savePatient(patientDTO: PatientDTO) = post("/patients", patientDTO, PatientDTO::class.java)

    fun deletePatient(id : Long) = delete("/patients/${id}")

    fun updatePatient(id : Long, patientDTO: PatientDTO) = put("/patients/${id}", patientDTO)

    fun <T> get(url: String, responseType: Class<T>): ResponseEntity<T> {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType)
    }

    private fun delete(url: String) : ResponseEntity<Unit> = restTemplate.exchange(url, HttpMethod.DELETE, null, Unit::class.java)

    private fun <T> put(url: String, data: T) {
        restTemplate.put(url, HttpEntity(data))
    }

    private fun <T, R> post(url: String, data: T, responseType: Class<R>): ResponseEntity<R>? {
        return restTemplate.exchange(url, HttpMethod.POST, HttpEntity(data), responseType)
    }

}
