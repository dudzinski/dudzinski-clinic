package pl.dudzinski.clinic.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pl.dudzinski.clinic.AbstractSpringIntegrationTest
import pl.dudzinski.clinic.assertDoctor
import pl.dudzinski.clinic.assertDoctors

internal class DoctorControllerTest : AbstractSpringIntegrationTest() {

    @Test
    fun `get doctor which is not exist`() {
        assertResourceNotFound { invoker.getDoctor(33L) }
    }

    @Test
    fun `delete doctor which is not exist`() {
        assertResourceNotFound { invoker.deleteDoctor(33L) }
    }

    @Test
    fun `update doctor which is not exist`() {
        val doctor = DoctorDTO("Franek", "Barańczuk", "Dermatolog")
        assertEquals(HttpStatus.CREATED, invoker.updateDoctor(33L, doctor)?.statusCode)
    }

    @Test
    fun `save new doctor and get him by id`() {
        val doctor = DoctorDTO("Tomasz", "Barańczuk", "Dentysta")

        val responseEntity = invoker.saveDoctor(doctor)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)

        val resourceURI = hostWithPort + responseEntity.headers.location.toString()
        val savedDoctorEntityResponse = invoker.get(resourceURI, DoctorDTO::class.java)
        assertDoctor(doctor, savedDoctorEntityResponse.body)
    }

    @Test
    fun `save three doctors and get them all`() {
        val doctors = listOf(
                DoctorDTO("Bronisław", "Włodarski", "Chirurg"),
                DoctorDTO("Waldemar", "Dudkiewicz", "Kardiolog"),
                DoctorDTO("Tomasz", "Barańczuk", "Dentysta")
        )
        doctors.forEach { assertEquals(HttpStatus.CREATED, invoker.saveDoctor(it)!!.statusCode) }

        val responseEntity = invoker.getAllDoctors()
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertDoctors(doctors, responseEntity.body?.toList())
    }

    @Test
    fun `save one doctor and delete him`() {
        val doctor = DoctorDTO("Tomasz", "Barańczuk", "Dentysta")

        val responseEntity = invoker.saveDoctor(doctor)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)

        val resourceURI = hostWithPort + responseEntity.headers.location.toString()
        val getDoctorEntityResponse = invoker.get(resourceURI, DoctorDTO::class.java)
        assertDoctor(doctor, getDoctorEntityResponse.body)

        val doctorId = getDoctorEntityResponse.body?.id!!
        val deleteDoctorResponseEntity = invoker.deleteDoctor(doctorId)

        assertEquals(HttpStatus.OK, deleteDoctorResponseEntity.statusCode)
        assertResourceNotFound { invoker.getDoctor(doctorId) }
    }

    @Test
    fun `save one doctor and update him`() {
        val doctor = DoctorDTO("Tomasz", "Barańczuk", "Dentysta")
        val responseEntity = invoker.saveDoctor(doctor)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)

        val resourceURI = hostWithPort + responseEntity.headers.location.toString()
        val getDoctorEntityResponse = invoker.get(resourceURI, DoctorDTO::class.java)
        assertDoctor(doctor, getDoctorEntityResponse.body)

        val doctorId = getDoctorEntityResponse.body?.id!!
        val doctorWithNewData = DoctorDTO("Marek", "Dobrzycki", "Ortodonta")
        invoker.updateDoctor(doctorId, doctorWithNewData)

        val updateDoctorEntityResponse = invoker.get(resourceURI, DoctorDTO::class.java)
        assertEquals(HttpStatus.OK, updateDoctorEntityResponse.statusCode)
        assertDoctor(doctorWithNewData, updateDoctorEntityResponse.body)
    }
}


