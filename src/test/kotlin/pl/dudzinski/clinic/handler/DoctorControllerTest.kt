package pl.dudzinski.clinic.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pl.dudzinski.clinic.AbstractSpringIntegrationTest

internal class DoctorControllerTest : AbstractSpringIntegrationTest() {

    @Test
    fun `save new doctor without address`() {
        val doctorDTO = DoctorDTO("Tomasz", "Barańczuk","Dentysta")

        val responseEntity = invoker.saveDoctor(doctorDTO)
        assertEquals(HttpStatus.CREATED, responseEntity!!.statusCode)
    }

    @Test
    fun `save three doctors and get them all`() {
        assertEquals(HttpStatus.CREATED, invoker.saveDoctor(DoctorDTO("Bronisław", "Włodarski", "Chirurg"))!!.statusCode)
        assertEquals(HttpStatus.CREATED, invoker.saveDoctor(DoctorDTO("Waldemar", "Dudkiewicz", "Kardiolog"))!!.statusCode)
        assertEquals(HttpStatus.CREATED, invoker.saveDoctor(DoctorDTO("Tomasz", "Barańczuk", "Dentysta"))!!.statusCode)

        val responseEntity = invoker.getAllDoctors()
        assertEquals(HttpStatus.OK, responseEntity.statusCode)

        val allDoctors = responseEntity.body
        assertNotNull(allDoctors)
        assertEquals(3, allDoctors?.size)
    }

    @Test
    fun `save one doctor and delete him`() {
        val responseEntity = invoker.saveDoctor(DoctorDTO("Bronisław", "Włodarski", "Chirurg"))
        assertEquals(HttpStatus.CREATED, responseEntity?.statusCode)

        val responseEntityBeforeDelete = invoker.getAllDoctors()
        assertEquals(HttpStatus.OK, responseEntityBeforeDelete.statusCode)
        val allDoctorsBeforeDelete = invoker.getAllDoctors().body
        assertEquals(1, allDoctorsBeforeDelete?.size)

        val deleteDoctorResponseEntity = invoker.deleteDoctor(allDoctorsBeforeDelete?.first()?.id!!)
        assertEquals(HttpStatus.OK, deleteDoctorResponseEntity.statusCode)

        val responseEntityAfterDelete = invoker.getAllDoctors()
        assertEquals(HttpStatus.OK, responseEntityAfterDelete.statusCode)
        assertEquals(0, responseEntityAfterDelete.body?.size)
    }

    @Test
    fun `save one doctor and update him`() {
        val responseEntity = invoker.saveDoctor(DoctorDTO("Bronisław", "Włodarski", "Chirurg"))
        assertEquals(HttpStatus.CREATED, responseEntity?.statusCode)

        val resourceURI = hostWithPort + responseEntity?.headers?.location.toString();

        val savedDoctorEntityResponse =
                invoker.get(resourceURI, DoctorDTO::class.java)
        assertEquals(HttpStatus.OK, savedDoctorEntityResponse.statusCode)

        val doctor = savedDoctorEntityResponse.body
        invoker.updateDoctor(doctor?.id!!, DoctorDTO("Marek","Dobrzycki","Ortodonta"))

        val updatedDoctorEntiyResponse =
                invoker.get(resourceURI, DoctorDTO::class.java)
        assertEquals(HttpStatus.OK, updatedDoctorEntiyResponse.statusCode)
        val updatedDoctor = updatedDoctorEntiyResponse.body

        assertEquals("Marek",updatedDoctor?.name)
        assertEquals("Dobrzycki",updatedDoctor?.surname)
        assertEquals("Ortodonta",updatedDoctor?.specialization)
    }
}


